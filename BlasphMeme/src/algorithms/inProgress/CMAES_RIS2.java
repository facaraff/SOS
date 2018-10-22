/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/
package algorithms.inProgress;

import static utils.algorithms.operators.DEOp.crossOverExp;
//import static utils.algorithms.operators.MemesLibrary.ThreeSome_ShortDistance;
import static utils.algorithms.operators.MemesLibrary.intermediatePerturbation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import static utils.algorithms.Misc.Cov;
import static utils.algorithms.Misc.cloneArray;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;

import utils.MatLab;
import utils.algorithms.Misc;
import utils.algorithms.cmaes.CMAEvolutionStrategy;
import interfaces.Algorithm;
import interfaces.Problem;

import static utils.MatLab.indexMin;
import static utils.MatLab.multiply;
import static utils.MatLab.subtract;
import static utils.MatLab.sum;
import static utils.MatLab.transpose;
import static utils.RunAndStore.FTrend;

/*
 * Covariance Matrix Adaptation Evolutionary Strategy 
 */
public class CMAES_RIS2 extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		double[] best = new double[problemDimension];
		if (initialSolution != null)
			best = initialSolution;
		else
			best = generateRandomSolution(bounds, problemDimension);
		double fBest = Double.NaN;

		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		//cma.parameters.setPopulationSize(10);
		cma.setRepairMatrix(true);				// repair ill-conditioned matrix
		cma.setDimension(problemDimension);		// overwrite some loaded properties
		cma.setInitialX(best);					// in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.2);	// also a mandatory setting 
		cma.options.verbosity = -1;
		cma.options.writeDisplayToFile = -1;
		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();
		
		//System.out.println(problemDimension);
		//System.out.println(cma.getDataC());
		
		// iteration loop
		int j = 0; 
		int budget = 3*maxEvaluations/10;
		while (j < budget)
		{
			// get a new population of solutions
			double[][] pop = cma.samplePopulation();
			
			for(int i = 0; i < pop.length && j < maxEvaluations; ++i)
			{ 
				// saturate solution inside bounds 
				pop[i] = toro(pop[i], bounds);
				
				// compute fitness/objective value	
				fitness[i] = problem.f(pop[i]);
				
				// save best
				if (j == 0 || fitness[i] < fBest)
				{
					fBest = fitness[i];
					for (int n = 0; n < problemDimension; n++)
						best[n] = pop[i][n];
					FT.add(j, fBest);
				}
				
				j++;
			}

			// pass fitness array to update search distribution
			cma.updateDistribution(fitness);
		}
		
		
		double globalAlpha = getParameter("p0").doubleValue(); // 0.5
//		double radius = getParameter("p1").doubleValue(); // 0.2
		double precision = getParameter("p1").doubleValue(); // 0.0000001
		double CR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
//		int deepLSSteps = getParameter("p2").intValue();  //150;
		double deepLSRadius = getParameter("p3").doubleValue();//0.4;
		int samplesNr = getParameter("p4").intValue();//30;
		double samplingRadius = getParameter("P5").doubleValue();//0.1
			
		double[] temp;
		
		int i = 0;
		double[] x = new double[problemDimension];
		for(int k=0; k < problemDimension; k++)
		  x[k] = best[k];
		
		while (i < maxEvaluations)
		{
			if(i==0)
			{
				i = j;
				//for(int n=0;n<problemDimension;n++)
					//x[n] = best[n];
			}
			else
			{
				temp = generateRandomSolution(bounds, problemDimension);
				x = crossOverExp(best, temp, CR);
			}
			double fx = problem.f(x); i++;
						
			if(fx < fBest)
			{
				fBest = fx;
				for(int n=0;n<problemDimension;n++)
					best[n] = x[n];
				FT.add(i, fBest);
			}
					
			
			double[] SR = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
				SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;
			
			boolean improve = true;
	
			
			int popSize = samplesNr/2;
			double[][] samples = new double[samplesNr][problemDimension]; 
			double[] samplesFitnesses = new double[samplesNr];
			double[][] population = new double[popSize][problemDimension]; 
			

			//Sampling
			for(int k=0; k<samplesNr && i<maxEvaluations;k++)
			{
				samples[k] = intermediatePerturbation(bounds, best, samplingRadius);
				samplesFitnesses[k] = problem.f(samples[k]);
				i++;
				if (samplesFitnesses[k] < fBest)
				{
					fBest = samplesFitnesses[k];
					for (int n = 0; n < problemDimension; n++)
						best[n] = samples[k][n];
				}
			}
			
			//extracting the population
			for(int k=0;k<popSize;k++)
			{
				int minIndex = indexMin(samplesFitnesses);
				samplesFitnesses[minIndex] = Double.MAX_VALUE;
				population[k] = samples[minIndex];
			}
			
			//generate the P matrix and free memory
			EigenDecomposition E =  new EigenDecomposition(new Array2DRowRealMatrix(Cov(population)));
			population = null;
			samples = null;
			samplesFitnesses = null;
			double[][] P = E.getV().getData();
			E = null;
			
//			System.out.println("P "+P.length);
//			System.out.println("SR "+SR.length);
			//scale P columns with the corresponding perturbation radius
			double[][] R = scale(P,SR);
			
			//transpose R so that rows can be selected to act as perturbation vectors
			R = transpose(R);

			//Execute S along rotated axes
			while ((MatLab.max(SR) > precision) && (j < maxEvaluations))
			{
				double[] Xk = new double[problemDimension];
				double[] Xk_orig = new double[problemDimension];
				for (int k = 0; k < problemDimension; k++)
				{
					Xk[k] = best[k];//temp[k];
					Xk_orig[k] = best[k];//temp[k];
				}

				if (!improve) //@fabio this can be done on each dimension 
				{
					for(int k=0;k<problemDimension;k++)
						half(R,k);
				}
				
				improve = false;
				int k = 0;
				while ((k < problemDimension) && (j < maxEvaluations))
				{
					Xk = subtract(Xk,R[k]);
					Xk = Misc.toro(Xk, bounds);
					double fXk = problem.f(Xk);
					j++;
					// FT update
					if (fXk < fBest) //< or <= ?????????
					{
						fBest = fXk;
//						for (int n = 0; n < problemDimension; n++)
//							best[n] = Xk[n];
						best = cloneArray(Xk);
						Xk_orig = cloneArray(Xk);
						
						improve = true;
					}
					else if(j<maxEvaluations)
					{
						Xk = cloneArray(Xk_orig);
						Xk = sum(Xk,multiply(0.5, R[k]));
						Xk = Misc.toro(Xk, bounds);
						fXk = problem.f(Xk);
						j++;
						// FT update
						if (fXk < fBest)
						{
							fBest = fXk;
							best = cloneArray(Xk);
							Xk_orig = cloneArray(Xk);
							improve = true;
						}
						else
							Xk = cloneArray(Xk_orig);
					}
					
					k++;
				}
				
			}

		}
	
		finalBest = best;

		FT.add(i, fBest);
		
		return FT;
	}
	
	protected double[][] scale(double[][] P, double[] SR) 
	{
		double[][] R = new double[SR.length][SR.length];
		
		for(int c=0;c<SR.length;c++)
			for(int r=0;r<SR.length; r++)
				R[r][c] = P[r][c]*SR[c];
		
		return R;
		
	}
	
	protected void half(double[][] PT, int k) 
	{
		for(int c=0;c<PT.length;c++)
			PT[k][c] = PT[k][c]/2;
	}
	
	
	
	
}
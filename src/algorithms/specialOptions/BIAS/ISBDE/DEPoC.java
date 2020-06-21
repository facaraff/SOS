/**
Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package algorithms.specialOptions.BIAS.ISBDE;

import utils.random.RandUtilsISB;

import static utils.algorithms.Misc.generateRandomSolution;

import static utils.algorithms.operators.ISBOp.currentToRand1;
import static utils.algorithms.operators.ISBOp.currentToBest1;
import static utils.algorithms.operators.ISBOp.rand1;
import static utils.algorithms.operators.ISBOp.rand2;
import static utils.algorithms.operators.ISBOp.best1;
import static utils.algorithms.operators.ISBOp.best2;
import static utils.algorithms.operators.ISBOp.randToBest2;
import static utils.algorithms.operators.ISBOp.crossOverExp;
import static utils.algorithms.operators.ISBOp.crossOverBin;
import static utils.algorithms.ISBHelper.getNuberOfNonPositionColumnsForDE;

import static utils.MatLab.cloneArray;

import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.MatLab;
import utils.RunAndStore.FTrend;
import utils.algorithms.Counter;

/**
 * @author Fabio Caraffini (fabio.caraffini@gmail.com)
 * @link www.tinyurl.com/FabioCaraffini
 * 
 * Differential Evolution (all established variants)
 * 
 */

public class DEPoC extends AlgorithmBias
{
	protected String mutationStrategy = null;
	protected char crossoverStrategy = 'X';
	protected boolean addBestDetails = false;
	
	public DEPoC(String mut) {this.mutationStrategy = mut; this.nonPositionColumns = getNuberOfNonPositionColumnsForDE(mut);}
	
	public DEPoC(String mut, char xover)
	{
		this.mutationStrategy = mut;
		
		if(!mut.equals("ctro"))
			this.crossoverStrategy = xover;
		
		this.nonPositionColumns = getNuberOfNonPositionColumnsForDE(mut);
	}
	
	public DEPoC(String mut, char xover, boolean bestDetails)
	{
		this.mutationStrategy = mut;
		
		if(!mut.equals("ctro"))
			this.crossoverStrategy = xover;
		
		this.nonPositionColumns = getNuberOfNonPositionColumnsForDE(mut);
		this.addBestDetails = bestDetails;
	}
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue(); 
		double F = getParameter("p1").doubleValue();
		double CR = getParameter("p2").doubleValue();
		double alpha = getParameter("p3").doubleValue();

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		if(CR==-1)
			CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
		
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		
		int period = maxEvaluations/3;
		this.numberOfCorrections1 = this.numberOfCorrections2 = this.numberOfCorrections = 0;

		
		String FullName = getFullName("DE"+this.mutationStrategy+this.crossoverStrategy+this.correction+"p"+populationSize,problem); 
		Counter PRNGCounter = new Counter(0);


	
		
		writeHeader("popSize "+populationSize+" F "+F+" CR "+CR+" alpha "+alpha, problem);
		
		
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			i++;

			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
					FT.add(i, fBest);
			}

			

		}

		// temp variables
		double[] currPt = cloneArray(population[0]); //new double[problemDimension];
		double[] newPt = cloneArray(currPt);//new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		

		

		// iterate
		while (i < maxEvaluations)
		{
		
			//double[][] newPop = new double[populationSize][problemDimension];
			
			double[][] temp = new double[populationSize][problemDimension];
			double[] temp2 = new double[populationSize];

			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				
				int r1, r2, r3, r4, r5;
				int indexBest = MatLab.indexMin(fitnesses);
				
				int[] r_no_best, r;
				
				if(this.mutationStrategy.equals("ctbo"))
				{
					r = null;
					r_no_best = new int[populationSize-1];
					for (int n = 0; n < populationSize-2; n++)
						if(n != indexBest && n !=j)
							r_no_best[n] = n;
					r_no_best= RandUtilsISB.randomPermutation(r_no_best, PRNGCounter); 
					
					r3 = r4 = r5 = -1;
					
					r1 = r_no_best[0];
					r2 = r_no_best[1];
				}
				else if(this.mutationStrategy.equals("bo") || this.mutationStrategy.equals("bt"))
				{
					r = null;
					r_no_best = new int[populationSize-1];
					for (int n = 0; n < populationSize-1; n++)
						if(n != indexBest)
							r_no_best[n] = n;
					r_no_best= RandUtilsISB.randomPermutation(r_no_best, PRNGCounter); 
					
					r5 = -1;
					
					r1 = r_no_best[0];
					r2 = r_no_best[1];
					r3 = r_no_best[2];
					r4 = r_no_best[2];
				}
				else
				{
					r_no_best = null;
					r = new int[populationSize];
					
					for (int n = 0; n < populationSize; n++)
						r[n] = n;
					
					r = RandUtilsISB.randomPermutation(r,PRNGCounter); 
					
					r1 = r[0];
					r2 = r[1];
					r3 = r[2];
					r4 = r[3];
					r5 = r[4];
				}			
				
				// mutation
				switch (mutationStrategy)
				{
					case "ro":
						// DE/rand/1
						newPt = rand1(population[r1], population[r2],  population[r3], F, PRNGCounter);
						break;
					case "ctbo":
						// DE/cur-to-best/1
						newPt = currentToBest1(currPt, population[indexBest], population[r1], population[r2], F, PRNGCounter);
						break;
					case "rt":
						// DE/rand/2
						newPt = rand2(population[r1], population[r2], population[r3], population[r4], population[r5],  F, PRNGCounter);
						break;
					case "ctro":
						// DE/current-to-rand/1
						crossPt = currentToRand1(population[r1], population[r2],  population[r3], currPt, F, PRNGCounter);
						break;
					case "rtbt":
						// DE/rand-to-best/2
						newPt = randToBest2(population[r1], population[r2], population[r3], population[r4], population[r5], population[indexBest], F, PRNGCounter);
						break;
					case "bo":
						// DE/best/1
						newPt = best1(population[indexBest], population[r1], population[r2], F, PRNGCounter);
						break;
					case "bt":
						// DE/best/2
						newPt =  best2(population[indexBest], population[r1], population[r2], population[r3], population[r4], F, PRNGCounter);
						break;
					default:
						break;
				}	

		
				// crossover
				if (!mutationStrategy.equals("ctro"))
				{
					if (crossoverStrategy == 'b')
						crossPt = crossOverBin(currPt, newPt, CR, PRNGCounter);
					else if (crossoverStrategy == 'e')
						crossPt = crossOverExp(currPt, newPt, CR, PRNGCounter);
					else if (crossoverStrategy == 'x')
						crossPt = newPt;
				}
				
		
				
				crossPt = correct(crossPt, currPt, bounds, PRNGCounter);
				storeNumberOfCorrectedSolutions(period,i);
				
				crossFit = problem.f(crossPt);
				i++; 


				
				// replacement
				if (crossFit < currFit)
				{
					
					for (int n = 0; n < problemDimension; n++)
						temp[j][n] = crossPt[n];

					fitnesses[j] = crossFit;
					
					temp2[j] = crossFit;
				
					
					
					
					// best update
					if (crossFit < fBest)
					{
						fBest = crossFit;
						for (int n = 0; n < problemDimension; n++)
							best[n] = crossPt[n];
						//if(i==problemDimension)	
						FT.add(i, fBest);
					}
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						temp[j][n] = currPt[n];
						//newPop[j][n] = currPt[n];
					fitnesses[j] = currFit;
					
				
					temp2[j] = currFit;
				}
				crossPt = null; newPt = null;
			}
			
			population = cloneArray(temp);
			temp=null;
			fitnesses = cloneArray(temp2);
			temp2=null;
			
			
		}
		
		closeAll();	
		
		String s = "";
		if(addBestDetails) s = positionAndFitnessToString(best, fBest);
		
		finalBest = best;
		FT.add(i, fBest);
		writeStats(FullName,  ((double)this.numberOfCorrections1/((double)period)),  ((double)this.numberOfCorrections2/((double)period*2)), (double) this.numberOfCorrections/maxEvaluations, PRNGCounter.getCounter(),s, "correctionsDE");
		return FT;
		
		
	}

}

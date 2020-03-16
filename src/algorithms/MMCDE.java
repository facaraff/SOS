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
package algorithms;

import  utils.algorithms.operators.DEOp;
import utils.random.RandUtils;

import static utils.algorithms.Misc.generateRandomSolution;

//import java.util.Vector; serve?

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/**
 * Mixed mutation and crossover strategies rotation invariant Differential Evolution with Gram-Shmidt process 
 * 
 * 
 * @author facaraff fabio.caraffini@gmail.com
 */
public class MMCDE extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue();
		double F = getParameter("p1").doubleValue();
		double CR = getParameter("p2").doubleValue();
		double alpha = getParameter("p3").doubleValue();
		double crbin = Double.NaN;
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		if(CR==-1) {
			CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
			crbin = alpha;
		}
		else
		{
			crbin = CR;
		}
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		boolean both = false;
		
		int i = 0;
		
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
					FT.add(i, fBest);
			}
			
			i++;
		}
		
		//orthonormal basis
		double[][] b=null;

		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		
		

		// iterate
		while (i < maxEvaluations)
		{
		
			b = DEOp.getBasis(population);
			

			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				int mutationStrategy= 1+RandUtils.randomInteger(3);
				switch (mutationStrategy)
				{
					case 1:
						// DE/rand/1
						newPt = DEOp.rand1(population, F);
						break;
					case 2:
						// DE/best/1
						newPt = DEOp.best1(population, fitnesses, F);
						break;
					case 3:
						// DE/rand-ti-best/1
						newPt = DEOp.randToBest1(population, fitnesses, F);
						break;
					case 4:
						// DE/best2
						newPt = DEOp.best2(population,fitnesses,F);
						break;
					default:
						break;
				}
				// crossover
				int crossoverStrategy= 1+RandUtils.randomInteger(1);
				switch (crossoverStrategy)
				{	case 1:
						// Rotation invariant binomial xo
						crossPt = DEOp.ribc(currPt, newPt, crbin,b);
						break;
					case 2:
						// Rotation invariant exponential xo
						crossPt = DEOp.riec(currPt, newPt, CR,b);
						break;
					default:
						break;
				}
						
				crossPt = correct(crossPt, bounds);
				crossFit = problem.f(crossPt);
				i++;


				// replacement
				if (crossFit < currFit)
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = crossPt[n];
					fitnesses[j] = crossFit;
					
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
					if(both)
					{
						if(crossoverStrategy==6)
							crossPt = DEOp.ribc(currPt, newPt, CR,b);	
						else if(crossoverStrategy==7)
							crossPt = DEOp.riec(currPt, newPt, CR,b);		
						// replacement
						if (crossFit < currFit)
						{
							for (int n = 0; n < problemDimension; n++)
								population[j][n] = crossPt[n];
							fitnesses[j] = crossFit;
							
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
					}
					
//					for (int n = 0; n < problemDimension; n++)
//						population[j][n] = currPt[n];
//					fitnesses[j] = currFit;
				}
				crossPt = null; newPt = null;
			}
			
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		return FT;
		
		
	}

}

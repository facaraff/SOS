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


import static utils.algorithms.CompactAlgorithms.generateIndividual;
import static utils.algorithms.CompactAlgorithms.scale;
import static utils.algorithms.CompactAlgorithms.updateMean;
import static utils.algorithms.CompactAlgorithms.updateSigma2;
import static utils.algorithms.Misc.toro;

//import java.io.FileWriter;
//import java.io.IOException;

import utils.MatLab;
//import utils.random.RandUtils;

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.random.RandUtils;

import static utils.algorithms.operators.CompactPerturbations.cdelight;
import static utils.algorithms.operators.CompactPerturbations.rand_one;
import static utils.algorithms.operators.CompactPerturbations.rand_one_randomF;
import static utils.algorithms.operators.CompactPerturbations.rand_to_best;
import static utils.algorithms.operators.CompactPerturbations.rand_to_best_two;
import static utils.algorithms.operators.CompactPerturbations.rand_two;

/*
 * compact multistrategy Differential Evolution 
 */
public class MScDE extends Algorithm
{

	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
	
		//COMMON PARAMTERS
		int virtualPopulationSize = this.getParameter("p0").intValue();//300
		
		//DE PARAMTERS
		double alpha = this.getParameter("p1").doubleValue();//0.25
		double F = this.getParameter("p2").doubleValue();//0.5
		int maxConsecutiveRep = this.getParameter("p3").intValue();
		boolean isPersistent = this.getParameter("p4").intValue()!=0;//true
		int eta = virtualPopulationSize*2/3;
			
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		double[] normalizedBounds = {-1.0, 1.0};
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		int teta = 0;

		double[] mean = new double[problemDimension];
		double[] sigma2 = new double[problemDimension];
		for (int j = 0; j < problemDimension; j++)
		{
			mean[j] = 0.0;
			sigma2[j] = 1.0;
		}
		
		double[] xc = new double[problemDimension];
		for (int n = 0; n < problemDimension; n++)
			xc[n] = (bounds[n][1]+bounds[n][0])/2;
		
		// evaluate initial solutions
		
		double[] b = new double[problemDimension];
		double[] aScaled = new double[problemDimension];
		double[] bScaled = new double[problemDimension];
		double fB = Double.NaN;

		if (initialSolution != null)
		{
			for (int n = 0; n < problemDimension; n++)
				best[n] = initialSolution[n];
		    fBest = initialFitness;
		}
		else
		{
			double[] a = new double[problemDimension];
			
			a = generateIndividual(mean, sigma2);
			b = generateIndividual(mean, sigma2);
			aScaled = scale(a, bounds, xc);
			bScaled = scale(b, bounds, xc);

			double fA = problem.f(aScaled);
			fB = problem.f(bScaled);
			if (fA < fB)
			{
				fBest = fA;
				for (int n = 0; n < problemDimension; n++)
					best[n] = a[n];
					FT.add(0, fA);
			}
			else
			{
				fBest = fB;
				for (int n = 0; n < problemDimension; n++)
					best[n] = b[n];
					FT.add(0,fB);
			}
			i += 2;
		}

		

		double[] winner = new double[problemDimension];
		double[] loser = new double[problemDimension];
		

		

		int mutationStrategy = -1;
		int crossoverStrategy = -1;
		
		// iterate
		while (i < maxEvaluations)
		{

			mutationStrategy  = RandUtils.randomInteger(5)+1;
			if(mutationStrategy>1) crossoverStrategy = RandUtils.randomInteger(1)+1;
			
			int k = 0;
			while(k<maxConsecutiveRep && i<maxEvaluations)
			{
				// mutation
				switch (mutationStrategy)
				{
					case 1:
						
						b = cdelight(F, alpha, best, mean, sigma2);
						break;
					case 2:
						b = rand_one_randomF(alpha, crossoverStrategy,  best,  mean,  sigma2);
						break;
					case 3:
						b = rand_one(F, alpha, crossoverStrategy, best,  mean, sigma2);
						break;
					case 4:
						b = rand_to_best( F, alpha, crossoverStrategy, best, mean, sigma2);
						break;
					case 5:
						b = rand_two(F, alpha,  crossoverStrategy, best,  mean, sigma2);
					case 6:
						b = rand_to_best_two(F, alpha, crossoverStrategy, best, mean,  sigma2);
					default:
						break;
						
				}

						b = toro(b, normalizedBounds);
						bScaled = scale(b, bounds, xc);
						fB = problem.f(bScaled);
						i++;
						k++;

						if (fB < fBest)
						{
							for (int n = 0; n < problemDimension; n++)
							{
								winner[n] = b[n];
								loser[n] = best[n];
							}
							fBest = fB;

							if (isPersistent)
								// log best fitness (persistent elitism)
								FT.add(i, fBest);
								//bests.add(new Best(i, fBest));
							else
							{
								// log best fitness (non persistent elitism)
								if (fBest < FT.getF(FT.size()-1))
									FT.add(i, fBest);
									//bests.add(new Best(i, fBest));
							}
						}
						else
						{
							for (int n = 0; n < problemDimension; n++)
							{
								winner[n] = best[n];
								loser[n] = b[n];
							}
						}
						
						if (!isPersistent)
						{
							if ((teta < eta) && (MatLab.isEqual(winner, best)))
								teta++;
							else
							{
								teta = 0;
								for (int n = 0; n < problemDimension; n++)
									winner[n] = b[n];
								fBest = fB;
							}
						}
						
						for (int n = 0; n < problemDimension; n++)
							best[n] = winner[n];

						// best and mean/sigma2 update
						mean = updateMean(winner, loser, mean, virtualPopulationSize);
						sigma2 = updateSigma2(winner, loser, mean, sigma2, virtualPopulationSize);	
						
						}

				}
			
	
		
		if (isPersistent)
			// log best fitness (persistent elitism)
			FT.add(i, fBest);
		else
		{
			// log best fitness (non persistent elitism)
			double lastFBest = FT.getF(FT.size()-1); 
			if (fBest < lastFBest)
				FT.add(i, fBest);
			else
				FT.add(i, fBest);
		}
		
	
		finalBest = best;
		
		FT.add(i, fBest);
		return FT;

	}
	
}

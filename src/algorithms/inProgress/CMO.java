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

//import utils.MatLab;

import interfaces.Algorithm;
import interfaces.Problem;
import utils.MatLab;
import utils.RunAndStore.FTrend;
import utils.random.RandUtils;

import static utils.algorithms.operators.CompactPerturbations.cdelight;


//import static utils.algorithms.operators.CompactPerturbations.cPSOPerturbation;

/*
 * compact multistrategy Differential Evolution 
 */
public class CMO extends Algorithm
{

	double[] mean;
	double[] sigma2;
	
	double[] winner;
	double[] loser;
	
	double[] best;
	double fBest = Double.NaN;
	
	int i = 0;
	int k;
	double[] normalizedBounds = {-1.0, 1.0};
	FTrend FT = new FTrend();
	
	//BFO
	int t = 0;
	double C_i; 
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations)  throws Exception
	{
		
		int	 problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
	
		//COMMON PARAMTERS
		int virtualPopulationSize = this.getParameter("p0").intValue();//300
		
		// PARAMTERS
		//cDElight
		double alpha = this.getParameter("p1").doubleValue();//0.25
		double F = this.getParameter("p2").doubleValue();//0.5
		

		//cBFO
//		int virtualPopulationSizeBFO = this.getParameter("p0").intValue();		// number of bacteria 300
		double C_initial = this.getParameter("p3").doubleValue();				// chemotactic step size 0.1
		int Ns = this.getParameter("p4").intValue();							// swim steps 4
		
		// Adaptive BFO (ABFO0) parameters
		double epsilon_initial = this.getParameter("p5").doubleValue();		// initial epsilon 1
		int ng = this.getParameter("p6").intValue();							// number of generations for adaptation 10
		double alfa = this.getParameter("p7").doubleValue();					// C_i reduction ratio 2
		double beta = this.getParameter("p8").doubleValue();					// epsilon reduction ratio 2
		boolean enableAdaptation0 = this.getParameter("p9").intValue()!=0;
		C_i =C_initial;
		
		int maxConsecutiveRep = this.getParameter("p10").intValue();
		maxConsecutiveRep = maxConsecutiveRep*problemDimension;
//		//cPSO
//		int virtualPopulationSizePSO = getParameter("p0").intValue(); //50
//		double phi1 = getParameter("p1").doubleValue(); // -0.2
//		double phi2 = getParameter("p2").doubleValue(); // -0.07
//		double phi3 = getParameter("p3").doubleValue(); // 3.74
//		double gamma1 = getParameter("p4").doubleValue(); // 1.0
//		double gamma2 = getParameter("p5").doubleValue(); // 1.0
		
		


		mean = new double[problemDimension];
		sigma2 = new double[problemDimension];
		best = new double[problemDimension];
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

		
		best = new double[problemDimension];
		winner = new double[problemDimension];
		loser = new double[problemDimension];
		

		//to save memory, b is re-used instead of x as position for cPSO while velocity ha to be initialised here
		//double[] x = generateIndividual(mean, sigma2);
		double[] v = generateIndividual(mean, sigma2);
		for (int n = 0; n < problemDimension; n++)
			v[n] = 0.1*v[n];
		
		
		
		
		
		
		// iterate
		while (i < maxEvaluations)
		{
			int perturbation = 1;//RandUtils.randomInteger(1)+1;
			
//			 int k = 0;
			//boolean improved = true;
			//while(improved && k<maxConsecutiveRep && i<maxEvaluations)
			//while(k<maxConsecutiveRep && i<maxEvaluations)
			{
				// mutation
				switch (perturbation)
				{
					case 1:
						cDElight(F, alpha, virtualPopulationSize, problem, bounds,  xc);
						break;
					case 2:
						cBFO(C_initial, Ns, epsilon_initial, ng, alfa, beta, virtualPopulationSize, enableAdaptation0, problem, bounds, xc,  maxEvaluations);				
						break;
//					case 3:
//						//cGA?
//						break;
//					case 4:
//						cPSO( phi1, phi2, phi3, gamma1, gamma2, b, v, virtualPopulationSizePSO, problem, bounds, xc);	
					default:
						break;
				}
						
			}
		}
	
		finalBest = best;
		
		FT.add(i, fBest);
		return FT;
	}
	
	
	
	
	
	private void cDElight(double F, double alpha, int virtualPopulationSize, Problem problem, double[][] bounds, double[] xc) throws Exception
	{
		double[] b = cdelight(F, alpha, best, mean, sigma2);
		
		b = toro(b, normalizedBounds);
		double fB = problem.f(scale(b, bounds, xc));
		i++;
		k++;
		int problemDimension = problem.getDimension();
		if(fB < fBest)
		{
			for (int n = 0; n < problemDimension; n++)
			{
				winner[n] = b[n];
				loser[n] = best[n];
			}
			fBest = fB;

			if (i % problemDimension == 0) FT.add(i, fBest);

		}
		else
		{
			for (int n = 0; n < problemDimension; n++)
			{
				winner[n] = best[n];
				loser[n] = b[n];
			}
		}
		
		
		for (int n = 0; n < problemDimension; n++)
			best[n] = winner[n];

		// best and mean/sigma2 update
		mean = updateMean(winner, loser, mean, virtualPopulationSize);
		sigma2 = updateSigma2(winner, loser, mean, sigma2, virtualPopulationSize);	
	}
	
//	private void cPSO(double phi1, double phi2, double phi3, double gamma1, double gamma2, double[] x, double[] v, int virtualPopulationSizePSO, Problem problem, double[][] bounds, double[] xc) throws Exception
//	{
////		 cPSOPerturbation(v, x, x_gb, mean, sigma2, phi1, phi2, phi3, gamma1, gamma2);
//		double[] x_lb = cPSOPerturbation(v, x, best, mean, sigma2, phi1, phi2, phi3, gamma1, gamma2);
//		
//		int problemDimension = problem.getDimension();
//		x = toro(x, normalizedBounds);
//		double[] xScaled = scale(x, bounds, xc);
//		double fitness_x = problem.f(xScaled);
//		
//		double[] x_lbScaled = scale(x_lb, bounds, xc);
//		double fitness_lb = problem.f(x_lbScaled);
//		
//		i += 2;
//		k+=2;
//
//		if (fitness_lb < fitness_x)
//		{
//			for (int n = 0; n < problemDimension; n++)
//			{
//				winner[n] = x_lb[n];
//				loser[n] = x[n];
//			}
//			
//			if (fitness_lb < fBest)
//			{
//				for (int n = 0; n < problemDimension; n++)
//					best[n] = x_lb[n];
//				fBest = fitness_lb;
//			}
//		}
//		else
//		{
//			for (int n = 0; n < problemDimension; n++)
//			{
//				winner[n] = x[n];
//				loser[n] = x_lb[n];
//			}
//			
//			if (fitness_x < fBest)
//			{
//				for (int n = 0; n < problemDimension; n++)
//					best[n] = x[n];
//				fBest = fitness_x;
//			}
//		}
//		
//		if (i % problemDimension == 0)
//			FT.add(i, fBest);
//
//		// best and mean/sigma2 update
//		mean = updateMean(winner, loser, mean, virtualPopulationSizePSO);
//		sigma2 = updateSigma2(winner, loser, mean, sigma2, virtualPopulationSizePSO);	
//	}


	
	private void cBFO(double C_initial, int Ns, double epsilon_initial, int ng, double alfa, double beta, int virtualPopulationSizeBFO, boolean enableAdaptation0, Problem problem, double[][] bounds, double[] xc, int maxEvaluations) throws Exception
	{
		double epsilon_i = epsilon_initial;
		double fBest_bak = fBest;
		
		/*
		 * chemotaxis (iterate on bacteria)
		 */
		for (int ii = 0; ii < virtualPopulationSizeBFO && i < maxEvaluations; ii++)
		{
			//if ((i % 10000) == 0)
			//	System.out.println(i);
			
			//a = generateIndividual(mean, sigma2, random);
			double[] a = mean;
			double[] aScaled = scale(a, bounds, xc);
			double fA = problem.f(aScaled);
			
			i++;
			k++;
			int problemDimension = problem.getDimension();

			if (fA < fBest)
			{
				for (int n = 0; n < problemDimension; n++)
				{
					winner[n] = a[n];
					loser[n] = best[n];
				}	
			}
			else
			{
				for (int n = 0; n < problemDimension; n++)
				{
					winner[n] = best[n];
					loser[n] = a[n];
				}
			}
			
			mean = updateMean(winner, loser, mean, virtualPopulationSizeBFO);
			sigma2 = updateSigma2(winner, loser, mean, sigma2, virtualPopulationSizeBFO);

			// update best
			if (fA < fBest)
			{
				fBest = fA;
				for (int n = 0; n < problemDimension; n++)
					best[n] = a[n];
				FT.add(i, fBest);
			}
			
			if (i >= maxEvaluations)
				break;
			
			double J_last = fA;
			
			double[] delta = new double[problemDimension];
			
			// evaluate chemotactic direction vector
			for (int n = 0; n < problemDimension; n++)
				delta[n] = -1.0 + 2*RandUtils.random();

			// chemotactic direction vector norm
			double stepNorm = MatLab.norm2(delta);
			
			// tumble and move
			for (int n = 0; n < problemDimension; n++)
				a[n] = a[n] + C_i * delta[n]/stepNorm;

			// bacteria health (fitness)
			a = toro(a, normalizedBounds);
			aScaled = scale(a, bounds, xc);
			fA = problem.f(aScaled);
			
			i++;

			// swim
			for (int j = 0; j < Ns && i < maxEvaluations; j++)
			{
				if (fA < fBest)
				{
					for (int n = 0; n < problemDimension; n++)
					{
						winner[n] = a[n];
						loser[n] = best[n];
					}	
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
					{
						winner[n] = best[n];
						loser[n] = a[n];
					}
				}		
				
				mean = updateMean(winner, loser, mean, virtualPopulationSizeBFO);
				sigma2 = updateSigma2(winner, loser, mean, sigma2, virtualPopulationSizeBFO);
				
				// update best
				if (fA < fBest)
				{
					fBest = fA;
					for (int n = 0; n < problemDimension; n++)
						best[n] = a[n];
					FT.add(i, fBest);
				}
				
				// update best fitness value found so far 
				if (fA < J_last)
				{
					// move in the same direction
					J_last = fA;
					for (int n = 0; n < problemDimension; n++)
						a[n] = a[n] + C_i * delta[n]/stepNorm;
					
					// bacteria health (fitness)
					a = toro(a, normalizedBounds);
					aScaled = scale(a, bounds, xc);
					fA = problem.f(aScaled);
					
					i++;
				}
			}
		}
			
		/*
		 * reproduction ~ shift mean towards the best
		 */
		mean = updateMean(best, mean, mean, virtualPopulationSizeBFO);
		sigma2 = updateSigma2(best, mean, mean, sigma2, virtualPopulationSizeBFO);
		
		/*
		 * elimination-dispersal ~ inject new individuals into the population
		 */
		int problemDimension = problem.getDimension();
		for (int j = 0; j < problemDimension; j++)
		{
			mean[j]=mean[j]+(0.2*RandUtils.random()-0.1);
			if (mean[j] > 1)
				mean[j] = 1.0;
			else if (mean[j] < -1)
				mean[j] = -1.0;
			sigma2[j] = Math.abs(sigma2[j] + 0.1*RandUtils.random());
		}
		
		// apply adaptation scheme of ABFO0
		if (enableAdaptation0)
		{
			if ((t > 0) && (t % ng == 0))
			{
				if (fBest == fBest_bak)
				{
					C_i = C_initial;
					epsilon_i = epsilon_initial;
				}
				else if (Math.abs((fBest - fBest_bak)/fBest) < epsilon_i)
				{
					C_i = C_i/alfa;
					epsilon_i = epsilon_i/beta;
				}
			}
			
			if (t % ng == 0)
				fBest_bak = fBest;
		}
		
		t++;
	
	}
}

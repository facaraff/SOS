package algorithms.compact;

import static utils.algorithms.CompactAlgorithms.generateIndividual;
import static utils.algorithms.CompactAlgorithms.scale;
import static utils.algorithms.CompactAlgorithms.updateMean;
import static utils.algorithms.CompactAlgorithms.updateSigma2;

import utils.MatLab;
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * compact Bacterial Foraging Optimization
 */
public class cBFO extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int virtualPopulationSize = this.getParameter("p0").intValue();		// number of bacteria 300
		double C_initial = this.getParameter("p1").doubleValue();				// chemotactic step size 0.1
		int Ns = this.getParameter("p2").intValue();							// swim steps 4
		
		// Adaptive BFO (ABFO0) parameters
		double epsilon_initial = this.getParameter("p3").doubleValue();		// initial epsilon 1
		int ng = this.getParameter("p4").intValue();							// number of generations for adaptation 10
		double alfa = this.getParameter("p5").doubleValue();					// C_i reduction ratio 2
		double beta = this.getParameter("p6").doubleValue();					// epsilon reduction ratio 2

		// enable ABFO0 flag
		boolean enableAdaptation0 = this.getParameter("p2").intValue()!=0;//XXX qualche errore? dovrebbe eseere  p7?

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		double[] normalizedBounds = {-1.0, 1.0};

		int evalCount = 0;
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
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
		double[] a = new double[problemDimension];
		double[] b = new double[problemDimension];
		double[] aScaled = new double[problemDimension];
		double[] bScaled = new double[problemDimension];
		
		a = generateIndividual(mean, sigma2);
		b = generateIndividual(mean, sigma2);
		aScaled = scale(a, bounds, xc);
		bScaled = scale(b, bounds, xc);

		double fA = problem.f(aScaled);
		double fB = problem.f(bScaled);
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
			FT.add(0, fB);			
		}
		evalCount += 2;

		double[] winner = new double[problemDimension];
		double[] loser = new double[problemDimension];

		// tumble
		double[] delta = new double[problemDimension];
		
		// generation counter
		int t = 0;

		double C_i = C_initial;
		double epsilon_i = epsilon_initial;
		double fBest_bak = fBest;
		
		while (evalCount < maxEvaluations)
		{
			/*
			 * chemotaxis (iterate on bacteria)
			 */
			for (int i = 0; i < virtualPopulationSize && evalCount < maxEvaluations; i++)
			{
				//if ((evalCount % 10000) == 0)
				//	System.out.println(evalCount);
				
				//a = generateIndividual(mean, sigma2, random);
				a = mean;
				aScaled = scale(a, bounds, xc);
				fA = problem.f(aScaled);
				
				evalCount++;

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
				
				mean = updateMean(winner, loser, mean, virtualPopulationSize);
				sigma2 = updateSigma2(winner, loser, mean, sigma2, virtualPopulationSize);

				// update best
				if (fA < fBest)
				{
					fBest = fA;
					for (int n = 0; n < problemDimension; n++)
						best[n] = a[n];
					FT.add(evalCount, fBest);
				}
				
				if (evalCount >= maxEvaluations)
					break;
				
				double J_last = fA;
				
				// evaluate chemotactic direction vector
				for (int n = 0; n < problemDimension; n++)
					delta[n] = -1.0 + 2*RandUtils.random();

				// chemotactic direction vector norm
				double stepNorm = MatLab.norm2(delta);
				
				// tumble and move
				for (int n = 0; n < problemDimension; n++)
					a[n] = a[n] + C_i * delta[n]/stepNorm;

				// bacteria health (fitness)
				a = correct(a, normalizedBounds);
				aScaled = scale(a, bounds, xc);
				fA = problem.f(aScaled);
				
				evalCount++;

				// swim
				for (int j = 0; j < Ns && evalCount < maxEvaluations; j++)
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
					
					mean = updateMean(winner, loser, mean, virtualPopulationSize);
					sigma2 = updateSigma2(winner, loser, mean, sigma2, virtualPopulationSize);
					
					// update best
					if (fA < fBest)
					{
						fBest = fA;
						for (int n = 0; n < problemDimension; n++)
							best[n] = a[n];
						FT.add(evalCount, fBest);
					}
					
					// update best fitness value found so far 
					if (fA < J_last)
					{
						// move in the same direction
						J_last = fA;
						for (int n = 0; n < problemDimension; n++)
							a[n] = a[n] + C_i * delta[n]/stepNorm;
						
						// bacteria health (fitness)
						a = correct(a, normalizedBounds);
						aScaled = scale(a, bounds, xc);
						fA = problem.f(aScaled);
						
						evalCount++;
					}
				}
			}
				
			/*
			 * reproduction ~ shift mean towards the best
			 */
			mean = updateMean(best, mean, mean, virtualPopulationSize);
			sigma2 = updateSigma2(best, mean, mean, sigma2, virtualPopulationSize);
			
			/*
			 * elimination-dispersal ~ inject new individuals into the population
			 */
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
		
		finalBest = best;
		
		FT.add(evalCount, fBest);		
		return FT;
	}
}
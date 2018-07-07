package algorithms.inProgress;

//import static utils.algorithms..operators.Operators.DEO;
import static utils.algorithms.operators.DEOp.crossOverExpFast;
import static utils.algorithms.CompactAlgorithms.generateIndividual;
import static utils.algorithms.CompactAlgorithms.scale;
import static utils.algorithms.CompactAlgorithms.normalize;
import static utils.algorithms.CompactAlgorithms.updateMean;
import static utils.algorithms.CompactAlgorithms.updateSigma2;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;

//import utils.MatLab;
import utils.random.RandUtils;

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;


/*
 * compact Differential Evolution Light (with light exponential crossover and light mutation)
 */
public class ibrido extends Algorithm
{
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int virtualPopulationSize = this.getParameter("p0").intValue();//300
		double alpha = this.getParameter("p1").doubleValue();//0.25
		double F = this.getParameter("p2").doubleValue();//0.5
		int initialSolutions = this.getParameter("p3").intValue();//10
		double alphaTemp = this.getParameter("p4").doubleValue(); //0.9
		int Lk = this.getParameter("p5").intValue(); //3
		int B = this.getParameter("p6").intValue(); //5
//		boolean isPersistent = this.getParameter("p6").intValue()!=0;//true
//		int eta = virtualPopulationSize*2/3;
			
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		double[] normalizedBounds = {-1.0, 1.0};
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
//		int teta = 0;

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
				best[n] = initialSolution[n];//xxx(fabio) forgot normalization?
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
		//FT.add(0, fParticle);
		
		double[] winner = new double[problemDimension];
		double[] loser = new double[problemDimension];
		
		double[] sigma2_F = new double[problemDimension];
		
		
		
		double CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
		//double Fmod = (1+2*F);
		double Fmod = (1+2*Math.pow(F,2));

		
		//*********************************
	
		//double[] newPt = new double[problemDimension];
//		double[] oldPt = new double[problemDimension];
		
	
		double fOld;
		double fWorst;
		
		fOld = fBest;
		fWorst = fBest;
		

		
		// evaluate initial solutions to set the initial temperature
		for (int j = 0; j < initialSolutions; j++)
		{
			b = generateRandomSolution(bounds, problemDimension);
			fB = problem.f(b);

			// update best
			if (fB < fBest)
			{
				FT.add(j+i+1, fB);
				//for (int k = 0; k < problemDimension; k++)
					//best[k] = b[k];
				best = normalize(b,bounds,xc);
				fBest = fB;
			}
			
			// update worst
			if (fB > fWorst)
				fWorst = fB;
		}
		
//		for (int k = 0; k < problemDimension; k++)
//			oldPt[k] = best[k];
		
		// initialize temperature
		double delt0 = fWorst-fOld;
		double accept0 = 0.9;
		double T0 = -delt0/Math.log(accept0);		
		double tk = T0;

		i += initialSolutions;
	
		int  generationIndex = 1;
		int totalGenerations = (maxEvaluations-i)/Lk;
		
		// iterate
		while (i < maxEvaluations)
		{
			//if ((i % 10000) == 0)
			//	System.out.println(i);
			
			
			// DE/rand/1
			for (int n = 0; n < problemDimension; n++)
				sigma2_F[n] = Fmod*sigma2[n];
			b = generateIndividual(mean, sigma2_F);
			b = crossOverExpFast(best, b, CR);
			
			
			b = toro(b, normalizedBounds);
			bScaled = scale(b, bounds, xc);
			fB = problem.f(bScaled);
			
			i++;

			if (fB < fBest)
			{
				for (int n = 0; n < problemDimension; n++)
				{
					winner[n] = b[n];
					loser[n] = best[n];
				}
				generationIndex++;
				//fBest = fB;
				//FT.add(i, fBest);
			}
			else
			{
//				for (int n = 0; n < problemDimension; n++)
//				{
//					winner[n] = best[n];
//					loser[n] = b[n];
//				}
				
				
				for (int j = 0; j < Lk && i < maxEvaluations; j++)
				{
					// non-uniform mutation
					for (int k = 0; k < problemDimension; k++)
					{
						//double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)i/maxEvaluations, B));
						double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)generationIndex/totalGenerations, B));
						
						if (RandUtils.random()<0.5)
							bScaled[k] = best[k] - (best[k]-problem.getBounds()[k][0])*(1-temp);
						else
							bScaled[k] = best[k] + (problem.getBounds()[k][1]-best[k])*(1-temp);
					}
					
					// evaluate fitness
					bScaled = toro(bScaled, bounds);
					b = normalize(bScaled,bounds,xc);
					fB = problem.f(bScaled);
					i++;
					
					if (fB < fBest)
					{
						for (int n = 0; n < problemDimension; n++)
						{
							
							winner[n] = b[n];
							loser[n] = best[n];
						}
						//fBest = fB;
						//FT.add(i, fBest);
					}
					else
					{
						for (int n = 0; n < problemDimension; n++)
						{
							winner[n] = best[n];
							loser[n] = b[n];
						}
					}
					
				}
				
				generationIndex++;
				
				
				
				
				
			}
			
//			if (!isPersistent)
//			{
//				if ((teta < eta) && (MatLab.isEqual(winner, best)))
//					teta++;
//				else
//				{
//					teta = 0;
//					for (int n = 0; n < problemDimension; n++)
//						winner[n] = b[n];
//					//fBest = fB;
//				}
//			}
			
			
			
			
			// move to the neighbor point
			if ((fB <= fOld) || (Math.exp((fOld-fB)/tk) > RandUtils.random()))
			{
				for (int n = 0; n < problemDimension; n++)
					best[n] = winner[n];
				fBest = fB;
				fOld = fB;
			}
			
			if(generationIndex%Lk==0)
				tk = alphaTemp*tk;

			// best and mean/sigma2 update
			mean = updateMean(winner, loser, mean, virtualPopulationSize);
			sigma2 = updateSigma2(winner, loser, mean, sigma2, virtualPopulationSize);
		
		}
		

		finalBest = scale(best, bounds, xc);
		FT.add(i, fBest);
		return FT;
	}
	

}


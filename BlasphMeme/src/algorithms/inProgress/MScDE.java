package algorithms.inProgress;

//import static utils.algorithms..operators.Operators.DEO;
import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.operators.DEOp.crossOverExpFast;
import static utils.algorithms.operators.DEOp.currentToBest1;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.operators.DEOp.rand2;
import static utils.algorithms.operators.DEOp.randToBest2;
import static utils.algorithms.Misc.toro;

import static  utils.algorithms.operators.CompactAlgorithms.generateIndividual;
import static  utils.algorithms.operators.CompactAlgorithms.scale;
import static  utils.algorithms.operators.CompactAlgorithms.updateMean;
import static  utils.algorithms.operators.CompactAlgorithms.updateSigma2;

import java.io.FileWriter;
import java.io.IOException;

import utils.MatLab;
import utils.random.RandUtils;

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;


/*
 * compact Differential Evolution Light (with light exponential crossover and light mutation)
 */
public class MScDE extends Algorithm
{
	private double[] mean;
	private double[] sigma2;
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
	
		//COMMON PARAMTERS
		int virtualPopulationSize = this.getParameter("p0").intValue();//300
		
		//DE PARAMTERS
		double alpha = this.getParameter("p1").doubleValue();//0.25
		double F = this.getParameter("p2").doubleValue();//0.5
		int crossoverStrategy = this.getParameter("p3").intValue();//3
		int mutationStrategy = this.getParameter("p4").intValue();//1
		
		boolean isPersistent = this.getParameter("p5").intValue()!=0;//true
		int eta = virtualPopulationSize*2/3;
			
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		double[] normalizedBounds = {-1.0, 1.0};
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		int teta = 0;

		mean = new double[problemDimension];
		sigma2 = new double[problemDimension];
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

		double[] xr = new double[problemDimension];
		double[] xs = new double[problemDimension];
		double[] xt = new double[problemDimension];
		double[] xu = new double[problemDimension];
		double[] xv = new double[problemDimension];
		
		double[] winner = new double[problemDimension];
		double[] loser = new double[problemDimension];
		
		double[] sigma2_F = new double[problemDimension];
		
		FileWriter fileWriter = null;
		FileWriter fileWriter2 = null;

		// iterate
		while (i < maxEvaluations)
		{

			// mutation
			//...
			// crossover

			
			
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


	
	
	
	private double[] cdelight(double F, double alpha, int problemDimension, double[] best) 
	{
		double CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
		double Fmod = (1+2*Math.pow(F,2));
		double[] sigma2_F = new double[problemDimension];
		for (int n = 0; n < problemDimension; n++)
			sigma2_F[n] = Fmod*sigma2[n];	
		double[] b = generateIndividual(mean, sigma2_F);
		return crossOverExpFast(best, b, CR);
	}
	
	private double[] rand_one_randomF(double alpha, int problemDimension, int xo_strat, double[] best)
	{
		 // DE/rand/1-Random-Scale-Factor
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double F = 0.5*(1+RandUtils.random());
		double[] b = rand1(xr, xs, xt, F);
		return binOrExpXO(problemDimension, F, alpha, b, best, xo_strat);
	}
	
	private double[] rand_one(double F, double alpha, int problemDimension, int xo_strat, double[] best)
	{
	
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double[] b = rand1(xr, xs, xt, F);
		return binOrExpXO(problemDimension, F, alpha,  b, best, xo_strat);
	}
	
	private double[] rand_to_best(double F, double alpha, int problemDimension, int xo_strat, double[] best)
	{
	
		// DE/current(rand)-to-best/1
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double[] b = currentToBest1(xt, xr, xs, best, F);
		return binOrExpXO(problemDimension, F, alpha,  b, best, xo_strat);
	}
	
	private double[] rand_two(double F, double alpha, int problemDimension, int xo_strat, double[] best)
	{	
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double[] xu = generateIndividual(mean, sigma2);
		double[] xv = generateIndividual(mean, sigma2);
		double[] b = rand2(xr, xs, xt, xu, xv, F);
		return binOrExpXO(problemDimension, F, alpha,  b, best, xo_strat);
	}
	
	private double[] rand_to_best_two(double F, double alpha, int problemDimension, int xo_strat, double[] best)
	{	
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double[] xu = generateIndividual(mean, sigma2);
		double[] xv = generateIndividual(mean, sigma2);
		double[] b = randToBest2(xr, xs, xt, xu, xv, best, F);
		return binOrExpXO(problemDimension, F, alpha,  b, best, xo_strat);
	}
	
	
	private double[] binOrExpXO(int problemDimension, double F, double alpha, double[] b, double[] best, int strategy)
	{
		double CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
		if (strategy == 1)
			b = crossOverBin(best, b, CR);
		else if (strategy == 2)
			b = crossOverExp(best, b, CR);
		else
			
			System.out.println("Andate affanculo tutti quanti porcodio");
		return b;
	}
	
	

	
	
	
}

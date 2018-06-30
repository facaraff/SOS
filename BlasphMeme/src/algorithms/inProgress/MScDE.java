package algorithms.inProgress;

//import static utils.algorithms..operators.Operators.DEO;
import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.operators.DEOp.crossOverExpFast;
import static utils.algorithms.operators.DEOp.currentToBest1;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.operators.DEOp.rand2;
import static utils.algorithms.operators.DEOp.randToBest2;
import static utils.algorithms.CompactAlgorithms.generateIndividual;
import static utils.algorithms.CompactAlgorithms.scale;
import static utils.algorithms.CompactAlgorithms.updateMean;
import static utils.algorithms.CompactAlgorithms.updateSigma2;
import static utils.algorithms.Misc.toro;

import java.io.FileWriter;
//import java.io.IOException;

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
//	private double[] mean;
//	private double[] sigma2;
	
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


	
	
	
		

	
	
	
}

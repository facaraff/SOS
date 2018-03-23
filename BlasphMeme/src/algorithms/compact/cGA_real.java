package algorithms.compact;

import static  utils.algorithms.operators.CompactAlgorithms.generateIndividual;
import static  utils.algorithms.operators.CompactAlgorithms.scale;
import static  utils.algorithms.operators.CompactAlgorithms.updateMean;
import static  utils.algorithms.operators.CompactAlgorithms.updateSigma2;

import java.io.FileWriter;
import java.io.IOException;

import utils.MatLab;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Real coded compact Genetic Algorithm
 */
public class cGA_real extends Algorithm
{
	boolean debugPV = false;
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int virtualPopulationSize = this.getParameter("p0").intValue();
		int eta = this.getParameter("p1").intValue();
		boolean isPersistent = this.getParameter("p2").intValue()!=0;
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
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
		i += 2;

		double[] winner = new double[problemDimension];
		double[] loser = new double[problemDimension];
		
		FileWriter fileWriter = null;
		FileWriter fileWriter2 = null;
		if (debugPV)
		{
			try {
				fileWriter = new FileWriter("mean.txt");
				fileWriter2 = new FileWriter("sigma2.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// iterate
		while (i < maxEvaluations)
		{
			b = generateIndividual(mean, sigma2);
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
				else
				{
					// log best fitness (non persistent elitism)
					if (fBest < FT.getLastF())
						FT.add(i, fBest);
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
			
			if (debugPV)
			{
				try {
					fileWriter.write(MatLab.toString(mean) + "\n");
					fileWriter2.write(MatLab.toString(sigma2) + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (isPersistent)
			// log best fitness (persistent elitism)
			FT.add(i, fBest);
		else
		{
			// log best fitness (non persistent elitism)
			double lastFBest = FT.getLastF(); 
			if (fBest < lastFBest)
				FT.add(i, fBest);
			else
				FT.add(i, lastFBest);
		}
		
		if (debugPV)
		{
			try {
				fileWriter.close();
				fileWriter2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		finalBest = best;
		
		return FT;
	}
}
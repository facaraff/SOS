package algorithms.compact;

import static utils.algorithms.CompactAlgorithms.generateIndividual;
import static utils.algorithms.CompactAlgorithms.scale;
import static utils.algorithms.CompactAlgorithms.updateMean;
import static utils.algorithms.CompactAlgorithms.updateSigma2;
import static utils.algorithms.Misc.toro;

import java.io.FileWriter;
import java.io.IOException;

import utils.MatLab;
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * compact Particle Swarm Optimization
 */
public class cPSO extends Algorithm
{
	public boolean debugPV = false;
		
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int virtualPopulationSize = getParameter("p0").intValue(); //50
		double phi1 = getParameter("p1").doubleValue(); // -0.2
		double phi2 = getParameter("p2").doubleValue(); // -0.07
		double phi3 = getParameter("p3").doubleValue(); // 3.74
		double gamma1 = getParameter("p4").doubleValue(); // 1.0
		double gamma2 = getParameter("p5").doubleValue(); // 1.0
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		double[] normalizedBounds = {-1.0, 1.0};
		
		int i = 0;

		// initialize mean and sigma
		double[] mean = new double[problemDimension];
		double[] sigma2 = new double[problemDimension];
		for (int j = 0; j < problemDimension; j++)
		{
			mean[j] = 0.0;
			sigma2[j] = 8.0;
		}
		
		double[] xc = new double[problemDimension];
		for (int n = 0; n < problemDimension; n++)
			xc[n] = (bounds[n][1]+bounds[n][0])/2;
		
		// global best
		double[] x_gb = new double[problemDimension];
		double fitness_gb = Double.NaN;

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
		i += 2;
				
		if (fA < fB)
		{
			fitness_gb = fA;
			for (int n = 0; n < problemDimension; n++)
				x_gb[n] = a[n];
			FT.add(0, fA);
		}
		else
		{
			fitness_gb = fB;
			for (int n = 0; n < problemDimension; n++)
				x_gb[n] = b[n];
			FT.add(0, fB);			
		}
		
		a = null;
		b = null;
		aScaled = null;
		bScaled = null;
		
		// local best
		double[] x_lb = new double[problemDimension];

		// position and velocity
		double[] x = generateIndividual(mean, sigma2);
		double[] v = generateIndividual(mean, sigma2);
		for (int n = 0; n < problemDimension; n++)
			v[n] = 0.1*v[n];
		
		double[] xScaled = new double[problemDimension];
		double[] x_lbScaled = new double[problemDimension];
		double fitness_x;
		double fitness_lb;
		
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
			//if ((i % 10000) == 0)
			//	System.out.println(i);
			
			x_lb = generateIndividual(mean, sigma2);
			for (int n = 0; n < problemDimension; n++)
			{
				v[n] = phi1*v[n]+phi2*RandUtils.random()*(x_lb[n]-x[n])+phi3*RandUtils.random()*(x_gb[n]-x[n]);
				x[n] = gamma1*x[n] + gamma2*v[n];
			}
			
			x = toro(x, normalizedBounds);
			xScaled = scale(x, bounds, xc);
			fitness_x = problem.f(xScaled);
			
			x_lbScaled = scale(x_lb, bounds, xc);
			fitness_lb = problem.f(x_lbScaled);
			
			i += 2;

			if (fitness_lb < fitness_x)
			{
				for (int n = 0; n < problemDimension; n++)
				{
					winner[n] = x_lb[n];
					loser[n] = x[n];
				}
			}
			else
			{
				for (int n = 0; n < problemDimension; n++)
				{
					winner[n] = x[n];
					loser[n] = x_lb[n];
				}
			}
			
			if (fitness_lb < fitness_gb)
			{
				for (int n = 0; n < problemDimension; n++)
					x_gb[n] = x_lb[n];
				fitness_gb = fitness_lb;
				//FT.add(i, fitness_gb));
			}
			else if (fitness_x < fitness_gb)
			{
				for (int n = 0; n < problemDimension; n++)
					x_gb[n] = x[n];
				fitness_gb = fitness_x;
			}
			
			if (i % problemDimension == 0)
				FT.add(i, fitness_gb);

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
		
		// log best fitness
		FT.add(i, fitness_gb);
		
		if (debugPV)
		{
			try {
				fileWriter.close();
				fileWriter2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		finalBest = x_gb;
		
		return FT;
	}
}
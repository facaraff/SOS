package algorithms.compact;

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
import java.io.IOException;
//import java.util.Vector;

import utils.MatLab;
import utils.random.RandUtils;

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;


/*
 * compact Differential Evolution Light (with light exponential crossover and light mutation)
 */
public class cDE_exp_light extends Algorithm
{
	public boolean debugPV = false;
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int virtualPopulationSize = this.getParameter("p0").intValue();//300
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
		//FT.add(0, fParticle);

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
		if (debugPV)
		{
			try {
				fileWriter = new FileWriter("mean.txt");
				fileWriter2 = new FileWriter("sigma2.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		double CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
		//double Fmod = (1+2*F);
		double Fmod = (1+2*Math.pow(F,2));
		
		// iterate
		while (i < maxEvaluations)
		{
			//if ((i % 10000) == 0)
			//	System.out.println(i);
			
			// mutation
			switch (mutationStrategy)
			{
				case 1:
					// DE/rand/1
					// XXX (gio) Jarek Arabas' suggestion
					for (int n = 0; n < problemDimension; n++)
						sigma2_F[n] = Fmod*sigma2[n];
					b = generateIndividual(mean, sigma2_F);
					break;
				case 2:
					// DE/current(rand)-to-best/1
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xt = generateIndividual(mean, sigma2);
					b = currentToBest1(xt, xr, xs, best, F);
					break;
				case 3:
					// DE/rand/2
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xt = generateIndividual(mean, sigma2);
					xu = generateIndividual(mean, sigma2);
					xv = generateIndividual(mean, sigma2);
					b = rand2(xr, xs, xt, xu, xv, fB);
					break;
				case 4:
					// DE/rand/2
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xt = generateIndividual(mean, sigma2);
					xu = generateIndividual(mean, sigma2);
					xv = generateIndividual(mean, sigma2);
					b = randToBest2(xr, xs, xt, xu, xv, best, F);
					break;
				case 5:
					 // DE/rand/1-Random-Scale-Factor
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xt = generateIndividual(mean, sigma2);
					F = 0.5*(1+RandUtils.random());
					b = rand1(xr, xs, xt, F);
				default:
					break;
			}

			// crossover
			if (mutationStrategy != 4)
			{
				if (crossoverStrategy == 1)
					b = crossOverBin(best, b, alpha);
				else if (crossoverStrategy == 2)
					b = crossOverExp(best, b, CR);
				// XXX (gio) reviewer's suggestion about "fast" exponential crossover
				else if (crossoverStrategy == 3)
					b = crossOverExpFast(best, b, CR);
			}
			
			//b = saturateToro(b, normalizedBounds);
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
		
		//System.out.println(i);
		
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
		
		FT.add(i, fBest);
		return FT;

	}
}

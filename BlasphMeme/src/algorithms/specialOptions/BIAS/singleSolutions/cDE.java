package algorithms.specialOptions.BIAS.singleSolutions;

import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.operators.DEOp.currentToBest1;
import static utils.algorithms.operators.DEOp.currentToRand1;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.operators.DEOp.rand2;
//import static utils.algorithms.operators.DEOp.randToBest1;
import static utils.algorithms.operators.DEOp.randToBest2;
import static utils.algorithms.operators.DEOp.best1;
import static utils.algorithms.operators.DEOp.best2;

import static utils.algorithms.CompactAlgorithms.generateIndividual;
import static utils.algorithms.CompactAlgorithms.scale;
import static utils.algorithms.CompactAlgorithms.updateMean;
import static utils.algorithms.CompactAlgorithms.updateSigma2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * compact Differential Evolution Light (with light exponential crossover and light mutation)
 */
public class cDE extends AlgorithmBias
{	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int virtualPopulationSize = this.getParameter("p0").intValue();//300
		double alpha = this.getParameter("p1").doubleValue();//0.25
		double F = this.getParameter("p2").doubleValue();//0.5
		String mutationStrategy = this.getParameter("p3").toString();//1
		String crossoverStrategy = this.getParameter("p4").toString();//3
			
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		double[] normalizedBounds = {-1.0, 1.0};
		

		

				
		char correctionStrategy = this.correction;  // t --> toroidal   s --> saturation  d -->  discard  e ---> penalty
		String fileName = "cDE"+mutationStrategy+crossoverStrategy+correctionStrategy; 
		
		
		fileName+="D"+problem.getDimension()+"f0-"+(run+1);
		File file = new File(Dir+fileName+".txt");
		if (!file.exists()) 
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		int i = 0;
		int prevID = -1;
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);	
		String line = "# function 0 dim "+problemDimension+" virtualPopulationSize "+virtualPopulationSize+" alpha "+alpha+" F "+F+"\n";
		bw.write(line);
		line = null;
		line = new String();
		
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

		double fA = problem.f(aScaled); i++; newID++;
		
		FT.add(i, fA);
		line =""+newID+" "+formatter(fA)+" "+i+" "+prevID;
		for(int n = 0; n < problemDimension; n++)
			line+=" "+formatter(aScaled[n]);
		line+="\n";
		bw.write(line);
		line = null;
		line = new String();
		prevID = newID;
		
		
		double fB = problem.f(bScaled); i++;
		if (fA < fB)
		{
			fBest = fA;
			for (int n = 0; n < problemDimension; n++)
				best[n] = a[n];
			
		}
		else
		{
			fBest = fB;
			for (int n = 0; n < problemDimension; n++)
				best[n] = b[n];
			FT.add(i, fB);

			
			newID++; 
			
			line =""+newID+" "+formatter(fB)+" "+i+" "+prevID;
			for(int n = 0; n < problemDimension; n++)
				line+=" "+formatter(bScaled[n]);
			line+="\n";
			bw.write(line);
			line = null;
			line = new String();
			prevID = newID;		
		}
		

		double[] xr = new double[problemDimension];
		double[] xs = new double[problemDimension];
		double[] xt = new double[problemDimension];
		double[] xu = new double[problemDimension];
		double[] xv = new double[problemDimension];
		
		double[] winner = new double[problemDimension];
		double[] loser = new double[problemDimension];
		
		double CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
		
		this.numberOfCorrections = 0;
		
		// iterate
		while (i < maxEvaluations)
		{
			// mutation
			switch (mutationStrategy)
			{
				case "ro":
					 // DE/rand/1
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xt = generateIndividual(mean, sigma2);
					b = rand1(xr, xs, xt, F);
					break;
				case "rt":
					 // DE/rand/2
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xt = generateIndividual(mean, sigma2);
					xu = generateIndividual(mean, sigma2);
					xv = generateIndividual(mean, sigma2);
					b = rand2(xr, xs, xt, xu, xv, F);
					break;
				case "ctro":
					// DE/current-to-rand/1
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xt = generateIndividual(mean, sigma2);
					xc = generateIndividual(mean, sigma2);
					b = currentToRand1(xr, xs, xt, xc, F);
					break;
				case "bo":
					 // DE/best/1
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					b = best1(best,xr,xs,F);
					break;
				case "bt":
					 // DE/best/1
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xu = generateIndividual(mean, sigma2);
					xv = generateIndividual(mean, sigma2);
					b = best2(best,xr,xs,xu,xv,F);
					break;
				case "ctbo":
					// DE/current(rand)-to-best/1
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xt = generateIndividual(mean, sigma2);
					b = currentToBest1(xt, xr, xs, best, F);
					break;
				case "rtbt":
					// DE/rand-to-best/2
					xr = generateIndividual(mean, sigma2);
					xs = generateIndividual(mean, sigma2);
					xt = generateIndividual(mean, sigma2);
					xu = generateIndividual(mean, sigma2);
					xv = generateIndividual(mean, sigma2);
					b = randToBest2(xr, xs, xt, xu, xv, best, F);
					break;		
				case "rsf":
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
			if (!mutationStrategy.equals("ctro"))
			{
				if (crossoverStrategy.equals("bin"))
					b = crossOverBin(best, b, CR);
				else if (crossoverStrategy.equals("exp"))
					b = crossOverExp(best, b, CR);
			}
			
			b = correct(b,best,normalizedBounds);

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
				
				newID++; 
				
				line =""+newID+" "+formatter(fB)+" "+i+" "+prevID;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(bScaled[n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				prevID = newID;		
				
				FT.add(i, fBest);
				
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
		
		
		
		FT.add(i, fBest);
		finalBest = best;
		bw.close();

		wrtiteCorrectionsPercentage(fileName, (double) this.numberOfCorrections/maxEvaluations, "correctionsSingleSol");
		return FT;
	}
	
}
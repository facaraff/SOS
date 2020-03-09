package algorithms.specialOptions.BIAS.singleSolutions;

import static utils.algorithms.operators.ISBOp.generateIndividual;
import static utils.algorithms.CompactAlgorithms.scale;
import static utils.algorithms.CompactAlgorithms.updateMean;
import static utils.algorithms.CompactAlgorithms.updateSigma2;


import utils.random.RandUtilsISB;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.algorithms.Counter;

/*
 * Real coded compact Genetic Algorithm
 */
public class cGA_real extends AlgorithmBias
{



	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int virtualPopulationSize = this.getParameter("p0").intValue();//300
		int eta = this.getParameter("p1").intValue();//200
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		this.correction = 'x';
		
		String FullName = getFullName("cGA"+this.correction,problem); 
		Counter PRGCounter = new Counter(0);
		createFile(FullName);
		
		int i = 0;
		int prevID = -1;
		int newID = 0;
		
		
		
		RandUtilsISB.setSeed(this.seed);	
		writeHeader("virtualPopulationSize "+virtualPopulationSize+" eta "+eta, problem);	
		
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
		
		a = generateIndividual(mean, sigma2, PRGCounter);
		b = generateIndividual(mean, sigma2, PRGCounter);
		aScaled = scale(a, bounds, xc);
		bScaled = scale(b, bounds, xc);

		double fA = problem.f(aScaled); i++; newID++; 
		FT.add(1, fA);
		
		String line = new String();
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


		double[] winner = new double[problemDimension];
		double[] loser = new double[problemDimension];

		// iterate
		while (i < maxEvaluations)
		{
			b = generateIndividual(mean, sigma2, PRGCounter);
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
		
		finalBest = best;
		bw.close();
		FT.add(i, fBest);
		
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(), "correctionsSingleSol");
		return FT;
	
	}


}
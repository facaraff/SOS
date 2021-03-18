package algorithms.specialOptions.BIAS.singleSolutions;


import static utils.algorithms.Misc.cloneSolution;
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
 * compact Particle Swarm Optimization
 */
public class cPSO extends AlgorithmBias
{	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int virtualPopulationSize = getParameter("p0").intValue(); //50
		double phi1 = getParameter("p1").doubleValue(); // -0.2
		double phi2 = getParameter("p2").doubleValue(); // -0.07
		double phi3 = getParameter("p4").doubleValue(); // 3.74
		double gamma1 = getParameter("p4").doubleValue(); // 1.0
		double gamma2 = getParameter("p5").doubleValue(); // 1.0
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		double[] normalizedBounds = {-1.0, 1.0};
		
		String FullName = getFullName("cPSO"+this.correction,problem); 
		Counter PRGCounter = new Counter(0);
		createFile(FullName);
		
		int i = 0;
		int prevID = -1;
		int newID = 0;
		
		RandUtilsISB.setSeed(this.seed);	
		writeHeader("phi1 "+phi1+" phi2 "+phi2+" phi3 "+phi3+" gamma1 "+gamma1+" gamma2 "+gamma2,problem);


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
		a = generateIndividual(mean, sigma2, PRGCounter);
		b = generateIndividual(mean, sigma2, PRGCounter);
		aScaled = scale(a, bounds, xc);
		bScaled = scale(b, bounds, xc);
		double fA = problem.f(aScaled); i++; newID++;
		FT.add(i,fA);
		
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
			fitness_gb = fA;
			for (int n = 0; n < problemDimension; n++)
				x_gb[n] = a[n];
			FT.add(i,fA);
		}
		else
		{
			fitness_gb = fB;
			for (int n = 0; n < problemDimension; n++)
				x_gb[n] = b[n];
			FT.add(i,fB);
			
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
		
		a = null;
		b = null;
		aScaled = null;
		bScaled = null;
		
		// local best
		double[] x_lb = new double[problemDimension];

		// position and velocity
		double[] x = generateIndividual(mean, sigma2, PRGCounter);
		double[] v = generateIndividual(mean, sigma2, PRGCounter);
		for (int n = 0; n < problemDimension; n++)
			v[n] = 0.1*v[n];
		
		double[] xScaled = new double[problemDimension];
		double[] x_lbScaled = new double[problemDimension];
		double fitness_x;
		double fitness_lb;
		
		double[] winner = new double[problemDimension];
		double[] loser = new double[problemDimension];
		
		double[] prevX = cloneSolution(x);
		
		while (i < maxEvaluations)
		{

			x_lb = generateIndividual(mean, sigma2, PRGCounter);
			for (int n = 0; n < problemDimension; n++)
			{
				v[n] = phi1*v[n]+phi2*RandUtilsISB.random(PRGCounter)*(x_lb[n]-x[n])+phi3*RandUtilsISB.random(PRGCounter)*(x_gb[n]-x[n]);
				x[n] = gamma1*x[n] + gamma2*v[n];
			}
			
			x = correct(x, prevX, normalizedBounds, PRGCounter);
			
			xScaled = scale(x, bounds, xc);
			fitness_x = problem.f(xScaled); i++;
			
			
			
			
			x_lbScaled = scale(x_lb, bounds, xc);
			fitness_lb = problem.f(x_lbScaled);
	
			
			
			if (fitness_x < fitness_gb)
			{
				for (int n = 0; n < problemDimension; n++)
					x_gb[n] = x[n];
				fitness_gb = fitness_x;
				
				newID++; 
				
				line =""+newID+" "+formatter(fitness_x)+" "+i+" "+prevID;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(xScaled[n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				prevID = newID;		
				
			}
			
			if (i % problemDimension == 0)
				FT.add(i,fitness_gb);

			if (fitness_lb < fitness_x)
			{
				for (int n = 0; n < problemDimension; n++)
				{
					winner[n] = x_lb[n];
					loser[n] = x[n];
				}
				
				newID++; 
				
				line =""+newID+" "+formatter(fitness_lb)+" "+i+" "+prevID;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(x_lbScaled[n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				prevID = newID;	
				
			}
			else
			{
				for (int n = 0; n < problemDimension; n++)
				{
					winner[n] = x[n];
					loser[n] = x_lb[n];
				}
			}
			
			
			// best and mean/sigma2 update
			mean = updateMean(winner, loser, mean, virtualPopulationSize);
			sigma2 = updateSigma2(winner, loser, mean, sigma2, virtualPopulationSize);
		}
		
		// log best fitness
		FT.add(i, fitness_gb);
		bw.close();
		finalBest = x_gb;
		
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(), "correctionsSingleSol");
		return FT;
	}
}
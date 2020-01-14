package algorithms.specialOptions.BIAS.singleSolutions;

import static utils.algorithms.Misc.toro;
import static utils.algorithms.Misc.saturation;
import static utils.algorithms.Misc.cloneSolution;
import static utils.algorithms.CompactAlgorithms.generateIndividual;
import static utils.algorithms.CompactAlgorithms.scale;
import static utils.algorithms.CompactAlgorithms.updateMean;
import static utils.algorithms.CompactAlgorithms.updateSigma2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

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
		
		
	 // t --> toroidal   s --> saturation  d -->  discard  e ---> penalty
		String fileName = "cPSO"+this.correction; 
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
		String line = "# function 0 dim "+problemDimension+" phi1 "+phi1+" phi2 "+phi2+" phi3 "+phi3+" gamma1 "+gamma1+" gamma2 "+gamma2+"\n";
		bw.write(line);
		line = null;
		line = new String();

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
		double fA = problem.f(aScaled); i++; newID++;
		
		FT.add(i,fA);
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
		
		double[] prevX = cloneSolution(x);
		
		while (i < maxEvaluations)
		{

			x_lb = generateIndividual(mean, sigma2);
			for (int n = 0; n < problemDimension; n++)
			{
				v[n] = phi1*v[n]+phi2*RandUtils.random()*(x_lb[n]-x[n])+phi3*RandUtils.random()*(x_gb[n]-x[n]);
				x[n] = gamma1*x[n] + gamma2*v[n];
			}
			
			
			
			//x = toro(x, normalizedBounds);
			
			double[] output = new double[problemDimension];
			if(this.correction == 't')
			{
				//System.out.println("TORO");
				output = toro(x, normalizedBounds);
			}
			else if(this.correction== 's')
			{
				//System.out.println("SAT");
				output = saturation(x, normalizedBounds);
			}
			else if(this.correction== 'd')
			{
				output = toro(x, normalizedBounds);
				if(!Arrays.equals(output, x))
					output = prevX;
				else
					prevX = cloneSolution(x);
			}
			else
				System.out.println("No bounds handling shceme seleceted");
			
			if(!Arrays.equals(output, x))
			{
				x = output;
				output = null;
				this.numberOfCorrections++;
			}
			
			//a = correct(a, prevX, problem.getBounds());
			
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
		wrtiteCorrectionsPercentage(fileName, (double) this.numberOfCorrections/maxEvaluations, "correctionsSingleSol");
		return FT;
	}
}
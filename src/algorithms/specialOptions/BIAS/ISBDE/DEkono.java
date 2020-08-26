package algorithms.specialOptions.BIAS.ISBDE;

import utils.algorithms.operators.DEOp;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Corrections.torus;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;

import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;
/*
 * Differential Evolution (standard version, rand/1/bin)
 */
public class DEkono extends AlgorithmBias
{
	
	static String Dir = "/home/facaraff/Desktop/KONODATA/DEroe";
	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue(); 
		double CR = getParameter("p1").doubleValue();
		double F = getParameter("p2").doubleValue();
		int crossoverStrategy = 2;//getParameter("p3").intValue();
		int mutationStrategy = 1;//getParameter("p4").intValue();
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		

		int[] ids = new int[populationSize];
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		
		

		File file = new File(Dir+"/DEroe"+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1)+".txt");
		if (!file.exists()) 
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);	
		RandUtils.setSeed(seed);
	
		String line = "# function 0 dim "+problemDimension+" pop "+populationSize+" F "+F+" CR "+CR+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
		bw.write(line);
		line = null;
		line = new String();

		
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			i++;
			newID++;
			ids[j] = newID;
			line =""+newID+" -1 "+"-1 "+"-1 "+formatter(fitnesses[j])+" "+i+" -1";
			for(int n = 0; n < problemDimension; n++)
				line+=" "+formatter(population[j][n]);
			line+="\n";
			bw.write(line);
			line = null;
			line = new String();
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
			}
			
	
		}

		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		
		// iterate
		while (i < maxEvaluations)
		{
			double[][] temp = new double[populationSize][problemDimension];
			//for(int p=0;p<populationSize;p++)
				//for(int d=0;d<problemDimension;d++)
					//temp[p][d]=population[p][d];
			double[] temp2 = new double[populationSize];
			
			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				// mutation
				switch (mutationStrategy)
				{
					case 1:
						// DE/rand/1
						newPt = DEOp.rand1(population, F);
						break;
					case 2:
						// DE/cur-to-best/1newPt
						newPt = DEOp.currentToBest1(population, best, j, F);
						break;
					case 3:
						// DE/rand/2
						newPt = DEOp.rand2(population, F);
						break;
					case 4:
						// DE/current-to-rand/1
						crossPt = DEOp.currentToRand1(population, j, F);
						break;
					case 5:
						// DE/rand-to-best/2
						newPt = DEOp.randToBest2(population, best, F);
						break;
					default:
						break;
				}
		
				// crossover
				if (mutationStrategy != 4)
				{
					if (crossoverStrategy == 1)
						crossPt = DEOp.crossOverBin(currPt, newPt, CR);
					else if (crossoverStrategy == 2)
						crossPt = DEOp.crossOverExp(currPt, newPt, CR);
				}
				
				crossPt = torus(crossPt, bounds);
				crossFit = problem.f(crossPt);
				i++;
				newID++;

				// best update
				if (crossFit < fBest)
				{
					fBest = crossFit;
					for (int n = 0; n < problemDimension; n++)
						best[n] = crossPt[n];
					FT.add(i, fBest);
				}

				// replacement
				if (crossFit < currFit)
				{
					for (int n = 0; n < problemDimension; n++)
						temp[j][n] = crossPt[n];
						//population[j][n] = crossPt[n];
					temp2[j] = crossFit;
					
					
					
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						temp[j][n] = currPt[n];
						//population[j][n] = currPt[n];
					temp2[j] = currFit;
				}
			}
			
			population = temp;
			temp=null;
			fitnesses = temp2;
			temp2=null;
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		bw.close();
		return FT;
	}
	
	
	
	public String formatter(double value)
	{
		String str =""+value;
		str = this.DF.format(value).toLowerCase();
		if (!str.contains("e-"))  
			str = str.replace("e", "e+");
		return str;
	}
	
	public int[] getIndices(int popSize)
	{
		int[] indices = new int[popSize];
		for(int n=0; n<popSize; n++)
			indices[n] = n;
		return indices;
	}
	
	public double[] saturation(double[] x, double[][] bounds)
	{
		double[] xs = new double[x.length];
		for(int i=0; i<x.length; i++)
		{
			if(x[i]>bounds[i][1])
				xs[i] = bounds[i][1];
			else if(x[i]<bounds[i][0])
				xs[i] = bounds[i][0];
			else
				xs[i] = x[i];
		}		
		return xs;
	}
	
	
}



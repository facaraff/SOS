package algorithms.specialOptions.BIAS;

import static utils.algorithms.Misc.generateRandomSolution;


import java.text.DecimalFormat;

import utils.random.RandUtils;

import static utils.MatLab.subtract;
import static utils.MatLab.dot;
import static utils.MatLab.sum;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class simplifiedSTOCAZZO extends Algorithm
{
	
	private int run = 0;
	
	public void setRun(int r)
	{
		this.run = r;
	}
	

	
	
	//static String Dir = "/home/fabio/Desktop/kylla/Kononova/GA";
	static String Dir = "/home/facaraff/Desktop/kononova/GA";
	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{	
		String s = new String();
		int populationSize = getParameter("p0").intValue(); 
		//double nt =  getParameter("p1").intValue();
		double md = getParameter("p1").doubleValue();
		double d = getParameter("p2").doubleValue();
			
		
		
		File file = new File(Dir+"/GAs"+"p"+populationSize+"D"+problem.getDimension()+"f01-"+(run+1)+".txt");
		if (!file.exists()) 
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		int[] ids = new int[populationSize];
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);
		String line = "# dim "+problemDimension+" pop "+populationSize+" md "+md+" SEED  "+seed+"\n";
		s+=line;
		
		
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		int parent1 = 0;
		int parent2 = 0; 
		int random = 0;
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
			line =""+newID+" -1 "+"-1 "+formatter(fitnesses[j])+" "+i+" -1";
			for(int n = 0; n < problemDimension; n++)
				line+=" "+formatter(population[j][n]);
			line+="\n";
			s+=line;
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
			} 
				
		}
							
		// iterate
		while (i < maxEvaluations)
		{ 	
			//int[] indices = getIndices(populationSize);
			//indices = RandUtils.randomPermutation(indices); 
			//if(fitnesses[indices[0]] < fitnesses[indices[1]])
				//parent1 = indices[0];
			//else
				//parent1 = indices[1];
			//indices = RandUtils.randomPermutation(indices); 
			//if(fitnesses[indices[0]] < fitnesses[indices[1]])
				//parent2 = indices[0];
			//else
				//parent2 = indices[1];
			
			int[] indices = getIndices(populationSize);
			indices = RandUtils.randomPermutation(indices); 
			parent1 = indices[0];
			parent2 = indices[1];
			indices = null;
			double[] alpha = new double[problemDimension];
			for(int n=0; n<problemDimension; n++)
				alpha[n] = RandUtils.uniform(-d,1+d);
			
			double[] xChild = sum(population[parent1],dot(alpha, subtract(population[parent2],population[parent1])));
			
			for(int n=0; n<problemDimension; n++)
				xChild[n] += RandUtils.gaussian(0, md*(bounds[n][1]-bounds[n][0]));
			//xChild = saturateToro(xChild, bounds);
			xChild = saturation(xChild, bounds);
			double fChild = problem.f(xChild);
			i++;
			
			//worst = indexMax(fitnesses);
			random = RandUtils.randomInteger(populationSize-1);
			if(fChild<fitnesses[random])
			{
				newID++;
				int indexRandom = ids[random];
				int indexParent1 = ids[parent1];
				int indexParent2 = ids[parent2];
				ids[random] = newID;
				for(int n=0; n<problemDimension; n++)
					population[random][n] = xChild[n];
				fitnesses[random] = fChild;
				
				
				line =""+newID+" "+indexParent1+" "+indexParent2+" "+formatter(fChild)+" "+i+" "+indexRandom;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(xChild[n]);
				line+="\n";
				s+=line;
				bw.write(s);
				s = null;
				s = new String();
				
			}
				
			
		}	
		
		finalBest = best;
		
		FT.add(i, fBest);
		
		bw.close();
		
		return FT;
	}
	
	
	
	//bw.write(simpga.getFile());
	//bw.write(ga.getFile());
	//bw.close();
   // simpga.delFile();
	
	
	
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

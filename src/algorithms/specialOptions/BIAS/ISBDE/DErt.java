package algorithms.specialOptions.BIAS.ISBDE;

import utils.algorithms.operators.DEOp;

import static utils.algorithms.Corrections.completeOneTailedNormal;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Corrections.toro;

import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;

import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

public class DErt extends AlgorithmBias
{
	
	static String Dir = "/home/facaraff/Desktop/KONODATA/DErt/";
//	static String Dir = "C:\\Users\\fcaraf00\\Desktop\\KONO\\DErt\\";

	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue();
		double F = getParameter("p1").doubleValue();
		double CR = getParameter("p2").doubleValue();
		char crossoverStrategy = 'b'; //e-->exponential  b-->binary
		char correctionStrategy = 'c';  // t --> toro   s-->saturation
		String fileName = "DErt"+crossoverStrategy+""+correctionStrategy;
		
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		int[] ids = new int[populationSize];
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		
		
		fileName+="p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1);
		File file = new File(Dir+fileName+".txt");
		//File file = new File(Dir+"/DEroe"+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1)+".txt");
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);
		String line = "# function 0 dim "+problemDimension+" pop "+populationSize+" F "+F+" CR "+CR+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
		bw.write(line);
		line = null;
		line = new String();
		//bw.close();
		
		
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
			line =""+ids[j]+" -1 "+"-1 "+"-1 "+"-1 "+"-1 "+formatter(fitnesses[j])+" "+i+" -1";
			for(int n = 0; n < problemDimension; n++)
				line+=" "+formatter(population[j][n]);
			line+="\n";
			bw.write(line);
			line = null;
			line = new String();
			//this.file+=line;
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
			}
			
			//i++;
		}

		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		int ciccio = 0;
		
		// iterate
		while (i < maxEvaluations)
		{
			double[][] temp = new double[populationSize][problemDimension];
			double[] temp2 = new double[populationSize];
			int[] idsTemp = new int[populationSize];
			
			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
			
					
				int[] r = new int[populationSize];
				for (int n = 0; n < populationSize; n++)
					r[n] = n;
				r = RandUtils.randomPermutation(r);
				
				int r1 = r[0];
				int r2 = r[1];
				int r3 = r[2];
				int r4 = r[3];
				int r5 = r[4];
				
				for (int n = 0; n < problemDimension; n++)
					newPt[n] = population[r1][n] + F*(population[r2][n]-population[r3][n]) + F*(population[r4][n]-population[r5][n]);
		
				// crossover
				if (crossoverStrategy == 'b')
					crossPt = DEOp.crossOverBin(currPt, newPt, CR);
				else if (crossoverStrategy == 'e')
					crossPt = DEOp.crossOverExp(currPt, newPt, CR);
				else
					System.out.println("Crossover is not used");
				
				//crossPt = correction(crossPt, bounds, correctionStrategy);ciccio++; incCorrected();
				double[] output = new double[problemDimension];
				if(correctionStrategy == 't')
				{
					//System.out.println("toro");
					output = toro(crossPt, bounds);
					
					if(!Arrays.equals(output, crossPt))
					{
						crossPt = output;
						ciccio++;//incCorrected();
					}
					crossFit = problem.f(crossPt);
				}
				else if(correctionStrategy== 's')
				{
					//System.out.println("SAT");
					output = saturation(crossPt, bounds);
					
					if(!Arrays.equals(output, crossPt))
					{
						crossPt = output;
						ciccio++;//incCorrected();
					}
					crossFit = problem.f(crossPt);
				}
				else if(correctionStrategy== 'e')
				{
					output = saturation(crossPt, bounds);
					if(!Arrays.equals(output, crossPt))
					{
						ciccio++;
						crossFit = 2;
					}
				}
				else if(correctionStrategy == 'c')
				{
					output = completeOneTailedNormal(crossPt, bounds, 3.0);
					
					if(!Arrays.equals(output, crossPt))
					{
						crossPt = output;
						ciccio++;
					}
					crossFit = problem.f(crossPt);
				}
				else
					System.out.println("No bounds handling shceme seleceted");
				
				
//				if(!Arrays.equals(output, crossPt))
//				{
//					crossPt = output;
//					ciccio++;//incCorrected();
//				}
//				crossFit = problem.f(crossPt);
				i++;

				
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
					newID++;
					for (int n = 0; n < problemDimension; n++)
						temp[j][n] = crossPt[n];
						//population[j][n] = crossPt[n];
					temp2[j] = crossFit;
					idsTemp[j] = newID;
					line =""+newID+" "+ids[r2]+" "+ids[r3]+" "+ids[r4]+" "+ids[r5]+" "+ids[r1]+" "+formatter(fitnesses[j])+" "+i+" "+ids[j];
					for(int n = 0; n < problemDimension; n++)
						line+=" "+formatter(population[j][n]);
					line+="\n";
					bw.write(line);
					line = null;
					line = new String();
					
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						temp[j][n] = currPt[n];
						//population[j][n] = currPt[n];
					idsTemp[j] = ids[j];
					temp2[j] = currFit;
				}
			}
			
			population = temp;
			temp=null;
			fitnesses = temp2;
			temp2=null;
			ids = idsTemp;
			idsTemp=null;
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		bw.close();
		
//		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations);
		//wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, fitnesses, correctionStrategy);
		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, F, CR, seed);
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
	
	
	
	
/*
	public double[] correction(double[] x, double[][] bounds, char correctionType)
	{
		//boolean equal = false;
		double[] output = new double[x.length];
		for(int i=0; i<x.length; i++)
			output[i] = x[i];
		if(correctionType=='t')
		{
			//System.out.println("toro");
			output = toro(x, bounds);
		}
		else
		{
			//System.out.println("SAT");
			output = saturation(x, bounds);
		}
		//for(int i=0; i<x.length; i++)
			//if(output[i] != x[i])
				//equal = false;
		//if(!Arrays.equals(output, x))
			//incCorrected();
		return output;
	}
*/
	
	public void wrtiteCorrectionsPercentage(String name, double percentage, double F_value, double CR_value, long SEED) throws Exception
	{
		File f = new File(Dir+"corrections.txt");
		if(!f.exists())
			f.createNewFile();
		FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write(name+" "+percentage+" "+F_value+" "+CR_value+" "+SEED+"\n");
		BW.close();
	}
	
	public void wrtiteCorrectionsPercentage(String name, double percentage) throws Exception
	{
		File f = new File(Dir+"corrections.txt");
		if(!f.exists())
			f.createNewFile();
		FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write(name+" "+percentage+"\n");
		BW.close();
	}
	
	public void wrtiteCorrectionsPercentage(String name, double percentage, double[] finalFitnesses, char boundaHendler) throws Exception
	{
		if(boundaHendler != 'e')
			wrtiteCorrectionsPercentage(name, percentage);
		else
		{
			int counter = 0;
			for(int n=0; n<finalFitnesses.length; n++)
				if(finalFitnesses[n]==2)
					counter++;
			File f = new File(Dir+"corrections.txt");
			if(!f.exists())
				f.createNewFile();
			FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
			BufferedWriter BW = new BufferedWriter(FW);
			BW.write(name+" "+percentage+" "+formatter((double)counter/finalFitnesses.length)+"\n");
			BW.close();
		}
	}
	
	
}



/**
Copyright (c) 2019, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FreeBSD Project.
*/

package algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections.old;


import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Corrections.toro;

import java.util.Arrays;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;

import utils.MatLab;
import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;
import static utils.RunAndStore.slash;

public class DEcbob extends AlgorithmBias
{
	
//	private int run = 0;
//
//	public void setRun(int r)
//	{
//		this.run = r;
//	}
	
	
	//static String Dir = "/home/facaraff/Desktop/KONODATA/";
	static String Dir = "/home/facaraff/Dropbox/AnnaFabio"+slash();
	protected int run = 0;
	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	FTrend FT = new FTrend();
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue();
		double F = getParameter("p1").doubleValue();
		double CR = getParameter("p2").doubleValue();
		//char crossoverStrategy = 'b';
		char correctionStrategy = 'e';  // t --> toro   s-->saturation  'e'--> penalty
//		String fileName = "DEcbo"+crossoverStrategy+""+correctionStrategy;
		String fileName = "DEcbob"+correctionStrategy+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1)+".txt";
		
		

		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

//		int[] ids = new int[populationSize];
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		
		
//		fileName+="p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1);
//		File file = new File(Dir+fileName+".txt");
//		//File file = new File(Dir+"/DEroe"+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1)+".txt");
//		if (!file.exists())
//			file.createNewFile();
//		FileWriter fw = new FileWriter(file.getAbsoluteFile());
//		BufferedWriter bw = new BufferedWriter(fw);
		
		
		
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);
//		String line = "# function 0 dim "+problemDimension+" pop "+populationSize+" F "+F+" CR "+CR+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
//		bw.write(line);
//		line = null;
//		line = new String();
		//bw.close();
		
//		int bestID = -1;
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			i++;
				
//			newID++;
//			ids[j] = newID;
			
			if (j == 0 || fitnesses[j] < fBest)
			{
//				bestID = ids[j];
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
			}
			
//			line =""+ids[j]+" -1 "+"-1 "+bestID+" "+formatter(fitnesses[j])+" "+i+" -1";
//			for(int n = 0; n < problemDimension; n++)
//				line+=" "+formatter(population[j][n]);
//			line+="\n";
//			bw.write(line);
//			line = null;
//			line = new String();
			//this.file+=line;
			
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
				
				// Mutation current-t0-Best/rand/1
				
				int indexBest = MatLab.indexMin(fitnesses);
				int[] r = new int[populationSize-1];
				for (int n = 0; n < populationSize-1; n++)
					if(n != indexBest)
						r[n] = n;
				r = RandUtils.randomPermutation(r);
				
				int r2 = r[0];
				int r3 = r[1];

				for (int n = 0; n < problemDimension; n++)
					newPt[n] = population[j][n] + F*(population[indexBest][n]-population[j][n]) + F*(population[r2][n]-population[r3][n]);
					

							
//				// crossover
//				if (crossoverStrategy == 'b')
//					crossPt = crossOverBin(currPt, newPt, CR);
//				else if (crossoverStrategy == 'e')
//					crossPt = crossOverExp(currPt, newPt, CR);
//				else
//					System.out.println("Crossover is not used");
				
				crossPt = crossOverBin(currPt, newPt, CR);
				
				//crossPt = correction(crossPt, bounds, correctionStrategy);ciccio++; incCorrected();
				double[] output = new double[problemDimension];
				if(correctionStrategy == 't')
				{
					//System.out.println("toro");
					output = toro(crossPt, bounds);
					
					if(!Arrays.equals(output, crossPt))
					{
						crossPt = output;
						ciccio++;
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
						ciccio++;
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
				else
					System.out.println("No bounds handling shceme seleceted");
				
//				if(!Arrays.equals(output, crossPt))
//				{
//					crossPt = output;
//					ciccio++;
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
//					line =""+newID+" "+ids[r2]+" "+ids[r3]+" "+ids[indexBest]+" "+formatter(fitnesses[j])+" "+i+" "+ids[j];
//					for(int n = 0; n < problemDimension; n++)
//						line+=" "+formatter(population[j][n]);
//					line+="\n";
//					bw.write(line);
//					line = null;
//					line = new String();
					
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						temp[j][n] = currPt[n];
						//population[j][n] = currPt[n];
//					idsTemp[j] = ids[j];
					temp2[j] = currFit;
				}
			}
			
			population = temp;
			temp=null;
			fitnesses = temp2;
			temp2=null;
//			ids = idsTemp;
			idsTemp=null;
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
//		bw.close();
		
		//wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, F, CR, seed);
		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, fitnesses, correctionStrategy, F, CR, seed);
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
		
	
	public void wrtiteCorrectionsPercentage(String name, double percentage, double F_value, double CR_value, long SEED) throws Exception
	{
		File f = new File(Dir+"correctionsTEMP.txt");
		if(!f.exists())
			f.createNewFile();
		FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write(name+" "+percentage+" "+F_value+" "+CR_value+" "+SEED+"\n");
		BW.close();
	}
	
	public void wrtiteCorrectionsPercentage(String name, double percentage, double[] finalFitnesses, char boundaHendler, double F_value, double CR_value, long SEED) throws Exception
	{
		if(boundaHendler != 'e')
			wrtiteCorrectionsPercentage(name, percentage, F_value, CR_value, SEED);
		else
		{
			int counter = 0;
			for(int n=0; n<finalFitnesses.length; n++)
				if(finalFitnesses[n]==2)
					counter++;
			File f = new File(Dir+"correctionsTEMP.txt");
			if(!f.exists())
				f.createNewFile();
			FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
			BufferedWriter BW = new BufferedWriter(FW);
			BW.write(name+" "+percentage+" "+formatter((double)counter/finalFitnesses.length)+" "+F_value+" "+CR_value+" "+SEED+"\n");
			BW.close();
		}
	}
	
}





















				
				
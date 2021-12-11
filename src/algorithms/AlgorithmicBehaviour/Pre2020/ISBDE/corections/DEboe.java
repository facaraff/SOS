/**
Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections;

import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Corrections.completeOneTailedNormal;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Corrections.mirroring;
import static utils.algorithms.Corrections.toro;

import java.util.Arrays;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;

import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import  utils.MatLab;
import static utils.RunAndStore.FTrend;
//import static utils.RunAndStore.slash;


public class DEboe extends AlgorithmBias
{
	
	//static String Dir = "C:\\Users\\fcaraf00\\Desktop\\KONONOVA\\";
	static String Dir = "/home/facaraff/Desktop/KONODATA/DECorrections/";
	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	
	protected char correctionStrategy = 'e';  // t --> toro   s-->saturation
	protected int run = 0;
	
	
	public DEboe(char correction) { super(); this.correctionStrategy = correction;}
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue();
		double F = getParameter("p1").doubleValue();
		double CR = getParameter("p2").doubleValue();
//		char correctionStrategy = 'e';  // t --> toro   s-->saturation
		String fileName = "DEboe"+correctionStrategy+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1)+".txt";
		
		FTrend FT = new FTrend();
		
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);
		
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			i++;
			
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
		int ciccio = 0;
		
		// iterate
		while (i < maxEvaluations)
		{
			double[][] temp = new double[populationSize][problemDimension];
			double[] temp2 = new double[populationSize];
//			int[] idsTemp = new int[populationSize];
			
			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				int indexBest = MatLab.indexMin(fitnesses);
				int[] r = new int[populationSize-1];
				for (int n = 0; n < populationSize-1; n++)
					if(n != indexBest)
						r[n] = n;
				r = RandUtils.randomPermutation(r);
				
				int r2 = r[0];
				int r3 = r[1];

				for (int n = 0; n < problemDimension; n++)
					newPt[n] = population[indexBest][n] + F*(population[r2][n]-population[r3][n]);
							
				crossPt = crossOverExp(currPt, newPt, CR);
				
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
				else if(correctionStrategy== 'm')
				{
					output = mirroring(crossPt, bounds);
					if(!Arrays.equals(output, crossPt))
					{
						ciccio++;
						crossPt = output;
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
					for (int n = 0; n < problemDimension; n++)
						temp[j][n] = crossPt[n];
					temp2[j] = crossFit;
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						temp[j][n] = currPt[n];
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
		
		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, F, CR, seed);
		//wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, fitnesses, correctionStrategy, F, CR, seed);
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
		File f = new File(Dir+"correctionsTEMP2.txt");
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
			File f = new File(Dir+"correctionsTEMP2.txt");
			if(!f.exists())
				f.createNewFile();
			FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
			BufferedWriter BW = new BufferedWriter(FW);
			BW.write(name+" "+percentage+" "+formatter((double)counter/finalFitnesses.length)+" "+F_value+" "+CR_value+" "+SEED+"\n");
			BW.close();
		}
	}
	
	
}





















				
				
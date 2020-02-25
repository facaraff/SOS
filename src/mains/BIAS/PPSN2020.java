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
package mains.BIAS;


import java.util.Vector;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import algorithms.specialOptions.BIAS.singleSolutions.*;
import utils.MatLab;
import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import static utils.RunAndStore.slash;
	
public class PPSN2020
{
	static int nrRepetitions = 50;
	static int budgetFactor = 10000;
	static int problemDimension = 30;
	
	static String dir = "/home/facaraff/Desktop/KONODATA/PPSN/";
	//static String dir="C:\\Users\\fcaraf00\\Desktop\\KONONOVA\\";
	
	public static void main(String[] args) throws Exception
	{	
		double[] bias = null; //Benchmark additive bias
		
		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
		
		AlgorithmBias a;
		Problem p;
		
		char[] corrections = {'s','t','d','m','c'};
		String[] DEMutations = {"ro","rt","ctro","bo","bt","ctbo","rtbt"};
		char[] CrossOvers = {'b','e'};
		
		for (char correction : corrections)
			for (String mutation : DEMutations)
				if(mutation.equals("ctro"))
				{
					a = new cDE(mutation,'x');
					a.setDir(dir+"CDE"+slash());
					a.setCorrection(correction);
					a.setParameter("p0", 300.0);
					a.setParameter("p1", 0.25);
					a.setParameter("p2", 0.5);
					algorithms.add(a);	
					a = null;
				}
				else
					for(char xover : CrossOvers)
						{
							a = new cDE(mutation,xover);
							a.setDir(dir+"CDE"+slash());
							a.setCorrection(correction);
							a.setParameter("p0", 300.0);
							a.setParameter("p1", 0.25);
							a.setParameter("p2", 0.5);
							algorithms.add(a);	
							a = null;
						}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		


//		
//		a = new cBFO();
//		a.setDir(dir+"CBFO"+slash());
//		a.setParameter("p0", 300.0);
//		a.setParameter("p1", 0.1);
//		a.setParameter("p2", 4.0);
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 10.0);
//		a.setParameter("p5", 2.0);
//		a.setParameter("p6", 2.0);
//		a.setCorrection('t');
//		algorithms.add(a);
//		
//		a = new cBFO();
//		a.setDir(dir+"CBFO"+slash());
//		a.setParameter("p0", 300.0);
//		a.setParameter("p1", 0.1);
//		a.setParameter("p2", 4.0);
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 10.0);
//		a.setParameter("p5", 2.0);
//		a.setParameter("p6", 2.0);
//		a.setCorrection('s');
//		algorithms.add(a);
//		
//		a = new cBFO();
//		a.setDir(dir+"CBFO"+slash());
//		a.setParameter("p0", 300.0);
//		a.setParameter("p1", 0.1);
//		a.setParameter("p2", 4.0);
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 10.0);
//		a.setParameter("p5", 2.0);
//		a.setParameter("p6", 2.0);
//		a.setCorrection('d');
//		algorithms.add(a);	
//		
//		a = new cBFO();
//		a.setDir(dir+"CBFO"+slash());
//		a.setParameter("p0", 300.0);
//		a.setParameter("p1", 0.1);
//		a.setParameter("p2", 4.0);
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 10.0);
//		a.setParameter("p5", 2.0);
//		a.setParameter("p6", 2.0);
//		a.setCorrection('m');
//		algorithms.add(a);
//		
//		a = new cBFO();
//		a.setDir(dir+"CBFO"+slash());
//		a.setParameter("p0", 300.0);
//		a.setParameter("p1", 0.1);
//		a.setParameter("p2", 4.0);
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 10.0);
//		a.setParameter("p5", 2.0);
//		a.setParameter("p6", 2.0);
//		a.setCorrection('c');
//		algorithms.add(a);
//		
//
//		a = new cPSO();
//		a.setDir(dir+"CPSO"+slash());
//		a.setCorrection('t');
//		a.setParameter("p0", 50.0);
//		a.setParameter("p1", -0.2);
//		a.setParameter("p2", -0.07);
//		a.setParameter("p3", 3.74);
//		a.setParameter("p4", 1.0);
//		a.setParameter("p5", 1.0);
//		algorithms.add(a);
//		
//		a = new cPSO();
//		a.setDir(dir+"CPSO"+slash());
//		a.setCorrection('s');
//		a.setParameter("p0", 50.0);
//		a.setParameter("p1", -0.2);
//		a.setParameter("p2", -0.07);
//		a.setParameter("p3", 3.74);
//		a.setParameter("p4", 1.0);
//		a.setParameter("p5", 1.0);
//		algorithms.add(a);
//		
//		a = new cPSO();
//		a.setDir(dir+"CPSO"+slash());
//		a.setCorrection('d');
//		a.setParameter("p0", 50.0);
//		a.setParameter("p1", -0.2);
//		a.setParameter("p2", -0.07);
//		a.setParameter("p3", 3.74);
//		a.setParameter("p4", 1.0);
//		a.setParameter("p5", 1.0);
//		algorithms.add(a);
//		
//		a = new cPSO();
//		a.setDir(dir+"CPSO"+slash());
//		a.setCorrection('m');
//		a.setParameter("p0", 50.0);
//		a.setParameter("p1", -0.2);
//		a.setParameter("p2", -0.07);
//		a.setParameter("p3", 3.74);
//		a.setParameter("p4", 1.0);
//		a.setParameter("p5", 1.0);
//		algorithms.add(a);
//		
//		a = new cPSO();
//		a.setDir(dir+"CPSO"+slash());
//		a.setCorrection('c');
//		a.setParameter("p0", 50.0);
//		a.setParameter("p1", -0.2);
//		a.setParameter("p2", -0.07);
//		a.setParameter("p3", 3.74);
//		a.setParameter("p4", 1.0);
//		a.setParameter("p5", 1.0);
//		algorithms.add(a);
		
		double[][] bounds = new double[problemDimension][2];
		for(int i=0; i<problemDimension; i++)
		{
			bounds[i][0] = 0;
			bounds[i][1] = 1;
		}	
		p = new Noise(problemDimension, bounds);
		problems.add(p);	
		
		int algorithmIndex = 0;
		for (AlgorithmBias algorithm : algorithms)
		{
			System.out.print("" + "\t");
			String algName = algorithm.getClass().getSimpleName();
			if (algName.length() >= 8)
				System.out.print(algName + "\t");
			else
				System.out.print(algName + "\t\t");
			
			if (algorithmIndex > 0)
			{
				System.out.print("\t" + "W");
				
			}
			algorithmIndex++;
		}	
		System.out.println();
				
		double[][] finalValues;
		MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();


		int problemIndex = 0;
		for (Problem problem: problems)
		{
			System.out.print("f" + (problemIndex+1) + "\t");

			finalValues = new double[algorithms.size()][nrRepetitions];
			algorithmIndex = 0;
			for (AlgorithmBias algorithm : algorithms)
				{
					for (int i = 0; i < nrRepetitions; i++)
					{
						algorithm.setRun(i);
						
						double best = runAlgorithmRepetition(algorithm, problem);
						if (bias != null)
							finalValues[algorithmIndex][i] = best - bias[problemIndex];
						else
							finalValues[algorithmIndex][i] = best;
								
					}
					System.out.print("done+\t");
					String mean = utils.RunAndStore.format(MatLab.mean(finalValues[algorithmIndex]));
					String std = utils.RunAndStore.format(MatLab.std(finalValues[algorithmIndex]));
					System.out.print(mean + " \u00B1 " + std + "\t");
					if (algorithmIndex > 0)
					{			
						double pValue = mannWhitneyUTest.mannWhitneyUTest(finalValues[0], finalValues[algorithmIndex]);
						char w = '=';
						if (pValue < 0.05)
						{
							if (MatLab.mean(finalValues[0]) < MatLab.mean(finalValues[algorithmIndex]))
								w = '+';
							else
								w = '-';
						}

						System.out.print(w + "\t");
							
					}
					algorithmIndex++;
				}

				System.out.println();
				problemIndex++;
				}
			}

			public static class Noise extends Problem 
			{
				public Noise(int dimension, double[][] bounds) { super(dimension, bounds); }

				public double f(double[] x){return RandUtils.random();}
			}
			

			private static double runAlgorithmRepetition(AlgorithmBias algorithm, Problem problem) throws Exception
			{
				FTrend FT = algorithm.execute(problem, budgetFactor*problem.getDimension());

				return FT.getLastF();
			
			
			}
				
			
		}

		
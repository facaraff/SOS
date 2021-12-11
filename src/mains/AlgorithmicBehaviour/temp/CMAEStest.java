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
package mains.AlgorithmicBehaviour.temp;

//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

	

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import algorithms.AlgorithmicBehaviour.Pre2020.CMAES;
//import test.TestOptimizerHelper.CEC2005;
import utils.MatLab;
import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

	
public class CMAEStest
{
	// number of repetitions and budget factor
	static int nrRepetitions = 50;
	static int budgetFactor = 1000;//10000;
	static int problemDimension = 100;//30;
	
	static boolean debug = false;
	static boolean showPValue = false;
	static boolean multiThread = false;
	
	public static void main(String[] args) throws Exception
	{	
		// run the optimization algorithm
		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
		
		AlgorithmBias a;
		Problem p;
		
		double[] bias = null;
		
		a = new CMAES('s');
		a.setParameter("p0",5.0);
		algorithms.add(a);
		
		a = new CMAES('s');
		a.setParameter("p0",20.0);
		algorithms.add(a);

		a = new CMAES('s');
		a.setParameter("p0",100.0);
		algorithms.add(a);
		
		a = new CMAES('d');
		a.setParameter("p0",5.0);
		algorithms.add(a);
		
		a = new CMAES('d');
		a.setParameter("p0",20.0);
		algorithms.add(a);

		a = new CMAES('d');
		a.setParameter("p0",100.0);
		algorithms.add(a);
		
		double[][] bounds = new double[problemDimension][2];
		for(int i=0; i<problemDimension; i++)
		{
			bounds[i][0] = 0;
			bounds[i][1] = 1;
		}	
		p = new Noise( problemDimension, bounds);
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
				if (showPValue)
					System.out.print("\t" + "p-value" + "\t");
						}
					algorithmIndex++;
				}	
				System.out.println();
				
				double[][] finalValues;
				MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

				int nrProc = Runtime.getRuntime().availableProcessors();
				ExecutorService //static String Dir = "/home/fabio/Desktop/kylla/CMAES";
threadPool = Executors.newFixedThreadPool(nrProc);
				CompletionService<AlgorithmResult> pool = new ExecutorCompletionService<AlgorithmResult>(threadPool);

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
							
							if (multiThread)
							{
								AlgorithmRepetitionThread thread = new AlgorithmRepetitionThread(algorithm, problem, i, problemIndex);
								pool.submit(thread);
							}
							else
							{
								double best = runAlgorithmRepetition(algorithm, problem);
								if (bias != null)
									finalValues[algorithmIndex][i] = best - bias[problemIndex];
								else
									finalValues[algorithmIndex][i] = best;
							}
						} System.out.println("done");

						if (multiThread)
						{
							for (int j = 0; j < nrRepetitions; j++)
							{
								Future<AlgorithmResult> result = pool.take();
								AlgorithmResult algorithmResult = result.get();
								finalValues[algorithmIndex][algorithmResult.repNr] = algorithmResult.best - bias[problemIndex];
							}
						}

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
							if (showPValue)
								System.out.print(utils.RunAndStore.format(pValue) + "\t");
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

				public double f(double[] x)
				{ 	
					return RandUtils.random();
				}
			}
			
			private static class AlgorithmResult
			{
				double best;
				int repNr;

				public AlgorithmResult(double best, int repNr) {
					super();
					this.best = best;
					this.repNr = repNr;
				}
			}

			private static class AlgorithmRepetitionThread implements Callable<AlgorithmResult>
			{
				AlgorithmBias algorithm;
				Problem problem;
				int repNr;

				public AlgorithmRepetitionThread(AlgorithmBias algorithm, Problem problem, int repNr, int problemIndex)
				{
					this.algorithm = algorithm;
					this.problem = problem;
					this.repNr = repNr;
				}

				@Override
				public AlgorithmResult call() throws Exception {
					double best = runAlgorithmRepetition(algorithm, problem);
					
					System.out.println("finish");
					
					return new AlgorithmResult(best, repNr);
				}		
			}

			private static double runAlgorithmRepetition(AlgorithmBias algorithm, Problem problem) throws Exception
			{
				FTrend FT = algorithm.execute(problem, budgetFactor*problem.getDimension());

				return FT.getLastF();
			
			
			}
				
			
		}

		
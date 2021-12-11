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

import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections.DEbob;
import algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections.DEboe;
import algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections.DEcbob;
import algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections.DEcboe;
import algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections.DErob;
import algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections.DEroe;
import algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections.DErtb;
import algorithms.AlgorithmicBehaviour.Pre2020.ISBDE.corections.DErte;
import utils.MatLab;

//import benchmarks.problemsImplementation.CEC2010.BenchmarkCEC2010_C;

import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;
import static utils.RunAndStore.format;
	
public class DECorrections
{
	// number of repetitions and budget factor
	static int nrRepetitions = 50;
	static int budgetFactor = 10000;
	static int problemDimension = 30;
	static double fUpperBound = 2;
	
	static boolean debug = false;
	static boolean showPValue = false;
	static boolean multiThread = false;
	
	public static void main(String[] args) throws Exception
	{	
		// run the optimization algorithm
		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
		
//		Algorithm a;
		Problem p;
		
		
		double[] bias = null;	
		
		
		int[] DEVariants = {1,2,3,4,5,6,7,8};
		

		
		char[] corrections = {'t','s','e', 'm'};

		
		double[] FSteps = {0.05, (0.05+(1.95/9.0)), (0.05+2.0*(1.95/9.0)), (0.05+3.0*(1.95/9.0)), (0.05+4.0*(1.95/9.0)),(0.05+5.0*(1.95/9.0)),(0.05+6.0*(1.95/9.0)),(0.05+7.0*(1.95/9.0)),(0.05+8.0*(1.95/9.0)),(0.05+9.0*(1.95/9.0))};
//		
		double[] CRSteps = {0.05, (0.05+(0.94/4.0)), (0.05+2.0*(0.94/4.0)), (0.05+3.0*(0.94/4.0)), (0.05+4.0*(0.94/4.0))};
		
		int[] popSizes = {5,20,100};
		
		generateNewConfiguration(algorithms, DEVariants, FSteps, CRSteps, popSizes, corrections);
		
		
		//********************************************************
		
		double[][] bounds = new double[problemDimension][2];
		for(int i=0; i<problemDimension; i++)
		{
			bounds[i][0] = 0;
			bounds[i][1] = fUpperBound;
//			bounds[i][0] = 0;
//			bounds[i][1] = 100;
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
				System.out.print//static String Dir = "/home/fabio/Desktop/kylla/CMAES";
("\t" + "W");
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

						String mean = format(MatLab.mean(finalValues[algorithmIndex]));
						String std = format(MatLab.std(finalValues[algorithmIndex]));
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
								System.out.print(format(pValue) + "\t");
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
					return Math.random();		
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
				@SuppressWarnings("unused")
				int index;
				

				public AlgorithmRepetitionThread(AlgorithmBias algorithm, Problem problem, int repNr, int problemIndex)
				{
					this.algorithm = algorithm;
					this.problem = problem;
					this.repNr = repNr;
					this.index = problemIndex;
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
			
			
		/*****************************************************************************/
		
			static protected AlgorithmBias initialiseDEVariant(int v, char correction)
			{		
				switch (v) 
				{
				case 1:
					return new DEcbob(correction);
				case 2:
					return new DEcboe(correction);
				case 3:
					return new DEroe(correction);
				case 4:
					return new DErob(correction);
				case 5:
					return new DErtb(correction);
				case 6:
					return new DErte(correction);
				case 7:
					return new DEbob(correction);
				case 8:
					return new DEboe(correction);
				default:
					return null;
				}
				
			}
			
			
			
//			F: 0.05 min 2 max with step 1.95/8
//			Cr: min 0.05 max 0.99 step 0.95/4
//			I am fine with F
//			but for Cr should not it be step = 0.94/4.0?
			
			static void generateNewConfiguration( Vector<AlgorithmBias> algorithms, int[] DEVariants,  double[] FSteps, double[] CRSteps, int[] popSizes, char[] corrections)
			{
				
				int FStepsNr = FSteps.length;
				int CRStepsNr = CRSteps.length;
				int popSizesNr = popSizes.length;
				int correctionsNr = corrections.length;
				int DEVariantsNr = DEVariants.length;
				
				AlgorithmBias a = null;
				
				//for(int devar = 2; devar < 3; devar++)
				for(int devar = 0; devar < DEVariantsNr; devar++)
					for(int f = 0; f < FStepsNr; f++)
						for(int cr = 0; cr < CRStepsNr; cr++)
							for(int ps = 0; ps < popSizesNr; ps++)
								for(int corr = 0; corr < correctionsNr; corr++) 
								{
									a = initialiseDEVariant(DEVariants[devar],corrections[corr]);
									a.setParameter("p0",(double)popSizes[ps]);
									a.setParameter("p1",FSteps[f]);
									a.setParameter("p2",CRSteps[cr]);
									algorithms.add(a);
								}
			}			

}
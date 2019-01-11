package mains.BIAS;

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
	

//import test.utils.RunAndStore.CEC2005;
import utils.MatLab;
import utils.random.RandUtils;
import algorithms.specialOptions.BIAS.CMAESkono;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
	
public class kononovaCMAEStest
{
	// number of repetitions and budget factor
	static int nrRepetitions = 50;
	static int budgetFactor =  1000;//10000;
	static int problemDimension = 100;//= 30;
	
	static boolean debug = false;
	static boolean showPValue = false;
	static boolean multiThread = false;
	
	public static void main(String[] args) throws Exception
	{	
		// run the optimization algorithm
		Vector<Algorithm> algorithms = new Vector<Algorithm>();
		Vector<Problem> problems = new Vector<Problem>();
		
		Algorithm a;
		Problem p;
		
		double[] bias = null;
		a = new CMAESkono('t');
		a.setParameter("p0",5.0);
		algorithms.add(a);
		
		a = new CMAESkono('t');
		a.setParameter("p0",20.0);
		algorithms.add(a);
		//static String Dir = "/home/fabio/Desktop/kylla/CMAES";

		a = new CMAESkono('t');
		a.setParameter("p0",100.0);
		//a.setParameter("p1",0.01);
		//a.setParameter("p2",0.25);
		algorithms.add(a);
		
		
//		a = new CMAESkono('s');
//		a.setParameter("p0",5.0);
//		algorithms.add(a);
//		
//		a = new CMAESkono('s');
//		a.setParameter("p0",20.0);
//		algorithms.add(a);
//		//static String Dir = "/home/fabio/Desktop/kylla/CMAES";
//
//		a = new CMAESkono('s');
//		a.setParameter("p0",100.0);
//		//a.setParameter("p1",0.01);
//		//a.setParameter("p2",0.25);
//		algorithms.add(a);
//		
//		
//		a = new CMAESkono('d');
//		a.setParameter("p0",5.0);
//		algorithms.add(a);
//		
//		a = new CMAESkono('d');
//		a.setParameter("p0",20.0);
//		algorithms.add(a);
//		//static String Dir = "/home/fabio/Desktop/kylla/CMAES";
//
//		a = new CMAESkono('d');
//		a.setParameter("p0",100.0);
//		//a.setParameter("p1",0.01);
//		//a.setParameter("p2",0.25);
//		algorithms.add(a);
		
		
		double[][] bounds = new double[problemDimension][2];
		for(int i=0; i<problemDimension; i++)
		{
			bounds[i][0] = 0;
			bounds[i][1] = 1;
		}	
		p = new Noise( problemDimension, bounds);
		problems.add(p);	
		
		int algorithmIndex = 0;
		for (Algorithm algorithm : algorithms)
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
					for (Algorithm algorithm : algorithms)
					{
						for (int i = 0; i < nrRepetitions; i++)
						{
							//CMAESkono pippo = (CMAESkono)algorithm;
							//pippo.setRun(i); pippo =null;
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
//					return Math.random();		
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
				Algorithm algorithm;
				Problem problem;
				int repNr;
//			int index;

				public AlgorithmRepetitionThread(Algorithm algorithm, Problem problem, int repNr, int problemIndex)
				{
					this.algorithm = algorithm;
					this.problem = problem;
					this.repNr = repNr;
//					this.index = problemIndex;
				}

				@Override
				public AlgorithmResult call() throws Exception {
					double best = runAlgorithmRepetition(algorithm, problem);
					
					System.out.println("finish");
					
					return new AlgorithmResult(best, repNr);
				}		
			}

			private static double runAlgorithmRepetition(Algorithm algorithm, Problem problem) throws Exception
			{
				FTrend FT = algorithm.execute(problem, budgetFactor*problem.getDimension());
				return FT.getLastF();
			}
				
			
		}

		
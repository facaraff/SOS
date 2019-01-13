package mains.BIAS;

import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
	
import utils.MatLab;
import algorithms.specialOptions.BIAS.corrections.DEcbob;
import algorithms.specialOptions.BIAS.corrections.DEcboe;
import algorithms.specialOptions.BIAS.corrections.DEroe;
import algorithms.specialOptions.BIAS.corrections.DErob;
import algorithms.specialOptions.BIAS.corrections.DErtb;
import algorithms.specialOptions.BIAS.corrections.DErte;
import algorithms.specialOptions.BIAS.corrections.DEbob;
import algorithms.specialOptions.BIAS.corrections.DEboe;

//import benchmarks.problemsImplementation.CEC2010.BenchmarkCEC2010_C;

import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;
import static utils.RunAndStore.format;
	
public class DECorrections
{
	// number of repetitions and budget factor
	static int nrRepetitions = 50;
	static int budgetFactor = 10000;
	static int problemDimension = 30;
	
	static boolean debug = false;
	static boolean showPValue = false;
	static boolean multiThread = false;
	
	public static void main(String[] args) throws Exception
	{	
		// run the optimization algorithm
		Vector<Algorithm> algorithms = new Vector<Algorithm>();
		Vector<Problem> problems = new Vector<Problem>();
		
//		Algorithm a;
		Problem p;
		

		
		
//		 F \in {0.05, 0.2, 0.4, 0.7, 0.9}
//		CR \in {0.05, 0.4, 0.7, 0.9 0.99} 

		
		
//		F: 0.05 min 2 max with step 1.95/8
//		Cr: min 0.05 max 0.99 step 0.95/4
//		I am fine with F
//		but for Cr should not it be step = 0.94/4?
		
		
		
		double[] bias = null;	
		
//		setDEbob(algorithms);	
//		setDErtb(algorithms);
//		setDEboe(algorithms);
//		setDEroe(algorithms);
		
//		setDErob(algorithms,'e');
//		setDErob(algorithms,'s');
//		setDErob(algorithms,'t');
//		
//		setDErte(algorithms, 'e');
//		setDErte(algorithms, 's');
//		setDErte(algorithms, 't');
//		
//		setDEcboe(algorithms, 'e');
//		setDEcboe(algorithms, 's');
//		setDEcboe(algorithms, 't');
//		
//		setDEbob(algorithms, 'e');
//		setDEbob(algorithms, 's');
//		setDEbob(algorithms, 't');
		
		//*********NEW METHOD 2018************************
		
		int[] DEVariants = {1,2,3,4,5,6,7,8};
		
		char[] corrections = {'t','s','e'};
		
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
			bounds[i][1] = 1;
//			bounds[i][0] = 0;
//			bounds[i][1] = 100;
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
//							DEro pippo = (DEro)algorithm;
//							DEbo pippo = (DEbo)algorithm;
//							DEcbo pippo = (DEcbo)algorithm;
//							DErt pippo = (DErt)algorithm;
//							pippo.setRun(i); pippo =null;
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
				Algorithm algorithm;
				Problem problem;
				int repNr;
				@SuppressWarnings("unused")
				int index;
				

				public AlgorithmRepetitionThread(Algorithm algorithm, Problem problem, int repNr, int problemIndex)
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

			private static double runAlgorithmRepetition(Algorithm algorithm, Problem problem) throws Exception
			{
//				long t0, t1;

//				t0 = System.currentTimeMillis();
				FTrend FT = algorithm.execute(problem, budgetFactor*problem.getDimension());
//				t1 = System.currentTimeMillis();

//				int n = FT.size();

				
				return FT.getLastF();
			}
			
			
		/*****************************************************************************/
		
			static protected Algorithm initialiseDEVariant(int v, char correction)
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
			
			static void generateNewConfiguration( Vector<Algorithm> algorithms, int[] DEVariants,  double[] FSteps, double[] CRSteps, int[] popSizes, char[] corrections)
			{
				
				int FStepsNr = FSteps.length;
				int CRStepsNr = CRSteps.length;
				int popSizesNr = popSizes.length;
				int correctionsNr = corrections.length;
				int DEVariantsNr = DEVariants.length;
				
				Algorithm a = null;
				
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
			
			
			
/*****************************************************************************/
//OLD METHODS IS COMMENTED OUT FOR NOW... I WIL REMOVE IT		
			
//			private static void setDErob(Vector<Algorithm> algorithms, char correction)
//			{
//				Algorithm a;
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//			
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				/************************************************/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				
//				
//			}
//			
//			
//			
//			
//			
//			private static void setDErte(Vector<Algorithm> algorithms, char correction)
//			{
//				Algorithm a;
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//			
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				/************************************************/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DErte(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DErte(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DErte(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//			}
//			
//			
//			private static void setDEcboe(Vector<Algorithm> algorithms, char correction)
//			{
//				Algorithm a;
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//			
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				/************************************************/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEcboe(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEcboe(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//			}
//			
//			private static void setDEbob(Vector<Algorithm> algorithms, char correction)
//			{
//				Algorithm a;
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//			
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.05);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				/************************************************/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.2);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.4);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.7);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//		/************************************************/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.05);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.4);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.7);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.9);
//				algorithms.add(a);
//				
//				/*********/
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",5.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//				a = new DEbob(correction);
//				a.setParameter("p0",20.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//
//				a = new DEbob(correction);
//				a.setParameter("p0",100.0);
//				a.setParameter("p1",0.9);
//				a.setParameter("p2",0.99);
//				algorithms.add(a);
//				
//			}
			

}
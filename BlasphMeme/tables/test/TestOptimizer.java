package test;

import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import test.TestOptimizerHelper.CEC2014;

import test.TestOptimizerHelper.CEC2005;
import utils.MathUtils;
import algorithms.SteepestDescent;
import algorithms.interfaces.Algorithm;
import algorithms.interfaces.Problem;
import algorithms.utils.Best;

public class TestOptimizer
{
	// number of repetitions and budget factor
	static int nrRepetitions = 2;
	static int budgetFactor = 10000;
	static int problemDimension = 10;

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

//		a = new ISPO();
//		a.pushParameter("p0", 1.0);
//		a.pushParameter("p1", 10.0);
//		a.pushParameter("p2", 2.0);
//		a.pushParameter("p3", 4.0);
//		a.pushParameter("p4", 1e-5);
//		a.pushParameter("p5", 30.0);
//		algorithms.add(a);

//	    a = new AdpISPO();
//		a.pushParameter("p0",2.0);
//		a.pushParameter("p1",2.0);
//		a.pushParameter("p2",1e-5);
//		a.pushParameter("p3",30.0);
//		a.pushParameter("p4",10.0);
//		a.pushParameter("p5",0.4);
//		a.pushParameter("p6",0.9);
//		a.pushParameter("p7",1.49445);
//		a.pushParameter("p8",8.0);
//		algorithms.add(a);

//		a = new ISPOrestart();
//		a.pushParameter("p0",2.0);
//		a.pushParameter("p1",2.0);
//		a.pushParameter("p2",1e-5);
//		a.pushParameter("p3",1.0);
//		a.pushParameter("p4",0.01);
//		a.pushParameter("p5",10.0);
//		algorithms.add(a);

//		a = new MDE();
//		a.pushParameter("p0",60.0);
//		algorithms.add(a);

//		a = new DE();
//		a.pushParameter("p0", 60.0);
//		a.pushParameter("p1", 0.5);
//		a.pushParameter("p2", 0.7);
//		a.pushParameter("p3", 1.0);
//		a.pushParameter("p4", 1.0);
//		algorithms.add(a);

//		a = new CLPSO();
//		a.pushParameter("p0", 60.0);
//		algorithms.add(a);

//		a = new cPSO();
//		a.pushParameter("p0", 300.0);
//		a.pushParameter("p1", -0.2);
//		a.pushParameter("p2", -0.07);
//		a.pushParameter("p3", 3.74);
//		a.pushParameter("p4", 1.0);
//		a.pushParameter("p5", 1.0);
//		algorithms.add(a);

//		a = new SPORE();
//		a.pushParameter("p0",1e-8);
//		algorithms.add(a);

//		a = new VISPO();
//		a.pushParameter("p0",10.0);
//		a.pushParameter("p1",30.0);
//		a.pushParameter("p2",0.65);
//		algorithms.add(a);
		
//		a = new JADE();
//		a.pushParameter("p0",60.0);
//		a.pushParameter("p1",0.05);
//		a.pushParameter("p2",0.1); 
//		algorithms.add(a);
		
//		a = new CCPSO2();
//		a.pushParameter("p0", 30.0);
//		a.pushParameter("p1", 0.5);
//		algorithms.add(a);
	
//		a = new PMS();
//    	a.pushParameter("p0", 0.95);
//     	a.pushParameter("p1", 150.0);
//    	a.pushParameter("p2", 0.4);
//    	a.pushParameter("p3", 10e-5);
//		a.pushParameter("p4", 2.0);
//    	a.pushParameter("p5", 0.5);
//		algorithms.add(a);
	
//    	a = new ThreeSome();
//    	a.pushParameter("p0", 0.95);
//    	a.pushParameter("p1", 4.0);
//    	a.pushParameter("p2", 0.1);
//    	a.pushParameter("p3", 150.0);
//    	a.pushParameter("p4", 0.4);
//		algorithms.add(a);
		
//		a = new CMAES();
//		algorithms.add(a);

//		a = new jDE();
//		a.pushParameter("p0", 40.0);
//		a.pushParameter("p1", 0.1);
//		a.pushParameter("p2", 1.0);
//		a.pushParameter("p3", 0.1);
//		a.pushParameter("p4", 0.1);
//		algorithms.add(a);
		
//		a = new NelderMead();
//		a.pushParameter("p0", 1.0);
//		a.pushParameter("p1", 0.5);
//		a.pushParameter("p2", 2.0);
//		a.pushParameter("p3", 0.5);
//		algorithms.add(a);

//		a = new CA_ILS();
//		a.pushParameter("p0",3.0);
//		a.pushParameter("p1",5.0);
//		a.pushParameter("p2",0.02);
//		a.pushParameter("p3",100.0);
//		a.pushParameter("p4",0.838);
//		a.pushParameter("p5",0.3);
//		a.pushParameter("p6",0.0);
//		algorithms.add(a);

//		a = new SPAM();
//		a.pushParameter("p0", 0.5);
//		a.pushParameter("p1", 0.4);
//		a.pushParameter("p2", 150.0);
//		a.pushParameter("p3", 2.0);
//		a.pushParameter("p4", 0.5);
//		a.pushParameter("p5", 1e-5);
//		algorithms.add(a);
		
//		a = new DifferentialSearch();
//		a.pushParameter("p0",50.0);
//		a.pushParameter("p1",0.3);
//		a.pushParameter("p2",0.3);
//		a.pushParameter("p3",1.0);
//		algorithms.add(a);
		
//		a = new EPSDE_LS();
//		a.pushParameter("p0",50.0);
//		a.pushParameter("p1",200.0);
//		a.pushParameter("p2",1000.0);
//		a.pushParameter("p3",0.0);
//		algorithms.add(a);

//		a = new EPSDE_LS();
//		a.pushParameter("p0",50.0);
//		a.pushParameter("p1",200.0);
//		a.pushParameter("p2",1000.0);
//		a.pushParameter("p3",1.0);
//		algorithms.add(a);

//		a = new SA_EPSDE();
//		a.pushParameter("p0",50.0);
//		a.pushParameter("p1",20.0);
//		algorithms.add(a);
		
//		a = new MS_CAP_SADE();
//		a.pushParameter("p0",50.0);
//		a.pushParameter("p1",1e-6);
//		a.pushParameter("p2",3.0);
//		a.pushParameter("p3",10.0);
//		algorithms.add(a);
		
//		a = new MS_CAP();
//		a.pushParameter("p0",50.0);
//		a.pushParameter("p1",1e-6);
//		a.pushParameter("p2",3.0);
//		algorithms.add(a);
		
		a = new SteepestDescent();
		a.pushParameter("p0",0.5);
		algorithms.add(a);
		
//		a = new MetaGenesisGA();
//		a.pushParameter("p0",10.0);
//		a.pushParameter("p1",5.0);
//		a.pushParameter("p2",0.1);
//		a.pushParameter("p3",0.5);
//		a.pushParameter("p4",0.5);
//		algorithms.add(a);

		/*
		// BBOB 2010
		bias = new double[] {79.48, -209.88, -462.09, -462.09, -9.21, 35.9, 
								92.94, 149.15, 123.83, -54.94, 76.27, -621.11, 
								29.97, -52.35, 1000.0, 71.35, -16.94, -16.94,
								-102.55, -546.5, 40.78, -1000.0, 6.87, 102.61};
		for (int i = 0; i < 24; i++)
		{
			double[] bounds;
			if ((i <= 1) || (i == 3) || (i == 4) || (i == 6)|| (i == 7)|| (i == 8)|| (i == 9) || 
				(i == 11) || (i == 12) || (i == 13) || (i == 14) || (i >= 16))
				bounds = new double[] {-5, 5};
			else
				bounds = new double[] {-32, 32};


			p = new BBOB10(i+1, problemDimension, bounds);
			problems.add(p);

			//p.f(AlgorithmUtils.generateRandomSolution(bounds, problemDim));
			//double precision = 1e-8; // see DefaultParam in fgeneric.c
			//System.out.println((i+1) + "\t" + (BenchmarkBBOB2010.getJNIfgeneric().getFtarget()-precision));
			//BenchmarkBBOB2010.finalizeBBOB();
		}
		*/

		
		// CEC 2005
		bias = new double[] {-450, -450, -450, -450, -310, 390, -180, -140, -330, -330, 
				90, -460, -130, -300, 120, 120, 120, 10, 10, 10, 
				360, 360, 360, 260, 260};
		for (int i = 1; i <= 25; i++)
		{
			double[] bounds;
			if ((i <= 6) || (i == 14))
				bounds = new double[] {-100, 100};
			else if (i == 7)
				bounds = new double[] {0, 600};
			else if (i == 8)
				bounds = new double[] {-32, 32};
			else if (i == 11)
				bounds = new double[] {-0.5, 0.5};
			else if (i == 12)
				bounds = new double[] {-Math.PI, Math.PI};
			else if (i == 13)
				bounds = new double[] {-5, 5}; //XXX (gio) {-3,1} in Suganthan's code, {-5,5} in report
			else if (i == 25)
				bounds = new double[] {2, 5};
			else
				bounds = new double[] {-5, 5};
			p = new CEC2005(i, problemDimension, bounds);
			problems.add(p);
		}
	

		/*
		// SISC 2010
		bias = new double[] {-450.0, -450.0, 390.0, -330.0, -180.0, -140.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		for (int i = 1; i <= 19; i++)
		{
			double[] bounds;
			if ((i == 18) || (i == 14) || (i == 4))
				bounds = new double[] {-5, 5};
			else if ((i == 5))
				bounds = new double[] {-600, 600};
			else if ((i == 6))
				bounds = new double[] {-32, 32};
			else if ((i == 7) || (i == 19) || (i == 15))
				bounds = new double[] {-10, 10};
			else if ((i == 8))
				bounds = new double[] {-65.536, 65.536};
			else if ((i == 10))
				bounds = new double[] {-15, 15};
			else 
				bounds = new double[] {-100, 100};
			p = new SISC2010(i, problemDimension, bounds);
			problems.add(p);
		}
		*/
		
		/*
		// CEC 2013
		bias = new double[] {-1400, -1300, -1200, -1100, -1000, -900, -800, 
				-700, -600, -500, -400, -300, -200, -100, 
				100, 200, 300, 400, 500, 600, 700, 
				800, 900, 1000, 1100, 1200, 1300, 1400};
		for (int i = 1; i <= 28; i++)
		{
			double[] bounds = {-100, 100};
			p = new CEC2013(i, problemDimension, bounds);
			problems.add(p);
		}
		*/
		/*
		// CEC 2014
		bias = new double[30];
		for (int i = 1; i <= 30; i++)
		{
			bias[i-1] = i*100;
			double[] bounds = {-100, 100};
			p = new CEC2014(i, problemDimension, bounds);
			problems.add(p);
		}
*/
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
		ExecutorService threadPool = Executors.newFixedThreadPool(nrProc);
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
					if (multiThread)
					{
						AlgorithmRepetitionThread thread = new AlgorithmRepetitionThread(algorithm, problem, i);
						pool.submit(thread);		a.pushParameter("p1",1e-6);
						a.pushParameter("p2",3.0);
					}
					else
					{
						double best = runAlgorithmRepetition(algorithm, problem);
						if (bias != null)
							finalValues[algorithmIndex][i] = best - bias[problemIndex];
						else
							finalValues[algorithmIndex][i] = best;
					}
				}

				if (multiThread)
				{
					for (int j = 0; j < nrRepetitions; j++)
					{
						Future<AlgorithmResult> result = pool.take();
						AlgorithmResult algorithmResult = result.get();
						finalValues[algorithmIndex][algorithmResult.repNr] = algorithmResult.best - bias[problemIndex];
					}
				}

				String mean = TestOptimizerHelper.format(MathUtils.mean(finalValues[algorithmIndex]));
				String std = TestOptimizerHelper.format(MathUtils.std(finalValues[algorithmIndex]));
				System.out.print(mean + " \u00B1 " + std + "\t");
				if (algorithmIndex > 0)
				{			
					double pValue = mannWhitneyUTest.mannWhitneyUTest(finalValues[0], finalValues[algorithmIndex]);
					char w = '=';
					if (pValue < 0.05)
					{
						if (MathUtils.mean(finalValues[0]) < MathUtils.mean(finalValues[algorithmIndex]))
							w = '+';
						else
							w = '-';
					}
					System.out.print(w + "\t");
					
					if (showPValue)
						System.out.print(TestOptimizerHelper.format(pValue) + "\t");
					
					//String w = TableAvgStdStat.compareResults(finalValues[0], finalValues[algorithmIndex], false, 0.05);
					//System.out.print(w + "\t");
				}
				algorithmIndex++;
			}

			System.out.println();
			problemIndex++;
		}
		
		threadPool.shutdownNow();
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

		public AlgorithmRepetitionThread(Algorithm algorithm, Problem problem, int repNr)
		{
			this.algorithm = algorithm;
			this.problem = problem;
			this.repNr = repNr;
		}

		@Override
		public AlgorithmResult call() throws Exception {
			double best = runAlgorithmRepetition(algorithm, problem);
			return new AlgorithmResult(best, repNr);
		}		
	}

	private static double runAlgorithmRepetition(Algorithm algorithm, Problem problem) throws Exception
	{
		long t0, t1;

		t0 = System.currentTimeMillis();
		Vector<Best> bests = algorithm.execute(problem, budgetFactor*problem.getDimension());
		t1 = System.currentTimeMillis();

		int n = bests.size();
		if (debug)
		{
			for (int j = n-1; j < n; j++)
				System.out.println(bests.get(j).getI() + "\t" + bests.get(j).getfBest());
			System.out.println("Elapsed time: " + (long)(t1-t0) + " ms.");
		}
		
		return bests.get(n-1).getfBest();
	}
}
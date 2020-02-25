package test;

import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import utils.MathUtils;
import algorithms.jDE;
import algorithms.interfaces.Algorithm;
import algorithms.interfaces.Problem;
import algorithms.utils.Best;

public class TestConstraintOptimization
{
	// number of repetitions and budget factor
	private static int nrRepetitions = 8;
	private static int budget = (int)250000;

	private static boolean debug = false;
	private static boolean showAvgStd = false;
	private static boolean showPValue = false;
	private static boolean multiThread = false;

	public static void main(String[] args) throws Exception
	{	
		// run the optimization algorithm
		Vector<Algorithm> algorithms = new Vector<Algorithm>();
		Vector<Problem> problems = new Vector<Problem>();

		Algorithm a;
		
//		a = new EPSDE_LS();
//		a.pushParameter("p0",50.0);
//		a.pushParameter("p1",200.0);
//		a.pushParameter("p2",1000.0);
//		a.pushParameter("p3",1.0);
//		algorithms.add(a);
		
		a = new jDE();
		a.pushParameter("p0", 100.0);
		a.pushParameter("p1", 0.1);
		a.pushParameter("p2", 1.0);
		a.pushParameter("p3", 0.1);
		a.pushParameter("p4", 0.1);
		algorithms.add(a);
		
		TestConstraintOptimizationHelper testConstraintOptimizationHelper = new TestConstraintOptimizationHelper();
		
		problems.add(testConstraintOptimizationHelper.g06);
		problems.add(testConstraintOptimizationHelper.g07);
		problems.add(testConstraintOptimizationHelper.g09);
		problems.add(testConstraintOptimizationHelper.g10);
		problems.add(testConstraintOptimizationHelper.weldedBeam);
		problems.add(testConstraintOptimizationHelper.speedReducer);
		problems.add(testConstraintOptimizationHelper.treeBarTruss);
		problems.add(testConstraintOptimizationHelper.pressureVessel);
		problems.add(testConstraintOptimizationHelper.spring);
		
		//testConstraintOptimizationHelper.test();
		
		int algorithmIndex = 0;
		if (showAvgStd)
		{
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
		}
		
		MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
		
		int nrProc = Runtime.getRuntime().availableProcessors();
		ExecutorService threadPool = Executors.newFixedThreadPool(nrProc);
		CompletionService<AlgorithmResult> pool = new ExecutorCompletionService<AlgorithmResult>(threadPool);
		
		int problemIndex = 0;
		for (Problem problem: problems)
		{
			System.out.print("f" + (problemIndex+1) + "\t");

			double[][][] finalBestPts = new double[algorithms.size()][nrRepetitions][problem.getDimension()];
			double[][] finalValues = new double[algorithms.size()][nrRepetitions];

			algorithmIndex = 0;
			for (Algorithm algorithm : algorithms)
			{
				for (int i = 0; i < nrRepetitions; i++)
				{
					if (multiThread)
					{
						AlgorithmRepetitionThread thread = new AlgorithmRepetitionThread(algorithm, problem, i);
						pool.submit(thread);
					}
					else
					{
						double best = runAlgorithmRepetition(algorithm, problem);
						finalValues[algorithmIndex][i] = best;
						finalBestPts[algorithmIndex][i] = algorithm.getFinalBest();
					}
				}
				
				if (multiThread)
				{
					for (int j = 0; j < nrRepetitions; j++)
					{
						Future<AlgorithmResult> result = pool.take();
						AlgorithmResult algorithmResult = result.get();
						finalValues[algorithmIndex][algorithmResult.repNr] = algorithmResult.bestF;
						finalBestPts[algorithmIndex][algorithmResult.repNr] = algorithmResult.bestPt;
					}
				}

				if (showAvgStd)
				{
					String mean = TestOptimizerHelper.format(MathUtils.mean(finalValues[algorithmIndex]));
					String std = TestOptimizerHelper.format(MathUtils.std(finalValues[algorithmIndex]));
					System.out.print(mean + " \u00B1 " + std + "\t");
				}
				else
				{
					String algName = algorithm.getClass().getSimpleName();
					System.out.println();
					System.out.println(algName);
					
					double[] bestFitnesses = finalValues[algorithmIndex];
					
					MathUtils.print(bestFitnesses);
					
					double min = bestFitnesses[0];
					int mini = 0;
					for (int i = 1; i < nrRepetitions; i++)
					{
						if (bestFitnesses[i] < min)
						{
							min = bestFitnesses[i];
							mini = i;
						}
					}

					double[] best = finalBestPts[algorithmIndex][mini];
						
					System.out.println(problem.f(best) + " <- " + MathUtils.toString(best));
					TestConstraintOptimizationHelper.showViolation = true;
					problem.f(best);
					TestConstraintOptimizationHelper.showViolation = false;
				}
				
				if (showAvgStd)
				{
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
					}
				}
				algorithmIndex++;
			}
			
			System.out.println();
			problemIndex++;
		}
	}
	
	private static class AlgorithmResult
	{
		double bestF;
		double[] bestPt;
		int repNr;
		
		public AlgorithmResult(double bestF, double[] bestPt, int repNr) {
			super();
			this.bestF = bestF;
			this.bestPt = bestPt;
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
			return new AlgorithmResult(best, algorithm.getFinalBest(), repNr);
		}		
	}
	
	private static double runAlgorithmRepetition(Algorithm algorithm, Problem problem) throws Exception
	{
		long t0, t1;
		
		t0 = System.currentTimeMillis();
		Vector<Best> bests = algorithm.execute(problem, budget);
		t1 = System.currentTimeMillis();

		int n = bests.size();
		if (debug)
		{
			for (int j = n-1; j < n; j++)
				System.out.println(bests.get(j).getI() + "\t" + bests.get(j).getfBest() + " " + MathUtils.toString(algorithm.getFinalBest()));
			System.out.println("Elapsed time: " + (long)(t1-t0) + " ms.");
		}

		return bests.get(n-1).getfBest();
	}
}
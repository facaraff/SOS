package mains.test;

import java.util.Vector;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import benchmarks.BBOB2010;



import algorithms.paperReviews.*;

import utils.MatLab;
import utils.RunAndStore;
//import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;



public class TestBBOB2010
{
	// number of repetitions and budget factor
	static int nrRepetitions = 30;
	static int budgetFactor = 5000;
	static int problemDimension = 100;

	static boolean debug = false;
	static boolean showPValue = false;

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

		a = new HyperSPAMnoSnoR();
		algorithms.add(a);
			
	    a = new HyperSPAMnoR();
	    algorithms.add(a);
	  		
	  	a = new HyperSPAMnoS();
	  	algorithms.add(a);
		
		bias = null;
		
		
	
		
//		// BBOB 2010
		bias = new double[] {79.48, -209.88, -462.09, -462.09, -9.21, 35.9, 
								92.94, 149.15, 123.83, -54.94, 76.27, -621.11, 
								29.97, -52.35, 1000.0, 71.35, -16.94, -16.94,
								-102.55, -546.5, 40.78, -1000.0, 6.87, 102.61};
		
		for (int i = 0; i < 24; i++)
		{
			p = new BBOB2010(problemDimension,i+1);
			problems.add(p);
		}
		

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
					double best = runAlgorithmRepetition(algorithm, problem);
					if (bias != null)
						finalValues[algorithmIndex][i] = best - bias[problemIndex];
					else
						finalValues[algorithmIndex][i] = best;
				}


				String mean = RunAndStore.format(MatLab.mean(finalValues[algorithmIndex]));
				String std = RunAndStore.format(MatLab.std(finalValues[algorithmIndex]));
				System.out.print(mean + " \u00b1 " + std + "\t");
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
						System.out.print(RunAndStore.format(pValue) + "\t");
				}
				algorithmIndex++;
			}

			System.out.println();
			problemIndex++;
		}
		

	}





	private static double runAlgorithmRepetition(Algorithm algorithm, Problem problem) throws Exception
	{
		//long t0, t1;

		//t0 = System.currentTimeMillis();
		FTrend FT = algorithm.execute(problem, budgetFactor*problem.getDimension());
		//t1 = System.currentTimeMillis();
		return FT.getLastF();
	}
}
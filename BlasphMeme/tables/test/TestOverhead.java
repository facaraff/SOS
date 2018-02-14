package test;

import static algorithms.utils.AlgorithmUtils.generateRandomSolution;

import java.util.Vector;

import algorithms.CMAES;
import algorithms.SEP_CMAES;
import algorithms.interfaces.Algorithm;
import algorithms.interfaces.Problem;
import darwin.BenchmarkBBOB2010;
import darwin.BenchmarkCEC2005;
import darwin.BenchmarkCEC2013;

public class TestOverhead
{
	public static class BBOB10_F3 extends Problem 
	{
		public BBOB10_F3(int dimension, double[] bounds) { super(dimension, bounds); }
		public double f(double[] x) { return BenchmarkBBOB2010.f3(x); }
	}
	
	public static class CEC05_F3 extends Problem 
	{
		public CEC05_F3(int dimension, double[] bounds) { super(dimension, bounds); }
		public double f(double[] x) { return BenchmarkCEC2005.f3(x); }
	}
	
	public static class CEC2013_F1 extends Problem 
	{
		public CEC2013_F1(int dimension, double[] bounds) { super(dimension, bounds); }
		public double f(double[] x) { return BenchmarkCEC2013.f1(x); }
	}
	
	public static class CEC2013_F3 extends Problem 
	{
		public CEC2013_F3(int dimension, double[] bounds) { super(dimension, bounds); }
		public double f(double[] x) { return BenchmarkCEC2013.f3(x); }
	}
	
	public static class CEC2013_F14 extends Problem 
	{
		public CEC2013_F14(int dimension, double[] bounds) { super(dimension, bounds); }
		public double f(double[] x) { return BenchmarkCEC2013.f14(x); }
	}

	public static void main(String[] args) throws Exception
	{	
		// the dimensions we want to test
		int [] sizes = {2, 10, 30, 50};
		
		// the algorithms whose overhead must be compared
		Vector<Algorithm> algorithms = new Vector<Algorithm>();

		Algorithm alg;
		
		/*
		alg = new EPSDE_LS();
		alg.pushParameter("p0",50.0);
		alg.pushParameter("p1",200.0);
		alg.pushParameter("p2",1000.0);
		alg.pushParameter("p3",1.0);
		algorithms.add(alg);
		
		alg = new EPSDE_LS();
		alg.pushParameter("p0",50.0);
		alg.pushParameter("p1",200.0);
		alg.pushParameter("p2",1000.0);
		alg.pushParameter("p3",0.0);
		algorithms.add(alg);

		alg = new SADE();
		alg.pushParameter("p0", 50.0);
		alg.pushParameter("p1", 4.0);
		alg.pushParameter("p2", 20.0);
		algorithms.add(alg);
		
		alg = new MDE_pBX();
		alg.pushParameter("p0", 100.0);
		alg.pushParameter("p1", 0.15);
		algorithms.add(alg);
		
		alg = new JADE();
		alg.pushParameter("p0",60.0);
		alg.pushParameter("p1",0.05);
		alg.pushParameter("p2",0.1); 
		algorithms.add(alg);

		alg = new CLPSO();
		alg.pushParameter("p0", 60.0);
		algorithms.add(alg);

		alg = new CCPSO2();
		alg.pushParameter("p0", 30.0);
		alg.pushParameter("p1", 0.5);
		algorithms.add(alg);
		
		alg = new PMS();
		alg.pushParameter("p0", 0.95);
		alg.pushParameter("p1", 150.0);
		alg.pushParameter("p2", 0.4);
		alg.pushParameter("p3", 10e-5);
		alg.pushParameter("p4", 2.0);
		alg.pushParameter("p5", 0.5);
		algorithms.add(alg);
		*/

		alg = new CMAES();
		algorithms.add(alg);
		
		alg = new SEP_CMAES();
		algorithms.add(alg);

		/*
		alg = new RIS();
		alg.pushParameter("p0", 0.5);
		alg.pushParameter("p1", 0.4);
		alg.pushParameter("p2", 0.000001);
		algorithms.add(alg);
		
		alg = new ThreeSome();
		alg.pushParameter("p0", 0.95);
		alg.pushParameter("p1", 4.0);
		alg.pushParameter("p2", 0.1);
		alg.pushParameter("p3", 150.0);
		alg.pushParameter("p4", 0.4);
		algorithms.add(alg);

		alg = new MALSChSSW();
		alg.pushParameter("p0", 100.0);
		alg.pushParameter("p1", 3.0);
		alg.pushParameter("p2", 0.5);
		alg.pushParameter("p3", 0.125);
		alg.pushParameter("p4", 0.8);
		alg.pushParameter("p5", 500.0);
		alg.pushParameter("p6", 0.0);
		algorithms.add(alg);

		alg = new MALSChCMA();
		alg.pushParameter("p0",60.0);
		alg.pushParameter("p1",3.0);
		alg.pushParameter("p2",0.5);
		alg.pushParameter("p3",0.125);
		alg.pushParameter("p4",0.5);
		alg.pushParameter("p5",100.0);
		alg.pushParameter("p6",1E-8);
		algorithms.add(alg);
		*/

		// fixed budget
		int budget = 10000;
		// number of runs per algorithm
		int numOfRuns = 10;

		int numberOfSizes = sizes.length;
		int numOfAlgs = algorithms.size();

		long[][] timesEval = new long[numberOfSizes][numOfRuns];
		long[][][] times = new long[numberOfSizes][numOfAlgs][numOfRuns];

		//double[] bounds = {-5, 5};
		double[] bounds = {-100, 100};

		// dummy runs (needed to fix preallocation time)
		{
			//Problem problem = new BBOB10_F3(sizes[0], bounds);
			//Problem problem = new CEC05_F3(sizes[0], bounds);
			Problem problem = new CEC2013_F1(sizes[0], bounds);

			for (int j = 0; j < numOfAlgs; j++)
			{
				for (int k = 0; k < numOfRuns+1; k++)
					algorithms.get(j).execute(problem, budget);
			}
		}
		
		for (int i = 0; i < numberOfSizes; i++)
		{
			//Problem problem = new BBOB10_F3(sizes[i], bounds);
			//Problem problem = new CEC05_F3(sizes[i], bounds);
			Problem problem = new CEC2013_F1(sizes[i], bounds);
			
			for (int j = 0; j < numOfAlgs; j++)
			{
				for (int k = 0; k < numOfRuns+1; k++)
				{
					long t0 = System.currentTimeMillis();

					algorithms.get(j).execute(problem, budget);

					// we need an extra run to preallocate classes
					if (k > 0)
						times[i][j][k-1] = System.currentTimeMillis()-t0;
				}
			}
		}

		for (int i = 0; i < numberOfSizes; i++)
		{
			//Problem problem = new BBOB10_F3(sizes[i], bounds);
			//Problem problem = new CEC05_F3(sizes[i], bounds);
			Problem problem = new CEC2013_F1(sizes[i], bounds);
			
			for (int j = 0; j < numOfRuns; j++)
			{
				double[] x = generateRandomSolution(problem.getBounds(), problem.getDimension());
				long t0 = System.currentTimeMillis();
				for (int k = 0; k < budget; k++)
					problem.f(x);
				timesEval[i][j] = System.currentTimeMillis()-t0;
			}
		}

		for (int i = 0; i < numberOfSizes; i++)
		{
			System.out.print(sizes[i]);
			if (i < numberOfSizes-1)
				System.out.print("\t");
		}
		System.out.println();
		for (int j = 0; j < numOfAlgs; j++)
		{
			System.out.print(algorithms.get(j).getClass().getSimpleName());
			if (j < numOfAlgs-1)
				System.out.print("\t");
		}
		System.out.println();
		System.out.println("*****************************");

		for (int i = 0; i < numberOfSizes; i++)
		{
			for (int j = 0; j < numOfRuns-1; j++)
				System.out.print(timesEval[i][j] + "\t");
			System.out.println(timesEval[i][numOfRuns-1]);
		}
		System.out.println("*****************************");
				
		for (int i = 0; i < numberOfSizes; i++)
		{
			for (int j = 0; j < numOfAlgs; j++)
			{
				for (int k = 0; k < numOfRuns-1; k++)
					System.out.print(times[i][j][k] + "\t");
				System.out.println(times[i][j][numOfRuns-1]);
			}

			System.out.println("*****************************");
		}
	}
}
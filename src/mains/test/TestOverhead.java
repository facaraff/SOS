 package mains.test;

 import static utils.algorithms.Misc.generateRandomSolution;
 import static utils.MatLab.mean;
 import static utils.RunAndStore.toText;
 import static utils.RunAndStore.slash;
 import static java.time.Instant.now;
 
import java.util.Vector;

import algorithms.compact.*;
import algorithms.*;
import algorithms.singleSolution.*;
//import benchmarks.problemsImplementation.CEC2014.CEC2014TestFunc;
import static benchmarks.problemsImplementation.BaseFunctions.Sphere;
import interfaces.Algorithm;
import interfaces.Problem;

public class TestOverhead
{
//	public static class P extends Problem 
//	{
//		private Problem p = null;
//		public P(int dimension, double[] bounds) throws Exception
//		{ 
//			super(dimension, bounds); 
//			p = new CEC2014TestFunc(dimension,1);
//		}
//		
//		public double f(double[] x) { return p.f(x); }
//	}

	public static void main(String[] args) throws Exception
	{	
		// the dimensionsNr we want to test
		int [] dimensions = {2, 10, 30, 50, 100, 500, 1000};
		
		// the algorithms whose overhead must be compared
		Vector<Algorithm> algorithms = new Vector<Algorithm>();

		Algorithm alg;
		

		
//		alg = new RIS();
//		alg.setParameter("p0", 0.5);
//		alg.setParameter("p1", 0.4);
//		alg.setParameter("p2", 0.000001);
//		algorithms.add(alg);
//		
//		alg = new ThreeSome();
//		alg.setParameter("p0", 0.95);
//		alg.setParameter("p1", 4.0);
//		alg.setParameter("p2", 0.1);
//		alg.setParameter("p3", 150.0);
//		alg.setParameter("p4", 0.4);
//		algorithms.add(alg);

		
		alg = new RIcDE_light();
		alg.setParameter("p0", 0.95);
		alg.setParameter("p1", 0.25);
		algorithms.add(alg);
			
		alg = new cDE_exp_light();
		alg.setParameter("p0", 300.0);
		alg.setParameter("p1", 0.25);
		alg.setParameter("p2", 0.5);
		alg.setParameter("p3", 3.0);
		alg.setParameter("p4", 1.0);
		alg.setParameter("p5", 1.0);
		algorithms.add(alg);	
		
		
		alg = new CMAES_11();
		alg.setParameter("p0",(2.0/11.0));
		alg.setParameter("p1",(1.0/12.0));
		alg.setParameter("p2",0.44);
		alg.setParameter("p3",1.0);
		algorithms.add(alg);
		
		
		alg = new NonUniformSA();
		alg.setParameter("p0",5.0);
		alg.setParameter("p1",0.9);
		alg.setParameter("p2",3.0);
		alg.setParameter("p3",10.0);
		algorithms.add(alg);
		
		
		alg = new PMS();
		alg.setParameter("p0",0.95);
		alg.setParameter("p1",150.0);
		alg.setParameter("p2",0.4);
		alg.setParameter("p3",10e-5);
		alg.setParameter("p4",3.0);
		alg.setParameter("p5",0.5);
		algorithms.add(alg);
		
		alg = new JADE();
		alg.setParameter("p0",60.0);
		alg.setParameter("p1",0.05);
		alg.setParameter("p2",0.1);
		algorithms.add(alg);
		
		alg = new CLPSO();
		alg.setParameter("p0",60.0);
		algorithms.add(alg);
		
	    alg = new EPSDE_LS();
	    alg.setParameter("p0",50.0);
	    alg.setParameter("p1", 200.0);
	    alg.setParameter("p2", 1000.0);
	    alg.setParameter("p3", 1.0);
	    algorithms.add(alg);
		
		
		// fixed budget
		int budget = 100000;
		// number of runs per algorithm
		int numOfRuns = 30;

		int dimensionsNr = dimensions.length;
		int numOfAlgs = algorithms.size();

		long[][] timesEval = new long[dimensionsNr][numOfRuns];
		double[] timesEvalAVG = new double[dimensionsNr];
		long[][][] times = new long[dimensionsNr][numOfAlgs][numOfRuns];
		double[][] timesAVG = new double[dimensionsNr][numOfAlgs];
		
		double[][] overhead = new double[dimensionsNr][numOfAlgs];

		double[] bounds = {-100, 100};

		// dummy runs (needed to fix preallocation time)
		{
//			Problem problem = new CEC2014_F1(dimensions[0], bounds);
			Problem problem = new Sphere (dimensions[0], bounds);

			for (int j = 0; j < numOfAlgs; j++)
			{
				for (int k = 0; k < numOfRuns+1; k++)
					algorithms.get(j).execute(problem, budget);
			}
		}
		
		for (int i = 0; i < dimensionsNr; i++)
		{
			Problem problem = new Sphere(dimensions[i], bounds);
			
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

		for (int i = 0; i < dimensionsNr; i++)
		{
			Problem problem = new Sphere(dimensions[i], bounds);
			
			for (int j = 0; j < numOfRuns; j++)
			{
				double[] x = generateRandomSolution(problem.getBounds(), problem.getDimension());
				long t0 = System.currentTimeMillis();
				for (int k = 0; k < budget; k++)
					problem.f(x);
				timesEval[i][j] = System.currentTimeMillis()-t0;
			}
		}
		
		String report = "Perfomed: "+now()+"\nNumber of runs = "+numOfRuns+"\nBdget (FEs) = "+budget+"\n\n";
		
		report += "*** Function evaluation times ***\n";

		for (int i = 0; i < dimensionsNr; i++)
		{
			report += dimensions[i]+"D:\t";
			for (int j = 0; j < numOfRuns-1; j++)
				report +=timesEval[i][j] + "\t";
			timesEvalAVG[i] = mean(timesEval[i]);
			report +=timesEval[i][numOfRuns-1]+"\t==> AVG: "+timesEvalAVG[i]+"\n\n";
		}
		
		report += "*** Algorithms evaluation times ***\n";
		
		for (int i = 0; i < dimensionsNr; i++)
		{
			report += "--- "+dimensions[i]+"D ---\n";
					
			for (int j = 0; j < numOfAlgs; j++)
			{
				report += algorithms.get(j).getClass().getSimpleName()+":"+"\t";
				
				for (int k = 0; k < numOfRuns-1; k++)
					report += times[i][j][k] + "\t";
				timesAVG[i][j] = mean(times[i][j]);
				report += times[i][j][numOfRuns-1]+"\t==> AVG: "+timesAVG[i][j]+"\n";
			}
		}
		
		report += "\n***  AVG Algorithmich Overhead over increasing dimension values***\n";
		
		
		
		for (int j = 0; j < numOfAlgs; j++)
		{
			report += algorithms.get(j).getClass().getSimpleName()+":"+"\t";
					
			for (int i = 0; i < dimensionsNr-1; i++)
			{
				overhead[i][j] = timesAVG[i][j]-timesEvalAVG[i];
				report += overhead[i][j] + "\t";
			}
			overhead[dimensionsNr-1][j] = timesAVG[dimensionsNr-1][j]-timesEvalAVG[dimensionsNr-1];
			report += overhead[dimensionsNr-1][j] + "\n";
		}
		
		report +="\n****************************************\n\n";
		System.out.println(report);
		toText("results"+slash()+"overhead",report);
		
		
		
	}
}
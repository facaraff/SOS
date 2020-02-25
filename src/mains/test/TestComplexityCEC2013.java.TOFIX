package test;

import java.text.DecimalFormat;
import java.util.Vector;

import test.TestOverhead.CEC2013_F14;
import test.TestOverhead.CEC2013_F3;
import utils.MathUtils;
import algorithms.CMAES_RIS;
import algorithms.interfaces.Algorithm;
import algorithms.interfaces.Problem;
import algorithms.utils.AlgorithmUtils;

public class TestComplexityCEC2013
{
	public static void main(String[] args) throws Exception
	{	
		// the dimensions we want to test
		int[] sizes = {10, 30, 50};

		// the algorithms whose overhead must be compared
		Vector<Algorithm> algorithms = new Vector<Algorithm>();
		
		Algorithm alg;
		
		alg = new CMAES_RIS();
		alg.pushParameter("p0", 0.5);
		alg.pushParameter("p1", 0.4);
		alg.pushParameter("p2", 0.000001);
		algorithms.add(alg);
		
		// fixed budget
		int budget = 200000;
		// number of runs per algorithm
		int numOfRuns = 5;
		
		int numberOfSizes = sizes.length;
		int numOfAlgs = algorithms.size();
		
		double T0;
		double[] T1 = new double[numberOfSizes];
		double[][] T2 = new double[numberOfSizes][numOfAlgs];
		
		long t0, t1;
		@SuppressWarnings("unused")
		double x,y;

		t0 = System.currentTimeMillis();
		for (int i=0; i < 1000000; i++)
		{
			x = 5.55;
			x = x + x;
			x = x/2;
			x = x*x;
			x = Math.sqrt(x);
			x = Math.log(x);
			x = Math.exp(x); 
			y = x/x;
		}
		t1 = System.currentTimeMillis();
		
		T0 = (double)(t1-t0);
		
		double[][][] times = new double[numberOfSizes][numOfAlgs][numOfRuns];

		double[] bounds = {-100, 100};

		DecimalFormat df = new DecimalFormat("#.#");
		
		for (int i = 0; i < numberOfSizes; i++)
		{
			System.out.println("D = " + sizes[i]);
			
			t0 = System.currentTimeMillis();
			Problem problem = new CEC2013_F14(sizes[i], bounds);
			double[] tmp = AlgorithmUtils.generateRandomSolution(problem.getBounds(), sizes[i]);
			for (int j=0; j < budget; j++)
				problem.f(tmp);
			t1 = System.currentTimeMillis();

			T1[i] = (double)(t1-t0);
			
			for (int j = 0; j < numOfAlgs; j++)
			{
				System.out.println(algorithms.get(j).getClass().getSimpleName());
				
				for (int k = 0; k < numOfRuns+1; k++)
				{
					problem = new CEC2013_F3(sizes[i], bounds);
					
					t0 = System.currentTimeMillis();
					algorithms.get(j).execute(problem, budget);
					t1 = System.currentTimeMillis();
					
					// we need an extra run to preallocate classes
					if (k > 0)
					{
						times[i][j][k-1] = (double)(t1-t0);
						System.out.println("T2[" + (k-1) + "] = " + times[i][j][k-1] + " [ms]");
					}
				}
				System.out.println();
				
				T2[i][j] = MathUtils.mean(times[i][j]);
				
				System.out.println("T0 = " + T0 + " [ms]");
				System.out.println("T1 = " + T1[i] + " [ms]");
				System.out.println("T2 = " + T2[i][j] + " [ms]");
				System.out.println("(T2-T1)/T0 = " + df.format((T2[i][j]-T1[i])/T0));
			}
			
			System.out.println();
		}
		
		for (int j = 0; j < numOfAlgs; j++)
		{
			System.out.println(algorithms.get(j).getClass().getSimpleName());
			System.out.println();
			
			System.out.println("\\begin{table}");
			System.out.println("\\caption{Computational Complexity}\\label{tab:complexity}");
			System.out.println("\\begin{center}");
			System.out.println("\\begin{tabular}{|c|c|c|c|c|}");
			System.out.println("\\hline");
			System.out.println("& $\\mathbf{T0}$ & $\\mathbf{T1}$ & $\\mathbf{\\widehat{T2}}$ & $\\mathbf{(\\widehat{T2}-T1)/T0}$\\\\");
			System.out.println("\\hline");
			for (int i = 0; i < sizes.length; i++)
			{
				if ((i+1) == Math.ceil(sizes.length/2.0))
				{
					System.out.println("\\cline{0-0} \\cline{3-5}");
					System.out.println("$\\mathbf{D = " + sizes[i] + "}$ & $" + T0 + "$ & $" + T1[i] + "$ & $" + T2[i][j] + "$ & $" + df.format((T2[i][j]-T1[i])/T0) + "$\\\\");
					System.out.println("\\cline{0-0} \\cline{3-5}");
				}
				else
					System.out.println("$\\mathbf{D = " + sizes[i] + "}$ & & $" + T1[i] + "$ & $" + T2[i][j] + "$ & $" + df.format((T2[i][j]-T1[i])/T0) + "$\\\\");
			}
			System.out.println("\\hline");
			System.out.println("\\end{tabular}");
			System.out.println("\\end{center}");
			System.out.println("\\end{table}");
		}
	}
}
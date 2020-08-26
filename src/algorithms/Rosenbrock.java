package algorithms;


import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Corrections.torus;

import static utils.MatLab.eye;
import static utils.MatLab.subtract;
import static utils.MatLab.zeros;
import static utils.MatLab.min;
import static utils.MatLab.abs;

import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

public class Rosenbrock extends Algorithm {
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double eps = getParameter("p0").doubleValue();		// 10e-5
		double alpha = getParameter("p1").doubleValue();	// 2  (3-->original paper value)
		double beta = getParameter("p2").doubleValue();	// 0.5
	
		FTrend FT = new FTrend();
		int n = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		double[][] xi = eye(n);
		double[][] A = zeros(n);
		double[] lambda = new double[n];
		double[] xCurrent = new double[n];
		double[] t = new double[n];
		double[] xk = new double[n];
		double[] d = new double[n];
		double[] x = new double[n];
		double yFirstFirst;
		if (initialSolution != null)
		{
			x = initialSolution;
			yFirstFirst = initialFitness;
		}
		else
		{
			x = generateRandomSolution(bounds, n);
			yFirstFirst = problem.f(x);
		}
		
		FT.add(0, yFirstFirst);
		double yFirst = yFirstFirst;
		double yBest = yFirst;
		double yCurrent;
		for (int i=0; i<n; i++)
		{
			d[i] = 0.1;
			xk[i] = x[i];
		}
	
		boolean restart = true;
		double mini; double div;
		int iter = 1;

		do
		{
			yBest = yFirstFirst;
			do
			{
				yFirst = yBest;
				for (int i = 0; (i < n) && (iter < maxEvaluations);i++)
				{
					for (int j=0;j<n;j++)
						 xCurrent[j]= xk[j]+d[i]*xi[i][j];
					xCurrent = torus(xCurrent, bounds);
					yCurrent = problem.f(xCurrent);
					iter++;

		            if (yCurrent < yBest)
		            {
		            	lambda[i] += d[i];
		            	d[i] *= alpha;
		            	yBest = yCurrent;
		            	for (int j=0;j<n;j++)
		            		xk[j] = xCurrent[j];
		            }
		            else
		            	d[i] *= -beta;
				}
				
				if (iter%n == 0)
					FT.add(iter, yBest);
			}
			while ((yBest < yFirst) && (iter < maxEvaluations));
			
			mini = min(abs(d));
			restart = mini>eps;
			
			if ((yBest < yFirstFirst) && (iter < maxEvaluations))
			{ 
				mini = min(abs(subtract(xk,x)));
				restart = restart || (mini > eps);
				
				if (restart)
				{ 
					for (int i=0;i<n;i++)
						A[n-1][i] = lambda[n-1]*xi[n-1][i];
					for (int k=n-2; k>=0;k--)
					{
						for (int i=0;i<n;i++)
							A[k][i] = A[k+1][i] + lambda[i]*xi[k][i];
					}

					t[n-1] = lambda[n-1]*lambda[n-1];

					for (int i=n-2; i>=0;i--)
						t[i] = t[i+1] + lambda[i]*lambda[i];
					
					for (int i=n-1;i>0;i--)
					{
						div = Math.sqrt(t[i-1]*t[i]);
						if (div != 0)
							for (int j=0;j<n;j++)
								xi[i][j] = (lambda[i-1]*A[i][j]-xi[i-1][j]*t[i])/div;
					}
					div = Math.sqrt(t[0]);
					for (int i=0; i<n;i++)
					{
						if (div != 0)
							xi[0][i] = A[0][i]/div;	
						x[i] = xk[i];
						lambda[i] = 0;
						d[i] = 0.1;
					}
					yFirstFirst = yBest;
				}
			}
		}
		while (iter < maxEvaluations);
		
		finalBest = xCurrent;
		FT.add(iter, yBest);
	
		return FT;
	}
}

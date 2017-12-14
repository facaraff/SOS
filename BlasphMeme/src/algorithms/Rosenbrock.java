package algorithms;


import static algorithms.utils.AlgorithmUtils.generateRandomSolution;
import static algorithms.utils.AlgorithmUtils.saturateToro;

import static utils.MatrixUtils.eye;
import static utils.MatrixUtils.subtract;
import static utils.MatrixUtils.zeros;
import static utils.MathUtils.min;
import static utils.MathUtils.abs;

import java.util.Vector;
import algorithms.interfaces.Algorithm;
import algorithms.interfaces.Problem;
import algorithms.utils.Best;

public class Rosenbrock extends Algorithm {
	
	@Override
	public Vector<Best> execute(Problem problem, int maxEvaluations) throws Exception
	{
		double eps = pullParameter("p0").doubleValue();		// 10e-5
		double alpha = pullParameter("p1").doubleValue();	// 2  (3-->original paper value)
		double beta = pullParameter("p2").doubleValue();	// 0.5
	
		Vector<Best> bests = new Vector<Best>();
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
		
		bests.add(new Best(0, yFirstFirst));
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
					xCurrent = saturateToro(xCurrent, bounds);
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
					bests.add(new Best(iter, yBest));
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
		bests.add(new Best(iter, yBest));
	
		return bests;
	}
}
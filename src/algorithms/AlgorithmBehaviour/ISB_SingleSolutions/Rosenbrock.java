package algorithms.AlgorithmBehaviour.ISB_SingleSolutions;


import static utils.algorithms.operators.ISBOp.generateRandomSolution;
import static utils.algorithms.Misc.cloneSolution;

import static utils.MatLab.eye;
import static utils.MatLab.subtract;
import static utils.MatLab.zeros;
import static utils.MatLab.min;
import static utils.MatLab.abs;

import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.algorithms.Counter;

public class Rosenbrock extends AlgorithmBias {
	
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
		

		String FullName = getFullName("RM"+this.correction,problem); 
		Counter PRGCounter = new Counter(0);
		createFile(FullName);
		
		int prevID = -1;
		int newID = 0;
		writeHeader("eps "+eps+" alpha "+alpha+" beta "+beta, problem);
		
		int iter = 0;
		String line = new String();
		
		if (initialSolution != null)
		{
			x = initialSolution;
			yFirstFirst = initialFitness;
		}
		else
		{
			x = generateRandomSolution(bounds, n, PRGCounter);
			yFirstFirst = problem.f(x);
			
			iter++; newID++;
			
			line =""+newID+" "+formatter(yFirstFirst)+" "+iter+" "+prevID;
			for(int k = 0; k < n; k++)
				line+=" "+formatter(x[k]);
			line+="\n";
			bw.write(line);
			line = null;
			line = new String();
			prevID = newID;
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
		
		double[] prevCurrent = cloneSolution(x);


		
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
					
					//xCurrent = toro(xCurrent, bounds);
					xCurrent = correct(xCurrent,prevCurrent, bounds, PRGCounter);
					
					yCurrent = problem.f(xCurrent);
					iter++;

		            if (yCurrent <= yBest)
		            {
		            	lambda[i] += d[i];
		            	d[i] *= alpha;
		            	yBest = yCurrent;
		            	for (int j=0;j<n;j++)
		            		xk[j] = xCurrent[j];
		            	
		            	newID++;
		            	
		    			line =""+newID+" "+formatter(yCurrent)+" "+iter+" "+prevID;
		    			for(int k = 0; k < n; k++)
		    				line+=" "+formatter(xCurrent[k]);
		    				
		    			
		    			line+="\n";
		    			bw.write(line);
		    			line = null;
		    			line = new String();
		    			prevID = newID;
		    			
		            }
		            else
		            	d[i] *= -beta;
		            
		            prevCurrent = cloneSolution(xCurrent);
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
		bw.close();
		//wrtiteCorrectionsPercentage(fileName, (double) this.numberOfCorrections/maxEvaluations,"correctionsSingleSol");
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(), "correctionsSingleSol");
		return FT;
	}
}
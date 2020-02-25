/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/
package algorithms.singleSolution;

import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import static utils.MatLab.abs;
import static utils.MatLab.min;
import static utils.MatLab.eye;
import static utils.MatLab.subtract;
import static utils.MatLab.zeros;

import utils.RunAndStore.FTrend;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;

public class PMS extends Algorithm
{	
	int i;

	double[] best; 
	double fBest;

	FTrend FT = new FTrend();

	//int LCounter;
	//int SCounter;
	//int RCounter; 
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		if (initialSolution != null)
			best = initialSolution;
		else
			best = generateRandomSolution(bounds, problemDimension);
		
		fBest = problem.f(best);
		FT.add(0, fBest);
		i = 1;
		

		while (i < maxEvaluations)
		{
			longDistance(maxEvaluations, problem);
			if (RandUtils.random() > 0.5)
				shortDistance(maxEvaluations, problem);
			else
				rosenbrock(maxEvaluations, problem);
		}
		FT.add(i,fBest);
		
		finalBest = best;
		
		return FT;
	}

	//******LONG DISTANCE OPERATOR****//
	private void longDistance(int budget, Problem prob) throws Exception
	{	
		double globalAlpha = getParameter("p0").doubleValue(); //0.95

		int problemDimension = prob.getDimension(); 
		double[][] bounds = prob.getBounds();
		double globalCR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
		double[] solution = new double[problemDimension];
		double fSolution;
		boolean solImproved = false;
		int counter = 0;
		while ((i < budget) && !solImproved && (counter < 0.05*budget))
		{	
//			if(i%problemDimension==0)
//			{
//				FT.add(i,fBest);
//	
//			}

			solution = generateRandomSolution(bounds, problemDimension);
			solution = crossOverExp(best, solution, globalCR);
			fSolution = prob.f(solution);
			i++;
			counter++;

			// best update
			if (fSolution < fBest)
			{
				fBest = fSolution;
				best = solution;
				solImproved = true;
				
				if(i%problemDimension==0)
				{
					FT.add(i,fBest);
		
				}
				
			}
		}
	}
	//*****SHORT DISTANCE OPERATOR*****//
	private void shortDistance(int budget, Problem prob) throws Exception
	{
		int deepLSSteps = getParameter("p1").intValue(); //150;
		double deepLSRadius = getParameter("p2").doubleValue();//0.4;

		int problemDimension = prob.getDimension(); 
		double[][] bounds = prob.getBounds();

		double[] SR = new double[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;

		boolean improve = true;
		int j = 0;
		while ((j < deepLSSteps) && (i < budget))
		{
			double[] Xk = new double[problemDimension];
			double[] Xk_orig = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
			{
				Xk[k] = best[k];
				Xk_orig[k] = best[k];
			}

			double fXk_orig = fBest;

			if (!improve)
			{
				for (int k = 0; k < problemDimension; k++)
					SR[k] = SR[k]/2;
			}

			improve = false;
			int k = 0;
			while ((k < problemDimension) && (i < budget))
			{
//				if(i%problemDimension==0)
//				{
//					FT.add(i,fBest);
//
//				}

				Xk[k] = Xk[k] - SR[k];
				Xk = toro(Xk, bounds);
				double fXk = prob.f(Xk);
				i++;

				// best update
				if (fXk < fBest)
				{
					fBest = fXk;
					for (int n = 0; n < problemDimension; n++)
						best[n] = Xk[n];
					
					if(i%problemDimension==0)
					{
						FT.add(i,fBest);

					}
					
				}


				if (i < budget)
				{
					if (fXk == fXk_orig)
					{
						for (int n = 0; n < problemDimension; n++)
							Xk[n] = Xk_orig[n];
					}
					else
					{
//						if(i%problemDimension==0)
//						{
//							FT.add(i,fBest);;
//						}

						if (fXk > fXk_orig)
						{
							Xk[k] = Xk_orig[k];
							Xk[k] = Xk[k] + 0.5*SR[k];
							Xk = toro(Xk, bounds);
							fXk = prob.f(Xk);
							i++;

							// best update
							if (fXk < fBest)
							{
								fBest = fXk;
								for (int n = 0; n < problemDimension; n++)
									best[n] = Xk[n];
								
								if(i%problemDimension==0)
								{
									FT.add(i,fBest);;
								}

							}

							if (fXk >= fXk_orig)
								Xk[k] = Xk_orig[k];
							else
								improve = true;
						}
						else
							improve = true;
					}
				}

				k++;
			}

			j++;
		}
	}
	//******ROSENBROCK METHOD****//
	private void rosenbrock(int budget, Problem prob) throws Exception
	{
		double eps = getParameter("p3").doubleValue(); //10e-5
		double alpha = getParameter("p4").doubleValue(); // 2  (3-->original paper value)
		double beta = getParameter("p5").doubleValue();// 0.5

		int problemDimension = prob.getDimension(); 
		double[][] bounds = prob.getBounds();

		double[] x = new double[problemDimension];
		double[][] xi = eye(problemDimension);
		double[][] A = zeros(problemDimension);
		double[] lambda = new double[problemDimension];
		double[] xCurrent = new double[problemDimension];
		double[] t = new double[problemDimension];
		double[] xk = new double[problemDimension];
		double[] d = new double[problemDimension];

		double yFirstFirst = fBest;
		double yFirst = yFirstFirst;
		double yCurrent;
		for(int n=0; n<problemDimension; n++)
		{
			d[n] = 0.1;
			xk[n] = best[n];
			x[n] = best[n];
		}

		boolean restart = true;
		boolean firstExternalWhile = true;
		boolean firstInternalWhile;
		double mini; double div;

		while (firstExternalWhile || (restart && i < budget))
		{	
			fBest = yFirstFirst;
			firstInternalWhile = true;
			while (firstInternalWhile || (fBest < yFirst))
			{
				yFirst = fBest;
				for(int j=0;j<problemDimension;j++)
				{
//					if(i%problemDimension==0)
//					{
//						FT.add(i,fBest);
//					}

					for(int n=0;n<problemDimension;n++)
						xCurrent[n]= xk[n]+d[j]*xi[j][n];
					xCurrent = toro(xCurrent, bounds);
					yCurrent = prob.f(xCurrent);
					i++; 
					if(i >= budget )
						break;

					if(yCurrent < fBest)
					{
						lambda[j] = lambda[j] + d[j];
						d[j] = alpha*d[j];
						fBest = yCurrent;
						for(int n=0;n<problemDimension;n++)
						{
							xk[n] = xCurrent[n];
							best[n] = xCurrent[n];
						}
						
						if(i%problemDimension==0)
						{
							FT.add(i,fBest);
						}

					}
					else
						d[j] = -beta*d[j];
					//bests.add(new Best(i, yBest)); original
				}

				if(i >= budget)
					break;
				firstInternalWhile = false;
			}

			mini = min(abs(d));
			restart = mini>eps;

			if(fBest < yFirstFirst)
			{
				mini = min(abs(subtract(xk,x)));
				restart = restart || (mini > eps);
				if(restart)
				{
					for(int n=0;n<problemDimension;n++)
						A[problemDimension-1][n] = lambda[n]*xi[problemDimension-1][n];
					t[problemDimension-1] = lambda[problemDimension-1]*lambda[problemDimension-1];
					for(int k=problemDimension-2; k>0;k--)
					{
						for(int n=0;n<problemDimension;n++)
							A[k][n] = A[k+1][n] + lambda[n]*xi[k][n];
						t[k] = t[k+1] + lambda[k]*lambda[k];
					}	

					for(int k=problemDimension-1;k>1;k--)
					{
						div = Math.sqrt(t[k-1]*t[k]);
						if(div != 0)
							for(int n=0;n<problemDimension;n++)
								xi[k][n] = (lambda[k-1]*A[k][n]-xi[k-1][n]*t[k])/div;
					}
					div = Math.sqrt(t[0]);
					for(int n=0; n<problemDimension;n++)
					{
						if(div != 0)
							xi[0][n] = A[0][n]/div;	
						x[n] = xk[n];
						lambda[n] = 0;
						d[n] = 0.1;
					}
					yFirstFirst = fBest;			
				}
			}

			firstExternalWhile = false;
		}
	}
}

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
package algorithms.inProgress;


import utils.algorithms.Misc;

import interfaces.Algorithm;
import interfaces.Problem;

import static utils.MatLab.multiply;
import static utils.RunAndStore.FTrend;
import static utils.algorithms.Misc.Cov;
import static utils.algorithms.operators.MemesLibrary.intermediatePerturbation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

public class CMS extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
 
		int deepLSSteps = getParameter("p0").intValue(); //150;
		double deepLSRadius = getParameter("p1").doubleValue();//0.4;
		int samples = getParameter("p2").intValue();//30;
		double samplingRadius = getParameter("P3").doubleValue();//0.1
		boolean restart = getParameter("P4").intValue()!=0;// 0---> no restart  1---> restart
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();


		// current FT
		double[] best = new double[problemDimension];
		double fBest;
		
		double[][] population = new double[samples][problemDimension]; 

		int i = 0;
		if (initialSolution != null)
		{
			best = initialSolution;
			fBest = initialFitness;
		}
		else
		{
			best = Misc.generateRandomSolution(bounds, problemDimension);		
			fBest = problem.f(best);
			i++;
		}
	
		
		while (i < maxEvaluations)
		{
		
			double[] SR = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
				SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;
			boolean improve = true;
			int j = 0;
			
			
			double[] temp = new double[problemDimension];
			double fTemp = Double.NaN;
			
					
			//Generate the point to use to centre the hypercube 
			if(restart)
			{
				temp = Misc.generateRandomSolution(bounds, problemDimension);
				fTemp = problem.f(temp);
				i++;
				if (fTemp < fBest)
				{
					fBest = fTemp;
					for (int n = 0; n < problemDimension; n++)
						best[n] = temp[n];
				}
				
			}
			else
			{
				for (int k = 0; k < problemDimension; k++)
					temp[k] = best[k];
				fTemp = fBest;
			}
			
			
			
			//the centre of the hypercube is the first individual 
			for(int k=0; k<problemDimension;k++)
				population[0][k] = temp[k];
			
		
			//Sample the population	
			for(int k=1; k<samples-1 && i<maxEvaluations;k++)
				population[k] = intermediatePerturbation(bounds, temp, samplingRadius);
			
			
			
			//rotate the search radius
			EigenDecomposition E =  new EigenDecomposition(new Array2DRowRealMatrix(Cov(population)));
			population = null;
			double[][] P = E.getV().getData();
			double[] rSR = multiply(P,SR);
			E = null; P = null;
			
			//Execute S along rotated axes
			while ((j < deepLSSteps) && (i < maxEvaluations))
			{
				double[] Xk = new double[problemDimension];
				double[] Xk_orig = new double[problemDimension];
				for (int k = 0; k < problemDimension; k++)
				{
					Xk[k] = temp[k];
					Xk_orig[k] = temp[k];
				}

				if (!improve)
				{
					for (int k = 0; k < problemDimension; k++)
						rSR[k] = rSR[k]/2;
				}
				improve = false;
				int k = 0;
				while ((k < problemDimension) && (i < maxEvaluations))
				{
					Xk[k] = Xk[k] - SR[k];
					Xk = Misc.toro(Xk, bounds);
					double fXk = problem.f(Xk);
					i++;
					// FT update
					if (fXk < fBest)
					{
						fBest = fXk;
						for (int n = 0; n < problemDimension; n++)
							best[n] = Xk[n];
						
						improve = true;
					}
					else if(i<maxEvaluations)
					{
						
						Xk[k] = Xk_orig[k];
						Xk[k] = Xk[k] + 0.5*SR[k];
						Xk = Misc.toro(Xk, bounds);
						fXk = problem.f(Xk);
						//dCounter++;
						i++;
						// FT update
						if (fXk < fBest)
						{
							fBest = fXk;
							for (int n = 0; n < problemDimension; n++)
								best[n] = Xk[n];
							improve = true;
						}
						else
							Xk[k] = Xk_orig[k];
						
						
					}
					
					k++;
				}
				
				j++;
			}
			rSR = null;
		}

		finalBest = best;
		FT.add(i,fBest);
		return FT;
	}
}
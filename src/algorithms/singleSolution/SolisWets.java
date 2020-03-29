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


import static utils.algorithms.Misc.generateRandomSolution;
import static utils.MatLab.subtract;
import static utils.MatLab.sum;

import utils.random.RandUtils;

import interfaces.Algorithm;
import interfaces.Problem;

import static utils.RunAndStore.FTrend;


public class SolisWets extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
 
		double rho = getParameter("p0").doubleValue();//???;
	
		
		FTrend FT =new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// current best
		double[] best = new double[problemDimension];
		double fBest;
		
		int i = 0;
		if (initialSolution != null)
		{
			best = initialSolution;
			fBest = initialFitness;
		}
		else
		{
			best = generateRandomSolution(bounds, problemDimension);		
			fBest = problem.f(best);
			i++;
		}

		
		int numSuccess = 0;
		int numFailed = 0;

		double newfBest;
		double[] dif = new double[problemDimension];
		double[] bestFirst = new double[problemDimension];
		double[] bestSecond = new double[problemDimension];
		double[] bias = new double[problemDimension];

		

		while (i < maxEvaluations )
		{	
			for(int n=0; n < problemDimension; n++)
				dif[n] = RandUtils.gaussian(0,rho);
			bestFirst = sum(sum(best, bias ) , dif );
			bestFirst = correct(bestFirst, bounds);
			newfBest = problem.f(bestFirst);
			i++;
			if(newfBest < fBest)
			{
				fBest = newfBest;
				//bias = sum( multiply(0.2, bias) , multiply( 0.4, sum(dif, bias) )  );
				for(int n=0; n < problemDimension; n++)
				{
					best[n] = bestFirst[n];
					bias[n] = 0.2*bias[n] + 0.4*(dif[n] + bias[n]);
				}
				if(i%problemDimension==0) FT.add(i,fBest);
				numSuccess++;
				numFailed = 0;
			}
			else if(i < maxEvaluations)
			{
				bestSecond = subtract(  subtract(best, bias) , dif  );
				bestSecond = correct(bestSecond, bounds);
				newfBest = problem.f(bestSecond);
				i++;
				if(newfBest < fBest)
				{
					fBest = newfBest;
					//bias = subtract( bias, multiply(0.4, sum(dif, bias)) );
					for(int n=0; n < problemDimension; n++)
					{
						best[n] = bestSecond[n];
						bias[n] = bias[n] - 0.4*(dif[n] + bias[n]);
					}
					if(i%problemDimension==0) FT.add(i,fBest);
					numSuccess++;
					numFailed = 0;
				}
				else
				{
					numFailed++;
					numSuccess = 0;
				}
			}
			
			if(numSuccess > 5)
			{
				rho = rho*2;
				numSuccess = 0;
			}
			else if(numFailed > 3)
			{
				rho = rho/2;
				numFailed = 0;
			}
		}

		finalBest = best;
		FT.add(i,fBest);
		return FT;
	}
}
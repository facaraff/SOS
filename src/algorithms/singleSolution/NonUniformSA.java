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
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.algorithms.Misc.toro;
import utils.RunAndStore.FTrend;





public class NonUniformSA extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{		
		int B = this.getParameter("p0").intValue(); //5
		double alpha = this.getParameter("p1").doubleValue(); //0.9
		int Lk = this.getParameter("p2").intValue(); //3
		int initialSolutions = this.getParameter("p3").intValue(); //10
				
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[] bestPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] oldPt = new double[problemDimension];
		
		double fNew;
		double fOld;
		double fBest;
		double fWorst;
		
		// initialize first point

		int i=0;
		if (initialSolution != null)
		{
			
			bestPt = initialSolution;
			fNew = initialFitness;
		}
		else
		{
			//compute and evaluate initial solution
			bestPt = generateRandomSolution(bounds, problemDimension);
			fNew = problem.f(bestPt);
			i++;
		}
		
		fOld = fNew;
		fBest = fNew;
		fWorst = fNew;
		
		FT.add(i, fNew);
		
		// evaluate initial solutions to set the initial temperature
		for (int j = 0; j < initialSolutions; j++)
		{
			newPt = generateRandomSolution(bounds, problemDimension);
			fNew = problem.f(newPt);

			// update best
			if (fNew < fBest)
			{
				FT.add(j+i+1, fNew);
				for (int k = 0; k < problemDimension; k++)
					bestPt[k] = newPt[k];
				fBest = fNew;
			}
			
			// update worst
			if (fNew > fWorst)
				fWorst = fNew;
		}
		
		for (int k = 0; k < problemDimension; k++)
			oldPt[k] = bestPt[k];
		
		// initialize temperature
		double delt0 = fWorst-fOld;
		double accept0 = 0.9;
		double T0 = -delt0/Math.log(accept0);		
		double tk = T0;

		i = initialSolutions+1;
		
		int generationIndex = 1;
		int totalGenerations = (maxEvaluations-i)/Lk;
		
		while (i < maxEvaluations)
		{
			for (int j = 0; j < Lk && i < maxEvaluations; j++)
			{
				// non-uniform mutation
				for (int k = 0; k < problemDimension; k++)
				{
					//double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)i/maxEvaluations, B));
					double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)generationIndex/totalGenerations, B));
					
					if (RandUtils.random()<0.5)
						newPt[k] = oldPt[k] - (oldPt[k]-problem.getBounds()[k][0])*(1-temp);
					else
						newPt[k] = oldPt[k] + (problem.getBounds()[k][1]-oldPt[k])*(1-temp);
				}
				
				// evaluate fitness
				newPt = toro(newPt, bounds);
				fNew = problem.f(newPt);
				i++;
				
				// update best
				if (fNew < fBest)
				{
					FT.add(i, fNew);
					for (int k = 0; k < problemDimension; k++)
						bestPt[k] = newPt[k];
					fBest = fNew;
				}

				// move to the neighbor point
				if ((fNew <= fOld) || (Math.exp((fOld-fNew)/tk) > RandUtils.random()))
				{
					for (int k = 0; k < problemDimension; k++)
						oldPt[k] = newPt[k];
					fOld = fNew;
				}
			}
			
			generationIndex++;
			
			// update temperature
		    tk = alpha*tk;
		}
		
		finalBest = bestPt;
		
		FT.add(i, fNew);
		
		return FT;
	}
}

//System.out.println(f_parent+" contro "+FT.getLastF());
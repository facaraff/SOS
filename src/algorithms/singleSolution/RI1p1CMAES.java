
/**
Copyright (c) 2019, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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


import algorithms.singleSolution.CMAES_11;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.MatLab.min;
import utils.RunAndStore.FTrend;

import interfaces.Algorithm;
import interfaces.Problem;

public class RI1p1CMAES extends Algorithm
{	
	boolean verbose = false;

	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		int i;

		double[] best; 
		double fBest;

		FTrend FT = new FTrend();
		
		i = 0;
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
		FT.add(i, fBest);
				
		// INITIALISE MEMES//
		
		double globalCR = getParameter("p0").doubleValue(); 
	
		CMAES_11 cma11 = new CMAES_11();
		cma11.setParameter("p0",getParameter("p1").doubleValue()); 
		cma11.setParameter("p1",getParameter("p2").doubleValue()); 
		cma11.setParameter("p2",getParameter("p3").doubleValue()); 
		cma11.setParameter("p3",getParameter("p4").doubleValue()); 
		
		
		double maxB = getParameter("p5").doubleValue();
		
		FTrend ft = null;
		
		
		double[] xTemp;
		double fTemp;
		
		while (i < maxEvaluations)
		{
			xTemp = generateRandomSolution(bounds, problemDimension);
			xTemp = crossOverExp(best, xTemp, globalCR);
			fTemp = problem.f(xTemp);
			i++;
			if(fTemp<fBest)
			{
				fBest = fTemp;
				for(int n=0;n<problemDimension; n++)
					best[n] = xTemp[n];
				FT.add(i, fBest);
			}
			
				
				cma11.setInitialSolution(xTemp);
				cma11.setInitialFitness(fTemp);
				
				int  budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
				
				ft = cma11.execute(problem, budget);
				xTemp = cma11.getFinalBest();
				fTemp = ft.getLastF();
				
				FT.merge(ft, i);
				i+=budget;
				
				if(fTemp<fBest)
				{
					fBest = fTemp;
					for(int n=0;n<problemDimension; n++)
						best[n] = xTemp[n];
				}
			
		}
		
		FT.add(i,fBest);
		finalBest = best;
		return FT;
			
			
	}
}
			
			
						
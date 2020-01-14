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
import static utils.RunAndStore.FTrend;

public class EA1p1OneFifth extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double alpha = this.getParameter("p0").intValue(); //0.9
		int initialSolutions = this.getParameter("p1").intValue(); //10
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[] bestPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];

		double fOld;
		double fNew;
		double fWorst;
		double fBest;

		// initialize first point
		bestPt = generateRandomSolution(bounds, problemDimension);
		fNew = problem.f(bestPt);
		FT.add(0, fNew);
		fBest = fNew;
		fWorst = fNew;
		fOld = fNew;

		// evaluate initial solutions to set the initial temperature
		for (int j = 0; j < initialSolutions; j++)
		{
			newPt = generateRandomSolution(bounds, problemDimension);
			fNew = problem.f(newPt);

			// update best
			if (fNew < fBest)
			{
				FT.add(j+1, fNew);
				fBest = fNew;
			}

			// update worst
			if (fNew <= fOld)
			{
				for (int k = 0; k < problemDimension; k++)
					bestPt[k] = newPt[k];
				fOld = fNew;
			}
			else
				fWorst = fNew;			
		}

		// initialize temperature
		double delt0 = fWorst-fOld;
		double accept0 = 0.9;
		double T0 = -delt0/Math.log(accept0);		
		double tk = T0;

		int i = initialSolutions+1;

		//int generationIndex = 1;
		//int totalGenerations = (maxEvaluations-i);
		//int generationIndex = 1;
		
		while (i < maxEvaluations)
		{

			//basic SA sampling
			newPt = generateRandomSolution(bounds, problemDimension);

			// evaluate fitness
			fNew = problem.f(newPt);
			i++;

			// update best
			if (fNew < fBest)
			{
				FT.add(i, fNew);
				fBest = fNew;
			}

			// move to the neighbor point
			if ((fNew <= fOld) || (Math.exp((fOld-fNew)/tk) > RandUtils.random()))
			{
				for (int k = 0; k < problemDimension; k++)
					bestPt[k] = newPt[k];
				fOld = fNew;
			}


			//generationIndex++;

			// update temperature
			tk = alpha*tk;
		}
		
		finalBest = bestPt;
		
		FT.add(i, fBest);

		return FT;
	}
}

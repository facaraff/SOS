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
package algorithms.abstractAlgorithms;

import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;

import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Three Stage Optimal Memetic Exploration 
 */
public abstract class LongDistanceExploration extends Algorithm
{
	private boolean improved = false;
	private double accuracy = Double.NaN;
	
	protected void setImproved(boolean improved) {this.improved = improved;}
	protected boolean getImproved() {return this.improved;}
	
	protected void setAccuracy(double accuracy) {this.accuracy = accuracy;}
	protected boolean getAccuracy() {return this.improved;}
	
	protected abstract boolean stopCondition(int i, int budget, boolean b, double fx, double accuracy);
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double globalAlpha = getParameter("p0").doubleValue(); //0.95
		

		FTrend FT =new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// temp solution
		double[] solution = new double[problemDimension];
		double fSolution;
		// current best
		double[] best = new double[problemDimension];
		double fBest;

		int i=0;
		if (initialSolution != null)
		{
			solution = initialSolution;
			fSolution = initialFitness;
		}
		else
		{
			solution = generateRandomSolution(bounds, problemDimension);		
			fSolution = problem.f(solution);
			i++;
		}

		best = solution;
		fBest = fSolution;
		FT.add(i, fBest);

		double globalCR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
		
		
		while (stopCondition(i, maxEvaluations, improved, fSolution, accuracy))
		{
			solution = generateRandomSolution(bounds, problemDimension);
			solution = crossOverExp(best, solution, globalCR);
			fSolution = problem.f(solution);
			i++;

			
			// best update
			if (fSolution < fBest)
			{
				fBest = fSolution;
				best = solution;
				setImproved(true);

			}
			if(i%problemDimension == 0)
				FT.add(i,fBest);	
		}

		finalBest = best;
		
		FT.add(i,fBest);

		return FT;
	}
	
}










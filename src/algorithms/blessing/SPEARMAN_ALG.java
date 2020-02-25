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
package algorithms.blessing;



import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;

import utils.algorithms.cmaes.CMAEvolutionStrategy;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

//import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public class SPEARMAN_ALG extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		//System.out.println("budget "+maxEvaluations);	
		FTrend FT =new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		double[] best = new double[problemDimension];
		if (initialSolution != null)
			best = initialSolution;
		else
			best = generateRandomSolution(bounds, problemDimension);
		double fBest = Double.NaN;

		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.parameters.setPopulationSize(100);
		cma.setRepairMatrix(true);				// repair ill-conditioned matrix
		cma.setDimension(problemDimension);		// overwrite some loaded properties
		cma.setInitialX(best);					// in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.2);	// also a mandatory setting 
		cma.options.verbosity = -1;
		cma.options.writeDisplayToFile = -1;

		double[] fitness = cma.init();
		
		double[][] POP = null;		
		// iteration loop
		int j = 0;
		int localBudget = (int)(maxEvaluations*0.2);

		while (j < localBudget)
		{
			double[][] pop = cma.samplePopulation();
			
			for(int i = 0; i < pop.length && j < localBudget; ++i)
			{ 
				pop[i] = toro(pop[i], bounds);
				
				fitness[i] = problem.f(pop[i]);
				
				if (j == 0 || fitness[i] < fBest)
				{
					fBest = fitness[i];
					for (int n = 0; n < problemDimension; n++)
						best[n] = pop[i][n];
					FT.add(j, fBest);
				}
				
				j++;
			}

			// pass fitness array to update search distribution
			cma.updateDistribution(fitness);
			POP = pop;
		}
		
		//double[][] data = new double[10*POP.length][problemDimension];
		//int ind = 0;
		//for(int r=0; r<10*POP.length; r++)
		//{
		//	if(ind == POP.length)
		//	{
		//		ind = 0;
		//		POP = cma.samplePopulation();
		//	}
		//	data[r] = POP[ind];
		//	ind++;		
		//}
			
		double srho = 0;
		SpearmansCorrelation SC = new SpearmansCorrelation();
		boolean discFlag = false;
		double[][] CorrMatrix = SC.computeCorrelationMatrix(POP).getData();
		
		for(int r=0;r<CorrMatrix.length;r++)
		{
			for(int c=r+1;c<CorrMatrix[0].length;c++)
			{	
				if(discFlag)
					srho += discretization(CorrMatrix[r][c]);
				else
					srho += Math.abs(CorrMatrix[r][c]);

			}

		}
		
		srho = srho/((problemDimension*problemDimension - problemDimension)/2);
		finalBest = best;
		FT.add(j, fBest); 
		FT.setExtraDouble(srho);
		return FT;
	}
	
	private double discretization(double value)
	{
		value = Math.abs(value);
		double out;
		if(value < 0.2)
			out = 0.0;
		else if((value >= 0.2) && (value < 0.4))
			out = 0.3;
		else if((value >= 0.4) && (value < 0.6))
			out = 0.5;
		else if((value >= 0.6) && (value < 0.8))
			out = 0.7;
		else 
			out = 1;	
		return out;
	}
}



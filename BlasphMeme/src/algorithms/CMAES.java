package algorithms;


import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.algorithms.cmaes.CMAEvolutionStrategy;

import static utils.algorithms.Misc.saturate;
import static utils.algorithms.Misc.generateRandomSolution; //WARNING:  this method is incomplete and will affect the search. You will have to complete in Task 2


/**
 * Covariance Matrix Adaptation Evolutionary Strategy 
 */
public class CMAES extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception //this function implement the algorithm
	{
		FTrend FT = new FTrend(); //Use this object to store solutions
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		int j = 0;
		if (initialSolution != null)
		{
			best = initialSolution;
			fBest = initialFitness;
		}
		else //if not inserted, we need to randomly sample the initial guess
		{
			best = generateRandomSolution(bounds, problemDimension);
			fBest = problem.f(best);
			FT.add(j, fBest);
			j++;
		}
		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.setRepairMatrix(true);				// repair ill-conditioned matrix
		cma.setDimension(problemDimension);		// overwrite some loaded properties
		cma.setInitialX(best);					// in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.2);	// also a mandatory setting 
		cma.options.verbosity = -1;
		cma.options.writeDisplayToFile = -1;
		
		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();
				
		// iteration loop
		while (j < maxEvaluations)
		{
            // --- core iteration step ---
			// get a new population of solutions
			double[][] pop = cma.samplePopulation();
			
			for(int i = 0; i < pop.length && j < maxEvaluations; ++i)
			{ 
				// saturate solution on bounds 
				pop[i] = saturate(pop[i], bounds);
				
				// compute fitness/objective value	
				fitness[i] = problem.f(pop[i]);
				
				// save best
				if (fitness[i] < fBest)
				{
					fBest = fitness[i];
					for (int n = 0; n < problemDimension; n++)
						best[n] = pop[i][n];
						FT.add(j, fBest);//add the best solution found in this iteration to the fitness trend (it will be saved in a txt file)
				}
				
				j++;
			}

			// pass fitness array to update search distribution
			cma.updateDistribution(fitness);
		}
		
		finalBest = best;

		//add the best solutionat the end f the fitness trend
		FT.add(j, fBest);
		
		//return bests;
		return FT;
	}
}

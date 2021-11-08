package algorithms.specialOptions.FCGIAY;


import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.algorithms.cmaes.CMAEvolutionStrategy;

import static utils.algorithms.Misc.generateRandomSolution; 


/**
 * Covariance Matrix Adaptation Evolutionary Strategy 
 */
public class PC_CMAES extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception //this function implement the algorithm
	{

		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		double[] best = new double[problemDimension];
		double fBest = Double.MAX_VALUE;
		int j = 0;
		
		FTrend FT = new FTrend(true); 
		FT.setExtraValuesColumns(problemDimension+1);
		
		if (initialSolution != null)
		{
			best = initialSolution;
			fBest = initialFitness;
		}
		else //if not inserted, we need to randomly sample the initial guess (the initial mean vector)
		{
			best = generateRandomSolution(bounds, problemDimension);
//			fBest = problem.f(best);
//			FT.add(j, fBest);
//			j++;
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
		
		int generations = 0;
		
		
		// iteration loop
		while (j < maxEvaluations && generations < 100)
		{
            // --- core iteration step ---
			// get a new population of solutions
			double[][] pop = cma.samplePopulation();
			
			for(int i = 0; i < pop.length && j < maxEvaluations; ++i)
			{ 
				// saturate solution on bounds 
				pop[i] = correct(pop[i], bounds);
				
				// compute fitness/objective value	
				fitness[i] = problem.f(pop[i]);
				
				//add extras
				for (int n = 0; n < problemDimension; n++)
					FT.addExtra(pop[i][n]);
				FT.addExtra(fitness[i]);
				
				// save best
				if (fitness[i] < fBest)
				{
					fBest = fitness[i];
					for (int n = 0; n < problemDimension; n++)
						best[n] = pop[i][n];
						FT.add(j, fBest);
				}
				
				j++;
			}

			generations++;
			
			// pass fitness array to update search distribution
			cma.updateDistribution(fitness);
		}
		
		finalBest = best;

		//add the best solution at the end f the fitness trend
		FT.add(j, fBest);
		
		//return bests;
		return FT;
	}
}

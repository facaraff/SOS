package algorithms;

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.algorithms.cmaes.CMAEvolutionStrategy;

import static utils.algorithms.Misc.generateRandomSolution; 
/*
 * Covariance Matrix Adaptation Evolutionary Strategy 
 */
public class SEP_CMAES extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception 
	{
		FTrend FT = new FTrend(); 
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
		//cma.parameters.setPopulationSize(10);
		cma.setRepairMatrix(true);				// repair ill-conditioned matrix
		cma.setDimension(problemDimension);		// overwrite some loaded properties
		cma.setInitialX(best);					// in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.2);	// also a mandatory setting 
		cma.options.verbosity = -1;
		cma.options.writeDisplayToFile = -1;
		// XXX this parameter forces the use of a diagonal matrix, thus making the time complexity O(n) 
		cma.options.diagonalCovarianceMatrix = 1;
		
		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();
		
		//System.out.println(cma.getDataC());
		
		// iteration loop
		int j = 0;
		while (j < maxEvaluations)
		{
            // --- core iteration step ---
			// get a new population of solutions
			double[][] pop = cma.samplePopulation();
			
			for(int i = 0; i < pop.length && j < maxEvaluations; ++i)
			{ 
				// saturate solution inside bounds 
				pop[i] = correct(pop[i], bounds);
				
				// compute fitness/objective value	
				fitness[i] = problem.f(pop[i]);
				
				// save best
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
		}
		
		finalBest = best;

		FT.add(j, fBest);
		
		return FT;
	}
}
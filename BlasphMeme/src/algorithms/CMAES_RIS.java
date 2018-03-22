package algorithms;

import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.operators.MemesLibrary.ThreeSome_ShortDistance;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;

import utils.algorithms.cmaes.CMAEvolutionStrategy;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Covariance Matrix Adaptation Evolutionary Strategy 
 */
public class CMAES_RIS extends Algorithm
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
		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();
		
		//System.out.println(problemDimension);
		//System.out.println(cma.getDataC());
		
		// iteration loop
		int j = 0; 
		int budget = 3*maxEvaluations/10;
		while (j < budget)
		{
			// get a new population of solutions
			double[][] pop = cma.samplePopulation();
			
			for(int i = 0; i < pop.length && j < maxEvaluations; ++i)
			{ 
				// saturate solution inside bounds 
				pop[i] = toro(pop[i], bounds);
				
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
		
		
		double globalAlpha = getParameter("p0").doubleValue(); // 0.5
		double radius = getParameter("p1").doubleValue(); // 0.2
		double xi = getParameter("p2").doubleValue(); // 0.0000001
		double CR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
			
		double[] temp;
		
		int i = 0;
		double[] x = new double[problemDimension];
		for(int k=0; k < problemDimension; k++)
		  x[k] = best[k];
		
		while (i < maxEvaluations)
		{
			if(i==0)
			{
				i = j;
				//for(int n=0;n<problemDimension;n++)
					//x[n] = best[n];
			}
			else
			{
				temp = generateRandomSolution(bounds, problemDimension);
				x = crossOverExp(best, temp, CR);
			}
			double fx = problem.f(x); i++;
						
			if(fx < fBest)
			{
				fBest = fx;
				for(int n=0;n<problemDimension;n++)
					best[n] = x[n];
				FT.add(i, fBest);
			}
					
			temp = ThreeSome_ShortDistance(x, fx, radius, xi, problem, maxEvaluations,i, FT);
			fx = temp[0];
			i += temp[1];
							
			if(fx < fBest)
			{
				//improved = true;
				fBest = fx;
				for(int n=0;n<problemDimension;n++)
					best[n] = x[n];
				FT.add(i, fBest);
			}

		}
	
		finalBest = best;

		FT.add(i, fBest);
		
		return FT;
	}
}
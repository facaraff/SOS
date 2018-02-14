package algorithms.blessing;


import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;


//import utils.MathUtils;
//import utils.random.RandUtils;
//import algorithms.*;
import utils.algorithms.cmaes.CMAEvolutionStrategy;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;


public class PEARSON_ALG extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		//System.out.println("budget "+maxEvaluations);	
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		double[] best = new double[problemDimension];
		if (initialSolution != null)
			best = initialSolution;
		else
			best = generateRandomSolution(bounds, problemDimension);
		double fBest = Double.NaN;
		
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.parameters.setPopulationSize(100);
		cma.setRepairMatrix(true);				// repair ill-conditioned matrix
		cma.setDimension(problemDimension);		// overwrite some loaded properties
		cma.setInitialX(best);					// in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.2);	// also a mandatory setting 
		cma.options.verbosity = -1;
		cma.options.writeDisplayToFile = -1;
		
		double[] fitness = cma.init();				
		
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

			cma.updateDistribution(fitness);
		}
		double[][] cov = cma.getRho(); cma = null;
		double ci = 0;
		for(int c=0;c<cov.length;c++)
		{
			for(int r=c+1;r<cov[0].length;r++)
			{	
				ci += Math.abs(cov[r][c]);
			}
		}
		
		ci /= (Math.pow(problemDimension, 2) - problemDimension)/2; //bests.add(new Best(0, ci));
		
		finalBest = best;
		FT.add(j, fBest); 
		FT.setExtraDouble(ci);
		return FT;
	}
}



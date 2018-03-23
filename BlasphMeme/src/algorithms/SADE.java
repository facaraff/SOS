package algorithms;

import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.currentToRand1;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.operators.DEOp.rand2;
import static utils.algorithms.operators.DEOp.randToBest2;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;

import utils.MatLab;
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * Self-Adaptive Differential Evolution
 */
public class SADE extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = this.getParameter("p0").intValue();//50
		int strategyNumber = this.getParameter("p1").intValue();//4
		int LP = this.getParameter("p2").intValue();//20
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[] strategyProbability = new double[strategyNumber];
		int[][] successes = new int[LP][strategyNumber];
		int[][] failures  = new int[LP][strategyNumber];
		double[][] CRmemory  = new double[LP][strategyNumber];

		for (int m = 0; m < strategyNumber; m++)
		{
			for (int n = 0; n < LP; n++)
			{
				successes[n][m] = 0;
				failures[n][m] = 0;
				CRmemory[n][m] = 0;
			}
			
			strategyProbability[m] = 0.25;
		}
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			double[] tmp;
			if (j == 0 && initialSolution != null)
				tmp = initialSolution;
			else
				tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
			}
			
			i++;
		}

		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;

		double[] cumSumOfStrategies = new double[strategyNumber];
		double[] CR = new double[strategyNumber];
		double[] CRmemoryColumn = new double[LP];
		double[] S = new double[strategyNumber];
		double[] successesColumn = new double[LP];
		double[] failuresColumn = new double[LP];

		int generation = 1;
				
		// iterate
		while (i < maxEvaluations)
		{
			int generationRow = generation;
			if (generationRow >= LP)
			{
				generationRow = LP-1;
				// cut the success memory
				for (int m = 0; m < strategyNumber; m++)
				{
					for (int n = 0; n < LP; n++)
					{
						if (n < LP-1)
						{
							successes[n][m] = successes[n+1][m];
							failures[n][m] = failures[n+1][m];
							CRmemory[n][m] = CRmemory[n+1][m];
						}
						else
						{
							successes[n][m] = 0;
							failures[n][m] = 0;
							CRmemory[n][m] = 0;
						}
					}
				}
				
				// update the strategy Probability vector
				for (int m = 0; m < strategyNumber; m++)
				{
					for (int n = 0; n < LP; n++)
					{
						successesColumn[n] = successes[n][m];
						failuresColumn[n] = failures[n][m];
					}
					
					int sumSuccesses = (int) MatLab.sum(successesColumn);
					int sumFailures = (int) MatLab.sum(failuresColumn);
					int totalSum = sumSuccesses+sumFailures;
					if (totalSum != 0)
						S[m] = sumSuccesses/(totalSum) + 0.01;
					else
						S[m] = 0;
				}
				
				double sumS = MatLab.sum(S);
				for (int m = 0; m < strategyNumber; m++)
					strategyProbability[m] = S[m]/sumS;					
			}
			
			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				// mutation
				cumSumOfStrategies = MatLab.cumsum(strategyProbability);
				int mutationStrategy = 1 + RandUtils.randomInteger(3);
				boolean found = false;
			    for (int m = 0; m < strategyNumber && !found; m++)
			    {
			    	if (RandUtils.random() < cumSumOfStrategies[m])
			    	{
			    		mutationStrategy = m;
			    		found = true;
			    	}
			    }
			    
			    double F = RandUtils.gaussian(0.5, 0.3);
			    for (int m = 0; m < strategyNumber; m++)
			    {
			    	if (generation <= LP)
			    		CR[m] = RandUtils.gaussian(0.5, 0.1);
			    	else
			    	{
			    		for (int n = 0; n < LP; n++)
			    			CRmemoryColumn[n] = CRmemory[n][m];
			    		CR[m] = RandUtils.gaussian(MatLab.median(CRmemoryColumn), 0.1);
			    	}
			    }
			    
				// mutation
				switch (mutationStrategy)
				{
					case 1:
						// DE/rand/1
						newPt = rand1(population, F);
						break;
					case 2:
						// DE/rand-to-best/2
						newPt = randToBest2(population, best, F);
						break;
					case 3:
						// DE/rand/2
						newPt = rand2(population, F);
						break;
					case 4:
						// DE/current-to-rand/1
						crossPt = currentToRand1(population, j, F);
						break;
					default:
						break;
				}
		
				// crossover
				if (mutationStrategy != 4)
				{
					crossPt = crossOverBin(currPt, newPt, CR[mutationStrategy]);					
				}
				
				crossPt = toro(crossPt, bounds);
				crossFit = problem.f(crossPt);
				i++;

				// best update
				if (crossFit < fBest)
				{
					fBest = crossFit;
					for (int n = 0; n < problemDimension; n++)
						best[n] = crossPt[n];
					FT.add(i, fBest);
				}

				// replacement
				if (crossFit < currFit)
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = crossPt[n];
					fitnesses[j] = crossFit;
					
			        // update the number of success for each strategy
			        successes[generationRow][mutationStrategy] = successes[generationRow][mutationStrategy]+1;
			        CRmemory[generationRow][mutationStrategy] = CR[mutationStrategy]; 
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = currPt[n];
					fitnesses[j] = currFit;
					
			        // update the number of failures for each strategy
			        failures[generationRow][mutationStrategy] = failures[generationRow][mutationStrategy]+1;
				}
			}
			
			generation++;
		}

		finalBest = best;
		
		FT.add(i, fBest);
		
		return FT;
	}
}
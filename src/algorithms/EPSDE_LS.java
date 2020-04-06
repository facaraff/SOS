package algorithms;

import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;


import utils.MatLab;
import utils.random.RandUtils;
import interfaces.Algorithm;
import algorithms.singleSolution.Powell;
import algorithms.NelderMead;
import algorithms.singleSolution.Rosenbrock;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Ensemble of Parallel Self-Adaptive Differential Evolution with Local Search
 */
public class EPSDE_LS extends Algorithm
{
	static enum CrossoverStrategy {BIN, EXP};
	static enum MutationStrategy {MUT_STRAT_1, MUT_STRAT_2};

	static double[] poolCR = {0.1,0.5,0.9};
	static double[] poolF = {0.5,0.9};
	


	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue();	// 50
		
		int LSF = getParameter("p1").intValue();				// 200
		int lsBudget = getParameter("p2").intValue();			// 1000	
		boolean activateLS = (getParameter("p3").intValue()==1);
		
		// global data
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[][] currentPopulation = new double[populationSize][problemDimension];
		double[][] oldPopulation = new double[populationSize][problemDimension];
		double[] currentFitnesses = new double[populationSize];
		double[] oldFitnesses = new double[populationSize];

		double[] newPt = new double[problemDimension];
		double newFit = Double.NaN;
		
		double[] bestPt = new double[problemDimension];
		double bestFit = Double.NaN;
		
		int evalCount = 0;
		int genCount = 1;

		double[][] fitIndices = new double[populationSize][2];
		int[] bmIndex = new int[populationSize];
		
		int[][] r = new int[populationSize][3];

		// pools of strategies and parameters
		CrossoverStrategy[] poolCrossoverStrategies = CrossoverStrategy.values();
		MutationStrategy[] poolMutationStrategies = MutationStrategy.values();

		int nrCrossoverStrategies = poolCrossoverStrategies.length-1;
		int nrMutationStrategies = poolMutationStrategies.length-1;

		int nrCR = poolCR.length-1;
		int nrF = poolF.length-1;

		CrossoverStrategy[] crossoverStrategies = new CrossoverStrategy[populationSize];
		MutationStrategy[] mutationStrategies = new MutationStrategy[populationSize];

		double[] CR = new double[populationSize];
		double[] F = new double[populationSize];
		
		// evaluate initial population
		for (int i = 0; i < populationSize; i++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int j = 0; j < problemDimension; j++)
				currentPopulation[i][j] = tmp[j];
			currentFitnesses[i] = problem.f(currentPopulation[i]);

			if (i == 0 || currentFitnesses[i] < bestFit)
			{
				for (int j = 0; j < problemDimension; j++)
					bestPt[j] = currentPopulation[i][j];
				bestFit = currentFitnesses[i];
				FT.add(evalCount, bestFit);
			}

			evalCount++;
		}

		// assign random strategies and parameters from the pools
		for (int i = 0; i < populationSize; i++)
		{
			CR[i] = poolCR[RandUtils.randomInteger(nrCR)];
			F[i] = poolF[RandUtils.randomInteger(nrF)];
			crossoverStrategies[i] = poolCrossoverStrategies[RandUtils.randomInteger(nrCrossoverStrategies)];
			mutationStrategies[i] = poolMutationStrategies[RandUtils.randomInteger(nrMutationStrategies)];
		}
		
		// initialize local search methods
		Powell powell = new Powell();
		powell.setParameter("p0",-1.0); // purposely set a negative tolerance
		powell.setParameter("p1",100.0);
		
		Rosenbrock rosenbrock = new Rosenbrock();
		rosenbrock.setParameter("p0",10e-5);
		rosenbrock.setParameter("p1",2.0);
		rosenbrock.setParameter("p2",0.5);
		
		NelderMead nelderMead = new NelderMead();
		nelderMead.setParameter("p0", 1.0);
		nelderMead.setParameter("p1", 0.5);
		nelderMead.setParameter("p2", 2.0);
		nelderMead.setParameter("p3", 0.5);
		
		FTrend ft;
		
		// iterate
		while (evalCount < maxEvaluations)
		{
			// save old population
			for (int i = 0; i < populationSize; i++)
			{
				for (int j = 0; j < problemDimension; j++)
					oldPopulation[i][j] = currentPopulation[i][j];
				oldFitnesses[i] = currentFitnesses[i];
			}
			
			for (int i = 0; i < populationSize; i++)
			{
				fitIndices[i][0] = oldFitnesses[i];
				fitIndices[i][1] = i;
			}
			MatLab.sortRows(fitIndices, 0);
			int tmp = (int) Math.floor(0.5*(1-evalCount/maxEvaluations)*populationSize);
			for (int i = 0; i < populationSize; i++)
				bmIndex[i] = (int)fitIndices[RandUtils.randomInteger(tmp)][1];
			
			for (int i = 0; i < populationSize; i++)
				System.arraycopy(RandUtils.randomPermutation(populationSize), 0, r[i], 0, r[i].length);
			
			for (int i = 0; i < populationSize && evalCount < maxEvaluations; i++)
			{
				// mutation
				if (mutationStrategies[i] == MutationStrategy.MUT_STRAT_1)
				{
					for (int j = 0; j < problemDimension; j++)
						newPt[j] = oldPopulation[i][j] + F[i]*(oldPopulation[bmIndex[i]][j] - oldPopulation[i][j] 
									+ oldPopulation[r[i][0]][j] - oldPopulation[r[i][1]][j]);
				}
				else if (mutationStrategies[i] == MutationStrategy.MUT_STRAT_2)
				{
					for (int j = 0; j < problemDimension; j++)
						newPt[j] = oldPopulation[i][j] + RandUtils.random()*(1+F[i])*(oldPopulation[r[i][0]][j]-oldPopulation[i][j]) 
									+ F[i]*(oldPopulation[r[i][1]][j]-oldPopulation[r[i][2]][j]);
				}

	
				newPt = correct(newPt, bounds);
		

				// crossover
				if (mutationStrategies[i] == MutationStrategy.MUT_STRAT_1)
				{
					if (crossoverStrategies[i] == CrossoverStrategy.BIN)
						newPt = crossOverBin(oldPopulation[i], newPt, CR[i]);
					else if (crossoverStrategies[i] == CrossoverStrategy.EXP)
						newPt = crossOverExp(oldPopulation[i], newPt, CR[i]);
				}
				
				newFit = problem.f(newPt);
				evalCount++;

				// replacement
				if (newFit <= oldFitnesses[i])
				{
					for (int j = 0; j < problemDimension; j++)
						currentPopulation[i][j] = newPt[j];
					currentFitnesses[i] = newFit;
					
					// best update
					if (newFit <= bestFit)
					{
						for (int j = 0; j < problemDimension; j++)
							bestPt[j] = newPt[j];
						bestFit = newFit;
						
						if (evalCount % problemDimension == 0)
							FT.add(evalCount, bestFit);
					}
				}
				else
				{
					// Randomly select a new mutation strategy and parameter values from the pools
					CR[i] = poolCR[RandUtils.randomInteger(nrCR)];
					F[i] = poolF[RandUtils.randomInteger(nrF)];
					crossoverStrategies[i] = poolCrossoverStrategies[RandUtils.randomInteger(nrCrossoverStrategies)];
					mutationStrategies[i] = poolMutationStrategies[RandUtils.randomInteger(nrMutationStrategies)];
				}
			}

			// activate local search
			if (((genCount % LSF) == 0) && activateLS)
			{
				for (int i = 0; i < populationSize; i++)
				{
					fitIndices[i][0] = currentFitnesses[i];
					fitIndices[i][1] = i;
				}
				MatLab.sortRows(fitIndices, 0);
				int bestIndex = (int)fitIndices[0][1];
				
//				int ls = RandUtils.randomInteger(2);

				int ls = 1;
				
				
				switch (ls) {
					case 0:
					{
						// powell
						//FTrend ft;
						powell.setInitialSolution(currentPopulation[bestIndex]);
						powell.setInitialFitness(currentFitnesses[bestIndex]);
						int budget = (int)(MatLab.min(lsBudget, maxEvaluations-evalCount));
						ft  = powell.execute(problem, budget+1);
						currentPopulation[bestIndex] = powell.getFinalBest();
						//System.out.println("powell "+ft.getLastF());
						FT.append(ft, evalCount); ft = null;
						evalCount += budget;
						break;
					}
					case 1:
					{
						// rosenbrock
						//FTrend ft;
						//System.out.println("best mortacci f "+bestPt[0]);
						rosenbrock.setInitialSolution(currentPopulation[bestIndex]);
						rosenbrock.setInitialFitness(currentFitnesses[bestIndex]);
						int budget = (int)(MatLab.min(lsBudget, maxEvaluations-evalCount));
						ft = rosenbrock.execute(problem, budget+1);
						currentPopulation[bestIndex] = rosenbrock.getFinalBest();
						//System.out.println("ROS "+ft.getLastF());
						currentFitnesses[bestIndex] = ft.getLastF();
						FT.append(ft, evalCount); ft = null;
						evalCount += budget;
						break;
					}
					case 2:
					{
						// nelder-mead
						//FTrend ft;
						nelderMead.setInitialSolution(currentPopulation[bestIndex]);
						nelderMead.setInitialFitness(currentFitnesses[bestIndex]);
						int budget = (int)(MatLab.min(lsBudget, maxEvaluations-evalCount));
						ft = nelderMead.execute(problem, budget+1);
						currentPopulation[bestIndex] = nelderMead.getFinalBest();
						//System.out.println("NELDER "+ft.getLastF());
						currentFitnesses[bestIndex] = ft.getLastF();
						FT.append(ft, evalCount); ft = null;
						evalCount += budget;
						break;
					}
					default:
						break;
				}
				
				// best update
				if (currentFitnesses[bestIndex] <= bestFit)
				{
					for (int j = 0; j < problemDimension; j++)
						bestPt[j] = currentPopulation[bestIndex][j];
					bestFit = currentFitnesses[bestIndex];
					FT.add(evalCount, bestFit);
				}
			}

			genCount++;
		}

		finalBest = bestPt;

		FT.add(maxEvaluations, bestFit);

		return FT;
	}
}
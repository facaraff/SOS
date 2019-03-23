package algorithms.specialOptions;

import  utils.algorithms.operators.DEOp;
import static utils.algorithms.Misc.toro;
import static utils.algorithms.Misc.populationDiversity1;
import static utils.algorithms.Misc.populationDiversity2;
import static utils.algorithms.Misc.generateRandomSolution;

//import java.util.Vector; serve?

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/**
 * Differential Evolution (standard version, rand/1/bin)
 */
public class EarlyDE_ExampleWithUniqueFileFitnessPlesExtras extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue(); 
		double F = getParameter("p1").doubleValue();
		double CR = getParameter("p2").doubleValue();
		int mutationStrategy = getParameter("p3").intValue();
		int crossoverStrategy = getParameter("p4").intValue();
		double alpha = getParameter("p5").doubleValue();

		FTrend FT = new FTrend(true);
		FT.setExtraValuesColumns(2);
		FT.setTogether(true);
		
		
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		if(CR==-1)
			CR = 1.0/Math.pow(0.2,1.0/(problemDimension*alpha));
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
	
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
				FT.addExtra(Double.NaN);FT.addExtra(Double.NaN);
			}
			
			i++;
		}
		
//		FT.addExtra(populationDiversity1(population));
//		FT.addExtra(populationDiversity2(population));

		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;

		// iterate
		while (i < maxEvaluations)
		{
		
			double[][] newPop = new double[populationSize][problemDimension];
			double[] newFitnesses = new double[populationSize];
			
			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				// mutation
				switch (mutationStrategy)
				{
					case 1:
						// DE/current-to-rand/1
						crossPt = DEOp.currentToRand1(population, j, F);
						break;
					case 2:
						// DE/rand/1
						newPt = DEOp.rand1(population, F);
						break;
					case 3:
						// DE/best/1
						newPt = DEOp.best1(population, fitnesses, F); 
						break;
					case 4:
						// DE/rand-to-best/1
						newPt = DEOp.randToBest1(fitnesses,population,F);
						break;
					case 5:
						// DE/cur-to-best/1
						newPt = DEOp.currentToBest1(fitnesses, population, j, F);
						break;
					case 6:
						// DE/rand/2
						newPt = DEOp.rand2(population, F);
						break;
					case 7:
						// DE/best/2
						newPt = DEOp.best2(population, fitnesses, F);
						break;
					case 8:
						// DE/rand-to-best/2
						newPt = DEOp.randToBest2(fitnesses, population, F);
						break;
					default:
						break;
				}
		
				// crossover
				if (mutationStrategy != 1)
				{
					if (crossoverStrategy == 1)
						crossPt = DEOp.crossOverBin(currPt, newPt, CR);
					else if (crossoverStrategy == 2)
						crossPt = DEOp.crossOverExp(currPt, newPt, CR);
					else if (crossoverStrategy == 0)
						crossPt = newPt;
				}
				
				crossPt = toro(crossPt, bounds);
				crossFit = problem.f(crossPt);
				i++;


				// replacement
				if (crossFit < currFit)
				{
					for (int n = 0; n < problemDimension; n++)
						newPop[j][n] = crossPt[n];
					newFitnesses[j] = crossFit;
					
					// best update
					if (crossFit < fBest)
					{
						fBest = crossFit;
						for (int n = 0; n < problemDimension; n++)
							best[n] = crossPt[n];
						//if(problemDimension%i==0)	
							FT.add(i, fBest);
							FT.addExtra(populationDiversity1(population));FT.addExtra(populationDiversity1(population));
					}
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						newPop[j][n] = currPt[n];
					newFitnesses[j] = currFit;
				}
				
				
				crossPt = null; newPt = null;
//				if(problemDimension%i==0)	
					//FT.add(i, fBest);
			}
			
			
			population = newPop;
			fitnesses = newFitnesses;
//			FT.addExtra(populationDiversity1(population));
//			FT.addExtra(populationDiversity2(population));
			newPop = null; newFitnesses = null;
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		FT.addExtra(populationDiversity1(population));
		FT.addExtra(populationDiversity2(population));
		return FT;
		
		
	}

}

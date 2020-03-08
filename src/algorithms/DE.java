package algorithms;

import  utils.algorithms.operators.DEOp;
import static utils.algorithms.Misc.correct;
import static utils.algorithms.Misc.generateRandomSolution;

//import java.util.Vector; serve?

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/**
 * Differential Evolution (all established variants)
 */
public class DE extends Algorithm
{
	
	protected String mutationStrategy = null;
	protected char crossoverStrategy = 'X';
	
	public DE(String mut) {this.mutationStrategy = mut;}
	public DE(String mut, char xover)
	{
		this.mutationStrategy = mut;
		if(!mut.equals("ctro"))
			this.crossoverStrategy = xover;
	}
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue(); 
		double F = getParameter("p1").doubleValue();
		double CR = getParameter("p2").doubleValue();
		double alpha = getParameter("p3").doubleValue();

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		if(CR==-1)
			CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
		
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
			}
			
			i++;
		}

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

			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				// mutation
				switch (mutationStrategy)
				{
					case "ro":
						// DE/rand/1
						newPt = DEOp.rand1(population, F);
						break;
					case "ctbo":
						// DE/cur-to-best/1
						newPt = DEOp.currentToBest1(population, best, j, F);
						break;
					case "rt":
						// DE/rand/2
						newPt = DEOp.rand2(population, F);
						break;
					case "ctro":
						// DE/current-to-rand/1
						crossPt = DEOp.currentToRand1(population, j, F);
						break;
					case "rtbt":
						// DE/rand-to-best/2
						newPt = DEOp.randToBest2(population, best, F);
						break;
					default:
						break;
				}
		
				// crossover
				if (!mutationStrategy.equals("ctro"))
				{
					if (crossoverStrategy == 'b')
						crossPt = DEOp.crossOverBin(currPt, newPt, CR);
					else if (crossoverStrategy == 'e')
						crossPt = DEOp.crossOverExp(currPt, newPt, CR);
					else if (crossoverStrategy == 'x')
						crossPt = newPt;
				}
				
				
				crossPt = correct(this.correction,crossPt, bounds);
				crossFit = problem.f(crossPt);
				i++;


				// replacement
				if (crossFit < currFit)
				{
					for (int n = 0; n < problemDimension; n++)
						newPop[j][n] = crossPt[n];
					fitnesses[j] = crossFit;
					
					// best update
					if (crossFit < fBest)
					{
						fBest = crossFit;
						for (int n = 0; n < problemDimension; n++)
							best[n] = crossPt[n];
						//if(i==problemDimension)	
						FT.add(i, fBest);
					}
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						newPop[j][n] = currPt[n];
					fitnesses[j] = currFit;
				}
				crossPt = null; newPt = null;
			}
			
			population = newPop;
			newPop = null;
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		return FT;
		
		
	}

}

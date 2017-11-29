package algorithms;

import  utils.algorithms.operators.DEOp;
import static utils.algorithms.Misc.toro;
import static utils.algorithms.Misc.generateRandomSolution;

//import java.util.Vector; serve?

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/**
 * Rotation invariant Differential Evolution with Gram-Shmidt process 
 * 
 * @url file:///C:/Users/fcaraf00/Downloads/Solving_nonlinear_optimization_problems_by_Differe.pdf
 * 
 * @author facaraff fabio.caraffini@gmail.com
 */
public class RIDE extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue(); 
		double F = getParameter("p1").doubleValue();
		double CR = getParameter("p2").doubleValue();
		int mutationStrategy = getParameter("p3").intValue();
		int crossoverStrategy = getParameter("p4").intValue();

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		boolean both = false;
		
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
		
		//orthonormal basis
		double[][] b=null;

		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		
		

		// iterate
		while (i < maxEvaluations)
		{
		
			b = DEOp.getBasis(population);
			

			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				// mutation
				switch (mutationStrategy)
				{
					case 1:
						// DE/rand/1
						newPt = DEOp.rand1(population, F);
						break;
					case 2:
						// DE/cur-to-best/1
						newPt = DEOp.currentToBest1(population, best, j, F);
						break;
					case 3:
						// DE/rand/2
						newPt = DEOp.rand2(population, F);
						break;
					case 4:
						// DE/current-to-rand/1
						crossPt = DEOp.currentToRand1(population, j, F);
						break;
					case 5:
						// DE/rand-to-best/2
						newPt = DEOp.randToBest2(population, best, F);
						break;
					default:
						break;
				}
		
				// crossover
				if (mutationStrategy != 4)
				{
					switch (crossoverStrategy)
					{
						case 1:
							// Binomial xo
							crossPt = DEOp.crossOverBin(currPt, newPt, CR);
							break;
						case 2:
							// Exponential xo
							crossPt = DEOp.crossOverExp(currPt, newPt, CR);
							break;
						case 3:
							// DE/rand/2
							newPt = DEOp.rand2(population, F);
							break;
						case 4:
							// Rotation invariant binomial xo
							crossPt = DEOp.ribc(currPt, newPt, CR,b);
							break;
						case 5:
							// Rotation invariant exponential xo
							crossPt = DEOp.riec(currPt, newPt, CR,b);
							break;
						case 6:
							// Binomial + Rotation invariant binomial xo (as in RIDE article)
							crossPt = DEOp.crossOverBin(currPt, newPt, CR);
							both = true;
							break;
						case 7:
							// Exponential + Rotation invariant exponential xo (as in RIDE article)
							crossPt = DEOp.crossOverExp(currPt, newPt, CR);
							both = true;
							break;
						default:
							break;
					}
						
				}
				
				crossPt = toro(crossPt, bounds);
				crossFit = problem.f(crossPt);
				i++;


				// replacement
				if (crossFit < currFit)
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = crossPt[n];
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
					if(both)
					{
						if(crossoverStrategy==6)
							crossPt = DEOp.ribc(currPt, newPt, CR,b);	
						else if(crossoverStrategy==7)
							crossPt = DEOp.riec(currPt, newPt, CR,b);		
						// replacement
						if (crossFit < currFit)
						{
							for (int n = 0; n < problemDimension; n++)
								population[j][n] = crossPt[n];
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
					}
					
//					for (int n = 0; n < problemDimension; n++)
//						population[j][n] = currPt[n];
//					fitnesses[j] = currFit;
				}
				crossPt = null; newPt = null;
			}
			
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		return FT;
		
		
	}

}

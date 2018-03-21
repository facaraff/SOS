package algorithms;

import static utils.algorithms.operators.DEOp.best1;
import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.operators.DEOp.currentToBest1;
import static utils.algorithms.operators.DEOp.currentToRand1;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.operators.DEOp.rand2;
import static utils.algorithms.operators.DEOp.randToBest2;
import static utils.algorithms.Misc.toro;
import static utils.algorithms.Misc.generateRandomSolution;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * Differential Evolution (microDE with resampling)
 */
public class MicroDE extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue(); 
		double CR = getParameter("p1").doubleValue();
		double F = getParameter("p2").doubleValue();
		int crossoverStrategy = getParameter("p3").intValue();
		int mutationStrategy = getParameter("p4").intValue();
		//double restartThreshold = getParameter("p5").doubleValue();
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		int bestIndex = 0;
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
				bestIndex = j;
			}
			
			i++;
		}

		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		double initialFBest = fBest;
		double deltaF;
		//microDE specific variables
		int stagnationCounter = 0;
		double resampleP = 0.05;
		double minF = 0.5;
		double maxF = 1.0;
		CR = Math.pow(0.5, (1/(problemDimension*0.05)));
		double globalCR = Math.pow(0.5, (1/(problemDimension*0.95)));
		boolean parametercontrol = true;
		boolean strategycontrol = false;
		boolean StagnationRestart = false;
		int temp = 0;
		//double gamma = Math.pow(i/maxEvaluations, 1/4);
		
		// iterate
		while (i < maxEvaluations)
		{
			//update restart
			initialFBest = fBest;
			
			//Re-sampling
			if (RandUtils.random() < resampleP) {
				temp = RandUtils.randomInteger(populationSize-1);
				while (temp == bestIndex) { 
					temp = RandUtils.randomInteger(populationSize-1);
				}
				//population[temp] = generateRandomSolution(bounds, problemDimension);
				crossPt = generateRandomSolution(bounds, problemDimension);
				population[temp] = crossOverExp(population[temp], crossPt, globalCR);
				fitnesses[temp] = problem.f(population[temp]);
				//best update
				if (fitnesses[temp] < fBest) {
					fBest = fitnesses[temp];
					for (int n = 0; n < problemDimension; n++)
						best[n] = population[temp][n];
					FT.add(i, fBest);
					bestIndex = temp;
				}
				
				/*
				for (int j = 0; j < populationSize; j++) {
				 
					population[j] = generateRandomSolution(bounds, problemDimension);
					fitnesses[j] = problem.f(population[j]);
					i++;
					//best update
					if (fitnesses[j] < fBest) {
						fBest = fitnesses[j];
						for (int n = 0; n < problemDimension; n++)
							best[n] = population[j][n];
						bests.add(new Best(i, fBest));
					}
				}
				*/
				
			}
			
			//F & Cr control
			if (parametercontrol == true) {
				F = RandUtils.gaussian(0.75, 0.1);
				if (F < minF)
					F = minF;
				if (F > maxF)
					F = maxF;
				CR = (0.5 - 1/(10*Math.sqrt(3))) + RandUtils.random()*2.0*(1/(10*Math.sqrt(3)));
			}
			
			//mutation strategy selection
			if (strategycontrol == true) {
				//gamma = Math.pow(i/maxEvaluations, 1/4);
				if (RandUtils.random() < 0.1) {
					mutationStrategy = 2;
				}
				else mutationStrategy = 4;
			}
			//DE
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
						newPt = rand1(population, F);
						break;
					case 2:
						// DE/cur-to-best/1
						newPt = currentToBest1(population, best, j, F);
						break;
					case 3:
						// DE/rand/2
						newPt = rand2(population, F);
						break;
					case 4:
						// DE/current-to-rand/1
						crossPt = currentToRand1(population, j, F);
						break;
					case 5:
						// DE/rand-to-best/2
						newPt = randToBest2(population, best, F);
						break;
					case 6:
						// DE/best/1
						newPt = best1(population, fitnesses, F);
					default:
						break;	
				}
		
				// crossover
				if (mutationStrategy != 4)
				{
					if (crossoverStrategy == 1)
						crossPt = crossOverBin(currPt, newPt, CR);
					else if (crossoverStrategy == 2)
						crossPt = crossOverExp(currPt, newPt, CR);
					
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
					bestIndex = j;
				}

				// replacement
				if (crossFit < currFit)
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = crossPt[n];
					fitnesses[j] = crossFit;
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = currPt[n];
					fitnesses[j] = currFit;
				}
			}
		    
			//restart in case of stagnation
			if (StagnationRestart == true) {
				deltaF = (initialFBest-fBest)/initialFBest;
				if (deltaF < 0.01) {
					stagnationCounter++;
				}
				if (stagnationCounter >= 20 && i < maxEvaluations*0.8) {
					stagnationCounter = 0;
					//generate new population
					for (int n = 0 ;n < populationSize; n++) {
						population[n] = generateRandomSolution(bounds, problemDimension);
						fitnesses[n] = problem.f(population[n]);
						i++;
						//best update
						if (fitnesses[n] < fBest) {
							fBest = fitnesses[n];
							for (int k = 0; k < problemDimension; k++)
								best[k] = population[n][k];
						}		
					}				
				}
			}	
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		
		return FT;
	}
}
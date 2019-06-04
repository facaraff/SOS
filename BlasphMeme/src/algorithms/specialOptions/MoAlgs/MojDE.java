package algorithms.specialOptions.MoAlgs;

import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.Misc.toro;
import static utils.algorithms.Modality.TestModalityOld.basinEstimate;

import java.util.Arrays;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
/*
 * J(anez) Self-Adaptive Differential Evolution
 */
public class MojDE extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = this.getParameter("p0").intValue();//50
		double fl = this.getParameter("p1").doubleValue();//0.1
		double fu = this.getParameter("p2").doubleValue();//1
		double tau1 = this.getParameter("p3").doubleValue(); //0.1
		double tau2 = this.getParameter("p4").doubleValue(); //0.1
		int modalityPopulation = this.getParameter("p5").intValue();
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		double[] F = new double[populationSize];
		double[] CR = new double[populationSize];
		for (int j = 0; j < populationSize; j++)
		{
			F[j] = fl + RandUtils.random()*(fu-fl);
			CR[j] = RandUtils.random();
		}
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 1;
		
		//***MODALITY STUFF STARTS****
		
		//Evaluate function modality
		int modality = -1;
		int modalityEvaluations = maxEvaluations/3; //33%
		int repeatsSaved = 0;
		double[][] returnMatrix = basinEstimate(problem, modalityPopulation, repeatsSaved, modalityEvaluations);
		//i = i + modalityEvaluations;
		modality = returnMatrix.length;
		
		//rank/weight solutions
		double[] rankedFitness = new double[modality];
		double[][] rankedSolutions = new double[modality][problemDimension];
		for (int j = 0; j < modality; j++) {
			rankedFitness[j] = returnMatrix[j][problemDimension];
		}
		Arrays.sort(rankedFitness);
		
		for (int k = 0; k < rankedFitness.length; k++) { 
			for (int j = 0; j < modality; j++) {
				if (rankedFitness[k] == returnMatrix[j][problemDimension]) {
					for (int n = 0; n < problemDimension; n++) {
						rankedSolutions[k][n] = returnMatrix[j][n];
					}
					break;
				}
			}
		}
		
		for (int n = 0; n < problemDimension; n++)
			best[n] = rankedSolutions[0][n];
		FT.add(1, rankedFitness[0]);
		fBest = rankedFitness[0];
		
		//initialize population around modes with gaussian.
		int[] formula = {10,6,5,4,3,2,1,1,1,1};
		int[] formulasum = {10,16,21,25,28,30,31,32,33,34};
		int index = -1;
		
		for (int j = 0; j < populationSize; j++) {
			if (j < modality) {
				for (int n = 0;n < problemDimension; n++) {
					population[j][n] = rankedSolutions[j][n];
				}
				fitnesses[j] = rankedFitness[j];
			}
			else {
				//select mode as mean point
				double random = Math.random()*formulasum[modality-1];
				for (int k = 0; k < formula.length; k++) {
					if (random < formula[modality-1]) {
						index = k;
						break;
					}
					random = random - formula[k];
				}
				//gaussian around mode
				for (int n = 0; n < problemDimension; n++) {
					double stdDev = 0.1*Math.abs(bounds[n][0] - bounds[n][1]);
					population[j][n] = RandUtils.gaussian(rankedSolutions[index][n], stdDev);
				}
				
				population[j] = toro(population[j], bounds);
				fitnesses[j] = problem.f(population[j]);
				
				if (fitnesses[j] < fBest)
				{
					fBest = fitnesses[j];
					for (int n = 0; n < problemDimension; n++)
						best[n] = population[j][n];
					FT.add(i, fBest);
				}
				
				i++;
			}
		}
		
		//***MODALITY STUFF ENDS******

		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		
		// iterate
		while (i < maxEvaluations)
		{
			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{				
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				// update F
				if (RandUtils.random() < tau1)
					F[j] = fl + fu * RandUtils.random();
				
				// DE/rand/1
				newPt = rand1(population, F[j]);
				newPt = toro(newPt, bounds);
				
				// update CR
				if (RandUtils.random() < tau2)
		        	CR[j] = RandUtils.random();
				
				// crossover
				crossPt = crossOverBin(currPt, newPt, CR[j]);
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
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = currPt[n];
					fitnesses[j] = currFit;
				}
			}
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		
		return FT;
	}
}
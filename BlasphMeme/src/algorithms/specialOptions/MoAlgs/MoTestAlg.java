package algorithms.specialOptions.MoAlgs;

//import static utils.algorithms.operators.DEOp.crossOverBin;
//import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.Misc.toro;
import static utils.algorithms.TestModality.basinEstimate;

import java.util.Arrays;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
/*
 * J(anez) Self-Adaptive Differential Evolution
 */
public class MoTestAlg extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = this.getParameter("p0").intValue();//50

		int modalityPopulation = this.getParameter("p1").intValue();
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
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
		System.out.println("modality = "+modality);
		
		
		
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
		
		
		
		
		
		
		//NOT NEEDED
		
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


		finalBest = best;
		
		FT.add(i, fBest);
		
		return FT;
	}
}
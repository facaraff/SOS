package algorithms.specialOptions.FCGIAY;

import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.Misc.generateRandomSolution;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * J(anez) Self-Adaptive Differential Evolution
 */
public class PC_jDE extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int generations = 0;
		
		int populationSize = this.getParameter("p0").intValue();//50
		double fl = this.getParameter("p1");//0.1
		double fu = this.getParameter("p2");//1
		double tau1 = this.getParameter("p3"); //0.1
		double tau2 = this.getParameter("p4"); //0.1
		
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		FTrend FT = new FTrend(true);
		FT.setExtraValuesColumns(problemDimension+1);
		
		
		
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
		
		int i = 0;
		
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			double[] tmp=new double[problemDimension];
			if(initialSolution!=null && j==0)
				for(int k=0;k<problemDimension;k++)
					tmp[k]=initialSolution[k];
			else
				tmp = generateRandomSolution(bounds, problemDimension);
			
			for (int n = 0; n < problemDimension; n++)
			{
				population[j][n] = tmp[n];
				FT.addExtra(tmp[n]);
			}
			fitnesses[j] = problem.f(population[j]);
			i++;
			
			FT.addExtra(fitnesses[j]);
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
			}
		}
		generations++;

		
		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		
		// iterate
		while ((i < maxEvaluations ) && (generations < 100))
		{
			for (int j = 0; j < populationSize && i < maxEvaluations && generations < 100; j++)
			{				
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				// update F
				if (RandUtils.random() < tau1)
					F[j] = fl + fu * RandUtils.random();
				
				// DE/rand/1
				newPt = rand1(population, F[j]);
				newPt = correct(newPt, bounds);
				
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
			
			
			for (int j = 0; j < populationSize; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					FT.addExtra(population[j][n]);
				FT.addExtra(fitnesses[j]);
			}
			generations++;
			
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		
		return FT;
	}
	
}
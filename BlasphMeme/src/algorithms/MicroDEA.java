package algorithms;

import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.operators.DEOp.currentToBest1;
import static utils.algorithms.operators.DEOp.currentToRand1;
import static utils.algorithms.operators.DEOp.randToBest2;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.operators.DEOp.rand2;
import static utils.algorithms.Misc.generateRandomSolution;




import static utils.algorithms.Misc.toro;
import static utils.MatLab.indexMin;

//import java.util.Vector;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;





public class MicroDEA extends Algorithm
{
	public double[] SR;


	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{		
		int populationSize = getParameter("p0").intValue(); 
		double F = getParameter("p1").doubleValue();
		double CR = getParameter("p2").doubleValue();
		int mutationStrategy = getParameter("p3").intValue();
		int crossoverStrategy = getParameter("p4").intValue();
		double alpha = getParameter("p5").doubleValue();
		
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
		
		SR = new double[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			SR[k] = (bounds[k][1] - bounds[k][0]) * 0.4;

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
				}
				if(i%problemDimension == 0)
						FT.add(i, fBest);

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
			
			//if(Math.random() < 1- genIndex/genNumber)
			if (RandUtils.random() >= 0.75)
			{
				int index =indexMin(fitnesses);
				double[] temp = Extra_Moves(best, fBest, 20, problem, maxEvaluations,i, FT);
				fitnesses[index] = temp[0];
				fBest = temp[0];
				i += temp[1];
				for(int z=0; z < populationSize; z++)
					population[index][z] = best[z];
			}
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		
		return FT;
	}
	
	public  double[] Extra_Moves(double[] sol, double fit,int deepLSSteps, Problem prob, int totalBudget, int iter, FTrend ft) throws Exception
	{
		int numEval = 0;
		int problemDimension = prob.getDimension();
		double[][] bounds = prob.getBounds();


		boolean improve = true;
		int j = 0;
		while ((j < deepLSSteps) && (iter < totalBudget))
		{	
			double[] Xk = new double[problemDimension];
			double[] Xk_orig = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
			{
				Xk[k] = sol[k];
				Xk_orig[k] = sol[k];
			}

			double fXk_orig = fit;

			
			if (!improve)
			{
				for (int k = 0; k < problemDimension; k++)
					SR[k] = SR[k]/2;
			}

			improve = false;
			int k = 0;
			while ((k < problemDimension) && (iter < totalBudget))
			{
				Xk[k] = Xk[k] - SR[k];
				Xk = toro(Xk, bounds);
				double fXk = prob.f(Xk);
				iter++; numEval++;

				// best update
				if (fXk < fit)
				{
					fit = fXk;
					for (int n = 0; n < problemDimension; n++)
						sol[n] = Xk[n];
					ft.add(iter,fit);
				}

				if (iter < totalBudget)
				{
					if (fXk == fXk_orig)
					{
						for (int n = 0; n < problemDimension; n++)
							Xk[n] = Xk_orig[n];
					}
					else
					{
						if (fXk > fXk_orig)
						{
							Xk[k] = Xk_orig[k];
							Xk[k] = Xk[k] + 0.5*SR[k];
							Xk = toro(Xk, bounds);
							fXk = prob.f(Xk);
							iter++; numEval++;

							// best update
							if (fXk < fit)
							{
								fit = fXk;
								for (int n = 0; n < problemDimension; n++)
									sol[n] = Xk[n];
									ft.add(iter,fit);
							}

							if (fXk >= fXk_orig)
								Xk[k] = Xk_orig[k];
							else
								improve = true;
						}
						else
							improve = true;
					}
				}

				k++;
			}

			j++;
		}

		double[] out = {fit, numEval};
		return out;
	}
}
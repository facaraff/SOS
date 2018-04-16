package algorithms.singleSolution;


import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import static utils.MatLab.subtract;
import static utils.MatLab.sum;

import utils.random.RandUtils;

import interfaces.Algorithm;
import interfaces.Problem;

import static utils.RunAndStore.FTrend;


public class SolisWets extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
 
		double rho = getParameter("p0").doubleValue();//???;
	
		
		FTrend FT =new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// current best
		double[] best = new double[problemDimension];
		double fBest;
		
		int i = 0;
		if (initialSolution != null)
		{
			best = initialSolution;
			fBest = initialFitness;
		}
		else
		{
			best = generateRandomSolution(bounds, problemDimension);		
			fBest = problem.f(best);
			i++;
		}

		
		int numSuccess = 0;
		int numFailed = 0;

		double newfBest;
		double[] dif = new double[problemDimension];
		double[] bestFirst = new double[problemDimension];
		double[] bestSecond = new double[problemDimension];
		double[] bias = new double[problemDimension];

		

		while (i < maxEvaluations )
		{	
			for(int n=0; n < problemDimension; n++)
				dif[n] = RandUtils.gaussian(0,rho);
			bestFirst = sum(sum(best, bias ) , dif );
			bestFirst = toro(bestFirst, bounds);
			newfBest = problem.f(bestFirst);
			i++;
			if(newfBest < fBest)
			{
				fBest = newfBest;
				//bias = sum( multiply(0.2, bias) , multiply( 0.4, sum(dif, bias) )  );
				for(int n=0; n < problemDimension; n++)
				{
					best[n] = bestFirst[n];
					bias[n] = 0.2*bias[n] + 0.4*(dif[n] + bias[n]);
				}
				if(i%problemDimension==0) FT.add(i,fBest);
				numSuccess++;
				numFailed = 0;
			}
			else if(i < maxEvaluations)
			{
				bestSecond = subtract(  subtract(best, bias) , dif  );
				bestSecond = toro(bestSecond, bounds);
				newfBest = problem.f(bestSecond);
				i++;
				if(newfBest < fBest)
				{
					fBest = newfBest;
					//bias = subtract( bias, multiply(0.4, sum(dif, bias)) );
					for(int n=0; n < problemDimension; n++)
					{
						best[n] = bestSecond[n];
						bias[n] = bias[n] - 0.4*(dif[n] + bias[n]);
					}
					if(i%problemDimension==0) FT.add(i,fBest);
					numSuccess++;
					numFailed = 0;
				}
				else
				{
					numFailed++;
					numSuccess = 0;
				}
			}
			
			if(numSuccess > 5)
			{
				rho = rho*2;
				numSuccess = 0;
			}
			else if(numFailed > 3)
			{
				rho = rho/2;
				numFailed = 0;
			}
		}

		finalBest = best;
		FT.add(i,fBest);
		return FT;
	}
}
package algorithms;

import static utils.algorithms.operators.MemesLibrary.ThreeSome_ShortDistance;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;

import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;


public class RIS extends Algorithm
{	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		double globalAlpha = getParameter("p0").doubleValue(); // 0.5
		double radius = getParameter("p1").doubleValue(); // 0.4
		double xi = getParameter("p2").doubleValue(); // 0.000001
		double CR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
			
		double[] best; 
		double fBest;
		double[] temp;
		
		FTrend FT = new FTrend();
		
		best = generateRandomSolution(bounds, problemDimension);
		fBest = problem.f(best);
		FT.add(0, fBest);
		int i = 1;
		double[] x = new double[problemDimension];
		for(int k=0; k < problemDimension; k++)
		  x[k] = best[k];
		
		
		while (i < maxEvaluations)
		{
			temp = generateRandomSolution(bounds, problemDimension);
			x = crossOverExp(best, temp, CR);
			
			double fx = problem.f(x); i++;
						
			if(fx < fBest)
			{
				fBest = fx;
				for(int n=0;n<problemDimension;n++)
					best[n] = x[n];
				FT.add(i, fBest);
			}
					
			temp = ThreeSome_ShortDistance(x, fx, radius, xi, problem, maxEvaluations,i, FT);
			fx = temp[0];
			i += temp[1];
							
			if(fx < fBest)
			{
				//improved = true;
				fBest = fx;
				for(int n=0;n<problemDimension;n++)
					best[n] = x[n];
				FT.add(i, fBest);
			}

		}
		
		finalBest = best;
		
		FT.add(i,fBest);
		
		return FT;
	}
}
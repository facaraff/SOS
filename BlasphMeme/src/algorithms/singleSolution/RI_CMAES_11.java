package algorithms.singleSolution;

import static utils.algorithms.operators.MemesLibrary.CMAES_11;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;

import interfaces.Algorithm;
import interfaces.Problem;

import static utils.RunAndStore.FTrend;


public class RI_CMAES_11 extends Algorithm
{	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		double globalAlpha = getParameter("p0").doubleValue(); // 0.5

		double CR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
		
		double p_target_succ = getParameter("p1").doubleValue(); // 2/11
		double c_p = getParameter("p2").doubleValue(); // 1/12
		double p_thresh = getParameter("p3").doubleValue();// 0.44
		double sigma0 = getParameter("p4").doubleValue();// 1 --> problem dependent!!
		double failsNum = getParameter("p5").doubleValue();// e' un double che salva un valore intero!!! 
		
		
		double alpha = getParameter("p6").doubleValue(); // 
		
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
			     
		    temp = CMAES_11(x, fx, (int) (maxEvaluations*alpha), maxEvaluations, problem, i, p_target_succ, c_p, p_thresh, sigma0, failsNum, FT);
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
package algorithms;

import static utils.algorithms.Misc.generateRandomSolution;
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.algorithms.Misc.toro;
import utils.RunAndStore.FTrend;





public class NonUniformSA extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{		
		int B = this.getParameter("p0").intValue(); //5
		double alpha = this.getParameter("p1").intValue(); //0.9
		int Lk = this.getParameter("p2").intValue(); //3
		int initialSolutions = this.getParameter("p3").intValue(); //10
				
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[] bestPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] oldPt = new double[problemDimension];
		
		double fNew;
		double fOld;
		double fBest;
		double fWorst;
		
		// initialize first point
		if (initialSolution != null)
			bestPt = initialSolution;
		else
			bestPt = generateRandomSolution(bounds, problemDimension);
		
		fNew = problem.f(bestPt);
		fOld = fNew;
		fBest = fNew;
		fWorst = fNew;

		FT.add(0, fNew);
		
		// evaluate initial solutions to set the initial temperature
		for (int j = 0; j < initialSolutions; j++)
		{
			newPt = generateRandomSolution(bounds, problemDimension);
			fNew = problem.f(newPt);

			// update best
			if (fNew < fBest)
			{
				FT.add(j+1, fNew);
				for (int k = 0; k < problemDimension; k++)
					bestPt[k] = newPt[k];
				fBest = fNew;
			}
			
			// update worst
			if (fNew > fWorst)
				fWorst = fNew;
		}
		
		for (int k = 0; k < problemDimension; k++)
			oldPt[k] = bestPt[k];
		
		// initialize temperature
		double delt0 = fWorst-fOld;
		double accept0 = 0.9;
		double T0 = -delt0/Math.log(accept0);		
		double tk = T0;

		int i = initialSolutions+1;
		
		int generationIndex = 1;
		int totalGenerations = (maxEvaluations-i)/Lk;
		
		while (i < maxEvaluations)
		{
			for (int j = 0; j < Lk && i < maxEvaluations; j++)
			{
				// non-uniform mutation
				for (int k = 0; k < problemDimension; k++)
				{
					//double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)i/maxEvaluations, B));
					double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)generationIndex/totalGenerations, B));
					
					if (RandUtils.random()<0.5)
						newPt[k] = oldPt[k] - (oldPt[k]-problem.getBounds()[k][0])*(1-temp);
					else
						newPt[k] = oldPt[k] + (problem.getBounds()[k][1]-oldPt[k])*(1-temp);
				}
				
				// evaluate fitness
				newPt = toro(newPt, bounds);
				fNew = problem.f(newPt);
				i++;
				
				// update best
				if (fNew < fBest)
				{
					FT.add(i, fNew);
					for (int k = 0; k < problemDimension; k++)
						bestPt[k] = newPt[k];
					fBest = fNew;
				}

				// move to the neighbor point
				if ((fNew <= fOld) || (Math.exp((fOld-fNew)/tk) > RandUtils.random()))
				{
					for (int k = 0; k < problemDimension; k++)
						oldPt[k] = newPt[k];
					fOld = fNew;
				}
			}
			
			generationIndex++;
			
			// update temperature
		    tk = alpha*tk;
		}
		
		finalBest = bestPt;
		
		FT.add(i, fNew);
		
		return FT;
	}
}
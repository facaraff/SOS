package algorithms;

import static utils.algorithms.Misc.generateRandomSolution;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

public class SA extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double alpha = this.getParameter("p0").intValue(); //0.9
		int initialSolutions = this.getParameter("p1").intValue(); //10
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[] bestPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];

		double fOld;
		double fNew;
		double fWorst;
		double fBest;

		// initialize first point
		bestPt = generateRandomSolution(bounds, problemDimension);
		fNew = problem.f(bestPt);
		FT.add(0, fNew);
		fBest = fNew;
		fWorst = fNew;
		fOld = fNew;

		// evaluate initial solutions to set the initial temperature
		for (int j = 0; j < initialSolutions; j++)
		{
			newPt = generateRandomSolution(bounds, problemDimension);
			fNew = problem.f(newPt);

			// update best
			if (fNew < fBest)
			{
				FT.add(j+1, fNew);
				fBest = fNew;
			}

			// update worst
			if (fNew <= fOld)
			{
				for (int k = 0; k < problemDimension; k++)
					bestPt[k] = newPt[k];
				fOld = fNew;
			}
			else
				fWorst = fNew;			
		}

		// initialize temperature
		double delt0 = fWorst-fOld;
		double accept0 = 0.9;
		double T0 = -delt0/Math.log(accept0);		
		double tk = T0;

		int i = initialSolutions+1;

		//int generationIndex = 1;
		//int totalGenerations = (maxEvaluations-i);
		//int generationIndex = 1;
		
		while (i < maxEvaluations)
		{

			//basic SA sampling
			newPt = generateRandomSolution(bounds, problemDimension);

			// evaluate fitness
			fNew = problem.f(newPt);
			i++;

			// update best
			if (fNew < fBest)
			{
				FT.add(i, fNew);
				fBest = fNew;
			}

			// move to the neighbor point
			if ((fNew <= fOld) || (Math.exp((fOld-fNew)/tk) > RandUtils.random()))
			{
				for (int k = 0; k < problemDimension; k++)
					bestPt[k] = newPt[k];
				fOld = fNew;
			}


			//generationIndex++;

			// update temperature
			tk = alpha*tk;
		}
		
		finalBest = bestPt;
		
		FT.add(i, fBest);

		return FT;
	}
}

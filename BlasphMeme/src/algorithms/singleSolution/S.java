package algorithms.singleSolution;


import utils.algorithms.Misc;

import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;


public class S extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
 
		int deepLSSteps = getParameter("p0").intValue(); //150;
		double deepLSRadius = getParameter("p1").doubleValue();//0.4;
		boolean restart = false;
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();


		// current FT
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
			best = Misc.generateRandomSolution(bounds, problemDimension);		
			fBest = problem.f(best);
			i++;
		}
		
		
		while (i < maxEvaluations)
		{
		
			double[] SR = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
				SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;
			boolean improve = true;
			int j = 0;
			
			double[] temp = new double[problemDimension];
			double fTemp = Double.NaN;
			if(restart)
			{
				for (int k = 0; k < problemDimension; k++)
					temp[k] = best[k];
				fTemp = fBest;
			}
			else
			{
				temp = Misc.generateRandomSolution(bounds, problemDimension);
				fTemp = problem.f(temp);
				i++;
				if (fTemp < fBest)
				{
					fBest = fTemp;
					for (int n = 0; n < problemDimension; n++)
						best[n] = temp[n];
				}
			}
				
			
			while ((j < deepLSSteps) && (i < maxEvaluations))
			{
				double[] Xk = new double[problemDimension];
				double[] Xk_orig = new double[problemDimension];
				for (int k = 0; k < problemDimension; k++)
				{
					Xk[k] = temp[k];
					Xk_orig[k] = temp[k];
				}

				if (!improve)
				{
					for (int k = 0; k < problemDimension; k++)
						SR[k] = SR[k]/2;
				}
				improve = false;
				int k = 0;
				while ((k < problemDimension) && (i < maxEvaluations))
				{
					Xk[k] = Xk[k] - SR[k];
					Xk = Misc.toro(Xk, bounds);
					double fXk = problem.f(Xk);
					i++;
					// FT update
					if (fXk < fBest)
					{
						fBest = fXk;
						for (int n = 0; n < problemDimension; n++)
							best[n] = Xk[n];
						
						improve = true;
					}
					else if(i<maxEvaluations)
					{
						
						Xk[k] = Xk_orig[k];
						Xk[k] = Xk[k] + 0.5*SR[k];
						Xk = Misc.toro(Xk, bounds);
						fXk = problem.f(Xk);
						//dCounter++;
						i++;
						// FT update
						if (fXk < fBest)
						{
							fBest = fXk;
							for (int n = 0; n < problemDimension; n++)
								best[n] = Xk[n];
							improve = true;
						}
						else
							Xk[k] = Xk_orig[k];
						
						
					}
					
					k++;
				}
				
				j++;
			}
		}

		finalBest = best;
		FT.add(i,fBest);
		return FT;
	}
}
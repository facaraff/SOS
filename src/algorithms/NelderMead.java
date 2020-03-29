package algorithms;

import static utils.algorithms.Misc.generateRandomSolution;

import utils.MatLab;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/**
 * A Nelder-Mead simplex search.
 */
public final class NelderMead extends Algorithm {

	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double alpha = getParameter("p0").doubleValue();	// 1
		double beta  = getParameter("p1").doubleValue();	// 0.5
		double gamma = getParameter("p2").doubleValue();	// 2
		double delta = getParameter("p3").doubleValue();	// 0.5
		
		FTrend FT = new FTrend();
		
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		double[][] simplex = new double[problemDimension+1][problemDimension];
		double[] fSimplex = new double[problemDimension+1];
		
		double[][] iSimplex = new double[problemDimension+1][2];

		double[] best = new double[problemDimension];
		double fBest = Double.POSITIVE_INFINITY;
		
		// evaluate the function at each point in simplex,
		for (int i = 0; i < (problemDimension+1); i++)
		{
			if (initialSolution != null && i == 0)
			{
				simplex[0] = initialSolution;
				fSimplex[0]=initialFitness;
			}
			else
			{
				simplex[i] = generateRandomSolution(problem.getBounds(), problemDimension);
				fSimplex[i] = problem.f(simplex[i]);
			}
			
			iSimplex[i][0] = fSimplex[i];
			iSimplex[i][1] = i;
			
			if (i == 0 || fSimplex[i] < fBest)
			{
				fBest = fSimplex[i];
				for (int j = 0; j < problemDimension; j++)
					best[j] = simplex[i][j];
				FT.add(i, fBest);
			}
		}

		double[] reflect = new double[problemDimension];
		double[] expand = new double[problemDimension];
		double[] contract = new double[problemDimension];
		double reflectVal;
		double expandVal;
		double contractVal;
		
		double[] mean = new double[problemDimension];
		
		int l;
		int h;
		int s;
		double total;
		
		int i = problemDimension+1;
		while (i < maxEvaluations)
		{
			for (int k = 0; k < (problemDimension+1); k++)
			{
				iSimplex[k][0] = fSimplex[k];
				iSimplex[k][1] = k;
			}
			
			// sort the points and get the best, worst, and next to worst
			MatLab.sortRows(iSimplex, 0);
			
			l  = (int)iSimplex[0][1];					// best
			h  = (int)iSimplex[problemDimension][1];	// worst
			s = (int)iSimplex[problemDimension-1][1];	// second worst

			// sum all but the worst point
			for (int j = 0; j < problemDimension; j++)
			{
				total = 0;
				for (int k = 0; k < (problemDimension+1); k++)
				{
					if (k != h)
						total += simplex[k][j];
				}

				mean[j] = total/problemDimension;
			}

			// reflect
			for (int j = 0; j < problemDimension; j++)
				reflect[j] = mean[j] + alpha*(mean[j] - simplex[h][j]);
			reflect = correct(reflect, bounds);
			reflectVal = problem.f(reflect);
			i++;
			
			if (reflectVal < fBest)
			{
				fBest = reflectVal;
				for (int j = 0; j < problemDimension; j++)
					best[j] = reflect[j];
				FT.add(i, fBest);
			}
			
			if (i >= maxEvaluations)
				break;

			
			if (reflectVal < iSimplex[l][0])
			{
				// expand
				for (int j = 0; j < problemDimension; j++)
					expand[j] = mean[j] + gamma*(reflect[j] - mean[j]);
				expand = correct(expand, bounds);
				expandVal = problem.f(expand);
				i++;
				
				if (expandVal < fBest)
				{
					fBest = expandVal;
					for (int j = 0; j < problemDimension; j++)
						best[j] = expand[j];
					FT.add(i, fBest);
				}
				
				if (i >= maxEvaluations)
					break;
				
				// check if reflect or expand is better
				if (expandVal < reflectVal)
				{
					for (int j = 0; j < problemDimension; j++)
						simplex[h][j] = expand[j];
					fSimplex[h] = expandVal;
				}
				else
				{
					for (int j = 0; j < problemDimension; j++)
						simplex[h][j] = reflect[j];
					fSimplex[h] = reflectVal;
				}
			}
			else if (reflectVal >= iSimplex[l][0] && reflectVal < iSimplex[s][0])
			{
				// accept reflect point
				for (int j = 0; j < problemDimension; j++)
					simplex[h][j] = reflect[j];
				fSimplex[h] = reflectVal;
			}
			else if (reflectVal >= iSimplex[s][0] && reflectVal < iSimplex[h][0])
			{
				// contract outside
				for (int j = 0; j < problemDimension; j++)
					contract[j] = mean[j] + beta*(reflect[j] - mean[j]);
				contract = correct(contract, bounds);
				contractVal = problem.f(contract);
				i++;

				if (contractVal < fBest)
				{
					fBest = contractVal;
					for (int j = 0; j < problemDimension; j++)
						best[j] = contract[j];
					FT.add(i, fBest);
				}

				if (i >= maxEvaluations)
					break;

				if (contractVal <= reflectVal)
				{
					for (int j = 0; j < problemDimension; j++)
						simplex[h][j] = contract[j];
					fSimplex[h] = contractVal;
				}
				else
				{
					// shrink
					for (int k = 0; k < (problemDimension+1) && i < maxEvaluations; k++)
					{
						if (k != l)
						{
							for (int j = 0; j < problemDimension; j++)
								simplex[k][j] = simplex[l][j]+delta*(simplex[k][j]-simplex[l][j]);
							simplex[k] = correct(simplex[k], bounds);
							fSimplex[k] = problem.f(simplex[k]);
							i++;
							
							if (fSimplex[k] < fBest)
							{
								fBest = fSimplex[k];
								for (int j = 0; j < problemDimension; j++)
									best[j] = simplex[k][j];
								FT.add(i, fBest);
							}
						}
					}
				}
			}
			else if (reflectVal >= iSimplex[h][0])
			{
				// contract inisde
				for (int j = 0; j < problemDimension; j++)
					contract[j] = mean[j] + beta*(simplex[h][j] - mean[j]);
				contract = correct(contract, bounds);
				contractVal = problem.f(contract);
				i++;

				if (contractVal < fBest)
				{
					fBest = contractVal;
					for (int j = 0; j < problemDimension; j++)
						best[j] = contract[j];
					FT.add(i, fBest);
				}

				if (i >= maxEvaluations)
					break;

				if (contractVal <= iSimplex[h][0])
				{
					for (int j = 0; j < problemDimension; j++)
						simplex[h][j] = contract[j];
					fSimplex[h] = contractVal;
				}
				else
				{
					// shrink
					for (int k = 0; k < (problemDimension+1) && i < maxEvaluations; k++)
					{
						if (k != l)
						{
							for (int j = 0; j < problemDimension; j++)
								simplex[k][j] = simplex[l][j]+delta*(simplex[k][j]-simplex[l][j]);
							simplex[k] = correct(simplex[k], bounds);
							fSimplex[k] = problem.f(simplex[k]);
							i++;
							
							if (fSimplex[k] < fBest)
							{
								fBest = fSimplex[k];
								for (int j = 0; j < problemDimension; j++)
									best[j] = simplex[k][j];
								FT.add(i, fBest);
							}
						}
					}
				}
			}
		}
		
		finalBest = best;
		FT.add(i, fBest);
		
		return FT;
	}
}
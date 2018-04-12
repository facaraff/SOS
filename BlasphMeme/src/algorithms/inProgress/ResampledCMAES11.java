package algorithms.inProgress;


import algorithms.singleSolution.CMAES_11;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.MatLab.min;
import utils.RunAndStore.FTrend;

import interfaces.Algorithm;
import interfaces.Problem;

public class ResampledCMAES11 extends Algorithm
{	
	boolean verbose = false;

	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		int i;

		double[] best; 
		double fBest;

		FTrend FT = new FTrend();
		
		i = 0;
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
		FT.add(i, fBest);
		
		
		// INITIALISE MEMES//
		
	
		double globalCR = getParameter("p0").doubleValue(); //0.95
	
		CMAES_11 cma11 = new CMAES_11();
		cma11.setParameter("p0",getParameter("p1").doubleValue()); // 2/11
		cma11.setParameter("p1",getParameter("p2").doubleValue());  // 1/12
		cma11.setParameter("p2",getParameter("p3").doubleValue()); // 0.44
		cma11.setParameter("p3",getParameter("p4").doubleValue()); // 1 --> problem dependent!!
		
		
		double maxB = getParameter("p5").doubleValue();
		
		FTrend ft = null;
		
//		int  budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
		
		double[] xTemp;
		double fTemp;
		
		while (i < maxEvaluations)
		{
			xTemp = generateRandomSolution(bounds, problemDimension);
			xTemp = crossOverExp(best, xTemp, globalCR);
			fTemp = problem.f(xTemp);
			i++;
			if(fTemp<fBest)
			{
				fBest = fTemp;
				for(int n=0;n<problemDimension; n++)
					best[n] = xTemp[n];
				FT.add(i, fBest);
			}
			
				if (verbose) System.out.println("C start point: "+fBest);
				cma11.setInitialSolution(xTemp);
				cma11.setInitialFitness(fTemp);
				int  budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
				ft = cma11.execute(problem, budget);
				xTemp = cma11.getFinalBest();
				fTemp = ft.getLastF();
				if (verbose) System.out.println("C final point: "+fBest);
				FT.merge(ft, i);
				i+=budget;
				if (verbose) System.out.println("C appended point: "+FT.getLastF());
				if(fTemp<fBest)
				{
					fBest = fTemp;
					for(int n=0;n<problemDimension; n++)
						best[n] = xTemp[n];
				}
			
		}
		
		FT.add(i,fBest);
		finalBest = best;
		return FT;
			
			
	}
}
			
			
						
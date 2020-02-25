package algorithms.inProgress;

import algorithms.memes.LineMinimization;
//import algorithms.singleSolution.S;
import algorithms.singleSolution.CMAES_11;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.MatLab.min;
import utils.RunAndStore.FTrend;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;

public class ResampledBrentCMAES11 extends Algorithm
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
		cma11.setSavematrix(true);
		
//		S s = new S();
//		s.setParameter("p0", getParameter("p8").doubleValue()); //150;
//		s.setParameter("p1", getParameter("p9").doubleValue()); //0.4;
		
		LineMinimization brent = new LineMinimization();
		int brentBudget = getParameter("p5").intValue();//100
		if(brentBudget==Double.NaN) brentBudget = problemDimension;
		double maxB = getParameter("p6").doubleValue();
		
		
		FTrend ft = null;
		
		int  budget; 
		
		double[] xTemp = new double[problemDimension];
		for(int n=0;n<problemDimension; n++)
			xTemp[n] = best[n];
		double fTemp = fBest;
		
		boolean matrixDoNotExists = true;
		
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
			
			if (RandUtils.random() > 0.5 || matrixDoNotExists)
			{
				budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
				if (verbose) System.out.println("C start point: "+fBest);
				cma11.setInitialSolution(xTemp);
				cma11.setInitialFitness(fTemp);
//				budget = (int)(MatLab.min(maxB*maxEvaluations, maxEvaluations-i));
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
				matrixDoNotExists = false;
			}
			else
			{
				double[][] m = cma11.getMatrix();
//				for(int c=0; c<m.length;c++)
//				{
//					for(int b=0; b<m[0].length;b++)
//						System.out.print(m[c][b]+"\t");
//					System.out.println();
//				}
//				System.out.println();
				
				for(int k=0; k<m.length;k++)
				{
					brent.setInitialSolution(xTemp);
					brent.setInitialFitness(fTemp);
//					System.out.println(m[k]==null);
					brent.setXI(m[k]);
					budget = (int)(min(brentBudget, maxEvaluations-i)); if(budget<0) budget = 0;
					ft = brent.execute(problem, budget);
					xTemp = brent.getFinalBest();
					fTemp = ft.getLastF();
					if (verbose) System.out.println("brent final point: "+ft.getLastF());
					FT.merge(ft, i);
//					System.out.println(i+" "+ft.getLastI()+" "+budget+" "+FT.getLastI());
					i+=budget;
					if (verbose) System.out.println("brent appended point: "+FT.getLastF());
					if(fTemp<fBest)
					{
						fBest = fTemp;
						for(int n=0;n<problemDimension; n++)
							best[n] = xTemp[n];
					}
				}
				m = null;
			}
			//if (verbose) System.out.println("pippo");
		}
		
		FT.add(i,fBest);
		finalBest = best;
		return FT;
			
			
	}
}
			
			
						
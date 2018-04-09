package algorithms.inProgress;

import algorithms.singleSolution.S;
import algorithms.singleSolution.NonUniformSA;
import algorithms.singleSolution.CMAES_11;

import static utils.algorithms.Misc.generateRandomSolution;


import static utils.MatLab.min;
import utils.RunAndStore.FTrend;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;

public class inprogressAlg extends Algorithm
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
		
//		L l = new L();
//		l.setParameter("p0",getParameter("p0").doubleValue()); //0.95
		
		NonUniformSA nusa = new NonUniformSA();
		nusa.setParameter("p0",getParameter("p0").doubleValue()); //5
		nusa.setParameter("p1", getParameter("p1").doubleValue()); //0.9
		nusa.setParameter("p2", getParameter("p2").doubleValue()); //3
		nusa.setParameter("p3", getParameter("p3").doubleValue()); //10
		
		
		CMAES_11 cma11 = new CMAES_11();
		cma11.setParameter("p0",getParameter("p4").doubleValue()); // 2/11
		cma11.setParameter("p1",getParameter("p5").doubleValue());  // 1/12
		cma11.setParameter("p2",getParameter("p6").doubleValue()); // 0.44
		cma11.setParameter("p3",getParameter("p7").doubleValue()); // 1 --> problem dependent!!
		
		
		S s = new S();
		s.setParameter("p0", getParameter("p8").doubleValue()); //150;
		s.setParameter("p1", getParameter("p9").doubleValue()); //0.4;
		
		
		double maxB = getParameter("p10").doubleValue();
		
		FTrend ft = null;
		
		int  budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
		
		double[] xTemp = new double[problemDimension];
		for(int n=0;n<problemDimension; n++)
			xTemp[n] = best[n];
		double fTemp = fBest;
		
		while (i < maxEvaluations)
		{
			if (verbose) System.out.println("L start point: "+fBest);
			nusa.setInitialSolution(xTemp);
			nusa.setInitialFitness(fTemp);
//			budget = (int)(MatLab.min(maxB*maxEvaluations, maxEvaluations-i));
			ft = nusa.execute(problem, budget/4);
			i+=FT.getLastI();
			xTemp = nusa.getFinalBest();
			fTemp = ft.getLastF();
			if (verbose) System.out.println("L final point: "+fBest);
			FT.append(ft, i);
			if (verbose) System.out.println("L appended point: "+FT.getLastF());
			if(fTemp<fBest)
			{
				fBest = fTemp;
				for(int n=0;n<problemDimension; n++)
					best[n] = xTemp[n];
			}
			
			//if (RandUtils.random() > 0.5)
			{
				if (verbose) System.out.println("C start point: "+fBest);
				cma11.setInitialSolution(xTemp);
				cma11.setInitialFitness(fTemp);
//				budget = (int)(MatLab.min(maxB*maxEvaluations, maxEvaluations-i));
				ft = cma11.execute(problem, budget);
				i+=FT.getLastI(); 
				xTemp = cma11.getFinalBest();
				fTemp = ft.getLastF();
				if (verbose) System.out.println("C final point: "+fBest);
				FT.append(ft, i);
				if (verbose) System.out.println("C appended point: "+FT.getLastF());
				if(fTemp<fBest)
				{
					fBest = fTemp;
					for(int n=0;n<problemDimension; n++)
						best[n] = xTemp[n];
				}
			}
			//else
			{
				if (verbose) System.out.println("N start point: "+FT.getLastF());
				s.setInitialSolution(xTemp);
				s.setInitialFitness(fTemp);
//				budget = (int)(MatLab.min(maxB*maxEvaluations, maxEvaluations-i));
				ft = s.execute(problem, 0);
				i+=FT.getLastI();
				xTemp = s.getFinalBest();
				fTemp = ft.getLastF();
				if (verbose) System.out.println("N final point: "+FT.getLastF());
				FT.append(ft, i);
				if (verbose) System.out.println("N appended point: "+FT.getLastF());
				if(fTemp<fBest)
				{
					fBest = fTemp;
					for(int n=0;n<problemDimension; n++)
						best[n] = xTemp[n];
				}
			}
			//if (verbose) System.out.println("pippo");
		}
		
		FT.add(i,fBest);
		finalBest = best;
		return FT;
			
			
	}
}
			
			
						
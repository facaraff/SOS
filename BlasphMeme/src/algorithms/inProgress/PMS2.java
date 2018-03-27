package algorithms.inProgress;

import algorithms.memes.L;
import algorithms.singleSolution.NonUniformSA;
import algorithms.singleSolution.CMAES_11;

import static utils.algorithms.Misc.generateRandomSolution;


import static utils.MatLab.min;
import utils.RunAndStore.FTrend;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;

public class PMS2 extends Algorithm
{	

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
		
		L l = new L();
		l.setParameter("p0",getParameter("p0").doubleValue()); //0.95
		
		NonUniformSA nusa = new NonUniformSA();
		nusa.setParameter("p0",getParameter("p1").doubleValue()); //5
		nusa.setParameter("p1", getParameter("p2").doubleValue()); //0.9
		nusa.setParameter("p2", getParameter("p3").doubleValue()); //3
		nusa.setParameter("p3", getParameter("p4").doubleValue()); //10
		
		
		CMAES_11 cma11 = new CMAES_11();
		cma11.setParameter("p0",getParameter("p5").doubleValue()); // 2/11
		cma11.setParameter("p1",getParameter("p6").doubleValue());  // 1/12
		cma11.setParameter("p2",getParameter("p7").doubleValue()); // 0.44
		cma11.setParameter("p3",getParameter("p8").doubleValue()); // 1 --> problem dependent!!
		
		
		double maxB = getParameter("p9").doubleValue();
		
		FTrend ft = null;
		
		int  budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
		
		while (i < maxEvaluations)
		{
			System.out.println("L start point: "+fBest);
			l.setInitialSolution(best);
			l.setInitialFitness(fBest);
//			budget = (int)(MatLab.min(maxB*maxEvaluations, maxEvaluations-i));
			ft = l.execute(problem, budget);
			i+=FT.getLastI();
			best = l.getFinalBest();
			fBest = ft.getLastF();
			System.out.println("L final point: "+fBest);
			FT.append(ft, i);
			System.out.println("L appended point: "+FT.getLastF());
			if (RandUtils.random() > 0.5)
			{
				System.out.println("C start point: "+fBest);
				cma11.setInitialSolution(best);
				cma11.setInitialFitness(fBest);
//				budget = (int)(MatLab.min(maxB*maxEvaluations, maxEvaluations-i));
				ft = cma11.execute(problem, budget);
				i+=FT.getLastI(); 
				best = cma11.getFinalBest();
				fBest = ft.getLastF();
				System.out.println("C final point: "+fBest);
				FT.append(ft, i);
				System.out.println("C appended point: "+FT.getLastF());
			}
			else
			{
				System.out.println("N start point: "+FT.getLastF());
				nusa.setInitialSolution(best);
				nusa.setInitialFitness(fBest);
//				budget = (int)(MatLab.min(maxB*maxEvaluations, maxEvaluations-i));
				ft = nusa.execute(problem, budget);
				i+=FT.getLastI();
				best = nusa.getFinalBest();
				fBest = ft.getLastF();
				System.out.println("N final point: "+FT.getLastF());
				FT.append(ft, i);
				System.out.println("N appended point: "+FT.getLastF());
			}
			//System.out.println("pippo");
		}
		
		FT.add(i,fBest);
		finalBest = best;
		return FT;
			
			
	}
}
			
			
						
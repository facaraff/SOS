package algorithms.singleSolution;


import utils.algorithms.Misc;
import static utils.MatLab.multiply;
import static utils.MatLab.sum;


import java.util.Arrays;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

public class SPSA extends Algorithm {
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		
		double a=getParameter("p0").doubleValue(); //a = 0.5
		double A=getParameter("p1").doubleValue(); //A = 1  "(SPALL 1998) a should be much less (usually 10% or less) than maxevaluations" 
		double alpha=getParameter("p2").doubleValue(); //alpha = 0.602
		double c=getParameter("p3").doubleValue();  //c = 0.032
		double gamma=getParameter("p4").doubleValue(); //gamma = 0.1
		double myEps=0.01;
		//int k=0;

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		double[] best;
		double fBest;
		int  i = 0;
		if (initialSolution != null)
		{
			best = initialSolution;
			fBest = initialFitness;
			FT.add(i, fBest);
		}
		else
		{
			best = Misc.generateRandomSolution(bounds, problemDimension);
			fBest= problem.f(best);
			i++;
			
		}
		
		double[] theta = Misc.clone(best);
		double y = fBest;
		
		int stopcounter=0;

		while(i < maxEvaluations)
		{
			double ak = a/Math.pow((i+1+A),alpha);
			double ck = c/Math.pow((i+1),gamma);
			double[] delta = new double[problemDimension];
			for(int j=0;j < problemDimension;j++)
				delta[j] = (RandUtils.random() > 0.5) ? 1 : -1;
			double[] thetaplus = Misc.toro(sum(theta,multiply(ck,delta)), bounds);
			double[] thetaminus = Misc.toro(sum(theta,multiply(-ck,delta)), bounds);
			double yplus=problem.f(thetaplus);
			double yminus=problem.f(thetaminus);
			i +=2; 
			double[] ghat= new double[problemDimension];
			for(int j=0;j < problemDimension;j++)
				ghat[j]=(yplus-yminus)/(2*ck*delta[j]);
			theta = Misc.toro(sum(theta,multiply(-ak, ghat)), bounds);
			y = problem.f(theta);
			i++;
			double[] ys={y, yplus, yminus};
			double[] ys0={y, yplus, yminus};
			Arrays.sort(ys);
			double yFinal = ys[0]; ys = null;
//			double yOld = fBest;
			if(yFinal == ys0[1])
				theta = thetaplus;
			if(yFinal == ys0[2])
				theta = thetaminus;
			double DELTA = 0;
			if(fBest > yFinal)
			{
				DELTA = fBest - yFinal;
				fBest = yFinal;
				best = Misc.clone(theta);
				FT.add(i, fBest);	
			}
			if(i>5)
				if(Math.abs(DELTA) < myEps)
					stopcounter++;
			if(stopcounter>10)
			{
				break;
			}
			
			//k++;
		}

	
		
	finalBest = best;
	FT.add(i, fBest);

	return FT;
	}
}
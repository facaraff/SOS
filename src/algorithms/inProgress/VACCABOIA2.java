
/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/package algorithms.inProgress;


import algorithms.compact.cDE_exp_light;
import algorithms.singleSolution.NonUniformSA;


import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.MatLab.min;
import utils.RunAndStore.FTrend;
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;

public class VACCABOIA2 extends Algorithm
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

		cDE_exp_light cde = new cDE_exp_light();
		cde.setParameter("p0", 300.0);
		cde.setParameter("p1", 0.25);
		cde.setParameter("p2", 0.5);
		cde.setParameter("p3", 3.0);
		cde.setParameter("p4", 1.0);
		cde.setParameter("p5", 1.0);	
		
		NonUniformSA nusa = new NonUniformSA();
		nusa.setParameter("p0",5.0);
		nusa.setParameter("p1", 0.9);
		nusa.setParameter("p2", 3.0);
		nusa.setParameter("p3", 10.0);
		
		
		double maxB = getParameter("p1").doubleValue();//0.2
//		int maxB = getParameter("p1").intValue();//
		
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
			
				if(RandUtils.random() > 0.5)
				{
					if (verbose) System.out.println("C start point: "+fBest);
					cde.setInitialSolution(xTemp);
					cde.setInitialFitness(fTemp);
//					int  budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
					int  budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
					ft = cde.execute(problem, budget);
					xTemp = cde.getFinalBest();
					fTemp = ft.getLastF();
					if (verbose) System.out.println("C final point: "+fBest);
					FT.merge(ft, i);
					i+=budget;
					if (verbose) System.out.println("C appended point: "+FT.getLastF());
				}
				else
				{
					if (verbose) System.out.println("C start point: "+fBest);
					nusa.setInitialSolution(xTemp);
					nusa.setInitialFitness(fTemp);
//					int  budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
					int  budget = (int)(min(maxB*maxEvaluations, maxEvaluations-i));
					ft = nusa.execute(problem, budget);
					xTemp = nusa.getFinalBest();
					fTemp = ft.getLastF();
					if (verbose) System.out.println("C final point: "+fBest);
					FT.merge(ft, i);
					i+=budget;
					if (verbose) System.out.println("C appended point: "+FT.getLastF());
				}
				
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
			
			
						
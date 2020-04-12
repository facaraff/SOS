package algorithms.specialOptions.BIAS.singleSolutions;


import static utils.algorithms.operators.ISBOp.generateRandomSolution;


import static utils.MatLab.subtract;
import static utils.MatLab.sum;

import utils.random.RandUtilsISB;

import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.algorithms.Counter;


public class SolisWets extends AlgorithmBias
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
 
		double rho = getParameter("p0").doubleValue();//???;
	
		String FullName = getFullName("SWA"+this.correction,problem); 
		Counter PRGCounter = new Counter(0);
		createFile(FullName);
		String line = new String();
				
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		
		
		
		int prevID = -1;
		int newID = 0;
		
		writeHeader(" rho "+rho, problem);
		//prevID = newID;

		// current best
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
			best = generateRandomSolution(bounds, problemDimension, PRGCounter);		
			fBest = problem.f(best);
			i++;newID++;
			
			line =""+newID+" "+formatter(fBest)+" "+i+" "+prevID;
			for(int k = 0; k < problemDimension; k++)
				line+=" "+formatter(best[k]);
			line+="\n";
			bw.write(line);
			line = null;
			line = new String();
			prevID = newID;
		}

		
		int numSuccess = 0;
		int numFailed = 0;

		double newfBest;
		double[] dif = new double[problemDimension];
		double[] bestFirst = new double[problemDimension];
		double[] bestSecond = new double[problemDimension];
		double[] bias = new double[problemDimension];

		

		while (i < maxEvaluations )
		{	
			for(int n=0; n < problemDimension; n++)
				dif[n] = RandUtilsISB.gaussian(0,rho,PRGCounter);
			bestFirst = sum(sum(best, bias ) , dif );
			
			
			bestFirst = correct(bestFirst, best, bounds);
			
			
			newfBest = problem.f(bestFirst);
			i++;
			
			if(newfBest < fBest)
			{
				fBest = newfBest;
				//bias = sum( multiply(0.2, bias) , multiply( 0.4, sum(dif, bias) )  );
				for(int n=0; n < problemDimension; n++)
				{
					best[n] = bestFirst[n];
					bias[n] = 0.2*bias[n] + 0.4*(dif[n] + bias[n]);
				}
				if(i%problemDimension==0) FT.add(i, fBest);
				numSuccess++;
				numFailed = 0;
				
				line =""+newID+" "+formatter(fBest)+" "+i+" "+prevID;
				for(int k = 0; k < problemDimension; k++)
					line+=" "+formatter(best[k]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				prevID = newID;
				
			}
			else if(i < maxEvaluations)
			{
				bestSecond = subtract(  subtract(best, bias) , dif  );
				
				bestSecond = correct(bestSecond, best, bounds);
				
				newfBest = problem.f(bestSecond);
				i++;
				if(newfBest < fBest)
				{
					fBest = newfBest;
					//bias = subtract( bias, multiply(0.4, sum(dif, bias)) );
					for(int n=0; n < problemDimension; n++)
					{
						best[n] = bestSecond[n];
						bias[n] = bias[n] - 0.4*(dif[n] + bias[n]);
					}
					if(i%problemDimension==0) FT.add(i, fBest);
					numSuccess++;
					numFailed = 0;
					
					line =""+newID+" "+formatter(fBest)+" "+i+" "+prevID;
					for(int k = 0; k < problemDimension; k++)
						line+=" "+formatter(best[k]);
					line+="\n";
					bw.write(line);
					line = null;
					line = new String();
					prevID = newID;
				}
				else
				{
					numFailed++;
					numSuccess = 0;
				}
			}
			
			if(numSuccess > 5)
			{
				rho = rho*2;
				numSuccess = 0;
			}
			else if(numFailed > 3)
			{
				rho = rho/2;
				numFailed = 0;
			}
		}

		finalBest = best;
		FT.add(i, fBest);
		//wrtiteCorrectionsPercentage(fileName, (double) this.numberOfCorrections/maxEvaluations,"correctionsSingleSol");
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(), "correctionsSingleSol");
		return FT;
	}
}
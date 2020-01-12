package algorithms.specialOptions.BIAS.singleSolutions;


import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.saturation;
import static utils.algorithms.Misc.toro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

import static utils.MatLab.subtract;
import static utils.MatLab.sum;

import utils.random.RandUtils;

import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;


public class SolisWets extends AlgorithmBias
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
 
		double rho = getParameter("p0").doubleValue();//???;
	
		String fileName = "SWA"+this.correction; 
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		fileName+="D"+problem.getDimension()+"f0-"+(run+1);
		File file = new File(Dir+fileName+".txt");
		if (!file.exists()) 
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		int prevID = -1;
		int newID = 0;
		int ciccio = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);	
		String line = "# function 0 dim "+problemDimension+" rho "+rho+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
		bw.write(line);
		line = null;
		line = new String();
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
			best = generateRandomSolution(bounds, problemDimension);		
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
				dif[n] = RandUtils.gaussian(0,rho);
			bestFirst = sum(sum(best, bias ) , dif );
			
			
			
			double[] output = new double[problemDimension];
			if(this.correction == 't')
			{
				//System.out.println("TORO");
				output = toro(bestFirst, bounds);
			}
			else if(this.correction== 's')
			{
				//System.out.println("SAT");
				output = saturation(bestFirst, bounds);
			}
			else if(this.correction== 'd')
			{
				output = toro(bestFirst, bounds);
				if(!Arrays.equals(output, bestFirst))
					output = best;
			}
			else
				System.out.println("No bounds handling shceme seleceted");
			
			if(!Arrays.equals(output, bestFirst))
			{
				bestFirst = output;
				output = null;
				ciccio++;
			}
			
			
			
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
				

				output = new double[problemDimension];
				if(this.correction == 't')
				{
					//System.out.println("TORO");
					output = toro(bestSecond, bounds);
				}
				else if(this.correction== 's')
				{
					//System.out.println("SAT");
					output = saturation(bestSecond, bounds);
				}
				else if(this.correction== 'd')
				{
					output = toro(bestSecond, bounds);
					if(!Arrays.equals(output, bestSecond))
						output = best;
				}
				else
					System.out.println("No bounds handling shceme seleceted");
				
				if(!Arrays.equals(output, bestSecond))
				{
					bestSecond = output;
					output = null;
					ciccio++;
				}
				
				
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
		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations,"correctionsSingleSol");
		bw.close();
		return FT;
	}
}
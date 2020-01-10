package algorithms.specialOptions.BIAS.singleSolutions;


import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import static utils.algorithms.Misc.saturation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

public class NonUniformSA extends AlgorithmBias
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{		
		int B = this.getParameter("p0").intValue(); //5
		double alpha = this.getParameter("p1").intValue(); //0.9
		int Lk = this.getParameter("p2").intValue(); //3
		int initialSolutions = this.getParameter("p3").intValue(); //10
		
		char correctionStrategy = this.correction;  // t --> toroidal   s --> saturation  d -->  discard  e ---> penalty
		String fileName = "nuSA"+correctionStrategy; 
		
				
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[] bestPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] oldPt = new double[problemDimension];
		
		double fNew;
		double fOld;
		double fWorst;
		
		
		fileName+="D"+problem.getDimension()+"f0-"+(run+1);
		File file = new File(Dir+fileName+".txt");
		if (!file.exists()) 
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		int i = 0;
		int prevID = -1;
		int newID = 0;
		int ciccio = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);	
		String line = "# function 0 dim "+problemDimension+" B "+B+" alpha "+alpha+" Lk "+Lk+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
		bw.write(line);
		line = null;
		line = new String();
		
		// initialize first point
		if (initialSolution != null)
			bestPt = initialSolution;
		else
			bestPt = generateRandomSolution(bounds, problemDimension);
		
		fNew = problem.f(bestPt);
		fOld = fNew;
		fWorst = fNew;

		FT.add(0, fNew);
		newID++; i++;
		line =""+newID+" "+formatter(fNew)+" "+i+" "+prevID;
		for(int n = 0; n < problemDimension; n++)
			line+=" "+formatter(bestPt[n]);
		line+="\n";
		bw.write(line);
		line = null;
		line = new String();
		prevID = newID;
		
		
		// evaluate initial solutions to set the initial temperature
		for (int j = 0; j < initialSolutions; j++)
		{
			newPt = generateRandomSolution(bounds, problemDimension);
			fNew = problem.f(newPt);
			i++;
			// update best
			if (fNew < fOld)
			{
				newID++;
				FT.add(j+1, fNew);
				for (int k = 0; k < problemDimension; k++)
					oldPt[k] = newPt[k];
				fOld = fNew;
				
				line =""+newID+" "+formatter(fNew)+" "+i+" "+prevID;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(newPt[n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				prevID = newID;
			}
			
			// update worst
			if (fNew > fWorst)
			{
				fWorst = fNew;
			}
		}
		
//		for (int k = 0; k < problemDimension; k++)
//			oldPt[k] = bestPt[k];
//		
		// initialize temperature
		double delt0 = fWorst-fOld;
		double accept0 = 0.9;
		double T0 = -delt0/Math.log(accept0);		
		double tk = T0;

		//i += initialSolutions;
		
		int generationIndex = 1;
		int totalGenerations = (maxEvaluations-i)/Lk;
		
		while (i < maxEvaluations)
		{
			for (int j = 0; j < Lk && i < maxEvaluations; j++)
			{
				// non-uniform mutation
				for (int k = 0; k < problemDimension; k++)
				{
					//double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)i/maxEvaluations, B));
					double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)generationIndex/totalGenerations, B));

					if (RandUtils.random()<0.5)
						newPt[k] = oldPt[k] - (oldPt[k]-problem.getBounds()[k][0])*(1-temp);
					else
						newPt[k] = oldPt[k] + (problem.getBounds()[k][1]-oldPt[k])*(1-temp);
				}
				
//				// evaluate fitness
//				newPt = toro(newPt, bounds);
//				fNew = problem.f(newPt);
//				i++;
				
				double[] output = new double[problemDimension];
				if(correctionStrategy == 't')
				{
					//System.out.println("TORO");
					output = toro(newPt, bounds);
				}
				else if(correctionStrategy== 's')
				{
					//System.out.println("SAT");
					output = saturation(newPt, bounds);
				}
				else if(correctionStrategy== 'd')
				{
					output = toro(newPt, bounds);
					if(!Arrays.equals(output, newPt))
						output = oldPt;
				}
				else if(correctionStrategy== 'e')
				{
					output = toro(newPt, bounds);
				}
				else if(correctionStrategy== 'x')
				{
					output = newPt;
				}
				else
					System.out.println("No bounds handling shceme seleceted");
				
				if(!Arrays.equals(output, newPt))
				{
					newPt = output;
					output = null;
					ciccio++;
				}
				
				if( (correctionStrategy== 'e') && (!Arrays.equals(output, newPt)) )
				{
					fNew = 2;
					newPt = oldPt;
				}
				else
					fNew=problem.f(newPt);
				
					

				i++;

				if ((fNew <= fOld) || (Math.exp((fOld-fNew)/tk) > RandUtils.random()))
				{
					for (int k = 0; k < problemDimension; k++)
						oldPt[k] = newPt[k];
					fOld = fNew;
					
					
					newID++;
					
					line =""+newID+" "+formatter(fNew)+" "+i+" "+prevID;
					for(int n = 0; n < problemDimension; n++)
						line+=" "+formatter(newPt[n]);
					line+="\n";
					bw.write(line);
					line = null;
					line = new String();
					prevID = newID;
				}
			}
			
			generationIndex++;
			
			// update temperature
		    tk = alpha*tk;
		}
		
		finalBest = oldPt;
		FT.add(i, fOld);
		bw.close();
		
		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, "correctionsSingleSol");
		return FT;
	}
	
}
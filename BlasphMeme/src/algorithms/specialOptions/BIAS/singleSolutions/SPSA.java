/** Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
*/
package algorithms.specialOptions.BIAS.singleSolutions;


import static utils.algorithms.Misc.cloneSolution;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.MatLab.multiply;
import static utils.MatLab.sum;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

public class SPSA extends AlgorithmBias {
	
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

		
		String fileName = "SPSA"+this.correction; 
		
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
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);	
		String line = "# function 0 dim "+problemDimension+" a "+a+" A "+A+""+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
		bw.write(line);
		line = null;
		line = new String();
		
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
			best = generateRandomSolution(bounds, problemDimension);
			fBest= problem.f(best);
			i++; newID++;
			
			line =""+newID+" "+formatter(fBest)+" "+i+" "+prevID;
			for(int k = 0; k < problemDimension; k++)
				line+=" "+formatter(best[k]);
			line+="\n";
			bw.write(line);
			line = null;
			line = new String();
			prevID = newID;
			
		}
		
		double[] theta = cloneSolution(best);
		double y = fBest;
		
		int stopcounter=0;

		while(i < maxEvaluations)
		{
			double ak = a/Math.pow((i+1+A),alpha);
			double ck = c/Math.pow((i+1),gamma);
			double[] delta = new double[problemDimension];
			for(int j=0;j < problemDimension;j++)
				delta[j] = (RandUtils.random() > 0.5) ? 1 : -1;
			
			double[] thetaplus = correct(sum(theta,multiply(ck,delta)), best, bounds);
			double[] thetaminus = correct(sum(theta,multiply(-ck,delta)), best, bounds);
			
			double yplus=problem.f(thetaplus);
			double yminus=problem.f(thetaminus);
			i +=2; 
			double[] ghat= new double[problemDimension];
			for(int j=0;j < problemDimension;j++)
				ghat[j]=(yplus-yminus)/(2*ck*delta[j]);
			
			theta = correct(sum(theta,multiply(-ak, ghat)), best, bounds);
			
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
				best = cloneSolution(theta);
				FT.add(i, fBest);	
				
				newID++;
				
				line =""+newID+" "+formatter(yFinal)+" "+i+" "+prevID;
				for(int k = 0; k < problemDimension; k++)
					line+=" "+formatter(theta[k]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				prevID = newID;
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
	wrtiteCorrectionsPercentage(fileName, (double) this.numberOfCorrections/maxEvaluations, "correctionsSingleSol");
	bw.close();
	return FT;
	}
}
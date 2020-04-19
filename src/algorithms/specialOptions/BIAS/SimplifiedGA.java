/**
Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package algorithms.specialOptions.BIAS;

import static utils.algorithms.operators.ISBOp.generateRandomSolution;

import utils.algorithms.Counter;
import utils.random.RandUtilsISB;

import static utils.MatLab.subtract;
import static utils.MatLab.dot;
import static utils.MatLab.sum;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;


public class SimplifiedGA extends AlgorithmBias
{
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{	
		int populationSize = getParameter("p0").intValue(); 
		double md = getParameter("p1").doubleValue();
		double d = getParameter("p2").doubleValue();
			
		
		String FullName = getFullName("SimplifiedGA"+this.correction+"p"+populationSize,problem); 
		Counter PRNGCounter = new Counter(0);
		createFile(FullName);
		
		String line = new String();		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		int[] ids = new int[populationSize];
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		
		int newID = 0;

		writeHeader(" popSize "+populationSize+" md "+md, problem);
		
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		int parent1 = 0;
		int parent2 = 0; 
		int random = 0;
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			
			double[] tmp = generateRandomSolution(bounds, problemDimension, PRNGCounter);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			i++;
			newID++;
			ids[j] = newID;
			
			line =""+newID+" -1 "+"-1 "+formatter(fitnesses[j])+" "+i+" -1";
			for(int n = 0; n < problemDimension; n++)
				line+=" "+formatter(population[j][n]);
			line+="\n";
			bw.write(line);
			line = null;
			line = new String();
			
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
			} 
				
		}
							
		// iterate
		while (i < maxEvaluations)
		{ 	
			int[] indices = getIndices(populationSize);
			indices = RandUtilsISB.randomPermutation(indices, PRNGCounter); 
			parent1 = indices[0];
			parent2 = indices[1];
			indices = null;
			double[] alpha = new double[problemDimension];
			for(int n=0; n<problemDimension; n++)
				alpha[n] = RandUtilsISB.uniform(-d,1+d, PRNGCounter);
			
			double[] xChild = sum(population[parent1],dot(alpha, subtract(population[parent2],population[parent1])));
			
			for(int n=0; n<problemDimension; n++)
				xChild[n] += RandUtilsISB.gaussian(0, md*(bounds[n][1]-bounds[n][0]), PRNGCounter);
			xChild = correct(xChild, population[parent1], bounds);
			double fChild = problem.f(xChild);
			i++;
			
			random = RandUtilsISB.randomInteger(populationSize-1, PRNGCounter);
			if(fChild<fitnesses[random])
			{
				newID++;
				int indexRandom = ids[random];
				int indexParent1 = ids[parent1];
				int indexParent2 = ids[parent2];
				ids[random] = newID;
				for(int n=0; n<problemDimension; n++)
					population[random][n] = xChild[n];
				fitnesses[random] = fChild;
				
				
				line =""+newID+" "+indexParent1+" "+indexParent2+" "+formatter(fChild)+" "+i+" "+indexRandom;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(xChild[n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();		
			}
				
			
		}	
		
		finalBest = best;
		
		FT.add(i, fBest);
		
		closeAll();	
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRNGCounter.getCounter(), "correctionsGA");
		
		return FT;
	}
	
	
	
	public int[] getIndices(int popSize)
	{
		int[] indices = new int[popSize];
		for(int n=0; n<popSize; n++)
			indices[n] = n;
		return indices;
	}
	
	
}

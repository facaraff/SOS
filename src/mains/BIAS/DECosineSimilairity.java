/**
Copyright (c) 2021, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package mains.BIAS;


import java.util.Vector;

import algorithms.specialOptions.BIAS.ISBDE.DEPoCAndCS;
import benchmarks.Noise;
import utils.ExperimentHelper;
import interfaces.AlgorithmBias;
import interfaces.Problem;


import static utils.RunAndStore.slash;
	
public class DECosineSimilairity extends ISBMain
{	

	
	public static void main(String[] args) throws Exception
	{	
		AlgorithmBias a;
		Problem p;
		
		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
	

		ExperimentHelper expSettings = new ExperimentHelper();
		expSettings.setBudgetFactor(10000);
		expSettings.setProblemDimension(30);
		expSettings.setNrRepetition(30);
		
		int n = expSettings.getProblemDimension();
		double[][] bounds = new double[n][2];
		for(int i=0; i<n; i++)
		{
			bounds[i][0] = 0;
			bounds[i][1] = 1;
		}	
		
		p = new Noise(n, bounds);
		p.setFID("f0");
		
		problems.add(p);
		
	
		double[] populationSizes;
		double[] FSteps;
		double[] CRSteps;

					
		char[] corrections = {'h','m', 't','c','s','u'}; //'d',
		String[] DEMutations = {"ro"}; 		//String[] DEMutations = {"ro","ctbo","rt","ctro", "rtbt", "bo", "bt"};
//		char[] DECrossOvers = {'b','e'};
		char[] DECrossOvers = {'b'};
		populationSizes = new double[]{5, 20, 100};


		FSteps = new double[]{0.05,0.285,0.52,0.755,0.99};	
//		CRSteps = new double[]{0.05,0.285,0.52,0.755,0.99};	
		CRSteps = new double[]{0.05, 0.0891, 0.1283, 0.1675, 0.2067, 0.2458, 0.285, 0.52, 0.755, 0.99};	
		
		
//		CRSteps = new double[5];
//		double step =Math.ceil(((0.285-0.05)/6)*1000)/1000;
//		
//		for(int i=0;i<5;i++) 
//		{
//			CRSteps[i]= (Math.ceil(0.05+(i+1)*step*1000)/1000);
//		}
		
		

		
//settings for the first experimentation phase		
//		FSteps = new double[]{0.05, 0.916};
//		CRSteps = new double[]{0.05, 0.99};	

		
//setting used for DEOTB		
//		FSteps = new double[]{0.05, 0.266, 0.483, 0.7, 0.916, 1.133, 1.350, 1.566, 1.783, 2.0};
//		CRSteps = new double[]{0.05,0.285,0.52,0.755,0.99};	


	
		
		for (double popSize : populationSizes)
		{
			for (char correction : corrections)
			{		
				for (String mutation : DEMutations)
				{
					for (double F : FSteps)
					{
							for(char xover : DECrossOvers)
							{
								for (double CR : CRSteps)
								{
//									a = new DEPoCAndFinpos(mutation,xover,true);
									a = new DEPoCAndCS(mutation,xover,false);
									a.setID("DE");
									a.setDir("CosineSimilarity"+slash()+a.getNPC()+slash());
									a.setCorrection(correction);
									a.setParameter("p0", popSize); //Population size
									a.setParameter("p1", F); //F - scale factor
									a.setParameter("p2", CR); //CR - Crossover Ratio
									a.setParameter("p3", Double.NaN); //Alpha
									algorithms.add(a);		
									a = null;
								}
							}
						}
					
					}
				}	
			}
			
		
		execute(algorithms, problems, expSettings);	
//		System.out.println("Done and dusted!");
		}
}







		
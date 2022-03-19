/**
Copyright (c) 2022, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package mains.AlgorithmicBehaviour;


import java.util.Vector;

import algorithms.AlgorithmicBehaviour.DE_TIOBRSDIS;
import benchmarks.Noise;
import utils.ExperimentHelper;
import interfaces.AlgorithmBias;
import interfaces.ISBMain;
import interfaces.Problem;

import static utils.RunAndStore.slash;
	
public class PPSN2022 extends ISBMain
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
		expSettings.setNrRepetition(50);
		
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
		double[] CRSteps,CRStepsBin;

					
//		char[] corrections = {'h','m', 't','c','s','u'}; 
		String[] corrections = {"es","ec"}; 
		String[] DEMutations = {"ro"};
		char[] DECrossOvers = {'b','e'};
		populationSizes = new double[]{5, 20, 100};


		FSteps = new double[]{0.05,0.285,0.52,0.755,0.99};	
		CRSteps = new double[]{0.05,0.285,0.52,0.755,0.99};	
		CRStepsBin = new double[]{0.05, 0.0891, 0.1283, 0.1675, 0.2067, 0.2458, 0.285, 0.52, 0.755, 0.99};	

		
		for (double popSize : populationSizes)
		{
			for (String correction : corrections)
			{		
				for (String mutation : DEMutations)
				{
					for (double F : FSteps)
					{
							for(char xover : DECrossOvers)
							{
								if(xover=='b') CRSteps = CRStepsBin;
								
								for (double CR : CRSteps)
								{
									a = new DE_TIOBRSDIS(mutation,xover,false);
									a.setID("DE");
									a.setDir("CosineSimilarity"+slash()+a.getNPC()+slash());
									a.setSDIS(correction);
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







		
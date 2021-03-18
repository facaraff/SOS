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
package mains.BIAS.temp;


import java.util.Vector;

import algorithms.specialOptions.BIAS.ISBDE.DEPoC;
import benchmarks.Noise;
import utils.ExperimentHelper;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import mains.BIAS.ISBMain;


import static utils.RunAndStore.slash;
	
public class MainDE_BAS extends ISBMain
{	
	public static void main(String[] args) throws Exception
	{	
		AlgorithmBias a;
		Problem p;
		
		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
	
		ExperimentHelper expSettings = new ExperimentHelper();
		expSettings.setBudgetFactor(10000);
		expSettings.setNrRepetition(600);
		

		
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
		
//		char[] corrections = {'m', 't', 'c', 'd','s','u'};
		char[] corrections = {'u'};

//		String[] DEMutations = {"ro","ctbo","rt","ctro", "rtbt", "bo", "bt"};
		String[] DEMutations = {"ro"};
		
		char[] DECrossOvers = {'b','e'};
		
		double[] populationSizes = {5, 20, 100};
		
		
//		double[] FSteps = {0.05, 0.266, 0.483, 0.7, 0.916, 1.133, 1.350, 1.566, 1.783, 2.0};
//		double[] CRSteps = {0.05,0.285,0.52,0.755,0.99};	
		double[] FSteps = {0.7};
		double[] CRSteps = {0.99};	
		
//		double[] CRSteps = {0.75, 0.775, 0.8, 0.85, 0.9, 0.925, 0.95, 0.975, 0.9875, 1.00};
		

		
		

		
		for (double popSize : populationSizes)
		{
			for (char correction : corrections)
			{
				
				for (String mutation : DEMutations)
				{
					for (double F : FSteps)
					{
						if(mutation.equals("ctro"))
						{
							a = new DEPoC(mutation,'x',true);
							a.setDir("DEPOISBIAS"+slash());
							a.setCorrection(correction);
							a.setParameter("p0", popSize); //Population size
							a.setParameter("p1", F); //F - scale factor
							a.setParameter("p2", Double.NaN); //CR - Crossover Ratio
							a.setParameter("p3", Double.NaN); //Alpha
							algorithms.add(a);	
							a = null;
						}
						else
							for(char xover : DECrossOvers)
							{
								for (double CR : CRSteps)
								{
									a = new DEPoC(mutation,xover,true);
									a.setDir("DEPOISBIAS"+slash());
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





		
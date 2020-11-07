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
package mains.BIAS;


import java.util.Vector;



import algorithms.specialOptions.BIAS.ISBDE.DEPoC;
//import benchmarks.Noise;
import benchmarks.ISBSuite;
import utils.ExperimentHelper;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import mains.BIAS.ISBMain;



import static utils.RunAndStore.slash;
	
public class DEPOIS extends ISBMain
{	
	public static void main(String[] args) throws Exception
	{	
		AlgorithmBias a;
		Problem p;

		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
	
		ExperimentHelper expSettings = new ExperimentHelper();
//		expSettings.setBudgetFactor(10000);
//		expSettings.setNrRepetition(100);

		
		int n = expSettings.getProblemDimension();
		
		
//		p = new ISBSuite("h0",n);
		p = new ISBSuite("f0",n);
		
		problems.add(p);
		
//		char[] corrections = {'d','s','u'};
		char[] corrections = {'u'};
//		String[] DEMutations = {"ro"};
		char[] DECrossOvers = {'b','e'};
		double[] populationSizes = {5, 20, 100};
		
		//ISBDE setting
		double[] FSteps = {0.05, (0.05+(1.95/9.0)), (0.05+2.0*(1.95/9.0)), (0.05+3.0*(1.95/9.0)), (0.05+4.0*(1.95/9.0)),(0.05+5.0*(1.95/9.0)),(0.05+6.0*(1.95/9.0)),(0.05+7.0*(1.95/9.0)),(0.05+8.0*(1.95/9.0)),(0.05+9.0*(1.95/9.0))};	
		double[] CRSteps = {0.05, (0.05+(0.94/4.0)), (0.05+2.0*(0.94/4.0)), (0.05+3.0*(0.94/4.0)), (0.05+4.0*(0.94/4.0))};
		
		//High F-Cr values
//		double[] FSteps = {0.05, 0.266, 0.483, 0.7, 0.916, 1.133, 1.350, 1.566, 1.783, 2.0};
//		double[] CRSteps = {0.75, 0.775, 0.8, 0.85, 0.9, 0.925, 0.95, 0.975, 0.9875, 1.00};
		
			for(double popSize : populationSizes)
			{
				for (char correction : corrections)
				{
						for(char xover : DECrossOvers)
						{
							for (double F : FSteps)
							{
								for (double CR : CRSteps)
								{
									a = new DEPoC("ro",xover,true);
									a.setDir("DEPOIS"+slash());
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
				
				execute(algorithms, problems, expSettings);
				algorithms = null;
				algorithms = new Vector<AlgorithmBias>();
			}
			
			System.out.println("Done And Dusted");
		}
}
		
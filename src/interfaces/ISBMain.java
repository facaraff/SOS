/**
Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com)
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

package interfaces;

import java.util.Vector;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import utils.ExperimentHelper;
import utils.MatLab;
import utils.RunAndStore.FTrend;

public abstract class ISBMain {


	protected static double[] bias = null; //Benchmark additive bias
	
	

	protected static double runAlgorithmRepetition(AlgorithmBias algorithm, Problem problem, ExperimentHelper expSettings) throws Exception
	{
		FTrend FT = algorithm.execute(problem, (expSettings.getBudgetFactor()*problem.getDimension()));

		return FT.getLastF();
	}

	
	
	protected static void execute(Vector<AlgorithmBias> algorithms, Vector<Problem> problems, ExperimentHelper expSettings) throws Exception
	{	
		
		int algorithmIndex = 0;
		for (AlgorithmBias algorithm : algorithms)
		{
			System.out.print("" + "\t");
			String algName = algorithm.getClass().getSimpleName();
			if (algName.length() >= 8)
				System.out.print(algName + "\t");
			else
				System.out.print(algName + "\t\t");
			
			if (algorithmIndex > 0)
			{
				System.out.print("\t" + "W");
				
			}
			algorithmIndex++;
		}	
		System.out.println();
				
		double[][] finalValues;
		MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();


		int problemIndex = 0;
		for (Problem problem: problems)
		{
			System.out.print("f" + (problemIndex+1) + "\t");

			finalValues = new double[algorithms.size()][expSettings.getNrRepetitions()];
			algorithmIndex = 0;
			for (AlgorithmBias algorithm : algorithms)
				{
					for (int i = 0; i < expSettings.getNrRepetitions(); i++)
					{
						algorithm.setRun(i);
						
						double best = runAlgorithmRepetition(algorithm, problem, expSettings);
						if (bias != null)
							finalValues[algorithmIndex][i] = best - bias[problemIndex];
						else
							finalValues[algorithmIndex][i] = best;
								
					}
					System.out.print("done+\t");
					String mean = utils.RunAndStore.format(MatLab.mean(finalValues[algorithmIndex]));
					String std = utils.RunAndStore.format(MatLab.std(finalValues[algorithmIndex]));
					System.out.print(mean + " \u00B1 " + std + "\t");
					if (algorithmIndex > 0)
					{			
						double pValue = mannWhitneyUTest.mannWhitneyUTest(finalValues[0], finalValues[algorithmIndex]);
						char w = '=';
						if (pValue < 0.05)
						{
							if (MatLab.mean(finalValues[0]) < MatLab.mean(finalValues[algorithmIndex]))
								w = '+';
							else
								w = '-';
						}

						System.out.print(w + "\t");
						
					}
					algorithmIndex++;
				}

				System.out.println();
				problemIndex++;
				}
			}


}
	



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
package mains.AlgorithmicBehaviour.BIAS;


import java.util.Vector;

import algorithms.AlgorithmBehaviour.ISB_SingleSolutions.*;
import utils.ExperimentHelper;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import mains.AlgorithmicBehaviour.BIAS.ISBMain;
import benchmarks.Noise;


import static utils.RunAndStore.slash;
	
public class PPSN2020 extends ISBMain
{
	
	public static void main(String[] args) throws Exception
	{	

		AlgorithmBias a;
		Problem p;

		
	
		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
	
		ExperimentHelper expSettings = new ExperimentHelper();
		
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
		
		
		char[] corrections = {'s','t','d','m','c'};
		String[] DEMutations = {"ro","rt","ctro","bo","bt","ctbo","rtbt"};
		char[] CrossOvers = {'b','e'};
		
		for (char correction : corrections)
		{
			for (String mutation : DEMutations)
				if(mutation.equals("ctro"))
				{
					a = new cDE(mutation);
					a.setDir("COMPACTS"+slash());
					a.setCorrection(correction);
					a.setParameter("p0", 300.0);
					a.setParameter("p1", 0.25);
					a.setParameter("p2", 0.5);
					algorithms.add(a);	
					a = null;				
				}
				else
					for(char xover : CrossOvers)
						{
							a = new cDE(mutation,xover);
							a.setDir("COMPACTS"+slash());
							a.setCorrection(correction);
							a.setParameter("p0", 300.0);
							a.setParameter("p1", 0.25);
							a.setParameter("p2", 0.5);
							algorithms.add(a);	
							a = null;
						}
			
			a = new cDELight();
			a.setDir("COMPACTS"+slash());
			a.setCorrection(correction);
			a.setParameter("p0", 300.0);
			a.setParameter("p1", 0.25);
			a.setParameter("p2", 0.5);
			algorithms.add(a);	
			a = null;
			
			a = new cPSO();
			a.setDir("COMPACTS"+slash());
			a.setCorrection(correction);
			a.setParameter("p0", 50.0);
			a.setParameter("p1", 0.2);
			a.setParameter("p2", 0.07);
			a.setParameter("p3", 3.74);
			a.setParameter("p4", 1.0);
			a.setParameter("p5", 1.0);
			algorithms.add(a);
			a = null;
			
			a = new cBFO();
			a.setDir("COMPACTS"+slash());
			a.setCorrection(correction);
			a.setParameter("p0", 300.0);
			a.setParameter("p1", 0.1);
			a.setParameter("p2", 4.0);
			a.setParameter("p3", 1.0);
			a.setParameter("p4", 10.0);
			a.setParameter("p5", 2.0);
			a.setParameter("p6", 2.0);
			algorithms.add(a);
			a = null;
			
			
		}
			
		a = new cGA_real();
		a.setDir("COMPACTS"+slash());
		a.setParameter("p0",300.0);
//		a.setParameter("p1",200.0);
		a.setCorrection('x');
		algorithms.add(a);
		
		execute(algorithms, problems, expSettings);
	
		}
}

		
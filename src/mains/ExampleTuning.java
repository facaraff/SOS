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
package mains;

import algorithms.DE;
import benchmarks.RCEC2014;
import interfaces.Algorithm;
import interfaces.Problem;
import interfaces.Experiment;
	
public class ExampleTuning extends Experiment
{
	public ExampleTuning() {super("ExampleTuning");};
	
	public static void main(String[] args) throws Exception
	{	

		
	
		ExampleTuning test = new ExampleTuning();
		test.setBudgetFactor(1000);
		test.setNrRuns(10);
//		test.setMT(false); Uncomment of you want to use the single-thread mode


		
		Problem p = new RCEC2014(10,1);
		p.setFID("sphere");
		test.add(p);

		Algorithm a;
		
		char[] corrections = {'s','t','m','c'};
		String[] DEMutations = {"ro","rt","bo","bt","ctbo","rtbt"};
		char[] CrossOvers = {'b','e'};
		
		for (char correction : corrections)
		{
			for (String mutation : DEMutations)
					for(char xover : CrossOvers)
						{
							a = new DE(mutation,xover);
							a.setCorrection(correction);
							a.setParameter("p0", (double)20.0); //Population size
							a.setParameter("p1", 0.7); //F
							a.setParameter("p2", 0.5); //CR
							a.setParameter("p3", Double.NaN);
							test.add(a);	
							a = null;
						}
			
		}
		
		test.startExperiment();
	
		}
}

		
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

import algorithms.AlgorithmicBehaviour.ISB_PopBased.DE;
import algorithms.AlgorithmicBehaviour.ISB_SingleSolutions.*;
//import algorithms.specialOptions.BIAS.SimplifiedGA;
import benchmarks.Noise;
import utils.ExperimentHelper;
import interfaces.AlgorithmBias;
import interfaces.ISBMain;
import interfaces.Problem;



import static utils.RunAndStore.slash;
	
public class AnisotropyOfSB extends ISBMain
{	
	public static void main(String[] args) throws Exception
	{	
		AlgorithmBias a;
		Problem p;

		
		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
	
		ExperimentHelper expSettings = new ExperimentHelper();
		expSettings.setBudgetFactor(10000);
		expSettings.setNrRepetition(15);
		
		int n = expSettings.getProblemDimension();
		double[][] bounds = new double[n][2];
		for(int i=0; i<n; i++)
		{
			bounds[i][0] = 0;
			bounds[i][1] = 1;
		}	
		
		double[] populationSizes = {5, 20, 100};
		
		p = new Noise(n, bounds);
		p.setFID("f0");
		
		problems.add(p);
	
		for(double popSize : populationSizes)
		{
			a = new DE("bt",'b');
			a.countInfeasibleDimenions("DEb2bd");
			a.setDir("DE"+slash()+a.getNPC()+slash());
			a.setCorrection('d'); //DMISMISS
			a.setParameter("p0", popSize); //Population size
			a.setParameter("p1", 0.5); //F - scale factor
			a.setParameter("p2", -1.0); //CR - Crossover Ratio
			a.setParameter("p3", 0.25); //Alpha
			algorithms.add(a);		
			a = null;
		}
					
		a = new Powell_correct();
		a.countInfeasibleDimenions("PMs");
		a.setDir("Powell"+slash());
		a.setCorrection('s');
		a.setParameter("p0",  0.00001);
		a.setParameter("p1",  100.0);
		algorithms.add(a);	
		a=null;
			
		a = new cBFO();
		a.countInfeasibleDimenions("cBFOs");
		a.setDir("COMPACTS"+slash());
		a.setCorrection('s'); //SATURATION
		a.setParameter("p0", 300.0);
		a.setParameter("p1", 0.1);
		a.setParameter("p2", 4.0);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 10.0);
		a.setParameter("p5", 2.0);
		a.setParameter("p6", 2.0);
		algorithms.add(a);
		a = null;
		
			
	
		a = new NonUniformSA();
		a.countInfeasibleDimenions("NUSAd");
		a.setDir("NUSA"+slash());
		a.setCorrection('d');
		a.setParameter("p0",5.0);
		a.setParameter("p1",0.9);
		a.setParameter("p2",3.0);
		a.setParameter("p3",10.0);
		algorithms.add(a);
		a=null;
		
		execute(algorithms, problems, expSettings);	
			
		}
}


		
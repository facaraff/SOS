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


import algorithms.specialOptions.BIAS.singleSolutions.*;
import benchmarks.Noise;
import utils.ExperimentHelper;
import interfaces.AlgorithmBias;
import interfaces.Problem;
//import mains.BIAS.ISBMain.Noise;

import static utils.RunAndStore.slash;
	
public class TELO extends ISBMain
{
	static int nrRepetitions = 50;
	static int budgetFactor = 10000;
	static int problemDimension = 30;
	

	
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
		
		a = new CMAES11('t');
		a.setDir("CMAES11"+slash());
		a.setParameter("p0",(2.0/11.0));
		a.setParameter("p1",(1.0/12.0));
		a.setParameter("p2",0.44);
		a.setParameter("p3",1.0);
		algorithms.add(a);
		
		a = new CMAES11('s');
		a.setDir("CMAES11"+slash());
		a.setParameter("p0",(2.0/11.0));
		a.setParameter("p1",(1.0/12.0));
		a.setParameter("p2",0.44);
		a.setParameter("p3",1.0);
		algorithms.add(a);	
		
//		a = new CMAES11('e');
//		a.setDir("CMAES11"+slash());
//		a.setParameter("p0",(2.0/11.0));
//		a.setParameter("p1",(1.0/12.0));
//		a.setParameter("p2",0.44);
//		a.setParameter("p3",1.0);
//		algorithms.add(a);
		
		a = new CMAES11('d');
		a.setDir("CMAES11"+slash());
		a.setParameter("p0",(2.0/11.0));
		a.setParameter("p1",(1.0/12.0));
		a.setParameter("p2",0.44);
		a.setParameter("p3",1.0);
		algorithms.add(a);
		
		a = new CMAES11('m');
		a.setDir("CMAES11"+slash());
		a.setParameter("p0",(2.0/11.0));
		a.setParameter("p1",(1.0/12.0));
		a.setParameter("p2",0.44);
		a.setParameter("p3",1.0);
		algorithms.add(a);
		
		a = new CMAES11('c');
		a.setDir("CMAES11"+slash());
		a.setParameter("p0",(2.0/11.0));
		a.setParameter("p1",(1.0/12.0));
		a.setParameter("p2",0.44);
		a.setParameter("p3",1.0);
		algorithms.add(a);
		
		
		a = new ISPO();
		a.setCorrection('t');
		a.setParameter("p0",1.0);
		a.setParameter("p1",10.0);
		a.setParameter("p2",2.0);
		a.setParameter("p3",4.0);
		a.setParameter("p4",0.000001);
		a.setParameter("p5",30.0);
		algorithms.add(a);
		
		a = new ISPO();
		a.setDir("ISPO"+slash());
		a.setCorrection('s');
		a.setParameter("p0",1.0);
		a.setParameter("p1",10.0);
		a.setParameter("p2",2.0);
		a.setParameter("p3",4.0);
		a.setParameter("p4",0.000001);
		a.setParameter("p5",30.0);
		algorithms.add(a);
		
		a = new ISPO();
		a.setDir("ISPO"+slash());
		a.setCorrection('t');
		a.setParameter("p0",1.0);
		a.setParameter("p1",10.0);
		a.setParameter("p2",2.0);
		a.setParameter("p3",4.0);
		a.setParameter("p4",0.000001);
		a.setParameter("p5",30.0);
		algorithms.add(a);
		
		a = new ISPO();
		a.setDir("ISPO"+slash());
		a.setCorrection('d');
		a.setParameter("p0",1.0);
		a.setParameter("p1",10.0);
		a.setParameter("p2",2.0);
		a.setParameter("p3",4.0);
		a.setParameter("p4",0.000001);
		a.setParameter("p5",30.0);
		algorithms.add(a);
		
		a = new ISPO();
		a.setDir("ISPO"+slash());
		a.setCorrection('m');
		a.setParameter("p0",1.0);
		a.setParameter("p1",10.0);
		a.setParameter("p2",2.0);
		a.setParameter("p3",4.0);
		a.setParameter("p4",0.000001);
		a.setParameter("p5",30.0);
		algorithms.add(a);
		
		a = new ISPO();
		a.setDir("ISPO"+slash());
		a.setCorrection('c');
		a.setParameter("p0",1.0);
		a.setParameter("p1",10.0);
		a.setParameter("p2",2.0);
		a.setParameter("p3",4.0);
		a.setParameter("p4",0.000001);
		a.setParameter("p5",30.0);
		algorithms.add(a);
		
		a = new RISold();
		a.setDir("RIS"+slash());
		a.setParameter("p0",0.5);
		a.setParameter("p1",0.4);
		a.setParameter("p2",0.000001);
		a.setCorrection('s');
		algorithms.add(a);
		
		a = new RISold();
		a.setDir("RIS"+slash());
		a.setParameter("p0",0.5);
		a.setParameter("p1",0.4);
		a.setParameter("p2",0.000001);
		a.setCorrection('t');
		algorithms.add(a);
		
		a = new RISold();
		a.setDir("RIS"+slash());
		a.setParameter("p0",0.5);
		a.setParameter("p1",0.4);
		a.setParameter("p2",0.000001);
		a.setCorrection('c');
		algorithms.add(a);
		
		a = new RISold();
		a.setDir("RIS"+slash());
		a.setParameter("p0",0.5);
		a.setParameter("p1",0.4);
		a.setParameter("p2",0.000001);
		a.setCorrection('d');
		algorithms.add(a);
		
		
		a = new RISold();
		a.setDir("RIS"+slash());
		a.setParameter("p0",0.5);
		a.setParameter("p1",0.4);
		a.setParameter("p2",0.000001);
		a.setCorrection('m');
		algorithms.add(a);
	

	
		a = new NonUniformSA();
		a.setDir("NUSA"+slash());
		a.setCorrection('t');
		a.setParameter("p0",5.0);
		a.setParameter("p1",0.9);
		a.setParameter("p2",3.0);
		a.setParameter("p3",10.0);
		algorithms.add(a);
		
		a = new NonUniformSA();
		a.setDir("NUSA"+slash());
		a.setCorrection('s');
		a.setParameter("p0",5.0);
		a.setParameter("p1",0.9);
		a.setParameter("p2",3.0);
		a.setParameter("p3",10.0);
		algorithms.add(a);
		
		a = new NonUniformSA();
		a.setDir("NUSA"+slash());
		a.setCorrection('d');
		a.setParameter("p0",5.0);
		a.setParameter("p1",0.9);
		a.setParameter("p2",3.0);
		a.setParameter("p3",10.0);
		algorithms.add(a);
		
		a = new NonUniformSA();
		a.setDir("NUSA"+slash());
		a.setCorrection('m');
		a.setParameter("p0",5.0);
		a.setParameter("p1",0.9);
		a.setParameter("p2",3.0);
		a.setParameter("p3",10.0);
		algorithms.add(a);
		
		a = new NonUniformSA();
		a.setDir("NUSA"+slash());
		a.setCorrection('c');
		a.setParameter("p0",5.0);
		a.setParameter("p1",0.9);
		a.setParameter("p2",3.0);
		a.setParameter("p3",10.0);
		algorithms.add(a);

		
		a = new Rosenbrock();
		a.setDir("Rosenbrock"+slash());
		a.setCorrection('t');
		a.setParameter("p0", 10e-5);
		a.setParameter("p1", 2.0); 
		a.setParameter("p2", 0.5);
		algorithms.add(a); 
				
		
		a = new Rosenbrock();
		a.setDir("Rosenbrock"+slash());
		a.setCorrection('s');
		a.setParameter("p0", 10e-5);
		a.setParameter("p1", 2.0); 
		a.setParameter("p2", 0.5);
		algorithms.add(a);
		
		a = new Rosenbrock();
		a.setDir("Rosenbrock"+slash());
		a.setCorrection('d');
		a.setParameter("p0", 10e-5);
		a.setParameter("p1", 2.0); 
		a.setParameter("p2", 0.5);
		algorithms.add(a);
	
		a = new Rosenbrock();
		a.setDir("Rosenbrock"+slash());
		a.setCorrection('m');
		a.setParameter("p0", 10e-5);
		a.setParameter("p1", 2.0); 
		a.setParameter("p2", 0.5);
		algorithms.add(a);
		
		a = new Rosenbrock();
		a.setDir("Rosenbrock"+slash());
		a.setCorrection('c');
		a.setParameter("p0", 10e-5);
		a.setParameter("p1", 2.0); 
		a.setParameter("p2", 0.5);
		algorithms.add(a);
		
		a = new SolisWets();
		a.setDir("SolisWets"+slash());
		a.setParameter("p0",0.5);
		a.setCorrection('t');
		algorithms.add(a);
		
		a = new SolisWets();
		a.setDir("SolisWets"+slash());
		a.setParameter("p0",0.5);
		a.setCorrection('s');
		algorithms.add(a);
		
		a = new SolisWets();
		a.setDir("SolisWets"+slash());
		a.setParameter("p0",0.5);
		a.setCorrection('d');
		algorithms.add(a);
		
		a = new SolisWets();
		a.setDir("SolisWets"+slash());
		a.setParameter("p0",0.5);
		a.setCorrection('m');
		algorithms.add(a);
		
		a = new SolisWets();
		a.setDir("SolisWets"+slash());
		a.setParameter("p0",0.5);
		a.setCorrection('c');
		algorithms.add(a);
		
		a = new SPSA();
		a.setDir("SPSA"+slash());
		a.setCorrection('t');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);
		
		a = new SPSA();
		a.setDir("SPSA"+slash());
		a.setCorrection('s');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);
		
		a = new SPSA();
		a.setDir("SPSA"+slash());
		a.setCorrection('d');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);
		
		a = new SPSA();
		a.setDir("SPSA"+slash());
		a.setCorrection('m');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);
		
		a = new SPSA();
		a.setDir("SPSA"+slash());
		a.setCorrection('c');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);
		
		
		
		a = new SA();
		a.setDir("SA"+slash());
		a.setParameter("p0", 0.9);
		a.setParameter("p1", 10.0 );
		algorithms.add(a);

		a = new ES1p1OneFifth();
		a.setDir("ES11"+slash());
		a.setCorrection('t');
		a.setParameter("p0", 2.0);
		algorithms.add(a);
		
		a = new ES1p1OneFifth();
		a.setDir("ES11"+slash());
		a.setCorrection('s');
		a.setParameter("p0", 2.0);
		algorithms.add(a);
		
		a = new ES1p1OneFifth();
		a.setDir("ES11"+slash());
		a.setCorrection('m');
		a.setParameter("p0", 2.0);
		algorithms.add(a);

		a = new ES1p1OneFifth();
		a.setDir("ES11"+slash());
		a.setCorrection('d');
		a.setParameter("p0", 2.0);
		algorithms.add(a);
		
		a = new ES1p1OneFifth();
		a.setDir("ES11"+slash());
		a.setCorrection('c');
		a.setParameter("p0", 2.0);
		algorithms.add(a);
		
		a = new ES1p1OneFifthV2();
		a.setDir("ES11"+slash());
		a.setCorrection('t');
		a.setParameter("p0", 2.0);
		algorithms.add(a);
		
		a = new ES1p1OneFifthV2();
		a.setDir("ES11"+slash());
		a.setCorrection('s');
		a.setParameter("p0", 2.0);
		algorithms.add(a);
		
		a = new ES1p1OneFifthV2();
		a.setDir("ES11"+slash());
		a.setCorrection('d');
		a.setParameter("p0", 2.0);
		algorithms.add(a);
		
		a = new ES1p1OneFifthV2();
		a.setDir("ES11"+slash());
		a.setCorrection('m');
		a.setParameter("p0", 2.0);
		algorithms.add(a);
		
		a = new ES1p1OneFifthV2();
		a.setDir("ES11"+slash());
		a.setCorrection('c');
		a.setParameter("p0", 2.0);
		algorithms.add(a);
		
		a = new Powell_correct();
		a.setDir("Powell"+slash());
		a.setCorrection('t');
		a.setParameter("p0",  0.00001);
		a.setParameter("p1",  100.0);
		algorithms.add(a);
		
		a = new Powell_correct();
		a.setDir("Powell"+slash());
		a.setCorrection('s');
		a.setParameter("p0",  0.00001);
		a.setParameter("p1",  100.0);
		algorithms.add(a);
	
		a = new Powell_correct();
		a.setDir("Powell"+slash());
		a.setCorrection('m');
		a.setParameter("p0",  0.00001);
		a.setParameter("p1",  100.0);
		algorithms.add(a);
		
		a = new Powell_correct();
		a.setDir("Powell"+slash());
		a.setCorrection('c');
		a.setParameter("p0",  0.00001);
		a.setParameter("p1",  100.0);
		algorithms.add(a);
		
		a = new Powell_correct();
		a.setDir("Powell"+slash());
		a.setCorrection('d');
		a.setParameter("p0",  0.00001);
		a.setParameter("p1",  100.0);
		algorithms.add(a);
		
		a = new Powell_correct();
		a.setDir("Powell"+slash());
		a.setCorrection('c');
		a.setParameter("p0",  0.00001);
		a.setParameter("p1",  100.0);
		
		a = new NelderMeadBias();
		a.setDir("NMA"+slash());
		a.setCorrection('t');
		a.setParameter("p0", 1.0);
		a.setParameter("p1", 0.5);
		a.setParameter("p2",2.0);
		a.setParameter("p3", 0.5);
		algorithms.add(a);
		
		a = new NelderMeadBias();
		a.setDir("NMA"+slash());
		a.setCorrection('s');
		a.setParameter("p0", 1.0);
		a.setParameter("p1", 0.5);
		a.setParameter("p2",2.0);
		a.setParameter("p3", 0.5);
		algorithms.add(a);
		
		a = new NelderMeadBias();
		a.setDir("NMA"+slash());
		a.setCorrection('d');
		a.setParameter("p0", 1.0);
		a.setParameter("p1", 0.5);
		a.setParameter("p2",2.0);
		a.setParameter("p3", 0.5);
		algorithms.add(a);
		
		a = new NelderMeadBias();
		a.setDir("NMA"+slash());
		a.setCorrection('m');
		a.setParameter("p0", 1.0);
		a.setParameter("p1", 0.5);
		a.setParameter("p2",2.0);
		a.setParameter("p3", 0.5);
		algorithms.add(a);
		
		a = new NelderMeadBias();
		a.setDir("NMA"+slash());
		a.setCorrection('c');
		a.setParameter("p0", 1.0);
		a.setParameter("p1", 0.5);
		a.setParameter("p2",2.0);
		a.setParameter("p3", 0.5);
		algorithms.add(a);
		
		
		a = new SPSAv2();
		a.setDir("SPSAv2"+slash());
		a.setCorrection('t');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);
		
		a = new SPSAv2();
		a.setDir("SPSAv2"+slash());
		a.setCorrection('s');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);
		
		a = new SPSAv2();
		a.setDir("SPSAv2"+slash());
		a.setCorrection('d');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);

		a = new SPSAv2();
		a.setDir("SPSAv2"+slash());
		a.setCorrection('c');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);
		
		a = new SPSAv2();
		a.setDir("SPSAv2"+slash());
		a.setCorrection('m');
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 1.0 );
		a.setParameter("p2", 0.602);
		a.setParameter("p3", 0.032);
		a.setParameter("p4", 0.1);
		algorithms.add(a);
		
		execute(algorithms, problems, expSettings);	
			
		}
}

		
/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package experiments.RiCompact;

import interfaces.Experiment;
import interfaces.Algorithm;
import algorithms.compact.*;
import benchmarks.problemsImplementation.BaseFunctions.Ackley;
import benchmarks.problemsImplementation.BaseFunctions.Rastigin;
import benchmarks.problemsImplementation.BaseFunctions.Sphere;

import interfaces.Problem;

public class RIBudgetStudy extends Experiment
{
	
	public RIBudgetStudy(int probDim) throws Exception
	{
		//super(probDim,"RIBudgetStudy");
		super(probDim,5000,"RIBudgetStudy");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		
//		a = new RIcDE();
//		a.setParameter("p0", 0.95);
//		a.setParameter("p1", 0.25);
//		add(a);
		
		a = new RIcDE_light();
		a.setParameter("p0", 0.95);
		a.setParameter("p1", 0.25);
		add(a);
//		
//		a = new RIcGA();
//		a.setParameter("p0", 0.95);
//		a.setParameter("p1", 0.25);
//		add(a);
//		
//		a = new RIcBFO();
//		a.setParameter("p0", 0.95);
//		a.setParameter("p1", 0.25);
//		add(a);	
//		
//		a = new RIcPSO();
//		a.setParameter("p0", 0.95);
//		a.setParameter("p1", 0.25);
//		add(a);	
		
//		a = new cDE_exp();
//		a.setParameter("p0", 300.0);
//		a.setParameter("p1", 0.25);
//		a.setParameter("p2", 0.5);
//		a.setParameter("p3", 2.0);
//		a.setParameter("p4", 1.0);
//		a.setParameter("p5", 1.0);
//		add(a);	
//		
		a = new cDE_exp_light();
		a.setParameter("p0", 300.0);
		a.setParameter("p1", 0.25);
		a.setParameter("p2", 0.5);
		a.setParameter("p3", 3.0);
		a.setParameter("p4", 1.0);
		a.setParameter("p5", 1.0);
		add(a);	
//		
//		a = new cBFO();
//		a.setParameter("p0", 300.0);
//		a.setParameter("p1", 0.1);
//		a.setParameter("p2", 4.0);
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 10.0);
//		a.setParameter("p5", 2.0);
//		a.setParameter("p6", 2.0);
//		add(a);
//		
//		a = new cGA_real();
//		a.setParameter("p0", 300.0);
//		a.setParameter("p1", 0.1);//not important as persisten  == 1 (p2) and therefore it is not used
//		a.setParameter("p2", 1.0);
//		add(a);
//
//		a = new cPSO();
//		a.setParameter("p0", 50.0);
//		a.setParameter("p1", -0.2);
//		a.setParameter("p2", -0.07);
//		a.setParameter("p3", 3.74);
//		a.setParameter("p4", 1.0);
//		a.setParameter("p5", 1.0);
//		add(a);
//
//
//		  a = new MS_CAP();
//		  a.setParameter("p0",50.0);
//		  a.setParameter("p1", 1e-6);
//		  a.setParameter("p2", 3.0);
//		  add(a);
//		  
//		   a = new MDE_pBX();
//		   a.setParameter("p0", 100.0);
//		   a.setParameter("p1", 0.15); 
//		   a.setParameter("p2", 0.6);
//		   a.setParameter("p3", 0.5);
//		   a.setParameter("p4", 1.5);
//		   add(a);
	

		Problem p;
		
		p = new Sphere(probDim);
		add(p);
		p = new Ackley(probDim); // M and NS
		add(p);//add it to the list
		p = new Rastigin(probDim);
		add(p);

		



	}
}

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
package experiments.applications;

import interfaces.Experiment;
import interfaces.Algorithm;
import interfaces.Problem;
import algorithms.CCPSO2;
import algorithms.CLPSO;
import algorithms.JADE;
import algorithms.MADE;
import algorithms.MS_CAP;
import algorithms.SADE;
import algorithms.CMAES;
//import algorithms.singleSolution.NonUniformSA;
//import algorithms.singleSolution.VISPO;
import applications.SearchProblem.SimpleAgentSearchProblem;


public class Chris extends Experiment
{
	public Chris(int probDim)
	{
		super("ChrissAppTesting");
		setMT(false);
		setNrRuns(30);	
		
		Algorithm a;// ///< A generic optimiser.
	    Problem p;// ///< A generic problem.
	
	    a = new MS_CAP();
	    a.setParameter("p0",50.0);
	    a.setParameter("p1", 1e-6);
	    a.setParameter("p2", 3.0);
	    add(a);
	    
	    a = new MADE();
	    a.setParameter("p0",50.0);
	    a.setParameter("p1", 50.0);
	    a.setParameter("p2", 0.5);
	    a.setParameter("p3", 0.7);
	    a.setParameter("p4", 0.02);
	    a.setParameter("p5", 1.0);
	    add(a);
	    
	    a = new JADE();
	    a.setParameter("p0",60.0);
	    a.setParameter("p1", 0.05);
	    a.setParameter("p2", 0.1);
	    add(a);
	    
	    a = new SADE();
	    a.setParameter("p0",50.0);
	    a.setParameter("p1", 4.0);
	    a.setParameter("p2", 20.0);
	    add(a);
	    
	    a = new CLPSO();
	    a.setParameter("p0",60.0);
	    add(a);
	    
	    a = new CCPSO2();
	    a.setParameter("p0",30.0);
	    a.setParameter("p1",0.5);
	    add(a);
	    
	    a = new CMAES();
	    add(a);
	    
//	    a = new VISPO();
//	    a.setParameter("p0",10.0);
//	    a.setParameter("p1", 30.0);
//	    a.setParameter("p2", 0.65);
//	    add(a);
//
//	    a = new NonUniformSA();
//	    a.setParameter("p0",5.0);
//	    a.setParameter("p1", 0.9);
//	    a.setParameter("p2", 3.0);
//	    a.setParameter("p3", 10.0);
//	    add(a);
		
	   	// bounds are of maximum turn in radians (roughly 60 degrees)
		// the number of particles parameter probably wants to be quite high as they are used
		// to approximate 5 different Gaussian distributions in this example
		p = new SimpleAgentSearchProblem(probDim, new double[] {-1.05,1.05},500,500.0,500.0,1);
		add(p);//add it to the list
//		
//		p = new P7();
//		add(p);//add it to the list
		

		

	}
}

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
package experiments.evostart19;

import interfaces.Experiment;
import interfaces.Algorithm;
//import benchmarks.CEC2014RotInvStudy;
//import benchmarks.problemsImplementation.BaseFunctions.Sphere;
import benchmarks.problemsImplementation.BaseFunctions.Ellipsoid;
//import benchmarks.problemsImplementation.BaseFunctions.Ackley;
//import benchmarks.problemsImplementation.BaseFunctions.Rastigin;
////import algorithms.RIDE;
//import algorithms.MMCDE;
//import algorithms.EigenDE;
//import algorithms.CMAES;
//import algorithms.inProgress.CMS;
//import algorithms.inProgress.CMAES_RIS2;
//import algorithms.CMAES_RIS;
import algorithms.inProgress.CMAES_PRE;





public class covariance extends Experiment
{
	
	public covariance(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"EvoF");
		setNrRuns(1);
//		setMT(false);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		
		
		
//		
//		a = new CMAES_RIS2();
//		a.setParameter("p0",(3.0/10.0));
//		a.setParameter("p1",0.5); 		
//		a.setParameter("p2",0.2);
//		a.setParameter("p3",0.0000001);
//		add(a);
//		
//		a = new CMAES_RIS();
//		a.setParameter("p0",0.5); 		
//		a.setParameter("p1",0.2);
//		a.setParameter("p2",0.0000001);
//		add(a);
//		
//		a = new CMAES_PRE();
//		add(a);
//		

		
//		
		a = new CMAES_PRE();
		a.setID("CMAES10");
		a.setParameter("p0", 0.1);
		add(a);
//		
//		a = new CMAES_PRE();
//		a.setID("CMAES20");
//		a.setParameter("p0", 0.2);
//		add(a);
//		
//		a = new CMAES_PRE();
//		a.setID("CMAES30");
//		a.setParameter("p0", 0.3);
//		add(a);
//		
//		a = new CMAES_PRE();
//		a.setID("CMAES50");
//		a.setParameter("p0", 0.5);
//		add(a);
//		
//		a = new CMAES_PRE();
//		a.setID("CMAE80");
//		a.setParameter("p0", 0.8);
//		add(a);
//		
//		
//		add(new Sphere(probDim));
//		add(new Ackley(probDim));
//		add(new Rastigin(probDim));
		
//		add(new Ellipsoid(probDim, new double[] {0,0}, new double[] {Math.cos(Math.PI/6), -Math.sin(Math.PI/6), Math.sin(Math.PI/6), Math.cos(Math.PI/6)}));
		add(new Ellipsoid(probDim));
			
//		for(int i = 1; i<=25; i++)
//				add(new CEC2014RotInvStudy(probDim, i));
		



	}
}

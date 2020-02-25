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
package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.DE;



public class CEC14_rot_EXP_TUNING extends Experiment
{
	
	public CEC14_rot_EXP_TUNING(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		

		a = new DE();
		a.setID("rDEr1exp01");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.1);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		add(a);

		//DATA with CR=0.3 already produced for conference papar		
//		a = new DE();
//		a.setID("rDEr1exp03");
//		a.setParameter("p0", 10.0);
//		a.setParameter("p1", 0.4);
//		a.setParameter("p2", 0.3);
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 2.0);
//		add(a);
		
		
		a = new DE();
		a.setID("rDEr1exp05");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.5);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		add(a);
		
		a = new DE();
		a.setID("rDEr1exp07");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.7);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		add(a);
		
		
		a = new DE();
		a.setID("rDEr1exp09");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.9);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		add(a);
		
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i));




	}
}

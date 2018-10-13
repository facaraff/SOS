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
package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
import interfaces.Problem;
import algorithms.singleSolution.NonUniformSA;
import algorithms.singleSolution.VISPO;
import applications.CEC2011.P1;
import applications.CEC2011.P2;


public class TestCEC2011 extends Experiment
{
	public TestCEC2011()
	{
		super("STIGRANCAZZI");
		setNrRuns(30);	
		
		Algorithm a;// ///< A generic optimiser.
	    Problem p;// ///< A generic problem.
	
	    a = new VISPO();
	    a.setParameter("p0",10.0);
	    a.setParameter("p1", 30.0);
	    a.setParameter("p2", 0.65);
	    add(a);

	    a = new NonUniformSA();
	    a.setParameter("p0",5.0);
	    a.setParameter("p1", 0.9);
	    a.setParameter("p2", 3.0);
	    a.setParameter("p3", 10.0);
	    add(a);
		
	    
		p = new P1();
		add(p);//add it to the list
//		
//		p = new P7();
//		add(p);//add it to the list
		
		p = new P2(30);
		add(p);//add it to the list
//		p = new P2(60);
//		add(p);//add it to the list
//		p = new P2(90);
//		add(p);//add it to the list
//		p = new P2(120);
//		add(p);//add it to the list
//		p = new P2(300);
//		add(p);//add it to the list
//		p = new P2(600);
//		add(p);//add it to the list
//		p = new P2(900);
//		add(p);//add it to the list

		

	}
}

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
import algorithms.RIDE;
import algorithms.DE;
import algorithms.MMCDE;



public class CEC14_RIDE_unrot extends Experiment
{
	
	public CEC14_RIDE_unrot(int probDim) throws Exception
	{
		super(probDim,5000,"testCEC14RIDE");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		
		a = new DE("ro",'b');
		a.setID("wrDEr1bin");
		a.setParameter("p0",  (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 0.3);//ALPHA
		add(a);
		
		a = new DE("ro",'e');
		a.setID("wrDEr1exp");
		a.setParameter("p0",  (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 0.3);//ALPHA
		add(a);
		
		a = new DE("ctro",'x');
		a.setID("wrDEctr1");
		a.setParameter("p0",  (double)probDim);
		a.setParameter("p1", 0.7); //F
		a.setParameter("p2", Double.NaN);
		a.setParameter("p3", Double.NaN);//ALPHA
		add(a);
		
		
		a = new MMCDE();
		a.setID("wrMMCDE");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7); //F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 0.3);//ALPHA
		add(a);
		
		a = new RIDE();
		a.setID("wrRIDErand1bin");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7); //F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 4.0);
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new RIDE();
		a.setID("wrRIDErand1exp");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7); //F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 5.0);
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i, 0));




	}
}

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
package experiments.paperReviews;

import interfaces.Experiment;
import interfaces.Algorithm;
import algorithms.singleSolution.SPAMAOS2;
import algorithms.paperReviews.HyperSPAMnoR;
import algorithms.paperReviews.HyperSPAMnoSnoR;
import benchmarks.CEC2014;
//import benchmarks.CEC2015;
//import benchmarks.SISC2010;
//import benchmarks.BBOB2010;
//import benchmarks.CEC2013;
import algorithms.paperReviews.HyperSPAMnoS;





public class HyperSPAM extends Experiment
{
	public HyperSPAM(int probDim) throws Exception
	{
		super("HYPER");
		setNrRuns(30);
		setMT(true);
		
		Algorithm a;// ///< A generic optimiser.
	    
	    
		
		
		a= new SPAMAOS2();
		a.setID("hyperSPAM-MT");
		a.setParameter("p0", 0.5);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 150.0);
		a.setParameter("p3", 2.0);
		a.setParameter("p4", 0.5);
		a.setParameter("p5", 0.00001);
		a.setParameter("p6", 3.0);
		add(a);
		
		a = new HyperSPAMnoSnoR();
		add(a);
			
	    a = new HyperSPAMnoR();
	  	add(a);
	  		
	  	a = new HyperSPAMnoS();
		add(a);
			
		
//	    
//		for(int i = 1; i<=28; i++)
//			add(new CEC2013(i,probDim));	

//		for(int i = 5; i<=5; i++)
//			add(new BBOB2010(probDim, i));	

//		for(int i = 1; i<=15; i++)
//			add(new CEC2015(probDim, i));
		
		for(int i = 1; i<=30; i++)
			add(new CEC2014(probDim, i));
		
	
		
		
	}
}

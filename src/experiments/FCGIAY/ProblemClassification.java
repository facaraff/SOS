/**
Copyright (c) 2021, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package experiments.FCGIAY;

import interfaces.Experiment;
import interfaces.Algorithm;
import algorithms.specialOptions.FCGIAY.PC_CMAES;
import algorithms.specialOptions.FCGIAY.PC_jDE;
import benchmarks.CEC2013;
//import benchmarks.CEC2014;

public class ProblemClassification extends Experiment
{
	
	public ProblemClassification(int probDim) throws Exception
	{

		super(probDim,5000,"PCDL"); 
		setNrRuns(10);
		
		//setMT(false);

		
		double[] popSizes = {10, 30, 50, 100, 200};
		
		Algorithm a;// ///< A generic optimiser.
	    //Problem P);// ///< A generic problem.
	
	
		for (double p : popSizes)
		{
			a = new PC_jDE();
			a.setID("jDE-P"+(int)p);
			a.setParameter("p0", p);
			a.setParameter("p1", 0.1);//FL
			a.setParameter("p2", 1.0); //FU
			a.setParameter("p3", 0.1);//tau1
			a.setParameter("p4", 0.1);//tau2
			add(a);
		}		

		a = new PC_CMAES();
		a.setID("CMAES-P"+(int) (4.0 + 3.0 * Math.log(probDim)));
		add(a);


		for(int i = 1; i<=28; i++)
			add(new CEC2013(i, probDim));
		
//		for(int i = 1; i<=30; i++)
//			add(new CEC2014(probDim,i));




	}
}

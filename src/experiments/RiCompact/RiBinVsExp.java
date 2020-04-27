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
import algorithms.compact.review.RIcDEexp;
import algorithms.compact.review.RIcDEbin;
import benchmarks.problemsImplementation.BaseFunctions.Ackley;
import benchmarks.problemsImplementation.BaseFunctions.Rastigin;
import benchmarks.problemsImplementation.BaseFunctions.Sphere;

import interfaces.Problem;

public class RiBinVsExp extends Experiment
{
	
	public RiBinVsExp(int probDim) throws Exception
	{
		super(probDim,5000,"BinVersusExp");
		setNrRuns(30);

		Algorithm a;
	    	
		//RIcDEb30a25exp
		a = new RIcDEexp();
		a.setParameter("p0", 0.25);
		a.setParameter("p1",0.3);
		a.setID("RIcDEb"+( (int) ( a.getParameter("p1").doubleValue()*100) )+"a"+((int)(a.getParameter("p0").doubleValue()*100))+"exp");
		add(a);
				
		//RIcDEb20a25bin
		a = new RIcDEbin();
		a.setParameter("p0", 0.25);
		a.setParameter("p1", 0.2);
		a.setID("RIcDEb"+( (int) ( a.getParameter("p1").doubleValue()*100) )+"a"+((int)(a.getParameter("p0").doubleValue()*100))+"bin");
		add(a);

		Problem p;
		
		p = new Sphere(probDim);
		add(p);
		p = new Ackley(probDim); 
		add(p);
		p = new Rastigin(probDim);
		add(p);

	}
}

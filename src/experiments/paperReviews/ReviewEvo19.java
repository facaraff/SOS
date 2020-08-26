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

import algorithms.compact.review.RecBFO;
import algorithms.compact.review.RecGA;
import algorithms.compact.review.RecPSO;
import benchmarks.CEC2014;
import interfaces.Algorithm;
import interfaces.Experiment;


public class ReviewEvo19 extends Experiment
{
	
	public ReviewEvo19(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		

//		a = new RandomSampling();
//		add(a);
		
//
//		a = new RecDE_light();
//		a.setParameter("p0", 0.25);
//		add(a);
//
		
		
		a = new RecGA();
		a.setParameter("p0", 0.25);
		add(a);
		
		
		a = new RecBFO();
		a.setParameter("p0", 0.25);
		add(a);
		
		
		a = new RecPSO();
		a.setParameter("p0", 0.25);
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
		

		for(int i = 1; i<=30; i++)
			add(new CEC2014(probDim, i));




	}
}

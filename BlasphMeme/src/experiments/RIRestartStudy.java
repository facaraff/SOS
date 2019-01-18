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
import algorithms.compact.review.RIcDEexp;
import algorithms.compact.review.RIcDEbin;
import benchmarks.problemsImplementation.BaseFunctions.Ackley;
import benchmarks.problemsImplementation.BaseFunctions.Rastigin;
import benchmarks.problemsImplementation.BaseFunctions.Sphere;

import interfaces.Problem;

public class RIRestartStudy extends Experiment
{
	
	public RIRestartStudy(int probDim) throws Exception
	{
		//super(probDim,"RIBudgetStudy");
		super(probDim,5000,"RIBudgetStudy");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		double[] budgets = {0.1, 0.15, 0.20, 0.25, 0.30};
		
		double[] alphas = {0.05, 0.1, 0.15, 0.2, 0.25};
		

		for(int i=0; i<5; i++)
		{
			for(int j=0; j<5; j++)
			{
				a = new RIcDEexp();
				//a.setID("RIcDE-10");
				a.setParameter("p0", alphas[j]);
				a.setParameter("p1", budgets[i]);
				a.setID("RIcDEb"+( (int) ( a.getParameter("p1").doubleValue()*100) )+"a"+((int)(a.getParameter("p0").doubleValue()*100))+"exp");
				add(a);
			}
			
		}

		
		for(int i=0; i<5; i++)
		{
			for(int j=0; j<5; j++)
			{
				a = new RIcDEbin();
				a.setParameter("p0", alphas[j]);
				a.setParameter("p1", budgets[i]);
				a.setID("RIcDEb"+( (int) ( a.getParameter("p1").doubleValue()*100) )+"a"+((int)(a.getParameter("p0").doubleValue()*100))+"bin");
				add(a);
			}
			
		}

	

		Problem p;
		
		p = new Sphere(probDim);
		add(p);
		p = new Ackley(probDim); // M and NS
		add(p);//add it to the list
		p = new Rastigin(probDim);
		add(p);

		



	}
}

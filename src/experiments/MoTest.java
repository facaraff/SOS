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
import algorithms.MoAlgs.MoTestAlg;
//import benchmarks.CEC2014RotInvStudy;
import benchmarks.problemsImplementation.BaseFunctions.*;





public class MoTest extends Experiment
{
	
	public MoTest(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"ILPOAGAIN");
		setNrRuns(30);
		setMT(false);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		
		a = new MoTestAlg();
		a.setParameter("p0", 10.0);//Repeats
		add(a);
		

		
		add(new Sphere(probDim));
//		add(new Ackley(probDim));
//		add(new Rastigin(probDim));
		add(new Schwefel(probDim));
		
//		add(new Ellipsoid(probDim, new double[] {0,0}, new double[] {Math.cos(Math.PI/6), -Math.sin(Math.PI/6), Math.sin(Math.PI/6), Math.cos(Math.PI/6)}));
//		add(new Ellipsoid(probDim));
			
//		for(int i = 1; i<=25; i++)
//				add(new CEC2014RotInvStudy(probDim, i));
		
//		for(int i = 1; i<=24; i++)
//			add(new BBOB2018(i, probDim));
		



	}
}

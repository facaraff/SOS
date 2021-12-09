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
package experiments.FCGIAY;

import interfaces.Experiment;
import interfaces.Algorithm;
import algorithms.PCDL.EarlyDE;
import benchmarks.BBOB2018;
//import benchmarks.CEC2014;

public class EarlyDEBBOB2018 extends Experiment
{
	
	public EarlyDEBBOB2018(int probDim, double popSize) throws Exception
	{

		super(probDim,5000,"AnilBBOB18");
		setNrRuns(30);

		//double popSize = 10;
		String P = "P"+(int)popSize;
		
		Algorithm a;// ///< A generic optimiser.
	    //Problem P);// ///< A generic problem.
				
		a = new EarlyDE();
		a.setID("DErob"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 2.0);//mutation
		a.setParameter("p4", 1.0);//crossover
		a.setParameter("p5", Double.NaN);//ALPHA
		add(a);
		
		
		a = new EarlyDE();
		a.setID("DEroe"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 2.0);//mutation
		a.setParameter("p4", 2.0);//crossover
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new EarlyDE();
		a.setID("DEcro"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", Double.NaN); //CR
		a.setParameter("p3", 2.0);//mutation
		a.setParameter("p4", 1.0);//crossover
		a.setParameter("p5", Double.NaN);//ALPHA
		add(a);

		a = new EarlyDE();
		a.setID("DEbob"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 3.0);//mutation
		a.setParameter("p4", 1.0);//crossover
		a.setParameter("p5", Double.NaN);//ALPHA
		add(a);
		
		
		a = new EarlyDE();
		a.setID("DEboe"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 3.0);//mutation
		a.setParameter("p4", 2.0);//crossover
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new EarlyDE();
		a.setID("DErbob"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 4.0);//mutation
		a.setParameter("p4", 1.0);//crossover
		a.setParameter("p5", Double.NaN);//ALPHA
		add(a);
		
		
		a = new EarlyDE();
		a.setID("DErboe"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 4.0);//mutation
		a.setParameter("p4", 2.0);//crossover
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new EarlyDE();
		a.setID("DEcbob"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 5.0);//mutation
		a.setParameter("p4", 1.0);//crossover
		a.setParameter("p5", Double.NaN);//ALPHA
		add(a);
		
		
		a = new EarlyDE();
		a.setID("DEcboe"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 5.0);//mutation
		a.setParameter("p4", 2.0);//crossover
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		
		a = new EarlyDE();
		a.setID("DErtb"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 6.0);//mutation
		a.setParameter("p4", 1.0);//crossover
		a.setParameter("p5", Double.NaN);//ALPHA
		add(a);
		
		
		a = new EarlyDE();
		a.setID("DErte"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 6.0);//mutation
		a.setParameter("p4", 2.0);//crossover
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new EarlyDE();
		a.setID("DEbtb"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 7.0);//mutation
		a.setParameter("p4", 1.0);//crossover
		a.setParameter("p5", Double.NaN);//ALPHA
		add(a);
		
		
		a = new EarlyDE();
		a.setID("DEbte"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 7.0);//mutation
		a.setParameter("p4", 2.0);//crossover
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		
		a = new EarlyDE();
		a.setID("DErbtb"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 8.0);//mutation
		a.setParameter("p4", 1.0);//crossover
		a.setParameter("p5", Double.NaN);//ALPHA
		add(a);
		
		
		a = new EarlyDE();
		a.setID("DErbte"+P);
		a.setParameter("p0", popSize);
		a.setParameter("p1", 0.4);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 8.0);//mutation
		a.setParameter("p4", 2.0);//crossover
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		

		for(int i = 1; i<=24; i++)
			add(new BBOB2018(i, probDim));
		
//		for(int i = 1; i<=30; i++)
//			add(new CEC2014(probDim,i));




	}
}

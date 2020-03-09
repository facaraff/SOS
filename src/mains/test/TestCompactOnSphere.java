package mains.test;
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



/** @file RunExperiments.java
 *  
 *  @author Fabio Caraffini
*/

import java.util.Vector;

import algorithms.compact.RIcDE;
import interfaces.Algorithm;
import interfaces.Experiment;

import static utils.RunAndStore.resultsFolder;
//import experiments.rotInvStudy.*;
//import experiments.BenchmarksTesting.*;

import interfaces.Problem;
//import algorithms.MDE_pBX;
import algorithms.MS_CAP;
//import algorithms.singleSolution.SPAMAOS2;


/** 
* This class contains the main method and has to be used for launching experiments.
*/
public class TestCompactOnSphere
{
	
	
	/** 
	* Main method.
	* This method has to be modified in order to launch a new experiment.
	* @param args For passing args to the main value.
	* @throws Exception The main method must be able to handle possible java.lang.Exceptionincluding I/O exceptions etc.
	*/
	public static void main(String[] args) throws Exception
	{	
		
		// make sure that "results" folder exists
		resultsFolder();
	
	
		Vector<Experiment> experiments = new Vector<Experiment>();////!< List of problems 
	
		Experiment e = new CompactOnLargeScaleSphere();
		
		//@@@ MODIFY THIS PART @@@		
				 
		experiments.add(e);
		
		//@@@@@@
	
		for(Experiment experiment : experiments)
		{
			//experiment.setShowPValue(true);
			experiment.startExperiment();
			System.out.println();
			experiment = null;
		}
	
		
		
	}
	
	
	public static class CompactOnLargeScaleSphere extends Experiment
	{
		
		public CompactOnLargeScaleSphere() throws Exception
		{
			//super(probDim,"cec2015allDim");
//			super(1000,5000,"CEC2013_LSGO_RICOMPACT");
			super(1000,5000,"TEST ON LARGE SCALE SPHERE");
			setNrRuns(30);


			Algorithm a;// ///< A generic optimiser.
		    //Problem p;// ///< A generic problem.
			
			
			a = new RIcDE();
			a.setParameter("p0", 0.95);
			a.setParameter("p1", 0.25);
			add(a);
			
//			a = new RIcDE_light();
//			a.setParameter("p0", 0.95);
//			a.setParameter("p1", 0.25);
//			add(a);
//			
//			a = new RIcGA();
//			a.setParameter("p0", 0.95);
//			a.setParameter("p1", 0.25);
//			add(a);
//			
//			a = new RIcBFO();
//			a.setParameter("p0", 0.95);
//			a.setParameter("p1", 0.25);
//			add(a);	
//			
//			a = new RIcPSO();
//			a.setParameter("p0", 0.95);
//			a.setParameter("p1", 0.25);
//			add(a);	
			
//			a = new cDE_exp();
//			a.setParameter("p0", 300.0);
//			a.setParameter("p1", 0.25);
//			a.setParameter("p2", 0.5);
//			a.setParameter("p3", 2.0);
//			a.setParameter("p4", 1.0);
//			a.setParameter("p5", 1.0);
//			add(a);	
//			
//			a = new cDE_exp_light();
//			a.setParameter("p0", 300.0);
//			a.setParameter("p1", 0.25);
//			a.setParameter("p2", 0.5);
//			a.setParameter("p3", 3.0);
//			a.setParameter("p4", 1.0);
//			a.setParameter("p5", 1.0);
//			add(a);	
//			
//			a = new cBFO();
//			a.setParameter("p0", 300.0);
//			a.setParameter("p1", 0.1);
//			a.setParameter("p2", 4.0);
//			a.setParameter("p3", 1.0);
//			a.setParameter("p4", 10.0);
//			a.setParameter("p5", 2.0);
//			a.setParameter("p6", 2.0);
//			add(a);
//			
//			a = new cGA_real();
//			a.setParameter("p0", 300.0);
//			a.setParameter("p1", 0.1);//not important as persisten  == 1 (p2) and therefore it is not used
//			a.setParameter("p2", 1.0);
//			add(a);
	//
//			a = new cPSO();
//			a.setParameter("p0", 50.0);
//			a.setParameter("p1", -0.2);
//			a.setParameter("p2", -0.07);
//			a.setParameter("p3", 3.74);
//			a.setParameter("p4", 1.0);
//			a.setParameter("p5", 1.0);
//			add(a);
	//
	//
			  a = new MS_CAP();
			  a.setParameter("p0",50.0);
			  a.setParameter("p1", 1e-6);
			  a.setParameter("p2", 3.0);
			  add(a);
//			  
//			   a = new MDE_pBX();
//			   a.setParameter("p0", 100.0);
//			   a.setParameter("p1", 0.15); 
//			   a.setParameter("p2", 0.6);
//			   a.setParameter("p3", 0.5);
//			   a.setParameter("p4", 1.5);
//			   add(a);
		

				add(new LargeSphere());

			



		}
	}

	

	public static class LargeSphere extends Problem
	{
		/**
		* Costructor for the Sphere function defined within the specified upper and lower bounds.
		*/
		public LargeSphere(){ super(1000, new double[] {-100,100} );}
		/**
		* Constructor for the Sphere function defined within a hyper-cube.
		* @param bounds the boundaries of the optimisation problem.
		*/
		public LargeSphere(double[] bounds) { super(1000, bounds); }
		/**
		* Costructor for the Sphere function defined within particular decision space.
		* @param bounds the boundaries of the optimisation problem.
		*/
		public LargeSphere(double[][] bounds) { super(1000, bounds); }
		/**
		* This method implement the Sphere function.
		* @param x solution to be avaluated
		*/
		public double f(double[] x)
		{
			final int n = x.length;
			double y = 0;
		
			if(this.getDimension()!= n)
			{
				y=Double.NaN;
				System.out.println("WARNING: the design variable does not match the dimensionality of the problem!");
			}
			else
			{
				for (int i = 0; i < n; i++)
					y += x[i]*x[i];
			}
			return y;
		}
	}
	

}

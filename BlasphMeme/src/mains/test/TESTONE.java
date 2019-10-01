package mains.test;

/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved


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



/** @file TSTONE.java
 *  
 *  @author Fabio Caraffini
*/

import java.util.Vector;

import static utils.random.RandUtils.uniform;
//import static utils.MatLab.transpose;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.linear.EigenDecomposition; 



/** 
* This class contains the main method and has to be used for launching experiments.
*/
public class TESTONE 
{
	
	
	/** 
	* Main method.
	*/
	final static double alpha = Math.PI;
	public static void main(String[] args) throws Exception
	{	
		
			
		Vector<double[]> levelX = new Vector<double[]>();////!< List of problems 
		double[] x = new double[2];

		int counter = 0;
		for(int i=0; i < 200000; i++)
		{
			x[0] = uniform(-10,10);
			x[1] = uniform(-10,10);
			double f =fitFunc(x,1);
			//System.out.println(f);
			if(f<5)
			{
					levelX.add(x);	
					counter++;
			}
		}
		System.out.println(counter);
		
		double[][] points = new double[levelX.size()][levelX.get(0).length];
		for(int i=0; i < levelX.size()-1; i++)
			points[i] = levelX.get(i);
		
		
		Covariance C = new Covariance(points);
		levelX = null; points = null;
		System.out.println("covariates number " + C.getN());
		System.out.println(C.getCovarianceMatrix().toString());
		
//		C = new Covariance(new double[][] {{1,2},{10,5}});
//		System.out.println(C.getCovarianceMatrix().toString());
		
		
		EigenDecomposition E = new EigenDecomposition(C.getCovarianceMatrix().transpose());
		System.out.println(E.getV().toString());

		
		
	}
	

	

	 public static double fitFunc(double[] x, int div) {return (Math.pow(x[0]*Math.cos(alpha/div)+x[1]*Math.sin(alpha/div),2) + 6*Math.pow(x[0]*Math.sin(alpha/div)+x[1]*Math.cos(alpha/div), 2));}
	 
	 

}

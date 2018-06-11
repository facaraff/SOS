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
package applications.CEC2011;

import interfaces.Problem;

public class P1 extends Problem {
	
		private static double theta = 2*Math.PI/100;
		
		// target signal parameters
		private double[] x0 = {1.0, 5.0, 1.5, 4.8, 2.0, 4.9};
		
		// parameter bounds
//		private double[][] bounds = {{-6.4, 6.35}, {-6.4, 6.35}, {-6.4, 6.35}, {-6.4, 6.35}, {-6.4, 6.35}, {-6.4, 6.35}};
			
		
		public P1() //throws Exception
		{
			super(6, new double[][] {{-6.4, 6.35}, {-6.4, 6.35}, {-6.4, 6.35}, {-6.4, 6.35}, {-6.4, 6.35}, {-6.4, 6.35}}); 
//			setFID(".p1");
			
		}
		
		public double f(double[] x){return squareErrorFunction(x);}
		
		
		
		/**
		 * Evaluate target Signal 
		 * @param x0
		 * @return y0
		 */
		private double targetSignal(int t)
		{
			return x0[0]*Math.sin(x0[1]*t*theta+x0[2]*Math.sin(x0[3]*t*theta+x0[4]*Math.sin(x0[5]*t*theta)));
		}
		
		/**
		 * Evaluate Signal 
		 * @param x
		 * @param t
		 * @return y
		 */
		private double signal(double[] x, int t)
		{
			return x[0]*Math.sin(x[1]*t*theta+x[2]*Math.sin(x[3]*t*theta+x[4]*Math.sin(x[5]*t*theta)));
		}
		
		/**
		 * Fitness function (square error)
		 * @param x
		 * @return fitness
		 */
		private double squareErrorFunction(double[] x)
		{
			double fitness = 0;
			for (int t = 0; t <= 100; t++)
				fitness += Math.pow(signal(x, t)-targetSignal(t), 2);
			return fitness;
		}
}

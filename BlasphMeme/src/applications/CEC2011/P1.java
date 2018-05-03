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

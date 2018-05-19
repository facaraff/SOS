/** @file BaseFunctions.java
 *  @author Fabio Caraffini
*/
package benchmarks.problemsImplementation;

import interfaces.Problem;

/**
 * Benchmark Base Functions.
 */
public class BaseFunctions
{
	/**
	 * Ackley function. 
	 * 
	 * References: 
	 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page295.htm" > Ref 1 </a>
	 * </br>
	 * <a href="http://www.it.lut.fi/ip/evo/functions/node14.html" > Ref 2 </a>
	 * </br>
	 * <a href="http://www.math.ntu.edu.tw/~wwang/cola_lab/test_problems/multiple_opt/multiopt_prob/Ackley/Ackley.htm" > Ref 3 </a>
	 * </br>
	 * <a href="http://tracer.lcc.uma.es/problems/ackley/ackley.html" > Ref 4 </a></br>
	*/
	public static class Ackley extends Problem
	{
		final double a = 20;
		final double b = 0.2;
		final double c = 2*Math.PI;		
		/**
		* Constructor for the  Ackley function defined within the specified upper and lower bounds.
		*/
		public Ackley(int dimension){ super(dimension, new double[] {-1, 1});}
		/**
		* Constructor for the Ackley function defined within a hyper-cube.
		*/
		public Ackley(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Constructor for the Ackley function defined within particular decision space.

		*/
		public Ackley(int dimension, double[][] bounds) { super(dimension, bounds); }
		/**
		* This method implement the Ackley function.
		* 
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
				double square_sum = 0;		
				double cos_sum = 0;

				for (int i = 0; i < n; i++)	
				{
					square_sum += Math.pow(x[i],2);
					cos_sum += Math.cos(c*x[i]);
				}

				y = -a * Math.exp(-b * Math.sqrt(square_sum/n)) - Math.exp(cos_sum/n) + a + Math.exp(1);
			}
			return y;
		}
	}

		
	/**
	 * Alpine function.
	 * 
	 * References:
	 * <a href="http://clerc.maurice.free.fr/pso/Alpine/Alpine_Function.htm" > Ref 1 </a>
	*/
	public static class Alpine extends Problem
	{		
		/**
		* Costructor for the Alpine function defined within the specified upper and lower bounds.
		*/
		public Alpine(int dimension){ super(dimension, new double[] {-10, 10});}
		/**
		* Costructor for the Alpine function defined within a hyper-cube.
		*/
		public Alpine(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Costructor for the Alpine function defined within particular decision space.
		*/
		public Alpine(int dimension, double[][] bounds) { super(dimension, bounds); }
		/**
		* This method implement the Alpine function.
		* 
		* @param x solution to be evaluated
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
					y += Math.abs(x[i]*Math.sin(x[i]) + 0.1*x[i]);
			}
			return y;
		}
	}
	
	/**
	 * Rosenbrock function.
	 * 
	 * References:
	 * <a href="http://en.wikipedia.org/wiki/Rosenbrock_function" > Ref 1 </a>
	 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2537.htm" > Ref 2 </a>
	 * <a href="http://mathworld.wolfram.com/RosenbrockFunction.html" > Ref 3 </a>
	*/
	public static class Rosenbrock extends Problem
	{
		/**
		* Costructor for  the  sphere function defined within the specified upper and lower bounds.
		*/
		public Rosenbrock(int dimension){ super(dimension, new double[] {-100, 100});}
		/**
		* Costructor the  the  sphere function defined within a hyper-cube.
		*/
		public Rosenbrock(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Costructor for the Rosenbrock function defined within particular decision space.
		*/
		public Rosenbrock(int dimension, double[][] bounds) { super(dimension, bounds); }
		/**
		* This method implement the Rosenbrock function.
		* 
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
				for (int i = 0; i < n-1; i++)
				y += Math.pow((1-x[i]),2) + 100*Math.pow((x[i+1]-Math.pow(x[i],2)),2);
			}
			return y;
		 }
	}
	
	/**
	 * Sphere function (DE JONG).
	 * 
	 * References:
	 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page1113.htm" > Ref 1 </a>
	 * <a href="http://www.it.lut.fi/ip/evo/functions/node2.html" > Ref 2 </a>
	*/
		//UNCOMMENT AND COMPLETE THE CODE!!!
//	public static class Sphere extends Problem
//	{
//		/**
//		* Costructor for the Sphere function defined within the specified upper and lower bounds.
//		*/
//		public Sphere(int dimension){ super(dimension, ....  );}
//		/**
//		* Costructor for the Sphere function defined within a hyper-cube.
//		*/
//		public Sphere(int dimension, double[] bounds) { super(dimension, bounds); }
//		/**
//		* Costructor for the Sphere function defined within particular decision space.
//		*/
//		public Sphere(int dimension, double[][] bounds) { super(dimension, bounds); }
//		/**
//		* This method implement the Sphere function.
//		* 
//		* @param x solution to be avaluated
//		*/
//		public double f(double[] x)
//		{
//			final int n = x.length;
//			double y = 0;
//		
//			if(this.getDimension()!= n)
//			{
//				y=Double.NaN;
//				System.out.println("WARNING: the design variable does not match the dimensionality of the problem!");
//			}
//			else
//			{
//				for (int i = 0; i < n; i++)
//					y += .....
//			}
//			return y;
//		}
//	}
	
	/**
	 * Schwefel function.
	 * 
	 * References:
	 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2530.htm" > Ref 1 </a>
	 * <a href="http://www.sfu.ca/~ssurjano/schwef.html" > Ref 2 </a>
	 */
		//UNCOMMENT AND COMPLETE THE CODE
//	public static class Schwefel extends Problem 
//	{
		/**
		* Costructor for the  Schwefel function with suggested bounds.
		*/
//		public Schwefel(int dimension) { super(dimension, ...... }
		/**
		* Costructor for the  Schwefel function defined within a hyper-cube.
		*/
//		public Schwefel(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Costructor for the  Schwefel function defined within particular decision space.
		*/
//		public Schwefel(int dimension, double[][] bounds) { super(dimension, bounds); }
		/**
		* This method implement the Schwefel function.
		* 
		* @param x solution to be avaluated
		*/
//		public double f(double[] x)
//		{
//			final int n = x.length;
//			double sum = 0;
//			double y = 0;
//			if(this.getDimension()!= n)
//			{
//				y=Double.NaN;
//				System.out.println("WARNING: the design variable does not match the dimensionality of the problem!");
//			}
//			else
//			{
//				.......
//			}
//			return y;
//		}
//	}
	
	/**
	 * Rastrigin function.
	 * 
	 * References:
	 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2607.htm" > Ref 1 </a>
	 * <a href="http://en.wikipedia.org/wiki/Rastrigin_function" > Ref 2 </a>
	 * <a href="http://www.mathworks.com/help/toolbox/gads/f14773.html" > Ref 3 </a>
	 */
	//	public static class Rastigin extends Problem
	//	{
		//IMPLEMENT RASTRING FROM SCRACTH
	
	/**
	 * Michalewicz function.
	 * 
	 * References:
	 * <a href="http://www.geatbx.com/docu/fcnindex-01.html#P204_10395" > Ref 1 </a>
	 * <a href="http://www.pg.gda.pl/~mkwies/dyd/geadocu/fcnfun12.html" > Ref 2 </a>
	*/	
//	public static class Michalewicz extends Problem
//	{
		//IMPLEMENT MICHALEXICZ FROM SCRACTH
	
	
}

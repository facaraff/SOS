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
		* Constructor for the Alpine function defined within the specified upper and lower bounds.
		*/
		public Alpine(int dimension){ super(dimension, new double[] {-10, 10});}
		/**
		* Constructor for the Alpine function defined within a hyper-cube.
		*/
		public Alpine(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Constructor for the Alpine function defined within particular decision space.
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
		* Constructor for  Rosenbrock function defined within the specified upper and lower bounds.
		*/
		public Rosenbrock(int dimension){ super(dimension, new double[] {-100, 100});}
		/**
		* Constructor the Rosenbrock function defined within a hyper-cube.
		*/
		public Rosenbrock(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Constructor for Rosenbrock function defined within particular decision space.
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
	public static class Sphere extends Problem
	{
		/**
		* Constructor for the Sphere function defined within the specified upper and lower bounds.
		*/
		public Sphere(int dimension){ super(dimension, new double[] {-5.12, 5.12} );}
		/**
		* Constructor for the Sphere function defined within a hyper-cube.
		*/
		public Sphere(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Constructor for the Sphere function defined within particular decision space.
		*/
		public Sphere(int dimension, double[][] bounds) { super(dimension, bounds); }
		/**
		* This method implement the Sphere function.
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
				for (int i = 0; i < n; i++)
					y += Math.pow(x[i],2);
			}
			return y;
		}
	}
	
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
		* Constructor for the  Schwefel function with suggested bounds.
		*/
//		public Schwefel(int dimension) { super(dimension, ...... }
		/**
		* Constructor for the  Schwefel function defined within a hyper-cube.
		*/
//		public Schwefel(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Constructor for the  Schwefel function defined within particular decision space.
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
	public static class Rastigin extends Problem
	{
		/**
//		 * Rastrigin function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2607.htm" > Ref 1 </a>
//		 * <a href="http://en.wikipedia.org/wiki/Rastrigin_function" > Ref 2 </a>
//		 * <a href="http://www.mathworks.com/help/toolbox/gads/f14773.html" > Ref 3 </a>
//		 */
		
		/**
		* Constructor for the Sphere function defined within the specified upper and lower bounds.
		*/
		public Rastigin(int dimension){ super(dimension, new double[] {-5, 5} );}
		/**
		* Constructor for the Sphere function defined within a hyper-cube.
		*/
		public Rastigin(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Constructor for the Sphere function defined within particular decision space.
		*/
		public Rastigin(int dimension, double[][] bounds) { super(dimension, bounds); }
		/**
		* This method implement the Sphere function.
		* 
		* @param x solution to be evaluated
		*/
		
		public double f(double[] x)
		{
			int n = x.length;
			double sum = 0;

			for (int i = 0; i < n; i++)
				sum += Math.pow(x[i],2) - 10*Math.cos(2*Math.PI*x[i]);

			double y = 10*n + sum;
			
			return y;
		}
	}
	
	/**
	 * Schwefel function.
//	 * 
//	 * References:
//	 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2530.htm" > Ref 1 </a>
//	 * <a href="http://www.it.lut.fi/ip/evo/functions/node11.html" > Ref 2 </a>
	 */
	public static class Schwefel extends Problem
	{
		/**
		 * Schwefel function.
		//	 * 
		//	 * References:
		//	 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2530.htm" > Ref 1 </a>
		//	 * <a href="http://www.it.lut.fi/ip/evo/functions/node11.html" > Ref 2 </a>
		 */
		
		/**
		* Constructor for the Sphere function defined within the specified upper and lower bounds.
		*/
		public Schwefel(int dimension){ super(dimension, new double[] {-500, 500} );}
		/**
		* Constructor for the Sphere function defined within a hyper-cube.
		*/
		public Schwefel(int dimension, double[] bounds) { super(dimension, bounds); }
		/**
		* Constructor for the Sphere function defined within particular decision space.
		*/
		public Schwefel(int dimension, double[][] bounds) { super(dimension, bounds); }
		/**
		* This method implement the Sphere function.
		* 
		* @param x solution to be evaluated
		*/
		
		public double f(double[] x)
		{
			int n = x.length;
			double sum = 0;
			
			for (int i = 0; i < n; i++)
				sum += -x[i] * Math.sin(Math.sqrt(Math.abs(x[i])));
	
			double y = 418.9829*n + sum;
	
			return y;
		}
	}
	
	
	
	
	
	
	
	
	
	
	

	
	
	
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
//}
	
	
	
	
	
	
	
	
	
//	
//	// Hartman 3,4 function constants
//		private static double[][] a_hartman34 = 
//			{{3, 10, 30},
//	  		{0.1, 10, 35},
//	  		{3, 10, 30},
//	  		{0.1, 10,35}};
//		private static double[] c_hartman34 = 
//			{1.0, 1.2, 3.0, 3.2};
//		private static double[][] p_hartman34 = 
//			{{0.36890, 0.11700, 0.26730},
//			{0.46990, 0.43870, 0.74700},
//			{0.10910, 0.87320, 0.55470},
//			{0.03815, 0.57430, 0.88280}};
//
//		// Hartman 6,4 function constants
//		private static double[][] a_hartman64 = 
//			{{10.0, 3.0, 17.0, 3.5, 1.7, 8.0},
//	      	{0.05, 10.0, 17.0, 0.1, 8.0, 14.0},
//	      	{3.0, 3.5, 1.7, 10.0, 17.0, 8.0},
//	      	{17.0, 8.0, 0.05, 10.0, 0.1, 14.0}};
//		private static double[] c_hartman64 = 
//			{1.0, 1.2, 3.0, 3.2};
//		private static double[][] p_hartman64 = 
//			{{0.1312, 0.1696, 0.5569, 0.0124, 0.8283, 0.5886},
//			{0.2329, 0.4135, 0.8307, 0.3736, 0.1004, 0.9991},
//			{0.2348, 0.1451, 0.3522, 0.2883, 0.3047, 0.6650},
//			{0.4047, 0.8828, 0.8732, 0.5743, 0.1091, 0.0381}};
//
//		// Kowalik function constants
//		private static double[] a_kowalik = 
//			{0.1957,0.1947,0.1735,0.16,0.0844,0.0627,0.0456,0.0342,0.0323,0.0235,0.0246};
//		private static double[] b_kowalik = 
//			{4,2,1,0.5,0.25,1/6,0.125,0.1,1/12,1/14,0.0625};
//		
//		// Shekel functions constants
//		private static double[][] a_shekel = 
//			{{4, 4, 4, 4},
//			{1, 1, 1, 1},
//			{8, 8, 8, 8},
//			{6, 6, 6, 6},
//			{3, 7, 3, 7},
//			{2, 9, 2, 9},
//			{5, 5, 3, 3},
//			{8, 1, 8, 1},
//			{6, 2, 6, 2},
//			{7, 3.6, 7, 3.6}};
//		private static double[] c_shekel = 
//			{0.1, 0.2, 0.2, 0.4, 0.4, 0.6, 0.3, 0.7, 0.5, 0.5};
//		
//		/**
//		 * Ackley function. 
//		 * 
//		 * References: 
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page295.htm" > Ref 1 </a>
//		 * </br>
//		 * <a href="http://www.it.lut.fi/ip/evo/functions/node14.html" > Ref 2 </a>
//		 * </br>
//		 * <a href="http://www.math.ntu.edu.tw/~wwang/cola_lab/test_problems/multiple_opt/multiopt_prob/Ackley/Ackley.htm" > Ref 3 </a>
//		 * </br>
//		 * <a href="http://tracer.lcc.uma.es/problems/ackley/ackley.html" > Ref 4 </a></br>
//		 */
//		// bounds = [-1 1];
//		public static double ackley(double[] x)
//		{
//			double a = 20;
//			double b = 0.2;
//			double c = 2*Math.PI;
//
//			int n = x.length;
//
//			double square_sum = 0;		
//			double cos_sum = 0;
//
//			for (int i = 0; i < n; i++)
//			{
//				square_sum += Math.pow(x[i],2);
//				cos_sum += Math.cos(c*x[i]);
//			}
//
//			double y = -a * Math.exp(-b * Math.sqrt(square_sum/n)) - 
//						Math.exp(cos_sum/n) + a + Math.exp(1);
//
//			return y;
//		}
//
//		/**
//		 * Alpine function.
//		 * 
//		 * References:
//		 * <a href="http://clerc.maurice.free.fr/pso/Alpine/Alpine_Function.htm" > Ref 1 </a>
//		 */
//		// bounds = [-10 10]
//		public static double alpine(double[] x)
//		{
//			double y = 0; 
//
//			int n = x.length;
//			for (int i = 0; i < n; i++)
//				y += Math.abs(x[i]*Math.sin(x[i]) + 0.1*x[i]);
//
//			return y;
//		}
//
//		/**
//		 * Axis Parallel function.
//		 * 
//		 * References:
//		 * <a href="http://www.scribd.com/doc/46766287/Test-functions-for-optimization-needs" > Ref 1 </a>
//		 */
//		// bounds = [-10 10]
//		public static double axisParallel(double[] x)
//		{
//			double y = 0; 
//
//			int n = x.length;
//			for (int i = 0; i < n; i++)
//				y += (i+1)*Math.pow(x[i],2);
//
//			return y;
//		}
//		
//		/**
//		 * Branin function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page913.htm" > Ref 1 </a>
//		 * <a href="http://www.it.lut.fi/ip/evo/functions/node27.html" > Ref 2 </a>
//		 * <a href="http://www.math.ntu.edu.tw/~wwang/cola_lab/test_problems/multiple_opt/multiopt_prob/Branin%20function/Branin%20function.htm" > Ref 3 </a>
//		 */	
//		// bounds = [-5 10; 0 15];
//		public static double branins(double[] x)
//		{
//			int n = x.length;
//			double y = Double.NaN;
//
//			if (n == 2)
//			{
//				y = Math.pow((x[1] - (5.1/(4*Math.PI*Math.PI))*Math.pow(x[0],2) + 
//						(5/Math.PI)*x[0] - 6), 2) + 10 * (1 - 1/(8*Math.PI))*Math.cos(x[0]) + 10;
//			}
//
//			return y;
//		}
//
//		/**
//		 * Six-hump Camel Back function.
//		 * 
//		 * References:
//		 * <a href="http://www.it.lut.fi/ip/evo/functions/node26.html" > Ref 1 </a>
//		 * <a href="http://www.math.ntu.edu.tw/~wwang/cola_lab/test_problems/multiple_opt/multiopt_prob/Camelback%20function/Camelback%20function.htm" > Ref 2 </a>
//		 */
//		//bounds = [-5 5; -5 5];
//		public static double camelback(double[] x)
//		{
//			int n = x.length;
//			double y = Double.NaN;
//
//			if (n == 2)
//			{
//				y = 4*Math.pow(x[0],2) - 2.1*Math.pow(x[0],4) + Math.pow(x[0],6)/3 + 
//						x[0]*x[1] - 4*Math.pow(x[1],2) + 4*Math.pow(x[1],4);
//			}
//			return y;
//		}
//		
//		/**
//		 * De Jong function 1.
//		 * 
//		 * References:
//		 * <a href="http://home.ku.edu.tr/~dyuret/pub/aitr1569/node19.html" > Ref 1 </a>
//		 */
//		// bounds = [-5.12 5.12]	
//		public static double deJong(double[] x)
//		{
//			int n = x.length;
//
//			double square_sum = 0;		
//
//			for (int i = 0; i < n; i++)
//				square_sum += Math.pow(x[i],2);
//
//			return square_sum;
//		}
//		
//		/**
//		 * Drop Wave function.
//		 * 
//		 * References:
//		 * <a href="http://www.scribd.com/doc/46766287/Test-functions-for-optimization-needs" > Ref 1 </a>
//		 */
//		// bounds = [-5.12 5.12]	
//		public static double dropWave(double[] x)
//		{
//			int n = x.length;
//			
//			double square_sum = 0;		
//			for (int i = 0; i < n; i++)
//				square_sum += Math.pow(x[i],2);
//			
//			double y = -(1 + Math.cos(12*Math.sqrt(square_sum))) / (0.5*square_sum + 2);
//
//			return y;
//		}
//
//		/**
//		 * Easom function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page1361.htm" > Ref 1 </a>
//		 */
//		// bounds = [-100 100; -100 100]
//		public static double easom(double[] x)
//		{
//			int n = x.length;
//			double y = Double.NaN;
//
//			if (n == 2)
//			{
//				y = -Math.cos(x[0])*Math.cos(x[1])*Math.exp(-Math.pow((x[0]-Math.PI),2) - Math.pow((x[1]-Math.PI),2));
//			}
//
//			return y;
//		}
//
//		/**
//		 * Goldstein-Prince function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page1760.htm" > Ref 1 </a>
//		 */	
//		// bounds = [-2 2]	
//		public static double goldsteinPrice(double[] x)
//		{
//			int n = x.length;
//			double y = Double.NaN;
//
//			if (n == 2)
//			{
//				y = (1+Math.pow((x[0]+x[1]+1),2)*(19-14*x[0]+3*Math.pow(x[0],2)-14*x[1]+6*x[0]*x[1]+3*Math.pow(x[1],2)))*
//					(30+Math.pow((2*x[0]-3*x[1]),2)*(18-32*x[0]+12*Math.pow(x[0],2)+48*x[1]-36*x[0]*x[1]+27*Math.pow(x[1],2)));
//			}
//
//			return y;
//		}
//
//		/**
//		 * Generalized penalized function 1.
//		 */	
//		// bounds = [-50 50]
//		public static double gpf1(double[] x)
//		{
//			int n = x.length;
//			double[] y = new double[n];
//			for (int i = 0; i < n; i++)
//				y[i] = 1 + 0.25*(x[i]+1);
//			
//			int sum = 0;
//			for (int i = 0; i < n-1; i++)
//				sum += Math.pow((y[i]-1),2) * (1 + 10*Math.pow(Math.sin(Math.PI*y[i+1]),2));
//
//			double z = Math.PI/n*10*Math.pow(Math.sin(Math.PI*y[0]),2) + sum + 
//					Math.pow((y[n-1]-1),2) + MathUtils.sum(u(x,10,100,4));
//
//			return z;
//		}
//
//		/**
//		 * Generalized penalized function 2.
//		 */	
//		// bounds = [-50 50]
//		public static double gpf2(double[] x)
//		{
//			int n = x.length;
//			
//			int sum = 0;
//			for (int i = 0; i < n-1; i++)
//				sum += Math.pow((x[i]-1),2) * (1 + Math.pow(Math.sin(3*Math.PI*x[i+1]),2));
//			
//			double z = 0.1*Math.pow(Math.sin(3*Math.PI*x[0]),2) + sum +
//			        (x[n-1]-1)*(1 + Math.pow(Math.sin(2*Math.PI*x[n-1]),2)) + MathUtils.sum(u(x,5,100,4));
//
//			return z;
//		}
//
//		/**
//		 * Griewank function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page1905.htm" > Ref 1 </a>
//		 * <a href="http://mathworld.wolfram.com/GriewankFunction.html" > Ref 2 </a>
//		 */	
//		// bounds [-600 600]
//		public static double griewank(double[] x)
//		{
//			int n = x.length;
//
//			double sum = 0;
//			double prod = 1;
//			for (int i = 0; i < n; i++)
//			{
//				sum += Math.pow(x[i],2);
//				prod *= Math.cos(x[i]/Math.sqrt(i+1));
//			}
//			
//			double y = sum/4000 - prod + 1;
//			
//			return y;
//		}
//		
//		/**
//		 * Hartman 3,4 function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page1488.htm" > Ref 1 </a>
//		 */
//		// bounds = [0 1]
//		public static double hartman34(double[] x)
//		{
//			int n = x.length; 
//			double y = Double.NaN;
//			
//			if (n == 3)
//			{
//				double sum = 0;
//				for (int i = 0; i < 4; i++)
//				{
//					double sm = 0;
//					for (int j = 0; j < 3; j++)
//						sm += a_hartman34[i][j]*Math.pow((x[j]-p_hartman34[i][j]),2);
//
//					sum += c_hartman34[i]*Math.exp(-sm);
//				}
//				y = -sum;
//			}
//			
//			return y;
//		}
//
//		/**
//		 * Hartman 6,4 function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page1488.htm" > Ref 1 </a>
//		 */
//		// bounds = [0 1]
//		public static double hartman64(double[] x)
//		{
//			int n = x.length; 
//			double y = Double.NaN;
//			
//			if (n == 6)
//			{
//				double sum = 0;
//				for (int i = 0; i < 4; i++)
//				{
//					double sm = 0;
//					for (int j = 0; j < 6; j++)
//						sm += a_hartman64[i][j]*Math.pow((x[j]-p_hartman64[i][j]),2);
//
//					sum += c_hartman64[i]*Math.exp(-sm);
//				}
//				y = -sum;
//			}
//			
//			return y;
//		}
//
//		/**
//		 * Kowalik function.
//		 * 
//		 * References:
//		 * <a href="https://docs.google.com/viewer?url=http://www.cba.nau.edu/facstaff/jerrell-m/Research/cef99.pdf" > Ref 1 </a>
//		 */
//		// bounds = [-5 5]
//		public static double kowalik(double[] x)
//		{
//			double y = Double.NaN;
//
//			if (x.length == 4)
//			{
//				double sum = 0;
//				for (int i= 0; i < 11; i++)
//				{
//					sum += a_kowalik[i] - (x[0]*(Math.pow(b_kowalik[i],2) + b_kowalik[i]*x[1]))/
//											(Math.pow(b_kowalik[i],2) + b_kowalik[i]*x[2] + x[3]);
//				}
//				y = sum;
//			}
//			
//			return y;
//		}
//
//		/**
//		 * Michalewicz function.
//		 * 
//		 * References:
//		 * <a href="http://www.geatbx.com/docu/fcnindex-01.html#P204_10395" > Ref 1 </a>
//		 * <a href="http://www.pg.gda.pl/~mkwies/dyd/geadocu/fcnfun12.html" > Ref 2 </a>
//		 */	
//		// bounds [0 pi]
//		public static double michalewicz(double[] x)
//		{
//			int n = x.length;
//			double m = 10;
//				
//			double sum = 0;
//			for (int i = 0; i < n; i++)
//				sum += Math.sin(x[i])*Math.pow(Math.sin((i+1)*Math.pow(x[i],2)/Math.PI),2*m);
//			
//			double y = -sum;
//			
//			return y;
//		}
//
//		/**
//		 * Moved axis function.
//		 * 
//		 * References:
//		 * <a href="http://www.geatbx.com/docu/fcnindex-01.html#P119_4694" > Ref 1 </a>
//		 */
//		// bounds = [-5.12 5.12]
//		public static double movedAxis(double[] x)
//		{
//			int n = x.length;
//			double y = 0;
//			for (int i = 0; i < n; i++)
//				y += 5*(i+1)*Math.pow(x[i],2);
//
//			return y;
//		}
//
//		/**
//		 * Pathological function.
//		 * 
//		 * References:
//		 * <a href="http://books.google.com/books?id=557PPQbkfNYC&pg=PA217&lpg=PA217&dq=tirronen+function&source=bl&ots=KAnV7QAcOC&sig=FeNphyyOBTeLhKJa-nkFGpnZNNY&hl=it&ei=pltjTeqEN4XCtAaole21CA&sa=X&oi=book_result&ct=result&resnum=1&sqi=2&ved=0CBUQ6AEwAA#v=onepage&q=tirronen%20function&f=false" > Ref 1 </a>
//		 */
//		// bounds = [-100 100]
//		public static double pathological(double[] x)
//		{
//			int n = x.length;
//			double y = 0;
//
//			for (int i = 0; i < n-1; i++)
//				y += 0.5 + Math.pow(Math.sin(Math.sqrt(100*Math.pow(x[i],2)+Math.pow(x[i+1],2))-0.5),2)/
//						(1+0.001*Math.pow((Math.pow(x[i],2)-2*x[i]*x[i+1]+Math.pow(x[i+1],2)),2));
//
//			return y;
//		}
//
//		/**
//		 * Rastrigin function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2607.htm" > Ref 1 </a>
//		 * <a href="http://en.wikipedia.org/wiki/Rastrigin_function" > Ref 2 </a>
//		 * <a href="http://www.mathworks.com/help/toolbox/gads/f14773.html" > Ref 3 </a>
//		 */
//		// bounds = [-5 5]
//		public static double rastrigin(double[] x)
//		{
//			int n = x.length;
//			double sum = 0;
//
//			for (int i = 0; i < n; i++)
//				sum += Math.pow(x[i],2) - 10*Math.cos(2*Math.PI*x[i]);
//
//			double y = 10*n + sum;
//			
//			return y;
//		}
//		
//		/**
//		 * Rastrigin non continuous function.
//		 * 
//		 * References:
//		 * <a href="http://www.cil.pku.edu.cn/resources/benchmark_pso/" > Ref 1 </a>
//		 */
//		// bounds = [-500 500]
//		public static double rastriginNonCont(double[] x)
//		{
//			int n = x.length;
//			double y = 0; 
//			
//			for (int i = 0; i < n; i++)
//			{
//				if (Math.abs(x[i]) < 0.5)
//					y = x[i];
//				else
//					y = Math.round(2*x[i])/2;
//
//				y += Math.pow(y,2) - 10*Math.cos(2*Math.PI*y) + 10;			
//			}
//			
//			return y;
//		}
//		
//		/**
//		 * Rosenbrock function.
//		 * 
//		 * References:
//		 * <a href="http://en.wikipedia.org/wiki/Rosenbrock_function" > Ref 1 </a>
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2537.htm" > Ref 2 </a>
//		 * <a href="http://mathworld.wolfram.com/RosenbrockFunction.html" > Ref 3 </a>
//		 */
//		// bounds = [-100 100]
//		public static double rosenbrock(double[] x)
//		{
//			int n = x.length;
//			double y = 0;
//
//			for (int i = 0; i < n-1; i++)
//				y += Math.pow((1-x[i]),2) + 100*Math.pow((x[i+1]-Math.pow(x[i],2)),2);
//
//			return y;
//		}
//		
//		/**
//		 * Rotated hyper-ellipsoid function.
//		 * 
//		 * References:
//		 * <a href="http://www.scribd.com/doc/46766287/Test-functions-for-optimization-needs" > Ref 1 </a>
//		 * <a href="http://www.geatbx.com/docu/fcnindex-01.html#P109_4163" > Ref 2 </a>
//		 */
//		// bounds = [-65536 65536]
//		public static double rotHypEllip(double[] x)
//		{
//			int n = x.length;
//			double y = 0;
//
//			for (int i = 0; i < n; i++)
//				y += Math.pow(MathUtils.sum(x,0,i),2);
//
//			return y;
//		}
//

//		
//		/**
//		 * Schwefel function 1.2.
//		 * 
//		 * References:
//		 * <a href="https://docs.google.com/viewer?url=http://sci2s.ugr.es/eamhco/testfunctions-SOCO.pdf" > Ref 1 </a>
//		 * <a href="http://books.google.com/books?id=b1Y7TmeWhqcC&pg=PA1103&lpg=PA1103&dq=schwefel+function+1.2&source=bl&ots=gjGmgrrEjs&sig=J2G7zg7z8DzNk1kmqk8RNHwIjlU&hl=it&ei=TVZjTdvPMMv5sgaA3vS1CA&sa=X&oi=book_result&ct=result&resnum=4&sqi=2&ved=0CCwQ6AEwAw#v=onepage&q=schwefel%20function%201.2&f=false" > Ref 2 </a>
//		 */	
//		// bounds = [-100 100]
//		public static double schwefel12(double[] x)
//		{
//			int n = x.length;
//			double y = 0;
//			
//			for (int i = 0; i < n; i++)
//				y += Math.pow(MathUtils.sum(x,0,i),2);
//			
//			return y;
//		}
//		
//		/**
//		 * Schwefel function 2.21.
//		 * 
//		 * References:
//		 * <a href="https://docs.google.com/viewer?url=http://sci2s.ugr.es/eamhco/testfunctions-SOCO.pdf" > Ref 1 </a>
//		 * <a href="http://books.google.com/books?id=yTAtVQev0uMC&pg=PA53&lpg=PA53&dq=schwefel+2.21&source=bl&ots=Ga40MjjSXK&sig=qHmHpq4aqlLQ9afxhz7re2P3z8o&hl=it&ei=FFljTfG_DsLLtAbk8_y1CA&sa=X&oi=book_result&ct=result&resnum=5&ved=0CEIQ6AEwBA#v=onepage&q=schwefel%202.21&f=false" > Ref 2 </a>
//		 */	
//		// bounds = [-100 100]
//		public static double schwefel221(double[] x)
//		{
//			return MathUtils.max(x);
//		}
//
//		/**
//		 * Schwefel function 2.22.
//		 * 
//		 * References:
//		 * <a href="https://docs.google.com/viewer?url=http://sci2s.ugr.es/eamhco/testfunctions-SOCO.pdf" > Ref 1 </a>
//		 * <a href="http://books.google.com/books?id=yTAtVQev0uMC&pg=PA53&lpg=PA53&dq=schwefel+2.21&source=bl&ots=Ga40MjjSXK&sig=qHmHpq4aqlLQ9afxhz7re2P3z8o&hl=it&ei=FFljTfG_DsLLtAbk8_y1CA&sa=X&oi=book_result&ct=result&resnum=5&ved=0CEIQ6AEwBA#v=onepage&q=schwefel%202.21&f=false" > Ref 2 </a>
//		 */	
//		// bounds = [-10 10]
//		public static double schwefel222(double[] x)
//		{
//			int n = x.length;
//			double sum = 0;
//			double prod = 1;
//			double y = 0;
//			double abs_xi = 0;
//
//			for (int i = 0; i < n; i++)
//			{
//				abs_xi = Math.abs(x[i]);
//				sum += abs_xi;
//				prod *= abs_xi;
//			}
//
//			y = sum + prod;
//
//			return y;
//		}
//		
//		/**
//		 * Shekel function 1.
//		 */
//		// bounds = [0 10]
//		public static double shekel1(double[] x)
//		{
//			return shekel(x, 5);
//		}
//
//		/**
//		 * Shekel function 2.
//		 */
//		// bounds = [0 10]
//		public static double shekel2(double[] x)
//		{
//			return shekel(x, 7);
//		}
//
//		/**
//		 * Shekel function 3.
//		 */
//		// bounds = [0 10]
//		public static double shekel3(double[] x)
//		{
//			return shekel(x, 10);
//		}
//		
//		/**
//		 * Sphere function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page1113.htm" > Ref 1 </a>
//		 * <a href="http://www.it.lut.fi/ip/evo/functions/node2.html" > Ref 2 </a>
//		 */
//		// bounds = [-100 100]
//		public static double sphere(double[] x)
//		{
//			int n = x.length;
//			double y = 0;
//			
//			for (int i = 0; i < n; i++)
//				y += x[i]*x[i];
//			
//			return y;
//		}
		
//		/**
//		 * Sum of powers function.
//		 */
//		// bounds = [-1 1]
//		public static double sumOfPowers(double[] x)
//		{
//			int n = x.length;
//			double y = 0;
//
//			for (int i = 0; i < n; i++)
//				y += Math.pow(Math.abs(x[i]), i+2);
//
//			return y;
//		}
//
//		/**
//		 * Tirronen function.
//		 * 
//		 * References:
//		 * <a href="http://books.google.com/books?id=557PPQbkfNYC&pg=PA217&lpg=PA217&dq=tirronen+function&source=bl&ots=KAnV7QAcOC&sig=FeNphyyOBTeLhKJa-nkFGpnZNNY&hl=it&ei=pltjTeqEN4XCtAaole21CA&sa=X&oi=book_result&ct=result&resnum=1&sqi=2&ved=0CBUQ6AEwAA#v=onepage&q=tirronen%20function&f=false" > Ref 1 </a>
//		 */
//		// bounds = [-10 5]
//		public static double tirronen(double[] x)
//		{
//			int n = x.length;
//			double normX = MathUtils.norm2(x);
//			
//			double sum = 0;
//			for (int i = 0; i < n; i++)
//				sum += Math.cos(5*(x[i] + (1 + (i+1)%2)))*Math.cos(normX);
//
//			double y = 3*Math.exp(-Math.pow(normX,2)/(10*n)) - 10*Math.exp(-8*Math.pow(normX,2)) + (2.5/n)*sum;
//
//			return y;
//		}
//		
//		/**
//		 * Zakharov function.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page3088.htm" > Ref 1 </a>
//		 */
//		// bounds = [-5 10]
//		public static double zakharov(double[] x)
//		{
//			int n = x.length;
//			
//			double s1 = 0;
//			double s2 = 0;
//			for (int i = 0; i < n; i++)
//			{
//				s1 += Math.pow(x[i],2);
//				s2 += 0.5*(i+1)*x[i];
//			}
//			
//			double y = s1 + Math.pow(s2,2) + Math.pow(s2,4);
//			
//			return y;
//		}
//
//		/**
//		 * This function is used in Shekel's family test functions.
//		 * 
//		 * References:
//		 * <a href="http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2354.htm" > Ref 1 </a>
//		 */
//		private static double shekel(double[] x, int m)
//		{
//			double y = 0;
//			
//			double sum = 0;
//			for (int j = 0; j < m; j++)
//			{		
//				double p = 0;
//				for (int k = 0; k < 4; k++)
//					p += Math.pow((x[k] - a_shekel[j][k]),2);
//				sum += 1/(p+c_shekel[j]);
//			}
//
//			y = -sum;
//			
//			return y;
//		}
//		
//		/**
//		 * This function is used in gpf1() and gpf2().
//		 */
//		private static double[] u(double[] x, int a, int k,int m)
//		{
//			int n = x.length; 
//			double[] y = new double[n];
//			for (int i = 0; i < n; i++)
//			{
//				y[i] = 0.0;
//				double x_i = x[i];
//				if (x_i > a)
//					y[i] = k * Math.pow((x_i-a), m);
//				else if (x_i < -a)
//					y[i] = k * Math.pow((-x_i-a), m);
//			}
//			
//			return y;
//		}
//	
//	
	
	
	
}

package test;

import java.text.DecimalFormat;
import java.util.Vector;

import algorithms.interfaces.Algorithm;
import algorithms.interfaces.Problem;
import algorithms.utils.Best;
import darwin.BenchmarkBBOB2010;
import darwin.BenchmarkBaseFunctions;
import darwin.BenchmarkCEC2005;
import darwin.BenchmarkCEC2008;
import darwin.BenchmarkCEC2013;
import darwin.BenchmarkCEC2014;
import darwin.BenchmarkSISC2010;

public class TestOptimizerHelper
{
	private static DecimalFormat formatter = new DecimalFormat("0.000E00");
	
	public static String format(double x)
	{
		String s = formatter.format(x);
		if (!s.contains("E-"))
		    s = s.replace("E", "E+");
		return s;
	}
	
	/**
	 * Wrapper class for meta-optimization (experimental).
	 */
	public static class MetaOptimization extends Problem 
	{
		private Algorithm algorithm;
		private Problem problem;
		
		public MetaOptimization(int dimension, double[] bounds, Algorithm algorithm, Problem problem)
		{
			super(dimension, bounds);
			this.algorithm = algorithm;
			this.problem = problem;
		}
		
		public double f(double[] x)
		{
			int n = x.length;
			for (int i = 0; i < n; i++)
				algorithm.pushParameter("p" + i,x[i]);
			
			try
			{
				Vector<Best> bests = algorithm.execute(problem, 5000*problem.getDimension());
				return bests.get(bests.size()-1).getfBest();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return Double.NaN;
			}
		}
	}

	public static class Rosenbrock extends Problem 
	{
		public Rosenbrock(int dimension, double[] bounds) { super(dimension, bounds); }
		public double f(double[] x) { return BenchmarkBaseFunctions.rosenbrock(x); }
	}

	public static class Michalewicz extends Problem 
	{
		public Michalewicz(int dimension, double[] bounds) { super(dimension, bounds); }
		public double f(double[] x) { return BenchmarkBaseFunctions.michalewicz(x); }
	}

	public static class Schwefel extends Problem 
	{
		public Schwefel(int dimension, double[] bounds) { super(dimension, bounds); }
		public double f(double[] x) { return BenchmarkBaseFunctions.schwefel(x); }
	}
	
	public static class Sphere extends Problem 
	{
		public Sphere(int dimension, double[] bounds) { super(dimension, bounds); }
		public double f(double[] x) { return BenchmarkBaseFunctions.sphere(x); }
	}
	
	public static class CEC2005 extends Problem 
	{
		int problem;
		public CEC2005(int problem, int dimension, double[] bounds) { super(dimension, bounds); this.problem = problem; }
		public double f(double[] x)
		{
			switch (problem) {
				case 1:
					return BenchmarkCEC2005.f1(x);
				case 2:
					return BenchmarkCEC2005.f2(x);
				case 3:
					return BenchmarkCEC2005.f3(x);
				case 4:
					return BenchmarkCEC2005.f4(x);
				case 5:
					return BenchmarkCEC2005.f5(x);
				case 6:
					return BenchmarkCEC2005.f6(x);
				case 7:
					return BenchmarkCEC2005.f7(x);
				case 8:
					return BenchmarkCEC2005.f8(x);
				case 9:
					return BenchmarkCEC2005.f9(x);
				case 10:
					return BenchmarkCEC2005.f10(x);
				case 11:
					return BenchmarkCEC2005.f11(x);
				case 12:
					return BenchmarkCEC2005.f12(x);
				case 13:
					return BenchmarkCEC2005.f13(x);
				case 14:
					return BenchmarkCEC2005.f14(x);
				case 15:
					return BenchmarkCEC2005.f15(x);
				case 16:
					return BenchmarkCEC2005.f16(x);
				case 17:
					return BenchmarkCEC2005.f17(x);
				case 18:
					return BenchmarkCEC2005.f18(x);
				case 19:
					return BenchmarkCEC2005.f19(x);
				case 20:
					return BenchmarkCEC2005.f20(x);
				case 21:
					return BenchmarkCEC2005.f21(x);
				case 22:
					return BenchmarkCEC2005.f22(x);
				case 23:
					return BenchmarkCEC2005.f23(x);
				case 24:
					return BenchmarkCEC2005.f24(x);
				case 25:
					return BenchmarkCEC2005.f25(x);
				default:
					return Double.NaN;
			}
		}
	}

	public static class CEC2008 extends Problem 
	{
		int problem;
		public CEC2008(int problem, int dimension, double[] bounds) { super(dimension, bounds); this.problem = problem; }
		public double f(double[] x)
		{
			switch (problem) {
				case 1:
					return BenchmarkCEC2008.f1(x);
				case 2:
					return BenchmarkCEC2008.f2(x);
				case 3:
					return BenchmarkCEC2008.f3(x);
				case 4:
					return BenchmarkCEC2008.f4(x);
				case 5:
					return BenchmarkCEC2008.f5(x);
				case 6:
					return BenchmarkCEC2008.f6(x);
				case 7:
					return BenchmarkCEC2008.f7(x);
				default:
					return Double.NaN;
			}
		}
	}

	public static class CEC2013 extends Problem 
	{
		int problem;
		public CEC2013(int problem, int dimension, double[] bounds) { super(dimension, bounds); this.problem = problem; }
		public double f(double[] x)
		{
			switch (problem) {
				case 1:
					return BenchmarkCEC2013.f1(x);
				case 2:
					return BenchmarkCEC2013.f2(x);
				case 3:
					return BenchmarkCEC2013.f3(x);
				case 4:
					return BenchmarkCEC2013.f4(x);
				case 5:
					return BenchmarkCEC2013.f5(x);
				case 6:
					return BenchmarkCEC2013.f6(x);
				case 7:
					return BenchmarkCEC2013.f7(x);
				case 8:
					return BenchmarkCEC2013.f8(x);
				case 9:
					return BenchmarkCEC2013.f9(x);
				case 10:
					return BenchmarkCEC2013.f10(x);
				case 11:
					return BenchmarkCEC2013.f11(x);
				case 12:
					return BenchmarkCEC2013.f12(x);
				case 13:
					return BenchmarkCEC2013.f13(x);
				case 14:
					return BenchmarkCEC2013.f14(x);
				case 15:
					return BenchmarkCEC2013.f15(x);
				case 16:
					return BenchmarkCEC2013.f16(x);
				case 17:
					return BenchmarkCEC2013.f17(x);
				case 18:
					return BenchmarkCEC2013.f18(x);
				case 19:
					return BenchmarkCEC2013.f19(x);
				case 20:
					return BenchmarkCEC2013.f20(x);
				case 21:
					return BenchmarkCEC2013.f21(x);
				case 22:
					return BenchmarkCEC2013.f22(x);
				case 23:
					return BenchmarkCEC2013.f23(x);
				case 24:
					return BenchmarkCEC2013.f24(x);
				case 25:
					return BenchmarkCEC2013.f25(x);
				case 26:
					return BenchmarkCEC2013.f26(x);
				case 27:
					return BenchmarkCEC2013.f27(x);
				case 28:
					return BenchmarkCEC2013.f28(x);
				default:
					return Double.NaN;
			}
		}
	}
	
	public static class CEC2014 extends Problem 
	{
		int problem;
		public CEC2014(int problem, int dimension, double[] bounds) { super(dimension, bounds); this.problem = problem; }
		public double f(double[] x)
		{
			switch (problem) {
				case 1:
					return BenchmarkCEC2014.f1(x);
				case 2:
					return BenchmarkCEC2014.f2(x);
				case 3:
					return BenchmarkCEC2014.f3(x);
				case 4:
					return BenchmarkCEC2014.f4(x);
				case 5:
					return BenchmarkCEC2014.f5(x);
				case 6:
					return BenchmarkCEC2014.f6(x);
				case 7:
					return BenchmarkCEC2014.f7(x);
				case 8:
					return BenchmarkCEC2014.f8(x);
				case 9:
					return BenchmarkCEC2014.f9(x);
				case 10:
					return BenchmarkCEC2014.f10(x);
				case 11:
					return BenchmarkCEC2014.f11(x);
				case 12:
					return BenchmarkCEC2014.f12(x);
				case 13:
					return BenchmarkCEC2014.f13(x);
				case 14:
					return BenchmarkCEC2014.f14(x);
				case 15:
					return BenchmarkCEC2014.f15(x);
				case 16:
					return BenchmarkCEC2014.f16(x);
				case 17:
					return BenchmarkCEC2014.f17(x);
				case 18:
					return BenchmarkCEC2014.f18(x);
				case 19:
					return BenchmarkCEC2014.f19(x);
				case 20:
					return BenchmarkCEC2014.f20(x);
				case 21:
					return BenchmarkCEC2014.f21(x);
				case 22:
					return BenchmarkCEC2014.f22(x);
				case 23:
					return BenchmarkCEC2014.f23(x);
				case 24:
					return BenchmarkCEC2014.f24(x);
				case 25:
					return BenchmarkCEC2014.f25(x);
				case 26:
					return BenchmarkCEC2014.f26(x);
				case 27:
					return BenchmarkCEC2014.f27(x);
				case 28:
					return BenchmarkCEC2014.f28(x);
				case 29:
					return BenchmarkCEC2014.f29(x);
				case 30:
					return BenchmarkCEC2014.f30(x);
				default:
					return Double.NaN;
			}
		}
	}

	public static class BBOB10 extends Problem 
	{
		int problem;
		public BBOB10(int problem, int dimension, double[] bounds) { super(dimension, bounds); this.problem = problem; }
		public double f(double[] x)
		{
			switch (problem) {
				case 1:
					return BenchmarkBBOB2010.f1(x);
				case 2:
					return BenchmarkBBOB2010.f2(x);
				case 3:
					return BenchmarkBBOB2010.f3(x);
				case 4:
					return BenchmarkBBOB2010.f4(x);
				case 5:
					return BenchmarkBBOB2010.f5(x);
				case 6:
					return BenchmarkBBOB2010.f6(x);
				case 7:
					return BenchmarkBBOB2010.f7(x);
				case 8:
					return BenchmarkBBOB2010.f8(x);
				case 9:
					return BenchmarkBBOB2010.f9(x);
				case 10:
					return BenchmarkBBOB2010.f10(x);
				case 11:
					return BenchmarkBBOB2010.f11(x);
				case 12:
					return BenchmarkBBOB2010.f12(x);
				case 13:
					return BenchmarkBBOB2010.f13(x);
				case 14:
					return BenchmarkBBOB2010.f14(x);
				case 15:
					return BenchmarkBBOB2010.f15(x);
				case 16:
					return BenchmarkBBOB2010.f16(x);
				case 17:
					return BenchmarkBBOB2010.f17(x);
				case 18:
					return BenchmarkBBOB2010.f18(x);
				case 19:
					return BenchmarkBBOB2010.f19(x);
				case 20:
					return BenchmarkBBOB2010.f20(x);
				case 21:
					return BenchmarkBBOB2010.f21(x);
				case 22:
					return BenchmarkBBOB2010.f22(x);
				case 23:
					return BenchmarkBBOB2010.f23(x);
				case 24:
					return BenchmarkBBOB2010.f24(x);
				default:
					return Double.NaN;
			}
		}
	}
	
	public static class SISC2010 extends Problem 
	{
		int problem;
		public SISC2010(int problem, int dimension, double[] bounds) { super(dimension, bounds); this.problem = problem; }
		public double f(double[] x)
		{
			switch (problem) {
			case 1:
				return BenchmarkSISC2010.f1(x);
			case 2:
				return BenchmarkSISC2010.f2(x);
			case 3:
				return BenchmarkSISC2010.f3(x);
			case 4:
				return BenchmarkSISC2010.f4(x);
			case 5:
				return BenchmarkSISC2010.f5(x);
			case 6:
				return BenchmarkSISC2010.f6(x);
			case 7:
				return BenchmarkSISC2010.f7(x);
			case 8:
				return BenchmarkSISC2010.f8(x);
			case 9:
				return BenchmarkSISC2010.f9(x);
			case 10:
				return BenchmarkSISC2010.f10(x);
			case 11:
				return BenchmarkSISC2010.f11(x);
			case 12:
				return BenchmarkSISC2010.f12(x);
			case 13:
				return BenchmarkSISC2010.f13(x);
			case 14:
				return BenchmarkSISC2010.f14(x);
			case 15:
				return BenchmarkSISC2010.f15(x);
			case 16:
				return BenchmarkSISC2010.f16(x);
			case 17:
				return BenchmarkSISC2010.f17(x);
			case 18:
				return BenchmarkSISC2010.f18(x);
			case 19:
				return BenchmarkSISC2010.f19(x);
			default:
				return Double.NaN;
			}
		}
	}
}
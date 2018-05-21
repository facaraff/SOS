package darwin;

import javacec2013.JNIfgeneric2013;

/**
 * Benchmark Functions for the IEEE CEC 2013 - Special Session 
 * and Competition on Large Scale Global Optimization.
 */
public class BenchmarkCEC2013_LSGO
{
	private static JNIfgeneric2013 fgeneric;
	
	private static void initializeCEC2013(int funcId, int dim)
	{
		fgeneric = new JNIfgeneric2013(funcId, dim);
	}

	public static void finalizeCEC2013()
	{
		if (fgeneric != null)
		{
			fgeneric.destroy();
			fgeneric = null;
		}
	}
	
	public static double f1(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(1, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f2(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(2, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f3(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(3, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f4(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(4, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f5(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(5, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f6(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(6, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f7(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(7, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f8(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(8, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f9(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(9, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f10(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(10, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f11(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(11, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f12(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(12, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f13(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(13, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f14(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(14, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f15(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(15, x.length);
		return fgeneric.evaluate(x);
	}
}
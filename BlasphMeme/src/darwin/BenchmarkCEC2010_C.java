package darwin;

import javacec2010.JNIfgeneric2010;

/**
 * Benchmark Functions for the IEEE CEC 2010 - Special Session 
 * and Competition on Large Scale Global Optimization.
 */
public class BenchmarkCEC2010_C
{
	private static JNIfgeneric2010 fgeneric;
	
	private static void initializeCEC2010(int funcId, int dim)
	{
		fgeneric = new JNIfgeneric2010(funcId, dim);
	}

	public static void finalizeCEC2010()
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
			initializeCEC2010(1, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f2(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(2, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f3(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(3, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f4(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(4, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f5(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(5, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f6(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(6, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f7(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(7, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f8(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(8, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f9(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(9, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f10(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(10, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f11(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(11, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f12(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(12, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f13(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(13, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f14(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(14, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f15(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(15, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f16(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(16, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f17(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(17, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f18(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(18, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f19(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(19, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f20(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2010(20, x.length);
		return fgeneric.evaluate(x);
	}
}
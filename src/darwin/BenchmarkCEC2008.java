package darwin;

import utils.benchmarks.BenchmarkLoader;
import benchmarks.problemsImplementation.fractalFunctions.FastFractal;


/**
 * Benchmark Functions for the IEEE CEC 2008 - Special Session and
 * Competition on Large Scale Global Optimization.
 */
public class BenchmarkCEC2008
{
	static
	{
		try
		{
			BenchmarkLoader.loadNativeLibraryFromJar("libcec2008");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static FastFractal fastFractal;
	
	private static native double nativeCEC2008Function(double[] x, int functionNumber);
	
	public static double f1(double[] x)
	{
		return nativeCEC2008Function(x,1);
	}

	public static double f2(double[] x)
	{
		return nativeCEC2008Function(x,2);
	}
	
	public static double f3(double[] x)
	{
		return nativeCEC2008Function(x,3);
	}
	
	public static double f4(double[] x)
	{
		return nativeCEC2008Function(x,4);
	}
	
	public static double f5(double[] x)
	{
		return nativeCEC2008Function(x,5);
	}

	public static double f6(double[] x)
	{
		return nativeCEC2008Function(x,6);
	}
	
	public static double f7(double[] x)
	{
		if (fastFractal == null)
		{
			try {
//				fastFractal = new FastFractal("fractalFunctions.DoubleDip", 3, 1, 1, x.length);
				fastFractal = new FastFractal("benchmarks.problemsImplementation.fractalFunctions.DoubleDip", 3, 1, 1, x.length);//XXX FABIO
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (fastFractal != null)
			return fastFractal.evaluate(x);
		else
			return Double.NaN;
	}
	
	public static  double[] getBounds(int funcNum)
	{
		double[] bounds = null;
		if((funcNum==7))
			bounds = new double[] {-1,1};
		else if((funcNum==6))
			bounds = new double[] {-32,32};
		else if((funcNum==5))
			bounds = new double[] {-600,600};
		else if((funcNum==4))
			bounds = new double[] {-5,5};
		else
			bounds = new double[] {-100,100};
		return bounds;
	}
	
	public static double f(int funcNum, double[] x)
	{
		switch (funcNum) 
		{
		case 1:
			return f1(x);
		case 2:
			return f2(x);
		case 3:
			return f3(x);
		case 4:
			return f4(x);
		case 5:
			return f5(x);
		case 6:
			return f6(x);
		case 7:
			return f7(x);
		default:
			System.out.println("This funcion does not exist in this benchmark!");
			return Double.NaN;
		}
	}
}
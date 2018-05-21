package darwin;

import utils.benchmarks.BenchmarkLoader;

/**
 * Benchmark Functions for the IEEE CEC 2013 - Special Session and 
 * Competition on Real-Parameter Optimization.
 */
public class BenchmarkCEC2013
{
	static
	{
		try
		{
			BenchmarkLoader.loadNativeLibraryFromJar("libcec2013");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static native double nativeCEC2013Function(double[] x, int functionNumber);
	
	private static double f1(double[] x)
	{
		return nativeCEC2013Function(x,1);
	}

	private static double f2(double[] x)
	{
		return nativeCEC2013Function(x,2);
	}
	
	private static double f3(double[] x)
	{
		return nativeCEC2013Function(x,3);
	}
	
	private static double f4(double[] x)
	{
		return nativeCEC2013Function(x,4);
	}
	
	private static double f5(double[] x)
	{
		return nativeCEC2013Function(x,5);
	}

	private static double f6(double[] x)
	{
		return nativeCEC2013Function(x,6);
	}
	
	private static double f7(double[] x)
	{
		return nativeCEC2013Function(x,7);
	}
	
	private static double f8(double[] x)
	{
		return nativeCEC2013Function(x,8);
	}
	
	private static double f9(double[] x)
	{
		return nativeCEC2013Function(x,9);
	}
	
	private static double f10(double[] x)
	{
		return nativeCEC2013Function(x,10);
	}
	
	private static double f11(double[] x)
	{
		return nativeCEC2013Function(x,11);
	}
	
	private static double f12(double[] x)
	{
		return nativeCEC2013Function(x,12);
	}
	
	private static double f13(double[] x)
	{
		return nativeCEC2013Function(x,13);
	}
	
	private static double f14(double[] x)
	{
		return nativeCEC2013Function(x,14);
	}
	
	private static double f15(double[] x)
	{
		return nativeCEC2013Function(x,15);
	}
	
	private static double f16(double[] x)
	{
		return nativeCEC2013Function(x,16);
	}
	
	private static double f17(double[] x)
	{
		return nativeCEC2013Function(x,17);
	}
	
	private static double f18(double[] x)
	{
		return nativeCEC2013Function(x,18);
	}
	
	private static double f19(double[] x)
	{
		return nativeCEC2013Function(x,19);
	}
	
	private static double f20(double[] x)
	{
		return nativeCEC2013Function(x,20);
	}
	
	private static double f21(double[] x)
	{
		return nativeCEC2013Function(x,21);
	}
	
	private static double f22(double[] x)
	{
		return nativeCEC2013Function(x,22);
	}
	
	private static double f23(double[] x)
	{
		return nativeCEC2013Function(x,23);
	}
	
	private static double f24(double[] x)
	{
		return nativeCEC2013Function(x,24);
	}
	
	private static double f25(double[] x)
	{
		return nativeCEC2013Function(x,25);
	}
	
	private static double f26(double[] x)
	{
		return nativeCEC2013Function(x,26);
	}
	
	private static double f27(double[] x)
	{
		return nativeCEC2013Function(x,27);
	}
	
	private static double f28(double[] x)
	{
		return nativeCEC2013Function(x,28);
	}
	
	public static double f(int num, double[] x)
	{
		switch (num) {
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
		case 8:
			return f8(x);
		case 9:
			return f9(x);
		case 10:
			return f10(x);
		case 11:
			return f11(x);
		case 12:
			return f12(x);
		case 13:
			return f13(x);
		case 14:
			return f14(x);
		case 15:
			return f15(x);
		case 16:
			return f16(x);
		case 17:
			return f17(x);
		case 18:
			return f18(x);
		case 19:
			return f19(x);
		case 20:
			return f20(x);
		case 21:
			return f21(x);
		case 22:
			return f22(x);
		case 23:
			return f23(x);
		case 24:
			return f24(x);
		case 25:
			return f25(x);
		case 26:
			return f26(x);
		case 27:
			return f27(x);
		case 28:
			return f28(x);
		default:
			System.out.println("This problem does not exist in this benchmark!");
			return Double.NaN;
		}
	}
}
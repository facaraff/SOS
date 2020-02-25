package darwin;

import utils.benchmarks.BenchmarkLoader;

/**
 * Benchmark Functions for the Special Issue of Soft Computing (SISC 2010) on 
 * Scalability of Evolutionary Algorithms and other Metaheuristics 
 * for Large Scale Continuous Optimization Problems.
 */
public class BenchmarkSISC2010
{
	static
	{
		try
		{
			BenchmarkLoader.loadNativeLibraryFromJar("libf1f6sisc2010");
			BenchmarkLoader.loadNativeLibraryFromJar("libf7f11sisc2010");
			BenchmarkLoader.loadNativeLibraryFromJar("libf12f19sisc2010");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static native double nativef1f6Function(double[] x, int functionNumber);
	private static native double nativef7f11Function(double[] x, int functionNumber);
	private static native double nativef12f19Function(double[] x, int functionNumber);
	
	private static double f1(double[] x)
	{
		return nativef1f6Function(x,1);
	}

	private static double f2(double[] x)
	{
		return nativef1f6Function(x,2);
	}
	
	private static double f3(double[] x)
	{
		return nativef1f6Function(x,3);
	}
	
	private static double f4(double[] x)
	{
		return nativef1f6Function(x,4);
	}
	
	private static double f5(double[] x)
	{
		return nativef1f6Function(x,5);
	}

	private static double f6(double[] x)
	{
		return nativef1f6Function(x,6);
	}
	
	private static double f7(double[] x)
	{
		return nativef7f11Function(x,7);
	}
	
	private static double f8(double[] x)
	{
		return nativef7f11Function(x,8);
	}
	
	private static double f9(double[] x)
	{
		return nativef7f11Function(x,9);
	}
	
	private static double f10(double[] x)
	{
		return nativef7f11Function(x,10);
	}
	
	private static double f11(double[] x)
	{
		return nativef7f11Function(x,11);
	}
	
	private static double f12(double[] x)
	{
		return nativef12f19Function(x,12);
	}
	
	private static double f13(double[] x)
	{
		return nativef12f19Function(x,13);
	}
	
	private static double f14(double[] x)
	{
		return nativef12f19Function(x,14);
	}
	
	private static double f15(double[] x)
	{
		return nativef12f19Function(x,15);
	}
	
	private static double f16(double[] x)
	{
		return nativef12f19Function(x,16);
	}
	
	private static double f17(double[] x)
	{
		return nativef12f19Function(x,17);
	}
	
	private static double f18(double[] x)
	{
		return nativef12f19Function(x,18);
	}
	
	private static double f19(double[] x)
	{
		return nativef12f19Function(x,19);
	}
	
	public static double[] getBounds(int fNum) 
	{
		double[] bounds = null;
		if((fNum==5))
			bounds = new double[] {-600,600};
		else if((fNum==4)||(fNum==14)||(fNum==18))
			bounds = new double[] {-5,5};
		else if((fNum==10))
			bounds = new double[] {-15,15};
		else if((fNum==6))
			bounds = new double[] {-32,32};
		else if((fNum==7)||(fNum==15)||(fNum==19))
			bounds = new double[] {-10,10};
		else if((fNum==8))
			bounds = new double[] {-65.536,-65.536};
		else
			bounds = new double[] {-100,-100};
			
		return bounds;
	}
	
	public static double f(double[] x, int problem)
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
				System.out.println("This problem is not defined!");
				return Double.NaN;
		}
	}
}
package benchmarks.problemsImplementation;

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
	
	public static double f1(double[] x)
	{
		return nativef1f6Function(x,1);
	}

	public static double f2(double[] x)
	{
		return nativef1f6Function(x,2);
	}
	
	public static double f3(double[] x)
	{
		return nativef1f6Function(x,3);
	}
	
	public static double f4(double[] x)
	{
		return nativef1f6Function(x,4);
	}
	
	public static double f5(double[] x)
	{
		return nativef1f6Function(x,5);
	}

	public static double f6(double[] x)
	{
		return nativef1f6Function(x,6);
	}
	
	public static double f7(double[] x)
	{
		return nativef7f11Function(x,7);
	}
	
	public static double f8(double[] x)
	{
		return nativef7f11Function(x,8);
	}
	
	public static double f9(double[] x)
	{
		return nativef7f11Function(x,9);
	}
	
	public static double f10(double[] x)
	{
		return nativef7f11Function(x,10);
	}
	
	public static double f11(double[] x)
	{
		return nativef7f11Function(x,11);
	}
	
	public static double f12(double[] x)
	{
		return nativef12f19Function(x,12);
	}
	
	public static double f13(double[] x)
	{
		return nativef12f19Function(x,13);
	}
	
	public static double f14(double[] x)
	{
		return nativef12f19Function(x,14);
	}
	
	public static double f15(double[] x)
	{
		return nativef12f19Function(x,15);
	}
	
	public static double f16(double[] x)
	{
		return nativef12f19Function(x,16);
	}
	
	public static double f17(double[] x)
	{
		return nativef12f19Function(x,17);
	}
	
	public static double f18(double[] x)
	{
		return nativef12f19Function(x,18);
	}
	
	public static double f19(double[] x)
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
}
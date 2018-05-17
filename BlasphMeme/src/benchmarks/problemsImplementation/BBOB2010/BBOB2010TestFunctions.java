package benchmarks.problemsImplementation.BBOB2010;

import benchmarks.problemsImplementation.BBOB2010.JNIfgeneric;
import benchmarks.problemsImplementation.BBOB2010.JNIfgeneric.Params;

/**
 * Benchmark Functions defined in the Real-Parameter Black-Box 
 * Optimization Benchmarking (BBOB 2010).
 */
public class BBOB2010TestFunctions
{
	private static int instanceId = 1;
	private static JNIfgeneric fgeneric;
	
	//private double[] bias = new double[] {79.48, -209.88, -462.09, -462.09, -9.21, 35.9, 92.94, 149.15, 123.83, -54.94, 76.27, -621.11, 29.97, -52.35, 1000.0, 71.35, -16.94, -16.94,-102.55, -546.5, 40.78, -1000.0, 6.87, 102.61};
	

	private static void initializeBBOB(int funcId, int instanceId, int dim)
	{
		fgeneric = new JNIfgeneric();
		fgeneric.initBBOB(funcId, instanceId, dim, "", new Params());
	}
	
	public static JNIfgeneric getJNIfgeneric()
	{
		return fgeneric;
	}

	public static void finalizeBBOB()
	{
		if (fgeneric != null)
		{
			fgeneric.exitBBOB();
			fgeneric = null;
			instanceId = 1;
		}
	}

	private static double f1(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(1, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f2(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(2, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f3(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(3, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f4(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(4, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f5(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(5, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f6(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(6, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f7(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(7, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f8(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(8, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f9(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(9, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f10(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(10, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f11(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(11, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f12(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(12, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f13(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(13, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f14(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(14, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f15(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(15, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f16(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(16, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f17(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(17, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f18(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(18, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f19(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(19, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f20(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(20, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f21(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(21, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f22(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(22, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f23(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(23, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f24(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(24, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	/*******************************************************************************************/
	/**                                     NOISE FUNCTIONS                                   **/
	/*******************************************************************************************/
	
	private static double f101(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(101, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f102(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(102, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f103(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(103, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f104(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(104, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f105(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(105, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f106(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(106, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f107(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(107, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f108(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(108, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f109(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(109, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f110(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(110, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f111(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(111, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f112(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(112, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f113(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(113, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f114(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(114, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f115(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(115, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f116(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(116, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f117(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(117, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f118(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(118, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f119(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(119, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f120(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(120, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f121(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(121, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f122(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(122, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f123(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(123, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	private static double f124(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(124, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	private static double f125(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(125, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	private static double f126(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(126, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	private static double f127(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(127, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	private static double f128(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(128, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	private static double f129(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(129, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	private static double f130(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(130, BBOB2010TestFunctions.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	public static  double f_noise(double[] x, int fNum)
	{
		switch (fNum) 
		{
		case 1:
			return f101(x);
		case 2:
			return f102(x);
		case 3:
			return f103(x);
		case 4:
			return f104(x);
		case 5:
			return f105(x);
		case 6:
			return f106(x);
		case 7:
			return f107(x);
		case 8:
			return f108(x);
		case 9:
			return f109(x);
		case 10:
			return f110(x);
		case 11:
			return f111(x);
		case 12:
			return f112(x);
		case 13:
			return f113(x);
		case 14:
			return f114(x);
		case 15:
			return f115(x);
		case 16:
			return f116(x);
		case 17:
			return f117(x);
		case 18:
			return f118(x);
		case 19:
			return f119(x);
		case 20:
			return f120(x);
		case 21:
			return f121(x);
		case 22:
			return f122(x);
		case 23:
			return f123(x);
		case 24:
			return f124(x);
		case 25:
			return f125(x);
		case 26:
			return f126(x);
		case 27:
			return f127(x);
		case 28:
			return f128(x);
		case 29:
			return f129(x);
		case 30:
			return f130(x);
		default:
			System.out.println("This problem does not exist");
			return Double.NaN;
		}
	}
	
	public static double f(double[] x, int fNum)
	{
		switch (fNum) 
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
		default:
			System.out.println("This problem does not exist");
			return Double.NaN;
		}
	}
	
	public static double[] getBounds(int fNum) 
	{
		double[] bounds=null;
		if ((fNum <= 1) || (fNum == 3) || (fNum == 4) || (fNum == 6)|| (fNum == 7)|| (fNum == 8)|| (fNum == 9) || 
			(fNum == 11) || (fNum == 12) || (fNum == 13) || (fNum == 14) || (fNum >= 16))
			bounds = new double[] {-5, 5};
		else
			bounds = new double[] {-32, 32};
		return bounds;
	}


		
	
	
}
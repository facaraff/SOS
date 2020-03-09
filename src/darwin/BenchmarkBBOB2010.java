package darwin;

import javabbob.JNIfgeneric;
import javabbob.JNIfgeneric.Params;

/**
 * Benchmark Functions defined in the Real-Parameter Black-Box 
 * Optimization Benchmarking (BBOB 2010).
 * 
 * @author Giovanni Iacca (giovanni.iacca@gmail.com )
 */
public class BenchmarkBBOB2010
{
	private static int instanceId = 1;
	private static JNIfgeneric fgeneric;

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

	public static double f1(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(1, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f2(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(2, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f3(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(3, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f4(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(4, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f5(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(5, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f6(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(6, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f7(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(7, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f8(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(8, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f9(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(9, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f10(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(10, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f11(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(11, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f12(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(12, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f13(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(13, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f14(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(14, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f15(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(15, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f16(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(16, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f17(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(17, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f18(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(18, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f19(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(19, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f20(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(20, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f21(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(21, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f22(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(22, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f23(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(23, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f24(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(24, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	/*******************************************************************************************/
	/**                                     NOISE FUNCTIONS                                   **/
	/*******************************************************************************************/
	
	public static double f101(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(101, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f102(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(102, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f103(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(103, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f104(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(104, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f105(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(105, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f106(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(106, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f107(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(107, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f108(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(108, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f109(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(109, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f110(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(110, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f111(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(111, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f112(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(112, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f113(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(113, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f114(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(114, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f115(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(115, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f116(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(116, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f117(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(117, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f118(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(118, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f119(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(119, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f120(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(120, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f121(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(121, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f122(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(122, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f123(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(123, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f124(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(124, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	public static double f125(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(125, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	public static double f126(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(126, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	public static double f127(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(127, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	public static double f128(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(128, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	public static double f129(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(129, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	public static double f130(double[] x)
	{
		if (fgeneric == null)
			initializeBBOB(130, BenchmarkBBOB2010.instanceId++, x.length);
		return fgeneric.evaluate(x);
	}
	
	public double f(double[] x, int fNum)
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
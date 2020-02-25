package benchmarks.problemsImplementation.CEC2010;



/**
 * Benchmark Functions for the IEEE CEC 2010 - 
 * Special Session andCompetition on Large Scale Global Optimization.
 */
public class BenchmarkCEC2010
{
	private static F1 f1;
	private static F2 f2;
	private static F3 f3;
	private static F4 f4;
	private static F5 f5;
	private static F6 f6;
	private static F7 f7;
	private static F8 f8;
	private static F9 f9;
	private static F10 f10;
	private static F11 f11;
	private static F12 f12;
	private static F13 f13;
	private static F14 f14;
	private static F15 f15;
	private static F16 f16;
	private static F17 f17;
	private static F18 f18;
	private static F19 f19;
	private static F20 f20;

	public static double f1(double[] x)
	{
		if (f1 == null)
			f1 = new F1();	
		return f1.compute(x);
	}

	public static double f2(double[] x)
	{
		if (f2 == null)
			f2 = new F2();
		return f2.compute(x);
	}

	public static double f3(double[] x)
	{
		if (f3 == null)
			f3 = new F3();
		return f3.compute(x);
	}

	public static double f4(double[] x)
	{
		if (f4 == null)
			f4 = new F4();
		return f4.compute(x);
	}

	public static double f5(double[] x)
	{
		if (f5 == null)
			f5 = new F5();	
		return f5.compute(x);
	}

	public static double f6(double[] x)
	{
		if (f6 == null)
			f6 = new F6();
		return f6.compute(x);
	}

	public static double f7(double[] x)
	{
		if (f7 == null)
			f7 = new F7();
		return f7.compute(x);
	}

	public static double f8(double[] x)
	{
		if (f8 == null)
			f8 = new F8();
		return f8.compute(x);
	}

	public static double f9(double[] x)
	{
		if (f9 == null)
			f9 = new F9();
		return f9.compute(x);
	}

	public static double f10(double[] x)
	{
		if (f10 == null)
			f10 = new F10();
		return f10.compute(x);
	}

	public static double f11(double[] x)
	{
		if (f11 == null)
			f11 = new F11();
		return f11.compute(x);
	}

	public static double f12(double[] x)
	{
		if (f12 == null)
			f12 = new F12();
		return f12.compute(x);
	}

	public static double f13(double[] x)
	{
		if (f13 == null)
			f13 = new F13();
		return f13.compute(x);
	}

	public static double f14(double[] x)
	{
		if (f14 == null)
			f14 = new F14();
		return f14.compute(x);
	}

	public static double f15(double[] x)
	{
		if (f15 == null)
			f15 = new F15();
		return f15.compute(x);
	}

	public static double f16(double[] x)
	{
		if (f16 == null)
			f16 = new F16();
		return f16.compute(x);
	}

	public static double f17(double[] x)
	{
		if (f17 == null)
			f17 = new F17();
		return f17.compute(x);
	}

	public static double f18(double[] x)
	{
		if (f18 == null)
			f18 = new F18();
		return f18.compute(x);
	}

	public static double f19(double[] x)
	{
		if (f19 == null)
			f19 = new F19();
		return f19.compute(x);
	}

	public static double f20(double[] x)
	{
		if (f20 == null)
			f20 = new F20();
		return f20.compute(x);
	}
	
	public static double[] getBounds(int fNum) {return BenchmarkCEC2010_C.getBounds(fNum);}
}
package benchmarks.problemsImplementation.CEC2010;



/**
 * Benchmark Functions for the IEEE CEC 2010 - 
 * Special Session andCompetition on Large Scale Global Optimization.
 */
public class BenchmarkCEC2010
{
	// The functions keep scratch vectors (e.g. ShiftedPermutatedRotatedFunction.m_tmp) that compute() overwrites, so one function
	// object must never be used by two threads at the same time: each thread gets its own instance (all instances are identical,
	// since they are built from the same seeded randomizer).
	private static final ThreadLocal<F1> f1 = ThreadLocal.withInitial(F1::new);
	private static final ThreadLocal<F2> f2 = ThreadLocal.withInitial(F2::new);
	private static final ThreadLocal<F3> f3 = ThreadLocal.withInitial(F3::new);
	private static final ThreadLocal<F4> f4 = ThreadLocal.withInitial(F4::new);
	private static final ThreadLocal<F5> f5 = ThreadLocal.withInitial(F5::new);
	private static final ThreadLocal<F6> f6 = ThreadLocal.withInitial(F6::new);
	private static final ThreadLocal<F7> f7 = ThreadLocal.withInitial(F7::new);
	private static final ThreadLocal<F8> f8 = ThreadLocal.withInitial(F8::new);
	private static final ThreadLocal<F9> f9 = ThreadLocal.withInitial(F9::new);
	private static final ThreadLocal<F10> f10 = ThreadLocal.withInitial(F10::new);
	private static final ThreadLocal<F11> f11 = ThreadLocal.withInitial(F11::new);
	private static final ThreadLocal<F12> f12 = ThreadLocal.withInitial(F12::new);
	private static final ThreadLocal<F13> f13 = ThreadLocal.withInitial(F13::new);
	private static final ThreadLocal<F14> f14 = ThreadLocal.withInitial(F14::new);
	private static final ThreadLocal<F15> f15 = ThreadLocal.withInitial(F15::new);
	private static final ThreadLocal<F16> f16 = ThreadLocal.withInitial(F16::new);
	private static final ThreadLocal<F17> f17 = ThreadLocal.withInitial(F17::new);
	private static final ThreadLocal<F18> f18 = ThreadLocal.withInitial(F18::new);
	private static final ThreadLocal<F19> f19 = ThreadLocal.withInitial(F19::new);
	private static final ThreadLocal<F20> f20 = ThreadLocal.withInitial(F20::new);

	public static double f1(double[] x)
	{
		return f1.get().compute(x);
	}

	public static double f2(double[] x)
	{
		return f2.get().compute(x);
	}

	public static double f3(double[] x)
	{
		return f3.get().compute(x);
	}

	public static double f4(double[] x)
	{
		return f4.get().compute(x);
	}

	public static double f5(double[] x)
	{
		return f5.get().compute(x);
	}

	public static double f6(double[] x)
	{
		return f6.get().compute(x);
	}

	public static double f7(double[] x)
	{
		return f7.get().compute(x);
	}

	public static double f8(double[] x)
	{
		return f8.get().compute(x);
	}

	public static double f9(double[] x)
	{
		return f9.get().compute(x);
	}

	public static double f10(double[] x)
	{
		return f10.get().compute(x);
	}

	public static double f11(double[] x)
	{
		return f11.get().compute(x);
	}

	public static double f12(double[] x)
	{
		return f12.get().compute(x);
	}

	public static double f13(double[] x)
	{
		return f13.get().compute(x);
	}

	public static double f14(double[] x)
	{
		return f14.get().compute(x);
	}

	public static double f15(double[] x)
	{
		return f15.get().compute(x);
	}

	public static double f16(double[] x)
	{
		return f16.get().compute(x);
	}

	public static double f17(double[] x)
	{
		return f17.get().compute(x);
	}

	public static double f18(double[] x)
	{
		return f18.get().compute(x);
	}

	public static double f19(double[] x)
	{
		return f19.get().compute(x);
	}

	public static double f20(double[] x)
	{
		return f20.get().compute(x);
	}
	
	public static double[] getBounds(int fNum) {return BenchmarkCEC2010_C.getBounds(fNum);}
}
package benchmarks;

import interfaces.Problem;

import benchmarks.problemsImplementation.BenchmarkSISC2010;

public class SISC2010 extends Problem 
{
	int problem;
	
	public SISC2010(int problem, int dim) {super(dim, BenchmarkSISC2010.getBounds(problem));}

	public double f(double[] x)
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
			default:
				return Double.NaN;
		}
	}
}
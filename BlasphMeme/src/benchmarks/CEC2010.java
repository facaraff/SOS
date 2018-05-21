package benchmarks;

import interfaces.Problem;

import benchmarks.problemsImplementation.CEC2010.BenchmarkCEC2010;

public class CEC2010 extends Problem 
{
	int problem;
	
	public CEC2010(int problem) {super(1000, BenchmarkCEC2010.getBounds(problem)); this.problem = problem;}
	public CEC2010(int problem, int dim) {this(problem); if (dim!=1000)System.out.println("The dimensionality of the problem was set up euqal to 1000");}

	public double f(double[] x)
	{
		switch (problem) {
			case 1:
				return BenchmarkCEC2010.f1(x);
			case 2:
				return BenchmarkCEC2010.f2(x);
			case 3:
				return BenchmarkCEC2010.f3(x);
			case 4:
				return BenchmarkCEC2010.f4(x);
			case 5:
				return BenchmarkCEC2010.f5(x);
			case 6:
				return BenchmarkCEC2010.f6(x);
			case 7:
				return BenchmarkCEC2010.f7(x);
			case 8:
				return BenchmarkCEC2010.f8(x);
			case 9:
				return BenchmarkCEC2010.f9(x);
			case 10:
				return BenchmarkCEC2010.f10(x);
			case 11:
				return BenchmarkCEC2010.f11(x);
			case 12:
				return BenchmarkCEC2010.f12(x);
			case 13:
				return BenchmarkCEC2010.f13(x);
			case 14:
				return BenchmarkCEC2010.f14(x);
			case 15:
				return BenchmarkCEC2010.f15(x);
			case 16:
				return BenchmarkCEC2010.f16(x);
			case 17:
				return BenchmarkCEC2010.f17(x);
			case 18:
				return BenchmarkCEC2010.f18(x);
			case 19:
				return BenchmarkCEC2010.f19(x);
			case 20:
				return BenchmarkCEC2010.f20(x);
			default:
				System.out.println("This function does not exit");
				return Double.NaN;
		}
	}
}
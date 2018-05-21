package benchmarks;

import interfaces.Problem;

import benchmarks.problemsImplementation.CEC2013_LSGO.BenchmarkCEC2013_LSGO;

public class CEC2013_LSGO extends Problem 
{
	int problem;
	
	public CEC2013_LSGO(int problem) {super(1000, BenchmarkCEC2013_LSGO.getBounds(problem)); this.problem = problem;}
	public CEC2013_LSGO(int problem, int dim) {this(problem); if (dim!=1000)System.out.println("The dimensionality of the problem was set up euqal to 1000");}

	public double f(double[] x)
	{
		switch (problem) {
			case 1:
				return BenchmarkCEC2013_LSGO.f1(x);
			case 2:
				return BenchmarkCEC2013_LSGO.f2(x);
			case 3:
				return BenchmarkCEC2013_LSGO.f3(x);
			case 4:
				return BenchmarkCEC2013_LSGO.f4(x);
			case 5:
				return BenchmarkCEC2013_LSGO.f5(x);
			case 6:
				return BenchmarkCEC2013_LSGO.f6(x);
			case 7:
				return BenchmarkCEC2013_LSGO.f7(x);
			case 8:
				return BenchmarkCEC2013_LSGO.f8(x);
			case 9:
				return BenchmarkCEC2013_LSGO.f9(x);
			case 10:
				return BenchmarkCEC2013_LSGO.f10(x);
			case 11:
				return BenchmarkCEC2013_LSGO.f11(x);
			case 12:
				return BenchmarkCEC2013_LSGO.f12(x);
			case 13:
				return BenchmarkCEC2013_LSGO.f13(x);
			case 14:
				return BenchmarkCEC2013_LSGO.f14(x);
			case 15:
				return BenchmarkCEC2013_LSGO.f15(x);
			default:
				return Double.NaN;
		}
	}
}
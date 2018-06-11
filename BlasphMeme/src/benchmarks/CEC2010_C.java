package benchmarks;

import interfaces.Problem;

import benchmarks.problemsImplementation.CEC2010.BenchmarkCEC2010_C;

public class CEC2010_C extends Problem 
{
	int problem;
	
	public CEC2010_C(int problem) {super(1000, BenchmarkCEC2010_C.getBounds(problem)); this.problem = problem; setFID(".f"+problem);}
	public CEC2010_C(int problem, int dim) {this(problem); if (dim!=1000)System.out.println("The dimensionality of the problem was set up euqal to 1000");}

	public double f(double[] x)
	{
		switch (problem) {
			case 1:
				return BenchmarkCEC2010_C.f1(x);
			case 2:
				return BenchmarkCEC2010_C.f2(x);
			case 3:
				return BenchmarkCEC2010_C.f3(x);
			case 4:
				return BenchmarkCEC2010_C.f4(x);
			case 5:
				return BenchmarkCEC2010_C.f5(x);
			case 6:
				return BenchmarkCEC2010_C.f6(x);
			case 7:
				return BenchmarkCEC2010_C.f7(x);
			case 8:
				return BenchmarkCEC2010_C.f8(x);
			case 9:
				return BenchmarkCEC2010_C.f9(x);
			case 10:
				return BenchmarkCEC2010_C.f10(x);
			case 11:
				return BenchmarkCEC2010_C.f11(x);
			case 12:
				return BenchmarkCEC2010_C.f12(x);
			case 13:
				return BenchmarkCEC2010_C.f13(x);
			case 14:
				return BenchmarkCEC2010_C.f14(x);
			case 15:
				return BenchmarkCEC2010_C.f15(x);
			default:
				return Double.NaN;
		}
	}
}
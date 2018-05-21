package benchmarks;

import interfaces.Problem;

//import benchmarks.problemsImplementation.BenchmarkSISC2010;
import darwin.BenchmarkSISC2010;

public class SISC2010 extends Problem 
{
	int problem;
	
	public SISC2010(int problem, int dim) {super(dim, BenchmarkSISC2010.getBounds(problem)); this.problem=problem;}
	
	public double f(double[] x) {return BenchmarkSISC2010.f(x, problem);}
}
package benchmarks;

import interfaces.Problem;

//import benchmarks.problemsImplementation.BenchmarkCEC2008;
import darwin.BenchmarkCEC2008;

public class CEC2008 extends Problem 
{
	int problem;
	
	public CEC2008(int problem) {super(1000, BenchmarkCEC2008.getBounds(problem)); this.problem = problem;}
	public CEC2008(int problem, int dim) {this(problem);}

	public double f(double[] x)
	{
		return BenchmarkCEC2008.f(problem,x);
	}
}
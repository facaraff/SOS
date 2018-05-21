package benchmarks;

import interfaces.Problem;

import benchmarks.problemsImplementation.CEC2013.BenchmarkCEC2013;

public class CEC2013_C extends Problem 
{
	int problem;
	
	public CEC2013_C(int problem, int dim) {super(dim, new double[] {-100,100});}

	public double f(double[] x){return BenchmarkCEC2013.f(problem,x);}
}
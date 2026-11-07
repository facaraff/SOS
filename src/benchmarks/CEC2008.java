package benchmarks;

import interfaces.Problem;

//import benchmarks.problemsImplementation.BenchmarkCEC2008;
import darwin.BenchmarkCEC2008;

public class CEC2008 extends Problem 
{
	int problem;
	
	public CEC2008(int problem) {super(1000, BenchmarkCEC2008.getBounds(problem)); this.problem = problem; setFID(".f"+problem);}
	public CEC2008(int problem, int dim) {this(problem);}

	// The static/native state behind this benchmark is not thread-safe: concurrent calls to f() return wrong values
	// (or even crash the JVM, for native code). In multi-thread experiments evaluations are therefore serialised.
	// See mains.test.TestMultiThreadProblems.
	private static final Object LOCK = new Object();

	public double f(double[] x)
	{
		synchronized(LOCK){return evaluate(x);}
	}

	private double evaluate(double[] x)
	{
		return BenchmarkCEC2008.f(problem,x);
	}
}
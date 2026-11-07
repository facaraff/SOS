package benchmarks;

import interfaces.Problem;


import benchmarks.problemsImplementation.COCO19.BBOB2018TestFunctions;



public class BBOB2018 extends Problem
{
	private BBOB2018TestFunctions bbob18;
	
	public BBOB2018(int problemNum, int dimension) throws Exception
	{
		super(dimension);
		bbob18 = new BBOB2018TestFunctions(problemNum,dimension); 
		setBounds(bbob18.getBounds());
		setFID(".f"+problemNum);
	}
	
	// The static/native state behind this benchmark is not thread-safe: concurrent calls to f() return wrong values
	// (or even crash the JVM, for native code). In multi-thread experiments evaluations are therefore serialised.
	// See mains.test.TestMultiThreadProblems.
	private static final Object LOCK = new Object();

	public double f(double[] x){synchronized(LOCK){return bbob18.evaluateFunction(x);}}
}
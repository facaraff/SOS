package benchmarks;

import interfaces.Problem;
import benchmarks.problemsImplementation.CEC2013LSGOTestFunc;

/**
 * Benchmark Functions for the IEEE CEC 2013 - Special Session 
 * and Competition on Large Scale Global Optimization.
 */
public class CEC2013LSGO extends Problem
{
	
	//private static JNIfgeneric2013 fgeneric;
	private CEC2013LSGOTestFunc testFunc;
		
	public CEC2013LSGO(int dimension, int problemNum) throws Exception
	{
		super(dimension, new double[] {-100, 100});  
		 setFID(".f"+problemNum);
			 
			testFunc = new CEC2013LSGOTestFunc(dimension,problemNum);
	}
		
	public double f(double[] x)
	{
		return testFunc.f(x);
	}
}
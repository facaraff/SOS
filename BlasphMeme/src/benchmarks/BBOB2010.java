package benchmarks;


import interfaces.Problem;
import benchmarks.problemsImplementation.BBOB2010.BBOB2010TestFunctions;


public class BBOB2010 extends Problem
{
	private int fNum;
	
	public BBOB2010(int dimension, int problemNum) throws Exception
	{
		 super(dimension, BBOB2010TestFunctions.getBounds(problemNum));  
		 setFID(".f"+problemNum);
		 fNum = problemNum;
	}
	
	public double f(double[] x)
	{
		return BBOB2010TestFunctions.f(x, fNum);
	}
}
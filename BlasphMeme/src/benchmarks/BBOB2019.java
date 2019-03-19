package benchmarks;

import interfaces.Problem;


import benchmarks.problemsImplementation.COCO19.BBOB2019TestFunctions;



public class BBOB2019 extends Problem
{
	private BBOB2019TestFunctions bbob18
	
	public BBOB2019(int dimension, int problemNum) throws Exception
	{
		 super(dimension, BBOB2019TestFunctions.getBounds(problemNum));  
		 setFID(".f"+problemNum);
		 fNum = problemNum;
	}
	
	public double f(double[] x)
	{

	}
		
}
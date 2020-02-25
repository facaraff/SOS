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
	
	public double f(double[] x){return bbob18.evaluateFunction(x);}
}
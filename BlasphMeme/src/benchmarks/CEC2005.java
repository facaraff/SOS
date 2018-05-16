package benchmarks;


//import benchmarks.problemsImplementation.CEC2005.Benchmark;
import benchmarks.problemsImplementation.CEC2005.CEC2005TestFunction;
import interfaces.Problem;


public class CEC2005 extends Problem
{
	private CEC2005TestFunction cec2005 = null;
	
	 
	public CEC2005(int dimension, int problemNum) throws Exception
	{
		super(dimension);  
		setFID(".f"+problemNum);
		cec2005 = cec2005.initiliaseFunction(problemNum,dimension);			
	}
	
	
	/**
	 * This method returns the fitness function value.
	 */
	public double f(double[] x){return cec2005.f(x);}
	
}




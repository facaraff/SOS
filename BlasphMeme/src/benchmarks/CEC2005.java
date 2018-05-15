package benchmarks;


import benchmarks.problemsImplementation.cec2005.Benchmark;
import benchmarks.problemsImplementation.cec2005.TestFunction;
import interfaces.Problem;


public class CEC2005 extends Problem
{
	private TestFunction testFunction;
	
	 
	public CEC2005(int dimension, int problemNum) throws Exception
	{
		 super(dimension);  
		setFID(".f"+problemNum);
		setBounds(getBoundaries(problemNum));
		Benchmark benchmark = new Benchmark();
		testFunction = benchmark.testFunctionFactory(problemNum, dimension);
		benchmark = null;				
	}
	
	
	/**
	 * This method returns the fitness function value.
	 */
	public double f(double[] x){return testFunction.f(x);}
	
	
	/**
	 * This method returns appropriate boundaries of a specific problem of the CEC2005 benchmarks suite.
	 */
	private double[] getBoundaries(int problemNum)
	{
		 double[] bounds=null;
			if ((problemNum <= 6) || (problemNum == 14))
				bounds = new double[] {-100, 100};
			else if (problemNum == 7)
				bounds = new double[] {0, 600};
			else if (problemNum == 8)
				bounds = new double[] {-32, 32};
			else if (problemNum == 11)
				bounds = new double[] {-0.5, 0.5};
			else if (problemNum == 12)
				bounds = new double[] {-Math.PI, Math.PI};
			else if (problemNum == 13)
				bounds = new double[] {-5, 5}; //XXX {-3,1} in Suganthan's code, {-5,5} in report
			else if (problemNum == 25)
				bounds = new double[] {2, 5};
			else
				bounds = new double[] {-5, 5};
		return bounds;
	}
	
	
}




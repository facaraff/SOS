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



double[] bounds;
if ((i <= 6) || (i == 14))
	bounds = new double[] {-100, 100};

else if (i == 11)
	bounds = new double[] {-0.5, 0.5};
else if (i == 12)
	bounds = new double[] {-Math.PI, Math.PI};
else if (i == 13)
	bounds = new double[] {-5, 5}; //XXX (gio) {-3,1} in Suganthan's code, {-5,5} in report
else if (i == 25)
	bounds = new double[] {2, 5};
else
	bounds = new double[] {-5, 5};
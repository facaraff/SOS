package benchmarks.problemsImplementation.CEC2013;


public class F13 extends AbstractCEC2013
{
	protected final double bias = -200.0;
	
	public F13 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.step_rastrigin_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

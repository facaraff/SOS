package benchmarks.problemsImplementation.CEC2013;


public class F6 extends AbstractCEC2013
{
	protected final double bias = -900.0;
	
	public F6 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.rosenbrock_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

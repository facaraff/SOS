package benchmarks.problemsImplementation.CEC2013;


public class F10 extends AbstractCEC2013
{
	protected final double bias = -500.0;
	
	public F10 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.griewank_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

package benchmarks.problemsImplementation.CEC2013;


public class F11 extends AbstractCEC2013
{
	protected final double bias = -400.0;
	
	public F11 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.rastrigin_func(x,nx,this.OShift,this.M,0)+this.bias;}
}

package benchmarks.problemsImplementation.CEC2013;


public class F9 extends AbstractCEC2013
{
	protected final double bias = -600.0;
	
	public F9 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.weierstrass_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

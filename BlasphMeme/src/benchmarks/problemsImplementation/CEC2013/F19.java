package benchmarks.problemsImplementation.CEC2013;


public class F19 extends AbstractCEC2013
{
	protected final double bias = 500.0;
	
	public F19 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.grie_rosen_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

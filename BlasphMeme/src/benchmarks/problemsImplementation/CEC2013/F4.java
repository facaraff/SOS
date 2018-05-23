package benchmarks.problemsImplementation.CEC2013;


public class F4 extends AbstractCEC2013
{
	protected final double bias = -1100.0;
	
	public F4 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.discus_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

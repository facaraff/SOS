package benchmarks.problemsImplementation.CEC2013;


public class F14 extends AbstractCEC2013
{
	protected final double bias = -100.0;
	
	public F14 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.schwefel_func(x,nx,this.OShift,this.M,0)+this.bias;}
}

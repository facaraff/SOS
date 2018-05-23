package benchmarks.problemsImplementation.CEC2013;


public class F26 extends AbstractCEC2013
{
	protected final double bias = 1200.0;
	
	public F26 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.cf06(x,nx,this.OShift,this.M,1)+this.bias;}
}

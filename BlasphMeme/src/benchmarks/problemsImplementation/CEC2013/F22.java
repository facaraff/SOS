package benchmarks.problemsImplementation.CEC2013;


public class F22 extends AbstractCEC2013
{
	protected final double bias = 800.0;
	
	public F22 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.cf02(x,nx,this.OShift,this.M,0)+this.bias;}
}

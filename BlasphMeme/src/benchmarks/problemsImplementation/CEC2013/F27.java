package benchmarks.problemsImplementation.CEC2013;


public class F27 extends AbstractCEC2013
{
	protected final double bias = 1300.0;
	
	public F27 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.cf07(x,nx,this.OShift,this.M,1)+this.bias;}
}

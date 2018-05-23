package benchmarks.problemsImplementation.CEC2013;


public class F24 extends AbstractCEC2013
{
	protected final double bias = 1000.0;
	
	public F24 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.cf04(x,nx,this.OShift,this.M,1)+this.bias;}
}

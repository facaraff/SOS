package benchmarks.problemsImplementation.CEC2013;


public class F21 extends AbstractCEC2013
{
	protected final double bias = 700.0;
	
	public F21 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.cf01(x,nx,this.OShift,this.M,1)+this.bias;}
}

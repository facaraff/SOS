package benchmarks.problemsImplementation.CEC2013;


public class F16 extends AbstractCEC2013
{
	protected final double bias = 200.0;
	
	public F16 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.katsuura_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

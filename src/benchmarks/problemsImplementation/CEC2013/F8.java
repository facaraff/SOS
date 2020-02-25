package benchmarks.problemsImplementation.CEC2013;


public class F8 extends AbstractCEC2013
{
	protected  final double bias = -700.0;
	
	public F8 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.ackley_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

package benchmarks.problemsImplementation.CEC2013;


public class F12 extends AbstractCEC2013
{
	protected final double bias = -300.0;
	
	public F12 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.rastrigin_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

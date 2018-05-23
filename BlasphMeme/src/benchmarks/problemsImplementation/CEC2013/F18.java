package benchmarks.problemsImplementation.CEC2013;


public class F18 extends AbstractCEC2013
{
	protected final double bias = 400.0;
	
	public F18 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.bi_rastrigin_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

package benchmarks.problemsImplementation.CEC2013;


public class F17 extends AbstractCEC2013
{
	protected final double bias = 300.0;
	
	public F17 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.bi_rastrigin_func(x,nx,this.OShift,this.M,0)+this.bias;}
}

package benchmarks.problemsImplementation.CEC2013;


public class F5 extends AbstractCEC2013
{
	protected final double bias = -1000.0;
	
	public F5 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.dif_powers_func(x,nx,this.OShift,this.M,0)+this.bias;}
}

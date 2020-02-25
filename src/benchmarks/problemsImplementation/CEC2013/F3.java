package benchmarks.problemsImplementation.CEC2013;


public class F3 extends AbstractCEC2013
{
	protected final double bias = -1200.0;
	
	public F3 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.bent_cigar_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

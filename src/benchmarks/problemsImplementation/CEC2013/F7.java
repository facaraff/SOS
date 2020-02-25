package benchmarks.problemsImplementation.CEC2013;


public class F7 extends AbstractCEC2013
{
	protected  final double bias = -800;
	
	public F7 (int dimension) {super(dimension);}

	// Function body
	public double f(double[] x) {return CEC2013TestFunctions.schaffer_F7_func(x,nx,this.OShift,this.M,1)+this.bias;}
}

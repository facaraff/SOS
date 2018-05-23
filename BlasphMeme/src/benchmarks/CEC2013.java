package benchmarks;

import interfaces.Problem;

import benchmarks.problemsImplementation.CEC2013.AbstractCEC2013;
import static benchmarks.problemsImplementation.CEC2013.CEC2013TestFunctions.initFunction;



public class CEC2013 extends Problem
{
	private AbstractCEC2013 cec13 = null;
	
	public CEC2013(int problem, int dim) {super(dim, new double[] {-100,100}); this.cec13=initFunction(problem,dim);}

	public double f(double[] x){return cec13.f(x);}
}
package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
import interfaces.Problem;
import algorithms.CMAES;
import algorithms.singleSolution.ISPO;
import benchmarks.BaseFunctions.Alpine; 
import benchmarks.BaseFunctions.Ackley;
import benchmarks.BaseFunctions.Rosenbrock;

public class Test extends Experiment
{
	
	public Test(int probDim)
	{
		super(probDim,"TEST");
			
		Algorithm a;// ///< A generic optimiser.
	    Problem p;// ///< A generic problem.

		a = new ISPO();
		a.setParameter("p0", 1.0);
		a.setParameter("p1", 10.0);
		a.setParameter("p2", 2.0);
		a.setParameter("p3", 4.0);
		a.setParameter("p4", 1e-5);
		a.setParameter("p5", 30.0);
		add(a); //add it to the list

		a = new CMAES(); //N.B. this algorithm makes use of "generateRandomSolution" that has still to be implemented.
		add(a);
		
		
		
        //double[] bounds = {-100, 100};
		//p = new Ackley(problemDimension, bounds);
		p = new Ackley(probDim);
		add(p);//add it to the list
		p = new Alpine(probDim);
		add(p);
		p = new Rosenbrock(probDim);
		add(p);

	}
}

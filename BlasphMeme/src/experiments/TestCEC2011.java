package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
import interfaces.Problem;
import algorithms.CMAES;
import algorithms.singleSolution.ISPO;
import applications.CEC2011.P1;


public class TestCEC2011 extends Experiment
{
	
	public TestCEC2011()
	{
		super("TESTCEC2011");
			
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
		p = new P1();
		add(p);//add it to the list


	}
}

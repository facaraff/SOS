package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014;
//import algorithms.CMAES;
//import algorithms.ISPO; 
import algorithms.Powell;
import algorithms.Powell_toro;
import algorithms.Rosenbrock;


public class CEC14 extends Experiment
{
	
	public CEC14(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14");
		setNrRuns(10);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		a = new Powell();
		a.setParameter("p0", 0.0001);
		a.setParameter("p1", 100.0);
		add(a);
		
		a = new Powell_toro();
		a.setParameter("p0", 0.0001);
		a.setParameter("p1", 100.0);
		add(a);
		
		a = new Rosenbrock();
		a.setParameter("p0", 10e-5);
		a.setParameter("p1", 2.0);
		a.setParameter("p2", 0.5);
		add(a);
		
//		a = new CMAES(); //N.B. this algorithm makes use of "generateRandomSolution" that has still to be implemented.
//		add(a);

//		a = new ISPO();
//		a.setParameter("p0", 1.0);
//		a.setParameter("p1", 10.0);
//		a.setParameter("p2", 2.0);
//		a.setParameter("p3", 4.0);
//		a.setParameter("p4", 1e-5);
//		a.setParameter("p5", 30.0);
//		add(a);
	
		for(int i = 1; i<=30; i++)
			add(new CEC2014(probDim, i));




	}
}

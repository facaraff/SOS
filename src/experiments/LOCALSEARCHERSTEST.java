package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
//import interfaces.Problem;
import algorithms.singleSolution.*;

//import benchmarks.BaseFunctions.Alpine;
import benchmarks.CEC2014;
//import benchmarks.BaseFunctions.Ackley;
//import benchmarks.BaseFunctions.Rosenbrock;

public class LOCALSEARCHERSTEST extends Experiment
{
	
	public LOCALSEARCHERSTEST(int probDim) throws Exception
	{
		//super(probDim,"DESIGN");
		super(probDim,5000,"DESIGN");
		setNrRuns(30);
		
		Algorithm a;// ///< A generic optimiser.
//	    Problem p;// ///< A generic problem.


	    a = new S();
	    a.setParameter("p0",150.0);
	    a.setParameter("p1", 0.4);
	    add(a);
	
	    
	    a = new Rosenbrock();
	    a.setParameter("p0",10e-5);
	    a.setParameter("p1", 2.0);
	    a.setParameter("p2", 0.5);
	    add(a);
	    
	    
	    a = new SPSA();
	    a.setParameter("p0",0.5);
	    a.setParameter("p1", 1.0);
	    a.setParameter("p2", 0.602);
	    a.setParameter("p3", 0.032);
	    a.setParameter("p4", 0.1);
	    add(a);
		
	    
	    a = new VISPO();
	    a.setParameter("p0",10.0);
	    a.setParameter("p1", 30.0);
	    a.setParameter("p2", 0.65);
	    add(a);

	    a = new NonUniformSA();
	    a.setParameter("p0",5.0);
	    a.setParameter("p1", 0.9);
	    a.setParameter("p2", 3.0);
	    a.setParameter("p3", 10.0);
	    add(a);
	    

		a = new ISPO();
		a.setParameter("p0", 1.0);
		a.setParameter("p1", 10.0);
		a.setParameter("p2", 2.0);
		a.setParameter("p3", 4.0);
		a.setParameter("p4", 1e-5);
		a.setParameter("p5", 30.0);
		add(a); //add it to the list

		for(int i = 1; i<=30; i++)
			add(new CEC2014(probDim, i));

	}
}

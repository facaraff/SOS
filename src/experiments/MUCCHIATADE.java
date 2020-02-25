package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
import algorithms.*;
//import interfaces.Problem;
//import benchmarks.BaseFunctions.Alpine;
import benchmarks.CEC2014;
//import benchmarks.BaseFunctions.Ackley;
//import benchmarks.BaseFunctions.Rosenbrock;

public class MUCCHIATADE extends Experiment
{
	
	public MUCCHIATADE(int probDim) throws Exception
	{
		//super(probDim,"DESIGN");
		super(probDim,5000,"DESIGN");
		setNrRuns(30);
		
		Algorithm a;// ///< A generic optimiser.
//	    Problem p;// ///< A generic problem.

	    a = new jDE();
	    a.setParameter("p0",50.0);
	    a.setParameter("p1", 0.1);
	    a.setParameter("p2", 1.0);
	    a.setParameter("p3", 0.1);
	    a.setParameter("p4", 0.1);
	    add(a);
	
	    
	    a = new JADE();
	    a.setParameter("p0",60.0);
	    a.setParameter("p1", 0.05);
	    a.setParameter("p2", 0.1);
	    add(a);
	    
	    a = new SADE();
	    a.setParameter("p0",50.0);
	    a.setParameter("p1", 4.0);
	    a.setParameter("p2", 20.0);
	    add(a);
	    
	    a = new CLPSO();
	    a.setParameter("p0",60.0);
	    add(a);
	    
	    a = new CCPSO2();
	    a.setParameter("p0",30.0);
	    a.setParameter("p1",0.5);
	    add(a);

		for(int i = 1; i<=30; i++)
			add(new CEC2014(probDim, i));

	}
}

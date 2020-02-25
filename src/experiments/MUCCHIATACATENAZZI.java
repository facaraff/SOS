package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
//import interfaces.Problem;
import algorithms.*;
//import benchmarks.BaseFunctions.Alpine;
import benchmarks.CEC2014;
//import benchmarks.BaseFunctions.Ackley;
//import benchmarks.BaseFunctions.Rosenbrock;

public class MUCCHIATACATENAZZI extends Experiment
{
	
	public MUCCHIATACATENAZZI(int probDim) throws Exception
	{
		//super(probDim,"DESIGN");
		super(probDim,5000,"DESIGN");
		setNrRuns(30);
		
		Algorithm a;// ///< A generic optimiser.
//	    Problem p;// ///< A generic problem.

	    a = new MS_CAP();
	    a.setParameter("p0",50.0);
	    a.setParameter("p1", 1e-6);
	    a.setParameter("p2", 3.0);
	    add(a);
	    
	    a = new MADE();
	    a.setParameter("p0",50.0);
	    a.setParameter("p1", 50.0);
	    a.setParameter("p2", 0.5);
	    a.setParameter("p3", 0.7);
	    a.setParameter("p4", 0.02);
	    a.setParameter("p5", 1.0);
	    add(a);
	    
	    a = new EPSDE_LS();
	    a.setID("EPSDE");
	    a.setParameter("p0",50.0);
	    a.setParameter("p1", 200.0);
	    a.setParameter("p2", 1000.0);
	    a.setParameter("p3", 0.0);
	    add(a);
	    
	    a = new EPSDE_LS();
	    a.setParameter("p0",50.0);
	    a.setParameter("p1", 200.0);
	    a.setParameter("p2", 1000.0);
	    a.setParameter("p3", 1.0);
	    add(a);
	    
	    a = new MicroDEA();
		a.setParameter("p0", 5.0);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		a.setParameter("p5", 0.3);//ALPHA
	    add(a);
	    
	    a = new MDE_pBX();
		a.setParameter("p0", 100.0);
		a.setParameter("p1", 0.15); 
		a.setParameter("p2", 0.6);
		a.setParameter("p3", 0.5);
		a.setParameter("p4", 1.5);
	    add(a);
	    

		for(int i = 1; i<=30; i++)
			add(new CEC2014(probDim, i));

	}
}

package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
//import interfaces.Problem;
import algorithms.singleSolution.*;
import algorithms.compact.*;
//import algorithms.inProgress.ResampledCMAES11;
//import algorithms.inProgress.ResampledBrentCMAES11;
//import benchmarks.BaseFunctions.Alpine;
import benchmarks.CEC2014;
//import benchmarks.BaseFunctions.Ackley;
//import benchmarks.BaseFunctions.Rosenbrock;

public class extendedtoro extends Experiment
{
	
	public extendedtoro(int probDim) throws Exception
	{
		//super(probDim,"DESIGN");
		super(probDim,5000,"DESIGN");
		setNrRuns(30);
		
//		//questo e' stato quello uufficializzato
//		Algorithm a;// ///< A generic optimiser.
////	    Problem p;// ///< A generic problem.
//		a = new ResampledCMAES11();
//	    a.setParameter("p0",0.95); 
//	    a.setParameter("p1",0.18181818181); // 2/11
//	    a.setParameter("p2", 0.08333333333);// 1/12
//	    a.setParameter("p3", 0.44);
//	    a.setParameter("p4", 1.0);// 1 --> problem dependent!!
//	    a.setParameter("p5", 0.20);
//	    add(a);
	    

		a = new Powell();
		a.setParameter("p0", 0.0001);
		a.setParameter("p1", 100.0);
		add(a);
		
	    
	    a = new cDE_exp_light();
	    a.setParameter("p0",300.0);
	    a.setParameter("p1", 0.25);
	    a.setParameter("p2", 0.5);
	    a.setParameter("p3", 3.0);
	    a.setParameter("p4", 1.0);
	    a.setParameter("p5", 1.0);
	    add(a);
	
		for(int i = 1; i<=30; i++)
			add(new CEC2014(probDim, i));

	}
}

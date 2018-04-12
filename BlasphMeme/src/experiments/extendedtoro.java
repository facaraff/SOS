package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
//import interfaces.Problem;
//import algorithms.singleSolution.*;
import algorithms.inProgress.ResampledCMAES11;
import algorithms.inProgress.ResampledBrentCMAES11;
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
		
		Algorithm a;// ///< A generic optimiser.
//	    Problem p;// ///< A generic problem.
		a = new ResampledCMAES11();
	    a.setParameter("p0",0.95); 
	    a.setParameter("p1",0.18181818181); // 2/11
	    a.setParameter("p2", 0.08333333333);// 1/12
	    a.setParameter("p3", 0.44);
	    a.setParameter("p4", 1.0);// 1 --> problem dependent!!
	    a.setParameter("p5", 0.20);
	    add(a);
	    
		a = new ResampledCMAES11();
	    a.setParameter("p0",0.95); 
	    a.setParameter("p1",0.18181818181); // 2/11
	    a.setParameter("p2", 0.08333333333);// 1/12
	    a.setParameter("p3", 0.44);
	    a.setParameter("p4", 1.0);// 1 --> problem dependent!!
	    a.setParameter("p5", 0.25);
	    add(a);
	    
	    a = new ResampledBrentCMAES11();
	    a.setParameter("p0", 0.95);
	    a.setParameter("p1", 0.18181818181);
	    a.setParameter("p2", 0.08333333333);
	    a.setParameter("p3", 0.44);
	    a.setParameter("p4", 1.0);
	    a.setParameter("p5", Double.NaN);	    
	    a.setParameter("p6", 0.25);
	    add(a);

	    a = new ResampledBrentCMAES11();
	    a.setParameter("p0", 0.95);
	    a.setParameter("p1", 0.18181818181);
	    a.setParameter("p2", 0.08333333333);
	    a.setParameter("p3", 0.44);
	    a.setParameter("p4", 1.0);
	    a.setParameter("p5", 10.0);	    
	    a.setParameter("p6", 0.25);
	    add(a);

		
		
		
//	    a = new RI_CMAES_11();
//	    a.setID("RICAMES11_5");
//	    a.setParameter("p0",0.5); // 2/11
//	    a.setParameter("p1",0.18181818181); // 2/11
//	    a.setParameter("p2", 0.08333333333);// 1/12
//	    a.setParameter("p3", 0.44);
//	    a.setParameter("p4", 1.0);// 1 --> problem dependent!!
//	    a.setParameter("p5", 5.0);
//	    a.setParameter("p6", 0.25);// 1 --> problem dependent!!
//	    add(a);
//	    
//	    a = new RI_CMAES_11();
//	    a.setID("RICAMES11_10");
//	    a.setParameter("p0",0.5); // 2/11
//	    a.setParameter("p1",0.18181818181); // 2/11
//	    a.setParameter("p2", 0.08333333333);// 1/12
//	    a.setParameter("p3", 0.44);
//	    a.setParameter("p4", 1.0);// 1 --> problem dependent!!
//	    a.setParameter("p5", 10.0);//fails
//	    a.setParameter("p6", 0.25);// 1 --> problem dependent!!
//	    add(a);
//	    
//	    a = new RI_CMAES_11();
//	    a.setID("RICAMES11_20");
//	    a.setParameter("p0",0.5); // 2/11
//	    a.setParameter("p1",0.18181818181); // 2/11
//	    a.setParameter("p2", 0.08333333333);// 1/12
//	    a.setParameter("p3", 0.44);
//	    a.setParameter("p4", 1.0);// 1 --> problem dependent!!
//	    a.setParameter("p5", Double.NaN);//fails
//	    a.setParameter("p6", 0.25);// 1 --> problem dependent!!
//	    add(a);

		for(int i = 1; i<=30; i++)
			add(new CEC2014(probDim, i));

	}
}

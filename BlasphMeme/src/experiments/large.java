package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
import algorithms.singleSolution.SPAMAOS2;
import benchmarks.CEC2013_LSGO;


public class large extends Experiment
{
	
	public large() throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(1000,5000,"CEC2013_LSGO_SMAPAOS");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
	    a = new SPAMAOS2();
			a.setParameter("p0",0.5);
			a.setParameter("p1", 0.4); 
			a.setParameter("p2",150.0);
			a.setParameter("p3",2.0);
			a.setParameter("p4",0.5);
			a.setParameter("p5",0.00001);
			a.setParameter("p6",3.0); //{1,2,3,4}
			add(a);
		    
		
//		a = new Powell();
//		a.setParameter("p0", 0.0001);
//		a.setParameter("p1", 100.0);
//		add(a);
//		
//		a = new Powell_toro();
//		a.setParameter("p0", 0.0001);
//		a.setParameter("p1", 100.0);
//		add(a);
//		
//		a = new Rosenbrock();
//		a.setParameter("p0", 10e-5);
//		a.setParameter("p1", 2.0);
//		a.setParameter("p2", 0.5);
//		add(a);
		
	
		for(int i = 1; i<=15; i++)
			add(new CEC2013_LSGO(i));




	}
}

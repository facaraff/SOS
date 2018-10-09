package experiments.paperReviews;

import interfaces.Experiment;
import interfaces.Algorithm;
//import interfaces.Problem;
import algorithms.singleSolution.ISPO;
import benchmarks.CEC2013;


public class Cec2013 extends Experiment
{
	
	public Cec2013(int probDim) throws Exception
	{
		super(probDim,5,"DESIGN");
		setNrRuns(10);
		
		Algorithm a;// ///< A generic optimiser.

	    
		a = new ISPO();
		a.setParameter("p0", 1.0);
		a.setParameter("p1", 10.0);
		a.setParameter("p2", 2.0);
		a.setParameter("p3", 4.0);
		a.setParameter("p4", 1e-5);
		a.setParameter("p5", 30.0);
		add(a); //add it to the list

		a = new ISPO();
		a.setParameter("p0", 1.0);
		a.setParameter("p1", 10.0);
		a.setParameter("p2", 2.0);
		a.setParameter("p3", 4.0);
		a.setParameter("p4", 1e-5);
		a.setParameter("p5", 30.0);
		add(a); //add it to the list	
		
   


		for(int i = 1; i<=28; i++)
			add(new CEC2013(i,probDim));			

	}
}

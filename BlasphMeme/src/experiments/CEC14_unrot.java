package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.DE;



public class CEC14_unrot extends Experiment
{
	
	public CEC14_unrot(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14");
		setNrRuns(10);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		a = new DE();
		a.setID("wrDEctr1");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", Double.NaN);
		a.setParameter("p3", 4.0);
		a.setParameter("p4", Double.NaN);
		add(a);
		
		a = new DE();
		a.setID("wrDEr1bin");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.3);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 1.0);
		add(a);
		
		a = new DE();
		a.setID("wrDEr1exp");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.3);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		add(a);

	
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i, 0));




	}
}

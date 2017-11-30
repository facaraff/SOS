package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.RIDE;
import algorithms.MMCDE;



public class CEC14_RIDE_unrot extends Experiment
{
	
	public CEC14_RIDE_unrot(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14RIDE");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		
		
		a = new MMCDE();
		a.setID("rMMCDE");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.4); //F
		a.setParameter("p2", 0.3); //CR
		add(a);
		
		a = new RIDE();
		a.setID("rRIDErand1bin");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.4); //F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 4.0);
		add(a);
		
		a = new RIDE();
		a.setID("rRIDErand1exp");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.4); //F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 5.0);
		add(a);
		
		
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i, 0));




	}
}

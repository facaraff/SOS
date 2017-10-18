package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.DE;



public class CEC14_rot_EXP_TUNING extends Experiment
{
	
	public CEC14_rot_EXP_TUNING(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		

		a = new DE();
		a.setID("rDEr1exp01");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.1);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		add(a);

		//DATA with CR=0.3 already produced for conference papar		
//		a = new DE();
//		a.setID("rDEr1exp03");
//		a.setParameter("p0", 10.0);
//		a.setParameter("p1", 0.4);
//		a.setParameter("p2", 0.3);
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 2.0);
//		add(a);
		
		
		a = new DE();
		a.setID("rDEr1exp05");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.5);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		add(a);
		
		a = new DE();
		a.setID("rDEr1exp07");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.7);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		add(a);
		
		
		a = new DE();
		a.setID("rDEr1exp09");
		a.setParameter("p0", 10.0);
		a.setParameter("p1", 0.4);
		a.setParameter("p2", 0.9);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		add(a);
		
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i));




	}
}

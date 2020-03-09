package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.RCEC2014;
import algorithms.EigenDE;




public class Unrot_eigen extends Experiment
{
	
	public Unrot_eigen(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC1Eigen");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
//		a = new EigenDE();
//		a.setID("wrEigenDEr1bin");
//		a.setParameter("p0", (double)probDim);
//		a.setParameter("p1", 0.7);//F
//		a.setParameter("p2", 0.3); //CR
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 1.0);
//		a.setParameter("p5", Double.NaN);//ALPHA
//		a.setParameter("p6", 1.0);//PR
//		add(a);
		
		a = new EigenDE();
		a.setID("wrEigenDEr1exp");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		a.setParameter("p5", 0.3);//ALPHA
		a.setParameter("p6", 1.0);//PR
		add(a);
		
//		if(probDim==50)
//			for(int i = 3; i<=30; i++)
//				add(new CEC2014RotInvStudy(probDim, i, 0));
//		else
//			for(int i = 1; i<=30; i++)
//				add(new CEC2014RotInvStudy(probDim, i, 0));
//			


				add(new RCEC2014(50, 2, 0));




	}
}

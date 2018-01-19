package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
//import algorithms.RIDE;
//import algorithms.DE;
//import algorithms.MMCDE;
import algorithms.EigenDE;



public class RotEigen5050 extends Experiment
{
	
	public RotEigen5050(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14Eigen");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		
		a = new EigenDE();
		a.setID("rEigenDEr1bin");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 1.0);
		a.setParameter("p5", Double.NaN);//ALPHA
		a.setParameter("p6", 0.5);//PR
		add(a);
		
		a = new EigenDE();
		a.setID("rEigenDEr1exp");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		a.setParameter("p5", 0.3);//ALPHA
		a.setParameter("p6", 0.5);//PR
		add(a);
		
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i));




	}
}

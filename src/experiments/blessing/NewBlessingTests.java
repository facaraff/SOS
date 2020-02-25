package experiments.blessing;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.blessing.*;



public class NewBlessingTests extends Experiment
{
	
	public NewBlessingTests(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,1000,"rotated_final");
		setNrRuns(1);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
//		a = new CMAES();
//		a.setID("rotation");//N.B. this algorithm makes use of "generateRandomSolution" that has still to be implemented.
//		add(a);
		
		a = new PEARSON_DE();
		a.setID("rDEr1exp_COV");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		
		add(new CEC2014RotInvStudy(probDim, 1));
		add(new CEC2014RotInvStudy(probDim, 1,0));

		
//		for(int i = 1; i<=30; i++)
//			add(new CEC2014RotInvStudy(probDim, i));
//


	}
}

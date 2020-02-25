package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.RIDE;
import algorithms.MMCDE;



public class RIDEbenchirideultimoNON extends Experiment
{
	
	public RIDEbenchirideultimoNON(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"rotated_final");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
//		a = new CMAES();
//		a.setID("rotation");//N.B. this algorithm makes use of "generateRandomSolution" that has still to be implemented.
//		add(a);
		
		a = new MMCDE();
		a.setID("rMMCDE");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.4); //F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 0.3);//ALPHA
		add(a);
		
		a = new RIDE();
		a.setID("rRIDErand1bin");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.4); //F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 4.0);
		a.setParameter("p5", Double.NaN);
		add(a);
		
		a = new RIDE();
		a.setID("rRIDErand1exp");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.4); //F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 5.0);
		a.setParameter("p5", 0.3);
		add(a);
		
		
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i,0));




	}
}

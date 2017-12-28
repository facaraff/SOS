package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.RIDE;
import algorithms.DE;
import algorithms.MMCDE;
import algorithms.EigenDE;



public class Rot_eigen extends Experiment
{
	
	public Rot_eigen(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14RIDE");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		

//		a = new DE();
//		a.setID("rDEr1bin");
//		a.setParameter("p0", (double)probDim);
//		a.setParameter("p1", 0.7);//F
//		a.setParameter("p2", -1.0); //CR
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 1.0);
//		a.setParameter("p5", 0.3);//ALPHA
//		add(a);
//		
//		a = new DE();
//		a.setID("rDEr1exp");
//		a.setParameter("p0", (double)probDim);
//		a.setParameter("p1", 0.7);//F
//		a.setParameter("p2", -1.0); //CR
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 2.0);
//		a.setParameter("p5", 0.3);//ALPHA
//		add(a);
//		
//		a = new DE();
//		a.setID("rDEctr1");
//		a.setParameter("p0", (double)probDim);
//		a.setParameter("p1", 0.7); //F
//		a.setParameter("p2", Double.NaN);
//		a.setParameter("p3", 4.0);
//		a.setParameter("p4", Double.NaN);
//		a.setParameter("p5", Double.NaN);//ALPHA
//		add(a);
//		
//		
//		a = new MMCDE();
//		a.setID("rMMCDE");
//		a.setParameter("p0", (double)probDim);
//		a.setParameter("p1", 0.7); //F
//		a.setParameter("p2", -1.0); //CR
//		a.setParameter("p3", 0.3);//ALPHA
//		add(a);
//		
//		a = new RIDE();
//		a.setID("rRIDErand1bin");
//		a.setParameter("p0", (double)probDim);
//		a.setParameter("p1", 0.7); //F
//		a.setParameter("p2", -1.0); //CR
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 4.0);
//		a.setParameter("p5", 0.3);//ALPHA
//		add(a);
//		
//		a = new RIDE();
//		a.setID("rRIDErand1exp");
//		a.setParameter("p0", (double)probDim);
//		a.setParameter("p1", 0.7); //F
//		a.setParameter("p2", -1.0); //CR
//		a.setParameter("p3", 1.0);
//		a.setParameter("p4", 5.0);
//		a.setParameter("p5", 0.3);//ALPHA
//		add(a);
		
		a = new DE();
		a.setID("rEigenDEr1bin");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 1.0);
		a.setParameter("p5", 0.3);//ALPHA
		a.setParameter("p5", 1.0);//PR
		add(a);
		
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i));




	}
}

package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.DE;
import algorithms.EigenDE;
import algorithms.EigenDE2;



public class senzaXOr extends Experiment
{
	
	public senzaXOr(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14RIDE");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
//		a = new CMAES();
//		a.setID("rotation");//N.B. this algorithm makes use of "generateRandomSolution" that has still to be implemented.
//		add(a);
		
	
		
		a = new DE();
		a.setID("DEr1exp");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new EigenDE2();
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p2", Double.NaN); //CR
		a.setParameter("p3", 1.0);//mutation strategy
		a.setParameter("p4", 2.0);//crossover strategy
		a.setParameter("p5", 0.3);//ALPHA
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
		
		a = new DE();
		a.setID("rDEr1noxo");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
//		a.setParameter("p2", -1.0); //CR
		a.setParameter("p2", Double.NaN); //CR
		a.setParameter("p3", 1.0);//mutation strategy
		a.setParameter("p4", 0.0);//crossover strategy
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i));




	}
}

package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.DE;



public class senzaXOwr extends Experiment
{
	
	public senzaXOwr(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14RIDE");
		setNrRuns(30);


		Algorithm a;// ///< A generic optimiser.
	    //Problem p;// ///< A generic problem.
		
		a = new DE();
		a.setID("wrDEr1noxo");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
//		a.setParameter("p2", -1.0); //CR
		a.setParameter("p2", Double.NaN); //CR
		a.setParameter("p3", 1.0);//mutation strategy
		a.setParameter("p4", 0.0);//crossover strategy
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i, 0));




	}
}

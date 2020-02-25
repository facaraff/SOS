package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.CEC2014RotInvStudy;
import algorithms.CMAES;
import algorithms.SEP_CMAES;


public class CMAESrot extends Experiment
{
	
	public CMAESrot(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14");
		setNrRuns(10);

		Algorithm a;// ///< A generic optimiser.
	
		a = new CMAES();
		a.setID("rCMAES");
		add(a);
		
		a = new SEP_CMAES();
		a.setID("rSEP_CMAES");
		add(a);
	
		for(int i = 1; i<=30; i++)
				add(new CEC2014RotInvStudy(probDim, i));
	}
}

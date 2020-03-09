package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import benchmarks.RCEC2014;
import algorithms.CMAES;
import algorithms.SEP_CMAES;



public class CMAESunrot extends Experiment
{
	
	public CMAESunrot(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14");
		setNrRuns(30);

		Algorithm a;// ///< A generic optimiser.
	
		a = new CMAES();
		a.setID("wrCMAES");
		add(a);
		
		a = new SEP_CMAES();
		a.setID("wrSEP_CMAES");
		add(a);
		
		for(int i = 1; i<=30; i++)
				add(new RCEC2014(probDim, i, 0));
	}
}

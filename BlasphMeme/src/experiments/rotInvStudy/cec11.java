package experiments.rotInvStudy;

import interfaces.Experiment;
import interfaces.Algorithm;
import interfaces.Problem;
import algorithms.RIDE;
import algorithms.DE;
import algorithms.EigenDE;
import algorithms.MMCDE;

import applications.CEC2011.P1;
import applications.CEC2011.P2;

public class cec11 extends Experiment
{
	
	public cec11(int probDim) throws Exception
	{
		//super(probDim,"cec2015allDim");
		super(probDim,5000,"testCEC14RIDE");
		setNrRuns(30);

		Algorithm a;
	    Problem p;
		
		a = new DE();
		a.setID("DEr1bin");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 1.0);
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new DE();
		a.setID("DEr1exp");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new DE();
		a.setID("DEctr1");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7); //F
		a.setParameter("p2", Double.NaN);
		a.setParameter("p3", 4.0);
		a.setParameter("p4", Double.NaN);
		a.setParameter("p5", Double.NaN);//ALPHA
		add(a);
		
		
		a = new MMCDE();
		a.setID("MMCDE");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7); //F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 0.3);//ALPHA
		add(a);
		
		a = new RIDE();
		a.setID("RIDErand1bin");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7); //F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 4.0);
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new RIDE();
		a.setID("RIDErand1exp");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7); //F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 5.0);
		a.setParameter("p5", 0.3);//ALPHA
		add(a);
		
		a = new EigenDE();
		a.setID("EigenDEr1bin");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", 0.3); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 1.0);
		a.setParameter("p5", Double.NaN);//ALPHA
		a.setParameter("p6", 1.0);//PR
		add(a);
		
		a = new EigenDE();
		a.setID("EigenDEr1exp");
		a.setParameter("p0", (double)probDim);
		a.setParameter("p1", 0.7);//F
		a.setParameter("p2", -1.0); //CR
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 2.0);
		a.setParameter("p5", 0.3);//ALPHA
		a.setParameter("p6", 1.0);//PR
		add(a);
		
		p = new P1();
		add(p);
		p = new P2(probDim);
		add(p);




	}
}

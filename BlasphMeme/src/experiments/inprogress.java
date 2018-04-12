package experiments;

import interfaces.Experiment;
import interfaces.Algorithm;
import algorithms.inProgress.inprogressAlg;
import algorithms.inProgress.inprogressAlg2;
//import interfaces.Problem;
//import algorithms.singleSolution.*;

//import benchmarks.BaseFunctions.Alpine;
import benchmarks.CEC2014;
//import benchmarks.BaseFunctions.Ackley;
//import benchmarks.BaseFunctions.Rosenbrock;

public class inprogress extends Experiment
{
	
	public inprogress(int probDim) throws Exception
	{
		//super(probDim,"DESIGN");
		super(probDim,5000,"DESIGN");
		setNrRuns(30);
		
		Algorithm a;// ///< A generic optimiser.
//	    Problem p;// ///< A generic problem.


			
		    a = new inprogressAlg2();
		    a.setID("PMS2_25");
		    a.setParameter("p0", 5.0);
		    a.setParameter("p1", 0.9);
		    a.setParameter("p2", 3.0);
		    a.setParameter("p3", 10.0);
		    a.setParameter("p4", 0.18181818181);
		    a.setParameter("p5", 0.08333333333);
		    a.setParameter("p6", 0.44);
		    a.setParameter("p7", 1.0);
		    a.setParameter("p8", 100.0);	    
		    a.setParameter("p9", 0.25);
		    add(a);
		    
			   a = new inprogressAlg2();
			    a.setID("PMS2_20");
			    a.setParameter("p0", 5.0);
			    a.setParameter("p1", 0.9);
			    a.setParameter("p2", 3.0);
			    a.setParameter("p3", 10.0);
			    a.setParameter("p4", 0.18181818181);
			    a.setParameter("p5", 0.08333333333);
			    a.setParameter("p6", 0.44);
			    a.setParameter("p7", 1.0);
			    a.setParameter("p8", 100.0);	    
			    a.setParameter("p9", 0.20);
			    add(a);
			    
			    
			    
				   a = new inprogressAlg2();
				    a.setID("GAMBAPMS2_25");
				    a.setParameter("p0", 5.0);
				    a.setParameter("p1", 0.9);
				    a.setParameter("p2", 3.0);
				    a.setParameter("p3", 10.0);
				    a.setParameter("p4", 0.18181818181);
				    a.setParameter("p5", 0.08333333333);
				    a.setParameter("p6", 0.44);
				    a.setParameter("p7", 1.0);
				    a.setParameter("p8", 0.0);	    
				    a.setParameter("p9", 0.25);
				    add(a);
//			
//		    a = new inprogressAlg();
//		    a.setID("PMS_gamba20");
//		    a.setParameter("p0", 5.0);
//		    a.setParameter("p1", 0.9);
//		    a.setParameter("p2", 3.0);
//		    a.setParameter("p3", 10.0);
//		    a.setParameter("p4", 0.18181818181);
//		    a.setParameter("p5", 0.08333333333);
//		    a.setParameter("p6", 0.44);
//		    a.setParameter("p7", 1.0);
//		    a.setParameter("p8", 150.0);
//		    a.setParameter("p9", 0.4);	    
//		    a.setParameter("p10", 0.20);
//		    add(a);
//		    
//		    a = new inprogressAlg();
//		    a.setID("PMS_gamba25");
//		    a.setParameter("p0", 5.0);
//		    a.setParameter("p1", 0.9);
//		    a.setParameter("p2", 3.0);
//		    a.setParameter("p3", 10.0);
//		    a.setParameter("p4", 0.18181818181);
//		    a.setParameter("p5", 0.08333333333);
//		    a.setParameter("p6", 0.44);
//		    a.setParameter("p7", 1.0);
//		    a.setParameter("p8", 150.0);
//		    a.setParameter("p9", 0.4);	    
//		    a.setParameter("p10", 0.25);
//		    add(a);
		    

//		    a = new PMS2();
//		    a.setParameter("p0",0.95);
//		    a.setParameter("p1", 5.0);
//		    a.setParameter("p2", 0.9);
//		    a.setParameter("p3", 3.0);
//		    a.setParameter("p4", 10.0);
//		    a.setParameter("p5", 0.18181818181);
//		    a.setParameter("p6", 0.08333333333);
//		    a.setParameter("p7", 0.44);
//		    a.setParameter("p8", 1.0);
//		    a.setParameter("p9", 0.25);
//		    add(a);

//		    a = new ResampledCMAES11();
//		    a.setParameter("p0",0.25);
//		    a.setParameter("p1", 0.18181818181);
//		    a.setParameter("p2", 0.08333333333);
//		    a.setParameter("p3", 0.44);
//		    a.setParameter("p4", 1.0);
//		    a.setParameter("p5", 0.25);
//		    add(a);
		    
//		    a = new PMS();
//		    a.setParameter("p0",0.5);
//		    a.setParameter("p1", 0.4);
//		    a.setParameter("p2", 150.0);
//		    a.setParameter("p3", 2.0);
//		    a.setParameter("p4", 0.5);
//		    a.setParameter("p5", 0.5);
//		    add(a);
		    

//		    a = new CMAES_11();
//		    a.setParameter("p0",0.18181818181); // 2/11
//		    a.setParameter("p1", 0.08333333333);// 1/12
//		    a.setParameter("p2", 0.44);
//		    a.setParameter("p3", 1.0);// 1 --> problem dependent!!
//		    add(a);
		    
//		    a = new NonUniformSA();
//		    a.setParameter("p0",5.0);
//		    a.setParameter("p1", 0.9);
//		    a.setParameter("p2", 3.0);
//		    a.setParameter("p3", 10.0);
//		    add(a);
		    

//

//	    


		for(int i = 1; i<=30; i++)
			add(new CEC2014(probDim, i));

	}
}

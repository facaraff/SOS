
/** @file RunExperiments.java
 *  
 *  @author Fabio Caraffini
*/
import java.util.Vector; 
import interfaces.Experiment;

import static utils.RunAndStore.resultsFolder;
//import experiments.*;
//import experiments.rotInvStudy.*;
import experiments.BenchmarksTesting.*;

/** 
* This class contains the main method and has to be used for launching experiments.
*/
public class RunExperiments
{
	
	
	/** 
	* Main method.
	* This method has to be modified in order to launch a new experiment.
	*/
	public static void main(String[] args) throws Exception
	{	
		
		// make sure that "results" folder exists
		resultsFolder();
	
	
		Vector<Experiment> experiments = new Vector<Experiment>();////!< List of problems 
	
			
		//@@@ MODIFY THIS PART @@@		

//		experiments.add(new extendedtoro(10));
//		
//		experiments.add(new extendedtoro(50));
//		
//		experiments.add(new extendedtoro(100));
		
//		experiments.add(new inprogress(10));
//		
//		experiments.add(new inprogress(50));
//		
//		experiments.add(new inprogress(100));
//		

	
//		experiments.add(new EigenToro(10));
//		
//		experiments.add(new EigenToro(50));
//		
//		experiments.add(new EigenToro(100));
		
//		experiments.add(new TestCEC2011());
		
		//experiments.add(new Bbob2010(10));//XXX FABIO: do not work 
		
		//experiments.add(new Cec2013_c(10));//XXX FABIO: do not work do implement the java version!!
		
		//experiments.add(new Cec2013_c(50));
		
		experiments.add(new Cec2010());
		
		experiments.add(new Cec2010_c());
		
		
		experiments.add(new Sisc2010(20));
		experiments.add(new Sisc2010(30));
		experiments.add(new Sisc2010(60));
		
		experiments.add(new Cec2008());
		
		//experiments.add(new cec11(30));
		
		
		//@@@@@@
	
		for(Experiment experiment : experiments)
		{
			//experiment.setShowPValue(true);
			experiment.startExperiment();
			System.out.println();
			experiment = null;
		}
	
		
		
	}
	
	

}

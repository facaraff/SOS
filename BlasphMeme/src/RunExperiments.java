
/** @file RunExperiments.java
 *  
 *  @author Fabio Caraffini
*/
import java.util.Vector; 
import interfaces.Experiment;

import static utils.RunAndStore.resultsFolder;
import experiments.*;
//import experiments.rotInvStudy.*;

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
		
//		experiments.add(new inprogress(10));
//		
//		experiments.add(new inprogress(50));
//		
//		experiments.add(new inprogress(100));
//		

	
		experiments.add(new EigenToro(10));
		
		experiments.add(new EigenToro(50));
		
		experiments.add(new EigenToro(100));
		
		
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

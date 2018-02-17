package mains;
/** @file RunExperiments.java
 *  
 *  @author Fabio Caraffini
*/
import java.util.Vector; 
import interfaces.Experiment;

import static utils.RunAndStore.resultsFolder;
//import experiments.*;
//import experiments.rotInvStudy.CEC14_rot;
//import experiments.rotInvStudy.CEC14_unrot;
//import experiments.rotInvStudy.*;
import experiments.blessing.*;

/** 
* This class contains the main method and has to be used for launching experiments.
*/
public class RunBlessing
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

		
		experiments.add(new NewBlessingTests(10));
		
		experiments.add(new NewBlessingTests(30));
		
		experiments.add(new NewBlessingTests(50));
		
		experiments.add(new NewBlessingTests(100));
		//@@@@@@
	
	
		for(Experiment experiment : experiments)
		{
			//experiment.setShowPValue(true);
			experiment.startBlindExperiment();
			System.out.println();
			experiment = null;
		}
	
		
		
	}
	
	

}

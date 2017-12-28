
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
import experiments.rotInvStudy.*;

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
	
//		experiments.add(new CEC14_rot(10));
//		experiments.add(new CEC14_rot(20));
//		experiments.add(new CEC14_rot(30));
//		experiments.add(new CEC14_rot(50));
//		experiments.add(new CEC14_rot(100));
//		experiments.add(new CEC14_unrot(10));
//		experiments.add(new CEC14_unrot(20));
//		experiments.add(new CEC14_unrot(30));
//		experiments.add(new CEC14_unrot(50));
//		experiments.add(new CEC14_unrot(100));

//		experiments.add(new CEC14_rot_EXP_TUNING(10));
//		experiments.add(new CEC14_rot_EXP_TUNING(20));
//		experiments.add(new CEC14_rot_EXP_TUNING(50));
//		experiments.add(new CEC14_rot_EXP_TUNING(100));
	
//		experiments.add(new CMAESrot(10));
//		experiments.add(new CMAESrot(50));
//		experiments.add(new CMAESrot(100));
		
//		experiments.add(new CMAESunrot(10));
//		experiments.add(new CMAESunrot(50));
//		experiments.add(new CMAESunrot(100));

//		experiments.add(new CEC14_RIDE_rot(10));
//		experiments.add(new CEC14_RIDE_rot(50));
//		experiments.add(new CEC14_RIDE_rot(100));
		
//		experiments.add(new Rot_final(10));
//		experiments.add(new Rot_final(50));
//		experiments.add(new Rot_final(100));
		
//		experiments.add(new Unrot_final(10));
//		experiments.add(new Unrot_final(50));
//		experiments.add(new Unrot_final(100));
		
		experiments.add(new Rot_eigen(10));
		
		experiments.add(new Rot_eigen(50));
		
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

/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FreeBSD Project.
*/



/** @file RunExperiments.java
 * 
 *  @author Fabio Caraffini
*/

import java.util.Vector;

import experiments.FCGIAY.*;
import interfaces.Experiment;

import static utils.RunAndStore.resultsFolder;

/** 
* This class contains the main method and has to be used for launching experiments.
*/
public class RunExperiments
{
	
	/**
	* Main method.
	* This method has to be modified in order to launch a new experiment.
	* @param args For passing args to the main value.
	* @throws Exception The main method must be able to handle possible java.lang.Exceptionincluding I/O exceptions etc.
	*/
	public static void main(String[] args) throws Exception
	{	
		
		// make sure that "results" folder exists
		resultsFolder();
	
	
		Vector<Experiment> experiments = new Vector<Experiment>();////!< List of problems 
	
			
		//@@@ MODIFY THIS PART @@@		
		
//		 experiments.add(new CEC14(10));
//		 experiments.add(new CEC14(50));
//		 experiments.add(new CEC14(100));
		
//		experiments.add(new contros(10));
//		experiments.add(new RotCEC14(10));
//		experiments.add(new RotCEC14(50));
//		experiments.add(new RotCEC14(100));
		
//		experiments.add(new RIAPP(60));
//		experiments.add(new RIAPP(300));
//		experiments.add(new RIAPP(3000));
//		experiments.add(new RIAPP(900));
		
//		experiments.add(new ProblemClassification(10));
		experiments.add(new ProblemClassification(50));
//		experiments.add(new ProblemClassification(100));	
		
//		experiments.add(new EarlyDE(10));
//		experiments.add(new EarlyDE(50));
//		experiments.add(new EarlyDE(100));

//		experiments.add(new EarlyDEBBOB2018(10, 100));
//		experiments.add(new Bbob2018(10));
		
//		experiments.add(new RiBinVsExp(10));
//		experiments.add(new RiBinVsExp(50));
//		experiments.add(new RiBinVsExp(100));
//		experiments.add(new RiBinVsExp(1000));



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

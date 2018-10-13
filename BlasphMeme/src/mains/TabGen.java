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
package mains;
import utils.resultsProcessing.Experiment;
//import utils.resultsProcessing.TableAvgStdStat;
//import utils.resultsProcessing.TableStatistics;
//import utils.resultsProcessing.TableHolmBonferroni;
import utils.resultsProcessing.*;
//import static utils.RunAndStore.slash;

public class TabGen
{
	public static void main (String args[]) throws Exception
	{
//		String s = slash();
		String workingDir = "";
		if (args.length < 1)
		{
//			workingDir ="../../results";
//			workingDir ="/home/facaraff/git/BlasphMeme/results";
			//workingDir ="/home/facaraff/git/BlasphMeme/BlasphMeme/results";
			//workingDir ="/home/facaraff/Desktop/LIBIBBO/UNROT";
//			workingDir ="/home/facaraff/Desktop/RisTORO/tottrend";
//			workingDir ="C:\\Users\\\\Badddobaby\\Desktop\\compatti";
//			workingDir ="/home/orzobimbo/Desktop/COMPACTORI";
			//workingDir ="/home/orzobimbo/Desktop/RICOMPACT";
			workingDir ="/home/facaraff/Desktop/HYPER";
			//workingDir ="/home/orzobimbo/Desktop/VACCHEBOIE";
//			workingDir ="C:\\Users\\fcaraf00\\Desktop\\TABELLONISSIME\\UNROT";
			//workingDir ="C:\\Users\\fcaraf00\\Desktop\\BIN";
			//System.err.println("Usage: " + Analyse.class.getSimpleName() + " " + "RESULT_FOLDER");
			//System.exit(-1);
		}
		else
			workingDir = args[0];

		Experiment experiment = new Experiment();
		experiment.setDirectory(workingDir);
		//experiment.setTrendsFlag(true, false);
//		experiment.setTrendsFlag(true, true);
		//experiment.setTrendsFlag(true);
		experiment.importData();
		experiment.describeExperiment();

		// to save some memory...
		/*
		experiment.computeAVG();
		experiment.computeSTD();
		experiment.computeMedian();
		experiment.deleteFinalValues();
		*/


		TableHolmBonferroni T3 = new TableHolmBonferroni(experiment);
		T3.setReferenceAlgorithm();
		T3.execute();
		
		
		TableStatistics T4 = new TableAvgStdStat(experiment, true, true);
		T4.setErrorFlag(true);
		T4.setReferenceAlgorithm();
		T4.execute();
	}
}

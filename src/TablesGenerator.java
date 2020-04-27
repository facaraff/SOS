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

import utils.resultsProcessing.Experiment;
import utils.resultsProcessing.TableAvgStdStat;
import utils.resultsProcessing.TableStatistics;
import utils.resultsProcessing.TableHolmBonferroni;

public class TablesGenerator
{
	public static void main (String args[]) throws Exception
	{
		String workingDir = "";
		if (args.length < 1)
		{
			workingDir ="./results/BINVSEXP";
		}
		else
			workingDir = args[0];

		Experiment experiment = new Experiment();
		experiment.setDirectory(workingDir);
		//experiment.setTrendsFlag(true, false);
		//experiment.setTrendsFlag(true, true);
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

		/*
		Tests T1 = new TableCEC2013Competition(experiment);
		T1.setErrorFlag(true);
		T1.execute();

		Tests T2 = new TableBestWorstMedAvgStd(experiment);
		T2.setErrorFlag(true);
		T2.execute();
		 */
		
		TableHolmBonferroni T3 = new TableHolmBonferroni(experiment);
		T3.setReferenceAlgorithm();
		T3.execute();
//		

		TableStatistics T4 = new TableAvgStdStat(experiment, true, true);
		T4.setErrorFlag(false);
		T4.setReferenceAlgorithm();
		T4.execute();
	}
}

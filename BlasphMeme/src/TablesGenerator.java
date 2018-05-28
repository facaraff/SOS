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
			//workingDir ="../results/cec2015allDim";
//			workingDir ="../../results/testCEC14";
//			workingDir ="C:\\Users\\Badddobaby\\git\\BlasphMeme\\BlasphMeme\\results\\TESTCEC2011";
			//workingDir ="/home/facaraff/Desktop/FINAL/ROT";
			workingDir ="/home/orzobimbo/Desktop/APP";
			//System.err.println("Usage: " + Analyse.class.getSimpleName() + " " + "RESULT_FOLDER");
			//System.exit(-1);
		}
		else
			workingDir = args[0];

		Experiment experiment = new Experiment();
		experiment.setDirectory(workingDir);
		experiment.setTrendsFlag(true, false);
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

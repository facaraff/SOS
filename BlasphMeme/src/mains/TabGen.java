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
			workingDir ="/home/facaraff/Desktop/DACAPIRE/PMS";
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

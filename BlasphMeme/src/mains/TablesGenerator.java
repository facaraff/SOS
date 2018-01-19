package mains;
import utils.resultsProcessing.Experiment;
import utils.resultsProcessing.TableAvgStdStat;
import utils.resultsProcessing.TableStatistics;
import utils.resultsProcessing.TableHolmBonferroni;
//import static utils.RunAndStore.slash;

public class TablesGenerator
{
	public static void main (String args[]) throws Exception
	{
//		String s = slash();
		String workingDir = "";
		if (args.length < 1)
		{
			//workingDir ="../results/cec2015allDim";
//			workingDir ="C:\\Users\\fcaraf00\\Desktop\\FINAL\\UNROT";
			workingDir ="C:\\Users\\fcaraf00\\Desktop\\BIN";
			//System.err.println("Usage: " + Analyse.class.getSimpleName() + " " + "RESULT_FOLDER");
			//System.exit(-1);
		}
		else
			workingDir = args[0];

		Experiment experiment = new Experiment();
		experiment.setDirectory(workingDir);
		//experiment.setTrendsFlag(true, false);
		experiment.setTrendsFlag(true, true);
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


//		TableHolmBonferroni T3 = new TableHolmBonferroni(experiment);
//		T3.setReferenceAlgorithm();
//		T3.execute();
		

		TableStatistics T4 = new TableAvgStdStat(experiment, true, true);
		T4.setErrorFlag(true);
		T4.setReferenceAlgorithm();
		T4.execute();
	}
}

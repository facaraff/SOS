package mains.BIAS;

import java.util.Vector;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import utils.ExperimentHelper;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.MatLab;
import utils.RunAndStore.FTrend;
import utils.random.RandUtils;

public abstract class ISBMain {


	protected static double[] bias = null; //Benchmark additive bias
	
	

	protected static double runAlgorithmRepetition(AlgorithmBias algorithm, Problem problem, ExperimentHelper expSettings) throws Exception
	{
		FTrend FT = algorithm.execute(problem, expSettings.getBudgetFactor()*problem.getDimension());

		return FT.getLastF();
	}

	
	
	protected static void execute(Vector<AlgorithmBias> algorithms, Vector<Problem> problems, ExperimentHelper expSettings) throws Exception
	{	
		
		int algorithmIndex = 0;
		for (AlgorithmBias algorithm : algorithms)
		{
			System.out.print("" + "\t");
			String algName = algorithm.getClass().getSimpleName();
			if (algName.length() >= 8)
				System.out.print(algName + "\t");
			else
				System.out.print(algName + "\t\t");
			
			if (algorithmIndex > 0)
			{
				System.out.print("\t" + "W");
				
			}
			algorithmIndex++;
		}	
		System.out.println();
				
		double[][] finalValues;
		MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();


		int problemIndex = 0;
		for (Problem problem: problems)
		{
			System.out.print("f" + (problemIndex+1) + "\t");

			finalValues = new double[algorithms.size()][expSettings.getNrRepetitions()];
			algorithmIndex = 0;
			for (AlgorithmBias algorithm : algorithms)
				{
					for (int i = 0; i < expSettings.getNrRepetitions(); i++)
					{
						algorithm.setRun(i);
						
						double best = runAlgorithmRepetition(algorithm, problem, expSettings);
						if (bias != null)
							finalValues[algorithmIndex][i] = best - bias[problemIndex];
						else
							finalValues[algorithmIndex][i] = best;
								
					}
					System.out.print("done+\t");
					String mean = utils.RunAndStore.format(MatLab.mean(finalValues[algorithmIndex]));
					String std = utils.RunAndStore.format(MatLab.std(finalValues[algorithmIndex]));
					System.out.print(mean + " \u00B1 " + std + "\t");
					if (algorithmIndex > 0)
					{			
						double pValue = mannWhitneyUTest.mannWhitneyUTest(finalValues[0], finalValues[algorithmIndex]);
						char w = '=';
						if (pValue < 0.05)
						{
							if (MatLab.mean(finalValues[0]) < MatLab.mean(finalValues[algorithmIndex]))
								w = '+';
							else
								w = '-';
						}

						System.out.print(w + "\t");
						
					}
					algorithmIndex++;
				}

				System.out.println();
				problemIndex++;
				}
			}
	
	
	
	
	public static  class Noise extends Problem 
	{
		public Noise(int dimension, double[][] bounds) { super(dimension, bounds); setFID("f0");}
		
		public void setFID(String string) {setFID(string);}

		public double f(double[] x){return RandUtils.random();}
	}
	


}
	



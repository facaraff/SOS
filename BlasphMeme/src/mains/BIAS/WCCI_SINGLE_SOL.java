package mains.BIAS;


import java.util.Vector;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import utils.MatLab;
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

import algorithms.specialOptions.BIAS.singleSol.CMAES11;
	
public class WCCI_SINGLE_SOL
{
	static int nrRepetitions = 50;
	static int budgetFactor = 10000;
	static int problemDimension = 30;
	
	public static void main(String[] args) throws Exception
	{	
		Vector<Algorithm> algorithms = new Vector<Algorithm>();
		Vector<Problem> problems = new Vector<Problem>();
		
		Algorithm a;
		Problem p;
		
		double[] bias = null;
		
		a = new CMAES11('t');
		a.setParameter("p0",(2.0/11.0));
		a.setParameter("p1",(1.0/12.0));
		a.setParameter("p2",0.44);
		a.setParameter("p3",1.0);
		algorithms.add(a);
		
		a = new CMAES11('s');
		a.setParameter("p0",(2.0/11.0));
		a.setParameter("p1",(1.0/12.0));
		a.setParameter("p2",0.44);
		a.setParameter("p3",1.0);
		algorithms.add(a);	
		
		a = new CMAES11('e');
		a.setParameter("p0",(2.0/11.0));
		a.setParameter("p1",(1.0/12.0));
		a.setParameter("p2",0.44);
		a.setParameter("p3",1.0);
		algorithms.add(a);
		
		a = new CMAES11('d');
		a.setParameter("p0",(2.0/11.0));
		a.setParameter("p1",(1.0/12.0));
		a.setParameter("p2",0.44);
		a.setParameter("p3",1.0);
		algorithms.add(a);
		
		double[][] bounds = new double[problemDimension][2];
		for(int i=0; i<problemDimension; i++)
		{
			bounds[i][0] = 0;
			bounds[i][1] = 1;
		}	
		p = new Noise( problemDimension, bounds);
		problems.add(p);	
		
		int algorithmIndex = 0;
		for (Algorithm algorithm : algorithms)
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

			finalValues = new double[algorithms.size()][nrRepetitions];
			algorithmIndex = 0;
			for (Algorithm algorithm : algorithms)
				{
					for (int i = 0; i < nrRepetitions; i++)
					{
						algorithm.setRun(i);
						
						double best = runAlgorithmRepetition(algorithm, problem);
						if (bias != null)
							finalValues[algorithmIndex][i] = best - bias[problemIndex];
						else
							finalValues[algorithmIndex][i] = best;
								
					}
					System.out.println("done");
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

			public static class Noise extends Problem 
			{
				public Noise(int dimension, double[][] bounds) { super(dimension, bounds); }

				public double f(double[] x){return RandUtils.random();}
			}
			

			private static double runAlgorithmRepetition(Algorithm algorithm, Problem problem) throws Exception
			{
				FTrend FT = algorithm.execute(problem, budgetFactor*problem.getDimension());

				return FT.getLastF();
			
			
			}
				
			
		}

		
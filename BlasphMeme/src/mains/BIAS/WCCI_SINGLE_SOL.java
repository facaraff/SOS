package mains.BIAS;


import java.util.Vector;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

//import algorithms.specialOptions.BIAS.singleSolutions.CMAES11;
//import algorithms.specialOptions.BIAS.singleSolutions.ISPO;
//import algorithms.specialOptions.BIAS.singleSolutions.RIS;
import algorithms.specialOptions.BIAS.singleSolutions.*;
import utils.MatLab;
import utils.random.RandUtils;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import static utils.RunAndStore.slash;
	
public class WCCI_SINGLE_SOL
{
	static int nrRepetitions = 50;
	static int budgetFactor = 10000;
	static int problemDimension = 30;
	
	static String dir = "/home/facaraff/Desktop/KONODATA/SINGLESOLUTION/";
	//static String dir="C:\\Users\\fcaraf00\\Desktop\\KONONOVA\\";
	
	public static void main(String[] args) throws Exception
	{	
		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
		
		AlgorithmBias a;
		Problem p;
		
		double[] bias = null;
		
//		a = new CMAES11('t');
//		a.setDir(dir+"CMAES11"+slash());
//		a.setParameter("p0",(2.0/11.0));
//		a.setParameter("p1",(1.0/12.0));
//		a.setParameter("p2",0.44);
//		a.setParameter("p3",1.0);
//		algorithms.add(a);
//		
//		a = new CMAES11('s');
//		a.setDir(dir+"CMAES11"+slash());
//		a.setParameter("p0",(2.0/11.0));
//		a.setParameter("p1",(1.0/12.0));
//		a.setParameter("p2",0.44);
//		a.setParameter("p3",1.0);
//		algorithms.add(a);	
//		
//		a = new CMAES11('e');
//		a.setDir(dir+"CMAES11"+slash());
//		a.setParameter("p0",(2.0/11.0));
//		a.setParameter("p1",(1.0/12.0));
//		a.setParameter("p2",0.44);
//		a.setParameter("p3",1.0);
//		algorithms.add(a);
//		
//		a = new CMAES11('d');
//		a.setDir(dir+"CMAES11"+slash());
//		a.setParameter("p0",(2.0/11.0));
//		a.setParameter("p1",(1.0/12.0));
//		a.setParameter("p2",0.44);
//		a.setParameter("p3",1.0);
//		algorithms.add(a);
//		
//		a = new ISPO();
//		a.setCorrection('t');
//		a.setParameter("p0",1.0);
//		a.setParameter("p1",10.0);
//		a.setParameter("p2",2.0);
//		a.setParameter("p3",4.0);
//		a.setParameter("p4",0.000001);
//		a.setParameter("p5",30.0);
//		algorithms.add(a);
//		
//		a = new ISPO();
//		a.setDir(dir+"ISPO"+slash());
//		a.setCorrection('s');
//		a.setParameter("p0",1.0);
//		a.setParameter("p1",10.0);
//		a.setParameter("p2",2.0);
//		a.setParameter("p3",4.0);
//		a.setParameter("p4",0.000001);
//		a.setParameter("p5",30.0);
//		algorithms.add(a);
//		
//		a = new ISPO();
//		a.setDir(dir+"ISPO"+slash());
//		a.setCorrection('t');
//		a.setParameter("p0",1.0);
//		a.setParameter("p1",10.0);
//		a.setParameter("p2",2.0);
//		a.setParameter("p3",4.0);
//		a.setParameter("p4",0.000001);
//		a.setParameter("p5",30.0);
//		algorithms.add(a);
//		
//		a = new ISPO();
//		a.setDir(dir+"ISPO"+slash());
//		a.setCorrection('d');
//		a.setParameter("p0",1.0);
//		a.setParameter("p1",10.0);
//		a.setParameter("p2",2.0);
//		a.setParameter("p3",4.0);
//		a.setParameter("p4",0.000001);
//		a.setParameter("p5",30.0);
//		algorithms.add(a);
		
//		a = new RIS();
//		a.setParameter("p0",0.5);
//		a.setParameter("p1",0.4);
//		a.setParameter("p2",0.000001);
//		a.setCorrection('s');
//		algorithms.add(a);
//		
//		a = new RIS();
//		a.setDir(dir+"RIS"+slash());
//		a.setParameter("p0",0.5);
//		a.setParameter("p1",0.4);
//		a.setParameter("p2",0.000001);
//		a.setCorrection('t');
//		algorithms.add(a);
//		
//		a = new RIS();
//		a.setDir(dir+"RIS"+slash());
//		a.setParameter("p0",0.5);
//		a.setParameter("p1",0.4);
//		a.setParameter("p2",0.000001);
//		a.setCorrection('d');
//		algorithms.add(a);
//		
//		a = new NonUniformSA();
//		a.setDir(dir+"NUSA"+slash());
//		a.setCorrection('t');
//		a.setParameter("p0",5.0);
//		a.setParameter("p1",0.9);
//		a.setParameter("p2",3.0);
//		a.setParameter("p3",10.0);
//		algorithms.add(a);
//		
//		a = new NonUniformSA();
//		a.setDir(dir+"NUSA"+slash());
//		a.setCorrection('s');
//		a.setParameter("p0",5.0);
//		a.setParameter("p1",0.9);
//		a.setParameter("p2",3.0);
//		a.setParameter("p3",10.0);
//		algorithms.add(a);
//		
//		a = new NonUniformSA();
//		a.setDir(dir+"NUSA"+slash());
//		a.setCorrection('d');
//		a.setParameter("p0",5.0);
//		a.setParameter("p1",0.9);
//		a.setParameter("p2",3.0);
//		a.setParameter("p3",10.0);
//		algorithms.add(a);
//		
//		a = new NonUniformSA();
//		a.setDir(dir+"NUSA"+slash());
//		a.setCorrection('e');
//		a.setParameter("p0",5.0);
//		a.setParameter("p1",0.9);
//		a.setParameter("p2",3.0);
//		a.setParameter("p3",10.0);
//		algorithms.add(a);
//
//		a = new cDE_exp_light();
//		a.setDir(dir+"CDEl"+slash());
//		a.setParameter("p0",300.0);
//		a.setParameter("p1",0.25);
//		a.setParameter("p2",0.5);
//		a.setCorrection('x');
//		algorithms.add(a);
//
//		a = new cGA_real();
//		a.setDir(dir+"CGA"+slash());
//		a.setParameter("p0",300.0);
//		a.setParameter("p1",200.0);
//		a.setCorrection('x');
//		algorithms.add(a);
//		
		a = new cBFO();
		a.setDir(dir+"CBFO"+slash());
		a.setParameter("p0", 300.0);
		a.setParameter("p1", 0.1);
		a.setParameter("p2", 4.0);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 10.0);
		a.setParameter("p5", 2.0);
		a.setParameter("p6", 2.0);
		a.setCorrection('t');
		algorithms.add(a);
		
		a = new cBFO();
		a.setDir(dir+"CBFO"+slash());
		a.setParameter("p0", 300.0);
		a.setParameter("p1", 0.1);
		a.setParameter("p2", 4.0);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 10.0);
		a.setParameter("p5", 2.0);
		a.setParameter("p6", 2.0);
		a.setCorrection('s');
		algorithms.add(a);
		
		a = new cBFO();
		a.setDir(dir+"CBFO"+slash());
		a.setParameter("p0", 300.0);
		a.setParameter("p1", 0.1);
		a.setParameter("p2", 4.0);
		a.setParameter("p3", 1.0);
		a.setParameter("p4", 10.0);
		a.setParameter("p5", 2.0);
		a.setParameter("p6", 2.0);
		a.setCorrection('d');
		algorithms.add(a);	

		a = new cPSO();
		a.setDir(dir+"CPSO"+slash());
		a.setCorrection('t');
		a.setParameter("p0", 50.0);
		a.setParameter("p1", -0.2);
		a.setParameter("p2", -0.07);
		a.setParameter("p3", 3.74);
		a.setParameter("p4", 1.0);
		a.setParameter("p5", 1.0);
		algorithms.add(a);
		
		a = new cPSO();
		a.setDir(dir+"CPSO"+slash());
		a.setCorrection('s');
		a.setParameter("p0", 50.0);
		a.setParameter("p1", -0.2);
		a.setParameter("p2", -0.07);
		a.setParameter("p3", 3.74);
		a.setParameter("p4", 1.0);
		a.setParameter("p5", 1.0);
		algorithms.add(a);
		
		a = new cPSO();
		a.setDir(dir+"CPSO"+slash());
		a.setCorrection('d');
		a.setParameter("p0", 50.0);
		a.setParameter("p1", -0.2);
		a.setParameter("p2", -0.07);
		a.setParameter("p3", 3.74);
		a.setParameter("p4", 1.0);
		a.setParameter("p5", 1.0);
		algorithms.add(a);


		
		double[][] bounds = new double[problemDimension][2];
		for(int i=0; i<problemDimension; i++)
		{
			bounds[i][0] = 0;
			bounds[i][1] = 1;
		}	
		p = new Noise(problemDimension, bounds);
		problems.add(p);	
		
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

			finalValues = new double[algorithms.size()][nrRepetitions];
			algorithmIndex = 0;
			for (AlgorithmBias algorithm : algorithms)
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

			public static class Noise extends Problem 
			{
				public Noise(int dimension, double[][] bounds) { super(dimension, bounds); }

				public double f(double[] x){return RandUtils.random();}
			}
			

			private static double runAlgorithmRepetition(AlgorithmBias algorithm, Problem problem) throws Exception
			{
				FTrend FT = algorithm.execute(problem, budgetFactor*problem.getDimension());

				return FT.getLastF();
			
			
			}
				
			
		}

		
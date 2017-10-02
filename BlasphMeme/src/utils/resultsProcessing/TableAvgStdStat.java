package utils.resultsProcessing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import jsc.independentsamples.MannWhitneyTest;
import jsc.independentsamples.TwoSampleTtest;
import jsc.tests.H1;
import static utils.MatLab.mean;

public class TableAvgStdStat extends TableStatistics
{	
	boolean useAdvancedStatistic;
	boolean useBold;

	/**
	 * Generate a table with averages, standard deviations, and statistic significance.
	 * 
	 * @param experiment the experiment;
	 * @param useAdvancedStatistic true if advanced statistics should be used, false otherwise.
	 * @param useBold true if boldify best results, false otherwise.
	 */
	public TableAvgStdStat(Experiment experiment, boolean useAdvancedStatistic, boolean useBold)
	{
		this.setExperiment(experiment);
		this.useAdvancedStatistic = useAdvancedStatistic;
		this.useBold = useBold;
	}

	public void execute() throws Exception
	{
		this.experiment.computeAVG();
		this.experiment.computeSTD();
		AlgorithmInfo[] AlgList = this.experiment.getAlgorithms();
		AlgorithmInfo[] A = null;

		int algorithms = AlgList.length;
		if (algorithms >= 2)
		{			
			DataBox[] refData = AlgList[this.referenceAlgorithm].getData();
			String refName = AlgList[this.referenceAlgorithm].getName();
			AlgList = removeReference(AlgList,this.referenceAlgorithm);

			int tabNum = 0;
			while (AlgList.length != 0)
			{
				tabNum++;
				if (AlgList.length == 1)
				{
					A = AlgList;
					AlgList = this.removeAlg(AlgList,0);
				}
				else
				{
					Vector<Integer> V = this.selectAlg(AlgList);
					A = this.select(V, AlgList);
					algorithms = A.length + 1;
					AlgList = removeAlg(AlgList, V);
				}
				int subExperiments = A[0].subExperimentsNumber();
				this.setTables(subExperiments);

				// for each sub-experiment (benchmark and problem size)
				for (int subE=0; subE < subExperiments; subE++)
				{	
					int dimensions = refData[subE].getDimension(); 
					String benchmark = refData[subE].getBenchmark();
					String citation = refData[subE].getCitation();
					this.Tables[subE] = refData[subE].getName() + ".WILCOXON"+"("+tabNum+")";
					int functions = refData[subE].getFunctionsNumber();
					Vector<Double>[] V = refData[subE].getFinalValues();
					String[][] W = new String[A.length][functions];

					double[][] AVGs = new double[functions][algorithms];
					double[][] STDs = new double[functions][algorithms]; 
					double[] temp1 = refData[subE].getAVG();
					double[] temp2 = refData[subE].getSTD(); 
					for (int i=0; i < functions; i++)
					{			
						AVGs[i][0] = temp1[i];
						STDs[i][0] = temp2[i];
					}
					temp1 = null; temp2 = null;

					// for each benchmark function
					for (int fun=0; fun < functions; fun++)
					{
						if (V[fun] != null)
						{
							double[] refDataArray = new double[V[fun].size()];
							for (int run=0; run < V[fun].size(); run++)
								refDataArray[run] = (Double)V[fun].get(run);
							// for each compared algorithm
							for (int alg=0; alg < A.length; alg++)
							{	
								AVGs[fun][alg + 1] = A[alg].getData()[subE].getAVG()[fun];
								STDs[fun][alg + 1] = A[alg].getData()[subE].getSTD()[fun];
								if (A[alg].getData()[subE].getFinalValues()[fun] != null)
								{
									double[] dataArray = new double[A[alg].getData()[subE].getFinalValues()[fun].size()];
									for (int run=0; run < dataArray.length; run++)
										dataArray[run] = (Double)A[alg].getData()[subE].getFinalValues()[fun].get(run);
									
									W[alg][fun] = compareResults(refDataArray, dataArray, useAdvancedStatistic, this.getSignificanceProbability());
								}
								else
								{
									W[alg][fun] = "";
									AVGs[fun][alg + 1] = A[alg].getData()[subE].getAVG()[fun];
									STDs[fun][alg + 1] = A[alg].getData()[subE].getSTD()[fun];
								}			
							}
						}
						else
						{
							for (int alg=0; alg < A.length; alg++)
							{
								W[alg][fun] = "";
								AVGs[fun][alg + 1] = Double.NaN;
								STDs[fun][alg + 1] = Double.NaN;
							}
						}
					}

					int[] bold = new int[functions];
					for (int i=0; i < functions; i++)
					{
						if (useBold)
							bold[i] = indexMin(AVGs[i],STDs[i]);
						else
							bold[i] = -1;
					}

					String[][] avgTab = new String[functions][algorithms];
					String[][] stdTab = new String[functions][algorithms];
					String caption = new String();	

					if (this.getErrorFlag())
					{
						double[] minima = refData[subE].getMinima();

						for (int i=0; i < functions; i++)
						{
							for (int n=0; n < algorithms; n++)
							{
								if (bold[i] != n)
								{
									double error = AVGs[i][n] - minima[i];
									if (error < 0)
									{
										System.out.println("Wrong BIAS");
										System.out.println("AVG: "+AVGs[i][n]);
										System.out.println("bias: "+minima[i]);
										System.out.println("error: "+error);
									}
									/*
									if (benchmark.equals("BBOB2010"))
									{
										System.out.println("avg: "+AVGs[i][n]);
										System.out.println("bias: "+minima[i]);
										System.out.println("avg - bias: "+(AVGs[i][n] - minima[i]));
										System.out.println("error: "+error);
										System.out.println("approx: "+ this.D2S(error));
										System.out.println();
									}
									 */
									avgTab[i][n] = this.D2S(error);
									stdTab[i][n] = this.D2S(STDs[i][n]);
								}
								else
								{	
									double error = AVGs[i][n] - minima[i];
									if (error < 0){
										System.out.println("Wrong BIAS");
										System.out.println("AVG: "+AVGs[i][n]);
										System.out.println("bias: "+minima[i]);
										System.out.println("error: "+error);
									}
									/*
									if (benchmark.equals("CEC2013"))
									{
										System.out.println("avg: "+AVGs[i][n]);
										System.out.println("bias: "+minima[i]);
										System.out.println("avg - bias: "+(AVGs[i][n] - minima[i]));
										System.out.println("error: "+error);
										System.out.println("approx: "+ this.D2S(error));
										System.out.println();
									}
									 */
									avgTab[i][n] = "\\mathbf{" + this.D2S(error) + "}";
									stdTab[i][n] = "\\mathbf{" + this.D2S(STDs[i][n]) + "}";
								}
							}
						}

						caption = "error";
					}
					else
					{
						for (int i=0; i < functions; i++)
						{
							for (int n=0; n < algorithms; n++)
							{
								if (bold[i] != n)
								{
									avgTab[i][n] = this.D2S(AVGs[i][n]);
									stdTab[i][n] = this.D2S(STDs[i][n]);
								}
								else
								{
									avgTab[i][n] = "\\mathbf{" + this.D2S(AVGs[i][n]) + "}";
									stdTab[i][n] = "\\mathbf{" + this.D2S(STDs[i][n]) + "}";
								}
							}
						}

						caption = "fitness";
					}

					String test = "";
					if (useAdvancedStatistic)
						test = "statistic comparison";
					else
						test = "Wilcoxon Rank-Sum Test";

					String[] table = new String[8 + functions + 5];
					table[0] = "\\begin{table}"; //\begin{table*}
					table[1] = "\\caption{Average "+caption+" $\\pm$ standard deviation and "+test+" (reference: "+refName+") for "+refName+" against ... on "+benchmark + citation+" in $"+dimensions+"$ dimensions.}\\label{tab:...}";
					table[2] = "\\begin{center}";
					table[3] = "\\begin{tiny}";
					table[4] = "\\begin{tabular}{l|r@{$\\,\\pm\\,$}l|c"; if (algorithms==1) table[4] += "}"; if (algorithms==2) table[4] += "|r@{$\\,\\pm\\,$}l|c}";
					if (algorithms > 2)
					{
						for (int i=1; i < algorithms-1; i++)
							table[4] += "|r@{$\\,\\pm\\,$}l|c";
						table[4] += "|r@{$\\,\\pm\\,$}l|c}";
					}
					for (int i=1; i < algorithms; i++)
						table[5] = "\\hline\\hline";
					table[6] = "              &      \\multicolumn{3}{c|}{"+refName+"}";
					for (int i=0; i < A.length-1; i++)
						table[6] += "    &   \\multicolumn{3}{c|}{"+A[i].getName()+"}";
					table[6] += "&   \\multicolumn{3}{c}{"+A[A.length-1].getName()+"}\\\\";
					table[7] = "\\hline";
					for (int i=0; i < functions; i++)
					{
						int k = i + 1;
						table[8+i] = "$f_{"+k+"}$ & \\multicolumn{3}{c|}{$"+avgTab[i][0]+"\\,\\pm\\,"+stdTab[i][0]+"$}";
						for (int n=1; n < algorithms; n++)
							table[8+i] += " & $"+avgTab[i][n]+"$ & $"+stdTab[i][n]+"$ & "+W[n-1][i];
						table[8+i] += "\\\\";
					}
					table[table.length-5] = "\\hline\\hline";
					table[table.length-4] = "\\end{tabular}";
					table[table.length-3] = "\\end{tiny}";
					table[table.length-2] = "\\end{center}";
					table[table.length-1] = "\\end{table}";//\end{table*}				

					this.table(table, "", this.Tables[subE]);
				}
				String tableName = "WILCOXON("+tabNum+")";
				this.mainLatex(this.getTables(), "", tableName);
			}
		}
		else
			System.out.println("At least 2 algorithms are needed fot this test");
	}

	public Vector<Integer> selectAlg(AlgorithmInfo[] alg)
	{
		System.out.println("Select the desired algorithms, then \"c\" to create a table. Press \"a\" to select all the algorithms.");
		String str = new String();
		Vector<Integer> V = new Vector<Integer>();

		for (int i=0; i < alg.length; i++)
			System.out.println("["+i+"] "+alg[i].getName());
		
		boolean exit = false;
		do
		{   
			try 
			{
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(isr);
				str = br.readLine(); System.out.println(" ");
			}
			catch (Exception e) {e.printStackTrace();}
			
			if (str.equals("a"))
			{
				V.clear();
				for (int i=0; i < alg.length; i++)
					V.add(i);
				exit = true;
			}
			else if (str.equals("c"))
			{
				if (V.isEmpty())
					System.out.println("Select at least one algorithm for comparison.");
				else
					exit = true;
			}
			else
			{
				try
				{
					int algIndex = Integer.parseInt(str);
					if (algIndex < alg.length)
						V.add(algIndex);
					else
						System.out.println("Ignored input \"" + str + "\"" + "(allowed values: [a, c, 0, ... " + (alg.length-1) + "])");
				}
				catch (Exception e)
				{
					System.out.println("Ignored input \"" + str + "\"" + "(allowed values: [a, c, 0, ... " + (alg.length-1) + "])");
				}
			}
		}
		while (!exit);
				
		return V;
	}

	public AlgorithmInfo[] select(Vector<Integer> V, AlgorithmInfo[] list)
	{
		AlgorithmInfo[] newList = new AlgorithmInfo[V.size()];
		for (int i=0; i < V.size(); i++)
			newList[i] = list[V.get(i).intValue()];
		return newList;
	}

	public static String compareResults(double[] refDataArray, double[] dataArray, boolean useAdvancedStatistic, double alpha)
	{
		String retVal = "";
		
		if (useAdvancedStatistic)
		{
			// test it the two distributions are normal with the Shapiro-Wilk test (null hypothesis: normal distribution)
			double p1 = StatisticTests.ShapiroWilk(refDataArray, true);
			double p2 = StatisticTests.ShapiroWilk(dataArray, true);

			// if the two distributions are normal
			if (p1 > alpha && p2 > alpha)
			{
				// check homoscedasticity (homogeneity of variances) with the F-test (requires normality, null hypothesis: same variance)
				// other possible homoscedasticity tests (for n>2 samples)
				// - Bartlett (requires normality)
				// - Hartley (requires normality and equal size, accounts for the minimum and the maximum of the variance range)
				// - Cochran's C-test (requires normality, mainly outlier test, accounts for all variances within the range)
				// - Goldfeld-Quandt (requires normality, checks for homoscedasticity in regression analyses)
				// - Levene (does not require normality, uses mean)
				// - Brown-Forsythe (does not require normality, uses median, more robust)
				double p = StatisticTests.FTest(refDataArray, dataArray);

				// (for n>2 samples, use ANOVA)
				if (p > alpha)
				{
					// equal variances -> T-test
					TwoSampleTtest tTest;
					double pValue;

					tTest = new TwoSampleTtest(refDataArray, dataArray, true);
					pValue = tTest.getSP();
					if (pValue > alpha)
					{
						retVal = "=";
					}
					else
					{
						tTest = new TwoSampleTtest(refDataArray, dataArray, H1.LESS_THAN, true);
						pValue = tTest.getSP();
						if (pValue > alpha)
							retVal = "-";
						else
							retVal = "+";
					}
				}
				else
				{
					// unequal variances -> Welch T-test
					TwoSampleTtest tTest;
					double pValue;

					tTest = new TwoSampleTtest(refDataArray, dataArray, false);
					pValue = tTest.getSP();
					if (pValue > alpha)
					{
						retVal = "=";
					}
					else
					{
						tTest = new TwoSampleTtest(refDataArray, dataArray, H1.LESS_THAN, false);
						pValue = tTest.getSP();
						if (pValue > alpha)
							retVal = "-";
						else
							retVal = "+";
					}
				}
			}
			else
			{
				// Mann Whitney U-Test (Wilcoxon Rank-Sum)
				MannWhitneyTest mannWhineyTest;
				double pValue;

				mannWhineyTest = new MannWhitneyTest(refDataArray, dataArray);
				pValue = mannWhineyTest.getSP();

				if (pValue > alpha)
				{
					retVal = "=";
				}
				else
				{
					mannWhineyTest = new MannWhitneyTest(refDataArray, dataArray, H1.LESS_THAN);
					pValue = mannWhineyTest.getSP();
					if (pValue > alpha)
						retVal = "-";
					else
						retVal = "+";
				}
			}
		}
		else
		{
			MannWhitneyTest test = new MannWhitneyTest(refDataArray, dataArray);
			double pValue = test.getSP();

			if (pValue > alpha)
			{
				retVal = "=";
			}
			else
			{
				if (mean(refDataArray) > mean(dataArray))
					retVal = "-";
				else if (mean(refDataArray) < mean(dataArray))
					retVal = "+";
				else
					retVal = "=";
			}
		}
		
		return retVal;
	}
}

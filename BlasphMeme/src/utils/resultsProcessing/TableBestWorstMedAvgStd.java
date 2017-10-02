package utils.resultsProcessing;

public class TableBestWorstMedAvgStd extends TableStatistics
{
	protected double precision = 0.00000001;

	public TableBestWorstMedAvgStd(Experiment experiment)
	{
		this.setExperiment(experiment);
	}
	
	public void execute() throws Exception
	{
		this.experiment.computeAVG();
		this.experiment.computeSTD();
		this.experiment.computeMedian();
		this.experiment.getMax();
		this.experiment.getMin();
		AlgorithmInfo[] AlgList = this.experiment.getAlgorithms();

		int tabNum = 0;
		for (int a = 0; a<AlgList.length; a++)
		{
			tabNum++;
			int subExperiments = AlgList[a].subExperimentsNumber();
			this.setTables(subExperiments);
			DataBox[] data = AlgList[a].getData();

			// for each sub-experiment (benchmark and problem size)
			for (int subE=0; subE < subExperiments; subE++)
			{	
				int dimensions = data[subE].getDimension(); 
				this.Tables[subE] = data[subE].getName() + ".COMPETITION"+"("+tabNum+")";
				int functions = data[subE].getFunctionsNumber();

				double[] AVGs = data[subE].getAVG();
				double[] STDs = data[subE].getSTD();
				double[] MEDIANs = data[subE].getMedian();
				double[] WORSTs = data[subE].getMax();
				double[] BESTs = data[subE].getMin();

				for (int i=0; i < BESTs.length; i++)
					System.out.println(BESTs[i]);
				
				String[] avgTab = new String[functions];
				String[] stdTab = new String[functions];
				String[] medianTab = new String[functions];
				String[] bestTab = new String[functions];
				String[] worstTab = new String[functions];

				if (this.getErrorFlag())
				{
					double[] minima = data[subE].getMinima();
					double temp;	
					for (int i=0; i < functions; i++)
					{
						temp = (double)(AVGs[i] - minima[i]); 
						avgTab[i] = this.D2S(temp); 
						System.out.println("AVG");
						System.out.println(AVGs[i]);
						System.out.println(temp);
						System.out.println(avgTab[i]);
						temp = (double)(STDs[i]);
						stdTab[i] = this.D2S(temp);
						temp = (double)(BESTs[i] - minima[i]);
						bestTab[i] = this.D2S(temp);
						System.out.println("BEST");
						System.out.println(BESTs[i]);
						System.out.println(temp);
						System.out.println(bestTab[i]);
						temp = (double)(WORSTs[i] - minima[i]);
						worstTab[i] = this.D2S(temp);
						temp = (double)(MEDIANs[i] - minima[i]);
						medianTab[i] = this.D2S(temp);
					}
				}
				else
				{
					for (int i=0; i < functions; i++)
					{
						avgTab[i] = this.D2S(AVGs[i]);
						stdTab[i] = this.D2S(STDs[i]);
						bestTab[i] = this.D2S(BESTs[i]);
						worstTab[i] = this.D2S(WORSTs[i]);
						medianTab[i] = this.D2S(MEDIANs[i]);
					}
				}	

				String[] table = new String[7 + 2*functions + 3];
				table[0] = "\\begin{table}"; //\begin{table*}
				table[1] = "\\caption{Results for $"+dimensions+"$D.}\\label{tab:"+dimensions+"}";
				table[2] = "\\begin{center}";
				table[3] = "\\begin{tabular}{|c|c|c|c|c|c|}";
				table[4] = "\\hline";
				table[5] = "$\\mathbf{Func.}$ & $\\mathbf{Best}$ & $\\mathbf{Worst}$ & $\\mathbf{Median}$ & $\\mathbf{Mean}$ & $\\mathbf{Std}$\\\\";
				table[6] = "\\hline";
				int j = 7; int func = 1;
				for (int i=0; i < functions; i++)
				{
					table[j+i] = "$\\mathbf{"+func+"}$ & $"+bestTab[i]+"$ & $"+worstTab[i]+"$ & $"+medianTab[i]+"$ & $"+avgTab[i]+"$ & $"+stdTab[i]+"$\\\\";
					j++;
					table[j+i] = "\\hline";
					func++;
				}
				table[table.length-3] = "\\end{tabular}";
				table[table.length-2] = "\\end{center}";
				table[table.length-1] = "\\end{table}";//\end{table*}

				this.table(table, "", this.Tables[subE]);	
			}
			String tableName = "SINGLETABLE("+tabNum+")";
			this.mainLatex(this.getTables(), "", tableName);
		}
	}
}
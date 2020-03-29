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
package utils.resultsProcessing;

import java.util.Arrays;

import jsc.distributions.Normal;


public class TableHolmBonferroni extends TableStatistics
{	
	public TableHolmBonferroni(Experiment experiment)
	{
		this.setExperiment(experiment);
	}
	
	public void execute() throws Exception
	{
		this.experiment.computeAVG();
		AlgorithmInfo[] A = this.experiment.getAlgorithms();
		int algorithms = A.length;
		this.setTables(1); this.Tables[0] = "holm-bon";
		if (algorithms >= 2)
		{
			int problems = A[0].getProblems();
			double[][] AVGs = new double[algorithms][problems];
			for (int i=0; i < algorithms; i++)
				AVGs[i] = getAVGArray(A[i]);
			double[] averageRank = averageRank(AVGs);

			double[] z = new double[algorithms - 1];
			int ii = 0; 
			for (int i=0; i < algorithms; i++)
			{
				if (i != this.referenceAlgorithm)
				{	
					z[ii] = (averageRank[i] - averageRank[this.referenceAlgorithm])/Math.sqrt((double)(algorithms*(algorithms -1))/(6*problems) );
					ii++;
				}
			}

			Normal N = new Normal(); 		
			double[] p = new double[algorithms - 1]; 
			for (int i=0; i < p.length; i++)
				p[i] = N.cdf(z[i]);

			String[] table = new String[6 + algorithms -1 +3];
			table[0] = "\\begin{table}"; 
			table[1] = "\\caption{Holm-Bonferroni procedure (reference: "+A[this.referenceAlgorithm].getName()+", Rank = "+D2S(averageRank[this.referenceAlgorithm])+")}\\label{holm-test}";
			table[2] = "\\begin{tabular}{c|c|c|c|c|c|c}";
			table[3] = "\\hline\\hline";
			table[4] = "$j$ & Optimizer & Rank & $z_j$ & $p_j$ & $\\alpha/j$ & Hypothesis\\\\";
			table[5] = "\\hline";
			A = this.removeReference(A, this.referenceAlgorithm);
			averageRank = removeReference(averageRank, this.referenceAlgorithm);
			int[] order = this.order(averageRank);
			String hypothesis = new String();

			for (int i=0; i < algorithms -1; i++)
				System.out.println(z[i]); 

			int j = 1; double mem = Double.NaN;
			for (int i=0; i < algorithms -1; i++)
			{
				int index = order[i];

				if (i != 0)
					if (averageRank[index] != mem)
						j++;

				if (p[index] < this.SP/j)
					hypothesis = "Rejected\\\\";
				else
					hypothesis = "Accepted\\\\";

				table[6 + i] = j  + " & " + A[index].getName() + " & " + D2S(averageRank[index]) + " & " + D2S(z[index]) + " & " + D2S(p[index]) + " & "+ D2S(this.SP/j) + " & " + hypothesis;
				mem = averageRank[index];
			}
			table[table.length -3] = "\\hline\\hline";
			table[table.length -2] = "\\end{tabular}";
			table[table.length -1] = "\\end{table}";

			this.table(table, "",this.Tables[0]);
			this.mainLatex(this.getTables(), "", "HOLM-BONFERRONI");
		}
		else
			System.out.println("At least 2 algorithms are needed fot this test");
	}

	public double[] rank(double[] avg)
	{
		int Na = avg.length;
		double[] out = new double[Na];
		double[] sorted = new double[Na];
		for (int i=0; i < Na; i++)
			sorted[i] = avg[i];
		Arrays.sort(sorted);
		int R = Na;
		for (int n=0; n < Na; n++)
		{
			for (int i=0; i < Na; i++)
				if (avg[i] == sorted[n])
					out[i] = R;
			R--;
		}
		return out;	
	}

	public double[] averageRank(double[][] avg)
	{
		int algorithms = avg.length;
		int problems = avg[0].length;
		double[][] ranks = new double[problems][algorithms];
		double[] temp = new double[algorithms];
		double[] out = new double[algorithms];
		for (int i=0; i < problems; i++)
		{
			for (int n=0; n < algorithms; n++)
				temp[n] = avg[n][i];
			ranks[i] = rank(temp);
		}

		for (int i=0; i < algorithms; i++)
			for (int n=0; n < problems; n++)
				out[i] += (ranks[n][i]/problems);
		return out;
	}

	public int[] order(double[] ranks)
	{
		int[] out = new int[ranks.length];
		double[] copy = new double[ranks.length];
		double[] sorted = new double[ranks.length];
		for (int i=0; i < ranks.length; i++)
		{
			sorted[i] = ranks[i];
			copy[i] = ranks[i];
		}
		Arrays.sort(sorted);;
		for (int i=0; i < ranks.length; i++)
		{
			boolean found = false;
			double temp = sorted[i];
			for (int n=0; n < ranks.length && !found; n++)
			{
				if (copy[n] == temp)
				{
					out[ranks.length -1 - i] = n;
					copy[n] = Double.NaN;
					found = true;
				}
			}
		}
		return out;	
	}
}
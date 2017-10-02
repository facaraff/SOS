package utils.resultsProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Vector;

public abstract class TableStatistics
{
	protected Experiment experiment = null;

	protected double SP = 0.05; //significance probability ---> 1 - confidence level
	protected int referenceAlgorithm;
	protected DecimalFormat DF = new DecimalFormat("0.00E00");
	protected String[] Tables = null;

	private boolean errorFlag = false;

	public abstract void execute() throws Exception;	

	public void setTables(int n){this.Tables = new String[n];}
	public String[] getTables(){return this.Tables;}

	public void setExperiment(Experiment experiment){this.experiment = experiment;}
	public Experiment getExperiment(){return this.experiment;}	

	public void setSignificanceProbability(double SP){this.SP = SP;}
	public double getSignificanceProbability(){return this.SP;}	

	public void setReferenceAlgorithm() throws Exception
	{
		AlgorithmInfo[] algorithms = this.experiment.getAlgorithms(); 
		System.out.println("Select the reference algorithm from the list below:");
		for (int i=0; i < algorithms.length; i++)
			System.out.println("["+i+"] "+algorithms[i].getName());
		System.out.println(""); System.out.print("Reference = ");

		try
		{
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String str = br.readLine();
			this.referenceAlgorithm = Integer.parseInt(str);
		}
		catch (Exception e) {e.printStackTrace();}
	}

	public int getReferenceAlgorithm(){return this.referenceAlgorithm;}

	public double[] getAVGArray(AlgorithmInfo A)
	{
		double[] out = new double[A.getProblems()];
		double[] temp = null;
		DataBox[] DB = A.getData();
		int i = 0;
		for (int n=0; n < DB.length; n++)
		{
			temp = DB[n].getAVG();
			for (int k=0; k < temp.length && i < A.getProblems(); k++)
			{
				if(!Double.isNaN(temp[k]))
				{
					out[i] = temp[k];
					i++;
				}
			}
		}
		return out;
	}

	public double[] getSTDArray(AlgorithmInfo A)
	{
		double[] out = new double[A.getProblems()];
		double[] temp = null;
		DataBox[] DB = A.getData();
		int i = 0;
		for (int n=0; n < DB.length; n++)
		{
			temp = DB[n].getSTD();
			for (int k=0; k < temp.length && i < A.getProblems(); k++)
			{
				if(temp[k] != Double.NaN)
				{
					out[i] = temp[k];
					i++;
				}
			}
		}
		return out;
	}

	public AlgorithmInfo[] removeReference(AlgorithmInfo[] list, int ref){return removeAlg(list, ref);}

	public AlgorithmInfo[] removeAlg(AlgorithmInfo[] list, int index)
	{
		AlgorithmInfo[] out = new AlgorithmInfo[list.length -1];
		for (int i=0; i < out.length; i++)
			if(i < index)
				out[i] = list[i];
			else
				out[i] = list[i + 1];
		return out;
	}

	public double[] removeReference(double[] list, int ref)
	{
		double[] out = new double[list.length -1];
		for (int i=0; i < out.length; i++)
			if(i < ref)
				out[i] = list[i];
			else
				out[i] = list[i + 1];
		return out;
	}

	public AlgorithmInfo[] removeAlg(AlgorithmInfo[] list, Vector<Integer> V)
	{
		AlgorithmInfo[] out = new AlgorithmInfo[list.length - V.size()];

		for (int i=0; i < V.size(); i++)
			list[V.get(i).intValue()] = null;
		int c =0;
		for (int i=0; i < list.length; i++)
		{
			if(list[i] != null)
			{
				out[c] = list[i];
				c++;
			}
		}

		return out;
	}

	public int indexMin(double[] x, double[] y)
	{
		double min = x[0];
		double smin = y[0];
		int index = 0;
		for (int i=1; i < x.length; i++)
		{
			if(x[i] < min)
			{
				min = x[i];
				smin = y[i];
				index = i;
			}
			else if(x[i] == min)
			{
				if(y[i] < smin)
				{
					min = x[i];
					index = i;
				}
				else if(y[i] == smin)
				{
					//XXX (fabio) handle results with same mean/std 
				}
			}
		}
		return index;
	}

	public String D2S(double value) throws Exception
	{
		String str;
		if(Double.isNaN(value))
			str = "";
		else
		{	
			str = this.DF.format(value).toLowerCase();
			if (!str.contains("e-"))  
				str = str.replace("e", "e+");
		}
		return str;
	}

	public void setFormat(String format){this.DF = new DecimalFormat(format);}

	public void table(String[] table, String location, String name) throws Exception
	{
//		String Dir = "./results/"; 
		String Dir = "./tables/"; 
		boolean success = (new File(Dir)).mkdir();
		Dir += location; 
		success = (new File(Dir)).mkdir();
		if(success)
			System.out.println("Path: " + Dir + " has been created");

		File file = new File(Dir + "/"+name+".tex");
		// if file doesn't exists, then create it
		if (!file.exists()) 
			file.createNewFile();

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i=0; i < table.length; i++)
		{
			bw.write(table[i]);
			bw.newLine();
		}
		bw.close();

		System.out.println("Find your table in: " + Dir);
	}

	public void mainLatex(String[] tables, String location, String mainName) throws Exception
	{
		String[] str = new String[10 + tables.length + 1];
		str[0] = "\\documentclass[a4paper,12pt]{article}";
		str[1] = "\\usepackage[latin1]{inputenc}";
		str[2] = "\\usepackage[T1]{fontenc}";
		str[3] = "\\usepackage{lmodern}";
		str[4] = "\\usepackage{graphicx}";
		str[5] = "\\usepackage[landscape,margin=1cm]{geometry}";
		str[6] = "\\usepackage{relsize}";
		str[7] = "\\usepackage{array}";
		str[8] = "%\\usepackage{slashbox}";
		str[9] = "\\begin{document}";
		for (int i=0; i < tables.length; i++)
			str[10+i] = "\\InputIfFileExists{"+tables[i]+".tex}{}{}";
		str[str.length-1] = "\\end{document}";

//		String Dir = "./results/"; 
		String Dir = "./tables/"; 
		boolean success = (new File(Dir)).mkdir();
		Dir += location; 
		success = (new File(Dir)).mkdir();
		if(success)
			System.out.println("Path: " + Dir + " has been created");
		mainName +=".tex";
		File file = new File(Dir + "/"+mainName);
		// if file doesn't exists, then create it
		if (!file.exists()) 
			file.createNewFile();

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i=0; i < str.length; i++)
		{
			bw.write(str[i]);
			bw.newLine();
		}
		bw.close();

		this.getPDF(mainName);
	}

	public void getPDF(String str) throws Exception
	{
		File file = new File("./compileLatex.sh");
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
//		bw.write("cd results");
		bw.write("cd tables");
		bw.newLine();
		bw.write("pdflatex \""+str+"\"");
		bw.close();

		Process p = Runtime.getRuntime().exec("sh compileLatex.sh");
		p.waitFor();
		Runtime.getRuntime().exec("rm compileLatex.sh"); 
	}

	public boolean getErrorFlag(){return this.errorFlag;}
	public void setErrorFlag(boolean error){this.errorFlag = error;}
}
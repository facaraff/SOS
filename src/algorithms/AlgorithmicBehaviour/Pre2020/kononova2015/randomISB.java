package algorithms.AlgorithmicBehaviour.Pre2020.kononova2015;


import java.text.DecimalFormat;

import utils.random.RandUtils;


import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class randomISB extends AlgorithmBias
{
	static String Dir = "/home/fabio/Desktop/kylla/random";
	
	private String file;
	
	public void setFile()
	{
		this.file = null;
	}
	
	public String getFile()
	{
		return this.file;
	}
	
	public void delFile()
	{
		this.file = null;
	}
	
	int INDEX;
	public void setINDEX(int ind)
	{
		this.INDEX = ind;
	}
	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{	
		this.file = new String();

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		File Ffile = new File(Dir+"/RAND"+(INDEX+1)+".txt");
		//File file = new File(Dir+"/"+ga.getClass().getSimpleName()+"p"+ga.pullParameter("p0").intValue()+"D"+problemDimension+"f0-"+(i+1)+".txt");
		
		if (!Ffile.exists()) 
			Ffile.createNewFile();
		FileWriter fw = new FileWriter(Ffile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		//bw.write(simpga.getFile());
		
	    //simpga.delFile();
	    //ga.delFile();
		
		
		
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);
		//String line = "# dim "+problemDimension+" pop "+populationSize+" mt "+md+" SEED  "+seed+"\n";
		String line =""+seed;
		
		bw.write(line);
		
		
		double[] best = new double[problemDimension];
								
		int i =0;
		while (i < 100000)
		{
			 
           
			bw.write("\n");
			bw.write(""+RandUtils.uniform(0, 1));
			i++;
			//xChild[n] += RandUtils.gaussian(0, md*(bounds[n][1]-bounds[n][0]));
			//rand = RandUtils.randomInteger(populationSize-1)			
			//line =""+newID+" "+indexParent1+" "+indexParent2+" "+formatter(fChild)+" "+i+" "+indexRand;
				
		}
				
		bw.write(file);
		bw.close();
	
		
		finalBest = best;
		
		FT.add(1, 666);
		
		return FT;
	}
	
	public String formatter(double value)
	{
		String str =""+value;
		str = this.DF.format(value).toLowerCase();
		if (!str.contains("e-"))  
			str = str.replace("e", "e+");
		return str;
	}
	

	
	
}

package algorithms.specialOptions.BIAS.singleSol;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import static utils.MatLab.norm2;
import static utils.MatLab.columnXrow;
import static utils.MatLab.eye;
import static utils.MatLab.multiply;
import static utils.MatLab.sum;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Arrays;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;

import utils.RunAndStore.FTrend;

/*
 * Covariance Matrix Adaptation Evolutionary Strategy (1+1)
 */
public class CMAES11 extends Algorithm
{
private int run = 0;	
private char correction = 0;
//private char correction;
	
	public void setRun(int r)
	{
		this.run = r;
	}
	
	public void setCorrection(char c)
	{
		this.correction = c;
	}
	
	static String Dir = "/home/facaraff/Desktop/KONODATA/SINGLESOLUTION/";


	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double p_target_succ = getParameter("p0").doubleValue(); // 2/11
		double c_p = getParameter("p1").doubleValue(); // 1/12
		double p_thresh = getParameter("p2").doubleValue();// 0.44
		double sigma0 = getParameter("p3").doubleValue();// 1 --> problem dependent!!

//		char correctionStrategy = 'd';  // t --> toroidal   s-->saturation
		char correctionStrategy = this.correction;  // t --> toroidal   s-->saturation
		String fileName = "1p1CMAES"+correctionStrategy; 

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double d = 1+problemDimension/2;
		double c_cov = 2/(Math.pow(problemDimension,2)+6);
		double c_a = Math.sqrt(1-c_cov);
		//double c_c = 2/(problemDimension+2); 
		double sigma=sigma0;
		double p_succ_sign = p_target_succ;
		int lambda_succ;
		//offspring
		double[] x_offspring; //= new double[problemDimension];
		double f_offspring;

		
		fileName+="D"+problem.getDimension()+"f0-"+(run+1);
		File file = new File(Dir+"CMAES11/"+fileName+".txt");
		if (!file.exists()) 
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		int i = 0;
		int prevID = -1;
		int newID = 0;
		int ciccio = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);	
		String line = "# function 0 dim "+problemDimension+" p_target_succ "+p_target_succ+" c_p "+c_p+" p_thresh "+p_thresh+" sigma0 "+sigma0+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
		bw.write(line);
		line = null;
		line = new String();
		prevID = newID;
		
		//compute and evaluate initial solution
		double[] x_parent = generateRandomSolution(bounds, problemDimension);
		double f_parent = problem.f(x_parent);
		FT.add(0, f_parent);
		newID++; i++;
		

		line =""+newID+" "+formatter(f_parent)+" "+i+" "+prevID;
		for(int n = 0; n < problemDimension; n++)
			line+=" "+formatter(x_parent[n]);
		line+="\n";
		bw.write(line);
		line = null;
		line = new String();
		
		double[] z;
		double[][] A = eye(problemDimension);
		double[] Az;

//		int improvements=0;
		while (i < maxEvaluations)
		{	
			z=newZ(problemDimension);
			Az = multiply(A,z);
			x_offspring = sum(x_parent,multiply(sigma,Az));
			//x_offspring = toro(x_offspring, bounds);
			double[] output = new double[problemDimension];
			if(correctionStrategy == 't')
			{
				//System.out.println("TORO");
				output = toro(x_offspring, bounds);
			}
			else if(correctionStrategy== 's')
			{
				//System.out.println("SAT");
				output = saturation(x_offspring, bounds);
			}
			else if(correctionStrategy== 'd')
			{
				output = toro(x_offspring, bounds);
				if(!Arrays.equals(output, x_offspring))
					output = x_parent;
			}
			else if(correctionStrategy== 'e')
			{
				output = toro(x_offspring, bounds);
			}
			else
				System.out.println("No bounds handling shceme seleceted");
			
			if(!Arrays.equals(output, x_offspring))
			{
				x_offspring = output;
				output = null;
				ciccio++;
			}
			
			if( (correctionStrategy== 'e') && (!Arrays.equals(output, x_offspring)) )
				f_offspring = 2;
			else
				f_offspring=problem.f(x_offspring);
			
				

			i++;
//			newID++;
			
			if(f_offspring <= f_parent)
				lambda_succ=1;
			else
				lambda_succ=0;
			//update step size procedure
			p_succ_sign = (1-c_p)*p_succ_sign + c_p*lambda_succ;
			sigma = sigma*Math.exp( (1/d)*(p_succ_sign-(p_target_succ/(1-p_target_succ))*(1-p_succ_sign)) );
			
			if(f_offspring <= f_parent)
			{
				newID++;
				
				f_parent=f_offspring;
				for(int dim=0;dim<problemDimension;dim++)
					x_parent[dim]=x_offspring[dim];
				//improvements++;
				//if(improvements%10==0)
					FT.add(i, f_parent);
				//update cholesky
				if(p_succ_sign < p_thresh)
					A=updateCholesky(A,z,c_a);
				
				line =""+newID+" "+formatter(f_parent)+" "+i+" "+prevID;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(x_parent[n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				prevID = newID;
			}
			//i++;
		}

		finalBest = x_parent;
				
	
		FT.add(i, f_parent);
		bw.close();
		
		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations);

		return FT;
	}

	public double[] newZ(int dimension)
	{
		double[] x = new double[dimension];
		for(int i=0;i<dimension;i++)
			x[i] = RandUtils.gaussian(0, 1);
		return x;
	}

	public  double[][] updateCholesky(double[][] A, double[] z, double c_a)
	{   
		//scalars factors
		double z2 = norm2(z); z2=z2*z2; 
		double ca2 = c_a*c_a;
		double factor = (c_a / z2)*(Math.sqrt(1+((1-ca2)*z2)/ca2)-1); 
		//System.out.println(factor);
		//matrix A*z*z'
		double[][] temp = columnXrow(multiply(A,z),z); 
		return sum( multiply(c_a,A) , multiply(factor,temp) );
	}
	
	public String formatter(double value)
	{
		String str =""+value;
		str = this.DF.format(value).toLowerCase();
		if (!str.contains("e-"))  
			str = str.replace("e", "e+");
		return str;
	}
	
	
	public double[] saturation(double[] x, double[][] bounds)
	{
		double[] xs = new double[x.length];
		for(int i=0; i<x.length; i++)
		{
			if(x[i]>bounds[i][1])
				xs[i] = bounds[i][1];
			else if(x[i]<bounds[i][0])
				xs[i] = bounds[i][0];
			else
				xs[i] = x[i];
		}		
		return xs;
	}
	
	
	public void wrtiteCorrectionsPercentage(String name, double percentage) throws Exception
	{
		File f = new File(Dir+"correctionsSingleSol.txt");
		if(!f.exists()) 
			f.createNewFile();
		FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write(name+" "+percentage+"\n");
		BW.close();
	}

}
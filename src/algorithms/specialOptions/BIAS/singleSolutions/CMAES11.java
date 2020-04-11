package algorithms.specialOptions.BIAS.singleSolutions;

import static utils.algorithms.operators.ISBOp.generateRandomSolution;
import static utils.MatLab.norm2;
import static utils.MatLab.columnXrow;
import static utils.MatLab.eye;
import static utils.MatLab.multiply;
import static utils.MatLab.sum;


import utils.random.RandUtilsISB;
import interfaces.AlgorithmBias;
import interfaces.Problem;

import utils.RunAndStore.FTrend;
import utils.algorithms.Counter;

/*
 * Covariance Matrix Adaptation Evolutionary Strategy (1+1)
 */
public class CMAES11 extends AlgorithmBias
{

	public CMAES11(char c) {this.correction = c;}

//	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double p_target_succ = getParameter("p0").doubleValue(); // 2/11
		double c_p = getParameter("p1").doubleValue(); // 1/12
		double p_thresh = getParameter("p2").doubleValue();// 0.44
		double sigma0 = getParameter("p3").doubleValue();// 1 --> problem dependent!!

//		char correctionStrategy = 'd';  // t --> toroidal   s-->saturation
		char correctionStrategy = this.correction;  // t --> toroidal   s-->saturation
	
		
		//String fileName = "1p1CMAES"+correctionStrategy; 

		String FullName = getFullName("1p1CMAES"+correctionStrategy,problem); 
		Counter PRGCounter = new Counter(0);
		createFile(FullName);
		
		
		
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

		
		int i = 0;
		int prevID = -1;
		int newID = 0;
		
//		RandUtilsISB.setSeed(seed);	
		writeHeader(" p_target_succ "+p_target_succ+" c_p "+c_p+" p_thresh "+p_thresh+" sigma0 "+sigma0, problem);
		
//		fileName+="D"+problem.getDimension()+"f0-"+(run+1);
////		File file = new File(Dir+"CMAES11/"+fileName+".txt");
//		File file = new File(Dir+fileName+".txt");
//		if (!file.exists()) 
//			file.createNewFile();
//		FileWriter fw = new FileWriter(file.getAbsoluteFile());
//		BufferedWriter bw = new BufferedWriter(fw);
		

//		int ciccio = 0;
//		long seed = System.currentTimeMillis();
//		RandUtilsISB.setSeed(seed);	
//		String line = "# function 0 dim "+problemDimension+" p_target_succ "+p_target_succ+" c_p "+c_p+" p_thresh "+p_thresh+" sigma0 "+sigma0+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
//		bw.write(line);
		
		String line = new String();
		
		
		//compute and evaluate initial solution
		double[] x_parent = generateRandomSolution(bounds, problemDimension, PRGCounter);
		double f_parent = problem.f(x_parent);
		FT.add(0, f_parent);
		newID++; i++;
		

		line =""+newID+" "+formatter(f_parent)+" "+i+" "+prevID;
		for(int n = 0; n < problemDimension; n++)
			line+=" "+formatter(x_parent[n]);
		line+="\n";
		bw.write(line);
		
		prevID = newID;
		line = null;
		line = new String();
		
		double[] z;
		double[][] A = eye(problemDimension);
		double[] Az;

		
		this.numberOfCorrections = 0;
		
//		int improvements=0;
		while (i < maxEvaluations)
		{	
			z=newZ(problemDimension, PRGCounter);
			Az = multiply(A,z);
			x_offspring = sum(x_parent,multiply(sigma,Az));

			x_offspring = correct(x_offspring, x_parent, bounds);
			f_offspring=problem.f(x_offspring);
			i++;

			
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
		
//		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, "correctionsSingleSol");
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(), "correctionsSingleSol");

		return FT;
	}

	public double[] newZ(int dimension, Counter counter)
	{
		double[] x = new double[dimension];
		for(int i=0;i<dimension;i++)
			x[i] = RandUtilsISB.gaussian(0, 1, counter);
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

}
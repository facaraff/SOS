package algorithms.specialOptions.BIAS.corrections;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Arrays;

import utils.random.RandUtils;
import utils.algorithms.cmaes.CMAEvolutionStrategy;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;


import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Covariance Matrix Adaptation Evolutionary Strategy 
 */
public class CMAES extends Algorithm
{
	
private int run = 0;
	
	public void setRun(int r)
	{
		this.run = r;
	}
	
	static String Dir = "/home/facaraff/Desktop/KONODATA/CMAES-disc/";
//	static String Dir = "/home/facaraff/Desktop/KONODATA/CMAES-pen/";
//	static String Dir = "/home/facaraff/Desktop/CMAES";
	//static String Dir = "/home/fabio/Desktop/kylla/CMAES";
	//static String Dir = "/home/fabio/Desktop/kylla/CMAES-SAT";
	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");

	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		double[] best = new double[problemDimension];
		if (initialSolution != null)
			best = initialSolution;
		else
			best = generateRandomSolution(bounds, problemDimension);
		double fBest = Double.NaN;
		
		
		int populationSize = getParameter("p0").intValue(); 
		
		

		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.parameters.setPopulationSize(populationSize);
		cma.setRepairMatrix(true);				// repair ill-conditioned matrix
		cma.setDimension(problemDimension);		// overwrite some loaded properties
		cma.setInitialX(best);					// in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.2);	// also a mandatory setting 
		cma.options.verbosity = -1;
		cma.options.writeDisplayToFile = -1;
		String s = new String();
		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();
		int[] rank = new int[populationSize];
		for(int p=0;p<populationSize; p++)
			rank[p]=-1;
		//System.out.println(cma.getDataC());
		
		char correctionStrategy = 'd';  // t --> toroidal   s-->saturation  'e'--> penalty 'd'--->discard
		String fileName = "CMAES"+correctionStrategy+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1)+".txt";
		File file = new File(Dir+"/"+fileName);
		if (!file.exists()) 
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);
		//String line = "# function 0 dim "+problemDimension+" pop "+populationSize+" max_evals "+maxEvaluations+" SEED  "+seed+" mu "+cma.parameters.getMu()+" mu_eff "+cma.parameters.getMueff()+" mu_cov "+cma.parameters.getMucov()+" c_cov "+cma.parameters.getCcov()+" c_c "+cma.parameters.getCc()+" d_sigma "+cma.parameters.getDamps()+"\n";
		String line = "# function 0 dim "+problemDimension+" pop "+populationSize+" max_evals "+maxEvaluations+" SEED  "+seed+" mu "+cma.parameters.getMu()+" mu_eff "+formatter(cma.parameters.getMueff())+" mu_cov "+formatter(cma.parameters.getMucov())+" c_cov "+formatter(cma.parameters.getCcov())+" c_c "+formatter(cma.parameters.getCc())+" d_sigma "+formatter(cma.parameters.getDamps())+"\n";
		s+=line;
		//System.out.println(s);
		bw.write(s);
		s = null;
		s = new String();
		//bw.close();
		
		
		// iteration loop
		int g = 1;
		int j = 0;
		int ciccio = 0; 
		double[] previousFit = new double[populationSize]; 
		
//		double[][] previousPop = cma.samplePopulation();
//		if(correctionStrategy == 'd')
//		{
//			for(int p=0;p<populationSize;p++)
//			{
//				previousPop[p] = toro(previousPop[p], bounds);
//				previousFit[p] = problem.f(previousPop[p]);
//				System.out.println(previousFit[p]);
//			}
//		}
//		else
//		{
//			previousFit = null;
//			previousPop = null;
//		}
		
		
		
		double[][] previousPop = new double[populationSize][problemDimension];
		if(correctionStrategy == 'd')
		{
			for(int p=0;p<populationSize;p++)
			{
				previousPop[p] = generateRandomSolution(bounds, problemDimension);
				previousFit[p] = problem.f(previousPop[p]);
				//System.out.println(previousFit[p]);
			}
		}
		else
		{
			previousFit = null;
			previousPop = null;
		}
		
		
		while (j < maxEvaluations)
		{
            // --- core iteration step ---
			// get a new population of solutions
			double[][] pop = cma.samplePopulation();
			
			for(int i = 0; i < pop.length && j < maxEvaluations; ++i)
			{ 
				// saturate solution inside bounds 
				//pop[i] = toro(pop[i], bounds);//RIMPIAZZZA SAT KONO
				//pop[i] = saturation(pop[i],bounds);
				
				// compute fitness/objective value	
				//fitness[i] = problem.f(pop[i]);
				double[] output = new double[problemDimension];
				if(correctionStrategy == 't')
				{
					//System.out.println("TORO");
					output = toro(pop[i], bounds);
					
					if(!Arrays.equals(output, pop[i]))
					{
						pop[i] = output;
						ciccio++;
					}
					fitness[i] = problem.f(pop[i]);
				}
				else if(correctionStrategy== 's')
				{
					//System.out.println("SAT");
					output = saturation(pop[i], bounds);
					
					if(!Arrays.equals(output, pop[i]))
					{
						pop[i] = output;
						ciccio++;
					}
					fitness[i] = problem.f(pop[i]);
				}
				else if(correctionStrategy== 'e')
				{
					output = saturation(pop[i], bounds);
					if(!Arrays.equals(output, pop[i]))
					{
						ciccio++;
						fitness[i] = 2;
					}
				}
				else if(correctionStrategy == 'd')
				{
					//System.out.println("madonna berra");
					output = toro(pop[i], bounds);
//					if(!Arrays.equals(output, pop[i]))
					if(!equal(output, pop[i]) || infinity(pop[i]))
					{
						ciccio++;
//						for(int n=0;n<problemDimension; n++)
//							System.out.print(previousPop[i][n]+" "); System.out.println("");
							for(int n=0;n<problemDimension; n++)
								pop[i][n]=previousPop[i][n];
						fitness[i] = previousFit[i];
					}
					else
					{
						//System.out.println(infinity(pop[i]));
						for(int n=0;n<problemDimension; n++)
							previousPop[i][n]=pop[i][n];
					}

				}
				else
					System.out.println("No bounds handling shceme seleceted");
				// save best
				if (j == 0 || fitness[i] < fBest)
				{
					fBest = fitness[i];
					for (int n = 0; n < problemDimension; n++)
						best[n] = pop[i][n];
					FT.add(j, fBest);
				}
				
				j++;
			}
			
			double[] sortedFitness = new double[populationSize];
			for(int p=0;p<populationSize;p++)
				sortedFitness[p]=fitness[p];
			Arrays.sort(sortedFitness);
			
			for(int p=0;p<populationSize;p++)
			{
				newID++;
				line =""+newID+" 0 0 "+formatter(fitness[p])+" "+rank(p,fitness[p], sortedFitness)+" "+g;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(pop[p][n]);
				line+="\n";
				//s+=line;
				bw.write(line);
			}
			
			g++;
			
			// pass fitness array to update search distribution
			cma.updateDistribution(fitness);
			
			//bw.write(s);
			//s = null;
			//s = new String();
			
//			if(correctionStrategy=='d')
//			{
//				for(int p=0;p<populationSize;p++)
//					for(int n=0;n<problemDimension;n++)
//						previousPop[p][n]=pop[p][n];
//				//previousPop = pop;
//				//pop = null;
//				for(int p=0;p<populationSize;p++)
//					previousFit[p] = fitness[p];
//				
//			}
			
		}
		
		bw.close();
		
		finalBest = best;

		FT.add(j, fBest);
		
		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, fitness, correctionStrategy);
		return FT;
	}
	
	public int rank(int index, double fit, double[] sorted)
	{   
		for(int i=0; i < sorted.length; ++i)
			if(fit <= sorted[i])
				return i+1;
		return -1;
	}
	

	
	public String formatter(double value)
	{
		String str =""+value;
		str = this.DF.format(value).toLowerCase();
		if (!str.contains("e-"))  
			str = str.replace("e", "e+");
		return str;
	}
	
//	public int[] getIndices(int popSize)
//	{
//		int[] indices = new int[popSize];
//		for(int n=0; n<popSize; n++)
//			indices[n] = n;
//		return indices;
//	}
	
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
		File f = new File(Dir+"corrections.txt");
		if(!f.exists()) 
			f.createNewFile();
		FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write(name+" "+percentage+"\n");
		BW.close();
	}
	
	public void wrtiteCorrectionsPercentage(String name, double percentage, double[] finalFitnesses, char boundaHendler) throws Exception
	{
		if(boundaHendler != 'e')
			wrtiteCorrectionsPercentage(name, percentage);
		else
		{
			int counter = 0;
			for(int n=0; n<finalFitnesses.length; n++)
				if(finalFitnesses[n]==2)
					counter++;
			File f = new File(Dir+"corrections.txt");
			if(!f.exists()) 
				f.createNewFile();
			FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
			BufferedWriter BW = new BufferedWriter(FW);
			BW.write(name+" "+percentage+" "+formatter((double)counter/finalFitnesses.length)+"\n");
			BW.close();
		}
	}
	
	public boolean equal(double[] A, double[] B)
	{
		boolean equal = true;
		
		for(int n=0;n<A.length;n++)
			if(A[n]!=B[n])
				equal = false;
		
		return equal;
	}
	
	public boolean infinity(double[] x)
	{
		boolean inf = false;
		for(int n=0;n<x.length;n++)
			if(x[n]==Double.NEGATIVE_INFINITY || x[n]==Double.POSITIVE_INFINITY)
				inf =true;
		return inf;
	}
	
}
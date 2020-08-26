package algorithms.specialOptions.BIAS;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Corrections.torus;
import static utils.algorithms.Corrections.saturate;
import static utils.algorithms.Corrections.discardAndResample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Arrays;

import utils.random.RandUtils;

import utils.algorithms.cmaes.CMAEvolutionStrategy;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * Covariance Matrix Adaptation Evolutionary Strategy 
 */
public class CMAES extends AlgorithmBias
{
	

	

	
//	public void setRun(int r)
//	{
//		this.run = r;
//	}
	
	public void setCorrectionStrategy(char c) {this.setCorrection(c);}
	
	public CMAES() {super();}
	public CMAES(char c) {super(); this.correction = c;}
	
	
	
	static String Dir = "/home/facaraff/kylla/CMAES";
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
		
		
		File file = new File(Dir+"/CMAES"+this.correction+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1)+".txt");
//		File file = new File(Dir+"/CMAES"+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1)+".txt");
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
		while (j < maxEvaluations)
		{
            // --- core iteration step ---
			// get a new population of solutions
			double[][] pop = cma.samplePopulation();
			
			for(int i = 0; i < pop.length && j < maxEvaluations; ++i)
			{ 
				// saturate solution inside bounds 
				pop[i] = correction(pop[i], bounds);//RIMPIAZZZA SAT KONO
				//pop[i] = saturation(pop[i],bounds);
				
				// compute fitness/objective value	
				fitness[i] = problem.f(pop[i]);
				
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
		}
		
		bw.close();
		
		finalBest = best;

		FT.add(j, fBest);
		
		
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
	
	public double[] correction (double[] x, double[][] bounds)
	{
		if(correction=='t') x = torus(x,bounds);
		else if(correction=='s') x = saturate(x,bounds);
		else if(correction=='d') x = discardAndResample(x,bounds);
		return x;
	}
	
}
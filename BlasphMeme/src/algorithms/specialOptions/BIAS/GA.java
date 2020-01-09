package algorithms.specialOptions.BIAS;


import java.text.DecimalFormat;


import utils.random.RandUtils;
import static utils.MatLab.indexMax;
import static utils.MatLab.subtract;
import static utils.MatLab.dot;
import static utils.MatLab.sum;


import static utils.algorithms.Misc.generateRandomSolution;


import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;


public class GA extends AlgorithmBias
{
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
	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{	
		this.file = new String();
		int populationSize = getParameter("p0").intValue(); 
		double nt =  getParameter("p1").intValue();
		double md = getParameter("p2").doubleValue();
		double d = getParameter("p3").doubleValue();
			
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		int[] ids = new int[populationSize];
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);
		String line = "# dim "+problemDimension+" pop "+populationSize+" nt "+nt+" mt "+md+" SEED  "+seed+"\n";
		this.file+=line;
		
		
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		int parent1 = 0;
		int parent2 = 0; 
		int worst = 0;
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			i++;
			newID++;
			ids[j] = newID;
			line =""+newID+" -1 "+"-1 "+formatter(fitnesses[j])+" "+i+" -1";
			for(int n = 0; n < problemDimension; n++)
				line+=" "+formatter(population[j][n]);
			line+="\n";
			this.file+=line;
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
			} 
				
		}
							
		// iterate
		while (i < maxEvaluations)
		{
			int[] indices = getIndices(populationSize);
			indices = RandUtils.randomPermutation(indices); 
			if(fitnesses[indices[0]] < fitnesses[indices[1]])
				parent1 = indices[0];
			else
				parent1 = indices[1];
			indices = RandUtils.randomPermutation(indices); 
			if(fitnesses[indices[0]] < fitnesses[indices[1]])
				parent2 = indices[0];
			else
				parent2 = indices[1];
			
			double[] alpha = new double[problemDimension];
			for(int n=0; n<problemDimension; n++)
				alpha[n] = RandUtils.uniform(-d,1+d);
			
			double[] xChild = sum(population[parent1],dot(alpha, subtract(population[parent2],population[parent1])));
			
			for(int n=0; n<problemDimension; n++)
				xChild[n] += RandUtils.gaussian(0, md*(bounds[n][1]-bounds[n][0]));
			//xChild = saturateToro(xChild, bounds);
			xChild = saturation(xChild, bounds);
			double fChild = problem.f(xChild);
			i++;
			
			worst = indexMax(fitnesses);
			if(fChild<fitnesses[worst])
			{
				newID++;
				int indexWorst = ids[worst];
				int indexParent1 = ids[parent1];
				int indexParent2 = ids[parent2];
				ids[worst] = newID;
				for(int n=0; n<problemDimension; n++)
					population[worst][n] = xChild[n];
				fitnesses[worst] = fChild;
				
				
				line =""+newID+" "+indexParent1+" "+indexParent2+" "+formatter(fChild)+" "+i+" "+indexWorst;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(xChild[n]);
				line+="\n";
				this.file+=line;
			}
				
			
		}	
		
		finalBest = best;
		
		FT.add(i, fBest);
		
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
	
	public int[] getIndices(int popSize)
	{
		int[] indices = new int[popSize];
		for(int n=0; n<popSize; n++)
			indices[n] = n;
		return indices;
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
	
}

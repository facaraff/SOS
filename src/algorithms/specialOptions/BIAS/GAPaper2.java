package algorithms.specialOptions.BIAS;

import static utils.algorithms.operators.GAOp.roulette;
import static utils.algorithms.Misc.generateRandomSolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;

import utils.algorithms.Corrections;

import utils.random.RandUtils;
import static utils.MatLab.indexMin;
import static utils.MatLab.indexMax;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

public class GAPaper2 extends AlgorithmBias
{	
	private int run = 0;
	
	public void setRun(int r)
	{
		this.run = r;
	}
	
	int ciccio = 0;
	static String Dir = "/home/facaraff/Desktop/KONODATA/GA/";

	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{	
		int populationSize = getParameter("p0").intValue(); 
		double tournamentProb =  getParameter("p1").doubleValue();
		double std = getParameter("p2").doubleValue();
		char selectionStrategy = 't';  // r --> fitness proportional roulette wheel  t-->stochastic tournament 
		char crossoverStrategy = 'a'; //d-->discrete  a-->full arithmetic
		char mutationStrategy = 'g';  // c --> Cauchy   g-->Gaussian 
		char correctionStrategy = 'e';  // t --> toroidal   s-->saturation d--->discard e--->penalty e---> penalty
		String fileName = "GA"+mutationStrategy+crossoverStrategy+selectionStrategy+correctionStrategy; 


		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		int[] ids = new int[populationSize];
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		
		
//		long seed = System.currentTimeMillis();
//		RandUtils.setSeed(seed);
//		String line = "# dim "+problemDimension+" pop "+populationSize+" nt "+nt+" mt "+md+" SEED  "+seed+"\n";
//		this.file+=line;
		
		
		fileName+="p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1);
		File file = new File(Dir+fileName+".txt");
		if (!file.exists()) 
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);	
		String line =""; 
		if(mutationStrategy=='t') line+="# dim "+problemDimension+" pop "+populationSize+" prob "+tournamentProb;
		else line+="# dim "+problemDimension+" pop "+populationSize;
		if(mutationStrategy=='g') line+=" std "+std+" SEED "+seed+"\n";
		else line+=" scale "+std+" SEED "+seed+"\n";
		bw.write(line);
		line = null;
		line = new String();
		//bw.close();
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		

		int i = 0;
		int parent1 = 0;
		int parent2 = 0; 
		int worst = 0;
		int ciccio = 0;

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
			bw.write(line);
			line = null;
			line = new String();
			
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
			
			parent1 = parentSelectionR0rT( selectionStrategy, fitnesses, tournamentProb);
			parent2 = parentSelectionR0rT( selectionStrategy, fitnesses, tournamentProb);
			double[] child =  DorAxover(population[parent1], population[parent2], crossoverStrategy);
			child = GorCmutations(child, mutationStrategy, std);
 			
			double[] temp = saturateAndEvaluate(correctionStrategy, child, problem, bounds);
			double fChild = temp[0]; 
			ciccio+=temp[1];
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
					population[worst][n] = child[n];
				fitnesses[worst] = fChild;
				
				
				line =""+newID+" "+indexParent1+" "+indexParent2+" "+formatter(fChild)+" "+i+" "+indexWorst;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(child[n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
			}
				
			
		}	
		
		int ib = indexMin(fitnesses);
		
		finalBest = population[ib];
		fBest = fitnesses[ib];
		FT.add(i, fBest);
		
		FT.add(i, fBest);
		bw.close();
		
		wrtiteCorrectionsPercentage(fileName, (double) ciccio/maxEvaluations, fitnesses, correctionStrategy);
		
		
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
	
	public double[] GorCmutations(double[] point, char mutation, double std)
	{
		double[] output = new double[point.length];
		if(mutation=='g')
			for(int i = 0; i<point.length; i++)
				output[i]=point[i]+RandUtils.gaussian(0, std);
		else if(mutation=='c')
			for(int i = 0; i<point.length; i++)
				output[i]=point[i]+RandUtils.cauchy(0, std);
		else
			System.out.println("FUCK OFF");
		return output;
	}
	
	public double[] DorAxover(double[] parentA, double[] parentB, char xover)
	{
		double[] output = new double[parentA.length];
		
		if(xover=='d') //discrete recombination
			for(int i=0;i<parentA.length;i++)
				output[i]=(RandUtils.random()<0.5)? parentA[i]:parentB[i];
		else if(xover=='a')// WHOLE ARITHMETIC RECOMBINATION
			for(int i=0;i<parentA.length;i++)
			{
				double alpha = RandUtils.uniform(0, 1);
				output[i]=alpha*parentA[i]+(1-alpha)*parentB[i];
				
			}
		else
			System.out.println("NO ADMISSIBLE OXVER SELECTED");
		
		return output;
	}
	
	
	public int parentSelectionR0rT(char selStrategy, double[] fitness, double tournamentProb)
	{
		int index = -1;
		
		if(selStrategy=='r')
		{
			double sum = 0;
			for(int i=0;i<fitness.length;i++)
				sum+=fitness[i];
			double[] prob = new double[fitness.length];
			for(int i=0;i<fitness.length;i++)
				prob[i] = fitness[i]/sum;
			index = roulette(prob);
		}
		else if(selStrategy=='t')
		{
//			int[] ind = new int[fitness.length];
//			for(int i=0;i<fitness.length;i++)
//				ind[i]=i;
			int[] ind = getIndices(fitness.length);
			ind=RandUtils.randomPermutation(ind);
			int ind1 = (fitness[ind[0]]<fitness[ind[1]])? ind[0] : ind[1];
			int ind2 = (fitness[ind[0]]<fitness[ind[1]])? ind[1] : ind[0];
			index = (RandUtils.random()<tournamentProb)? ind1 : ind2;
		}
		else
			System.out.println("NO PARENT SELECTION STRATEGY IS BEING USED");
		
		return index;
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
	
	
	
	public double[] saturateAndEvaluate(char scheme, double[] point, Problem prob, double[][] boundaries) throws Exception
	{
		double fit = Double.NaN;
		int corrected = 0;
		double[] output = new double[point.length];
		if(scheme == 't')
		{
			//System.out.println("TORO");
			output = Corrections.correct('t', point, boundaries);
			
			if(!equal(output, point))
			{
				for(int i=0;i<point.length;i++)
					point[i]=output[i];
				corrected = 1;//ciccio++;
			}
			fit = prob.f(point);
		}
		else if(scheme== 's')
		{
			//System.out.println("SAT");
			output = saturation(point, boundaries);
			
			if(!equal(output, point))
			{
				for(int i=0;i<point.length;i++)
					point[i]=output[i];
				corrected=1;//ciccio++;
			}
			fit = prob.f(point);
		}
		else if(scheme== 'e')
		{
			output = saturation(point, boundaries);
			if(!equal(output, point))
			{
				corrected=1;//ciccio++;
				fit = 2;
			}
			else
				fit = prob.f(point);
			
		}
		else
			System.out.println("No bounds handling shceme seleceted");
		
					
		double[] r = {fit, corrected};
		return r;
	}
	
	
	
	
}

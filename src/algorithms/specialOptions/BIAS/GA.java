package algorithms.specialOptions.BIAS;



import static utils.algorithms.operators.ISBOp.generateRandomSolution;
import static utils.algorithms.operators.ISBOp.GAmutations;
import static utils.algorithms.operators.ISBOp.GAParentSelections;
import static utils.algorithms.operators.ISBOp.GACrossovers;
import utils.algorithms.Counter;
import static utils.MatLab.indexMin;
import static utils.MatLab.indexMax;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

public class GA extends AlgorithmBias
{	
	private char selectionStrategy = 't';  // r --> fitness proportional roulette wheel  t-->stochastic tournament 
	private char crossoverStrategy = 'a'; //d-->discrete  a-->full arithmetic
	private char mutationStrategy = 'g';  // c --> Cauchy   g-->Gaussian 
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{	
		int populationSize = getParameter("p0").intValue(); 
		int nt =  getParameter("p1").intValue();
		int pt =  getParameter("p2").intValue(); //selection probability for stochastic tournament
		double CR = getParameter("p3").doubleValue();
		double d = getParameter("p4").doubleValue(); //fixed to 0.25 in Kononova 2015
		double md = getParameter("p5").doubleValue(); //fixed to ; 0.01 in Kononova 2015

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		this.numberOfCorrections=0;
		
		int[] ids = new int[populationSize];
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		String FullName = getFullName("GA"+this.mutationStrategy+this.crossoverStrategy+this.selectionStrategy+this.correction,problem); 
		Counter PRNGCounter = new Counter(0);
		createFile(FullName);
		
		int newID = 0;	
		String line = new String();
		
		
		if(this.selectionStrategy == 't') 
			line+="popSize "+populationSize+" nt "+nt;
		else if(this.selectionStrategy == 'r') 
			line+="popSize "+populationSize;
		else if(this.selectionStrategy == 's') 
			line+="popSize "+populationSize+" pt "+pt;
		else
			System.out.println("Unrecognised selection stratgy!");
		
		
		if(this.crossoverStrategy == 'a')
			line+=" d "+d;
		else if(this.crossoverStrategy == 'd') 
			line+=" CR "+CR;
		else
			System.out.println("Unrecognised crossover stratgy!");
		
		
		if(this.mutationStrategy == 'c')
			line+=" md "+md;
		else if(this.mutationStrategy == 'g')
			line+=" md "+md;
		else
			System.out.println("Unrecognised mutation stratgy!");
		
		
		writeHeader(line, problem);
		line = new String();
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		

		int i = 0;
		int parent1 = 0;
		int parent2 = 0; 
		int worst = 0;

		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			
			double[] tmp = generateRandomSolution(bounds, problemDimension, PRNGCounter);
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
			
			parent1 = GAParentSelections( selectionStrategy, fitnesses, nt,pt, PRNGCounter);
			parent2 = GAParentSelections( selectionStrategy, fitnesses, nt,pt,PRNGCounter);
			double[] child = GACrossovers(population[parent1], population[parent2], CR, d, crossoverStrategy, PRNGCounter);
			
			
			child = GAmutations(child, mutationStrategy, md, bounds, PRNGCounter);
 			
			child = correct(child,population[parent1],bounds, PRNGCounter);
			double fChild = problem.f(child);
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
		
		closeAllBF();	
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRNGCounter.getCounter(), "correctionsGA");
		
		return FT;
	}
	
	
	protected void setSelectionStrategy(char ss) {this.selectionStrategy = ss;}  // r --> fitness proportional roulette wheel  t-->stochastic tournament 
	protected void setCrossoverStrategy(char cs) {this.crossoverStrategy = cs;} //d-->discrete  a-->full arithmetic
	protected void setMutationStrategy(char ms) {this.mutationStrategy = ms;} // c --> Cauchy   g-->Gaussian 
	
	public GA() {}
	public GA(char ss,char cs, char ms){super(); setSelectionStrategy(ss); setCrossoverStrategy(cs); setMutationStrategy(ms);}

	
}

package algorithms;

import static utils.algorithms.operators.DEOp.currentToBest1;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;

import java.util.ArrayList;
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * JADE: Adaptive Differential Evolution with optional archive (based on code created by Isaac Triguero Velazquez 23-7-2009)
 */
public class JADE extends Algorithm
{
	/*Own parameters of the algorithm*/
	private int populationSize; 
	private int dimension;
	private double F;
	private double crossOverRate[];
	private int strategy;
	//private String CrossoverType;	// Binomial, Exponential, Arithmetic
	//private boolean Archive;		// JAde with or without Archive.
	private double p; 				// to select the number of pbest
	private double c;

	protected int numberOfbetters;

	private int maxEval;
	private int eval;
	double[][] bounds;

	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		this.populationSize = this.getParameter("p0").intValue();//60
		this.p = this.getParameter("p1");//0.05
		this.c = this.getParameter("p2");//0.1

		this.dimension = problem.getDimension();
		this.maxEval = maxEvaluations;

		this.bounds = problem.getBounds();

		this.strategy = 1;

		this.numberOfbetters = (int) (this.p*populationSize);
		if (numberOfbetters < 1)
			numberOfbetters  = 1;

		FTrend FT = new FTrend();

		eval = 0;

		// First, we create the population, with populationSize.
		ArrayList<double[]> population = new ArrayList<double[]>();
		double[] mutation = new double[dimension];
		double[] crossover = new double[dimension];

		double fitness[] = new double[populationSize];
		double meanCR = 0.5;
		double meanF = 0.5;
		ArrayList<double[]> Archivo = new ArrayList<double[]>();
		int utilArchivo = 0;

		// we save the different successful F and CR.
		double SF[] = new double[this.populationSize];
		double SCR[] = new double[this.populationSize];

		this.crossOverRate= new double[this.populationSize];
		double F[] = new double[this.populationSize];
		
		double[] best = new double[dimension];

		// First Stage, Initialization.
		for (int i=0; i< populationSize; i++)
		{
			double[] cromosoma = new double[dimension];
			if(initialSolution!=null && i==0)
				for(int k=0;k<dimension;k++)
					cromosoma[k]=initialSolution[k];
			else
			cromosoma = generateRandomSolution(bounds, dimension);
			fitness[i] = problem.f(cromosoma);
			eval++;
			population.add(cromosoma);
			Archivo.add(cromosoma); // to initialize the archive
		}

		// We select the best initial  particle
		double bestFitness=fitness[0];
		for (int n = 0; n < dimension; n++)
			best[n] = population.get(0)[n];
		FT.add(0, bestFitness);
				
		for (int i=1; i< populationSize;i++)
		{
			if (fitness[i]<bestFitness)
			{
				bestFitness = fitness[i];
				for (int n = 0; n < dimension; n++)
					best[n] = population.get(i)[n];
				FT.add(i, bestFitness);
			}
		}

		boolean exit = false;
		while (eval < maxEval && !exit)
		{ 	
			// Main loop
			int utilF = 0;
			int utilCR = 0;

			// If we are going to use exponential, I calculate the index of possible selecting  Mutation.
			for (int i=0; i<populationSize && !exit; i++)
			{
				//Generate CRi
				this.crossOverRate[i] = RandUtils.gaussian(meanCR, 0.1);
				//Normalize
				if (this.crossOverRate[i] > 1)
					this.crossOverRate[i] = 1;
				else if (this.crossOverRate[i] < 0) 
					this.crossOverRate[i] = 0;

				//Generate Fi
				double uniforme;
				do
				{
					uniforme = RandUtils.random();
					F[i]  = 0.1*Math.tan(Math.PI*uniforme) + meanF;
				}
				while (F[i] <= 0);

				if (F[i] >1)
					F[i] = 1;

				if (Double.isNaN(F[i]))
					F[i] = 0.1 + (1-0.1)*RandUtils.random();

				this.F = F[i];

				// Second:  Mutation Operation.
				// generate a mutant for each item of the population.
				mutation = mutant(population, fitness, i, Archivo, utilArchivo);

				// Third: Crossver Operation.
				// Now, we decide if the mutation will be the trial vector.
				crossover = new double[dimension];
				for (int j=0; j< this.dimension; j++)
					crossover[j] = population.get(i)[j];
				int j_rand = RandUtils.randomInteger(dimension-1);

				for (int j=0; j<dimension; j++)
				{
					// For each part of the solution
					double randNumber = RandUtils.random();

					if (randNumber<this.crossOverRate[i] || j == j_rand)
						crossover[j] = mutation[j]; // Overwrite.
				}

				// Fourth: Selection Operation.
				// Decide if the trial vector is better than initial population.
				// Crossover has the trialVector, we check its fitness.
				double trialVector = problem.f(crossover);
				eval++;
				if (eval >= maxEval)
					exit = true;

				if (trialVector < fitness[i])
				{
					//Archivo.set(utilArchivo%populationSize, population.get(i));
					double[] archived = new double[dimension];
					for (int j=0; j<dimension; j++)
						archived[j] = population.get(i)[j];
					Archivo.add(archived);
					utilArchivo++;
					SCR[utilCR%populationSize] = this.crossOverRate[i];
					utilCR++;
					SF[utilF%populationSize] = F[i];
					utilF++;

					population.set(i, crossover);
					fitness[i] = trialVector;
					//utilArchivo = utilArchivo%populationSize;
				}

				if (fitness[i] < bestFitness)
				{
					bestFitness = fitness[i];
					for (int n = 0; n < dimension; n++)
						best[n] = population.get(i)[n];
					FT.add(eval, bestFitness);
				}
			}

			// Now we remove solutions from A.
			if (utilArchivo > this.populationSize)
			{
				utilArchivo = this.populationSize;
				do
				{
					Archivo.remove(RandUtils.randomInteger(Archivo.size()-1));
				}
				while (Archivo.size() > this.populationSize);
			}

			double meanA= 0;
			double meanL =0;
			double numerator=0, denominator =0;

			for (int i=0; i< utilCR; i++)
			{
				meanA+= SCR[i];
				numerator += SF[i] * SF[i];
				denominator += SF[i];
			}

			if (numerator == 0)
				numerator =1;
			if (denominator ==0)
				denominator = 1;

			meanL = numerator/denominator;
			meanA = meanA/utilCR;

			meanCR =(1-c)*meanCR + c * meanA;
			meanF = (1-c)*meanF + c * meanL;

		} // End main LOOP
		
		finalBest = best;

		FT.add(eval, bestFitness);
		
		return FT;
	}

	public int[] getBestSolutions(ArrayList<double[]> population,double fitness[])
	{
		int number = this.numberOfbetters;
		int index[] = new int[number];
		int ind= 0;
		double mejor = Double.MAX_VALUE;
		double acc;

		for(int i=0; i< population.size(); i++)
		{
			acc =fitness[i]; 
			if (acc < mejor)
			{
				ind = i;
				mejor = acc;
			}	  
		}
		index[0] = ind;

		for (int j=1; j<number; j++)
		{
			mejor = Double.MAX_VALUE;
			for (int i=0; i< population.size(); i++)
			{
				acc = fitness[i];
				//if(acc > mejor && acc < accuracy(population[index[j-1]],trainingDataSet))
				if (acc < mejor && acc > fitness[index[j-1]])
				{
					ind = i;
					mejor = acc;
				}	  
			}
			index[j] =  ind;

		}
		return index;
	}

	public double[] mutant(ArrayList<double[]> population, double fitness[], int actual, ArrayList<double[]> Archivo, int utilArchivo)
	{
		double[] mutant = null, r1, r2, xbest;

		// r1 different to actual	      
		int ran;
		do
		{
			ran = RandUtils.randomInteger(population.size()-1);   
		}
		while (ran == actual);

		r1 = population.get(ran);

		int number;

		do
		{ 
			number = RandUtils.randomInteger(population.size() + utilArchivo-1);
		}
		while (number==ran || number == actual);

		if (number < population.size())
			r2 = population.get(number);  
		else
			r2 = Archivo.get(number-population.size());

		int indices[] = new int [this.numberOfbetters];

		indices = getBestSolutions(population,fitness);
		number = RandUtils.randomInteger(indices.length-1);

		xbest = population.get(indices[number]);

		switch (this.strategy)
		{
			case 1:
				mutant = currentToBest1(population.get(actual), r1, r2, xbest, F);
				mutant = toro(mutant, bounds);
				break;
		}   

		return mutant;
	}
}
package algorithms;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

public class CCPSO2 extends Algorithm
{	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{	
		int populationSize = getParameter("p0").intValue(); //30
		double p = getParameter("p1").doubleValue(); // 0.5
				
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		int[] s = {2, 5, 10, 50, 100, 250}; int[] s1 = {2, 5, 10}; int[] s2 = {2, 5, 10, 50, 100}; int[] s3 = {2, 3}; int[] s4 = {2, 3, 6, 8, 12};
		if(problemDimension == 30)
		{
			s = s1; s2 = null;	
		}
		else if (problemDimension == 100)
		{
			s = s2; s1 = null;	
		}
		else if (problemDimension == 6)
		{
			s = s3; s2 = null; s1 =null; s4 = null;	
		}
		else if (problemDimension == 28)
		{
			s = s4; s2 = null; s1 =null; s3 = null; 	
		}
		
		int[] indexes = new int[problemDimension];
		for(int n=0; n<problemDimension; n++)
			indexes[n] = n;
	
		double[][] X = new double[populationSize][problemDimension];
		double[][] Y = new double[populationSize][problemDimension];
		double[] YHat = generateRandomSolution(bounds, problemDimension);
		double fHat = Double.NaN;
		double[] best = new double[problemDimension];
		double fBest = Double.POSITIVE_INFINITY;
		double[][][] neighborhoodBests = null;
		double[][] yFitnesses = null;
		double[][] globalB = null;
		double[] globalFit = null;
		double[] currentVector = null;
		double currentFit = Double.NaN;
		
		// sample the initial population
		for (int j = 0; j < populationSize; j++)
		{
			double[] tmp1 = generateRandomSolution(bounds, problemDimension);
			double[] tmp2 = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
			{
				X[j][n] = tmp1[n];
				Y[j][n] = tmp2[n];		
			}
		}
		
		int K = 0;
		int S = 0;
		int counter = 0;
		
		boolean improve = false;
		// iterate
		while (counter < maxEvaluations)
		{
			if(!improve)
			{
				//improve = true;
				
				S = RandUtils.randomPermutation(s)[0]; 
				K = problemDimension/S;
				
				neighborhoodBests = new double[K][populationSize][S];
				yFitnesses = new double[K][populationSize];
				globalB = new double[K][S];
				globalFit = new double[K];
				currentVector = new double[S];
				
				indexes = RandUtils.randomPermutation(indexes);
				
				// evaluate initial sub-populations
				for (int j = 0; j < K && counter < maxEvaluations; j++) // for each swarm
				{
					globalFit[j] = Double.POSITIVE_INFINITY;
					for (int i = 0; i < populationSize && counter < maxEvaluations; i++) //for each particle
					{	
						currentVector = getVector(Y, indexes, j, i, S); 
						yFitnesses[j][i] = problem.f(b(currentVector, YHat, j));
						counter++;
						if(yFitnesses[j][i] < globalFit[j])
						{
							globalFit[j] = yFitnesses[j][i];
							for(int n=0; n < S; n++)
								globalB[j][n] = currentVector[n];
						}
					}
					
					//updateYHat(currentVector,YHat,j);
				}
				//generate the first context vector
				for (int j = 0; j < K; j++) // for each swarm
				{
					updateYHat(globalB[j],YHat,j);
				}
				fHat = problem.f(YHat); counter++;
				if(fHat < fBest)
				{
					fBest = fHat;
					for(int n=0; n < problemDimension; n++)
						best[n] = YHat[n];
					FT.add(counter, fBest);
				}	
			}
			
			int check = 0;
			for (int j = 0; j < K && counter < maxEvaluations; j++) // for each swarm
			{
				for (int i = 0; i < populationSize && counter < maxEvaluations; i++) //for each particle
				{	
					currentVector = getVector(X, indexes, j, i, S); 
					currentFit = problem.f(b(currentVector, YHat, j));
					if (currentFit < yFitnesses[j][i] )
					{
						yFitnesses[j][i] = currentFit;
						setVector(Y, currentVector, indexes, j, i);
						if(yFitnesses[j][i] < globalFit[j])
						{
							globalFit[j] = currentFit;
							for(int n=0; n < S; n++)
								globalB[j][n] = currentVector[n];
						}
					}
					counter++;
					if(counter%problemDimension == 0)
						FT.add(counter, fBest);
				}
				
				for (int i = 0; i < populationSize; i++) //for each particle
				{	
					int[] PN =  circularIndex(i, populationSize);
					int imin = localBest(yFitnesses, j, i, PN[0], PN[1]);
					neighborhoodBests[j][i] = getVector(Y, indexes, j, imin, S);
				}
				
				
				currentFit = problem.f(b(globalB[j], YHat, j)); 
				if(currentFit < fHat)
				{
					check++;
					fHat = currentFit;
					updateYHat(globalB[j], YHat, j);
					if(fHat < fBest)
					{
						fBest = fHat;
						for(int n=0; n < problemDimension; n++)
							best[n] = YHat[n];
					}
				}
				improve = check > 0;
			}
			
			
			for (int j = 0; j < K; j++) // for each swarm
			{
				for (int i = 0; i < populationSize; i++) //for each particle
				{	
					double[] y = getVector(Y, indexes, j, i, S);
					currentVector = updatePosition(y,neighborhoodBests[j][i], p);
					currentVector = toro(currentVector, bounds);
					setVector(X, currentVector, indexes, j, i); 
				}
			}
		}
		
		finalBest = best;
		
		FT.add(counter, fBest);
		
		return FT;
	}
	
	private double[] b(double[] vector, double[] contextVector, int swarm)
	{
		int c = 0;
		int S = vector.length;
		int probDim = contextVector.length;
		double[] temp  = new double[probDim];
		for(int n=0; n < probDim; n++)
			if(n < S*swarm || n >= S*(swarm + 1))
				temp[n] = contextVector[n];
			else
			{
				temp[n] = vector[c];
				c++;
			}
		
		return temp;
	}
	
	private double[] updatePosition(double[] y, double[] yFirst, double prob)
	{
		int S = yFirst.length; 
		double[] position = new double[S];
		for(int n=0; n < S; n++)
			if(RandUtils.random() <= prob)
				position[n] = RandUtils.cauchy(  yFirst[n] , Math.abs(y[n] - yFirst[n])  );
			else
				position[n] = RandUtils.gaussian(  yFirst[n] , Math.abs(y[n] - yFirst[n])  );
		
		return position;
	}
	
	private  int[] circularIndex(int index, int size)
	{
		int[] pn = new int[2];
		pn[0] = index - 1;
		pn[1] = index + 1;
		if(index == 0)
			pn[0] = size - 1;
		else if(index == size - 1)
			pn[1] = 0;
		return pn;
	}
	
	private  int localBest(double[][] fit, int swarm, int part, int previous, int next)
	{   
		
		int indexMin = part;
		if(fit[swarm][previous] < fit[swarm][indexMin])
			indexMin = previous;
		if(fit[swarm][next] < fit[swarm][indexMin])
			indexMin = next;
		
		return indexMin;
	}
	
	private double[] getVector(double[][] matrix, int[] ind, int swarm, int part, int dim)
	{
		double[] vector = new double[dim];
		int index;
		for (int n = 0; n < dim; n++) 
		{
			index = ind[swarm*dim + n];
			vector[n] = matrix[part][index];	
		}
		
		return vector;
	}
	
	private void setVector(double[][] matrix, double[] vector, int[] ind, int swarm, int part)
	{
		int dim = vector.length;
		int index;
		for (int n = 0; n < dim; n++) 
		{
			index = ind[swarm*dim + n];
			matrix[part][index] = vector[n];	
		}
		
	}
	
	private void updateYHat(double[] vector, double[] contextVector, int swarm)
	{
		int dim = vector.length;
		for(int n=0; n < dim; n++)
		{
			contextVector[swarm*dim + n] = vector[n];
		}	
	}
}
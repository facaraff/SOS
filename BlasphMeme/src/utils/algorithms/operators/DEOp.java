/** @file Operators.java
 *  
 *
 * BLASPHMEME
 * 
 * 
 * SCRIVICI QUEL CHE CAZZO TE PARE QUESTAA E@ LA DESCIZIONE PIU@ GENERICA CHE VA NELLA LISTA DEI FILES
 * LEGGI QUI https://www.cs.cmu.edu/~410/doc/doxygen.html#commands
 *  This file contains the kernel main() function.
 *  @author Fabio Caraffini
*/
package utils.algorithms.operators;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import static utils.MatLab.indexMin;
import static utils.MatLab.subtract;
import static utils.MatLab.sum;
import static utils.MatLab.multiply;
import utils.MatLab;

import static utils.algorithms.Misc.centroid;
import static utils.algorithms.Misc.orthonormalise;
import static utils.algorithms.Misc.Cov;

import utils.random.RandUtils;
/**
 * This class contains the implementation of Differential Evolution operators.
*/	
public class DEOp
{	
		/**
		* rand/1 mutation scheme
		* 
		* @param population set of candidate solutions.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] rand1(double[][] population, double F)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtils.randomPermutation(r); 
		
			int r1 = r[0];
			int r2 = r[1];
				int r3 = r[2];
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[r1][i] + F*(population[r2][i]-population[r3][i]);
		
			return newPt;
		}
	   /**
		* rand/1 mutation scheme
		* 
		* @param xr first (random) individual.
		* @param xs second (random) individual.
		* @param xt third (random) individual.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] rand1(double[] xr, double[] xs, double[] xt, double F)
		{
			int problemDimension = xr.length;
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = xr[i] + F*(xs[i]-xt[i]);
			return newPt;
		}
	   /**
		* rand/2 mutation scheme
		* 
		* @param population set of candidate solutions.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] rand2(double[][] population, double F)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtils.randomPermutation(r);
		
			int r1 = r[0];
			int r2 = r[1];
			int r3 = r[2];
			int r4 = r[3];
			int r5 = r[4];
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[r1][i] + F*(population[r2][i]-population[r3][i]) + F*(population[r4][i]-population[r5][i]);
		
			return newPt;
		}
	   /**
		* rand/2 mutation scheme, alternative method;
		* 
		* @param xr first (random) individual.
		* @param xs second (random) individual.
		* @param xt third (random) individual.
		* @param xu fourth (random) individual.
		* @param xv fifth (random) individual.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] rand2(double[] xr, double[] xs, double[] xt, double[] xu, double[] xv, double F)
		{
			int problemDimension = xr.length;
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = xr[i] + F*(xs[i]-xt[i]) + F*(xu[i]-xv[i]);
		
			return newPt;
		}
	   /**
		* cur-to-best/1 mutation scheme
		* 
		* @param population set of candidate solutions/ individuals.
		* @param bestPt best individual.
		* @param j index of the current individual.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] currentToBest1(double[][] population, double[] bestPt, int j, double F)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
			
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtils.randomPermutationExcl(r,j);
			
			int r1 = r[0];
			int r2 = r[1];

			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[j][i] + F*(bestPt[i]-population[j][i]) + F*(population[r1][i]-population[r2][i]);
			
			return newPt;
		}
	
	   /**
		* current-to-best/1 mutation scheme
		* 
		* @param population set of candidate solutions/individuals.
		* @param bestIndex index of the best individual.
		* @param j index of the current individual.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] currentToBest1(double[][] population, int bestIndex, int j, double F)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtils.randomPermutationExcl(r,j);
		
			int r1 = r[0];
			int r2 = r[1];

			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[j][i] + F*(population[bestIndex][i]-population[j][i]) + F*(population[r1][i]-population[r2][i]);
		
			return newPt;
		}
	   /**
		* current-to-best/1 mutation scheme, alternative method.
		* 
		* @param cur current individual.
		* @param xr first (random) individual.
		* @param xs second (random) individual.
		* @param bestPt best individual.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] currentToBest1(double[] cur, double[] xr, double[] xs, double[] bestPt, double F)
		{
			int problemDimension = cur.length;		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
			{
				newPt[i] = cur[i] + F*(bestPt[i]-cur[i]) + F*(xr[i]-xs[i]);
			}

			return newPt;
		}
		 /**
		* rand-to-best/1 mutation scheme
		* 
		* @param population set of candidate solutions/individuals.
		* @param bestPt best individual.
		* @param F scale factor.
		* @return newPt mutant indvidual.
		*/
		public static double[] randToBest1(double[][] population, double[] bestPt, double F)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtils.randomPermutation(r);
		
			int r1 = r[0];
			int r2 = r[1];
			int r3 = r[2];
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[r3][i] + F*(bestPt[i]-population[r3][i]) + F*(population[r1][i]-population[r2][i]);

			return newPt;
		}
	   /**
		* rand-to-best/2 mutation scheme
		* 
		* @param population set of candidate solutions/individuals.
		* @param bestPt best individual.
		* @param F scale factor.
		* @return newPt mutant indvidual.
		*/
		public static double[] randToBest2(double[][] population, double[] bestPt, double F)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtils.randomPermutation(r);
		
			int r1 = r[0];
			int r2 = r[1];
			int r3 = r[2];
			int r4 = r[3];
			int r5 = r[4];
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[r3][i] + F*(bestPt[i]-population[r3][i]) + F*(population[r1][i]-population[r2][i]) + F*(population[r4][i]-population[r5][i]);

			return newPt;
		}
	   /**
		* rand-to-best/2 mutation scheme, alternative method.
		* 
		* @param xr first (random) individual.
		* @param xs second (random) individual.
		* @param xt third (random) individual.
		* @param xu fourth (random) individual.
		* @param xv fifth (random) individual.
		* @param bestPt best individual.
		* @param F scale factor.
		* @return nrePt mutant individual.
		*/
		public static double[] randToBest2(double[] xr, double[] xs, double[] xt, double[] xu, double[] xv, double[] bestPt, double F)
		{
			int problemDimension = xr.length;
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = xt[i] + F*(bestPt[i]-xt[i]) + F*(xr[i]-xs[i]) + F*(xu[i]-xv[i]);

			return newPt;
		}
	   /**
		* current-to-rand/1 mutation scheme
		* 
		* @param population
		* @param j index of the current individual.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] currentToRand1(double[][] population, int j, double F)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtils.randomPermutationExcl(r,j);
		
			int r1 = r[0];
			int r2 = r[1];
			int r3 = r[2];
			double K = RandUtils.random();
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[j][i] + K*(population[r1][i]-population[j][i]) + K*F*(population[r2][i]-population[r3][i]);

			return newPt;
		}
		/**
		* current-to-rand/1 mutation scheme, alternative method.
		* 
		* @param xr first (random) individual.
		* @param xs second  (radonm) individual.
		* @param xt third (random) individual.
		* @param cur current individual.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] currentToRand1(double[] xr, double[] xs, double[] xt, double[] cur, double F)
		{
			int problemDimension = xr.length;
			double K = RandUtils.random();
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = cur[i] + K*(xt[i]-cur[i]) + K*F*(xr[i]-xs[i]);
		
			return newPt;
		}
		/**
		* best/1 mutation scheme
		* 
		* @param population set of candidate solutions/individuals.
		* @param fitnesses fitness value of each individual in the population.
		* @param F scale factor.
		* @return mutant individual.
		*/
		public static double[] best1(double[][] population, double[] fitnesses, double F)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int index = indexMin(fitnesses);
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize-1; i++)
				if(i!=index)
					r[i] = i;
			r = RandUtils.randomPermutation(r); 
	
			int r1 = r[0];
			int r2 = r[1];
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[index][i] + F*(population[r1][i]-population[r2][i]);
		
			return newPt;
		}
		/**
		* best/1 mutation scheme, alternative method.
		* 
		* @param xBest best individual.
		* @param xr first (random) individual.
		* @param xs second (random) individual.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] best1(double[] xBest, double[] xr, double[] xs, double F)
		{
			int problemDimension = xr.length;
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = xBest[i] + F*(xr[i]-xs[i]);
		
			return newPt;
		}
		/**
		* best/2 mutation scheme
		* 
		* @param population set of candidate solutions/individuals.
		* @param fitnesses fitness value of each individual in the population.
		* @param F scale factor.
		* @return mutant individual.
		*/
		public static double[] best2(double[][] population, double[] fitnesses, double F)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int index = indexMin(fitnesses);
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize-1; i++)
				if(i!=index)
					r[i] = i;
			r = RandUtils.randomPermutation(r); 
	
			int r1 = r[0];
			int r2 = r[1];
			int r3 = r[2];
			int r4 = r[3];
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[index][i] + F*(population[r1][i]-population[r2][i]) +  F*(population[r3][i]-population[r4][i]);
		
			return newPt;
		}
	   /**
		* Exponential crossover
		* 
		* @param x first parent solution.
		* @param y second parent solution.
		* @param CR crossover rate.
		* @return x_off offspring solution.
		*/
		public static double[] crossOverExp(double[] x, double[] y, double CR)
		{
			int n = x.length;
			int startIndex = RandUtils.randomInteger(n-1);
			int index = startIndex;

			double[] x_off = new double[n];
			for (int i = 0; i < n; i++)
				x_off[i] = x[i];
			          
			x_off[index] = y[index];

			index = index + 1;
			while ((RandUtils.random() <= CR) && (index != startIndex))
			{
				if (index >= n)
					index = 0;
				x_off[index] = y[index];
				index++;
			}

			return x_off;
		}
	
	   /**
		* Exponential crossover (fast version)
		* 
		* @param x first parent solution.
		* @param y secon parent solution.
		* @param CR crossover rate.
		* @return x_off offspring solution.
		*/
		public static double[] crossOverExpFast(double[] x, double[] y, double CR)
		{
			int n = x.length;
			int startIndex = RandUtils.randomInteger(n-1);
			int index = startIndex;

			double[] x_off = new double[n];
			for (int i = 0; i < n; i++)
				x_off[i] = x[i];
			          
			x_off[index] = y[index];

			index = index + 1;
			int xoverLength = (int)(Math.log(RandUtils.random())/Math.log(CR));
			int i = 0;
			while ((i < xoverLength) && (index != startIndex))
			{
				if (index >= n)
					index = 0;
				x_off[index] = y[index];
				index++;
				i++;
			}
			return x_off;
	    }
	   /**
		* Binomial crossover
		* 
		* @param x first parent solution.
		* @param y second parent solution.
		* @param CR crossover rate.
		* @return x_off offspring solution.
		*/
		public static double[] crossOverBin(double[] x, double[] y, double CR)
		{
			int n = x.length;
			double[] x_off = new double[n];
			int index = RandUtils.randomInteger(n-1);
			for (int i = 0; i < n; i++)
			{
				if (RandUtils.random() < CR || i == index)
					x_off[i] = y[i];
				else
					x_off[i] = x[i];
			}
			return x_off;
		}	
		
		   /**
			* Rotation-invariant exponential crossover
			* 
			* @param x first parent solution (TARGET).
			* @param m second parent solution (MUTANT).
			* @param CR crossover rate.
			* @param b orthonormal basis.
			* @return x_off offspring solution.
			*
			* see file:///C:/Users/fcaraf00/Downloads/Solving_nonlinear_optimization_problems_by_Differe.pdf
			* 
			*  N.B. b is obtained via the getBasis method
			*/
			public static double[] riec(double[] x, double[] m, double CR, double[][] b)
			{		
				double[] y = subtract(m,x);
				double[] x_off = MatLab.clone(x);
				
				int n = x.length;
				int j = RandUtils.randomInteger(n-1);
				int k = 0;
				try {
					
				do{
					x_off = sum(x_off,multiply(multiply(y,b[j]),b[j]));
					k++; 
					j=((j+1)%(n-1));
				} while(RandUtils.random() <= CR && k<= n);
				
				}catch(Exception ex){
				ex.printStackTrace();
				}
				
				return x_off;
			}
			/**
			* Rotation-invariant binomial crossover
			* 
			* @param x first parent solution (TARGET).
			* @param y second parent solution (MUTANT).
			* @param CR crossover rate.
			* @return x_off offspring solution.
			* 
			* see file:///C:/Users/fcaraf00/Downloads/Solving_nonlinear_optimization_problems_by_Differe.pdf
			* 
			*  N.B. b is obtained via the getBasis method
			*/
			public static double[] ribc(double[] x, double[] m, double CR, double[][] b)
			{
				double[] y = subtract(m,x);
				double[] x_off = MatLab.clone(x);
				
				int n = x.length;
				int j = RandUtils.randomInteger(n-1);
				for(int k=0;k<n;k++)
					if(k==j || RandUtils.random()< CR)
						x_off = sum(x_off,multiply(multiply(y,b[k]),b[k]));
				return x_off;
			}
			
			/**
			* Get orthonormal basis from populations via Gram-Shmidt process
			* 
			* @param pop population.
			* @return b Orthonormal basis.
			*
			*/
			public static double[][] getBasis(double[][] pop)
			{
				double[][] b=null;
				int popSize = pop.length;
				int probDim =pop[0].length;
				if(popSize<probDim)
					System.out.println("There are not enough individulas to perform Gram-Shmidt otrthogonalisation procedure");
				try
				{
					//get centroid
					double[] c = centroid(pop);
					
					//get n directional vectors (n=probDim)
					double[][] temp = new double[probDim][probDim];
					int[] indeces = new int[popSize];
					for(int i=0; i<probDim; i++) indeces[i]=i;
					indeces = RandUtils.randomPermutation(indeces);
					for(int i=0; i<probDim; i++) 
						temp[i]=MatLab.clone(pop[indeces[i]]);
					for(int i=0;i<probDim; i++) 
						temp[i]=subtract(temp[i],c);
					
					//get the basis
					b = orthonormalise(temp);
			
				}catch(Exception ex){
				ex.printStackTrace();
				}
				
				return b;
			}
			
			/**
			* Get orthonormal basis from populations via Eigen-decomposition method
			* 
			* @param pop population.
			* @param criterion the criterion used to select the vector from the eigen-vectors matrix (0 random; 1 highest eigenvalue; 2 lowest eigenvalue)
			* @return b Orthonormal basis.
			*
			*/
			public static double[][] getEigenBasis(double[][] pop)
			{
				double[][] b=null;
				int popSize = pop.length;
				int probDim =pop[0].length;
				if(popSize<probDim)
					System.out.println("There are not enough individulas to perform Gram-Shmidt otrthogonalisation procedure");
				try
				{
					//get centroid
					double[] c = centroid(pop);
					
					//get n directional vectors (n=probDim)
					double[][] temp = new double[probDim][probDim];
					int[] indeces = new int[popSize];
					for(int i=0; i<probDim; i++) indeces[i]=i;
					indeces = RandUtils.randomPermutation(indeces);
					for(int i=0; i<probDim; i++) 
						temp[i]=MatLab.clone(pop[indeces[i]]);
					for(int i=0;i<probDim; i++) 
						temp[i]=subtract(temp[i],c);
										
					EigenDecomposition E =  new EigenDecomposition(new Array2DRowRealMatrix(temp));
					b = E.getV().getData();
					E = null; temp =null;
				}catch(Exception ex){
				ex.printStackTrace();
				}
				
				return b;
			}
			
			
			/**
			* Rotation-invariant Eigen-cross-over
			* 
			* @param x first parent solution (TARGET).
			* @param m second parent solution (MUTANT).
			* @param CR crossover rate.
			* @param pop population.
			* @param pr activation probability for Eigen-XO
			* @param exp 1 for exponential cross-over, 0 for binomial cross-over.
			* @return x_off offspring solution.
			*/
			public static double[] eigenXOver(double[] x, double[] m, double CR, double[][] pop, double pr,  boolean exp)
			{	
				double[] eigen_x_off_apo = null;
				double[] eigen_x = null;
				double[] x_off = new double[x.length];
				double[][] Pt = null;
				
				double r = RandUtils.random();
				
				if(r<=pr)
				{
					double[][] Cov=Cov(pop);
					//System.out.println("uno="+Cov[0][0]+" due="+Cov[1][2]+" eud="+Cov[2][1]);
					EigenDecomposition E =  new EigenDecomposition(new Array2DRowRealMatrix(Cov));
					double[][] P = E.getV().getData();
					eigen_x_off_apo = multiply(P,m);
					eigen_x = multiply(P,x);
					Pt = E.getVT().getData();
					E = null; P = null;
				}
				else
				{
					eigen_x_off_apo = m;
					eigen_x = x;
				}
				
				
				if(exp)
					x_off=crossOverExp(eigen_x,eigen_x_off_apo, CR);
				else
					x_off=crossOverBin(eigen_x,eigen_x_off_apo, CR);
				
				if(r<=pr)
					x_off = multiply(Pt,x_off);
				
				return x_off;
			}
}



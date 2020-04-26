/**
Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/

package utils.algorithms.operators;

import org.apache.commons.math3.linear.EigenDecomposition;

import interfaces.Problem;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import static utils.MatLab.indexMin;
import static utils.MatLab.subtract;
import static utils.MatLab.sum;
import static utils.MatLab.multiply;
import utils.MatLab;

import static utils.algorithms.Misc.centroid;
import static utils.algorithms.Misc.orthonormalise;
import static utils.algorithms.Misc.Cov;
import static utils.algorithms.CompactAlgorithms.truncateRandn;
import utils.algorithms.Counter;
import utils.random.RandUtilsISB;

/** 
 *  
 *@author Fabio Caraffini (fabio.caraffini@gmail.com)
 * 
 * @link www.doi.org.com/10.0.81.208/preprints202003.0381.v1
 *
 * @package utils.algorithms.operators
 * 
 * @file ISBOp.java
 * 
 *  
 * This class contains the implementation of Particle Swarm Optimisation operators.
*/	
public class ISBOp
{	
		/**
		* rand/1 mutation scheme
		* 
		* @param population set of candidate solutions.
		* @param F scale factor.
		* @return newPt mutant individual.
		*/
		public static double[] rand1(double[][] population, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtilsISB.randomPermutation(r, counter); 
		
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
		public static double[] rand1(double[] xr, double[] xs, double[] xt, double F, Counter counter)
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
		public static double[] rand2(double[][] population, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtilsISB.randomPermutation(r, counter);
		
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
		public static double[] rand2(double[] xr, double[] xs, double[] xt, double[] xu, double[] xv, double F, Counter counter)
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
		public static double[] currentToBest1(double[][] population, double[] bestPt, int j, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
			
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtilsISB.randomPermutationExcl(r,j, counter);
			
			int r1 = r[0];
			int r2 = r[1];

			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[j][i] + F*(bestPt[i]-population[j][i]) + F*(population[r1][i]-population[r2][i]);
			
			return newPt;
		}
		
		/**
		* cur-to-best/1 mutation scheme, alternative method
		* @param fitnesses the an array containing the fitness values of all individual in the population.
		* @param population A set of candidate solutions/individuals.
		* @param j The index of the current individual.
		* @param F The scale factor.
		* @param counter A counter to monitor PRG activations.
		* @return newPt mutant individual.
		*/
		public static double[] currentToBest1(double[] fitnesses, double[][] population, int j, double F, Counter counter)
		{
			int bestIndex = indexMin(fitnesses);
			double[] bestPt = population[bestIndex];
			
			return currentToBest1(population, bestPt, j, F, counter);
		}
	
	   /**
		* current-to-best/1 mutation scheme
		* 
		* @param population set of candidate solutions/individuals.
		* @param bestIndex index of the best individual.
		* @param j index of the current individual.
		* @param F scale factor.
		* * @param counter A counter to monitor PRG activations.
		* @return newPt mutant individual.
		*/
		public static double[] currentToBest1(double[][] population, int bestIndex, int j, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtilsISB.randomPermutationExcl(r,j, counter);
		
			int r1 = r[0];
			int r2 = r[1];

			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[j][i] + F*(population[bestIndex][i]-population[j][i]) + F*(population[r1][i]-population[r2][i]);
		
			return newPt;
		}
		 /**
		* current-to-best/1 mutation scheme
		* 
		* @param current current individual from the population .
		* @param best best individual.
		* @param r1 a randomly selected individual.
		* @param r2 a randomly selected individual.
		* @param F scale factor.
		* * @param counter A counter to monitor PRG activations.
		* @return newPt mutant individual.
		*/
		public static double[] currentToBest1(double[] current, double[] best, double[] r1, double[] r2, double F, Counter counter)
		{
			int problemDimension = current.length;
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = current[i] + F*(best[i]-current[i]) + F*(r1[i]-r2[i]);
			return newPt;
		}
	
		 /**
		* rand-to-best/1 mutation scheme
		* 
		* @param population set of candidate solutions/individuals.
		* @param bestPt best individual.
		* @param F scale factor.
		* @param counter A counter to monitor PRG activations.
		* @return newPt mutant indvidual.
		*/
		public static double[] randToBest1(double[][] population, double[] bestPt, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtilsISB.randomPermutation(r, counter);
		
			int r1 = r[0];
			int r2 = r[1];
			int r3 = r[2];
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[r3][i] + F*(bestPt[i]-population[r3][i]) + F*(population[r1][i]-population[r2][i]);

			return newPt;
		}
		 /**
		* rand-to-best/1 mutation scheme, alternative method
		* 
		* @param fitnesses Array containing the fitness values of all individuals in the population.
		* @param population set of candidate solutions/individuals.
		* @param F scale factor.
		* @param counter A counter to monitor PRG activations.
		* @return newPt mutant indvidual.
		*/
		public static double[] randToBest1(double[] fitnesses, double[][] population, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int bestIndex = indexMin(fitnesses);
			double[] bestPt = population[bestIndex];
			
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtilsISB.randomPermutation(r, counter);
		
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
		* @param counter A counter to monitor PRG activations.
		* @return newPt mutant indvidual.
		*/
		public static double[] randToBest2(double[][] population, double[] bestPt, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtilsISB.randomPermutation(r, counter);
		
			int r1 = r[0];
			int r2 = r[1];
			int r3 = r[2];
			int r4 = r[3];
			int r5 = r[4];
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[r1][i] + F*(bestPt[i]-population[r1][i]) + F*(population[r2][i]-population[r3][i]) + F*(population[r4][i]-population[r5][i]);

			return newPt;
		}
		 /**
		* rand-to-best/2 mutation scheme, alternative method
		* 
		* @param fitnesses Array containing the fitness values of all individuals in the population.
		* @param population A set of candidate solutions/individuals.
		* @param F scale factor.
		* @param counter A counter to monitor PRG activations.
		* @return newPt A mutant indvidual.
		*/
		public static double[] randToBest2(double[] fitnesses, double[][] population, double F, Counter counter)
		{
			int bestIndex = indexMin(fitnesses);
			double[] bestPt = population[bestIndex];
			
			return randToBest2(population, bestPt, F, counter);
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
		* @param counter A counter to monitor PRG activations.
		* @return nrePt mutant individual.
		*/
		public static double[] randToBest2(double[] xr, double[] xs, double[] xt, double[] xu, double[] xv, double[] bestPt, double F, Counter counter)
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
		* @param counter A counter to monitor PRG activations.
		* @return newPt mutant individual.
		*/
		public static double[] currentToRand1(double[][] population, int j, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize; i++)
				r[i] = i;
			r = RandUtilsISB.randomPermutationExcl(r,j, counter);
		
			int r1 = r[0];
			int r2 = r[1];
			int r3 = r[2];
			double K = RandUtilsISB.random(counter);
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
		* @param counter A counter to monitor PRG activations.
		* @return newPt mutant individual.
		*/
		public static double[] currentToRand1(double[] xr, double[] xs, double[] xt, double[] cur, double F, Counter counter)
		{
			int problemDimension = xr.length;
			double K = RandUtilsISB.random(counter);
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
		* @param counter A counter to monitor PRG activations.
		* @return mutant individual.
		*/
		public static double[] best1(double[][] population, double[] fitnesses, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int index = indexMin(fitnesses);
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize-1; i++)
				if(i!=index)
					r[i] = i;
			r = RandUtilsISB.randomPermutation(r, counter); 
	
			int r1 = r[0];
			int r2 = r[1];
		
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = population[index][i] + F*(population[r1][i]-population[r2][i]);
		
			return newPt;
		}
		/**
		* best/1 mutation scheme
		* 
		* @param best best individual in the population.
		* @param r1 a randomly selected individual from the population.
		* @param r2 a randomly selected individual from the population.
		* @param F scale factor.
		* @param counter A counter to monitor PRG activations.
		* @return mutant individual.
		*/
		public static double[] best1(double[] best, double[] r1, double[] r2, double F, Counter counter)
		{
			int problemDimension = best.length;
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = best[i] + F*(r1[i]-r2[i]);
		
			return newPt;
		}
		/**
		* best/2 mutation scheme
		* 
		* @param population set of candidate solutions/individuals.
		* @param fitnesses fitness value of each individual in the population.
		* @param F scale factor.
		* @param counter A counter to monitor PRG activations.
		* @return mutant individual.
		*/
		public static double[] best2(double[][] population, double[] fitnesses, double F, Counter counter)
		{
			int problemDimension = population[0].length;
			int populationSize = population.length;
		
			int index = indexMin(fitnesses);
		
			int[] r = new int[populationSize];
			for (int i = 0; i < populationSize-1; i++)
				if(i!=index)
					r[i] = i;
			r = RandUtilsISB.randomPermutation(r,counter); 
	
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
		* best/2 mutation scheme
		* 
		* @param population set of candidate solutions/individuals.
		* @param fitnesses fitness value of each individual in the population.
		* @param F scale factor.
		* @param counter A counter to monitor PRG activations.
		* @return mutant individual.
		*/
		public static double[] best2(double[] best, double[] r1, double[] r2, double[] r3, double[] r4, double F, Counter counter)
		{
			int problemDimension = best.length;
	
			double[] newPt = new double[problemDimension];
			for (int i = 0; i < problemDimension; i++)
				newPt[i] = best[i] + F*(r1[i]-r2[i]) +  F*(r3[i]-r4[i]);
			
			return newPt;
		}
	   /**
		* Exponential crossover
		* 
		* @param x first parent solution.
		* @param y second parent solution.
		* @param CR crossover rate.
		* @param counter A counter to monitor PRG activations.
		* @return x_off offspring solution.
		*/
		public static double[] crossOverExp(double[] x, double[] y, double CR, Counter counter)
		{
			int n = x.length;
			int startIndex = RandUtilsISB.randomInteger(n-1, counter);
			int index = startIndex;

			double[] x_off = new double[n];
			for (int i = 0; i < n; i++)
				x_off[i] = x[i];
			          
			x_off[index] = y[index];

			index = index + 1;
			while ((RandUtilsISB.random(counter) <= CR) && (index != startIndex))
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
		* @param counter A counter to monitor PRG activations.
		* @return x_off offspring solution.
		*/
		public static double[] crossOverExpFast(double[] x, double[] y, double CR, Counter counter)
		{
			int n = x.length;
			int startIndex = RandUtilsISB.randomInteger(n-1, counter);
			int index = startIndex;

			double[] x_off = new double[n];
			for (int i = 0; i < n; i++)
				x_off[i] = x[i];
			          
			x_off[index] = y[index];

			index = index + 1;
			int xoverLength = (int)(Math.log(RandUtilsISB.random(counter))/Math.log(CR));
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
		* @param counter A counter to monitor PRG activations.
		* @return x_off offspring solution.
		*/
		public static double[] crossOverBin(double[] x, double[] y, double CR, Counter counter)
		{
			int n = x.length;
			double[] x_off = new double[n];
			int index = RandUtilsISB.randomInteger(n-1, counter);
			for (int i = 0; i < n; i++)
			{
				if (RandUtilsISB.random(counter) < CR || i == index)
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
			* @param counter A counter to monitor PRG activations.
			* @return x_off offspring solution.
			*
			* see file:///C:/Users/fcaraf00/Downloads/Solving_nonlinear_optimization_problems_by_Differe.pdf
			* 
			*  N.B. b is obtained via the getBasis method
			*/
			public static double[] riec(double[] x, double[] m, double CR, double[][] b, Counter counter)
			{		
				double[] y = subtract(m,x);
				double[] x_off = MatLab.cloneArray(x);
				
				int n = x.length;
				int j = RandUtilsISB.randomInteger(n-1, counter);
				int k = 0;
				try {
					
				do{
					x_off = sum(x_off,multiply(multiply(y,b[j]),b[j]));
					k++; 
					j=((j+1)%(n-1));
				} while(RandUtilsISB.random(counter) <= CR && k<= n);
				
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
			* @param counter A counter to monitor PRG activations.
			* @return x_off offspring solution.
			* 
			* see file:///C:/Users/fcaraf00/Downloads/Solving_nonlinear_optimization_problems_by_Differe.pdf
			* 
			*  N.B. b is obtained via the getBasis method
			*/
			public static double[] ribc(double[] x, double[] m, double CR, double[][] b, Counter counter)
			{
				double[] y = subtract(m,x);
				double[] x_off = MatLab.cloneArray(x);
				
				int n = x.length;
				int j = RandUtilsISB.randomInteger(n-1, counter);
				for(int k=0;k<n;k++)
					if(k==j || RandUtilsISB.random(counter)< CR)
						x_off = sum(x_off,multiply(multiply(y,b[k]),b[k]));
				return x_off;
			}
			
			/**
			* Get orthonormal basis from populations via Gram-Shmidt process
			* 
			* @param pop population.
			* @param counter A counter to monitor PRG activations.
			* @return b Orthonormal basis.
			*
			*/
			public static double[][] getBasis(double[][] pop, Counter counter)
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
					indeces = RandUtilsISB.randomPermutation(indeces, counter);
					for(int i=0; i<probDim; i++) 
						temp[i]=MatLab.cloneArray(pop[indeces[i]]);
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
			* @param counter A counter to monitor PRG activations.
			* @return b Orthonormal basis.
			*
			*/
			public static double[][] getEigenBasis(double[][] pop, Counter counter)
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
					indeces = RandUtilsISB.randomPermutation(indeces, counter);
					for(int i=0; i<probDim; i++) 
						temp[i]=MatLab.cloneArray(pop[indeces[i]]);
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
			* @param counter A counter to monitor PRG activations.
			* @return x_off offspring solution.
			*/
			public static double[] eigenXOver(double[] x, double[] m, double CR, double[][] pop, double pr,  boolean exp, Counter counter)
			{	
				double[] eigen_x_off_apo = null;
				double[] eigen_x = null;
				double[] x_off = new double[x.length];
				double[][] Pt = null;
				
				double r = RandUtilsISB.random(counter);
				
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
					x_off=crossOverExp(eigen_x,eigen_x_off_apo, CR, counter);
				else
					x_off=crossOverBin(eigen_x,eigen_x_off_apo, CR, counter);
				
				if(r<=pr)
					x_off = multiply(Pt,x_off);
				
				return x_off;
			}
			
			
			/**
			 * This method randomly generates an individuals in the normalised search space of a compact algorithm.
			 * @param mean The mean vector of the distribution of a compact algorithm.
			 * @param sigma2 The standard deviation vector of a compact algorithms.
			 * @param counter A counter to monitor PRG activations.
			 * @return
			 */
			public static double[] generateIndividual(double[] mean, double[] sigma2, Counter counter)
			{
				int n = mean.length;
				double[] x = new double[n];
				for (int i = 0; i < n; i++)
					x[i] = 2;

				double trial = 0;
				for (int i = 0; i < n; i++)
				{
					trial = 0;
					while ((Math.abs(x[i])>1) && trial < 10)
					{
						trial++;
						x[i] = RandUtilsISB.gaussian(mean[i], Math.sqrt(sigma2[i]), counter);
					}
					
					if (Math.abs(x[i])>1)
						x[i] = truncateRandn(RandUtilsISB.random(counter), mean[i], Math.sqrt(sigma2[i]));
				}
				
				return x;
			}
			
			
			
			/**
			 * Random point in bounds.
			 * 
			 * @param bounds
			 *            search space boundaries (general case).
			 * @param n
			 *            problem dimension.
			 * @return r randomly generated point.
			 */
			public static double[] generateRandomSolution(double[][] bounds, int n, Counter counter) {
				double[] r = new double[n];
				for (int i = 0; i < n; i++)
					r[i] = bounds[i][0] + (bounds[i][1] - bounds[i][0]) * RandUtilsISB.random(counter);
				return r;
			}
			
			public static double[] generateRandomSolution(Problem p, Counter counter) {
				int n = p.getDimension();
				double[][] bounds = p.getBounds();
				double[] r = new double[n];
				for (int i = 0; i < n; i++)
					r[i] = bounds[i][0] + (bounds[i][1] - bounds[i][0]) * RandUtilsISB.random(counter);
				return r;
			}
			
			
			
			/**
			 * Roulette wheel selection with PRNG counter
			 * 
			 * @param values fitness values
			 * @param counter PRNG counter
			 * @return
			 */
			public static int roulette(double[] values, Counter counter) 
			{
				double s = MatLab.sum(values);
				double r = RandUtilsISB.random(counter) * s;
				double thresh = 0.0;
				for (int i = 0; i < values.length-1; i++) {
					thresh += values[i];
					if (r <= thresh) 
						return i;
				}
				return values.length-1;
			}
			
			
			
			/**
			 * GA mutation switcher (with PSRNG counter)
			 * 
			 * @param point a candidate solution
			 * @param mutation the mutation identifier
			 * @param distributionParam a parameter used to set the std value (if Gaussian mutation is used); or the scalFactor (if Cauhcy is sued).  
			 * @return a mutated individual
			 */
			public static double[] GAmutations(double[] point, char mutation, double distributionParam, double[][] bounds, Counter counter)
			{
				double[] output = new double[point.length];
				if(mutation=='g')
					for(int i = 0; i<point.length; i++)
						output[i]=point[i]+RandUtilsISB.gaussian(0, distributionParam*(bounds[i][1]-bounds[i][0]), counter);
				else if(mutation=='c')
					for(int i = 0; i<point.length; i++)
						output[i]=point[i]+RandUtilsISB.cauchy(0, distributionParam, counter);
				else
					System.out.println("Unrecognised mutation");
				return output;
			}
			
			
			/**
			 * GA parent selections switcher (with PSRNG counter)
			 * 
			 * @param selStrategy the selection strategy identifier
			 * @param fitness fitness values of all individuals in the population
			 * @param nt The tournament size
			 * @param counter The PSRNG counter
			 * @return a the index of the selected individual
			 */
			public static int GAParentSelections(char selStrategy, double[] fitness, int nt, double pt, Counter counter)
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
					index = roulette(prob, counter);
				}
				else if(selStrategy=='t')
				{
					int[] ind = getIndices(fitness.length);
					ind=RandUtilsISB.randomPermutation(ind, counter);
					
					if(nt>fitness.length)System.out.println("The tournament size cannot be grater that the polution size");
					
					int indexMin = ind[0];
					double fitnessMin = fitness[ind[0]];
					
					for(int i=1; i<nt; i++)
						if(fitness[ind[i]]<fitnessMin) 
						{
							fitnessMin = fitness[ind[i]];
							indexMin = ind[i];
						}
					
					index = indexMin;
				}
				else if(selStrategy=='s') //Stochastic tournament (this implementation currently works only with a tournament size of 2)
				{
					int[] ind = getIndices(fitness.length);
					ind=RandUtilsISB.randomPermutation(ind, counter);
					int ind1 = (fitness[ind[0]]<fitness[ind[1]])? ind[0] : ind[1];
					int ind2 = (fitness[ind[0]]<fitness[ind[1]])? ind[1] : ind[0];
					index = (RandUtilsISB.random(counter)<pt)? ind1 : ind2;
				}
				else
					System.out.println("Unrecognised parent selction strategy!");
				
				return index;
			}
			 
			/**
			 * Utility method for associating indices to the individuals of the population
			 * 
			 * @param popSize The population size
			 * @return indices an array containing the indices
			 */
			public static int[] getIndices(int popSize)
			{
				int[] indices = new int[popSize];
				for(int n=0; n<popSize; n++)
					indices[n] = n;
				return indices;
			}
			
			
			/**
			 *  GA crossover operators switcher (with PSRNG counter)
			 *  
			 * @param parentA the first parent
			 * @param parentB The second Parent
			 * @param CR The cross over rate (for 'd' xover)
			 * @param d defines the upper and lower bound for the uniform distribution in 'a' crossover
			 * @param xover The crossover operator identifier
			 * @param counter The PSRNG counter
			 * @return output  Offspring solution
			 */
			public static double[] GACrossovers(double[] parentA, double[] parentB, double CR, double d, char xover, Counter counter)
			{
				double[] output = new double[parentA.length];
				
				if(xover=='d') //discrete recombination
					for(int i=0;i<parentA.length;i++)
						output[i]=(RandUtilsISB.random(counter)<CR)? parentA[i]:parentB[i];
				else if(xover=='a')// WHOLE ARITHMETIC RECOMBINATION
					for(int i=0;i<parentA.length;i++)
					{
						double alpha = RandUtilsISB.uniform(-d, 1+d,counter);
						output[i]=alpha*parentA[i]+(1-alpha)*parentB[i];
						
					}
				else
					System.out.println("Unrecognised crossover operator!");
				
				return output;
			}
			
			
}




//else if(selStrategy=='t') //one of the implemented methods in preparation of the 2015 study
//{
//	int[] ind = getIndices(fitness.length);
//	ind=RandUtilsISB.randomPermutation(ind, counter);
//	int ind1 = (fitness[ind[0]]<fitness[ind[1]])? ind[0] : ind[1];
//	int ind2 = (fitness[ind[0]]<fitness[ind[1]])? ind[1] : ind[0];
//	index = (RandUtilsISB.random(counter)<tournamentProb)? ind1 : ind2;
//}

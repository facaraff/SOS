/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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

/** @file Misc.java
 * 
 *
 * MISCELLANEOUS
 * 
 * A software platform for learning Computational Intelligence Optimisation
 * 
 *  @author Fabio Caraffini
*/
package utils.algorithms;

import static utils.MatLab.max;
import static utils.MatLab.min;
import static utils.MatLab.multiply;
import static utils.MatLab.columnXrow;
import static utils.MatLab.dot;
import static utils.MatLab.sum;
import static utils.MatLab.mean;

import static utils.MatLab.norm2;
import static utils.MatLab.getQuantile;
import static utils.MatLab.linearNormalisation;
import static utils.MatLab.cloneArray;

import utils.MatLab;
import utils.random.RandUtils;
import interfaces.Problem;

/**
 * This class contains useful miscellaneous methods.
 */
public class Misc {
	/**
	 * Check if a design variable needs to be corrected
	 * 
	 * @param x
	 *            (design variable)
	 * @param lb
	 *            (lower bound of x)
	 * @param lb
	 *            (lower bound of x)
	 * @return return true if the design variable x is inside the search space,
	 *         false otherwise
	 */
	public static boolean inDomain(double x, double lb, double up) {
		return (x <= up && x >= lb);
	}

	

	/**
	 * Clone a solution.
	 * 
	 * @param x
	 *            solution to be duplicated.
	 * @return xc cloned solution.
	 */
	public static double[] cloneSolution(double[] x) {return cloneArray(x);}

	/**
	 * Clone a solution a 2d matrix.
	 * 
	 * @param x
	 *            solution to be duplicated.
	 * @return xc cloned solution.
	 */
	public static double[][] cloneSolution(double[][] x) {return cloneArray(x);}

	/**
	 * Random point in bounds.
	 * 
	 * @param bounds
	 *            search space boundaries (general case).
	 * @param n
	 *            problem dimension.
	 * @return r randomly generated point.
	 */
	public static double[] generateRandomSolution(double[][] bounds, int n) {
		double[] r = new double[n];
		for (int i = 0; i < n; i++)
			r[i] = bounds[i][0] + (bounds[i][1] - bounds[i][0]) * RandUtils.random();
		return r;
	}
	
	public static double[] generateRandomSolution(Problem p) {
		int n = p.getDimension();
		double[][] bounds = p.getBounds();
		double[] r = new double[n];
		for (int i = 0; i < n; i++)
			r[i] = bounds[i][0] + (bounds[i][1] - bounds[i][0]) * RandUtils.random();
		return r;
	}
	/**
	 * Copy the content of an array onto another array.
	 * 
	 * @param a array containing the values to be copied
	 * @param b array receiving the values
	 * 
	 */
	public static void fillAWithB(double[][] a, double[][] b)
	{
		int I = a.length;
		int J = a[0].length;
		try {
			
			for (int i = 0; i < I; i++)
				for (int j = 0; j < J; j++)
					b[i][j] = a[i][j];
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static void fillAWithB(double[] a, double[] b)
	{
		int I = a.length;
		try {
			
			for (int i = 0; i < I; i++)
					a[i] = b[i];
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Random point in bounds.
	 * 
	 * @param bounds
	 *            search space boundaries (hyper-parallelepiped).
	 * @param n
	 *            problam dimension.
	 * @return r randomly generated point.
	 */
	public static double[] generateRandomSolution(double[] bounds, int n)
	{
		double[] r = new double[n];
		for (int i = 0; i < n; i++)
			r[i] = bounds[0] + (bounds[1] - bounds[0]) * RandUtils.random();
		return r;
	}

	/**
	 * Rounds x to the nearest integer towards zero.
	 */
	public static int fix(double x) {
		return (int) ((x >= 0) ? Math.floor(x) : Math.ceil(x));
	}

	/**
	 * Return an individual whose design variables are the AVG of the design
	 * variables of the individuals of the inputed population.
	 * 
	 * @param p
	 *            population.
	 * @return avgInd individual with averaged design variables.
	 */
	public static double[] AVGDesignVar(double[][] p) {
		int n = p[0].length;
		int ps = p.length;
		double[] avgInd = new double[n];
		try {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < ps; j++) {
					avgInd[i] += ((1.0 / ps) * (p[j][i]));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return avgInd;
	}

	/**
	 * Return the Cov matrix for implementing the eigenvalues-based cross over
	 * operator (DE).
	 * 
	 * @param p
	 *            population.
	 * @return Cov covariance matrix.
	 */
	public static double[][] Cov(double[][] p) {
		int n = p[0].length;// System.out.println("dimension "+n);
		int ps = p.length;// System.out.println(ps);
		double[][] Cov = new double[n][n];
		double[] xBar = AVGDesignVar(p);
		try {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					double temp = 0;

					for (int i = 0; i < ps; i++) {
						temp += ((p[i][j] - xBar[j]) * (p[i][k] - xBar[k]));
					}
					Cov[j][k] = (1.0 / (ps - 1)) * (temp);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Cov;
	}

	/**
	 * Return the centroid of a population of individuals.
	 * 
	 * @param p
	 *            population.
	 * @return c centroid.
	 */
	public static double[] centroid(double[][] p) {
		int dim = p[0].length;
		double[] c = new double[dim];
		int size = p.length;
		for (int i = 0; i < dim; i++) 
			for (int j = 0; j < size; j++)
				c[i] += p[j][i];
		return multiply((1.0 / size), c);
	}

	/**
	 * Return the orthonormilsed version of n n-dimensional direction vectors
	 * according to Gram-Shmidt. see
	 * {@link https://www.researchgate.net/publication/220881848_Solving_nonlinear_optimization_problems_by_Differential_Evolution_with_a_rotation-invariant_crossover_operation_using_Gram-Schmidt_process}
	 * 
	 * @param p
	 *            population.
	 * @return c centroid.
	 */
	public static double[][] orthonormalise(double[][] dirVec) {
		int n = dirVec.length;
		double[][] coorVec = new double[n][n];

		double[][] b = new double[n][n];
		try {
			b[0] = multiply((1 / norm2(dirVec[0])), dirVec[0]);
			coorVec[0] = cloneArray(b[0]);
			double[] innerb;
			for (int i = 1; i < n; i++) {
				innerb = new double[n];
				for (int j = 0; j < i; j++)
					innerb = multiply(-sum(dot(dirVec[i], b[j])), b[j]);
				double[] temp = sum(dirVec[i], innerb);
				b[i] = multiply((1 / norm2(temp)), temp);
				coorVec[i] = cloneArray(b[i]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return coorVec;
	}

	/**
	 * Return the A matrix (determinant = 1) by means of the cholesky decomposition
	 * method in (1+1)-CMAES.
	 * 
	 * @param A.
	 * @return A updated.
	 */
	public static double[][] updateCholesky(double[][] A, double[] z, double c_a) {
		// scalars factors
		double z2 = norm2(z);
		z2 = z2 * z2;
		double ca2 = c_a * c_a;
		double factor = (c_a / z2) * (Math.sqrt(1 + ((1 - ca2) * z2) / ca2) - 1);
		// System.out.println(factor);
		// matrix A*z*z'
		double[][] temp = columnXrow(multiply(A, z), z);
		return sum(multiply(c_a, A), multiply(factor, temp));
	}

	/**
	 * Return return a new Z vector in in (1+1)-CMAES.
	 * 
	 * @param A.
	 * @return A updated.
	 */
	public static double[] newZ(int dimension) {
		double[] x = new double[dimension];
		for (int i = 0; i < dimension; i++)
			x[i] = RandUtils.gaussian(0, 1);
		return x;
	}

	/**
	 * Return diversity measure as defined in Yaman 2018 (Early convergence in DE
	 * paper at LeGO18)
	 * 
	 * @param pop.
	 * @return diversity.
	 */
	public static double populationDiversity1(double[][] pop) {
		int n = pop[0].length;
		int NP = pop.length;

		double[] delta = new double[n];

		for (int j = 0; j < n; j++) {
			double min = pop[0][j];
			double max = pop[0][j];

			for (int i = 1; i < NP; i++) {
				if (pop[i][j] <= min)
					min = pop[i][j];
				else if (pop[i][j] > max)
					max = pop[i][j];
			}

			delta[j] = max - min;
		}

		return max(delta);
	}

	public static double populationDiversity2(double[][] pop) {
		int n = pop[0].length;
		int NP = pop.length;

		double[] delta = new double[n];

		for (int j = 0; j < n; j++) {
			double min = pop[0][j];
			double max = pop[0][j];

			for (int i = 1; i < NP; i++) {
				if (pop[i][j] <= min)
					min = pop[i][j];
				else if (pop[i][j] > max)
					max = pop[i][j];
			}

			delta[j] = max - min;
		}

		return min(delta);
	}

	public static double populationDiversity3(double[][] pop) {
		int n = pop[0].length;
		int NP = pop.length;

		double[] delta = new double[n];

		for (int j = 0; j < n; j++) {
			double min = pop[0][j];
			double max = pop[0][j];

			for (int i = 1; i < NP; i++) {
				if (pop[i][j] <= min)
					min = pop[i][j];
				else if (pop[i][j] > max)
					max = pop[i][j];
			}

			delta[j] = max - min;
		}

		return mean(delta);
	}

	/**
	 * Return diversity in term of standard deviation evaluated with respect to
	 * different central positions
	 * 
	 * @param pop.
	 * @param center
	 *            (can be either 'c' (for centroid) or 'b' (for best individual)
	 * @return diversity.
	 */
	public static double genotypicStd(double[][] pop, double[] best, char center) {
		int n = pop[0].length;
		double[] mu = new double[n];

		if (center == 'c')
			mu = centroid(pop);
		else if (center == 'b')
			mu = best;
		else
			System.out.println("Unknown value used for center, please use a valide one");

		int size = pop.length;
		double PDSquared = 0;

		for (int i = 0; i < size; i++)
			for (int j = 0; j < n; j++)
				PDSquared += Math.pow(pop[j][i] - mu[i], 2);
		PDSquared = PDSquared / size;

		return Math.sqrt(PDSquared);
	}

	/**
	 * Return Normalised p-th quantile of the input array
	 * (Implemented for the Diversity in DE study -Anil Yaman, Giovanni Iacca, Fabio Caraffini-)
	 * 
	 * @param p (0<p<=100).
	 * 
	 * @return normilised quantile (i.e. p=50--> normilised median; p=25--> normalised lower quartile; p=75-->normilised upper quartile; etc)
	 */
	public static double getNormalisedQuantile(double[] values, int p)
	{
		double[] normalisedValues = linearNormalisation(values);
		
		return getQuantile(normalisedValues,p);
	}
	
	
	/************************/
	
	/**
	 * Return the population diversity in term of average of  individual's standard deviation per dimension 
	 * @param pop.
	 * @return diversity.
	 */
	public static double averagedPolulationStandardDeviations(double[][] pop) {
		int n = pop[0].length;
		double[] mu = new double[n];

		mu = centroid(pop);

		
		double[] sigmaSquared = new double[n];
		int popSize = pop.length;
		

		for (int i = 0; i < n; i++)
			for (int j = 0; j < popSize; j++)
				sigmaSquared[i] += ((1.0/popSize)*Math.pow(pop[j][i] - mu[i], 2));
		
		double[] sigmas = new double[n];
		for (int i = 0; i < n; i++)
				sigmas[i] = Math.sqrt(sigmaSquared[i]);
		
		return MatLab.mean(sigmas);
	}
	
	/**
	 * Return the fitness diversity measure in term standard deviation
	 * @param fitnessValues.
	 * @return diversity.
	 */
	public static double fitnesStandardDeviation(double[] fitnessValues) {
		int popSize = fitnessValues.length;
		double mu = MatLab.mean(fitnessValues);
		
		double sigmaSquared = 0;
		

		for (int j = 0; j < popSize; j++)
			sigmaSquared += Math.pow(fitnessValues[j] - mu, 2);
		sigmaSquared = sigmaSquared/popSize;
		
		
		return Math.sqrt(sigmaSquared);
	}
}

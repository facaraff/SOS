/** @file Misc.java
 *  
 *
 * MISCELLANEOUS
 * A software platform for learning Computational Intelligence Optimisation
 * 
 * SCRIVICI QUEL CHE CAZZO TE PARE QUESTAA E@ LA DESCIZIONE PIU@ GENERICA CHE VA NELLA LISTA DEI FILES
 * LEGGI QUI https://www.cs.cmu.edu/~410/doc/doxygen.html#commands
 *  This file contains the kernel main() function.
 *  @author Fabio Caraffini
*/
package utils.algorithms;


import static utils.MatLab.max;
import static utils.MatLab.min;
import static utils.MatLab.multiply;
import static utils.MatLab.norm2;
import static utils.MatLab.clone;
import utils.random.RandUtils;


/**
 * This class contains useful miscellaneous methods.
*/	
public class Misc
{
	/**
	 * mirroring correction
	 * 
	 * @todo implement mirrroring.
	 * @param x
	 * @param bounds
	 * @return
	 */
	public static double[] mirroring(double[] x, double[][] bounds){double[] x_mirr = new double[666]; System.out.println("IMPLEMENTAMI!"); return x_mirr;}
	/**
	 * Saturation on bounds of the search space.
	 * 
	 * @param x solution to be saturated.
	 * @param bounds search space boudaries.
	 * @return x_tor corrected solution.
	 */
	public static double[] saturate(double[] x, double[][] bounds)
	{
		int n = x.length;
		double[] x_sat = new double[n];
		for (int i = 0; i < n; i++)
			x_sat[i] = min(max(x[i], bounds[i][0]), bounds[i][1]);
		return x_sat;
	}
	/**
	 * Toroidal correction within search space
	 * 
	 * @param x solution to be corrected.
	 * @param bounds search space boundaries (general case).
	 * @return x_tor corrected solution.
	 */
	public static double[] toro(double[] x, double[][] bounds)
	{
		int n = x.length;
		double[] x_tor = new double[n];
		for (int i = 0; i < n; i++)
		{
			x_tor[i] = (x[i]-bounds[i][0])/(bounds[i][1]-bounds[i][0]);
			
			if (x_tor[i] > 1)
				x_tor[i] = x_tor[i]-fix(x_tor[i]);
			else if (x_tor[i] < 0)
				x_tor[i]=1-Math.abs(x_tor[i]-fix(x_tor[i]));
			
			x_tor[i] = x_tor[i]*(bounds[i][1]-bounds[i][0])+bounds[i][0];
		}

		return x_tor;
	}
	/**
	 * Toroidal correction within the search space
	 * 
	 * @param x solution to be corrected.
	 * @param bounds search space boundaries (hyper-parallelepiped).
	 * @return x_tor corrected solution.
	 */
	public static double[] toro(double[] x, double[] bounds)
	{
		int n = x.length;
		double[] x_tor = new double[n];
		for (int i = 0; i < n; i++)
		{
			x_tor[i] = (x[i]-bounds[0])/(bounds[1]-bounds[0]);
			
			if (x_tor[i] > 1)
				x_tor[i] = x_tor[i]-fix(x_tor[i]);
			else if (x_tor[i] < 0)
				x_tor[i]=1-Math.abs(x_tor[i]-fix(x_tor[i]));
			
			x_tor[i] = x_tor[i]*(bounds[1]-bounds[0])+bounds[0];
		}

		return x_tor;
	}
	/**
	 * Clone a solution.
	 * 
	 * @param x solution to be duplicated.
	 * @return xc cloned solution.
	 */
	public static double[] clone(double[] x)
	{
		int n=x.length;
		double[] xc = new double[n];
		for (int i = 0; i < n; i++)
			xc[i] = x[i];
		return xc;
	}
/**
	 * Clone a solution a 2d matrix.
	 * 
	 * @param x solution to be duplicated.
	 * @return xc cloned solution.
	 */
	public static double[][] clone(double[][] x)
	{
		int n=x.length;
		int nn=x[0].length;
		double[][] xc = new double[n][nn];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < nn; j++)
				xc[i][j] = x[i][j];
		return xc;
	}
	/**
	 * Clone a solution.
	 * 
	 * @param x solution to be duplicated.
	 * @return xc cloned solution.
	 */
	public static double[] cloneArray(double[] x)
	{
		int n=x.length;
		double[] xc = new double[n];
		for (int i = 0; i < n; i++)
			xc[i] = x[i];
		return xc;
	}
/**
	 * Clone a solution a 2d matrix.
	 * 
	 * @param x solution to be duplicated.
	 * @return xc cloned solution.
	 */
	public static double[][] cloneArray(double[][] x)
	{
		int n=x.length;
		int nn=x[0].length;
		double[][] xc = new double[n][nn];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < nn; j++)
				xc[i][j] = x[i][j];
		return xc;
	}
	/**
	 * Random point in bounds.
	 * 
	 * @param bounds search space boundaries (general case).
	 * @param n problem dimension.
	 * @return r randomly generated point.
	 */
	public static double[] generateRandomSolution(double[][] bounds, int n)
	{
		double[] r = new double[n];
		for (int i = 0; i < n; i++)
			r[i] = bounds[i][0] + (bounds[i][1]-bounds[i][0])*RandUtils.random();
		return r;
	}
	/**
	 * Random point in bounds.
	 * 
	 * @param bounds search space boundaries (hyper-parallelepiped).
	 * @param n problam dimension.
	 * @return r randomly generated point.
	 */
	public static double[] generateRandomSolution(double[] bounds, int n)
	{
		double[] r = new double[n];
		for (int i = 0; i < n; i++)
			r[i] = bounds[0] + (bounds[1]-bounds[0])*RandUtils.random();
		return r;
	}
	
	/**
	 * Rounds x to the nearest integer towards zero.
	 */
	public static int fix(double x)
	{
		return (int) ((x >= 0) ? Math.floor(x) : Math.ceil(x));  
	}

	/**
	 * Return the centroid of a population of individuals.
	 * 
	 * @param p population.
	 * @return c centroid.
	 */
	public static double[] centroid(double[][] p)
	{
		int dim=p[0].length;
		double[] c= new double[dim];
		int size= p.length;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < dim; j++)
				c[j] += p[i][j];
		return multiply((1/size),c);
	}
	
	/**
	 * Return the orthonormilsed version of n n-dimensional direction vectors according to Gram-Shmidt.
	 * see {@link https://www.researchgate.net/publication/220881848_Solving_nonlinear_optimization_problems_by_Differential_Evolution_with_a_rotation-invariant_crossover_operation_using_Gram-Schmidt_process}
	 * @param p population.
	 * @return c centroid.
	 */
	public static double[][] orthonormalise(double[][] dirVec)
	{
		int n=dirVec.length;
		double[][] coorVec = new double[n][n];
		
		double[] b = new double[n];
		
		b= multiply((1/norm2(dirVec[0])),dirVec[0]);
		coorVec[0]=clone(b);
		double num, den =0;
		for(int i =1; i<n; i++)
		{
			
			//coorVec[i]=
		}
			
			
			
			
		
		return coorVec;
	}
		

}
		
	



// /**
//	 * Saturation with "rebound" on bounds of the search space ??????? MA CHE E' sta stronzata? non funziona!!! CANCELLA
//	 * 
//	 * @param x
//	 * @param bounds
//	 * @return
//	 */
//	public static double[] saturateRebound(double[] x, double[][] bounds)
//	{
//		int n = x.length;
//		double[] x_sat = new double[n];
//		double delta;
//		for (int i = 0; i < n; i++)
//		{
//			x_sat[i] = x[i];
//			while ((x_sat[i] < bounds[i][0]) || (x_sat[i] > bounds[i][1]))
//			{
//				delta = x_sat[i] - bounds[i][1];
//				if (delta > 0)
//					x_sat[i] = bounds[i][1] - delta;
//				delta = x_sat[i] - bounds[i][0];
//				if (delta < 0)
//					x_sat[i] = bounds[i][0] - delta;
//			}
//		}
//		return x_sat;
//	}



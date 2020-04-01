package utils.random;

import utils.algorithms.Counter;

public class RandUtilsISB extends PRNG 
{	
	
	
	
	/**Return a real number with a Cauchy distribution 
	 * @param locationFactor The location factor of the Cauchy distribution.
	 * @param scaleFactor The scale factor of the Cauchy distribution.
	 *  @param counter A counter Object for monitoring the number of PRG activations.
	 *  @return Return a real number with a Cauchy distribution.
	 * **/
	public static double cauchy(double locationFactor, double scaleFactor, Counter counter){counter.incrementCounter(); return cauchy(locationFactor, scaleFactor);}
	/** Return a real number with a Gaussian distribution
	 * @param mean The mean value of the Gaussian distribution.
	 * @param stdDev The standard deviation of the Gaussian distribution.
	 * @param counter A counter Object for monitoring the number of PRG activations.
	 * @return Return a real number with a Gaussian distribution.
	 * */
	public static double gaussian(double mean, double stdDev, Counter counter){counter.incrementCounter(); return gaussian(mean,stdDev);}
	/**Return a real number with a Uniformly distributed number in [a,b].
	 * @param a The lower-bound of the interval.
	 * @param b The upper-bound of the interval.
	 * @param counter A counter Object for monitoring the number of PRG activations.
	 * @return Return a real Uniformly distributed number in [a,b].
	 * */
	public static double uniform(double a, double b, Counter counter){counter.incrementCounter(); return uniform(a,b);}	
	/** Return random uniform number in [0,1)
	 * @param counter A counter Object for monitoring the number of PRG activations.
	 * */
	public static double random(Counter counter){counter.incrementCounter(); return random();}
	/** Random integer in [0,n]. 
	 * @param n The upper-bound of the interval.
	 * @param counter A counter Object for monitoring the number of PRG activations.
	 * @return A random number in [0,n].
	 * */
	public static int randomInteger(int n, Counter counter){return (int) Math.round((n)*random(counter));}
	
	
	/**
	 * Random integer in [a,b] excluding excl.
	 * @param a The lower-bound of the interval.
	 * @param b The upper-bound of the interval.
	 * @param excl This value cannot be returned.
	 * @param counter A counter Object for monitoring the number of PRG activations.
	 * @return Return a Uniformly distributed integer number in [a,b] which is not equal to excl.
	 */
	public static int randomIntegerExcl(int a, int b, int excl, Counter counter)
	{
		int r = excl;
		do
		{
			r = a+randomInteger(b-a, counter);
		}
		while (r == excl);
		return r;
	}
	
	/**
	 * Random integer [0,n], excluding excl.
	 * 
	 * @param n The upper-bound of the interval.
	 * @param counter A counter Object for monitoring the number of PRG activations.
	 * @param excl This value cannot be returned.
	 * @return Return an integer Uniformly distributed number in [a,b].
	 */
	public static int randomIntegerExcl(int n, int excl, Counter counter)
	{
		int r = excl;
		do
		{
			r = randomInteger(n, counter);
		}
		while (r == excl);
		return r;
	}
	
	/**
	 * Random permutation of array elements.
	 * 
	 * @param a
	 * @return
	 */
	public static int[] randomPermutation(int[] a, Counter counter)
	{
		int n = a.length;
		int[] b = (int[])a.clone();
		for (int k = n-1; k > 0; k--)
		{
			int w = (int) Math.floor(random(counter)*(k+1));
			int temp = b[w];
			b[w] = b[k];
			b[k] = temp;
		}
		return b;
	}
	
	/**
	 * Random permutation of array [1, 2, ... , (n-1)]
	 * 
	 * @param n The number of indices to be permuted.
	 * @return A vector with n permuted indices.
	 */
	public static int[] randomPermutation(int n, Counter counter)
	{
		int[] b = new int[n];
		for (int k = 0; k < n; k++)
			b[k] = k;
			
		for (int k = n-1; k > 0; k--)
		{
			int w = (int) Math.floor(random(counter)*(k+1));
			int temp = b[w];
			b[w] = b[k];
			b[k] = temp;
		}
		return b;
	}
	
	/**
	 * Random permutation of array elements, excluding an 
	 * element at the specified index.
	 * 
	 * @param a
	 * @return
	 */
	public static int[] randomPermutationExcl(int[] a, int excludedIndex, Counter counter)
	{
		int n = a.length;
		if (excludedIndex < 0 || excludedIndex >= n)
			return a;
		
		int[] b = new int[n-1];
		int k = 0;
		for (int i = 0; i < n; i++)
		{
			if (i != excludedIndex)
				b[k++] = a[i]; 
		}
		
		for (k = n-2; k > 0; k--)
		{
			int w = (int) Math.floor(random(counter)*(k+1));
			int temp = b[w];
			b[w] = b[k];
			b[k] = temp;
		}
		return b;
	}
	
	
	
	
	

	
}



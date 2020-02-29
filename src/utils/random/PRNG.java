package utils.random;

import java.util.Random;
//import java.util.Vector;
//import utils.algorithms.Counter;
//import java.util.Vector;


public abstract class PRNG
{
	private static enum RNGType {JAVA, MERSENNE_TWISTER, LFSR};
	
	private static Random random;
	private static long seed;
	private static RNGType rngType = RNGType.JAVA;
	
	static
	{
		initializeRandom();
	}

	/**
	 * Initialize RNG with default seed System.currentTimeMillis()
	 */
	private static void initializeRandom()
	{
		initializeRandom(System.currentTimeMillis());
	}
	
	/**
	 * Initialize RNG with specified seed.
	 * @param seed
	 */
	private static void initializeRandom(long seed)
	{
		if (rngType == RNGType.JAVA)
			random = new Random();
		else if (rngType == RNGType.MERSENNE_TWISTER)
			random = new MTwisterRandom();
		else if (rngType == RNGType.LFSR)
			random = new LFSR(16);
		
		random.setSeed(seed);
	}

	
	/**
	 * Return a real number with a Cauchy distribution
	 * 
	 * @param locationFactor
	 * @param scaleFactor
	 */
	public static double cauchy(double locationFactor, double scaleFactor)
	{
		return locationFactor + scaleFactor*Math.tan(Math.PI*(PRNG.random() - 0.5));
	}
	
	/**
	 * Return a real number with a Gaussian distribution
	 * 
	 * @param mean
	 * @param stdDev
	 */
	public static double gaussian(double mean, double stdDev)
	{
		return mean + stdDev*random.nextGaussian();
	}
	
	/**
	 * Return a real number with a Uniform distribution between a and b
	 * 
	 * @param a
	 * @param b
	 */
	public static double uniform(double a, double b)
	{
		return a + (b-a)*PRNG.random();
	}
	
	/**
	 * Set the seed of the RNG
	 * @param s seed of the RNG.
	 */
	public static void setSeed(long seed)
	{
		PRNG.seed = seed;
		initializeRandom(seed);
	}
	
	/**
	 * Get the seed of the RNG
	 * @return
	 */
	public static long getSeed()
	{
		return seed;
	}
		
	/**
	 * Return random uniform number in [0,1)
	 * 
	 * @return
	 */
	public static double random()
	{
		return random.nextDouble();
	}
	
	/**
	 * Gets the RNG
	 * @return
	 */
	public static Random getRNG()
	{
		return random;
	}
	


	
	
	
	/** ******************************** **/
	/** keeping tack of PRG activations **/
	/** ******************************** **/
	
	
//	/** Random integer 0 <= r <= n. **/
//	public static int randomInteger(int n, Counter counter){ counter.incrementCounter(); return randomInteger(n);}
//	
//	/**
//	 * Random integer a <= r <= b, excluding excl.
//	 * 
//	 * @param n
//	 * @return
//	 */
//	public static int randomIntegerExcl(int a, int b, int excl, Counter counter)
//	{
//		int r = excl;
//		do
//		{
//			r = a+PRNG.randomInteger(b-a, counter);
//		}
//		while (r == excl);
//		return r;
//	}
//	
//	/**
//	 * Random integer 0 <= r <= n, excluding excl.
//	 */
//	public static int randomIntegerExcl(int n, int excl, Counter counter)
//	{
//		int r = excl;
//		do
//		{
//			r = PRNG.randomInteger(n, counter);
//		}
//		while (r == excl);
//		return r;
//	}
//	
//	/**
//	 * Random permutation of array elements.
//	 */
//	public static int[] randomPermutation(int[] a, Counter counter)
//	{
//		int n = a.length;
//		int[] b = (int[])a.clone();
//		for (int k = n-1; k > 0; k--)
//		{
//			int w = (int) Math.floor(PRNG.random(counter)*(k+1));
//			int temp = b[w];
//			b[w] = b[k];
//			b[k] = temp;
//		}
//		return b;
//	}
//	
//	/**
//	 * Random permutation of array [1, 2, ... , (n-1)]
//	 */
//	public static int[] randomPermutation(int n, Counter counter)
//	{
//		int[] b = new int[n];
//		for (int k = 0; k < n; k++)
//			b[k] = k;
//			
//		for (int k = n-1; k > 0; k--)
//		{
//			int w = (int) Math.floor(PRNG.random(counter)*(k+1));
//			int temp = b[w];
//			b[w] = b[k];
//			b[k] = temp;
//		}
//		return b;
//	}
//	
//	/**
//	 * Random permutation of array elements, excluding an 
//	 * element at the specified index.
//	 */
//	public static int[] randomPermutationExcl(int[] a, int excludedIndex, Counter counter)
//	{
//		int n = a.length;
//		if (excludedIndex < 0 || excludedIndex >= n)
//			return a;
//		
//		int[] b = new int[n-1];
//		int k = 0;
//		for (int i = 0; i < n; i++)
//		{
//			if (i != excludedIndex)
//				b[k++] = a[i]; 
//		}
//		
//		for (k = n-2; k > 0; k--)
//		{
//			int w = (int) Math.floor(PRNG.random(counter)*(k+1));
//			int temp = b[w];
//			b[w] = b[k];
//			b[k] = temp;
//		}
//		return b;
//	}
//	
//	/**Return a real number with a Cauchy distribution **/
//	public static double cauchy(double locationFactor, double scaleFactor, Counter counter){counter.incrementCounter(); return cauchy(locationFactor, scaleFactor);}
//	/** Return a real number with a Gaussian distribution*/
//	public static double gaussian(double mean, double stdDev, Counter counter){counter.incrementCounter(); return gaussian(mean,stdDev);}
//	/**Return a real number with a Uniform distribution between a and b*/
//	public static double uniform(double a, double b, Counter counter){counter.incrementCounter(); return uniform(a,b);}	
//	/** Return random uniform number in [0,1)*/
//	public static double random(Counter counter){counter.incrementCounter(); return random.nextDouble();}
	


	
	
	
	
	
	
	
	
	
}
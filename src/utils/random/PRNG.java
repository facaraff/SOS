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
	 * @param seed The seed of the RNG.
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
}
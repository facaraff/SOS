package utils.random;

import java.util.Random;

@SuppressWarnings("serial")
public class MTwisterRandom extends Random
{
	private static MTwister generator;
	
	static
	{
		generator = new MTwister();
	}
	
	public void setSeed(long seed)
	{
		generator.init_genrand(seed);
	}
	
	// pseudo-random float value between 0 and 1, excluding 1
	public double nextDouble() {
		return (generator.genrand_res53());
	}
	
	// pseudo-random float value between 0 and 1, excluding 0 and 1
	public double nextDoubleOpen() {
		return (generator.genrand_real3());
	}
	
	// pseudo-random float value between 0 and 1 inclusive
	public double nextDoubleClosed() {
		return (generator.genrand_real1());
	}
	
	// pseudo-random Gaussian float value
	public double nextGaussian()
	{
		return (generator.genrand_gaussian());
	}
}
package utils.random;

import java.util.Random;

@SuppressWarnings("serial")
public final class LFSR extends Random
{
	private int nBits;
	private int[] taps;
	private boolean[] bits;
	
	private int MAX_VALUE;
	
	private boolean haveNextNextGaussian = false;
	private double nextNextGaussian;
	
	private static final double twoPI = 2*Math.PI;

	public LFSR(int nBits) {
		this.nBits = nBits;
		// the following taps allow the maximum period
		if (nBits == 8)
			this.taps = new int[] {8,6,5,4};
		else if (nBits == 16)
			this.taps = new int[] {16,15,13,4};
		else if (nBits == 32)
			this.taps = new int[] {32,22,2,1};
		else if (nBits == 64)
			this.taps = new int[] {64,63,61,60};
		
		this.bits = new boolean[nBits + 1];
		this.MAX_VALUE = (int) (Math.pow(2,nBits)-1);
	}

	public void setSeed(long seed) {
		if (bits != null)
		{
			for(int i = 0; i < nBits; i++) {
				bits[i] = (((1 << i) & seed) >>> i) == 1;
			}
		}
	}
	
	public int nextInt() {
		// calculate the integer value from the registers
		int next = 0;
		for(int i = 0; i < nBits; i++) {
			next |= (bits[i] ? 1 : 0) << i;
		}

		// allow for zero without allowing for -2^31
		if (next < 0) next++;

		// calculate the last register from all the preceding
		bits[nBits] = false;
		for(int i = 0; i < taps.length; i++) {
			bits[nBits] ^= bits[nBits - taps[i]];
		}

		// shift all the registers
		for(int i = 0; i < nBits; i++) {
			bits[i] = bits[i + 1];
		}

		return next;
	}

	public double nextDouble() {
		return (double)nextInt() / (MAX_VALUE+1);
	}

	public boolean nextBoolean() {
		return nextInt() >= 0;
	}

	public double nextGaussian() {
		if (haveNextNextGaussian) {
			haveNextNextGaussian = false;
			return nextNextGaussian;
		} else {
			// http://stackoverflow.com/questions/75677/converting-a-uniform-distribution-to-a-normal-distribution
			// http://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
			
			// Box-Muller transformation (polar form)
			/*
			double v1, v2, s;
			do { 
				v1 = 2 * nextDouble() - 1;   // between -1.0 and 1.0
				v2 = 2 * nextDouble() - 1;   // between -1.0 and 1.0
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			double multiplier = Math.sqrt(-2 * Math.log(s)/s);
			nextNextGaussian = v2 * multiplier;
			haveNextNextGaussian = true;
			return v1 * multiplier;
			*/
			
			// Box-Muller transformation (basic form)
			double u1, u2;
			do {
				u1 = nextDouble();
				u2 = nextDouble();
			}
			while (u1 == 0 || u2 == 0);
			double R = Math.sqrt(-2*Math.log(u1));
			double theta = twoPI*u2;
			nextNextGaussian = R*Math.sin(theta);
			haveNextNextGaussian = true;
			return R*Math.cos(theta);
		}
	}
	
	public void printBits() {
		System.out.print(bits[nBits] ? 1 : 0);
		System.out.print(" -> ");
		for(int i = nBits - 1; i >= 0; i--) {
			System.out.print(bits[i] ? 1 : 0);
		}
		System.out.println();
	}
}
package utils.random;

import java.util.Vector;

public class RandUtils extends PRNG 
{	
	
	
	/**
	 * Random integer 0 <= r <= n.
	 * 
	 * @param n
	 * @return
	 */
	public static int randomInteger(int n)
	{
		return (int) Math.round((n)*PRNG.random());
	}
	
	/**
	 * Random integer a <= r <= b, excluding excl.
	 * 
	 * @param n
	 * @return
	 */
	public static int randomIntegerExcl(int a, int b, int excl)
	{
		int r = excl;
		do
		{
			r = a+randomInteger(b-a);
		}
		while (r == excl);
		return r;
	}
	
	/**
	 * Random integer 0 <= r <= n, excluding excl.
	 * 
	 * @param n
	 * @return
	 */
	public static int randomIntegerExcl(int n, int excl)
	{
		int r = excl;
		do
		{
			r = randomInteger(n);
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
	public static int[] randomPermutation(int[] a)
	{
		int n = a.length;
		int[] b = (int[])a.clone();
		for (int k = n-1; k > 0; k--)
		{
			int w = (int) Math.floor(PRNG.random()*(k+1));
			int temp = b[w];
			b[w] = b[k];
			b[k] = temp;
		}
		return b;
	}
	
	/**
	 * Random permutation of array [1, 2, ... , (n-1)]
	 * 
	 * @param algorithm
	 * @return
	 */
	public static int[] randomPermutation(int n)
	{
		int[] b = new int[n];
		for (int k = 0; k < n; k++)
			b[k] = k;
			
		for (int k = n-1; k > 0; k--)
		{
			int w = (int) Math.floor(PRNG.random()*(k+1));
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
	public static int[] randomPermutationExcl(int[] a, int excludedIndex)
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
			int w = (int) Math.floor(PRNG.random()*(k+1));
			int temp = b[w];
			b[w] = b[k];
			b[k] = temp;
		}
		return b;
	}
	
	
	
	
	
	/**
	 * Test main
	 * @param args
	 */
	public static void main(String[] args) {
		Vector<Double> vec = new Vector<Double>();
		for(int i = 0; i <= 32766; i++) {
			double next = getRNG().nextDouble();
			if (vec.contains(next))
				throw new RuntimeException("Index repeat: " + i);
			vec.add(next);
			System.out.println(next);
		}
	}
	
}



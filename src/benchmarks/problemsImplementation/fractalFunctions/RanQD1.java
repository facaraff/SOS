package benchmarks.problemsImplementation.fractalFunctions;
//
//  RanQD1.java
//  FractalFunctions
//
//  Created by Cara MacNish on 20/10/07.
//  Copyright 2007 CSSE, UWA. All rights reserved.
//
//  This source is distributed under GPL3.0. See ../index.html
//  for important information on modifying and distributing.

/**
 * This class implements the (repeatable) "quick and dirty" portable pseudo-random 
 * generator with modulus 2<sup>32</sup> in
 * W. H. Press et al, "Numerical Recipes in C: The Art of Scientific Computing", 
 * Cambridge University Press, 2nd Ed., 1992, with values recommended by Knuth.
 * It is the fastest generator recommended in the above tome.
 * <p>
 * The original relies on the fact that in C on a 32-bit machine, multiplying two unsigned long ints
 * returns the lower 32 bits of the 64 bit product. Since we are using Java (no unsigned ints)
 * and a 64-bit architecture, we mimic this using 64 bit longs with bit-masking instead.
 * @author Cara MacNish, University of Western Australia
 * @version 1.0RC1, 7th Nov 2007
 */
 public class RanQD1 {

  final static long MASK = 0xffffffffl;         // lower order 32 bits of long
  final static double MAX_INT = 4294967295.0;   // 2^32-1 (MASK as a double)
  final static long A = 1664525l;               // suggested by Knuth
  final static long C = 1013904223l;            // suggested by Lewis
  
  private long idum;

  /**
   * Create a new pseudo-random generator.
   * @param seed the seed
   */
  public RanQD1 (long seed) {
    idum = seed;
    nextLong();                              // one multiple to scatter seeds
  }
  
  /**
   * Reset the seed (quicker than creating a new instance).
   * @param seed the seed
   */
  public void setSeed (long seed) {
    idum = seed;
    nextLong();                              // one multiple to scatter seeds
  }
  
  /**
   * Get the next long.
   */
  public long nextLong () {
    idum = (A * idum + C) & MASK;
    return idum;
  }
  
  /**
   * Get the next double.
   */
  public double nextDouble () {
    return nextLong()/MAX_INT;
  }  

  /**
  * Get an integer with equal probability from [min, max] inclusive.
  */
  public int nextInt (int min, int max) {
    return min + (int) Math.floor(nextDouble()*(max-min+1));
  }    
}

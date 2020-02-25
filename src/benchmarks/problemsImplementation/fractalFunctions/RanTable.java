package benchmarks.problemsImplementation.fractalFunctions;
//
//  RanTable.java
//  FractalFunctions
//
//  Created by Cara MacNish on 23/10/07.
//  Copyright 2007 CSSE, UWA. All rights reserved.
//
//  This source is distributed under GPL3.0. See ../index.html
//  for important information on modifying and distributing.

/**
 * For very high dimensional functions (eg 5M evaluations of a 1000 dimension function), re-seeding
 * random generators, generating the random numbers and mapping them into the right range on the fly
 * is too slow, even for {@link RanQD1}. 
 *<p>
 * This companion class pre-generates the random numbers needed by {@link FractalFunction1D} 
 * (in turn called by {@link FastFractal}) and stores them in lookup tables. Changing the index
 * simply becomes resetting the array index.
 *<p>
 * This results in a small loss of statistical diversity for a considerable gain in speed, even 
 * for quite large tables (eg 16K) since it is only done once for any given function.
 *<p>
 * The user does not need to access this class directly, all values are set by its owner, such
 * as {@link FractalFunction1D}.
 * @author {@link <a href="http://www.csse.uwa.edu.au/~cara/">Cara MacNish</a>}, University of Western Australia
 * @version 1.0RC1, 7th Nov 2007
 * <br>For the latest version and additional information see the
 * {@link <a href="http://www.cs.bham.ac.uk/research/projects/ecb/">Birmingham Repository</a>}
 */                                                                                  
public class RanTable {
  private double[] doubleTable;
  private int doubleTableIndex = 0;
  private int doubleTableSize;
  private int[] intTable;
  private int intTableIndex = 0;
  private int intTableSize;

  RanQD1 ran;

  /**
   * Create the tables and populate with appropriate random values.
   * @param doubleTableSize size of the table for double values
   * @param intTableSize size of the table for int values
   * @param aveInt average int value, this will be set from the density
   * @param index the index or seed for the tables
   */
  public RanTable (int doubleTableSize, int intTableSize, int aveInt, long index) {
    this.doubleTableSize = doubleTableSize;
    this.intTableSize = intTableSize;
    ran = new RanQD1(index);

    doubleTable = new double[doubleTableSize]; 
    for (int i=0; i<doubleTableSize; i++)  doubleTable[i] = ran.nextDouble();
    ran.setSeed(index);

    intTable = new int[intTableSize];
    for (int i=0; i<intTableSize; i++)  intTable[i] = ran.nextInt(0, 2*aveInt);
    ran.setSeed(index);
  }

  /** 
   * Reset the seed.
   */
  public void setSeed (long seed) {
    doubleTableIndex = (int) (seed & (doubleTableSize-1));
    intTableIndex = (int) (seed & (intTableSize-1));
  } 

  /**
   * Get the next double.
   */
  public double nextDouble () {
    doubleTableIndex = (doubleTableIndex+1) & (doubleTableSize-1);
    return doubleTable[doubleTableIndex];
  }
  
  /** 
   * Get the next int
   */
  public int nextInteger () {
    intTableIndex = (intTableIndex+1) & (intTableSize-1);
    return intTable[intTableIndex];
  }
}

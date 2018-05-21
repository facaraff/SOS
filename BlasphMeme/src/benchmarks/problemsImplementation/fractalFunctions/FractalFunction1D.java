package benchmarks.problemsImplementation.fractalFunctions;
//
//  FractalFunction1D.java
//  FractalFunctions
//
//  Created by Cara MacNish on 18/10/07.
//  Copyright 2007 CSSE, UWA. All rights reserved.
//
//  This source is distributed under GPL3.0. See ../index.html
//  for important information on modifying and distributing.

/** 
 * This class is called by {@link FastFractal} to evaluate the components for each dimension to
 * evaluate fast very high-dimensional fractal functions (landscapes).
 * <p>
 * It can also be used directly to generate 1-dimensional fractal landscapes.
 * @author {@link <a href="http://www.csse.uwa.edu.au/~cara/">Cara MacNish</a>}, University of Western Australia
 * @version 1.0RC1, 7th Nov 2007
 * <br>For the latest version and additional information see the
 * {@link <a href="http://www.cs.bham.ac.uk/research/projects/ecb/">Birmingham Repository</a>}
 */
public class FractalFunction1D {
  
  // random tables
  final static int DOUBLE_TABLE_SIZE = 0x3fff+1;   // 16384
  final static int INT_TABLE_SIZE = 0xff+1;        //   256
  private RanTable ranTable;
  
  // declare constructor parameters with some default values, these ones chosen for speed
  // for very high dimensional, use higher values for challenging problem in one dimension only
  private int fractalDepth = 3;  // maximum recursive depth of 40 suggested for 64-bit architecture
  private int density = 1;
  private long index = 1;
  private UnitFunction1D unitFunction;
  
  // Constructors
  
  /**
   * Create a new 1D fast fractal function generator.
   * @param unitFunction the base function for this generator
   * @param fractalDepth recursive depth of fractal - each increment adds detail at half the scale
   * (double the resolution).
   * Must be between 1 and 2^64 although in practice maximum supported by IEEE 64-bit floating point
   * is in the low 40s. Recommend maximum of 40.
   * @param density average number of base functions per unit area at each resolution
   * @param index the sequence number of this surface (for the given fractal depth and density)
   */
  public FractalFunction1D (UnitFunction1D unitFunction, int fractalDepth, int density, long index) {
    this.unitFunction = unitFunction;
    this.fractalDepth = fractalDepth;
    this.density = density;
    this.index = index;
    ranTable = new RanTable(DOUBLE_TABLE_SIZE, INT_TABLE_SIZE, density, index);
  }
  
  /**
   * Create a new 1D fast fractal function generator using default values (fractal depth = 3).
   * @param unitFunction the base function for this generator
   * @param density average number of base functions per unit area at each resolution
   * @param index the sequence number of this surface (for the given fractal depth and density)
   */
  public FractalFunction1D (UnitFunction1D unitFunction, int density, long index) {
    this.unitFunction = unitFunction;
    this.density = density;
    this.index = index;
  }
  
  /**
   * Create a new 1D fast fractal function generator using default values (fractal depth = 3, density = 1).
   * @param unitFunction the base function for this generator
   * @param index the sequence number of this surface (for the given fractal depth and density)
   */
  public FractalFunction1D (UnitFunction1D unitFunction, long index) {
    this.unitFunction = unitFunction;
    this.index = index;
  }
  
  /**
   * Create a new 1D fast fractal function generator using default values 
   * (fractal depth = 3, density = 1, index = 1).
   * @param unitFunction the base function for this generator
   */
  public FractalFunction1D (UnitFunction1D unitFunction) {
    this.unitFunction = unitFunction;
  }
  
  /**
   * Create a new 1D fast fractal function generator using default values 
   * (unitFunction = DoubleDip, fractal depth = 3, density = 1, index = 1).
   */
  public FractalFunction1D () {
    this.unitFunction = new DoubleDip();
  }
  
  
  /**
   * Create a new generator in the same series by resetting the index (faster than creating a new object).
   * @param index the new index (sequence number).
   */
  public void setIndex(long index) {
    this.index = index;
    ranTable.setSeed(index);
  }


  /**
   * Evaluate the function at the given co-ordinate.
   * @param x the point at which to evaluate
   * @return the value at that point
   */
  public double evaluate (double x) {
    x = x % 1;                     // first map pos into (0,1], (0,1]
                                   // note in Java -4.3%1 is -0.3 not 0.7 ie Matlab 'rem' function not 'mod'
    if (x <= 0)  x = x+1;          // 0 must move to 1, or will be in wrong "square"
    if (fractalDepth<1) return 0;  // check for valid depth argument, should never fire
    else return getDepthLocal (x, 1, index, 1);  // start recursion
  }
 

  private double getDepthLocal (double x, int recDepth, long seed, long span) {
    double depth = 0;
    double scale = 1.0/span;
    long square =  (long) Math.ceil(x*span);
    long newSeed, square1;
    double x1;
    // get contribution from each of the 3 relevant squares...
    for (int offset = -1; offset<2; offset++) {
      x1 = x;
      square1 = square + offset;
      if (square1 == 0) {           // wrap to r.h.s.
        square1 = span;
        x1 = x1 + 1;
      }
      else if (square1 > span) {    // wrap to l.h.s.
        square1 = 1;
        x1 = x1 - 1;
      }
      depth = depth + getDepthWRTSquare(x1, square1, recDepth, seed, span, scale);  // accumulate contributions
    }
    // now fire recursion to next level down...
    if (recDepth < fractalDepth) {
      newSeed = (span + seed) & (DOUBLE_TABLE_SIZE-1);              // unique seeds up to random table size
      long newSpan = span << 1;                                     // newSpan = 2^(recDepth-1), bit shift faster
      depth = depth + getDepthLocal(x,recDepth+1,newSeed,newSpan);  // recur to next level
    }
    return depth;
  }
  
  
  private double getDepthWRTSquare (double x, long square, int recDepth, long seed, long span, double scale) {
    double depth = 0;
    long squareSeed = (square-1);                                  // unique seed for this square
    long localSeed = (seed + squareSeed) & (DOUBLE_TABLE_SIZE-1);  // unique seed for square at depth up to table size
    ranTable.setSeed(localSeed);                                   // apply seed for this square
    int numUnits = ranTable.nextInteger();       // choose number of unit functions from uniform dist whose average is 'density'
    for (int i=1; i<=numUnits; i++) {            // get contribution from each
      double diameter = 1/(2 - ranTable.nextDouble()) * scale;     // get diameter from quadratic distribution
      double centre = (square - ranTable.nextDouble()) * scale;    // get centre from uniform distribution
      if ( (x-centre)*(x-centre) < diameter*diameter/4) {    // save making unnecessary call if unit function is too far to affect point
        unitFunction.setCentre(centre);          // faster to set individually (believe it or not)
        unitFunction.setScale(diameter);
        depth = depth+unitFunction.getValue(x);  // add this unit function's contribution
      }
    }
    return depth;
  }
  
  public int getDensity()
  {
	  return density;
  }

} 
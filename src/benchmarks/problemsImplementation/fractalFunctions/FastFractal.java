package benchmarks.problemsImplementation.fractalFunctions;
//
//  FastFractal.java
//  FractalFunctions
//
//  Created by Cara MacNish on 19/10/07.
//  Copyright 2007 CSSE, UWA. All rights reserved.
//
//  This source is distributed under GPL3.0. See ../index.html
//  for important information on modifying and distributing.

/**
 * This is the top level class (called by the user) for generating fast multidimensional 
 * fractal functions.
 *<p>
 * Each function (or "landscape") is made from a base function (or unit function) at various scales.
 * For the fast multidimensional functions these are subclasses of { UnitFunction1D}.
 *<p>
 * The choice of base function determines a class of fractal functions. 
 * Each instance within that class is then determined by three parameters: 
 * the fractal depth, the density, and a sequence number.
 *<p>
 * For the motivation behind the fractal functions see: 
 * MacNish, C., Towards Unbiased Benchmarking of Evolutionary and Hybrid Algorithms for Real-valued 
 * Optimisation, http://www.tandf.co.uk/journals/titles/09540091.asp Connection Science,
 * Vol. 19, No. 4, December 2007. Or visit Cara MacNish's website http://www.csse.uwa.edu.au/~cara/.
 * @see UnitFunction1D
 * @author Cara MacNish, http://www.csse.uwa.edu.au/~cara/, University of Western Australia
 * @version 1.0RC1, 7th Nov 2007
 * For the latest version and additional information see the
 * http://www.cs.bham.ac.uk/research/projects/ecb/ Birmingham Repository
 */
public class FastFractal {
  
  private UnitFunction1D unitFunction;
  private int dimensions;
  private FractalFunction1D ff;
  
  /**
   * Create a fast fractal function generator.
   * @param unitFunctionName the name of the base function for the generator. It must match the
   * class name of a subclass of {UnitFunction1D}.
   * @param fractalDepth recursive depth of fractal - each increment adds detail at half the scale
   * (double the resolution).
   * Must be between 1 and 48 (the maximum supported by IEEE 64-bit floating point resolution).
   * @param density average number of base functions per unit area at each resolution
   * @param index the sequence number of this surface (for the given fractal depth and density) 
   * @param dimensions number of dimensions (free variables) of the parameter space
   * @throws Exception This method must be able to handle exceptions. 
   */
  public FastFractal (String unitFunctionName, int fractalDepth, int density, long index, int dimensions) throws Exception {
    unitFunction = (UnitFunction1D) Class.forName(unitFunctionName).newInstance();
    this.dimensions = dimensions;
    ff = new FractalFunction1D(unitFunction, fractalDepth, density, index);
  }
  
/**
 * Evaluate the function at the given point.
 * @param point the point to evaluate. The size of the array must match the dimension of the problem.
 * point[0] is co-ordinate x1, point[1] is co-ordinate x2, ..., point [D-1] is co-ordinate xD, where
 * D is the dimension.
 * @return the value
 */
  public double evaluate (double[] point) {
    if (point.length != dimensions) throw new RuntimeException("Point does not have "+dimensions+" dimensions.");
    double depth = 0;
    double x, lastx, dx;
    ff.setIndex((6*dimensions-1)+1);
    lastx = point[dimensions-1];
    for (int i=0; i<dimensions; i++) {
      ff.setIndex(6*i+1);                   // spread to small "prime-ish" seeds for diversity
      x = point[i];
      dx = unitFunction.twist(x,lastx);
      depth = depth + ff.evaluate(x+dx);    // "twist" and evaluate
      lastx = x;
    }
    return depth;
  }

  /**
   * Evaluate the function on an array of points.
   * @param points the points to evaluate. The array must be size nxD where n is the number of points
   * to evaluate and D is the dimension.
   * @return an array of the n values
   */
  public double[] evaluate (double[][] points) {
    if (points[0].length != dimensions) throw new RuntimeException("Point does not have "+dimensions+" dimensions.");
    double[] results = new double[points.length];
    for (int i=0; i<points.length; i++)  results[i] = evaluate(points[i]);
    return results;
  }
  
}
    
    
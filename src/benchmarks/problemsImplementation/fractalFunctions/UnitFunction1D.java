package benchmarks.problemsImplementation.fractalFunctions;
//
//  UnitFunction1D.java
//  FractalFunctions
//
//  Created by Cara MacNish on 18/10/07.
//  Copyright 2007 CSSE, UWA. All rights reserved.
//
//  This source is distributed under GPL3.0. See ../index.html
//  for important information on modifying and distributing.

/**
 * Each fractal function is constructed from base functions or "unit functions" at various scales
 * (a little like wavelets) chosen from appropriate probability distributions to preserve 
 * self-similarity. 
 *<p>
 * The name <i>unit function</i> comes from the fact that the non-zero portion of each function
 * is constrained to a unit hypercube, centred at the origin. In the 1-d case this is therefore the
 * interval (-0.5, 0.5).
 *<p>
 * This is the superclass of all 1-d unit functions. If you are writing your own unit
 * function you only need to call the constructors from your own constructors using
 * super(), and provide an implementation for getValue(point).
 * 
 * @author Cara MacNish, University of Western Australia
 * @version 1.0RC1, 7th Nov 2007
 * For the latest version and additional information see the
 */
public abstract class UnitFunction1D {
  
  protected double centre=0;
  protected double scale=1;


  /**
   * Construct a default unit function whose centre and scale will be set later - only called
   * by subclasses.
   */
  public UnitFunction1D () {
  }
  
  /**
   * Construct a unit function - only called by subclasses.
   * @param centre the x-axis value to which the centre of this unitfunction is mapped
   * @param scale the factor by which this unit function is scaled
   */
  public UnitFunction1D (double centre, double scale) {
    setCentre(centre);
    setScale(scale);
  }

  /**
   * Set the location and scale at which this unit function is applied.
   * @param centre the x-axis value to which the centre of this unitfunction is mapped
   * @param scale the factor by which this unit function is scaled
   */
  public void setParams (double centre, double scale) {
    this.centre = centre;
    this.scale = scale;
  }
  
  /**
   * Set the location at which this unit function is applied.
   * @param centre the x-axis value to which the centre of this unitfunction is mapped
   */
  public void setCentre (double centre) {
    this.centre = centre;
  }
  
  /**
   * Get the location of this unit function.
   * @return the x-axis value to which the centre of this unitfunction is mapped
   */
  public double getCentre () {
    return centre;
  }
  
  /**
   * Set the scale at which this unit function is applied.
   * @param scale the factor by which this unit function is scaled
   */
  public void setScale (double scale) {
    this.scale = scale;
  }
  
  /**
   * Get the scale of this unit function.
   * @return the factor by which this unit function is scaled
   */
  public double getScale () {
    return scale;
  }
  

  /**
   * Evalutate this unit function at the given x-value.    
   * @param point the point at which this function is evaluated
   * @return the value
   */
  public abstract double getValue (double point);
  
  
  public double twist (double x, double y) {
    return 0;
  }
  
  /**
   * Convenience method to get the name of this function as a String.
   * @return The name of "this" function as a String
   */
  public String getName () {
    return this.getClass().getName();
  }
  
}

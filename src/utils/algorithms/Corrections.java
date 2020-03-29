package utils.algorithms;

import static utils.MatLab.max;
import static utils.MatLab.min;
import static utils.algorithms.Misc.*;

import interfaces.Problem;
import utils.random.RandUtils;

public class Corrections 
{

	//*******************************************************************
	//****  NON USATA AL MOMENTO, DEVO SPOSTARE LE CORREZZIONI QUI ******
	//*******************************************************************
	
	/**
	 * complete one tailed normal correction
	 * 
	 * implement complete one tailed normal correction.
	 * 
	 * @param x
	 * @param bounds
	 * @return corrected x
	 */
	public static double[] completeOneTailedNormal(double[] x, double[][] bounds, double scaleFactor) 
	{
		int n = x.length;
		double[] x_complete = new double[n];
		for (int i = 0; i < n; i++)
			x_complete[i] = completeOneTailedNormalRecursive(x[i], bounds[i][0], bounds[i][1], scaleFactor);
		return x_complete;
	}
	public static double[] completeOneTailedNormal(double[] x, double[] bounds, double scaleFactor)
	{
		double[][] BOUNDS = new double[x.length][2];
		for(int i=0; i<x.length; i++)
		{
			BOUNDS[i][1] = bounds[0];
			BOUNDS[i][1] = bounds[1];
		}	
		return completeOneTailedNormal(x, BOUNDS,scaleFactor);
	}

	protected static double completeOneTailedNormalRecursive(double x, double lb, double ub, double scaleFactor) {
		double x_complete = Double.NaN;
		if (inDomain(x, lb, ub))
			x_complete = x;
		else {
			x_complete = generateOneTailedNormalValue(x, lb, ub, scaleFactor);
			while (!inDomain(x_complete, lb, ub))
				x_complete = generateOneTailedNormalValue(x_complete, lb, ub, scaleFactor);

		}
		return x_complete;
	}

	protected static double generateOneTailedNormalValue(double x, double lb, double up, double scaleFactor)// N.B. to
																											// use after
																											// inDomain()!!!
	{
		double r = Math.abs(RandUtils.gaussian(0, (up - lb) / scaleFactor));
		return (x < lb) ? (lb + r) : (up - r);
	}

	/**
	 * mirroring correction
	 * 
	 * @todo implement mirrroring.
	 * @param x
	 * @param bounds
	 * @return
	 */
	public static double[] mirroring(double[] x, double[][] bounds) {
		int n = x.length;
		double[] x_mirrored = new double[n];
		for (int i = 0; i < n; i++)
			x_mirrored[i] = mirror_recursive(x[i], bounds[i][0], bounds[i][1]);
		return x_mirrored;
	}
	public static double[] mirroring(double[] x, double[] bounds)
	{
		double[][] BOUNDS = new double[x.length][2];
		for(int i=0; i<x.length; i++)
		{
			BOUNDS[i][1] = bounds[0];
			BOUNDS[i][1] = bounds[1];
		}	
		return mirroring(x, BOUNDS);
	}

	// %%%%%
	protected static double reflect(double x, double lb, double up) {
		double x_ref = Double.NaN;
		if (x > up)
			x_ref = up - (x - up);
		if (x < lb)
			x_ref = lb + (lb - x);
		return x_ref;
	}

	protected static double mirror(double x, double lb, double ub) {
		double x_mirr = Double.NaN;
		if (inDomain(x, lb, ub))
			x_mirr = x;
		else {
			x_mirr = reflect(x, lb, ub);
			while (!inDomain(x_mirr, lb, ub))
				x_mirr = reflect(x_mirr, lb, ub);

		}
		return x_mirr;
	}

	protected static double mirror_recursive(double x, double lb, double ub) {
		double x_mirr = Double.NaN;
		if (inDomain(x, lb, ub))
			x_mirr = x;
		else {
			x_mirr = reflect(x, lb, ub);
			x_mirr = mirror_recursive(x_mirr, lb, ub);

		}
		return x_mirr;
	}

	// %%%
	/**
	 * Saturation on bounds of the search space.
	 * 
	 * @param x
	 *            solution to be saturated.
	 * @param bounds
	 *            search space boudaries.
	 * @return x_tor corrected solution.
	 */
	public static double[] saturate(double[] x, double[][] bounds) {
		int n = x.length;
		double[] x_sat = new double[n];
		for (int i = 0; i < n; i++)
			x_sat[i] = min(max(x[i], bounds[i][0]), bounds[i][1]);
		return x_sat;
	}
	
	/**
	 * Saturation for BIAS study. (to check if this is the same as Saturate!1!)
	 * 
	 * @param x
	 *            solution to be saturated.
	 * @param bounds
	 *            search space boudaries.
	 * @return x_tor corrected solution.
	 */
	public static double[] saturation(double[] x, double[][] bounds)
	{
		double[] xs = new double[x.length];
		for(int i=0; i<x.length; i++)
		{
			if(x[i]>bounds[i][1])
				xs[i] = bounds[i][1];
			else if(x[i]<bounds[i][0])
				xs[i] = bounds[i][0];
			else
				xs[i] = x[i];
		}		
		return xs;
	}
	/**
	 * second version
	 */
	public static double[] saturation(double[] x, double[] bounds)
	{
		double[][] BOUNDS = new double[x.length][2];
		for(int i=0; i<x.length; i++)
		{
			BOUNDS[i][1] = bounds[0];
			BOUNDS[i][1] = bounds[1];
		}	
		return saturation(x, BOUNDS);
	}

	/**
	 * Toroidal correction within search space
	 * 
	 * @param x
	 *            solution to be corrected.
	 * @param bounds
	 *            search space boundaries (general case).
	 * @return x_tor corrected solution.
	 */
	public static double[] toro(double[] x, double[][] bounds) {
		int n = x.length;
		double[] x_tor = new double[n];
		for (int i = 0; i < n; i++) {
			x_tor[i] = (x[i] - bounds[i][0]) / (bounds[i][1] - bounds[i][0]);

			if (x_tor[i] > 1)
				x_tor[i] = x_tor[i] - fix(x_tor[i]);
			else if (x_tor[i] < 0)
				x_tor[i] = 1 - Math.abs(x_tor[i] - fix(x_tor[i]));

			x_tor[i] = x_tor[i] * (bounds[i][1] - bounds[i][0]) + bounds[i][0];
		}

		return x_tor;
	}

	/**
	 * Toroidal correction within the search space
	 * 
	 * @param x
	 *            solution to be corrected.
	 * @param bounds
	 *            search space boundaries (hyper-parallelepiped).
	 * @return x_tor corrected solution.
	 */
	public static double[] toro(double[] x, double[] bounds) {
		int n = x.length;
		double[] x_tor = new double[n];
		for (int i = 0; i < n; i++) {
			x_tor[i] = (x[i] - bounds[0]) / (bounds[1] - bounds[0]);

			if (x_tor[i] > 1)
				x_tor[i] = x_tor[i] - fix(x_tor[i]);
			else if (x_tor[i] < 0)
				x_tor[i] = 1 - Math.abs(x_tor[i] - fix(x_tor[i]));

			x_tor[i] = x_tor[i] * (bounds[1] - bounds[0]) + bounds[0];
		}

		return x_tor;
	}

	/**
	 * Discard and re-sample within the search space.
	 * 
	 * @param x
	 *            solution to be saturated.
	 * @param bounds
	 *            search space boundaries.
	 * @return x_res corrected solution.
	 */
	public static double[] discardAndResample(double[] x, double[][] bounds) {
		int n = x.length;
		int i = 0;
		while (i < n && x[i] >= bounds[0][i] && x[i] <= bounds[1][i])
			i++;
		if (i != n)
			x = generateRandomSolution(bounds, n);
		return x;
	}
	

	/**
	 * Note: since the Powell algorithm is meant for unconstrained optimization, we
	 * need to introduce a penalty factor for solutions outside the bounds.
	 * 
	 * @param x
	 * @param bounds
	 * @param PENALTY
	 * @return
	 * @throws Exception
	 */
	public static double fConstraint(double[] x, double[][] bounds, double PENALTY, Problem p) throws Exception {

		boolean outsideBounds = false;
		int n = bounds.length;
		for (int j = 0; j < n && !outsideBounds; j++) {
			if (x[j] < bounds[j][0] || x[j] > bounds[j][1])
				outsideBounds = true;
		}

		double orzobimbo;

		if (outsideBounds)
			orzobimbo = PENALTY;
		else
			orzobimbo = p.f(x);

		return orzobimbo;
	}
	
	

	
	
	/**
	 * Correction functions wrapper method
	 * 
	 * @param correction correction strategy to use.
	 * @param x solution to be corrected.
	 * @param bounds search space boundaries (general case).
	 * @return x_c corrected solution.
	 */
	public static double[] correct(char correction, double[] x, double[][] bounds) 
	{
		int n = x.length;
		double[] x_c = new double[n];
		
		switch (correction)
		{
			case 't':
				// toroidal
				x_c = toro(x, bounds);
				break;
			case 's':
				// saturation
				x_c = saturation(x, bounds);
				break;
			case 'm':
				//mirroring
				x_c = mirroring(x, bounds);
				break;
			case 'c':
				// complete one tailed normal correction
				x_c =completeOneTailedNormal(x, bounds, 3.0);
				break;
			default:
				System.out.println("No valid bounds handling scheme selected");
				break;
		}
		
		return x_c;
	}
	
	
	
	
}

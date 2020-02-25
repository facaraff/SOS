package utils.algorithms;

import static utils.MatLab.max;
import static utils.MatLab.min;
import static utils.algorithms.Misc.*;

public class Corrections 
{

	//*******************************************************************
	//****  NON USATA AL MOMENTO, DEVO SPOSTARE LE CORREZZIONI QUI ******
	//*******************************************************************
	
	/**
	 * mirroring correction
	 * 
	 * @todo implement mirrroring.
	 * @param x
	 * @param bounds
	 * @return
	 */
	public static double[] mirroring(double[] x, double[][] bounds)
	{
		int n = x.length;
		double[] x_mirrored = new double[n];
		for (int i = 0; i < n; i++)
			x_mirrored[i] = mirror_recursive(x[i], bounds[i][0],bounds[i][1]); 	
		return x_mirrored;
	}
	//%%%%%
	protected static boolean inDomain(double x, double lb, double up) {return (x<=up && x>=lb);}
	protected static double reflect(double x, double lb, double up)
	{
		double x_ref = Double.NaN;
		if(x>up) x_ref = up - (x-up);
		if(x<lb) x_ref = lb + (lb-x);
		return x_ref;
	}
	protected static double mirror(double x, double lb, double ub) 
	{
		double x_mirr = Double.NaN;
		if(inDomain(x,lb,ub)) 
			x_mirr = x;
		else
		{
			x_mirr = reflect(x, lb, ub);
			while(!inDomain(x_mirr,lb,ub))
					x_mirr = reflect(x_mirr, lb, ub);
				
		}
		return x_mirr;
	}
	protected static double mirror_recursive(double x, double lb, double ub) 
	{
		double x_mirr = Double.NaN;
		if(inDomain(x,lb,ub)) 
			x_mirr = x;
		else
		{
			x_mirr = reflect(x, lb, ub);
			x_mirr = mirror_recursive(x_mirr, lb, ub); 
		
				
		}
		return x_mirr;
	}
	
	//%%%
	/**
	 * Saturation on bounds of the search space.
	 * 
	 * @param x solution to be saturated.
	 * @param bounds search space boudaries.
	 * @return x_tor corrected solution.
	 */
	public static double[] saturate(double[] x, double[][] bounds)
	{
		int n = x.length;
		double[] x_sat = new double[n];
		for (int i = 0; i < n; i++)
			x_sat[i] = min(max(x[i], bounds[i][0]), bounds[i][1]);
		return x_sat;
	}
	/**
	 * Toroidal correction within search space
	 * 
	 * @param x solution to be corrected.
	 * @param bounds search space boundaries (general case).
	 * @return x_tor corrected solution.
	 */
	public static double[] toro(double[] x, double[][] bounds)
	{
		int n = x.length;
		double[] x_tor = new double[n];
		for (int i = 0; i < n; i++)
		{
			x_tor[i] = (x[i]-bounds[i][0])/(bounds[i][1]-bounds[i][0]);
			
			if (x_tor[i] > 1)
				x_tor[i] = x_tor[i]-fix(x_tor[i]);
			else if (x_tor[i] < 0)
				x_tor[i]=1-Math.abs(x_tor[i]-fix(x_tor[i]));
			
			x_tor[i] = x_tor[i]*(bounds[i][1]-bounds[i][0])+bounds[i][0];
		}

		return x_tor;
	}
	/**
	 * Toroidal correction within the search space
	 * 
	 * @param x solution to be corrected.
	 * @param bounds search space boundaries (hyper-parallelepiped).
	 * @return x_tor corrected solution.
	 */
	public static double[] toro(double[] x, double[] bounds)
	{
		int n = x.length;
		double[] x_tor = new double[n];
		for (int i = 0; i < n; i++)
		{
			x_tor[i] = (x[i]-bounds[0])/(bounds[1]-bounds[0]);
			
			if (x_tor[i] > 1)
				x_tor[i] = x_tor[i]-fix(x_tor[i]);
			else if (x_tor[i] < 0)
				x_tor[i]=1-Math.abs(x_tor[i]-fix(x_tor[i]));
			
			x_tor[i] = x_tor[i]*(bounds[1]-bounds[0])+bounds[0];
		}

		return x_tor;
	}
	/**
	 * Discard and re-sample within the search space.
	 * 
	 * @param x solution to be saturated.
	 * @param bounds search space boundaries.
	 * @return x_res corrected solution.
	 */
	public static double[] discardAndResample(double[] x, double[][] bounds)
	{
		int n = x.length;
		int i  = 0;
		while(i<n && x[i]>=bounds[0][i] && x[i]<=bounds[1][i])
			i++;
		if(i!=n)
			x = generateRandomSolution(bounds, n);
		return x;
	}
	
	
	static public double[] saturation(double[] x, double[][] bounds)
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
	
	
	
}

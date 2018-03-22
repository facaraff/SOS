package utils.algorithms.operators;

import static utils.algorithms.Misc.fix;
import utils.MatLab;
import utils.random.RandUtils;

public class GAOp
{
	/**
	 * Linear crossover
	 * param x
	 * param y
	 * return x + F(x-y), where  0 < F < 1.
	 */
	public static double[] crossOverLinear(double[] x, double[] y) {
		int n = x.length;
		double F = RandUtils.random(); //scale factor F
		double[] XX = new double[n];
		for (int i = 0; i < n; i++)
			XX[i]=x[i]+F*(y[i]-x[i]);
		return XX;
	}
	
	/**
	 * Biased Linear crossover
	 * @param x
	 * @param y
	 * @param CR
	 * @param dimension
	 * @return
	 */
	public static double[] crossOverBiasedLinear(double[] x, double[] y, double CR, int dimension) {
		int n = x.length;
		double F = 2*(Math.log(RandUtils.random())/Math.log(CR))/(dimension-1); //biased scale factor F
		double[] XX = new double[n];
		for (int i = 0; i < n; i++)
			XX[i]=x[i]+F*y[i];
		return XX;
	}
	
	/**
	 * SPX crossover
	 * 
	 * @param P
	 * @param epsilon
	 * @return
	 */
	public static double[] SPX(double[][] P, double epsilon)
	{
		int np = P.length;
		int individualSize = P[0].length;
		
		double[] Om = new double[individualSize];
		for (int i = 0; i < individualSize; i++)
			Om[i] = 0.0;

		for (int i = 0; i < individualSize; i++)
		{
			for (int j = 0; j < np; j++)
				Om[i] += P[j][i];
			Om[i] /= np;
		}
		
		double[] r = new double[np-1];
		int lengthR = r.length;
		for (int i = 0; i < lengthR; i++)
		{
			r[i] = Math.pow(RandUtils.random(), 1.0/(i+1));
		}	

		double[][] y = new double[np][individualSize];
		for (int i = 0; i < np; i++)
		{
			for (int j = 0; j < individualSize; j++)
				y[i][j] = Om[j] + epsilon*(P[i][j]-Om[j]);
		}
		
		double[][] C = new double[np][individualSize];
		for (int i = 0; i < np; i++)
		{
			for (int j = 0; j < individualSize; j++)
			{
				if (i == 0)
					C[i][j] = 0.0;
				else
					C[i][j] = r[i-1]*(y[i-1][j] - y[i][j] + C[i-1][j]);					
			}
		}
		
		double[] Coff = new double[individualSize];
		for (int i = 0; i < individualSize; i++)
			Coff[i] = y[np-1][i] + C[np-1][i];
		
		return Coff;
	}
	
	/**
	 * BLX-alpha Cross Over
	 * param x
	 * param y
	 * param alpha
	 */
	public static double[] BLX(double[] x, double[] y, double alpha) {
		int n = x.length;
		double[] z = new double[n];
		double max, min, I;
		for (int i = 0; i < n; i++) 
		{
			if(x[i]>=y[i])
			{
				max = x[i];
				min = y[i];
			}
			else
			{
				max = y[i];
				min = x[i];
			}
			I = max - min;
			z[i] = (min  - I*alpha) + (I + 2*I*alpha)*RandUtils.random();
		}
		return z;
	}
	
	
	/**
	 * BGA Mutation Scheme
	 * 
	 * @param x
	 * @param a
	 * @param b
	 * 
	 */
	public static double[] BGA(double[] x, double[][] bounds)
	{	
		double[] y = new double[x.length];
		double DELTA;
		int alpha, sign;
		double[] rang = new double[x.length];
		for(int n=0; n < x.length; n++)
			rang[n] = 0.1*(bounds[n][1]-bounds[n][0]);
		
		for(int n=0; n < x.length; n++)
		{
			DELTA = 0;
			for(int k=0; k <= 15; k++)
			{
				if(RandUtils.random() <= 0.0625)
					alpha = 0;
				else
					alpha = 1;
				DELTA+=alpha*Math.pow(2,-k);	
			}
			if(RandUtils.random() > 0.5)
				sign = 1;
			else
				sign =1;
			
			y[n] = x[n] +sign*rang[n]*DELTA;
		}
		return y;
	}
	
	/**
	 * Roulette wheel selection
	 * 
	 * @param values
	 * @return
	 */
	public static int roulette(double[] values) 
	{
		double s = MatLab.sum(values);
		double r = RandUtils.random() * s;
		double thresh = 0.0;
		for (int i = 0; i < values.length-1; i++) {
			thresh += values[i];
			if (r <= thresh) 
				return i;
		}
		return values.length-1;
	}
}
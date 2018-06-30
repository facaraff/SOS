package utils.algorithms;

import java.io.FileWriter;
import java.io.IOException;

import static utils.MatLab.transpose;
import static utils.MatLab.sum;
import static utils.algorithms.Misc.fix;
import utils.random.RandUtils;

public class CompactAlgorithms
{
	private static double[] acoef_invErf = { 0.886226899, -1.645349621, 0.914624893, -0.140543331 };
	private static double[] bcoef_invErf = { -2.118377725, 1.442710462, -0.329097515, 0.012229801 };
	private static double[] ccoef_invErf = { -1.970840454, -1.624906493,  3.429567803, 1.641345311 };
	private static double[] dcoef_invErf = { 3.543889200, 1.637067800 };

	private static double[] acoef = { 3.16112374387056560e00, 1.13864154151050156e02, 
										3.77485237685302021e02, 3.20937758913846947e03, 
										1.85777706184603153e-1 };
	private static double[] bcoef = { 2.36012909523441209e01, 2.44024637934444173e02,
										1.28261652607737228e03, 2.84423683343917062e03 };
	private static double[] ccoef = { 5.64188496988670089e-1, 8.88314979438837594e00,
										6.61191906371416295e01, 2.98635138197400131e02,
										8.81952221241769090e02, 1.71204761263407058e03,
										2.05107837782607147e03, 1.23033935479799725e03,
										2.15311535474403846e-8 };
	private static double[] dcoef = { 1.57449261107098347e01, 1.17693950891312499e02,
										5.37181101862009858e02, 1.62138957456669019e03,
										3.29079923573345963e03, 4.36261909014324716e03,
										3.43936767414372164e03, 1.23033935480374942e03 };
	
	private static double[] p = { 3.05326634961232344e-1, 3.60344899949804439e-1,
									1.25781726111229246e-1, 1.60837851487422766e-2,
									6.58749161529837803e-4, 1.63153871373020978e-2 };
	private static double[] q = { 2.56852019228982242e00, 1.87295284992346047e00,
									5.27905102951428412e-1, 6.05183413124413191e-2,
									2.33520497626869185e-3 };


	/**
	 * Scale a normalized solution (defined in [-1,1]) over bounds.
	 * 
	 * @param xNormalized
	 * @param bounds
	 * @param xc
	 * @param xScaled
	 * @return
	 */
	public static double[] scale(double[] xNormalized, double[][] bounds, double[] xc)
	{
		int n = xNormalized.length;
		double[] delta = new double[n];
		for (int i = 0; i < n; i++)
			delta[i] = bounds[i][1] - xc[i];
		double[] x = new double[n];
		for (int i = 0; i < n; i++)
			x[i] = xNormalized[i]*delta[i]+xc[i];
		return x;
	}
	
	/**
	 * Normalized a solution over [-1,1].
	 * 
	 * @param x
	 * @param bounds
	 * @param xc
	 * @param xNormalized
	 * @return
	 */
	public static double[] normalize(double[] x, double[] bounds, double xc)
	{
		double delta = bounds[1] - xc;
		int n = x.length;
		double[] xNormalized = new double[n];
		for (int i = 0; i < n; i++)
			xNormalized[i] = (x[i]-xc)/delta;
		return xNormalized;
	}
	
	/**
	 * 
	 * @param mean
	 * @param x
	 * @return
	 */
	public static byte[] generateIndividual(double[] PV)
	{
		int n = PV.length;
		byte[] x = new byte[n];
		for (int i = 0; i < n; i++)
		{
			if (RandUtils.random() >= PV[i])
				x[i] = 1;
			else
				x[i] = 0;
		}

		return x;
	}
	
	/**
	 * 
	 * @param mean
	 * @param sigma2
	 * @param x
	 * @return
	 */
	public static double[] generateIndividual(double[] mean, double[] sigma2)
	{
		int n = mean.length;
		double[] x = new double[n];
		for (int i = 0; i < n; i++)
			x[i] = 2;

		double trial = 0;
		for (int i = 0; i < n; i++)
		{
			trial = 0;
			while ((Math.abs(x[i])>1) && trial < 10)
			{
				trial++;
				x[i] = RandUtils.gaussian(mean[i], Math.sqrt(sigma2[i]));
			}
			
			if (Math.abs(x[i])>1)
				x[i] = truncateRandn(RandUtils.random(), mean[i], Math.sqrt(sigma2[i]));
		}
		
		return x;
	}
	
	/**
	 * 
	 * @param r
	 * @param m
	 * @param sigma
	 * @return
	 */
	public static double truncateRandn(double r, double m, double sigma)
	{
		double nmin = -1;
		double nmax = 1;

		double s1 = Math.sqrt(2)*sigma;
		double pdfmax = (nmax-m)/s1;
		double pdfmin = (nmin-m)/s1;

		double umin = erf(pdfmin);
		double umax = erf(pdfmax);

		double u0 = r;
		double u = umin +(umax-umin)*u0;

		double me = inverseErf(u);
		return s1*me+m;
	}

	/**
	 * Error function.
	 * 
	 * @param x
	 * @return
	 */
	private static double erf(double x)
	{
		double result = 0;
		double xbreak = 0.46875;
		double absX = Math.abs(x);

		// evaluate  erf  for  |x| <= 0.46875
		if (absX <= xbreak)
		{
			double y = absX;
			double z = y*y;
			double xnum = acoef[4]*z;
			double xden = z;
			for (int i = 0; i < 3; i++)
			{
				xnum = (xnum + acoef[i])*z;
				xden = (xden + bcoef[i])*z;
			}
			result = x * (xnum + acoef[3])/(xden + bcoef[3]);
		}
		
		// evaluate  erf for 0.46875 <= |x| <= 4.0
		if ((absX > xbreak) && (absX <= 4))
		{
			double y = absX;
			double xnum = ccoef[8]*y;
			double xden = y;
			for (int i = 0; i < 7; i++)
			{
				xnum = (xnum + ccoef[i])*y;
				xden = (xden + dcoef[i])*y;
			}
			result = (xnum + ccoef[7])/(xden + dcoef[7]);
			double z = fix(y*16)/16;
			double del = (y-z)*(y+z);
			result = Math.exp(-z*z) * Math.exp(-del) * result;
		}

		// evaluate  erfc  for |x| > 4.0

		if (absX > 4.0)
		{
			double y = absX;
			double z = 1/(y*y);
			double xnum = p[5]*z;
			double xden = z;
			for (int i = 0; i < 4; i++)
			{
				xnum = (xnum + p[i])*z;
				xden = (xden + q[i])*z;
			}
			result = z * (xnum + p[4])/(xden + q[4]);
			result = (1/Math.sqrt(Math.PI) - result) / y;
			z = fix(y*16)/16;
			double del = (y-z)*(y+z);
			result = Math.exp(-z*z) * Math.exp(-del) * result;
		}

		if (x > xbreak)
			result = (0.5 - result) + 0.5;

		if (x < -xbreak)
			result = (-0.5 + result) - 0.5;
		
		return result;
	}
	
	/**
	 * Inverse error function.
	 */
	private static double inverseErf(double yo)
	{
		double xo = 0;
		double y0 = 0.7;
		double z;

		if (Math.abs(yo) <= y0)
		{
			z = yo*yo;
			xo = yo*(((acoef_invErf[3]*z+acoef_invErf[2])*z+acoef_invErf[1])*z+acoef_invErf[0])/
				((((bcoef_invErf[3]*z+bcoef_invErf[2])*z+bcoef_invErf[1])*z+bcoef_invErf[0])*z+1);
		}
		else if ((y0 < yo) && (yo < 1))
		{
			z = Math.sqrt(-Math.log((1-yo)/2));
			xo = (((ccoef_invErf[3]*z+ccoef_invErf[2])*z+ccoef_invErf[1])*z+ccoef_invErf[0])/
				((dcoef_invErf[1]*z+dcoef_invErf[0])*z+1);
		}
		else if ((-y0 > yo ) && (yo > -1))
		{
			z = Math.sqrt(-Math.log((1+yo)/2));
			xo = -(((ccoef_invErf[3]*z+ccoef_invErf[2])*z+ccoef_invErf[1])*z+ccoef_invErf[0])/
				((dcoef_invErf[1]*z+dcoef_invErf[0])*z+1);
		}
		
		return xo;
	}
	
	/**
	 * Update means vector.
	 * 
	 * @param x
	 * @param y
	 * @param mean
	 * @param virtualPopSize
	 * @return
	 */
	public static double[] updateMean(double[] x, double[] y, double[] mean, int virtualPopSize)
	{
		int n = x.length;
		double[] newMean = new double[n];
		for (int k = 0; k < n; k++)
		{
			double dm = (1.0/virtualPopSize)*(x[k]-y[k]);
			double tmp = mean[k]+dm;
			if (Math.abs(tmp) <= 1)
				newMean[k] = tmp;
			else
				newMean[k] = mean[k];
		}
		
		return newMean;
	}

	/**
	 * Update variance vector.
	 * 
	 * @param x
	 * @param y
	 * @param mean
	 * @param sigma2
	 * @param virtualPopSize
	 * @return
	 */
	public static double[] updateSigma2(double[] x, double[] y, double[] mean, double[] sigma2, int virtualPopSize)
	{
		int n = x.length;
		double[] newSigma2 = new double[n];
		for (int k = 0; k < n; k++)
		{
			double dm = (1.0/virtualPopSize)*(x[k]-y[k]);
			double tmp = mean[k]+dm;
			double A = mean[k]*mean[k];
			double B = tmp*tmp;
			double C = (1.0/virtualPopSize)*(x[k]*x[k]-y[k]*y[k]);
			double dSIGMA2 = A-B+C;

			if (Math.abs(dSIGMA2) < sigma2[k])
				newSigma2[k] = sigma2[k]+dSIGMA2;
			else
				newSigma2[k] = sigma2[k]; 
		}
		
		return newSigma2;
	}
	
	/**
	 * Converts an array of bits xbin to an array of double (each one encoded with nbit bits).
	 * 
	 * @param xbin
	 * @param nbit
	 * @param bounds
	 * @return
	 */
	public static double[] bin2dec(byte[] xbin, int nbit, double[][] bounds)
	{
		int nv = xbin.length/nbit;
		double[] delta = new double[nv];
		for (int n = 0; n < nv; n++)
			delta[n] = bounds[n][1]-bounds[n][0];
		
		// nbit x nv
		double[][] xmat = new double[nbit][nv];
		int k = 0;
		for (int i = 0; i < nbit; i++)
		{
			for (int j = 0; j < nv; j++)
				xmat[i][j] = xbin[k++];
		}

		int[] l = new int[nbit];
		double[] base = new double[nbit];
		for (int i = 0; i < nbit; i++)
		{
			if (i == 0) 
				l[i] = nbit-1;
			else
				l[i] = l[i-1]-1;
			
			base[i] = Math.pow(2,l[i]);
		}

		// nv x nbit
		xmat = transpose(xmat);
		
		double[] xs = new double[nv];
		for (int i = 0; i < nv; i++)
			xs[i] = 0.0; 
			
		for (int i = 0; i < nv; i++)
		{
			for (int j = 0; j < nbit; j++)
				xs[i] += xmat[i][j]*base[j];
		}
		
		double sumBase = sum(base);
		for (int i = 0; i < nv; i++)
			xs[i] = ((xs[i]/sumBase)*delta[i])+bounds[i][0];
		
		return xs;
	}

	/**
	 * A simple main test class.
	 */
	public static void main(String[] args)
	{
		double[] x = new double[100];
		double[] y = new double[100];
		double[] z = new double[100];

		double increment = 0.1;
		
		for (int i = 0; i < x.length; i++)
		{
			if (i == 0)
				x[i] = -5.0;
			else
				x[i] = x[i-1] + increment;
			
			y[i] = erf(x[i]);
			z[i] = inverseErf(x[i]);
		}
		
		try
		{
			FileWriter fileWriter = new FileWriter("test_erf.txt");
			for (int i = 0; i < x.length; i++)
				fileWriter.write(Double.toString(x[i]) + "\t" + Double.toString(y[i]) + "\t" + Double.toString(z[i]) + "\n");
			fileWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
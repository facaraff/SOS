package applications.cargate;

import utils.MatLab;

public class CarGateUtils {

	/**
	 *  Multiply a series of binomials and returns the coefficients of the 
	 *  resulting polynomial. The multiplication has the following form:<b/>
	 *  
	 *  (x+p[0])*(x+p[1])*...*(x+p[n-1]) <b/>
	 *  
	 *  The p[i] coefficients are assumed to be complex and are passed to the
	 *  function as an array of doubles of length 2n.<b/>
	 *  
	 *  The resulting polynomial has the following form:<b/>
	 *  
	 *  x^n + a[0]*x^n-1 + a[1]*x^n-2 + ... +a[n-2]*x + a[n-1] <b/>
	 *  
	 *  The a[i] coefficients can in general be complex but should in most cases
	 *  turn out to be real. The a[i] coefficients are returned by the function 
	 *  as an array of doubles of length 2n.
	 *  
	 * @param p array of doubles where p[2i], p[2i+1] (i=0...n-1) is assumed to be the real, imaginary part of the i-th binomial.
	 * @return coefficients a: x^n + a[0]*x^n-1 + a[1]*x^n-2 + ... +a[n-2]*x + a[n-1]
	 */
	public static double [] binomialMult(double [] p) {
		int n = p.length / 2;
		double [] a = new double [2 * n];

		for (int i = 0; i < n; ++i) {
			for (int j = i; j > 0; --j) {
				a[2 * j] += p[2 * i] * a[2 * (j - 1)] - p[2 * i + 1]
						* a[2 * (j - 1) + 1];
				a[2 * j + 1] += p[2 * i] * a[2 * (j - 1) + 1] + p[2 * i + 1]
						* a[2 * (j - 1)];
			}

			a[0] += p[2 * i];
			a[1] += p[2 * i + 1];
		}

		return a;
	}

	/**
	 * Compute the B coefficients for low/high pass. The cutoff frequency is not
	 * required.
	 * 
	 * @param n
	 * @param lowp
	 * @return
	 */
	public static double [] computeB(int n, boolean lowp) {
		double [] ccof = new double [n + 1];

		ccof[0] = 1;
		ccof[1] = n;

		for (int i = 2; i < n/2 + 1; ++i) {
			ccof[i] = (n - i + 1) * ccof[i - 1] / i;
			ccof[n - i] = ccof[i];
		}

		ccof[n - 1] = n;
		ccof[n] = 1;

		if (!lowp) {
			for (int i = 1; i < n + 1; i += 2)
				ccof[i] = -ccof[i];
		}

		return ccof;
	}

	/**
	 * Compute the A coefficients for a low/high pass for the given frequency
	 * @param n
	 * @param f frequency in radians (2 * hz / samplef)
	 * @return
	 */
	public static double [] computeA(int n, double f) {
		double parg;    // pole angle
		double sparg;   // sine of the pole angle
		double cparg;   // cosine of the pole angle
		double a;       // workspace variable
		double [] rcof = new double [2 * n]; // binomial coefficients

		double theta = Math.PI * f;
		double st = Math.sin(theta);
		double ct = Math.cos(theta);

		for (int k = 0; k < n; ++k) {
			parg = Math.PI * (double) (2*k + 1) / (double) (2*n);
			sparg = Math.sin(parg);
			cparg = Math.cos(parg);
			a = 1. + st * sparg;
			rcof[2 * k] = -ct / a;
			rcof[2 * k + 1] = -st * cparg / a;
		}

		// compute the binomial
		double [] temp = binomialMult(rcof);

		// we only need the n+1 coefficients
		double [] dcof = new double [n + 1];
		dcof[0] = 1.0;
		dcof[1] = temp[0];
		dcof[2] = temp[2];
		for (int k = 3; k < n + 1; ++k)
			dcof[k] = temp[2*k - 2];

		return dcof;
	}

	/**
	 * Compute the scale factor for the b coefficients for given low/high pass
	 * filter.
	 * 
	 * @param n
	 * @param f
	 * @param lowp
	 * @return
	 */
	public static double computeScale(int n, double f, boolean lowp) {
		double omega = Math.PI * f;
		double fomega = Math.sin(omega);
		double parg0 = Math.PI / (double)(2*n);

		double sf = 1.;
		for (int k = 0; k < n/2; ++k )
			sf *= 1.0 + fomega * Math.sin((double)(2*k+1)*parg0);

		fomega = Math.sin(omega / 2.0);

		if (n % 2 == 1) 
			sf *= fomega + (lowp ? Math.cos(omega / 2.0) : Math.sin(omega / 2.));
		sf = Math.pow( fomega, n ) / sf;

		return sf;
	}

	// this method works only for digital low-pass filters
	public static double[] buttord(double Wp, double Ws, double Rp, double Rs)
	{
		double[] retValue = new double[2];

		int n;
		double Wn;

		// warp the target frequencies according to the bilinear transform
		Wp = Math.tan(Math.PI*Wp/2);
		Ws = Math.tan(Math.PI*Ws/2);

		double Wa = Ws/Wp;

		// compute minimum n which satisfies all band edge conditions
		n = (int) Math.ceil(Math.log10((Math.pow(10, 0.1*Rs)-1)/(Math.pow(10, 0.1*Rp)-1))/(2*Math.log10(Wa)));

		double W0 = Wa / (Math.pow((Math.pow(10,0.1*Rs) - 1),1/(2*n)));
		Wn = W0*Wp;

		// unwarp the returned frequency
		Wn = (2/Math.PI)*Math.atan(Wn);

		retValue[0] = n;
		retValue[1] = Wn;

		return retValue;
	}

	public static double[] bwFilter(double[] x, double[] t, double cf)
	{
		int n = x.length;
		double[] y = new double[n];
		double[] u = new double[n];

		for (int i = 0; i < n; i++)
		{
			y[i] = x[i];
			u[i] = x[i];
		}

		double simTime = t[t.length-1] - t[0];
		double sampRate = ((double)n)/simTime;

		double normPass = 2*Math.PI*cf/sampRate;
		//double normPass = 2*cf/sampRate;
		double normStop = 1.5*normPass;

		double buttord[] = buttord(normPass, normStop, 2.0, 30.0);

		int N = (int)buttord[0];
		double Wn = buttord[1];

		double scale = computeScale(N, Wn, true);

		double [] b = computeB(N, true);
		for (int i = 0; i < b.length; ++i)
			b[i] *= scale;

		double [] a = computeA(N, Wn);

		//MathUtils.print(buttord);
		//MathUtils.print(b);
		//MathUtils.print(a);

		int bLength = b.length;
		int aLength = a.length;
		double tmp;
		for (int i = 0; i < n; i++)
		{
			tmp = b[0]*u[i];
			for (int j = 1; j < bLength; j++)
			{
				if (i >= j)
					tmp += b[j]*u[i-j];
			}

			for (int j = 1; j < aLength; j++)
			{
				if (i >= j)
					tmp -= a[j]*y[i-j];
			}
			y[i] = tmp;
		}

		return y;
	}

	/**
	 * Utility function to generate a string representing 
	 * a variable-size solution (being dipoles ordered along x)
	 * @param p
	 * @param f
	 * @return
	 */
	public static String toStringSolution(double[] p, double f)
	{
		int n = (int)Math.floor(p[0]);
		String retValue =  n + "\n";
		double[][] dipoles = new double[n][4];
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < 4; j++)
				dipoles[i][j] = p[1+4*i+j];
		}
		MatLab.sortRows(dipoles, 1);
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < 4; j++)
				p[1+4*i+j] = dipoles[i][j];
		}

		for (int i = 0; i < n; i++)
		{
			for (int j = 1+4*i; j < 1+4*i+4; j++)
				retValue += p[j] + "\t";
			retValue += "\n";
		}
		retValue += f + "\n";
		return retValue;
	}
}

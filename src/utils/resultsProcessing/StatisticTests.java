/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FreeBSD Project.
*/
package utils.resultsProcessing;

import java.util.Arrays;

import jsc.independentsamples.TwoSampleTtest;
import jsc.tests.H1;
import static utils.MatLab.variance;
import utils.random.RandUtils;

public class StatisticTests {

	public static void main (String args[]) throws Exception
	{
		double[] x = new double[100];
		for (int i = 0; i < x.length; i++)
			x[i] = Math.random();

		double[] y = new double[100];
		for (int i = 0; i < y.length; i++)
			y[i] = Math.sin(2*Math.PI*i);

		double[] z = new double[100];
		for (int i = 0; i < z.length; i++)
			z[i] = RandUtils.gaussian(1, 1);

		double[] w = new double[100];
		for (int i = 0; i < w.length; i++)
			w[i] = RandUtils.gaussian(-1, 1);
		
		TwoSampleTtest ttest;
		
		ttest = new TwoSampleTtest(z, w, H1.NOT_EQUAL, true);
		System.out.println("t-test p-value = " + ttest.getSP());

		ttest = new TwoSampleTtest(z, w, H1.LESS_THAN, true);
		System.out.println("t-test p-value = " + ttest.getSP());

		ttest = new TwoSampleTtest(z, w, H1.GREATER_THAN, true);
		System.out.println("t-test p-value = " + ttest.getSP());
		
		/*
		double f = FTest(z, w);
		System.out.println("f-test value = " + f);

		Arrays.sort(x);
		printInfo(x);

		Arrays.sort(y);
		printInfo(y);

		Arrays.sort(z);
		printInfo(z);
		
		Arrays.sort(w);
		printInfo(w);
		*/
	}

	public static double[] printInfo (double[] x ){
		double[] r = ShapiroWilk(x);
		System.out.println("w = " + r[0]);
		System.out.println("pw = " + r[1]);
		return r;
	}

	// constant definitions for ShapiroWilk
	private static double  C1[]  = { 0.0E0, 0.221157E0, -0.147981E0, -0.207119E1, 0.4434685E1, -0.2706056E1 };
	private static double  C2[]  = { 0.0E0, 0.42981E-1, -0.293762E0, -0.1752461E1, 0.5682633E1, -0.3582633E1 };
	private static double  C3[]  = { 0.5440E0, -0.39978E0, 0.25054E-1, -0.6714E-3 };
	private static double  C4[]  = { 0.13822E1, -0.77857E0, 0.62767E-1, -0.20322E-2 };
	private static double  C5[]  = { -0.15861E1, -0.31082E0, -0.83751E-1, 0.38915E-2 };
	private static double  C6[]  = { -0.4803E0, -0.82676E-1, 0.30302E-2 };
	private static double  G[]   = { -0.2273E1, 0.459E0 };
	private static double  SQRTH = 0.70711E0, TH = 0.375E0, SMALL = 1E-19;
	private static double  PI6   = 0.1909859E1, STQR = 0.1047198E1;
	private static boolean UPPER = true;

	// see the implementation in http://www.math.kent.edu/~blewis/source/specialFunctions.java
	public static double FTest(double[] x, double[] y)
	{
		double varX = variance(x);
		double varY = variance(y);

		double f = 0;
		double fAlpha = 0;

		if (varX > varY) {
			f = varX/varY;
			fAlpha = fDist(x.length-1, y.length-1, f);
		}
		else {
			f = varY/varX;
			fAlpha = fDist(y.length-1, x.length-1, f);
		}
		
		return fAlpha;
	}
	
	private static double gamma(double x) {
		// An approximation of gamma(x)
		double f = 10E99;
		double g = 1;
		if (x > 0 ) {
			while (x < 3) {
				g = g * x;
				x = x + 1;
			}
			f = (1 - (2/(7*Math.pow(x,2))) * (1 - 2/(3*Math.pow(x,2))))/(30*Math.pow(x,2));
			f = (1-f)/(12*x) + x*(Math.log(x)-1);
			f = (Math.exp(f)/g)*Math.pow(2*Math.PI/x,0.5);
		}
		else {
			f = Double.POSITIVE_INFINITY;
		}
		return f;
	}
	
	private static double betacf(double a,double b,double x){
		// A continued fraction representation of the beta function
		int maxIterations = 50, m=1;
		double eps = 3E-5;
		double am = 1;
		double bm = 1;
		double az = 1;
		double qab = a+b;
		double qap = a+1;
		double qam = a-1;
		double bz = 1 - qab*x/qap;
		double aold = 0;
		double em, tem, d, ap, bp, app, bpp;
		while ((m<maxIterations)&&(Math.abs(az-aold)>=eps*Math.abs(az)))
		{
			em = m;
			tem = em+em;
			d = em*(b-m)*x/((qam + tem)*(a+tem));
			ap = az+d*am;
			bp = bz+d*bm;
			d = -(a+em)*(qab+em)*x/((a+tem)*(qap+tem));
			app = ap+d*az;
			bpp = bp+d*bz;
			aold = az;
			am = ap/bpp;
			bm = bp/bpp;
			az = app/bpp;
			bz = 1;
			m++;
		}
		return az;
	}
	
	private static double betai(double a, double b, double x) {
		// the incomplete beta function from 0 to x with parameters a, b
		// x must be in (0,1) (else returns error)
		double bt=0, beta=Double.POSITIVE_INFINITY;
		if (x==0 || x==1 ){
			bt = 0; }
		else if ((x>0)&&(x<1)) {
			bt = gamma(a+b)*Math.pow(x,a)*Math.pow(1-x,b)/(gamma(a)*gamma(b)); }
		if (x<(a+1)/(a+b+2)){
			beta = bt*betacf(a,b,x)/a; }
		else {
			beta = 1-bt*betacf(b,a,1-x)/b; }
		return beta;
	}

	private static double fDist(double v1, double v2, double f) {
		/* 	F distribution with v1, v2 deg. freedom
			P(x>f)
		 */
		double p =	betai(v1/2, v2/2, v1/(v1 + v2*f));
		return p;
	}

	/**
	 * 
	 * @param x
	 * @param sort
	 * @return
	 */
	public static double ShapiroWilk(double[] x, boolean sort)
	{
		if (sort)
		{
			double[] xSorted = Arrays.copyOf(x, x.length);
			Arrays.sort(xSorted);
			double[] ret = ShapiroWilk(xSorted);
			return ret[1];
		}
		else
		{
			double[] ret = ShapiroWilk(x);
			return ret[1];
		}
	}
	
	// see the implementation in http://pastebin.com/m3RYsyG9
	private static double[] ShapiroWilk(double[] x) {

		int n = x.length;
		int n2 = n/2;
		double w = 1d;	// Shapiro-Wilks W-stat
		double[] a = new double[n2];	// Calculated weights

		if (n < 3)
			//Kill(1);
			return new double[] {0, 0, 0};

		if (n == 3) a[0] = SQRTH;
		else {
			double an25 = n + 0.25;
			double summ2 = 0d;
			for (int i = 0; i < n2; i++) {
				a[i] = ppnd((i + 1 - TH) / an25);
				summ2 += a[i] * a[i];
			}
			summ2 *= 2d;
			double ssumm2 = Math.sqrt(summ2);
			double rsn = 1.0 / Math.sqrt(n);
			double a1 = poly(C1, rsn) - a[0] / ssumm2;

			// Normalize coefficients
			int i1;
			double fac;
			if (n > 5) {
				i1 = 2;
				double a2 = -a[1] / ssumm2 + poly(C2, rsn);
				fac = Math.sqrt((summ2 - 2.0 * a[0] * a[0] - 2.0 * a2 * a2)
						/ (1.0 - 2.0 * a1 * a1 - 2.0 * a2 * a2));
				a[0] = a1;
				a[1] = a2;
			} else {
				i1 = 1;
				fac = Math.sqrt((summ2 - 2.0 * a[0] * a[0]) / (1.0 - 2.0 * a1 * a1));
				a[0] = a1;
			}

			for (int i = i1; i < n2; i++) {
				a[i] = -a[i] / fac;
			}
		}

		double w1, xx;

		// check for zero range
		double range = x[n-1] - x[0];
		if (range < SMALL)
			//Kill(6);
			return new double[] {0, 0, 0};

		// Check for correct sort order on range - scaled X
		xx = x[0] / range;
		double sx = xx;
		double sa = -a[0];
		int j = n - 2;
		for (int i = 1; i < n; i++) {
			double xi = x[i] / range;
			if (xx - xi > SMALL)
				//Kill(7);
				return new double[] {0, 0, 0};
			sx += xi;
			if (i != j) {
				sa += Math.signum(i - j) * a[Math.min(i, j)];
			}
			xx = xi;
			j--;
		}

		// Calculate W statistic as squared correlation
		// between data and coefficients

		sa /= n;
		sx /= n;
		double ssa = 0D;
		double ssx = 0D;
		double sax = 0D;
		double asa;
		j = n-1;
		for (int i = 0; i < n; i++) {
			if      (i != j) asa = Math.signum(i - j) * a[Math.min(i, j)] - sa;
			else     asa = -sa;

			double xsx = x[i] / range - sx;
			ssa += asa * asa;
			ssx += xsx * xsx;
			sax += asa * xsx;
			j--;
		}

		// W1 equals (1-W) claculated to avoid excessive rounding error
		// for W very near 1 (a potential problem in very large samples)
		double ssassx = Math.sqrt(ssa * ssx);
		w1 = (ssassx - sax) * (ssassx + sax) / (ssa * ssx);
		w = 1.0 - w1;

		// Calculate significance level for W (exact for N=3)
		if (n == 3) return new double[] {w, PI6 * (Math.asin(Math.sqrt(w)) - STQR)};

		double y = Math.log(w1);
		xx = Math.log(n);
		double m = 0d;
		double s = 1d;
		if (n <= 11) {
			double gamma = poly(G, n);
			if (y >= gamma) return new double[] {w, SMALL};
			y = -Math.log(gamma - y);
			m = poly(C3, n);
			s = Math.exp(poly(C4, n));
		} else {
			m = poly(C5, xx);
			s = Math.exp(poly(C6, xx));
		}

		return new double[] {w, alnorm((y - m) / s, UPPER) };
	}

	// constant definitions for PPND
	private static double SPLIT1 = 0.425, SPLIT2 = 5.0;
	private static double CONST1 = 0.180625, CONST2 = 1.6;
	private static double A0 = 3.3871327179E+00, A1 = 5.0434271938E+01, A2 = 1.5929113202E+02, A3 = 5.9109374720E+01;
	private static double B1 = 1.7895169469E+01, B2 = 7.8757757664E+01, B3 = 6.7187563600E+01;
	private static double CC0 = 1.4234372777E+00, CC1 = 2.7568153900E+00, CC2 = 1.3067284816E+00, CC3 = 1.7023821103E-01;
	private static double D1 = 7.3700164250E-01, D2 = 1.2021132975E-01;
	private static double E0 = 6.6579051150E+00, E1 = 3.0812263860E+00, E2 = 4.2868294337E-01, E3 = 1.7337203997E-02;
	private static double F1 = 2.4197894225E-01, F2 = 1.2258202635E-02;

	private static double ppnd(double p) {
		double  q = p - 0.5;
		if (Math.abs(q) <= SPLIT1) {
			double r = CONST1 - q * q;
			return q * (((A3 * r + A2) * r + A1) * r + A0)
					/ (((B3 * r + B2) * r + B1) * r + 1d);
		}

		double r = (q < 0d) ? p : 1d - p;
		if (r <= 0d) return 0d;
		r = Math.sqrt(-Math.log(r));

		double normal_dev;
		if (r <= SPLIT2) {
			r -= CONST2;
			normal_dev = (((CC3 * r + CC2) * r + CC1) * r + CC0) / ((D2 * r + D1) * r + 1d);
		} else {
			r -= SPLIT2;
			normal_dev = (((E3 * r + E2) * r + E1) * r + E0) / ((F2 * r + F1) * r + 1d);
		}

		if (q < 0.0) normal_dev = -normal_dev;

		return normal_dev;
	}

	private static double poly(double[] c, double x) {
		double p = 0;
		for (int j = c.length-1; j > 0; j--)
			p = (p + c[j]) * x;
		return c[0]+p;
	}

	// constant definitions for ALNORM
	private static double CON_a = 1.28, LTONE_a = 7.0, UTZERO_a = 18.66;
	private static double P_a = 0.398942280444, Q_a = 0.39990348504, R_a = 0.398942280385;
	private static double A1_a = 5.75885480458, A2_a = 2.62433121679, A3_a = 5.92885724438;
	private static double B1_a = -29.8213557807, B2_a = 48.6959930692;
	private static double C1_a = -3.8052E-8, C2_a = 3.98064794E-4, C3_a = -0.151679116635, C4_a = 4.8385912808, C5_a = 0.742380924027, C6_a = 3.99019417011;
	private static double D1_a = 1.00000615302, D2_a = 1.98615381364, D3_a = 5.29330324926, D4_a = -15.1508972451, D5_a = 30.789933034;

	private static double alnorm(double x, boolean upper) {
		boolean up = upper;
		double z = x;
		if (z < 0.0) {
			up = !up;
			z = -z;
		}

		double fn_val;
		if (z > LTONE_a && (!up || z > UTZERO_a)) fn_val = 0.0;
		else {
			double y = 0.5 * z * z;
			if (z <= CON_a)     fn_val = 0.5 - z * (P_a - Q_a * y / (y + A1_a + B1_a / (y + A2_a + B2_a / (y + A3_a))));
			else                fn_val = R_a * Math.exp(-y)
					/ (z + C1_a + D1_a / (z + C2_a + D2_a / (z + C3_a + D3_a
							/ (z + C4_a + D4_a / (z + C5_a + D5_a / (z + C6_a))))));
		}

		if (!up) fn_val = 1.0 - fn_val;

		return fn_val;
	}
}

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
package algorithms.memes;

import static utils.algorithms.Misc.generateRandomSolution;

import static utils.algorithms.Corrections.fConstraint;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/**
 * This class implements linMin's minimization method which is quadratically convergent.
 * After Press WH, Teukolsky SA, Vetterling WT, Flannery BP: Numerical
 * recipes in C++. Cambridge University Press, 2002, Chap. 10
 */
public class LineMinimization extends Algorithm
{
//	private final static double TINY = 1.0e-25;
//	private final static double MIN_VECTOR_LENGTH = 1.0e-3;
	private final static double PENALTY = 1e20;
//	private final static boolean unitDirectionVectors = true;

	private double[] p;
	private double[] xi = null;
//	private double ftol, fret;
//	private int n, iter;

	private int iter =0;
	private int n;
	private int maxEvaluations; //in linMin sono 100
	private Problem problem;
	private double[][] bounds;

//	// temp variables
	private double[] p1dim;
	private double[] xi1dim;

	// current best
	double[] best;
	// current best fitness
	double fBest;

	FTrend FT = new FTrend();
	
	public void setXI(double[] xi) {this.xi=xi;}
	
	
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		
//		System.out.println(p1dim==null);
//		System.out.println(xi1dim==null);
		
		if(xi==null) 
		{
			
			System.out.println("A direction along wich the optimisation is performed must be provided!");
			FT = null;
		}
		else 
		{	
			this.maxEvaluations = maxEvaluations;
			this.problem = problem;
			this.bounds = problem.getBounds();

			n = problem.getDimension();

//			p1dim = new double[n];
//			xi1dim = new double[n];
//			


//			double del, fp, fptt, t;
//			
			// initial point
			p = new double[n];
			best = new double[n];
			if (initialSolution != null)
			{
				p = initialSolution;
				fBest = initialFitness;
				for(int k=0;k<n;k++)
					best[k] = p[k];
			}
			else
			{
				p = generateRandomSolution(bounds, n);
				fBest= fConstraint(p, bounds, PENALTY,problem);
				iter++;
				for(int k=0;k<n;k++)
					best[k] = p[k];
			
			}
			
			fBest = lineMinimization(p, xi);
			finalBest = best;
			FT.add(iter, fBest);
		}
		
		return FT;
	}

	/**
	 * Optimization along the specified direction xit, starting from point p.
	 * 
	 * @param p
	 * @param xit
	 * @return
	 * @throws Exception
	 */
	private double lineMinimization(double[] p, double[] xit) throws Exception
	{
		p1dim = new double[xit.length];
		xi1dim = new double[xit.length];
		
		for(int j=0; j<n; j++)
		{
			p1dim[j] = p[j];
			xi1dim[j] = xit[j];
		}

		// bracket minimum
		BracketMin bm = new BracketMin(this, 0.0, 1.0);
		// optimize along direction
		Brent br = new Brent(this, bm.ax, bm.bx, bm.cx);

		double xmin = br.xmin;
		for(int j=0; j<n; j++)
		{
			xit[j] *= xmin;
			p[j] += xit[j];
		}

		return br.fmin;
	}

	/**
	 * Bracket the minimum.
	 */
	private static class BracketMin
	{
		private final static double GOLD = 1.618034, GLIMIT = 100.0, TINY = 1.0e-20;
		private double ax, bx, cx, fa, fb, fc;

		public BracketMin(LineMinimization linMin, double ax, double bx) throws Exception
		{
			
			this.ax = ax;
			this.bx = bx;

			double ulim, u, r, q, fu, temp;
			fa = linMin.f1dim(ax);
			fb = linMin.f1dim(bx);
			if (fb>fa)
			{
				temp = ax; ax = bx; bx = temp;
				temp = fa; fa = fb; fb = temp;
			}
			cx = bx+GOLD*(bx-ax);
			fc = linMin.f1dim(cx);
			while (fb>fc && linMin.iter < linMin.maxEvaluations)
			{
				r = (bx-ax)*(fb-fc);
				q = (bx-cx)*(fb-fa);
				u = bx-((bx-cx)*q-(bx-ax)*r)/(2.0*sign(Math.max(Math.abs(q-r),TINY),q-r));
				ulim = bx+GLIMIT*(cx-bx);
				if ((bx-u)*(u-cx)>0.0)
				{
					fu = linMin.f1dim(u);
					if(fu<fc)
					{
						ax = bx; bx = u; fa = fb; fb = fu;
						return;
					}
					else if(fu>fb)
					{
						cx = u; fc = fu;
						return;
					}
					u = cx+GOLD*(cx-bx);
					fu = linMin.f1dim(u);
				}
				else if ((cx-u)*(u-ulim)>0.0)
				{
					fu = linMin.f1dim(u);
					if (fu<fc)
					{
						bx = cx; cx = u; u = cx+GOLD*(cx-bx);
						fb = fc; fc = fu; fu = linMin.f1dim(u);
					}
				}
				else if ((u-ulim)*(ulim-cx)>=0.0)
				{
					u = ulim;
					fu = linMin.f1dim(u);
				}
				else
				{
					u = cx+GOLD*(cx-bx);
					fu = linMin.f1dim(u);
				}
				ax = bx; bx = cx; cx = u;
				fa = fb; fb = fc; fc = fu;
			}
			return;
		}
	}

	/**
	 * Brent's method for 1-dimensional optimization.
	 */
	private static class Brent
	{
		// golden section
		private final static double CGOLD = 0.3819660;
		// default tolerance
		private static double myZeps = 1.0e-3, tol = 3.0e-8;
		// abscissa and ordinate of the minimum
		private double xmin, fmin = 0.0;
	

		public Brent(LineMinimization linMin, double ax, double bx, double cx) throws Exception {
			//if(!((ax<bx&&bx<cx)||(ax>bx&&bx>cx)))
			//	throw new ArithmeticException("Invalid arguments");

			/* a & b bracket the minimum.
			 * x is the point with the smallest value, f(x), so far
			 * w is the point with the second smallest value, f(w)
			 * v is the previous value of w
			 * u is the point at which the function was most recently evaluated
			 * xm is midpoint between a and b
			 */
			double a, b, d = 0.0, etemp, fu, fv, fw, fx, p, q ,r, tol1, tol2, u, v, w, x, xm, e = 0.0;
			// make a<c
			a = ax<cx ? ax : cx;
			b = ax>cx ? ax : cx;
			x = w = v = bx;
			fw = fv = fx = linMin.f1dim(x);
			
			// maximum iterations		
			//int maxIterations = linMin.getParameter("p1").intValue(); //100
			
			
//			for (brentIter=0; brentIter<maxIterations && linMin.iter < linMin.maxEvaluations; brentIter++)
			for (int brentIter = linMin.iter; brentIter<linMin.maxEvaluations; brentIter++)
			{
				
				xm = 0.5*(a+b);
				// fractional tolerance at x
				// Parker myZeps replaces ZEPS
				tol2 = 2.0*(tol1=tol*Math.abs(x)+myZeps);
				// test for done. 1. not done if the interval b-a is more than
				// 4*tol1. 2. done if distance from center is less than 2*tol1
				// minus 1/2 the interval.
				if (Math.abs(x-xm)<=(tol2-0.5*(b-a)))
				{
					xmin = x; fmin = fx;
					return;
				}
				if (Math.abs(e)>tol1)
				{
					// parabolic interpolation through x, v, w
					r = (x-w)*(fx-fv);
					q = (x-v)*(fx-fw);
					p = (x-v)*q-(x-w)*r;
					q = 2.0*(q-r);
					if(q>0.0) p = -p;
					q = Math.abs(q);
					etemp = e;
					e=d;
					// accept if 1 in (a,b) and 2 move less than half the
					// previous step, e.g. converging
					if (Math.abs(p)>=Math.abs(0.5*q*etemp) || p<=q*(a-x) || p>=q*(b-x))
						d = CGOLD*(e = (x>xm ? a-x : b-x));
					else
					{
						d = p/q; u = x+d;
						if (u-a<tol2 || b-u<tol2)
							d = sign(tol1, xm-x);
					}
				}
				else
				{
					d = CGOLD*(e = (x>=xm ? a-x : b-x));
				}
				u = (Math.abs(d)>=tol1 ? x+d : x+sign(tol1, d));
				fu = linMin.f1dim(u);

				if(fu<=fx)
				{
					if (u>=x)
						a = x;
					else
						b = x;
					v = w; w = x; x = u;
					fv = fw; fw = fx; fx = fu;
				}
				else
				{
					if(u<x)
						a = u;
					else
						b = u;
					if(fu<=fw || w==x)
					{
						v = w; w = u; fv = fw; fw = fu;
					}
					else if(fu<=fv || v==x || v==w)
					{
						v = u; fv = fu;
					}
				}
				
				linMin.iter++;
			}
			xmin = x; fmin = fx;
		}
	}

//	/**
//	 * Note: since the linMin algorithm is meant for unconstrained optimization, we need to
//	 * introduce a penalty factor for solutions outside the bounds. 
//	 *  
//	 * @param x
//	 * @return
//	 * @throws Exception
//	 */
//	private double fConstraint(double[] x) throws Exception
//	{
//		boolean outsiteBounds = false; 
//		for (int j = 0; j < n && !outsiteBounds; j++)
//		{
//			if (x[j] < bounds[j][0] || x[j] > bounds[j][1])
//				outsiteBounds = true;
//		}
//
//		if (outsiteBounds)
//			return PENALTY;
//		else
//		{
//			iter++;
//			return problem.f(x);
//		}
//	}

	/**
	 * 1-dimensional variable along the specified direction.
	 * 
	 * @param x
	 * @return
	 * @throws Exception
	 */
	private double f1dim(double x) throws Exception
	{
		double[] xt = new double[n];
		for (int j = 0; j < n; j++)
			xt[j] = p1dim[j]+x*xi1dim[j];		
		return fConstraint(xt, bounds, PENALTY, problem);
	}

	private static double sign(double a, double b)
	{
		return b>=0.0 ? Math.abs(a) : -Math.abs(a);
	}
}
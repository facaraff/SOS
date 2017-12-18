package algorithms;

import static utils.algorithms.Misc.generateRandomSolution;


import utils.MatLab;
import static utils.algorithms.Misc.fConstraint;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/**
 * This class implements Powell's minimization method which is quadratically convergent.
 * After Press WH, Teukolsky SA, Vetterling WT, Flannery BP: Numerical
 * recipes in C++. Cambridge University Press, 2002, Chap. 10
 */
public class Powell extends Algorithm
{
	private final static double TINY = 1.0e-25;
	private final static double MIN_VECTOR_LENGTH = 1.0e-3;
	private final static double PENALTY = 1e20;
	private final static boolean unitDirectionVectors = true;

	private double[] p;
	private double[][] xi;
	private double ftol, fret;
	private int n, iter;

	private int maxEvaluations;
	private Problem problem;
	private double[][] bounds;

	// temp variables
	private double[] p1dim;
	private double[] xi1dim;

	// current best
	double[] best;
	// current best fitness
	double fBest;

	FTrend FT = new FTrend();
	
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		
		ftol = getParameter("p0").doubleValue();	// fitness tolerance
		
		this.maxEvaluations = maxEvaluations;
		this.problem = problem;
		this.bounds = problem.getBounds();

		n = problem.getDimension();

		p1dim = new double[n];
		xi1dim = new double[n];
		
		

		double del, fp, fptt, t;
		
		// initial point
		p = new double[n];
		if (initialSolution != null)
			p = initialSolution;
		else
			p = generateRandomSolution(bounds, n);


		xi = MatLab.eye(n);

		int ibig;
		
		double[] pt = new double[n], ptt = new double[n], xit = new double[n];
		fret = fConstraint(p, bounds, PENALTY,problem);
		for (int j=0; j<n; j++)
			pt[j] = p[j];

		// initial best
		best = new double[n];
		for(int k=0;k<n;k++)
			best[k] = p[k];
		fBest = fret;
		FT.add(0, fBest);

		for (iter=1; iter < maxEvaluations; iter++)
		{
			fp = fret;
			ibig = 0;
			del = 0.0;
			
			// minimize in all directions, p records the change
			for (int i=0; i<n; i++)
			{
				for (int j=0; j<n; j++)
					xit[j] = xi[j][i];
				fptt = fret;
				fret = lineMinimization(p, xit);

				if (fret < fBest)
				{
					for (int j=0; j < n; j++)
						best[j] = p[j];
					fBest = fret;
					FT.add(iter, fBest);
				}

				// find direction with largest change
				if (fptt-fret>=del)
				{
					del = fptt-fret;
					ibig = i+1;
				}
			}

			// fractional change in one iteration, fp-fret/|fret|, is less than the tolerance
			if (2.0*(fp-fret)<=ftol*(Math.abs(fp)+Math.abs(fret))+TINY)
				break;

			for (int j=0; j<n; j++)
			{
				ptt[j] = 2.0*p[j]-pt[j];
				xit[j] = p[j]-pt[j];
				pt[j] = p[j];
			}

			fptt = fConstraint(ptt, bounds, PENALTY,problem);

			if (fptt < fBest)
			{
				for (int j = 0; j < n; j++)
					best[j] = ptt[j];
				fBest = fptt;
				FT.add(iter, fBest);
			}

			if (fptt<fp)
			{
				t = 2.0*(fp-2.0*fret+fptt)*Math.pow(fp-fret-del,2)-del*Math.pow(fp-fptt,2);
				if (t<0.0)
				{
					fret = lineMinimization(p, xit);

					if (fret < fBest)
					{
						for (int j = 0; j < n; j++)
							best[j] = p[j];
						fBest = fret;
						FT.add(iter, fBest);
					}

					// Parker option
					if (ibig > 0)
					{
						if (unitDirectionVectors)
						{
							double z = 0.0;
							for (int j=0; j<n; j++)
								z += xit[j]*xit[j];
							z = Math.sqrt(z);
							if (z>MIN_VECTOR_LENGTH)
							{
								z = 1.0/z;
								for (int j=0; j<n; j++)
								{
									xi[j][ibig-1] = xi[j][n-1];
									xi[j][n-1] = xit[j]*z;
								}
							}
						}
						else
						{
							// from Numerical Recipes 
							for(int j=0; j<n; j++)
							{
								xi[j][ibig-1] = xi[j][n-1];
								xi[j][n-1] = xit[j];
							}
						}
					}
				}
			}
		}

		if (fret < fBest)
		{
			for (int j = 0; j < n; j++)
				best[j] = p[j];
			fBest = fret;
			FT.add(iter, fBest);
		}

		finalBest = best;

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

		public BracketMin(Powell powell, double ax, double bx) throws Exception
		{
			//if (ax==bx)
			//	throw new IllegalArgumentException("ax == bx");
			this.ax = ax;
			this.bx = bx;

			double ulim, u, r, q, fu, temp;
			fa = powell.f1dim(ax);
			fb = powell.f1dim(bx);
			if (fb>fa)
			{
				temp = ax; ax = bx; bx = temp;
				temp = fa; fa = fb; fb = temp;
			}
			cx = bx+GOLD*(bx-ax);
			fc = powell.f1dim(cx);
			while (fb>fc && powell.iter < powell.maxEvaluations)
			{
				r = (bx-ax)*(fb-fc);
				q = (bx-cx)*(fb-fa);
				u = bx-((bx-cx)*q-(bx-ax)*r)/(2.0*sign(Math.max(Math.abs(q-r),TINY),q-r));
				ulim = bx+GLIMIT*(cx-bx);
				if ((bx-u)*(u-cx)>0.0)
				{
					fu = powell.f1dim(u);
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
					fu = powell.f1dim(u);
				}
				else if ((cx-u)*(u-ulim)>0.0)
				{
					fu = powell.f1dim(u);
					if (fu<fc)
					{
						bx = cx; cx = u; u = cx+GOLD*(cx-bx);
						fb = fc; fc = fu; fu = powell.f1dim(u);
					}
				}
				else if ((u-ulim)*(ulim-cx)>=0.0)
				{
					u = ulim;
					fu = powell.f1dim(u);
				}
				else
				{
					u = cx+GOLD*(cx-bx);
					fu = powell.f1dim(u);
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
		private int brentIter;

		public Brent(Powell powell, double ax, double bx, double cx) throws Exception {
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
			fw = fv = fx = powell.f1dim(x);
			
			// maximum iterations		
			int maxIterations = powell.getParameter("p1").intValue(); //100
			
			for (brentIter=0; brentIter<maxIterations && powell.iter < powell.maxEvaluations; brentIter++)
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
				fu = powell.f1dim(u);

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
			}
			xmin = x; fmin = fx;
		}
	}

//	/**
//	 * Note: since the Powell algorithm is meant for unconstrained optimization, we need to
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
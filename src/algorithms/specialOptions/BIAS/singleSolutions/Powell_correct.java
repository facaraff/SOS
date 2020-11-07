/**
Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package algorithms.specialOptions.BIAS.singleSolutions;

import static utils.algorithms.operators.ISBOp.generateRandomSolution;
import static utils.algorithms.Misc.cloneSolution;
import static utils.algorithms.Misc.fillAWithB;

import utils.MatLab;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.algorithms.Counter;

/**
 * This class implements Powell's minimization method which is quadratically convergent.
 * After Press WH, Teukolsky SA, Vetterling WT, Flannery BP: Numerical
 * recipes in C++. Cambridge University Press, 2002, Chap. 10
 */
public class Powell_correct extends AlgorithmBias
{
	private final static double TINY = 1.0e-25;
	private final static double MIN_VECTOR_LENGTH = 1.0e-3;
	private final static boolean unitDirectionVectors = true;
	private double[] dismissPreviousPt;
	protected boolean addBestDetails = false;
	
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
		this.numberOfCorrections = 0;
		ftol = getParameter("p0").doubleValue();	// fitness tolerance 0.00001
		int maxIterations = getParameter("p1").intValue(); //100
		
		this.maxEvaluations = maxEvaluations;
		this.problem = problem;
		this.bounds = problem.getBounds();

		n = problem.getDimension();

		p1dim = new double[n];
		xi1dim = new double[n];
			
		
		String FullName = getFullName("PM"+this.correction,problem); 
		Counter PRGCounter = new Counter(0);
		createFile(FullName);
		
		
		int period = maxEvaluations/3;
		this.numberOfCorrections1 = this.numberOfCorrections2 = this.numberOfCorrections = 0;
		if(this.CID) this.infeasibleDimensionCounter = new int[n];
		
		int prevID = -1;
		int newID = 0;
		
		writeHeader("ftol "+ftol+" maxIterations "+maxIterations, problem);
		//prevID = newID;
		

		String line = new String();
		
		double del, fp, fptt, t;

		// initial point
		p = new double[n];
		if (initialSolution != null)
			p = initialSolution;
		else
		{
			p = generateRandomSolution(bounds, n,PRGCounter);
			fret = problem.f(p);
			
			newID++;
			line =""+newID+" "+formatter(fret)+" "+1+" "+prevID;
			for(int k = 0; k < n; k++)
				line+=" "+formatter(p[k]);
			line+="\n";
			bw.write(line);
			line = null;
			line = new String();
			prevID = newID;
		}

		this.dismissPreviousPt = cloneSolution(p);
		
//		for(int k = 0; k < n; k++)
//		if(dismissPreviousPt[k]<0 || dismissPreviousPt[k]>1) System.out.println("OUT!");
		
		xi = MatLab.eye(n);

		int ibig;
		
		double[] pt = new double[n], ptt = new double[n], xit = new double[n];
//		fret = fConstraint(p, bounds, PENALTY,problem, FT);
//		p=toro(p,bounds);
//		fret = problem.f(p);
		for (int j=0; j<n; j++)
		{
			pt[j] = p[j];
			ptt[j] = p[j];
			xit[j] = p[j];
		}
		
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
			
			fillAWithB(this.dismissPreviousPt, best);
		
			// minimize in all directions, p records the change
			for (int i=0; i<n; i++)
			{
				for (int j=0; j<n; j++)
					xit[j] = xi[j][i];
				fptt = fret;
				
				fret = lineMinimization(p, xit, maxIterations, period, PRGCounter);
				
				
				incrementViolatedDimensions(p, bounds);
				
				p = correct(p,this.dismissPreviousPt, bounds, PRGCounter);
				
				storeNumberOfCorrectedSolutions(period,iter);

				for(int k = 0; k < n; k++)
					if(dismissPreviousPt[k]<0 || dismissPreviousPt[k]>1) System.out.println("this OUT!");
				for(int k = 0; k < n; k++)
					if(best[k]<0 || best[k]>1) System.out.println("best OUT!");
				for(int k = 0; k < n; k++)
					if(p[k]<0 || p[k]>1) System.out.println("p OUT!");
				for(int k = 0; k < n; k++)
					if(ptt[k]<0 || ptt[k]>1) System.out.println("ptt OUT!");
				

				if (fret < fBest)
				{
					for (int j=0; j < n; j++)
						best[j] = p[j];
					fBest = fret;
					FT.add(iter, fBest);
					
					for(int k = 0; k < n; k++)
						if(best[k]<0 || best[k]>1) System.out.println("OUT!");

					
					
					line =""+newID+" "+formatter(fret)+" "+iter+" "+prevID;
					for(int k = 0; k < n; k++)
						line+=" "+formatter(p[k]);
					line+="\n";
					bw.write(line);
					line = null;
					line = new String();
					prevID = newID;
						
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

//			fptt = fConstraint(ptt, bounds, PENALTY,problem, FT);
//			ptt = correct(ptt,best,bounds);
			
			incrementViolatedDimensions(ptt, bounds);
			
			ptt = correct(ptt,this.dismissPreviousPt,bounds, PRGCounter);
			
			storeNumberOfCorrectedSolutions(period,iter);
			
			fptt = problem.f(ptt);
			iter+=FT.getExtraInt();

			if (fptt < fBest)
			{
				for (int j = 0; j < n; j++)
					best[j] = ptt[j];
				fBest = fptt;
				FT.add(iter, fBest);
				
				for(int k = 0; k < n; k++)
					if(best[k]<0 || best[k]>1) System.out.println("OUT!");
				
				line =""+newID+" "+formatter(fptt)+" "+iter+" "+prevID;
				for(int k = 0; k < n; k++)
					line+=" "+formatter(ptt[k]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				prevID = newID;
			}

			if (fptt<fp)
			{
				t = 2.0*(fp-2.0*fret+fptt)*Math.pow(fp-fret-del,2)-del*Math.pow(fp-fptt,2);
				if (t<0.0)
				{
//					fret = lineMinimization(p, xit,maxIterations);				
//					double[] prevP = cloneSolution(p);
					fret = lineMinimization(p, xit, maxIterations, period,  PRGCounter);
					
					incrementViolatedDimensions(p, bounds);
					
					p = correct(p,this.dismissPreviousPt,bounds, PRGCounter);
					
					storeNumberOfCorrectedSolutions(period,iter);
					
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
		
		String s = "";
		if(addBestDetails) s = positionAndFitnessToString(best, fBest);
		
		//wrtiteCorrectionsPercentage(fileName, (double) this.numberOfCorrections/maxEvaluations,"correctionsSingleSol");
		
//		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(), "correctionsSingleSol");
		
		writeStats(FullName,  ((double)this.numberOfCorrections1/((double)period)),  ((double)this.numberOfCorrections2/((double)period*2)), (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(),s, "correctionsSingleSol");
		
		bw.close();
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
	private double lineMinimization(double[] p, double[] xit, int maxIterations, int period, Counter PRGCounter) throws Exception
	{
		
		for(int j=0; j<n; j++)
		{
			p1dim[j] = p[j];
			xi1dim[j] = xit[j];
		}

		// bracket minimum
		BracketMin bm = new BracketMin(this, 0.0, 1.0, period, PRGCounter);
		// optimize along direction
		Brent br = new Brent(this, bm.ax, bm.bx, bm.cx, maxIterations, period, PRGCounter);

//		
//		for(int k = 0; k < n; k++)
//		if(p[k] == 0.0) System.out.println("cazzo!");
		
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

		public BracketMin(Powell_correct powell, double ax, double bx, int period, Counter PRGCounter) throws Exception
		{
			//if (ax==bx)
			//	throw new IllegalArgumentException("ax == bx");
			this.ax = ax;
			this.bx = bx;

			double ulim, u, r, q, fu, temp;
			fa = powell.f1dim(ax, period, PRGCounter);
			fb = powell.f1dim(bx, period, PRGCounter);
			if (fb>fa)
			{
				temp = ax; ax = bx; bx = temp;
				temp = fa; fa = fb; fb = temp;
			}
			cx = bx+GOLD*(bx-ax);
			fc = powell.f1dim(cx, period, PRGCounter);
			while (fb>fc && powell.iter < powell.maxEvaluations)
			{
				r = (bx-ax)*(fb-fc);
				q = (bx-cx)*(fb-fa);
				u = bx-((bx-cx)*q-(bx-ax)*r)/(2.0*sign(Math.max(Math.abs(q-r),TINY),q-r));
				ulim = bx+GLIMIT*(cx-bx);
				if ((bx-u)*(u-cx)>0.0)
				{
					fu = powell.f1dim(u, period, PRGCounter);
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
					fu = powell.f1dim(u, period, PRGCounter);
				}
				else if ((cx-u)*(u-ulim)>0.0)
				{
					fu = powell.f1dim(u, period, PRGCounter);
					if (fu<fc)
					{
						bx = cx; cx = u; u = cx+GOLD*(cx-bx);
						fb = fc; fc = fu; fu = powell.f1dim(u, period,  PRGCounter);
					}
				}
				else if ((u-ulim)*(ulim-cx)>=0.0)
				{
					u = ulim;
					fu = powell.f1dim(u, period, PRGCounter);
				}
				else
				{
					u = cx+GOLD*(cx-bx);
					fu = powell.f1dim(u,period, PRGCounter);
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

		public Brent(Powell_correct powell, double ax, double bx, double cx, int maxIterations, int period, Counter PRGCounter) throws Exception {
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
			fw = fv = fx = powell.f1dim(x, period, PRGCounter);
			
			// maximum iterations		
//			int maxIterations = powell.getParameter("p1").intValue(); //100
			
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
				fu = powell.f1dim(u, period,  PRGCounter);

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


	/**
	 * 1-dimensional variable along the specified direction.
	 * 
	 * @param x
	 * @return
	 * @throws Exception
	 */
	private double f1dim(double x, int period,  Counter PRGCounter) throws Exception
	{
		double[] xt = new double[n];
		for (int j = 0; j < n; j++)
			xt[j] = p1dim[j]+x*xi1dim[j];		
		
		incrementViolatedDimensions(xt, bounds);
		
		double[] temp = correct(xt,this.dismissPreviousPt,bounds, PRGCounter);
		
		storeNumberOfCorrectedSolutions(period,iter);
		
		return problem.f(temp);
	}

	private static double sign(double a, double b)
	{
		return b>=0.0 ? Math.abs(a) : -Math.abs(a);
	}
}
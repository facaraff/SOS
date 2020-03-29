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
package utils.algorithms.operators;


import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.operators.DEOp.currentToRand1;

import utils.algorithms.Misc;


import java.util.Arrays;
import  utils.algorithms.Corrections;
import utils.MatLab;
import utils.random.RandUtils;
import interfaces.Problem;



import static utils.RunAndStore.FTrend;

public class MemesLibrary
{	
	private static double[] Brent(double[]p1dim, double[] xi1dim, double ax, double bx, double cx, 
			Problem prob, int iterations, int localBudget, int iter, int totalBudget) throws Exception
	{
		// golden section
		double CGOLD = 0.3819660;
		// default tolerance
		double myZeps = 1.0e-3, tol = 3.0e-8;
		// abscissa and ordinate of the minimum
		double xmin, fmin = 0.0;
		int maxBrentIterations = 100;
		
		double a, b, d = 0.0, etemp, fu, fv, fw, fx, p, q ,r, tol1, tol2, u, v, w, x, xm, e = 0.0;
		// make a<c
		a = ax<cx ? ax : cx;
		b = ax>cx ? ax : cx;
		x = w = v = bx;
		fw = fv = fx = f1dim(p1dim, xi1dim, x, prob);
		
		for (int brentIteration = 0; brentIteration<maxBrentIterations && iter < totalBudget && iterations < localBudget; brentIteration++)
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
				double[] ret = {iter,iterations, xmin, fmin};
				return ret;
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
			fu = f1dim(p1dim, xi1dim, u, prob);
			iter++; iterations++;

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
		double[] ret = {iter, iterations, xmin, fmin};
		return ret;
	}
	
	private static double fConstraint(double[] x, Problem prob) throws Exception
	{
		double PENALTY = 1e20;
		boolean outsiteBounds = false;
		int n = prob.getDimension();
		double[][] bounds = prob.getBounds();
		for (int j = 0; j < n && !outsiteBounds; j++)
		{
			if (x[j] < bounds[j][0] || x[j] > bounds[j][1])
				outsiteBounds = true;
		}

		if (outsiteBounds)
			return PENALTY;
		else
		{
			return prob.f(x);
		}
	}
	
	private static double f1dim(double[] p1dim, double[] xi1dim,double x, Problem prob) throws Exception
	{
		int n = prob.getDimension();
		double[] xt = new double[n];
		for (int j = 0; j < n; j++)
			xt[j] = p1dim[j]+x*xi1dim[j];		
		return fConstraint(xt, prob);
	}
	
	private static double sign(double a, double b) {
		return b>=0.0 ? Math.abs(a) : -Math.abs(a);
	}
	
	private static double[] bracketMin(double[] p1dim, double[] xi1dim, double ax, double bx, 
			Problem prob, int iterations, int localBudget, int iter, int totalBudget) throws Exception
	{
		double GOLD = 1.618034, GLIMIT = 100.0, TINY = 1.0e-20;
		double cx, fa, fb, fc;

		//if (ax==bx)
		//	throw new IllegalArgumentException("ax == bx");
		double ulim, u, r, q, fu, temp;
		fa = f1dim(p1dim, xi1dim, ax, prob);
		iter++; iterations++;
		fb = f1dim(p1dim, xi1dim, bx, prob);
		iter++; iterations++;
		if (fb>fa)
		{
			temp = ax; ax = bx; bx = temp;
			temp = fa; fa = fb; fb = temp;
		}
		cx = bx+GOLD*(bx-ax);
		fc = f1dim(p1dim, xi1dim, cx, prob);
		iter++; iterations++;
		while (fb>fc && iter < totalBudget && iterations < localBudget)
		{
			r = (bx-ax)*(fb-fc);
			q = (bx-cx)*(fb-fa);
			u = bx-((bx-cx)*q-(bx-ax)*r)/(2.0*sign(Math.max(Math.abs(q-r),TINY),q-r));
			ulim = bx+GLIMIT*(cx-bx);
			if ((bx-u)*(u-cx)>0.0)
			{
				fu = f1dim(p1dim, xi1dim, u, prob);
				iter++; iterations++;
				if(fu<fc)
				{
					ax = bx; bx = u; fa = fb; fb = fu;
					double[] ret = {iter, iterations, ax, bx, cx};
					return ret;
				}
				else if(fu>fb)
				{
					cx = u; fc = fu;
					double[] ret = {iter, iterations, ax, bx, cx};
					return ret;
				}
				u = cx+GOLD*(cx-bx);
				fu = f1dim(p1dim, xi1dim, u, prob);
				iter++; iterations++;
			}
			else if ((cx-u)*(u-ulim)>0.0)
			{
				fu = f1dim(p1dim, xi1dim, u, prob);
				iter++; iterations++;
				if (fu<fc)
				{
					bx = cx; cx = u; u = cx+GOLD*(cx-bx);
					fb = fc; fc = fu; fu = f1dim(p1dim, xi1dim, u, prob);
					iter++; iterations++;
				}
			}
			else if ((u-ulim)*(ulim-cx)>=0.0)
			{
				u = ulim;
				fu = f1dim(p1dim, xi1dim, u, prob);
				iter++; iterations++;
			}
			else
			{
				u = cx+GOLD*(cx-bx);
				fu = f1dim(p1dim, xi1dim, u, prob);
				iter++; iterations++;
			}
			ax = bx; bx = cx; cx = u;
			fa = fb; fb = fc; fc = fu;
		}	
			
		double[] ret = {iter, iterations, ax, bx, cx};
		return ret;
	}
	
	private static double[] lineMinimization(double[] p1dim, double[] xi1dim, double[] p, double[] xit, 
			Problem prob, int iterations, int localBudget, int iter, int totalBudget) throws Exception
	{
		int n = prob.getDimension();
		for(int j=0; j<n; j++)
		{
			p1dim[j] = p[j];
			xi1dim[j] = xit[j];
		}

		// bracket minimum
		double[] bracket = bracketMin(p1dim, xi1dim, 0.0, 1.0, prob, iterations, localBudget, iter, totalBudget);
		iter = (int) bracket[0];
		iterations = (int) bracket[1];
		double ax = bracket[2];
		double bx = bracket[3];
		double cx = bracket[4];

		// optimize along direction
		double[] Brent = Brent(p1dim, xi1dim, ax,bx,cx, prob, iterations, localBudget, iter, totalBudget);
		iter = (int) Brent[0];
		iterations = (int) Brent[1];
		double xmin = Brent[2];
		double fmin = Brent[3];
		for(int j=0; j<n; j++)
		{
			xit[j] *= xmin;
			p[j] += xit[j];
		}

		double[] ret = {iter,iterations, fmin};
		return ret;
	}
	
	private static void setSubSet(boolean[] change, int N, int maxVars)
	{		
		int iniVar = RandUtils.randomInteger(N-1);
		int numVars = (int)(N*0.2); 
		if(numVars >= maxVars)
			numVars = maxVars;
		for(int currentVar = iniVar; currentVar < numVars + iniVar; currentVar++)
			change[currentVar%N] = true; 
	}

	/** SUBGROUPING SOLIS WET' METHOD **/
	/** this function performs "maxEval" iterations and refine the vector "sol" returning its fitness value in out[0]. 
	 * The final value of rho is saved in out[1], while the number of iterations performed in stored in out[2].  **/
	public static double[] SSW(double[] sol, double fit, double[] bias, double rho, 
			Problem prob, int maxVars, int maxEval, int maxEvalSubSet, int totalBudget, int index, char correction) throws Exception
	{
		int N = prob.getDimension();
		double[][] bounds = prob.getBounds();
		int numSuccess = 0;
		int numFailed = 0;
		int numEval = 0;
		double newFit;
		double[] dif = new double[N];
		boolean[] change = new boolean[N];
		double[] solFirst = new double[N];
		double[] solSecond = new double[N];
		while(numEval < maxEval && index < totalBudget)
		{
			if(numEval%maxEvalSubSet == 0)
			{
				for(int n=0; n < N; n++)
					change[n] = false;
				dif = new double[N];
				setSubSet(change, N , maxVars);
			}
			for(int n=0; n < N; n++)
				if(change[n] == true)
					dif[n] = RandUtils.gaussian(0,rho);
			solFirst = MatLab.sum(MatLab.sum(sol ,bias ) , dif );
			solFirst = Corrections.correct( correction, solFirst, bounds);
			newFit = prob.f(solFirst);
			numEval++; index++;
			if(newFit < fit)
			{
				fit = newFit;
				for(int n=0; n < N; n++)
					sol[n] = solFirst[n];
				//bias = sum(multiply(0.2, bias), multiply(0.4, sum(dif, bias)));
				for(int n=0; n < N; n++)
					bias[n] = 0.2*bias[n] + 0.4*(dif[n] + bias[n]);
				numSuccess++;
				numFailed = 0;
			}
			else if(numEval < maxEval && index < totalBudget)
			{
				solSecond = MatLab.subtract(MatLab.subtract(sol, bias) , dif  );
				solSecond = Corrections.correct( correction, solSecond, bounds);
				newFit = prob.f(solSecond);
				numEval++; index++;
				if(newFit < fit)
				{
					fit = newFit;
					for(int n=0; n < N; n++)
						sol[n] = solSecond[n];
					//bias = subtract(bias, multiply(0.4, sum(dif, bias)));
					for(int n=0; n < N; n++)
						bias[n] = bias[n] - 0.4*(dif[n] + bias[n]);
					numSuccess++;
					numFailed = 0;
				}
				else
				{
					numFailed++;
					numSuccess = 0;
				}
			}

			if(numSuccess > 5)
			{
				rho = rho*2;
				numSuccess = 0;
			}
			else if(numFailed > 3)
			{
				rho = rho/2;
				numFailed = 0;
			}

		}

		double [] out = {fit, rho, numEval};
		return out;	
	}

	/**  SOLIS WET METHOD  **/
	public static double[] SW(double[] sol, double fit, double[] bias, double[] rho, Problem prob, int maxEval, int totalBudget, int iter, char correction) throws Exception
	{
		int N = prob.getDimension();
		double[][] bounds = prob.getBounds();
		int numSuccess = 0;
		int numFailed = 0;
		int numEval = 0;

		double newFit;
		double[] dif = new double[N];
		double[] solFirst = new double[N];
		double[] solSecond = new double[N];

		while (numEval < maxEval && iter < totalBudget)
		{	
			for(int n=0; n < N; n++)
				dif[n] = RandUtils.gaussian(0,rho[n]);
			solFirst = MatLab.sum(MatLab.sum(sol ,bias ) , dif );
			solFirst = Corrections.correct( correction, solFirst, bounds);
			newFit = prob.f(solFirst);
			numEval++; iter++;
			if(newFit < fit)
			{
				fit = newFit;
				for(int n=0; n < N; n++)
					sol[n] = solFirst[n];
				//bias = sum(multiply(0.2, bias), multiply(0.4, sum(dif, bias)));
				for(int n=0; n < N; n++)
					bias[n] = 0.2*bias[n] + 0.4*(dif[n] + bias[n]);
				numSuccess++;
				numFailed = 0;
			}
			else if(numEval < maxEval && iter < totalBudget)
			{
				solSecond = MatLab.subtract(MatLab.subtract(sol, bias), dif);
				solSecond = Corrections.correct( correction, solSecond, bounds);
				newFit = prob.f(solSecond);
				numEval++; iter++;
				if(newFit < fit)
				{
					fit = newFit;
					for(int n=0; n < N; n++)
						sol[n] = solSecond[n];
					//bias = subtract(bias, multiply(0.4, sum(dif, bias)));
					for(int n=0; n < N; n++)
						bias[n] = bias[n] - 0.4*(dif[n] + bias[n]);
					numSuccess++;
					numFailed = 0;
				}
				else
				{
					numFailed++;
					numSuccess = 0;
				}

			}

			if(numSuccess > 5)
			{
				for(int n=0; n < N; n++)
					rho[n] = rho[n]*2;
				numSuccess = 0;
			}
			else if(numFailed > 3)
			{
				for(int n=0; n < N; n++)
					rho[n] = rho[n]/2;
				numFailed = 0;
			}
		}

		double [] out = {fit, numEval};
		return out;	
	}

	/** ISPO **/
	public static double[] ISPO(double[] sol, double fit, double A, double P, int B, int S, double E, int PartLoop, 
			Problem prob, int maxEval, int totalBudget, int iter, char correction) throws Exception
	{
		int problemDimension = prob.getDimension(); 
		double[][] bounds = prob.getBounds();
		int numEval = 0;
		// temp solution
		double[] particle = sol;
		double fParticle = fit;

		double L = 0;
		double velocity = 0;
		double oldfFParticle = fParticle;
		double posOld;

		while (numEval < maxEval && iter < totalBudget)
		{
			for (int j = 0; j < problemDimension && numEval < maxEval && iter < totalBudget; j++)
			{
				// init learning factor
				L = 0;

				// for each part loop
				for (int k = 0; k < PartLoop && numEval < maxEval && iter < totalBudget; k++)
				{		
					// old fitness value
					oldfFParticle = fParticle;
					// old particle position
					posOld = particle[j];

					// calculate velocity
					velocity = A/Math.pow(k+1,P)*(-0.5+RandUtils.random())+B*L;

					// calculate new position
					particle[j] += velocity;
					particle[j] = MatLab.min(MatLab.max(particle[j], bounds[j][0]), bounds[j][1]);

					// calculate new fitness
					fParticle = prob.f(particle);
					numEval++; iter++;

					// estimate performance
					if (oldfFParticle < fParticle)
					{
						// adjust learning factor
						if (L != 0)
							L /= S;
						if (Math.abs(L) < E)
							L = 0;
						// use old position
						particle[j] = posOld;
						fParticle = oldfFParticle;
					}
					else
					{
						// use current velocity as learning factor
						L = velocity;

					}
				}	
			}
		}
		double[] out = {fParticle, numEval};
		return  out;
	}

	/**  3SOME's long distance searcher **/
	public static double[] ThreeSome_LongDistance(double[] sol, double fit, double globalAlpha, 
			Problem prob, int totalBudget, double exitPercentage, int iter, char correction) throws Exception
	{
		int N = prob.getDimension();
		double[][] bounds = prob.getBounds();

		double globalCR = Math.pow(0.5, (1/(N*globalAlpha)));

		double[] x = new double[N];
		double fx;
		int numEval = 0;
		boolean improved = false;

		while (numEval < totalBudget*exitPercentage && iter < totalBudget && !improved )
		{				
			x = Misc.generateRandomSolution(bounds, N);
			x = crossOverExp(sol, x, globalCR);
			fx = prob.f(x);
			numEval++; iter++;

			// best update
			if (fx < fit)
			{
				fit = fx;
				for(int n=0; n < N; n++)
					sol[n] = x[n];
				improved = true;
			}
		}
		int imp;
		imp = improved ? 1 : 0;
		double[] out = {fit, numEval, imp};
		return out;
	}
	
	
	/** 3SOME's short distance searcher **/
	/** Standard settings: deepLSRadius = 0.4, deepLSSteps = 150 **/
	public static double[] ThreeSome_ShortDistance(double[] sol, double fit, double deepLSRadius, int deepLSSteps, Problem prob, int totalBudget, int iter, FTrend  FT, char correction) throws Exception
	{
		int numEval = 0;
		int problemDimension = prob.getDimension();
		double[][] bounds = prob.getBounds();

		double[] SR = new double[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;

		boolean improve = true;
		int j = 0;
		while ((j < deepLSSteps) && (iter < totalBudget))
		{	
			double[] Xk = new double[problemDimension];
			double[] Xk_orig = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
			{
				Xk[k] = sol[k];
				Xk_orig[k] = sol[k];
			}

			double fXk_orig = fit;

			
			if (!improve)
			{
				for (int k = 0; k < problemDimension; k++)
					SR[k] = SR[k]/2;
			}

			improve = false;
			int k = 0;
			while ((k < problemDimension) && (iter < totalBudget))
			{
				Xk[k] = Xk[k] - SR[k];
				Xk = Corrections.correct( correction, Xk, bounds);
				double fXk = prob.f(Xk);
				iter++; numEval++;

				// best update
				if (fXk < fit)
				{
					fit = fXk;
					for (int n = 0; n < problemDimension; n++)
						sol[n] = Xk[n];
					if(problemDimension%iter==0) FT.add(iter, fit);
				}

				if (iter < totalBudget)
				{
					if (fXk == fXk_orig)
					{
						for (int n = 0; n < problemDimension; n++)
							Xk[n] = Xk_orig[n];
					}
					else
					{
						if (fXk > fXk_orig)
						{
							Xk[k] = Xk_orig[k];
							Xk[k] = Xk[k] + 0.5*SR[k];
							Xk = Corrections.correct( correction, Xk, bounds);
							fXk = prob.f(Xk);
							iter++; numEval++;

							// best update
							if (fXk < fit)
							{
								fit = fXk;
								for (int n = 0; n < problemDimension; n++)
									sol[n] = Xk[n];
								if(problemDimension%iter==0) FT.add(iter, fit);
							}

							if (fXk >= fXk_orig)
								Xk[k] = Xk_orig[k];
							else
								improve = true;
						}
						else
							improve = true;
					}
				}

				k++;
			}

			j++;
		}

		double[] out = {fit, numEval};
		return out;
	}

	/** 3SOME's short distance searcher with stop criterion **/
	/** Standard settings: deepLSRadius = 0.4, precision = 10^-6 **/
	public static double[] ThreeSome_ShortDistance(double[] sol, double fit, double deepLSRadius, double precision, Problem prob, int totalBudget, int iter, FTrend FT, char correction) throws Exception
	{
		int numEval = 0;
		int problemDimension = prob.getDimension();
		double[][] bounds = prob.getBounds();

		double[] SR = new double[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;

		boolean improve = true;

		//while ((SR[0] > precision) && (iter < totalBudget))
		while ((MatLab.max(SR) > precision) && (iter < totalBudget))
		{	
			double[] Xk = new double[problemDimension];
			double[] Xk_orig = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
			{
				Xk[k] = sol[k];
				Xk_orig[k] = sol[k];
			}

			double fXk_orig = fit;

			if (!improve)
			{
				for (int k = 0; k < problemDimension; k++)
					SR[k] = SR[k]/2;
			}

			improve = false;
			int k = 0;
			while ((k < problemDimension) && (iter < totalBudget))
			{
				Xk[k] = Xk[k] - SR[k];
				Xk = Corrections.correct( correction, Xk, bounds);
				double fXk = prob.f(Xk);
				iter++; numEval++;
				
				// best update
				if (fXk < fit)
				{
					fit = fXk;
					for (int n = 0; n < problemDimension; n++)
						sol[n] = Xk[n];
					if(problemDimension%iter==0) FT.add(iter, fit);
				}

				if (iter < totalBudget)
				{
					if (fXk == fXk_orig)
					{
						for (int n = 0; n < problemDimension; n++)
							Xk[n] = Xk_orig[n];
					}
					else
					{
						if (fXk > fXk_orig)
						{
							Xk[k] = Xk_orig[k];
							Xk[k] = Xk[k] + 0.5*SR[k];
							Xk = Corrections.correct( correction, Xk, bounds);
							fXk = prob.f(Xk);
							iter++; numEval++;

							// best update
							if (fXk < fit)
							{
								fit = fXk;
								for (int n = 0; n < problemDimension; n++)
									sol[n] = Xk[n];
								if(problemDimension%iter==0) FT.add(iter, fit);
							}

							if (fXk >= fXk_orig)
								Xk[k] = Xk_orig[k];
							else
								improve = true;
						}
						else
							improve = true;
					}
				}

				k++;
			}

		}

		double[] out = {fit, numEval};
		return out;
	}
	
	/**
	 * Intermediate perturbation (see 3SOME)
	 * 
	 * @param bounds
	 * @param solution
	 * @param intermediateLSRadius
	 * @return
	 */
	public static double[] intermediatePerturbation(double[][] bounds, double[] solution, double intermediateLSRadius)
	{
		int n = solution.length;
		double[] retValue = new double[n];
		for (int i = 0; i < n; i++)
			retValue[i] = solution[i] + intermediateLSRadius * (bounds[i][1]-bounds[i][0])*(-1+2*RandUtils.random());
		return retValue;
	}
	

	/** ROSENBROCK METHOD **/
	/** standard parameters setting: eps =  10e-5, alpha = 2, beta 0.5 **/
	public static double[] Rosenbrock(double[] sol, double fit, double eps, double alpha, double beta, Problem problem, int totalBudget, int iter, FTrend FT, char correction) throws Exception
	{		
		int n = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[][] xi = MatLab.eye(n);
		double[][] A = MatLab.zeros(n);
		double[] lambda = new double[n];
		double[] xCurrent = new double[n];
		double[] t = new double[n];
		double[] xk = new double[n];
		double[] d = new double[n];
		double[] x = new double[n];
		for (int i=0; i<n; i++)
			x[i] = sol[i];
		
		double yFirstFirst = fit;
		double yFirst = yFirstFirst;
		double yBest = yFirst;
		double yCurrent;
		for (int i=0; i<n; i++)
		{
			d[i] = 0.1;
			xk[i] = x[i];
		}
	
		boolean restart = true;
		double mini; double div;
		int numEval = 0;
		do
		{
			yBest = yFirstFirst;
			do
			{
				yFirst = yBest;
				for (int i = 0; (i < n) && (iter < totalBudget);i++)
				{
					for (int j=0;j<n;j++)
						 xCurrent[j]= xk[j]+d[i]*xi[i][j];
					xCurrent = Corrections.correct( correction, xCurrent, bounds);
					yCurrent = problem.f(xCurrent);
					iter++;
					numEval++;

		            if (yCurrent < yBest)
		            {
		            	lambda[i] += d[i];
		            	d[i] *= alpha;
		            	yBest = yCurrent;
		            	if(n%iter==0) FT.add(iter, yBest);
		            	for (int j=0;j<n;j++)
		            	{
		            		xk[j] = xCurrent[j];
		            		sol[j] = xCurrent[j];
		            	}
		            }
		            else
		            	d[i] *= -beta;
				}
			}
			while ((yBest < yFirst) && (iter < totalBudget));
			
			mini = MatLab.min(MatLab.abs(d));
			restart = mini>eps;
			
			if ((yBest < yFirstFirst) && (iter < totalBudget))
			{ 
				mini = MatLab.min(MatLab.abs(MatLab.subtract(xk,x)));
				restart = restart || (mini > eps);
				
				if (restart)
				{ 
					for (int i=0;i<n;i++)
						A[n-1][i] = lambda[n-1]*xi[n-1][i];
					for (int k=n-2; k>=0;k--)
					{
						for (int i=0;i<n;i++)
							A[k][i] = A[k+1][i] + lambda[i]*xi[k][i];
					}

					t[n-1] = lambda[n-1]*lambda[n-1];

					for (int i=n-2; i>=0;i--)
						t[i] = t[i+1] + lambda[i]*lambda[i];
					
					for (int i=n-1;i>0;i--)
					{
						div = Math.sqrt(t[i-1]*t[i]);
						if (div != 0)
							for (int j=0;j<n;j++)
								xi[i][j] = (lambda[i-1]*A[i][j]-xi[i-1][j]*t[i])/div;
					}
					div = Math.sqrt(t[0]);
					for (int i=0; i<n;i++)
					{
						if (div != 0)
							xi[0][i] = A[0][i]/div;	
						x[i] = xk[i];
						lambda[i] = 0;
						d[i] = 0.1;
					}
					yFirstFirst = yBest;
				}
			}
		}
		while (iter < totalBudget);

		double[] out = {yBest, numEval};
		return out;
	}

	/** SPSA **/
	/** Standard settings:  a = 0.5, A = 1, alpha = 0.602, c = 0.032, gamma = 0.1 **/
	public static double[] SPSA(double[] sol, double fit, double a, double A, double alpha, double c, double gamma, double eps, 
			Problem prob, int totalBudget, double exitPercentage, int iter, char correction) throws Exception
	{
		//double myEps=0.01;
		double myEps= eps;
		
		int k=0; int numEval = 0;
		double[] theta=Misc.cloneSolution(sol);
		double yOld = fit;
		int stopcounter=0;
		int dim = prob.getDimension();
		double[][] bounds = prob.getBounds();

		while(iter < totalBudget && numEval < exitPercentage*totalBudget)
		{
			k++;

			double ak = Math.pow(a/(k+1+A),alpha);
			double ck = Math.pow(c/(k+1),gamma);
			double[] delta = new double[dim];
			for(int j=0;j < dim;j++)
				delta[j] = (RandUtils.random() > 0.5) ? 1 : -1; //Math.round(RandUtils.random()*2)-1;
			double[] thetaplus = Corrections.correct( correction, MatLab.sum(theta,MatLab.multiply(ck,delta)), bounds);
			double[] thetaminus = Corrections.correct( correction, MatLab.sum(theta,MatLab.multiply(-ck,delta)), bounds);
			double yplus=prob.f(thetaplus);
			double yminus=prob.f(thetaminus);
			iter = iter + 2; numEval += 2;
			double[] ghat= new double[dim];
			for(int j=0;j < dim;j++)
				ghat[j]=(yplus-yminus)/(2*ck*delta[j]);
			theta = Corrections.correct( correction, MatLab.sum(theta,MatLab.multiply(-ak,ghat)), bounds);
			double y = prob.f(theta);
			iter++; numEval++;
			double[] ys={y, yplus, yminus};
			double[] ys0={y, yplus, yminus};
			Arrays.sort(ys);
			double yFinal = ys[0];
			if(yFinal == ys0[1])
				theta = thetaplus;
			if(yFinal == ys0[2])
				theta = thetaminus;
			double DELTA = 0;
			if(yOld > yFinal)
			{
				DELTA = yFinal - yOld;
				yOld = yFinal;

				for(int n=0; n < dim; n++)
					sol[n] = theta[n];
			}
			if(k>5)
				if(Math.abs(DELTA) < myEps)
					stopcounter++;
			if(stopcounter>10)
			{
				break;
			}
		}

		double[] out = {yOld, numEval};
		return out;
	}

	/** Multiple Trajectories Search LS1 (MODIFIED) **/
	/** The original version has been slightly modified by introducing a radius for each variable. 
	 * Standard settings: deepLSRadius = 0.4, deepLSRadius = 150 **/
	public static double[] MTS_LS1(double[] sol, double fit, double deepLSRadius, double deepLSSteps, 
			Problem prob, int totalBudget, int iter, char correction) throws Exception
	{
		int numEval = 0;
		int problemDimension = prob.getDimension();
		double[][] bounds = prob.getBounds();

		double[] SR = new double[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;

		boolean[] improve = new boolean[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			improve[k] = true;
		int j = 0;
		while ((j < deepLSSteps) && (iter < totalBudget))
		{	
			double[] Xk = new double[problemDimension];
			double[] Xk_orig = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
			{
				Xk[k] = sol[k];
				Xk_orig[k] = sol[k];
			}

			double fXk_orig = fit;

			for(int k = 0; k < problemDimension; k++)
				if (!improve[k])
					SR[k] = SR[k]/2;

			for(int k = 0; k < problemDimension; k++)
				improve[k] = false;
			int k = 0;
			while ((k < problemDimension) && (iter < totalBudget))
			{
				Xk[k] = Xk[k] - SR[k];
				Xk = Corrections.correct( correction, Xk, bounds);
				double fXk = prob.f(Xk);
				iter++; numEval++;

				// best update
				if (fXk < fit)
				{
					fit = fXk;
					for (int n = 0; n < problemDimension; n++)
						sol[n] = Xk[n];
				}

				if (iter < totalBudget)
				{
					if (fXk == fXk_orig)
					{
						for (int n = 0; n < problemDimension; n++)
							Xk[n] = Xk_orig[n];
					}
					else
					{
						if (fXk > fXk_orig)
						{
							Xk[k] = Xk_orig[k];
							Xk[k] = Xk[k] + 0.5*SR[k];
							Xk = Corrections.correct( correction, Xk, bounds);
							fXk = prob.f(Xk);
							iter++; numEval++;

							// best update
							if (fXk < fit)
							{
								fit = fXk;
								for (int n = 0; n < problemDimension; n++)
									sol[n] = Xk[n];
							}

							if (fXk >= fXk_orig)
								Xk[k] = Xk_orig[k];
							else
								improve[k] = true;
						}
						else
							improve[k] = true;
					}
				}

				k++;
			}

			j++;
		}

		double[] out = {fit, numEval};
		return out;
	}

	/** Multiple Trajectories Search LS2  **/
	public static double[] MTS_LS2(double[] sol, double fit, Problem prob, int numIterations, int totalBudget, int iter, char correction) throws Exception
	{
		boolean improve = true;
		int j = 0, numEval = 0;
		double[][] bounds = prob.getBounds();
		int dim = prob.getDimension();
		double[] SR = new double[dim];
		for (int k = 0; k < dim; k++)
			SR[k] = 0.4*(bounds[k][1] - bounds[k][0]);

		double[] Xk = Misc.cloneSolution(sol);
		double[] D = new double[dim];
		double[] r = new double[dim];
		double fXk = fit;

		while(iter < totalBudget && j < numIterations)
		{
			if(!improve)
			{
				for(int k = 0; k < dim; k++)
				{
					SR[k] = SR[k]/2;
					if(SR[j] < 0.00001)
						SR[k] = 0.4*(bounds[k][1] - bounds[k][0]);
				}
			}

			for(int n=0; n<dim && iter<totalBudget; n++)
			{
				for(int i=0; i < dim; i++)
				{
					D[i] = (RandUtils.random() > 0.5) ? 1 : -1;
					r[i] = (RandUtils.random() < 0.25) ? 0 : -1;
				}

				for(int i=0; i < dim; i++)
					if(r[i] == 0)
						Xk[i] = Xk[i] - SR[i]*D[i];
				Xk = Corrections.correct( correction, Xk, bounds);
				fXk = prob.f(Xk);
				iter++; numEval++;
				if(fXk < fit)
				{
					fit = fXk;
					sol = Misc.cloneSolution(Xk);
				}
				if(fXk == fit)
					Xk = Misc.cloneSolution(sol);
				else if(iter < totalBudget)
				{
					if(fXk > fit)
					{
						Xk = Misc.cloneSolution(sol);
						for(int i=0; i < dim; i++)
							if(r[i] == 0)
								Xk[i] = Xk[i] + 0.5*SR[i]*D[i];

						Xk = Corrections.correct( correction, Xk, bounds);
						fXk = prob.f(Xk);
						iter++; numEval++;

						if(fXk < fit)
						{
							fit = fXk;
							sol = Misc.cloneSolution(Xk);
						}
						if(fXk >= fit)
							Xk = Misc.cloneSolution(sol);
						else
							improve = true;
					}
					else
						improve = true;
				}
			}

			j++;
		}
		double[] out = {fit,numEval};
		return out;
	}

	public static double[] shrinking(double[] sol, double fit, double intermediateSizeinit, double CR,  Problem prob, int totalBudget, int iter, char correction) throws Exception
	{
		boolean solImproved=false;
		double intermediateSize = intermediateSizeinit;
		double[][] bounds = prob.getBounds();
		int problemDimension = prob.getDimension();
		double[] intermediateLSRadius = new double[problemDimension];
		double[] x = new double[problemDimension];

		int numEval = 0;
		double fx = fit;

		while (intermediateSize>0.0001 && iter < totalBudget) 
		{
			for (int k = 0; k < problemDimension; k++)
				intermediateLSRadius[k] = 0.5*(bounds[k][1]-bounds[k][0])*Math.pow(intermediateSize, 1/problemDimension);

			for (int k = 0; k < problemDimension && iter<totalBudget; k++)
			{   
				x = intermediatePerturbation(bounds, sol, intermediateLSRadius[k]);
				x = crossOverExp(sol, x, CR);
				x = Corrections.correct( correction, x, bounds);
				fx = prob.f(x);
				iter++; numEval++;

				// best update
				if (fx < fit)
				{
					fit = fx;
					for(int c=0; c<problemDimension ;c++)
						sol[c] = x[c];
					solImproved=true;
				}
			}				

			if(!solImproved) 
				intermediateSize = intermediateSize/2.0; 
			else
				solImproved=false;
		}
		double[] out = {fit, numEval};
		return out;
	}

	public static double[] shrinking_ri(double[] sol, double fit, double intermediateSizeinit, double F,  Problem prob, int totalBudget, int iter, char correction) throws Exception
	{
		boolean solImproved=false;
		double intermediateSize = intermediateSizeinit;
		double[][] bounds = prob.getBounds();
		int problemDimension = prob.getDimension();
		double[] intermediateLSRadius = new double[problemDimension];
		double[] xt = new double[problemDimension];
		double[] xr = new double[problemDimension];
		double[] xs = new double[problemDimension];
		double[] x = new double[problemDimension];

		int numEval = 0;
		double fx = fit;

		while (intermediateSize>0.0001 && iter < totalBudget) 
		{
			for (int k = 0; k < problemDimension; k++)
				intermediateLSRadius[k] = 0.5*(bounds[k][1]-bounds[k][0])*Math.pow(intermediateSize, 1/problemDimension);

			for (int k = 0; k < problemDimension && iter<totalBudget; k++)
			{   
				double K = RandUtils.random();
				//sampling
				for (int c = 0; c < problemDimension; c++)
				{
					xt[c] = sol[c] + intermediateLSRadius[c]*(-1+2*RandUtils.random());
					xr[c] = sol[c] + intermediateLSRadius[c]*(-1+2*RandUtils.random());
					xs[c] = sol[c] + intermediateLSRadius[c]*(-1+2*RandUtils.random());
				}
				//DE/current-to-rand/1 
				for(int c=0; c<problemDimension; c++)
					x[c]=sol[c]+K*(xt[c]-sol[c])+F*K*(xr[c]-xs[c]);

				x = Corrections.correct( correction, x, bounds);
				fx = prob.f(x);
				iter++; numEval++;

				// best update
				if (fx < fit)
				{
					fit = fx;
					for(int c=0; c<problemDimension ;c++)
						sol[c]=x[c];
					solImproved=true;
				}
			}				

			if(!solImproved) 
				intermediateSize = intermediateSize/2.0; 
			else
				solImproved=false;
		}
		double[] out = {fit, numEval};
		return out;
	}

	/** NUSA, Non Uniform Simulated Annealing **/
	/** standard settings:  B = 5, alpha = 0.9, Lk = 3,  **/
	public static double[] NUSA(double[] sol, double fit, int B, double alpha, int Lk, double fOld, double fWorst, 
			Problem prob, int localBudget, int totalBudget, int iter, char correction) throws Exception
	{
		double delt0 = fWorst - fOld;
		double accept0 = 0.9;
		double T0 = -delt0/Math.log(accept0);		
		double tk = T0;
		double[][] bounds = prob.getBounds();
		int dim = prob.getDimension();

		double[] newPt = new double[dim];
		double fNew;
		int numEval = 0;

		int generationIndex = 1;
		int totalGenerations = (totalBudget-numEval)/Lk;

		while (numEval < localBudget && iter < totalBudget)
		{
			for (int j = 0; j < Lk && iter < totalBudget && numEval < localBudget; j++)
			{
				// non-uniform mutation
				for (int k = 0; k < dim; k++)
				{
					//double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)i/maxEvaluations, B));
					double temp = Math.pow(RandUtils.random(), Math.pow(1.0-(double)generationIndex/totalGenerations, B));

					if (RandUtils.random()<0.5)
						newPt[k] = sol[k] - (sol[k]-bounds[k][0])*(1-temp);
					else
						newPt[k] = sol[k] + (bounds[k][1]-sol[k])*(1-temp);
				}

				fNew = prob.f(newPt);
				iter++; numEval++;

				// update best
				if (fNew < fit)
					fit = fNew;

				// move to the neighbor point
				if ((fNew <= fOld) || (Math.exp((fOld-fNew)/tk) > RandUtils.random()))
				{
					for (int k = 0; k < dim; k++)
						sol[k] = newPt[k];
					fOld = fNew;
				}
			}

			generationIndex++;

			// update temperature
			tk = alpha*tk;

		}
		double[] out = {fit, numEval};
		return out;
	}

	/** Evaluate fBest and fWorst to estimate the initial temperature interval to be used in NUSA  **/
	public static double[] initTemp(double[] sol, double fit, int localBudget, Problem prob) throws Exception
	{	
		double[][] bounds = prob.getBounds();
		int dim = prob.getDimension();
		double fWorst = fit;
		double[] newPt;
		double fNew;

		// evaluate initial solutions to set the initial temperature
		for (int j = 0; j < localBudget; j++)
		{
			newPt = Misc.generateRandomSolution(bounds, dim);
			fNew = prob.f(newPt);

			// update worst
			if (fNew <= fit)
			{
				for (int k = 0; k < dim; k++)
					sol[k] = newPt[k];
				fit = fNew;
			}
			else if(fNew > fWorst)
				fWorst = fNew;			
		}

		double[] out = {fit, fWorst};
		return out;
	}

	/** micro Differential Evolution with current-to-rand mutation strategy **/
	public static  double[] microDE(double[] sol, double fit, double[][] population, double[] fitnesses,double F, int localBudget, int totalBudget, 
			Problem prob, int iter, char correction) throws Exception
	{
		int dim = prob.getDimension();
		double populationSize = fitnesses.length;
		double numEval = 0;
		double[] currPt = new double[dim];
		double[] solution = new double[dim];
		double currFit = Double.NaN;
		double fSolution = Double.NaN;
		while ((numEval < localBudget) && (iter < totalBudget))
		{
			for (int j = 0; j < populationSize && iter < totalBudget && numEval < localBudget; j++)
			{
				for (int n = 0; n < dim; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				// mutation: DE/current-to-rand/1
				solution = currentToRand1(population, j, F);
				// crossover
				fSolution = prob.f(solution);
				iter++; numEval++;
				// best update
				if (fSolution < fit)
				{
					fit = fSolution;
					for (int n = 0; n < dim; n++)
						sol[n] = solution[n];
				}

				// replacement
				if (fSolution < currFit)
				{
					for (int n = 0; n < dim; n++)
						population[j][n] = solution[n];
					fitnesses[j] = fSolution;
				}
				else
				{
					for (int n = 0; n < dim; n++)
						population[j][n] = currPt[n];
					fitnesses[j] = currFit;
				}
			}
		}
		double[] out = {fit, numEval};
		return out;
	}
	
	/**Powell's algorithm
	 * Standard settings: ftol = 0.00001, BrentIterations fixed to 100.
	 * localBudget is local budget for meme, totalBudget is maximum FEs of optimization process, iter is current FEs used.
	 * 
	 * returns double[] out = {fBest, iter};
	 **/
    public static double[] Powell(double ftol, double[] sol, double fit, int localBudget, int totalBudget, Problem prob, int iter, char correction) throws Exception
    {       
        double TINY =  1.0e-25;
        double MIN_VECTOR_LENGTH = 1.0e-3;      
        double fret = fit, fBest = fit;
        double fptt;
        int ibig;
        double del;
        double fp;
        double t;
        int n = prob.getDimension();
        double[] best = new double[n];
        double[] pt = new double[n], ptt = new double[n], xit = new double[n];
        double[][] xi = MatLab.eye(n);
       
        boolean unitDirectionVectors = true;
        int iterations = 0;
        
        double[] p1dim = new double[n];
        double [] xi1dim = new double[n];
        
        while(iter < totalBudget && iterations < localBudget) {
            fp = fret;
            ibig = 0;
            del = 0.0;
            // minimize in all directions, p records the change
            for (int i=0; i<n; i++)
            {
                for (int j=0; j<n; j++)
                    xit[j] = xi[j][i];
                fptt = fret;
                
                double[] lineMinTemp = lineMinimization(p1dim, xi1dim, sol, xit, prob, iterations, localBudget, iter, totalBudget);
                iter = (int) lineMinTemp[0];
                iterations = (int) lineMinTemp[1];
                fret = lineMinTemp[2];
              
                if (fret < fBest)
                {
                    for (int j=0; j < n; j++)
                        best[j] = sol[j];
                    fBest = fret;
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
                ptt[j] = 2.0*sol[j]-pt[j];
                xit[j] = sol[j]-pt[j];
                pt[j] = sol[j];
            }
            fptt = fConstraint(ptt, prob);
            iter++; iterations++;
            if (fptt < fBest)
            {
                for (int j = 0; j < n; j++)
                    best[j] = ptt[j];
                fBest = fptt;
            }

            if (fptt<fp)
            {
                t = 2.0*(fp-2.0*fret+fptt)*Math.pow(fp-fret-del,2)-del*Math.pow(fp-fptt,2);
                if (t<0.0)
                {
                    double[] lineMinTemp2 = lineMinimization(p1dim, xi1dim, sol, xit, prob, iterations, localBudget, iter, totalBudget);
                    iter = (int) lineMinTemp2[0];
                    iterations = (int) lineMinTemp2[1];
                    fret = lineMinTemp2[2];
                    if (fret < fBest)
                        {
                            for (int j = 0; j < n; j++)
                                best[j] = sol[j];
                            fBest = fret;
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
                best[j] = sol[j];
            fBest = fret;
        }       
    double[] out = {fBest, iter};
    return out;
    }
    
    //**************************************************************************************************************
    
    /** ROSENBROCK METHOD **/
	/** standard parameters setting: eps =  10e-5, alpha = 2, beta 0.5 **/
	public static double[] RosenbrockShortTime(double[] sol, double fit, double eps, double alpha, double beta, 
			Problem problem, int totalBudget, int current_iter, int localBudget, FTrend FT, char correction) throws Exception
	{		
		int localFEs = 0;
		int iter = current_iter;
		int n = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[][] xi = MatLab.eye(n);
		double[][] A = MatLab.zeros(n);
		double[] lambda = new double[n];
		double[] xCurrent = new double[n];
		double[] t = new double[n];
		double[] xk = new double[n];
		double[] d = new double[n];
		double[] x = new double[n];
		for (int i=0; i<n; i++)
			x[i] = sol[i];
		
		double yFirstFirst = fit;
		double yFirst = yFirstFirst;
		double yBest = yFirst;
		double yCurrent;
		for (int i=0; i<n; i++)
		{
			d[i] = 0.1;
			xk[i] = x[i];
		}
	
		boolean restart = true;
		double mini; double div;
		int numEval = 0;
		do
		{
			yBest = yFirstFirst;
			do
			{
				yFirst = yBest;
				for (int i = 0; (i < n) && (iter < totalBudget) && (localFEs < localBudget);i++)
				{
					for (int j=0;j<n;j++)
						 xCurrent[j]= xk[j]+d[i]*xi[i][j];
					xCurrent = Corrections.correct( correction, xCurrent, bounds);
					yCurrent = problem.f(xCurrent);
					iter++;
					numEval++;
					localFEs++;

		            if (yCurrent < yBest)
		            {
		            	lambda[i] += d[i];
		            	d[i] *= alpha;
		            	yBest = yCurrent;
		            	FT.add(iter, yBest);
		            	for (int j=0;j<n;j++)
		            	{
		            		xk[j] = xCurrent[j];
		            		sol[j] = xCurrent[j];
		            	}
		            }
		            else
		            	d[i] *= -beta;
				}
//				System.out.println("i1: "+ iter);
			}
			while ((yBest < yFirst) && (iter < totalBudget) && (localFEs < localBudget));
			
			mini = MatLab.min(MatLab.abs(d));
			restart = mini>eps;
			
			if ((yBest < yFirstFirst) && (iter < totalBudget) && (localFEs < localBudget))
			{ 
				mini = MatLab.min(MatLab.abs(MatLab.subtract(xk,x)));
				restart = restart || (mini > eps);
				
				if (restart)
				{ 
					for (int i=0;i<n;i++)
						A[n-1][i] = lambda[n-1]*xi[n-1][i];
					for (int k=n-2; k>=0;k--)
					{
						for (int i=0;i<n;i++)
							A[k][i] = A[k+1][i] + lambda[i]*xi[k][i];
					}

					t[n-1] = lambda[n-1]*lambda[n-1];

					for (int i=n-2; i>=0;i--)
						t[i] = t[i+1] + lambda[i]*lambda[i];
					
					for (int i=n-1;i>0;i--)
					{
						div = Math.sqrt(t[i-1]*t[i]);
						if (div != 0)
							for (int j=0;j<n;j++)
								xi[i][j] = (lambda[i-1]*A[i][j]-xi[i-1][j]*t[i])/div;
					}
					div = Math.sqrt(t[0]);
					for (int i=0; i<n;i++)
					{
						if (div != 0)
							xi[0][i] = A[0][i]/div;	
						x[i] = xk[i];
						lambda[i] = 0;
						d[i] = 0.1;
					}
					yFirstFirst = yBest;
				}
			}
//				System.out.println("i2: "+ iter);
		}
		while (iter < totalBudget && (localFEs < localBudget) );
		
//		System.out.println("Rosen LOCAL FEs: " + localFEs);

		double[] out = {yBest, numEval};
		return out;
	}

	/** 3SOME's short distance searcher **/
	/** Standard settings: deepLSRadius = 0.4, deepLSSteps = 150 **/
	public static double[] ThreeSome_ShortDistanceShortTime(double[] sol, double fit, double deepLSRadius, int deepLSSteps, 
			Problem prob, int totalBudget, int current_iter, int localBudget, FTrend FT, char correction) throws Exception
	{
		int numEval = 0;
		int localFEs = 0;
		int iter = current_iter;
		int problemDimension = prob.getDimension();
		double[][] bounds = prob.getBounds();

		double[] SR = new double[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;

		boolean improve = true;
		int j = 0;
		while ((j < deepLSSteps) && (iter < totalBudget) && (localFEs < totalBudget))
		{	
			double[] Xk = new double[problemDimension];
			double[] Xk_orig = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
			{
				Xk[k] = sol[k];
				Xk_orig[k] = sol[k];
			}

			double fXk_orig = fit;

			
			if (!improve)
			{
				for (int k = 0; k < problemDimension; k++)
					SR[k] = SR[k]/2;
			}

			improve = false;
			int k = 0;
			while ((k < problemDimension) && (iter < totalBudget) && (localFEs < totalBudget))
			{
				Xk[k] = Xk[k] - SR[k];
				Xk = Corrections.correct( correction, Xk, bounds);
				double fXk = prob.f(Xk);
				iter++; numEval++; localFEs++;

				// best update
				if (fXk < fit)
				{
					fit = fXk;
					for (int n = 0; n < problemDimension; n++)
						sol[n] = Xk[n];
				}

				if (iter < totalBudget && (localFEs < totalBudget) )
				{
					if (fXk == fXk_orig)
					{
						for (int n = 0; n < problemDimension; n++)
							Xk[n] = Xk_orig[n];
					}
					else
					{
						if (fXk > fXk_orig)
						{
							Xk[k] = Xk_orig[k];
							Xk[k] = Xk[k] + 0.5*SR[k];
							Xk = Corrections.correct( correction, Xk, bounds);
							fXk = prob.f(Xk);
							iter++; numEval++; localFEs++;

							// best update
							if (fXk < fit)
							{
								fit = fXk;
								for (int n = 0; n < problemDimension; n++)
									sol[n] = Xk[n];
								FT.add(iter, fXk);
							}

							if (fXk >= fXk_orig)
								Xk[k] = Xk_orig[k];
							else
								improve = true;
						}
						else
							improve = true;
					}
				}

				k++;
			}

			j++;
		}

//		System.out.println("3some LOCAL FEs: " + localFEs);
		double[] out = {fit, numEval};
		return out;
	}
	
	
	
	//*******************************************************************************************
	//*************************** OLD STYLE FOR ILPO'S CODE**************************************
	//*******************************************************************************************
	
	/** 3SOME's short distance searcher **/
	/** Standard settings: deepLSRadius = 0.4, deepLSSteps = 150 **/
	public static double[] ThreeSome_ShortDistance(double[] sol, double fit, double deepLSRadius, int deepLSSteps, 
			Problem prob, int totalBudget, int iter, char correction) throws Exception
	{
		int numEval = 0;
		int problemDimension = prob.getDimension();
		double[][] bounds = prob.getBounds();

		double[] SR = new double[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;

		boolean improve = true;
		int j = 0;
		while ((j < deepLSSteps) && (iter < totalBudget))
		{	
			double[] Xk = new double[problemDimension];
			double[] Xk_orig = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
			{
				Xk[k] = sol[k];
				Xk_orig[k] = sol[k];
			}

			double fXk_orig = fit;

			
			if (!improve)
			{
				for (int k = 0; k < problemDimension; k++)
					SR[k] = SR[k]/2;
			}

			improve = false;
			int k = 0;
			while ((k < problemDimension) && (iter < totalBudget))
			{
				Xk[k] = Xk[k] - SR[k];
				Xk = Corrections.correct( correction, Xk, bounds);
				double fXk = prob.f(Xk);
				iter++; numEval++;

				// best update
				if (fXk < fit)
				{
					fit = fXk;
					for (int n = 0; n < problemDimension; n++)
						sol[n] = Xk[n];
				}

				if (iter < totalBudget)
				{
					if (fXk == fXk_orig)
					{
						for (int n = 0; n < problemDimension; n++)
							Xk[n] = Xk_orig[n];
					}
					else
					{
						if (fXk > fXk_orig)
						{
							Xk[k] = Xk_orig[k];
							Xk[k] = Xk[k] + 0.5*SR[k];
							Xk = Corrections.correct( correction, Xk, bounds);
							fXk = prob.f(Xk);
							iter++; numEval++;

							// best update
							if (fXk < fit)
							{
								fit = fXk;
								for (int n = 0; n < problemDimension; n++)
									sol[n] = Xk[n];
							}

							if (fXk >= fXk_orig)
								Xk[k] = Xk_orig[k];
							else
								improve = true;
						}
						else
							improve = true;
					}
				}

				k++;
			}

			j++;
		}

		double[] out = {fit, numEval};
		return out;
	}
	
	
	
	//*******************************************************************************************
	//*************************** OLD STYLE FOR ILPO'S CODE END**************************************
	//*******************************************************************************************

	/** 3SOME's short distance searcher with stop criterion **/
	/** Standard settings: deepLSRadius = 0.4, precision = 10^-6 **/
	public static double[] ThreeSome_ShortDistance(double[] sol, double fit, double deepLSRadius, double precision, 
			Problem prob, int totalBudget, int iter, char correction) throws Exception
	{
		int numEval = 0;
		int problemDimension = prob.getDimension();
		double[][] bounds = prob.getBounds();

		double[] SR = new double[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;

		boolean improve = true;

		//while ((SR[0] > precision) && (iter < totalBudget))
		while ((MatLab.max(SR) > precision) && (iter < totalBudget))
		{	
			double[] Xk = new double[problemDimension];
			double[] Xk_orig = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
			{
				Xk[k] = sol[k];
				Xk_orig[k] = sol[k];
			}

			double fXk_orig = fit;

			if (!improve)
			{
				for (int k = 0; k < problemDimension; k++)
					SR[k] = SR[k]/2;
			}

			improve = false;
			int k = 0;
			while ((k < problemDimension) && (iter < totalBudget))
			{
				Xk[k] = Xk[k] - SR[k];
				Xk = Corrections.correct( correction, Xk, bounds);
				double fXk = prob.f(Xk);
				iter++; numEval++;

				// best update
				if (fXk < fit)
				{
					fit = fXk;
					for (int n = 0; n < problemDimension; n++)
						sol[n] = Xk[n];
				}

				if (iter < totalBudget)
				{
					if (fXk == fXk_orig)
					{
						for (int n = 0; n < problemDimension; n++)
							Xk[n] = Xk_orig[n];
					}
					else
					{
						if (fXk > fXk_orig)
						{
							Xk[k] = Xk_orig[k];
							Xk[k] = Xk[k] + 0.5*SR[k];
							Xk = Corrections.correct( correction, Xk, bounds);
							fXk = prob.f(Xk);
							iter++; numEval++;

							// best update
							if (fXk < fit)
							{
								fit = fXk;
								for (int n = 0; n < problemDimension; n++)
									sol[n] = Xk[n];
							}

							if (fXk >= fXk_orig)
								Xk[k] = Xk_orig[k];
							else
								improve = true;
						}
						else
							improve = true;
					}
				}

				k++;
			}

		}

		double[] out = {fit, numEval};
		return out;
	}

	/** ROSENBROCK METHOD **/
	/** standard parameters setting: eps =  10e-5, alpha = 2, beta 0.5 **/
	public static double[] Rosenbrock(double[] sol, double fit, double eps, double alpha, double beta, 
			Problem problem, int totalBudget, int iter, char correction) throws Exception
	{		
		int n = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[][] xi = MatLab.eye(n);
		double[][] A = MatLab.zeros(n);
		double[] lambda = new double[n];
		double[] xCurrent = new double[n];
		double[] t = new double[n];
		double[] xk = new double[n];
		double[] d = new double[n];
		double[] x = new double[n];
		for (int i=0; i<n; i++)
			x[i] = sol[i];
		
		double yFirstFirst = fit;
		double yFirst = yFirstFirst;
		double yBest = yFirst;
		double yCurrent;
		for (int i=0; i<n; i++)
		{
			d[i] = 0.1;
			xk[i] = x[i];
		}
	
		boolean restart = true;
		double mini; double div;
		int numEval = 0;
		do
		{
			yBest = yFirstFirst;
			do
			{
				yFirst = yBest;
				for (int i = 0; (i < n) && (iter < totalBudget);i++)
				{
					for (int j=0;j<n;j++)
						 xCurrent[j]= xk[j]+d[i]*xi[i][j];
					xCurrent = Corrections.correct(correction, xCurrent, bounds);
					yCurrent = problem.f(xCurrent);
					iter++;
					numEval++;

		            if (yCurrent < yBest)
		            {
		            	lambda[i] += d[i];
		            	d[i] *= alpha;
		            	yBest = yCurrent;
		            	for (int j=0;j<n;j++)
		            	{
		            		xk[j] = xCurrent[j];
		            		sol[j] = xCurrent[j];
		            	}
		            }
		            else
		            	d[i] *= -beta;
				}
			}
			while ((yBest < yFirst) && (iter < totalBudget));
			
			mini = MatLab.min(MatLab.abs(d));
			restart = mini>eps;
			
			if ((yBest < yFirstFirst) && (iter < totalBudget))
			{ 
				mini = MatLab.min(MatLab.abs(MatLab.subtract(xk,x)));
				restart = restart || (mini > eps);
				
				if (restart)
				{ 
					for (int i=0;i<n;i++)
						A[n-1][i] = lambda[n-1]*xi[n-1][i];
					for (int k=n-2; k>=0;k--)
					{
						for (int i=0;i<n;i++)
							A[k][i] = A[k+1][i] + lambda[i]*xi[k][i];
					}

					t[n-1] = lambda[n-1]*lambda[n-1];

					for (int i=n-2; i>=0;i--)
						t[i] = t[i+1] + lambda[i]*lambda[i];
					
					for (int i=n-1;i>0;i--)
					{
						div = Math.sqrt(t[i-1]*t[i]);
						if (div != 0)
							for (int j=0;j<n;j++)
								xi[i][j] = (lambda[i-1]*A[i][j]-xi[i-1][j]*t[i])/div;
					}
					div = Math.sqrt(t[0]);
					for (int i=0; i<n;i++)
					{
						if (div != 0)
							xi[0][i] = A[0][i]/div;	
						x[i] = xk[i];
						lambda[i] = 0;
						d[i] = 0.1;
					}
					yFirstFirst = yBest;
				}
			}
		}
		while (iter < totalBudget);

		double[] out = {yBest, numEval};
		return out;
	}

	
	
	

}
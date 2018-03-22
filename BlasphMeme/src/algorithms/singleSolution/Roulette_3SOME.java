package algorithms.singleSolution;

import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;

import static utils.algorithms.Misc.saturate;
import static utils.algorithms.operators.MemesLibrary.intermediatePerturbation;



import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

public class Roulette_3SOME extends Algorithm {

	public static int GLOBAL = 0;
	public static int INTERMEDIATE = 1;
	public static int DEEP = 2;

	private class Counter {
		private int count[] = new int[] {0,0,0};
		private int success[] = new int[] {0,0,0};

		public void inc_count(int op) { count[op] ++; }

		public void inc_success(int op) { success[op] ++; }

		public double[] get_success_rate() {
			boolean zero = has_zero(count);

			double r[] = new double[3];
			for (int i = 0; i<count.length; i++) {
				r[i] = zero ? 1.0 : (double)success[i] / count[i];
			}
			return r;
		}
	}


	private int problemDimension; 
	private double[][] bounds;
	private Problem problem;
	// temp solution
	private double[] solution;
	private double fSolution;
	// current best
	private double[] best;
	private double fBest;

	private double globalSteps;
	private double globalCR;
	private double intermediateCR;
	private int intermediateLSSteps;

	private Counter c;
	private int nbEvaluations = 0;

	protected boolean globalSearch(int budget, double CR, FTrend ft) throws Exception {
		c = new Counter();
		int i = 0;
		int changed = 0;
		while (i < budget) {
			solution = generateRandomSolution(bounds, problemDimension);
			solution = crossOverExp(best, solution, CR);
			fSolution = problem.f(solution);
			i ++;
			nbEvaluations ++;
			c.inc_count(GLOBAL);

			// best update

			if (fSolution < fBest) {
				fBest = fSolution;
				best = solution;
				c.inc_success(GLOBAL);
				ft.add(nbEvaluations,fBest);
//				c.registerResults(currentBest);
				changed = 1;
				break;
			}
		}
		return changed > 0;
	}

	protected boolean intermediateSearch(int budget, double radius, double CR, FTrend ft) throws Exception {
		
		int i = 0;
		int changed = 0;
		while (i < budget) {
			solution = intermediatePerturbation(bounds, best, radius);
			solution = crossOverExp(best, solution, CR);
			solution = saturate(solution, bounds);
			fSolution = problem.f(solution);
			i++;
			nbEvaluations ++;
			c.inc_count(INTERMEDIATE);

			// best update
			if (fSolution < fBest) {
				fBest = fSolution;
				best = solution.clone();
				c.inc_success(INTERMEDIATE);
				ft.add(nbEvaluations,fBest);
				//c.registerResults(currentBest);
				changed ++;
			}
		}
		return changed > 0;
	}

	protected boolean deepSearch(int budget, double radius, FTrend ft) throws Exception {
		
		int changed = 0;
		int i = 0;
		while (i < budget) {

			double[] Xs = (double [])best.clone();
			double fXs = fBest;

			double[] Xt = (double [])best.clone();
			double fXt = fBest;

			double[] rho = new double[problemDimension];
			for (int k= 0; k < problemDimension; k++)
				rho[k] = radius * (bounds[k][1] - bounds[k][0]);
			for (int k= 0; k < problemDimension; k++) {
				if (i == budget) break;
				Xs[k] = best[k] - rho[k];
				Xs = saturate(Xs, bounds);
				fXs = problem.f(Xs);
				i++;
				nbEvaluations ++;
				c.inc_count(DEEP);

				if (fXs <= fXt) {
					System.arraycopy(Xs, 0, Xt, 0, problemDimension); // Xs into Xt
					fXt = fXs;
					c.inc_success(DEEP);
				}
				else {
					if (i == budget) break;
					Xs[k] = best[k] + rho[k] / 2;
					Xs = saturate(Xs, bounds);
					fXs = problem.f(Xs);
					i++;
					nbEvaluations ++;
					c.inc_count(DEEP);

					if (fXs <= fXt) {
						System.arraycopy(Xs, 0, Xt, 0, problemDimension); // Xs into Xt
						fXt = fXs;
						c.inc_success(DEEP);
					}  
				}
			}
			// best update
			if (fXt < fBest) {
				System.arraycopy(Xt, 0, best, 0, problemDimension); // Xt into best
				fBest = fXt;
				ft.add(nbEvaluations,fBest);
				changed ++;
			}
			else {
				radius /= 2;
			}
		}
		return changed > 0;
	}

	protected boolean deepSearchGiovanni(int budget, double radius, FTrend ft) throws Exception {
		
		int i = 0;
		boolean bestChanged = false;

		double[] SR = new double[problemDimension];
		for (int k = 0; k < problemDimension; k++)
			SR[k] = (bounds[k][1] - bounds[k][0]) * radius;

		boolean improve = true;
		//printVec("start", best, fBest);
		while (i < budget) {

			double[] Xk = new double[problemDimension];
			double[] Xk_orig = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++) {
				Xk[k] = best[k];
				Xk_orig[k] = best[k];
			}

			double fXk_orig = fBest;
			

			if (!improve) {
				for (int k = 0; k < problemDimension; k++)
					SR[k] = SR[k]/2;
			}

			improve = false;
			int k = 0;
			while ((k < problemDimension) && (i < budget)) {
				Xk[k] = Xk[k] - SR[k];
				Xk = saturate(Xk, bounds);
				double fXk = problem.f(Xk);
			
				c.inc_count(DEEP);
				i++;
				nbEvaluations ++;

				// best update
				if (fXk < fBest) { 
					fBest = fXk;
					for (int n = 0; n < problemDimension; n++)
						best[n] = Xk[n];
					
					c.inc_success(DEEP);
					ft.add(nbEvaluations,fBest);

					bestChanged = true;
				}

				if (i < budget) {
					if (fXk == fXk_orig) {
						for (int n = 0; n < problemDimension; n++)
							Xk[n] = Xk_orig[n];
					}
					else {
						if (fXk > fXk_orig) {
							Xk[k] = Xk_orig[k];
							Xk[k] = Xk[k] + 0.5*SR[k];
							Xk = saturate(Xk, bounds);
							fXk = problem.f(Xk);
							c.inc_count(DEEP);
							i++;
							nbEvaluations ++;

							// best update
							if (fXk < fBest) { 
								fBest = fXk;
								for (int n = 0; n < problemDimension; n++)
									best[n] = Xk[n];
								c.inc_success(DEEP);
								ft.add(nbEvaluations,fBest);
								bestChanged = true;
							}

							if (fXk >= fXk_orig) {
								Xk[k] = Xk_orig[k];
							
							}
							else { 
								improve = true;
							}
						}
						else { 
							improve = true;
						}
					}
				}

				k++;
			}
		}

		return bestChanged;
	}

	protected int roulette(double values[]) {
		double s = sum(values);


		double r = RandUtils.random() * s;
		double thresh = 0.0;
		for (int i = 0; i < values.length-1; i++) {
			thresh += values[i];
			if (r <= thresh) 
				return i;
		}
		return values.length-1;
	}

	private boolean has_zero(int a[]) {
		for (int e: a) {
			if (e == 0)
				return true;
		}
		return false;
	}

	private double sum(double a[]) {
		double s = 0.0;
		for (double e: a)
			s += e;
		return s;
	}

	private int min(int a, int b) {
		return a < b ? a : b;
	}

	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception {
		FTrend FT = new FTrend();
		double globalAlpha = getParameter("p0").doubleValue(); //0.95
		double intermediateAlpha = 1-globalAlpha;
		int intermediateLSStepsFactor = getParameter("p1").intValue(); //4 
		double intermediateLSRadius = getParameter("p2").doubleValue(); //0.1
		int deepLSSteps = getParameter("p3").intValue(); //150;
		double deepLSRadius = getParameter("p4").doubleValue();//0.4;

		this.problem = problem;
		problemDimension = problem.getDimension(); 
		bounds = problem.getBounds();

		solution = new double[problemDimension];
		best = new double[problemDimension];

		solution = generateRandomSolution(bounds, problemDimension);
		fSolution = problem.f(solution);
		nbEvaluations ++;

		best = solution;
		fBest = fSolution;
		FT.add(0,fBest);

		globalSteps = problemDimension*Math.log(problemDimension);
		globalCR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
		intermediateCR = Math.pow(0.5, (1/(problemDimension*intermediateAlpha)));
		intermediateLSSteps = intermediateLSStepsFactor * problemDimension;

		while (nbEvaluations < maxEvaluations) {
			int r = roulette(c.get_success_rate());
			switch (r) {
			case 0: globalSearch(      min((int)globalSteps,                  maxEvaluations - nbEvaluations), globalCR, FT);break;
			case 1: intermediateSearch(min((int)intermediateLSSteps,          maxEvaluations - nbEvaluations), intermediateLSRadius, intermediateCR, FT);break;
			case 2: deepSearchGiovanni(min((int)deepLSSteps*problemDimension, maxEvaluations - nbEvaluations), deepLSRadius, FT); break;
			}
		}
		
		finalBest = best;
		
		FT.add(nbEvaluations,fBest);
		return FT;
	}
}

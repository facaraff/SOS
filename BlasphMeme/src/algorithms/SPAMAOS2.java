package algorithms;


import static algorithms.utils.AlgorithmUtils.cloneSolution;
import static algorithms.utils.AlgorithmUtils.crossOverExp;
import static algorithms.utils.AlgorithmUtils.generateRandomSolution;
import static algorithms.utils.AlgorithmUtils.saturateToro;
//import static algorithms.utils.MemesLibrary.ThreeSome_ShortDistance;
import static algorithms.utils.MemesLibrary.ThreeSome_ShortDistanceShortTime;
//import static algorithms.utils.MemesLibrary.Rosenbrock;
import static algorithms.utils.MemesLibrary.RosenbrockShortTime;



import java.util.Vector;


import utils.random.RandUtils;
import algorithms.aos.AdaptPursuit;
import algorithms.aos.MultinomialTracking;
import algorithms.aos.OperatorSelection;
import algorithms.aos.ProbMatching;
import algorithms.aos.Uniform;
import algorithms.cmaes.CMAEvolutionStrategy;
import algorithms.interfaces.Algorithm;
import algorithms.interfaces.Problem;
import algorithms.utils.Best;

public class SPAMAOS2 extends Algorithm
{
	@SuppressWarnings("unused")
	@Override
	public Vector<Best> execute(Problem problem, int maxEvaluations) throws Exception
	{
		//System.out.println("budget "+maxEvaluations);	
		Vector<Best> bests = new Vector<Best>();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		double[] best = new double[problemDimension];
		if (initialSolution != null)
			best = initialSolution;
		else
			best = generateRandomSolution(bounds, problemDimension);
		double fBest = Double.NaN;

		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		//cma.parameters.setPopulationSize(10);
		cma.setRepairMatrix(true);				// repair ill-conditioned matrix
		cma.setDimension(problemDimension);		// overwrite some loaded properties
		cma.setInitialX(best);					// in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.2);	// also a mandatory setting 
		cma.options.verbosity = -1;
		cma.options.writeDisplayToFile = -1;
		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();


		// iteration loop
		int j = 0;
		int localBudget = (int)(maxEvaluations*0.2);
		while (j < localBudget)
		{
			// --- core iteration step ---
			// get a new population of solutions
			double[][] pop = cma.samplePopulation();

			for(int i = 0; i < pop.length && j < localBudget; ++i)
			{ 
				// saturate solution inside bounds 
				pop[i] = saturateToro(pop[i], bounds);

				// compute fitness/objective value	
				fitness[i] = problem.f(pop[i]);

				// save best
				if (j == 0 || fitness[i] < fBest)
				{
					fBest = fitness[i];
					for (int n = 0; n < problemDimension; n++)
						best[n] = pop[i][n];
					bests.add(new Best(j, fBest));
				}

				j++;
			}

			// pass fitness array to update search distribution
			cma.updateDistribution(fitness);
		}


		//System.out.println(cma.getDataC());
		double[][] cov = cma.getRho(); cma = null;

		//******************MODIFY THIS PART*************************
		int c, C = 0;
		for(c=0; c<problemDimension; c++)
			for(C=0; C<c; C++)
				if(Math.abs(cov[c][C]) < 0.2)
					cov[C][c] = 0.0;
				else if(Math.abs(cov[c][C]) >= 0.2 && Math.abs(cov[c][C]) < 0.4)
					cov[C][c] = 0.3;
				else if(Math.abs(cov[c][C]) >= 0.4 && Math.abs(cov[c][C]) < 0.6)
					cov[C][c] = 0.5;
				else if(Math.abs(cov[c][C]) >= 0.6 && Math.abs(cov[c][C]) < 0.8)
					cov[C][c] = 0.7;
				else 
					cov[C][c] = 1;	


		double ci = 0;
		for(c=0; c<problemDimension; c++)
			for(C=0; C<c; C++)
				ci += Math.abs(cov[C][c]);

		ci /= (Math.pow(problemDimension, 2) - problemDimension)/2;
		ci = (double)Math.round(ci*10)/10.0;		
		double prob = 2*ci;
		if(prob > 1)
			prob = 1;

		//      ***************************************************

		double globalAlpha = pullParameter("p0").doubleValue(); //0.5
		double deepLSRadius = pullParameter("p1").doubleValue(); //0.4
		int steps = pullParameter("p2").intValue(); //150
		double alpha = pullParameter("p3").doubleValue(); //2
		double beta = pullParameter("p4").doubleValue(); //0.5
		double eps = pullParameter("p5").doubleValue(); //0.00001
		int strategy = pullParameter("p6").intValue(); //{1,2,3,4}

		//		int strategy = 1;
		//		strategy = pullParameter("p6").intValue(); //{1,2,3,4}

		// One more parameter
		int maximumLocalBudget = 1000;
		OperatorSelection AOS = SelectModel(strategy);
		int selection = -1;

		//		double globalAlpha = 0.5;
		//		double deepLSRadius = 0.4;
		//		int steps = 150;
		//		double alpha = 2;
		//		double beta = 0.5;
		//		double eps = 0.00001;

		double CR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));

		double[] temp;

		double[] x = cloneSolution(best);
		double fx = fBest;
		boolean improved = true;

		while (j < maxEvaluations)
		{
			//			System.out.println("j:" + j +" of: "+ maxEvaluations + " fb: "+ fBest);
			if(!improved)
			{
				x = generateRandomSolution(bounds, problemDimension);
				x =  crossOverExp(best, x, CR);
				fx = problem.f(x);
				if(fx < fBest)
				{
					best = cloneSolution(x);
					fBest =fx;
					bests.add(new Best(j, fBest));
				}
				improved = true;
			}
			//			prob = 0.5;
			//			System.out.println("Prob: " + prob);

			selection = AOS.SelectOperator();
			//				System.out.println("Selection: " + selection + " p1: " + AOS.getProbability(0) +
			//						" p2: " + AOS.getProbability(1) );
			if (selection == 0)
			{
				/** 3SOME's local searcher with stop criterion **/
				temp = ThreeSome_ShortDistanceShortTime(x, fx, deepLSRadius, steps, problem, maxEvaluations, j, maximumLocalBudget);	
			}
			else
			{
				/** standard parameters setting: eps =  10e-5, alpha = 2, beta 0.5 **/
				temp = RosenbrockShortTime(x, fx, eps, alpha, beta,  problem, maxEvaluations,j, maximumLocalBudget);
			}
			double fold = fx, fnew = temp[0];
			AOS.credit[selection].addFitnessImprovM(selection, fold, fnew);
			double reward = AOS.ApplyReward(selection);

			if((fx - temp[0]) == 0) improved = false;
			fx = temp[0];
			j += temp[1];
			if(fx < fBest)
			{
				best = cloneSolution(x);
				fBest = fx;
				bests.add(new Best(j, fBest));
			}
		}


		finalBest = best;

		bests.add(new Best(j, fBest));

		return bests;
	}

	@SuppressWarnings("unused")
	protected OperatorSelection SelectModel(int idx)
	{
		int numberOfArms = 2;
		double pmin = 0.05;
		double alphaAOS = 0.8;
		double betaAOS = 0.8;	
		double v_scaling = 0.5;
		double v_gamma = 0.5; //DMAB
		double lambda = 0.99;

		OperatorSelection AOS = null;
		if (idx == 1) {
			AOS = new ProbMatching(numberOfArms, pmin, alphaAOS, RandUtils.getSeed());
		} else if (idx == 2) {
			AOS = new AdaptPursuit(numberOfArms, pmin, alphaAOS, betaAOS, RandUtils.getSeed());
		} else if (idx == 3) {
			AOS = new MultinomialTracking(numberOfArms, pmin, alphaAOS, RandUtils.getSeed(), lambda);
		} else {
			AOS = new Uniform(numberOfArms, RandUtils.getSeed());
		}

		int windowSize = 50, windowType = 1;//0-extreme; 1-average; 2-instantaneous
		boolean windowNorm = false; //if normalized or not
		double windowDecay = 0.5;//1.0;//0.5;
		AOS.InitCreditAssign(windowSize, windowType, windowNorm, windowDecay);

		return AOS;
	}
}
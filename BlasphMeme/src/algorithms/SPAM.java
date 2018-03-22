package algorithms;

import static utils.algorithms.Misc.cloneArray;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import static utils.algorithms.operators.MemesLibrary.ThreeSome_ShortDistance;
import static utils.algorithms.operators.MemesLibrary.Rosenbrock;
import utils.random.RandUtils;
import utils.algorithms.cmaes.CMAEvolutionStrategy;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

public class SPAM extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		//System.out.println("budget "+maxEvaluations);	
		FTrend FT = new FTrend();
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
				pop[i] = toro(pop[i], bounds);
				
				// compute fitness/objective value	
				fitness[i] = problem.f(pop[i]);
				
				// save best
				if (j == 0 || fitness[i] < fBest)
				{
					fBest = fitness[i];
					for (int n = 0; n < problemDimension; n++)
						best[n] = pop[i][n];
					FT.add(j, fBest);
				}
				
				j++;
			}

			// pass fitness array to update search distribution
			cma.updateDistribution(fitness);
		}
				
		
		//System.out.println(cma.getDataC());
		double[][] cov = cma.getRho(); cma = null;
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
		
//		System.out.println("---");
//		for(c=0; c<problemDimension; c++){
//			for(C=0; C<problemDimension; C++)
//				System.out.print((cov[C][c])+ "\t");
//			System.out.println();
//		}
		
//		System.out.println("---");
		double ci = 0;
		for(c=0; c<problemDimension; c++)
			for(C=0; C<c; C++)
//			{
				ci += Math.abs(cov[C][c]);
//				System.out.println(cov[C][c]); 
//			}

		ci /= (Math.pow(problemDimension, 2) - problemDimension)/2;
//		System.out.println("");
//		System.out.println("Separability coefficient "+ci);
		ci = (double)Math.round(ci*10)/10.0;
//		System.out.println("round "+ci);
//		System.out.println("");
		

		
		
		double prob = 2*ci;
		if(prob > 1)
			prob = 1;
		 			
		double globalAlpha = getParameter("p0").doubleValue(); //0.5
		double deepLSRadius = getParameter("p1").doubleValue(); //0.4
		int steps = getParameter("p2").intValue(); //150
		double alpha = getParameter("p3").doubleValue(); //2
		double beta = getParameter("p4").doubleValue(); //0.5
		double eps = getParameter("p5").doubleValue(); //0.00001
		
		
		double CR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));

		double[] temp;
		
		
		double[] x = cloneArray(best);
		double fx = fBest;
		boolean improved = true;
		
		while (j < maxEvaluations)
		{
			
			if(!improved)
			{
				 x = generateRandomSolution(bounds, problemDimension);
				 x =  crossOverExp(best, x, CR);
				 fx = problem.f(x);
				 if(fx < fBest)
					{
						best = cloneArray(x);
						fBest =fx;
						FT.add(j, fBest);
					}
				 improved = true;
			}
			
			
			if (RandUtils.random() > prob)
			{
				/** 3SOME's local searcher with stop criterion **/
				temp = ThreeSome_ShortDistance(x, fx, deepLSRadius, steps, problem, maxEvaluations, j, FT);	
			}
			else
			{
				/** standard parameters setting: eps =  10e-5, alpha = 2, beta 0.5 **/
				temp = Rosenbrock(x, fx, eps, alpha, beta,  problem, maxEvaluations,j, FT);
			}
				
			if((fx - temp[0]) == 0) improved = false;
			fx = temp[0];
			j += temp[1];
			if(fx < fBest)
			{
				best = cloneArray(x);
				fBest = fx;
				FT.add(j, fBest);
			}
		}
		
		finalBest = best;

		FT.add(j, fBest);
		
		return FT;
	}
}



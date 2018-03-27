package algorithms.abstractAlgorithms;

import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;

import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Three Stage Optimal Memetic Exploration 
 */
public abstract class LongDistanceExploration extends Algorithm
{
	private boolean improved = false;
	private double accuracy = Double.NaN;
	
	protected void setImproved(boolean improved) {this.improved = improved;}
	protected boolean getImproved() {return this.improved;}
	
	protected void setAccuracy(double accuracy) {this.accuracy = accuracy;}
	protected boolean getAccuracy() {return this.improved;}
	
	protected abstract boolean stopCondition(int i, int budget, boolean b, double fx, double accuracy);
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double globalAlpha = getParameter("p0").doubleValue(); //0.95
		

		FTrend FT =new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// temp solution
		double[] solution = new double[problemDimension];
		double fSolution;
		// current best
		double[] best = new double[problemDimension];
		double fBest;

		int i=0;
		if (initialSolution != null)
		{
			solution = initialSolution;
			fSolution = initialFitness;
		}
		else
		{
			solution = generateRandomSolution(bounds, problemDimension);		
			fSolution = problem.f(solution);
			i++;
		}

		best = solution;
		fBest = fSolution;
		FT.add(i, fBest);

		double globalCR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
		
		
		while (stopCondition(i, maxEvaluations, improved, fSolution, accuracy))
		{
			solution = generateRandomSolution(bounds, problemDimension);
			solution = crossOverExp(best, solution, globalCR);
			fSolution = problem.f(solution);
			i++;

			
			// best update
			if (fSolution < fBest)
			{
				fBest = fSolution;
				best = solution;
				setImproved(true);

			}
			if(i%problemDimension == 0)
				FT.add(i,fBest);	
		}

		finalBest = best;
		
		FT.add(i,fBest);

		return FT;
	}
	
}










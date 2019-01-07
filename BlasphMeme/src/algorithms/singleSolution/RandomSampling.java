package algorithms.singleSolution;

import static utils.algorithms.Misc.generateRandomSolution;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Random sampling
 */
public class RandomSampling extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		
		FTrend FT =new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// temp solution
		double[] solution = new double[problemDimension];
		double fSolution = Double.NaN;
		// current best
		double[] best = new double[problemDimension];
		double fBest;

		int i = 0;
		
		if (initialSolution != null)
			solution = initialSolution;
		else
		{
			solution = generateRandomSolution(bounds, problemDimension);		
			fSolution = problem.f(solution);
			i++;
		}
		best = solution;
		fBest = fSolution;
		FT.add(i, fBest);

	
		while(i<=maxEvaluations)
		{
			solution = generateRandomSolution(bounds, problemDimension);
			fSolution = problem.f(solution);

			// best update
			if (fSolution < fBest)
			{
				fBest = fSolution;
				best = solution;
				FT.add(i,fBest);
			}
			
		}

		finalBest = best;

		FT.add(i,fBest);
		NumberFormat decimalFormat = DecimalFormat.getInstance();
		decimalFormat.setMaximumFractionDigits(2);
		decimalFormat.setMinimumFractionDigits(2);


		return FT;
	}
}
package algorithms.specialOptions.MoAlgs;

//import static utils.algorithms.operators.DEOp.crossOverBin;
//import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.Misc.toro;
//import utils.algorithms.Modality.TestModality.Basins;
import utils.algorithms.Modality.TestModality;
import static utils.MatLab.min;

import java.util.Arrays;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
/*
 * MoTest
 */
public class MoTestAlg extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = this.getParameter("p0").intValue();//50

		int modalityPopulation = this.getParameter("p1").intValue();
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 1;
		
		//***MODALITY STUFF STARTS****
		
		//Evaluate function modality
		int modality = -1;
		int modalityEvaluations = maxEvaluations/3; //33%
		modalityEvaluations = min(modalityEvaluations, maxEvaluations-i);
		int repeatsSaved = 0;
		TestModality TM = new TestModality();
		TM.basinEstimate(problem, modalityPopulation, repeatsSaved, 2, modalityEvaluations);
		i = i + modalityEvaluations;
		modality = TM.getModality();
		System.out.println("modality = "+modality);
		
		
		
		


		finalBest = best;
		
		FT.add(i, fBest);
		
		return FT;
	}
}
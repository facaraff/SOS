package utils.algorithms.operators;

import static utils.algorithms.CompactAlgorithms.generateIndividual;
import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.operators.DEOp.crossOverExpFast;
import static utils.algorithms.operators.DEOp.currentToBest1;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.operators.DEOp.rand2;
import static utils.algorithms.operators.DEOp.randToBest2;

import utils.random.RandUtils;

public class CompactPerturbations {

	
	public static double[] cdelight(double F, double alpha, double[] best, double[] mean, double[] sigma2) 
	{
		int problemDimension = best.length;
		double CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
		double Fmod = (1+2*Math.pow(F,2));
		double[] sigma2_F = new double[problemDimension];
		for (int n = 0; n < problemDimension; n++)
			sigma2_F[n] = Fmod*sigma2[n];	
		double[] b = generateIndividual(mean, sigma2_F);
		return crossOverExpFast(best, b, CR);
	}
	
	public static double[] rand_one_randomF(double alpha, int xo_strat, double[] best, double[] mean, double[] sigma2)
	{
		 // DE/rand/1-Random-Scale-Factor
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double F = 0.5*(1+RandUtils.random());
		double[] b = rand1(xr, xs, xt, F);
		return binOrExpXO( best.length, F, alpha, b, best, xo_strat);
	}
	
	public static double[] rand_one(double F, double alpha, int xo_strat, double[] best, double[] mean, double[] sigma2)
	{
	
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double[] b = rand1(xr, xs, xt, F);
		return binOrExpXO( best.length, F, alpha,  b, best, xo_strat);
	}
	
	public static double[] rand_to_best(double F, double alpha, int problemDimension, int xo_strat, double[] best, double[] mean, double[] sigma2)
	{
	
		// DE/current(rand)-to-best/1
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double[] b = currentToBest1(xt, xr, xs, best, F);
		return binOrExpXO(problemDimension, F, alpha,  b, best, xo_strat);
	}
	
	public static double[] rand_two(double F, double alpha, int xo_strat, double[] best, double[] mean, double[] sigma2)
	{	
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double[] xu = generateIndividual(mean, sigma2);
		double[] xv = generateIndividual(mean, sigma2);
		double[] b = rand2(xr, xs, xt, xu, xv, F);
		return binOrExpXO( best.length, F, alpha,  b, best, xo_strat);
	}
	
	public static double[] rand_to_best_two(double F, double alpha, int xo_strat, double[] best, double[] mean, double[] sigma2)
	{	
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double[] xu = generateIndividual(mean, sigma2);
		double[] xv = generateIndividual(mean, sigma2);
		double[] b = randToBest2(xr, xs, xt, xu, xv, best, F);
		return binOrExpXO( best.length, F, alpha,  b, best, xo_strat);
	}
	
	
	public static double[] binOrExpXO(int problemDimension, double F, double alpha, double[] b, double[] best, int strategy)
	{
		double CR = 1.0/Math.pow(2.0,1.0/(problemDimension*alpha));
		if (strategy == 1)
			b = crossOverBin(best, b, CR);
		else if (strategy == 2)
			b = crossOverExp(best, b, CR);
		else
			
			System.out.println("Andate affanculo tutti quanti porcodio");
		return b;
	}
	

	
	
	
	
}

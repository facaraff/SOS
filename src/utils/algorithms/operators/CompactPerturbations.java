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

	//compact Differential Evolution
	
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
	
	public static double[] rand_to_best(double F, double alpha, int xo_strat, double[] best, double[] mean, double[] sigma2)
	{
		// DE/current(rand)-to-best/1
		double[] xr = generateIndividual(mean, sigma2);
		double[] xs = generateIndividual(mean, sigma2);
		double[] xt = generateIndividual(mean, sigma2);
		double[] b = currentToBest1(xt, xr, xs, best, F);
		return binOrExpXO(best.length, F, alpha,  b, best, xo_strat);
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
	

	
	//other compact algorithhms
	
	//cGA
	public static double[] cGArealPerturbation(double[] mean, double[] sigma2){return generateIndividual(mean, sigma2);}
	
	//cPSO
	public static double[] cPSOPerturbation(double[] v, double[] x, double[] x_gb, double[] mean, double[] sigma2, double phi1, double phi2, double phi3, double gamma1, double gamma2) 
	{	
		double[] x_lb = generateIndividual(mean, sigma2);
		for (int n = 0; n < v.length; n++)
		{
			v[n] = phi1*v[n]+phi2*RandUtils.random()*(x_lb[n]-x[n])+phi3*RandUtils.random()*(x_gb[n]-x[n]);
			x[n] = gamma1*x[n] + gamma2*v[n];
		}
		return x_lb; //return x_lb because x is passed by argument... then both of them have to be evualated and the best one used.
	}
	
}

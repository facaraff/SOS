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
package algorithms;

import static utils.algorithms.operators.DEOp.crossOverBin;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import static utils.algorithms.Misc.fix;
import static utils.MatLab.indexMin;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

public class MDE_pBX extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue(); //100
		double q = getParameter("p1").doubleValue(); //15% (of the population size)
		double CRm = getParameter("p2").doubleValue(); 
		double Fm = getParameter("p3").doubleValue(); 
		double N = getParameter("p4").doubleValue(); 
		
//		double CRm = 0.6;
//		double Fm = 0.5;
//		double N = 1.5;
		
		int groupSize = (int)(populationSize*q);
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		int G = 1;
		int Gmax = maxEvaluations%populationSize;
		if(Gmax == 0)
			Gmax = maxEvaluations/populationSize;
		else
			Gmax = maxEvaluations/populationSize + 1;
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			double[] tmp;
			if (j == 0 && initialSolution != null)
				tmp = initialSolution;
			else
				tmp = generateRandomSolution(bounds, problemDimension);
			
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = problem.f(population[j]);
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
					//FT.add(i, fBest));
			}
			if(i%problemDimension==0)
				FT.add(i, fBest);
			i++;
		}

		// temp variables
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double[] temp;
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		
		// iterate
		while (i < maxEvaluations)
		{
			double FmMeanPow = 0;
			double CrMeanPow = 0;
			int counter = 0;
			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				if(i%problemDimension==0)
					FT.add(i, fBest);
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				//MUTATION
				temp = new double[populationSize];
				for(int n=0; n<populationSize; n++)
					temp[n] = n; //vector of indexes
				int c = 1; 
				int r = RandUtils.randomInteger(populationSize-c); 
				int I = (int)temp[r]; int indMin = I;
				temp = removeElementAtIndex(r,temp); c++;
				double[] XgrBest = population[I];
				double grMin = fitnesses[I];  
				for(int n=0; n<groupSize-1; n++) 
				{
					r = RandUtils.randomInteger(populationSize-c);
					I = (int)temp[r];
					temp = removeElementAtIndex(r,temp); c++;
					if(fitnesses[I] < grMin)
					{
						grMin = fitnesses[I];
						XgrBest = population[I]; 
						indMin = I;
					}
					
				}
				
				/*int r1 = randInteger(populationSize-1);
				int r2 = randInteger(populationSize-1);
				boolean exit = (r1 != j) & (r1 != indMin) & (r1 != r2) & (r2 != j) & (r2 != indMin);
				while(!exit)
				{
					r1 = randInteger(populationSize-1);
					r2 = randInteger(populationSize-1);
					exit = (r1 != j) & (r1 != I) & (r1 != r2) & (r2 != j) & (r2 != indMin);
				}*/
				temp = new double[populationSize];
				for(int n=0; n<populationSize; n++)
					temp[n] = n; //vector of indexes
				temp = removeElementAtIndex(j,temp);
				temp = removeElementAtIndex(indMin,temp);
				r = RandUtils.randomInteger(populationSize-3);
				double[] Xr1 = population[(int)temp[r]];
				temp = removeElementAtIndex(r,temp);				
				r = RandUtils.randomInteger(populationSize-4);
				double[] Xr2 = population[(int)temp[r]];
				double F = generateF(Fm);
				
				newPt = currentToGrBest( XgrBest, currPt, Xr1, Xr2,  F);
						
				// CROSSOVER
				int P = (int)Math.ceil( (populationSize/2)*(1 - G/Gmax) );
				int Pindex = RandUtils.randomInteger(P-1);
				temp = new double[populationSize];
				for(int n=0; n<populationSize;n++)
					temp[n] = fitnesses[n];                 
				double[] pBest = null;
				for(int n=0; n<=Pindex;n++)
				{
					int min = indexMin(temp);
					pBest = population[min];
					temp = removeElementAtIndex(min, temp);
				}	
				double CR =  generateCR(CRm);
				crossPt = crossOverBin(pBest, newPt, CR);			
				
				//saturation
				crossPt = toro(crossPt, bounds);
				
				crossFit = problem.f(crossPt);
				i++;

				// best update
				if (crossFit < fBest)
				{
					fBest = crossFit;
					for (int n = 0; n < problemDimension; n++)
						best[n] = crossPt[n];
					//FT.add(i, fBest));
				}
				

				// replacement
				if (crossFit < currFit)
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = crossPt[n];
					fitnesses[j] = crossFit;
					//Mean_pow evaluation (Fsuccesfful and CRsuccessful sets)
					counter++;
					FmMeanPow += Math.pow( Math.pow(F, N)/counter , 1/N);
					CrMeanPow += Math.pow( Math.pow(CR, N)/counter , 1/N);
					
				}
				else // di troppo?
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = currPt[n];
					fitnesses[j] = currFit;
				}
			}
			G++;
			//Fm update
			double Wf = 0.9 + 0.2*RandUtils.random();
			Fm = Wf*Fm +(1 - Wf)*FmMeanPow;
			//Cr update
			double Wc = 0.9 + 0.1*RandUtils.random();
			CRm = Wc*CRm +(1 - Wc)*CrMeanPow;
		}

		finalBest = best;
		
		FT.add(i, fBest);
		
		return FT;
	}	
	
	private double[] currentToGrBest(double[] X_gr_best, double[] X_target, double[] X_r1, double[] X_r2, double F)
	{	
		int dim = X_target.length;
		double[] v = new double[dim];
		for(int i=0; i<dim; i++)
			v[i] = X_target[i] + F*(X_gr_best[i] - X_target[i] + X_r1[i] - X_r2[i]);
		
		return v;
	}
	
	private double[] removeElementAtIndex(int ind, double[] array)
	{
		double[] newArray = new double[array.length - 1];
		for(int n=0; n<newArray.length; n++)
		{
		if(n<ind)
			newArray[n] = array[n];
		else
			newArray[n] = array[n+1];
		}
		return newArray;
	}
	
	private double generateCR(double mean)
	{	
		double cr = Double.NaN;
//		boolean generate = true;
//		while(generate)
//		{
//			cr = gaussian(mean , 0.1);
//			generate = cr<0 || cr>1;
//		}	
		cr = RandUtils.gaussian(mean,0.1);
		if (cr > 1)
			cr = cr - fix(cr);
		else if (cr < 0)
			cr=1-Math.abs(cr - fix(cr));
		return cr;
	}
	
	private double generateF(double Fm)
	{
		double F = Double.NaN;
//		boolean generate = true;
//		while(generate)
//		{
//			F = cauchy(Fm , 0.1);
//			generate = F<=0 || F>1;
//		}
		F = RandUtils.cauchy(Fm , 0.1);
		double Fsat = (F - Double.MIN_VALUE)/(1 -Double.MIN_VALUE);
		if (Fsat > 1)
			F = Fsat - fix(Fsat);
		else if (Fsat < 0)
			F=1-Math.abs(Fsat-fix(Fsat));
		return F;
	}
}
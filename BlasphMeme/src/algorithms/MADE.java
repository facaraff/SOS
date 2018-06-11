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
import static utils.algorithms.operators.DEOp.currentToRand1;
import static utils.algorithms.operators.DEOp.rand1;
import static utils.algorithms.operators.DEOp.rand2;
import static utils.algorithms.operators.DEOp.randToBest2;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import static utils.MatLab.max;
import static utils.MatLab.sum;
import static utils.MatLab.rand;
import static utils.MatLab.zeros;

import java.util.Vector;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;


public class MADE extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int populationSize = getParameter("p0").intValue(); //50 
		int LP = getParameter("p1").intValue();   //50    
		double alpha = getParameter("p2").doubleValue(); //0.5 
		double beta = getParameter("p3").doubleValue(); //0.7 
		double pmin = getParameter("p4").doubleValue(); //0.02
		int aggGammaMethod = getParameter("p5").intValue(); // 1 = WS, 2 = NDGI, 3 = NDDI
		
		int poolSize = 4;
		double CR = Double.NaN;
		double F = Double.NaN;
		
		Vector<Vector<Double>> sF = new Vector<Vector<Double>>();
		Vector<Vector<Double>> sCR = new Vector<Vector<Double>>();
		for(int n=0; n < poolSize; n++)
		{
			sF.add(new Vector<Double>());
			sCR.add(new Vector<Double>());
		}
		
		double[] muF = {0.5, 0.5, 0.5, 0.5};
		double[] muCR = {0.5, 0.5, 0.5, 0.5};
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		double[][] population = new double[populationSize][problemDimension];
		double[] fitnesses = new double[populationSize];
		
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		@SuppressWarnings("unused")
		double g = 0;
		
		// evaluate initial population
		for (int j = 0; j < populationSize; j++)
		{
			if (initialSolution != null && j == 0)
			{
				for (int n = 0; n < problemDimension; n++)
					population[j][n] = initialSolution[n];
			}
			else
			{
				double[] tmp = generateRandomSolution(bounds, problemDimension);
				for (int n = 0; n < problemDimension; n++)
					population[j][n] = tmp[n];
			}
			fitnesses[j] = problem.f(population[j]);
			
			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = population[j][n];
				FT.add(i, fBest);
			}
			
			i++;
		}
		
		double[] currPt = new double[problemDimension];
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double currFit = Double.NaN;
		double crossFit = Double.NaN;
		double[] q = {1, 1, 1, 1};
		double[] p = {0.25, 0.25, 0.25, 0.25};
		
		// iterate
		while (i < maxEvaluations)
		{
			g++;
			double[][] pop = population.clone();
			double[] oldFitnesses = fitnesses.clone();
			double[] oldBest = best.clone();
			
			int[] k = roulette(p,populationSize);
			
			for (int j = 0; j < populationSize && i < maxEvaluations; j++)
			{
				for (int n = 0; n < problemDimension; n++)
					currPt[n] = population[j][n];
				currFit = fitnesses[j];
				
				do //ma saturarlo invece di fare sta boiata??
					F = RandUtils.cauchy(muF[k[j]],0.1);
				while(F > 1 || F <= 0);
				do
					CR = RandUtils.gaussian(muCR[k[j]],0.1);
				while(CR > 1 || CR <= 0);
				switch (k[j])
				{
					case 0:
						// DE/rand/1//bin
						newPt = rand1(population, F);
						crossPt = crossOverBin(currPt, newPt, CR);
						break;
					case 1:
						// DE/rand/2/bin
						newPt = rand2(population, F);
						crossPt = crossOverBin(currPt, newPt, CR);
						break;
					case 2:
						// DE/rand-to-best/2/bin
						newPt = randToBest2(population, best, F);
						break;
					case 3:
						// DE/current-to-rand/1
						crossPt = currentToRand1(population, j, F);
						break;
					default:
						break;
				}
				crossPt = toro(crossPt, bounds);
				crossFit = problem.f(crossPt);
				i++;

				if (crossFit < fBest)
				{
					fBest = crossFit;
					for (int n = 0; n < problemDimension; n++)
						best[n] = crossPt[n];
				}
				
				if(i%(10*problemDimension)==0)
					FT.add(i, fBest);
				
				// replacement
				if (crossFit < currFit)
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = crossPt[n];
					fitnesses[j] = crossFit;
					if(!sF.get(k[j]).contains(F))
						sF.get(k[j]).addElement(F); 
					if(!sCR.get(k[j]).contains(CR))
						sCR.get(k[j]).addElement(CR);
				}
				else
				{
					for (int n = 0; n < problemDimension; n++)
						population[j][n] = currPt[n];
					fitnesses[j] = currFit;
				}
				
			}
		
			double[] gamma = gamma(aggGammaMethod, eta(oldFitnesses, fitnesses, fBest), tau(pop, oldBest), alpha, k);
			if(sum(gamma) != 0)
			{
				double r = Double.NaN;
				int[] indices = null;
				//update p
				for(int n=0; n < poolSize; n++)
				{
					indices = find(n,k);
					r = mean(gamma, indices); 
					q[n] = (1-beta)*q[n]+beta*r;
				}
				
				double sum = sum(q);
				for(int n=0; n < p.length; n++)
					p[n] = pmin + (1 - poolSize*pmin)*(q[n]/sum);
				sum = sum(p);
				for(int n=0; n < p.length; n++)
					p[n] = p[n]/sum;
	            
			}
			
			updateDistribution(sF,sCR, muF, muCR, LP);	
		}
		
		finalBest = best;
		
		FT.add(i, fBest);
		
		return FT;
	}
	
	private double[] eta(double[] oldF, double[] newF, double bestF)
	{
		double[] etaHat = new double[oldF.length];
		boolean empty = true;
		for(int i=0; i < etaHat.length; i++)
		{
			double num = oldF[i] - newF[i];
			if(num > 0) //to avoid division by 0 (bestF is an element of the population and oldF - fBest will be = 0)
			{
				etaHat[i] = num/(oldF[i] - bestF); 
				empty = false;
			}
			else
				etaHat[i] = 0;
		}
		double[] eta = new double[etaHat.length];
		if(empty)
			for(int i=0; i < eta.length ; i++)
				eta[i] = 1;
		else
		{
			double max = max(etaHat);
			for(int i=0; i < eta.length ; i++)
				eta[i] = etaHat[i]/max;	
		}
		return eta;
	}
	
	private double[] tau(double[][] pop, double[] best)
	{
		double[] tauHat = new double[pop.length];
		for(int i=0; i < pop.length; i++)
			for(int j=0; j < best.length; j++)
				tauHat[i] += Math.pow( pop[i][j] - best[j] ,2);
		
		for(int i=0; i < tauHat.length; i++)
			tauHat[i] = Math.sqrt(tauHat[i]);
	
		double[] tau = new double[tauHat.length];
		
		double sum = sum(tauHat);
		if(sum == 0)
		{
			for(int i=0; i < tau.length ; i++)
				tau[i] = 1;
		}
		else
		{
		double max = max(tauHat);
		for(int i=0; i < tau.length ; i++)
			tau[i] = tauHat[i]/max;
		}
		
		return tau;
	}
	
	private double[] gamma(int method, double[] eta, double[] tau, double alpha, int[] k)
	{
		int popSize = tau.length;
		double[] gamma = new double[popSize];
		double[][] dom = null;
		double[][] byDom = null;
		double[][] equal = null;
		switch (method)
		{
			case 1: //WS
				for(int i=0; i < gamma.length; i++)
					gamma[i] = alpha*eta[i] + (1 - alpha)*tau[i]; 
				break;
			case 2: //NDGI
				dom = zeros(popSize,popSize);
				byDom = zeros(popSize,popSize);
				equal = zeros(popSize,popSize);
				IndCreditNonDom(dom, byDom, equal, eta, tau);
				for(int i=0; i < popSize; i++)
				{
					for(int j=0; j < popSize; j++)
					{
						if(k[i] == k[j])
						{
							dom[i][j] = 0;
							byDom[i][j] = 0;
							equal[i][j] = 0;
						}
					}
				}
				
				for(int i=0; i < popSize; i++)
					for(int j=0; j < popSize; j++)
						gamma[i] += dom[i][j];
				
				break;
			case 3://NDDI
				
				dom = zeros(popSize,popSize);
				byDom = zeros(popSize,popSize);
				equal = zeros(popSize,popSize);
				IndCreditNonDom(dom, byDom, equal, eta, tau);
				for(int i=0; i < popSize; i++)
				{
					for(int j=0; j < popSize; j++)
					{
						if(k[i] == k[j])
						{
							dom[i][j] = 0;
							byDom[i][j] = 0;
							equal[i][j] = 0;
						}
					}
				}
				
				for(int i=0; i < popSize; i++)
					for(int j=0; j < popSize; j++)
						gamma[i] += byDom[i][j];
				double max = max(gamma);
				for(int j=0; j < popSize; j++)
					gamma[j] = max - gamma[j];
				
				break;
			default:
				break;
		}
		return gamma;
	}
	
	private int[] find(int i, int[] set)
	{
		Vector<Integer> vector = new Vector<Integer>();
		for(int n=0; n < set.length; n++)
			if(set[n] == i)
				vector.add(n);
		int[] indices = null;
		int size = vector.size();
		if(size != 0)
		{
			indices = new int[size];
			for(int n=0; n < indices.length; n++)
				indices[n] = vector.get(n).intValue();
		}
		return indices;
	}
	
	private double mean(double[] input, int[] positions)
	{
		double sum = 0;
		if(positions != null)
		{
			for(int n=0; n < positions.length; n++)
				sum += input[positions[n]];
			sum = sum/positions.length;
		}
		return sum;
	}
	
	//private void updateDistribution(Vector<Double>[] sF, Vector<Double>[] sCR, double[] muF, double[] muCR, int LP)
	private void updateDistribution(Vector<Vector<Double>> sF, Vector<Vector<Double>> sCR, double[] muF, double[] muCR, int LP)
	{
		for(int k=0; k < sF.size(); k++)
			if(sF.get(k).size() >= LP)
			{
				Vector<Double> sFk = sF.get(k);
				Vector<Double> sCRk = sCR.get(k);
				int size = sFk.size();//> LP
				for(int i=0; i < size - LP; i++)
				{
					//sF.get(k).removeElementAt(i);
					//sCR.get(k).removeElementAt(i);
					sFk.removeElementAt(i);
					sCRk.removeElementAt(i);
				}
				double F = 0; double F2 = 0; double CR = 0;
				size = sFk.size();  // == LP
				for(int i=0; i < size; i++)
				{
					//F +=  sF.get(k).get(i).doubleValue();
					//F2 += Math.pow(sF.get(k).get(i).doubleValue(), 2);
					//CR += sCR.get(k).get(i).doubleValue();
					F +=  sFk.get(i).doubleValue();
					F2 += Math.pow(sFk.get(i).doubleValue(), 2);
					CR += sCRk.get(i).doubleValue();
				}
				muF[k] = F2/F;
				muCR[k] = CR/LP; 
			}
	}
		
	private int[] roulette(double[] p, int popSize)
	{
		int[] K = new int[popSize];
		int m = p.length;
		double[] r = rand(popSize);
		for(int i=0; i < popSize; i++)
		{
			double sumP = 0;
			int J = (int)Math.ceil(m*RandUtils.random());
			if(J == 0) J = 1;
			while(sumP < r[i])
			{
				sumP += p[(J-1)%m];
				J++;
			}
			K[i] = (J-2)%m;
		}
		return K;
	}
	
	private void IndCreditNonDom(double[][] dom, double[][] byDom, double[][] equal, double[] fitImpact, double[] divImpact)
	{
		/*non-dominated sorting 
		 input:
		    	FitImpact                             
		    	DivImpact                             
		    	PoolSize                                                              
		 Output:
		    	Dom                                   
		    	ByDom                                 
		    	Equal */
		int popSize = fitImpact.length;

		for(int i=0; i < popSize; i++)
		{
			for(int j=0; j < popSize; j++)
			{
				int worse = 0;
				int better = 0;
				int tie = 0;
				
				if(fitImpact[i] > fitImpact[j])
					better++;
				else if(fitImpact[i] < fitImpact[j])
					worse++;
				else
					tie++;
				
				if(divImpact[i] > divImpact[j])
					better++;
				else if(divImpact[i] < divImpact[j])
					worse++;
				else
					tie++;
				
				if((better + tie)==2 && tie!=2)
				{
					dom[i][j] = 1;
					byDom[j][i] = 1;
				}
				else if((worse + tie)==2 && tie!=2)
				{
					byDom[i][j] = 1;
					dom[j][i] = 1;
				}
				else if((better==1 && worse==1) || tie==2)
				{
					equal[i][j] = 1;
					equal[j][i] = 1;
				}
			}
		}
	}
}
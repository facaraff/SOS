package algorithms;

import static utils.algorithms.Misc.generateRandomSolution;


import utils.MatLab;
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * Comprehensive Learning Particle Swarm Optimization
 */
public class CLPSO extends Algorithm
{	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{	
		int ps = getParameter("p0").intValue(); //60

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		int maxGeneration;
		if ((maxEvaluations % ps) == 0)
			maxGeneration = maxEvaluations/ps;
		else
			maxGeneration = maxEvaluations/ps+1;

		double[] VRmin = new double[problemDimension];
		double[] VRmax = new double[problemDimension];
		for (int n = 0; n < problemDimension; n++)
		{
			VRmin[n] = bounds[n][0];
			VRmax[n] = bounds[n][1];
		}
		
		double[] t = new double[ps];
		double[] Pc = new double[ps];

		double tmp = 0;
		for (int i = 0; i < ps; i++)
		{
			t[i] = 5*tmp;
			tmp += 1.0/(ps-1);			
		}

		for (int i = 0; i < ps; i++)
			Pc[i] = (0.5)*(Math.exp(t[i])-Math.exp(t[0]))/(Math.exp(t[ps-1])-Math.exp(t[0]));

		double m[] = new double[ps];
		for (int i = 0; i < ps; i++)
			m[i] = 0.0;

		double[] iwt = new double[maxGeneration];
		for (int i = 0; i < maxGeneration; i++)
			iwt[i] = 0.9-(i+1)*(0.7/maxGeneration);

		double[] cc = {1.49445, 1.49445};

		double[] mv = new double[problemDimension];
		double[] Vmin = new double[problemDimension];
		double[] Vmax = new double[problemDimension];
		for (int n = 0; n < problemDimension; n++)
		{
			mv[n] = 0.2*(VRmax[n]-VRmin[n]);
			Vmin[n] = -mv[n];
			Vmax[n] = -Vmin[n];
		}

		double[][] pos = new double[ps][problemDimension];
		double[][] vel = new double[ps][problemDimension];
		double[][] acc = new double[ps][problemDimension];
		double[][] pBest = new double[ps][problemDimension];
		double[][] pBest_f = new double[ps][problemDimension];

		double[] fitnesses = new double[ps];
		double[] bestFitnesses = new double[ps];

		double[] best = new double[problemDimension];
		double fBest = Double.NaN;

		int fitcount = 0;

		// evaluate initial population
		for (int j = 0; j < ps; j++)
		{
			double[] particle;
			if (j == 0 && initialSolution != null)
				particle = initialSolution;
			else
				particle = generateRandomSolution(bounds, problemDimension);
			
			for (int n = 0; n < problemDimension; n++)
			{
				pos[j][n] = particle[n];
				pBest[j][n] = particle[n];
				vel[j][n] = Vmin[n]+2*Vmax[n]*RandUtils.random();
			}

			fitnesses[j] = problem.f(particle);
			bestFitnesses[j] = fitnesses[j];
			fitcount++; 

			if (j == 0 || fitnesses[j] < fBest)
			{
				fBest = fitnesses[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = pos[j][n];
				FT.add(fitcount, fBest);
			}
		}

		int[] stay_num = new int[ps];
		for (int j = 0; j < ps; j++)
			stay_num[j] = 0;

		int[][] ai = new int[ps][problemDimension]; 
		for (int i = 0; i < ps; i++)
			for (int j = 0; j < problemDimension; j++)
				ai[i][j] = 0;

		int[][] f_pbest = new int[ps][problemDimension];
		for (int k = 0; k < ps; k++)
		{
			for (int j = 0; j < problemDimension; j++)
				f_pbest[k][j] = k;
		}

		int[] fi1 = new int[problemDimension];
		int[] fi2 = new int[problemDimension];
		int[] fi = new int[problemDimension];
		int[] bi = new int[problemDimension];
		
		for (int k = 0; k < ps; k++)
		{
			int[] ar = RandUtils.randomPermutation(problemDimension);
			for (int j = 0; j < m[k]; j++)
				ai[k][ar[j]] = 1;

			for (int j = 0; j < problemDimension; j++)
			{
				fi1[j] = (int) Math.floor(ps*RandUtils.random());
				fi2[j] = (int) Math.floor(ps*RandUtils.random());
			}
			
			for (int j = 0; j < problemDimension; j++)
			{
				if (bestFitnesses[fi1[j]] < bestFitnesses[fi2[j]])
					fi[j] = fi1[j];
				else if (bestFitnesses[fi1[j]] >= bestFitnesses[fi2[j]])
					fi[j] = fi2[j];
			}

			for (int j = 0; j < problemDimension; j++)
				bi[j] = (int) Math.ceil(RandUtils.random() - 1 + Pc[k]);

			if (MatLab.isZeros(bi))
			{
				int rc = RandUtils.randomInteger(problemDimension-1);
				bi[rc] = 1;
			}
				
			for (int j = 0; j < problemDimension; j++)
				f_pbest[k][j] = bi[j]*fi[j]+(1-bi[j])*f_pbest[k][j];
		}


		// iterate
		int i = 0;
		while ((i < maxGeneration) && (fitcount < maxEvaluations))
		{
			i++;
			//if ((fitcount % 10000) == 0)
			//	System.out.println(fitcount);
			
			for (int k = 0; k < ps; k++)
				for (int j = 0; j < problemDimension; j++)
					pBest_f[k][j] = 0;
							
			for (int k = 0; k < ps && fitcount < maxEvaluations; k++)
			{
				if (stay_num[k] > 5)
				{
					stay_num[k] = 0;
					for (int j = 0; j < problemDimension; j++)
						ai[k][j] = 0;
					for (int j = 0; j < problemDimension; j++)
						f_pbest[k][j] = k;

					int[] ar = RandUtils.randomPermutation(problemDimension);
					for (int j = 0; j < m[k]; j++)
						ai[k][ar[j]] = 1;

					for (int j = 0; j < problemDimension; j++)
					{
						fi1[j] = (int) Math.floor(ps*RandUtils.random());
						fi2[j] = (int) Math.floor(ps*RandUtils.random());
					}

					for (int j = 0; j < problemDimension; j++)
					{
						if (bestFitnesses[fi1[j]] < bestFitnesses[fi2[j]])
							fi[j] = fi1[j];
						else if (bestFitnesses[fi1[j]] >= bestFitnesses[fi2[j]])
							fi[j] = fi2[j];
					}

					for (int j = 0; j < problemDimension; j++)
						bi[j] = (int) Math.ceil(RandUtils.random() - 1 + Pc[k]);

					if (MatLab.isZeros(bi))
					{
						int rc = RandUtils.randomInteger(problemDimension);
						rc = (rc < problemDimension) ? rc : (problemDimension-1);
						bi[rc] = 1;
					}

					for (int j = 0; j < problemDimension; j++)
						f_pbest[k][j] = bi[j]*fi[j]+(1-bi[j])*f_pbest[k][j];
				}

				for (int j = 0; j < problemDimension; j++)
					pBest_f[k][j] = pBest[f_pbest[k][j]][j];

				for (int j = 0; j < problemDimension; j++)
				{
					acc[k][j] = cc[0]*(1-ai[k][j])*RandUtils.random()*(pBest_f[k][j]-pos[k][j]) + cc[1]*ai[k][j]*RandUtils.random()*(best[j]-pos[k][j]);
					vel[k][j] = iwt[i]*vel[k][j] + acc[k][j];
					if (vel[k][j] > mv[j])
						vel[k][j] = mv[j];
					else if (vel[k][j] < -mv[j])
						vel[k][j] = -mv[j];
					pos[k][j] = pos[k][j] + vel[k][j];
				}
				
				pos[k] = correct(pos[k], bounds);
				fitnesses[k] = problem.f(pos[k]);
				fitcount=fitcount+1;
				
				boolean notImprove = (bestFitnesses[k] <= fitnesses[k]);
				if (notImprove)
					stay_num[k] = stay_num[k] + 1;
			
				// replacement
				if (fitnesses[k] < bestFitnesses[k])
				{
					for (int j = 0; j < problemDimension; j++)
						pBest[k][j] = pos[k][j];
					bestFitnesses[k] = fitnesses[k];
				}

				// best update
				if (bestFitnesses[k] < fBest)
				{
					fBest = fitnesses[k];
					for (int j = 0; j < problemDimension; j++)
						best[j] = pos[k][j];
				}
				if(fitcount%problemDimension == 0)
					FT.add(fitcount, fBest);
			}
		}

		finalBest = best;
		
		FT.add(fitcount, fBest);

		return FT;
	}
}
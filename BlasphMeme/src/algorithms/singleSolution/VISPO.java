package algorithms.singleSolution;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import static utils.MatLab.max;
import static utils.MatLab.min;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Very Intelligent Single Particle Optimization
 */
public class VISPO extends Algorithm
{	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int learningPeriod = this.getParameter("p0").intValue();	//10
		int learningSteps = this.getParameter("p1").intValue();	//30
		double threshold = this.getParameter("p2");				//0.65
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// particle
		double[] particle = new double[problemDimension];
		double[] bestParticle = new double[problemDimension];
		double fParticle;

		// initial solution
		if (initialSolution != null)
			for(int i=0;i<initialSolution.length;i++)
				particle[i] = initialSolution[i];
		else
			particle = generateRandomSolution(bounds, problemDimension);
		
		fParticle = problem.f(particle);
		FT.add(0, fParticle);
		for (int l = 0; l < problemDimension; l++)
			bestParticle[l] = particle[l];

		double[] A = new double[problemDimension];
		for (int j = 0; j < problemDimension; j++)
			A[j] = bounds[j][1]-bounds[j][0];

		// temp variables
		double velocity = 0;
		double oldfFParticle = fParticle;
		double initialFParticle = fParticle;
		double bestFParticle = fParticle;
		double posOld;
		double deltaF;
		
		boolean debug = false;
		boolean saturateToro = true;
		
		int i = 1;
		int iterationCount = 0;
		int kMax = learningSteps;
		int improvements = 0;
		
		while (i < maxEvaluations)
		{
			initialFParticle = bestFParticle;
			for (int j = 0; j < problemDimension && i < maxEvaluations; j++)
			{
				velocity = A[j]*(-0.5+RandUtils.random());
				for (int k = 0; k < kMax && i < maxEvaluations; k++)
				{
					// old fitness value
					oldfFParticle = fParticle;
					// old particle position
					posOld = particle[j];

					// calculate new position
					particle[j] += velocity;
					if (saturateToro)
						particle = toro(particle, bounds);
					else
						particle[j] = min(max(particle[j], bounds[j][0]), bounds[j][1]);
					
					// calculate new fitness
					fParticle = problem.f(particle);
					i++;

					// estimate performance
					if (oldfFParticle < fParticle)
					{
						// halve velocity
						velocity /= 2;
						
						// use old position
						particle[j] = posOld;
						fParticle = oldfFParticle;
					}
					
					// update best solution
					if (fParticle < bestFParticle)
					{
						for (int l = 0; l < problemDimension; l++)
							bestParticle[l] = particle[l];
						bestFParticle = fParticle;

						if (iterationCount < learningPeriod)
							improvements++;
					}
					
					if (i % problemDimension == 0)
						FT.add(i, bestFParticle);
				}
			}

			// learning complete
			if (iterationCount == learningPeriod)
			{
				if ((double)improvements/(problemDimension*learningPeriod) >= threshold)
					kMax = learningSteps;
				else
					kMax = 1;
			}
			
			if (iterationCount > learningPeriod)
			{
				deltaF = (initialFParticle-bestFParticle)/initialFParticle;

				// restart in case of stagnation
				if (deltaF == 0 && i < maxEvaluations)
				{
					// sample new solution and crossover it with best (adaptive CR)
					double CR = Math.pow((double)(maxEvaluations-i)/maxEvaluations,2);
					for (int j = 0; j < problemDimension; j++)
					{
						if (RandUtils.random() < CR)
							particle[j] = bounds[j][0] + (bounds[j][1]-bounds[j][0])*RandUtils.random();
						else
							particle[j] = bestParticle[j];
					}

					if (debug)
						System.out.println("(#" + i + " deltaF = " + deltaF + ") -> restart with CR = " + CR);

					// evaluate it
					fParticle = problem.f(particle);
					i++;

					// update best solution
					if (fParticle < bestFParticle)
					{
						for (int l = 0; l < problemDimension; l++)
							bestParticle[l] = particle[l];
						bestFParticle = fParticle;
					}
					
					if (i % problemDimension == 0)
						FT.add(i, bestFParticle);
				}
			}
			
			iterationCount++;
		}

		finalBest = bestParticle;
		FT.add(i, bestFParticle);

		return FT;
	}
}
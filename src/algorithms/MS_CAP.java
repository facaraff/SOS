package algorithms;

import static utils.algorithms.Misc.generateRandomSolution;



import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.algorithms.operators.DEOp;
import utils.RunAndStore.FTrend;


/*
 * Multi-Strategy Coevolving Aging Particles (MS-CAP)
 */
public class MS_CAP extends Algorithm
{	
	@SuppressWarnings("unused")
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		// 0: saturatecorrect, 1: saturateOnBounds, 2: saturateRebound
		//int saturateType = 0;
		boolean debug = false;

		int nrParticles = this.getParameter("p0").intValue();				//50
		double epsilon = this.getParameter("p1").doubleValue();			//1e-6
		int recombinationCycles = this.getParameter("p2").intValue();		//3


		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// current particles
		double[][] particles = new double[nrParticles][problemDimension];
		double[] fParticles = new double[nrParticles];

		// old particles
		double[][] oldParticles = new double[nrParticles][problemDimension];
		double[] fOldParticles = new double[nrParticles];

		// current velocities
		double[][] velocities = new double[nrParticles][problemDimension];

		// current particles' lifetime
		double[] lifetimes = new double[nrParticles];

		// mutated point
		double[] newPt = new double[problemDimension];
		double[] crossPt = new double[problemDimension];
		double crossFit;

		boolean bestUpdated = false;
		int bestIndex = 0;

		// initial solutions (they all start at the same position)
		if (initialSolution != null)
		{
			for (int j = 0; j < problemDimension; j++)
				particles[0][j] = initialSolution[j];
		}
		else
			particles[0] = generateRandomSolution(bounds, problemDimension);
		fParticles[0] = problem.f(particles[0]);
		for (int i = 1; i < nrParticles; i++)
		{
			for (int j = 0; j < problemDimension; j++)
				particles[i][j] = particles[0][j];
			fParticles[i] = fParticles[0];
		}

		FT.add(0,fParticles[0]);

		int evaluations = 1;

		for (int i = 0; i < nrParticles; i++)
		{
			// initial velocity
			for (int j = 0; j < problemDimension; j++)
				velocities[i][j] = (-0.25+0.5*RandUtils.random())*(bounds[j][1]-bounds[j][0]);
			// initial lifetime
			lifetimes[i] = 0;
		}

		// initialize jDE parameters
		double fl = 0.1;
		double fu = 1;

		double[] F = new double[nrParticles];
		double[] CR = new double[nrParticles];
		int[] strategyCR = new int[nrParticles];
		int[] strategyMut = new int[nrParticles];

		/*
		for (int j = 0; j < nrParticles; j++)
		{
			F[j] = fl + RandUtils.random()*(fu-fl);
			CR[j] = RandUtils.random();
			strategyCR[j] = RandUtbestsils.randomInteger(1);
			strategyMut[j] = RandUtils.randomInteger(3);
		}
		*/

		boolean[] particleChanged = new boolean[nrParticles];

		int gen = 0;
		int dispEval = 1;
		int deEval = 0;

		while (evaluations < maxEvaluations)
		{
			// if (debug)
			// {
			// 	//System.out.println("particles");
			// 	for (double[] particle : particles)
			// 		System.out.println(MathUtils.toString(particle));

			// 	/*
			// 	System.out.println("velocity");
			// 	for (double[] velocity : velocities)
			// 		System.out.println(MathUtils.toString(velocity));

			// 	System.out.println("lifetimes");
			// 	MathUtils.print(lifetimes);
			// 	 */
			// }

			bestUpdated = false;

			for (int i = 0; i < nrParticles && evaluations < maxEvaluations; i++)
			{
				// save old particle and its fitness
				for (int j = 0; j < problemDimension; j++)
					oldParticles[i][j] = particles[i][j];
				fOldParticles[i] = fParticles[i];

				// disperse particle
				for (int j = 0; j < problemDimension; j++)
				{
					//velocities[i][j] = velocities[i][j] + bestAttraction*(particles[bestIndex][j]-particles[i][j]);
					velocities[i][j] = velocities[i][j] + RandUtils.random()*((double)(evaluations)/maxEvaluations)*(particles[bestIndex][j]-particles[i][j]);
					particles[i][j] += velocities[i][j];
				}

				// saturation
//				if (saturateType == 0)
					particles[i] = correct(particles[i], bounds);
//				else if (saturateType == 1)
//					particles[i] = AlgorithmUtils.saturateOnBounds(particles[i], bounds);
//				else if (saturateType == 2)
//					particles[i] = AlgorithmUtils.saturateRebound(particles[i], bounds);

				// calculate new fitness
				fParticles[i] = problem.f(particles[i]);
				dispEval++;
				evaluations++;

				// update best solution
				if (fParticles[i] < fParticles[bestIndex])
				{
					bestUpdated = true;
					bestIndex = i;
				}

				// if the new solution is better than the previous one
				if (fParticles[i] < fOldParticles[i])
				{
					// reset particle life time
					lifetimes[i] = 0;
				}
				else
				{
					// increase lifetime and compute decay
					lifetimes[i] += 1;
					double decay = Math.exp(-lifetimes[i]);

					if (decay < epsilon)
					{
						// particle decays

						// reset to a random particle
						int r = RandUtils.randomIntegerExcl(nrParticles-1, i);
						for (int j = 0; j < problemDimension; j++)
							particles[i][j] = oldParticles[r][j];
						fParticles[i] = fOldParticles[r];

						// try a different velocity vector
						for (int j = 0; j < problemDimension; j++)
							velocities[i][j] = (-0.25+0.5*RandUtils.random())*(bounds[j][1]-bounds[j][0]);
							//velocities[i][j] = velocities[r][j];
						
						// reset particle life time
						lifetimes[i] = 0; 
						//lifetimes[i] = lifetimes[r];
					}
					else
					{
						// particle still lives

						// reset to the old particle
						for (int j = 0; j < problemDimension; j++)
							particles[i][j] = oldParticles[i][j];
						fParticles[i] = fOldParticles[i];

						if (lifetimes[i] % 2 == 0)
						{
							// shrink the opposite velocity vector
							for (int j = 0; j < problemDimension; j++)
								velocities[i][j] *= -decay;
						}
						else
						{
							// try the opposite velocity vector
							for (int j = 0; j < problemDimension; j++)
								velocities[i][j] *= -1;
						}
					}
				}
			}

			//bests.add(new Best(evaluations, fParticles[bestIndex]));
			FT.add(evaluations,fParticles[bestIndex]);

			// reset
			if (!bestUpdated)
			{
				for (int i = 0; i < nrParticles; i++)
					particleChanged[i] = false;

				// recombine particles according to a (modified) jDE logic
				for (int k = 0; k < recombinationCycles; k++)
				{
					// save old particles and their fitness
					for (int i = 0; i < nrParticles; i++)
					{
						for (int j = 0; j < problemDimension; j++)
							oldParticles[i][j] = particles[i][j];
						fOldParticles[i] = fParticles[i];
					}

					for (int i = 0; i < nrParticles && evaluations < maxEvaluations; i++)
					{
						F[i] = fl + fu * RandUtils.random();
						CR[i] = RandUtils.random();
						strategyCR[i] = RandUtils.randomInteger(1);
						strategyMut[i] = RandUtils.randomInteger(3);

						// mutation
						if (strategyMut[i] == 0)
							newPt = DEOp.rand1(oldParticles, F[i]);
						else if (strategyMut[i] == 1)
							newPt = DEOp.rand2(oldParticles, F[i]);
						else if (strategyMut[i] == 2)
							newPt = DEOp.randToBest2(oldParticles, oldParticles[bestIndex], F[i]);
						else if (strategyMut[i] == 3)
							newPt = DEOp.currentToBest1(oldParticles, oldParticles[bestIndex], i, F[i]);

						// saturation
//						if (saturateType == 0)
							newPt = correct(newPt, bounds);
//						else if (saturateType == 1)
//							newPt = AlgorithmUtils.saturateOnBounds(newPt, bounds);
//						else if (saturateType == 2)
//							newPt = AlgorithmUtils.saturateRebound(newPt, bounds);

						// crossover
						if (strategyCR[i] == 0)
							crossPt = DEOp.crossOverBin(oldParticles[i], newPt, CR[i]);
						else
							crossPt = DEOp.crossOverExp(oldParticles[i], newPt, CR[i]);

						crossFit = problem.f(crossPt);
						deEval++;
						evaluations++;

						// replacement (one-to-one spawning)
						if (crossFit < fOldParticles[i])
						{
							for (int j = 0; j < problemDimension; j++)
								particles[i][j] = crossPt[j];
							fParticles[i] = crossFit;

							particleChanged[i] = true;
						}
					}

					// update best solution
					bestIndex = 0;
					for (int i = 1; i < nrParticles; i++)
					{
						if (fParticles[i] < fParticles[bestIndex])
							bestIndex = i;					
					}

					gen++;
				}

				//bests.add(new Best(evaluations, fParticles[bestIndex]));
				FT.add(evaluations,fParticles[bestIndex]);

				// reset velocities and lifetimes of changed particles
				for (int i = 0; i < nrParticles; i++)
				{
					if (particleChanged[i])
					{
						// initial velocity
						for (int j = 0; j < problemDimension; j++)
							velocities[i][j] = (-0.25+0.5*RandUtils.random())*(bounds[j][1]-bounds[j][0]);
						// initial lifetime
						lifetimes[i] = 0;
					}
				}
			}
		}

		//System.out.println(dispEval + "\t" + deEval);

		finalBest = particles[bestIndex];
		//bests.add(new Best(maxEvaluations, fParticles[bestIndex]));
		FT.add(evaluations,fParticles[bestIndex]);
		return FT;
	}
}
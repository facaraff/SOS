package algorithms;
//in this part you can import the functionalities that yuo need to use for implementing your algorithm
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.MatLab.max;
import static utils.MatLab.min;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
/**
 * Intelligent Single Particle Optimization
 */
public class ISPO extends Algorithm //This class implements the algorithm. Every algorithm will have to contain its specific implementation within the method "execute". The latter will contain a main loop performing the iterations, and will have to return the fitness trend (including the final best) solution. Look at this examples before implementing your first algorithm.
{
	@Override
	//to implement a different algorithm you'll have to change the content of this function
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		// first, we need to define variables for storing the paramters of the algorithm
		double A = getParameter("p0");							
		double P = getParameter("p1");							
		int B = this.getParameter("p2").intValue();			
		int S = this.getParameter("p3").intValue();		
		double E = this.getParameter("p4");					
		int PartLoop = this.getParameter("p5").intValue();	

		//we always need an object of the kynd FTrend (for storing the fitness trend), and variables for storing the dimesionality vlue and the bounds of the problem as showed below
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// particle (the solution, i.e. "x")
		double[] particle = new double[problemDimension];
		double fParticle; //fitness value, i.e. "f(x)"
		int i = 0;
		// initial solution
		if (initialSolution != null)
		{
			particle = initialSolution;
		    fParticle = initialFitness;
		}
		else//random intitial guess
		{
			particle = generateRandomSolution(bounds, problemDimension);
			fParticle = problem.f(particle);
			i++;
		}
		//store the initital guess
		FT.add(0, fParticle);

		// temp variables
		double L = 0;
		double velocity = 0;
		double oldfFParticle = fParticle;
		double posOld;
		
		//main loop
		while (i < maxEvaluations)
		{
			for (int j = 0; j < problemDimension && i < maxEvaluations; j++)
			{
				// init learning factor
				L = 0;

				// for each part loop
				for (int k = 0; k < PartLoop && i < maxEvaluations; k++)
				{
					// old fitness value
					oldfFParticle = fParticle;
					// old particle position
					posOld = particle[j];

					// calculate velocity
					velocity = A/Math.pow(k+1,P)*(-0.5+RandUtils.random())+B*L;

					// calculate new position
					particle[j] += velocity;
					particle[j] = min(max(particle[j], bounds[j][0]), bounds[j][1]);

					// calculate new fitness
					fParticle = problem.f(particle);
					i++;//iteration couter has to be incremented

					// estimate performance
					if (oldfFParticle < fParticle)
					{
						// adjust learning factor
						if (L != 0)
							L /= S;
						if (Math.abs(L) < E)
							L = 0;
						// use old position
						particle[j] = posOld;
						fParticle = oldfFParticle;
					}
					else
					{
						// use current velocity as learning factor
						L = velocity;
					}
					
					if(i%problemDimension==0)
						FT.add(i, fParticle);
				}
			}
		}
		
		
		finalBest = particle; //save the final best
		FT.add(i, fParticle);//add it to the txt file (row data)

		
		return FT; //return the fitness trend
	}
}

/**
Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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

import static utils.algorithms.Misc.generateRandomSolution;

import utils.algorithms.operators.PSOOp;

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/**
 * @author Fabio Caraffini (fabio.caraffini@gmal.com)
 * {@link www.tinyurl.com/FabioCaraffini}
 * 
 * Particle Swarm Optimisation
 */

public class PSO extends Algorithm
{
	protected String vInitMethod = ""; 
	protected String velocityUpdteStrategy = null;
	protected String perturbationStrategy = null;
	
	public PSO() {this.velocityUpdteStrategy = "classic"; this.perturbationStrategy = "classic";}
	public PSO(String velocityUpdteStrategy) {this.velocityUpdteStrategy = velocityUpdteStrategy; this.perturbationStrategy = "classic";}
	public PSO(String mut, String velocityUpdteStrategy, String perturbationStrategy) {{this.velocityUpdteStrategy = velocityUpdteStrategy; this.perturbationStrategy = perturbationStrategy;}}
	
	public void setInitialVMethod(String method) {this.vInitMethod = method;}
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int swarmSize = getParameter("p0").intValue(); 
		double phi1 = getParameter("p1").doubleValue();
		double phi2 = getParameter("p2").doubleValue();
		double phi3 = getParameter("p3").doubleValue();
		
		double gamma1 = getParameter("p4").doubleValue(); //rarely used ==> = 1
		double gamma2 = getParameter("p5").doubleValue(); //rarely used ==> = 1

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[][] swarm = new double[swarmSize][problemDimension];
		double[][] pBest = new double[swarmSize][problemDimension];
		double[][] v = new double[swarmSize][problemDimension];
		double[] gBest = new double[problemDimension];
//		double[] fitness = new double[swarmSize];
		double[] pBestFitness = new double[swarmSize];
		double fBest = Double.NaN;
		double newFitness = Double.NaN;
		
		double[] newInfeasibleX = new double[problemDimension];
		
		int i = 0;
		
		// evaluate initial swarm and initialise velocity vectors
		for (int j = 0; j < swarmSize; j++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
			{
				swarm[j][n] = tmp[n];
				pBest[j][n] = tmp[n];
				v[j] = PSOOp.initVelocityVector(vInitMethod, problem);
			}
			pBestFitness[j] = problem.f(swarm[j]);
//			pFitness[j] = fitness[j];
			
			if (j == 0 || pBestFitness[j] < fBest)
			{
				fBest = pBestFitness[j];
				for (int n = 0; n < problemDimension; n++)
					gBest[n] = swarm[j][n];
					FT.add(i, fBest);
			}
			
			i++;
		}



		// iterate
		while (i < maxEvaluations)
		{

			for (int j = 0; j < swarmSize && i < maxEvaluations; j++)
			{
				
				
				switch (velocityUpdteStrategy)
				{
				case "classic":
					v[j] = PSOOp.classicVelocityUpdate(v[j], swarm[j], pBest[j], gBest, phi1, phi2, phi3);
					break;
				default:
					System.out.println("Unrecognised velocity update method!"); 
					break;
				}
				

				switch (perturbationStrategy)
				{
				case "classic":
					newInfeasibleX = PSOOp.moveParticle(swarm[j], v[j]);
//					swarm[j] = PSOOp.moveParticle(swarm[j], v[j]);
					break;
				case "weighted":
					newInfeasibleX = PSOOp.moveParticle(swarm[j], v[j], gamma1, gamma2);
//					swarm[j] = PSOOp.moveParticle(swarm[j], v[j], gamma1, gamma2);
					break;
				default:
					System.out.println("Unrecognised perturbation method!"); 
					break;
				}
		
				
//				swarm[j] = correct(swarm[j], bounds);
				swarm[j] = correct(newInfeasibleX, bounds);
				newFitness = problem.f(swarm[j]);
				i++;

				
				// update personal best position
				if (newFitness < pBestFitness[j])
				{
					for (int n = 0; n < problemDimension; n++)
						pBest[j][n] = swarm[j][n];
					pBestFitness[j] = newFitness;
					
					// best update
					if (newFitness < fBest)
					{
						fBest = newFitness;
						for (int n = 0; n < problemDimension; n++)
							gBest[n] = swarm[j][n];
						FT.add(i, fBest);
					}
				}
				
			}
			
		}
		
		finalBest = gBest;
		
		FT.add(i, fBest);
		return FT;
		
		
	}

}

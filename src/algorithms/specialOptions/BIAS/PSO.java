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
package algorithms.specialOptions.BIAS;

import static utils.algorithms.ISBHelper.getNuberOfNonPositionColumnsForPSO;
import static utils.algorithms.operators.ISBOp.generateRandomSolution;

import utils.algorithms.Counter;
import static utils.algorithms.operators.ISBOp.initVelocityVector;
import static utils.algorithms.operators.ISBOp.classicVelocityUpdate;
import static utils.algorithms.operators.PSOOp.moveParticle;

//import utils.random.RandUtilsISB;

import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/**
 * @author Fabio Caraffini (fabio.caraffini@gmal.com)
 * {@link www.tinyurl.com/FabioCaraffini}
 * 
 * Particle Swarm Optimisation
 */

public class PSO extends AlgorithmBias
{
	protected char vInitMethod; 
	protected char velocityUpdteStrategy;
	protected char perturbationStrategy;

	public PSO() {this.velocityUpdteStrategy = 'o'; this.perturbationStrategy = 'a'; this.nonPositionColumns = getNuberOfNonPositionColumnsForPSO(velocityUpdteStrategy);}
	public PSO(char velocityUpdteStrategy) {this.velocityUpdteStrategy = velocityUpdteStrategy; this.perturbationStrategy = 'w'; this.nonPositionColumns = getNuberOfNonPositionColumnsForPSO(velocityUpdteStrategy);}
	public PSO(char velocityUpdteStrategy, char perturbationStrategy) {this.velocityUpdteStrategy = velocityUpdteStrategy; this.perturbationStrategy = perturbationStrategy; this.nonPositionColumns = getNuberOfNonPositionColumnsForPSO(velocityUpdteStrategy);}
	
	public void setInitialVMethod(char method) {this.vInitMethod = method;}
	
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
		
		double[] pBestFitness = new double[swarmSize];
		double fBest = Double.NaN;
		double newFitness = Double.NaN;
		
		double[] newInfeasibleX = new double[problemDimension];
		
		//************ ISB *************
//		String FullName = getFullName("PSO"+this.vInitMethod+this.velocityUpdteStrategy+this.perturbationStrategy+this.correction+"p"+swarmSize,problem); 
		String FullName = getFullName("ncPSO"+this.correction+"p"+swarmSize,problem); 
		Counter PRNGCounter = new Counter(0);
		createFile(FullName);
		
		
		int[] ids = new int[swarmSize]; //int prevID = -1;
		int newID = 0;
		int gBestID = 0;
		
		writeHeader("popSize "+swarmSize+" phi1 "+phi1+" phi2 "+phi2+" phi3 "+phi3+" gamma1 "+gamma1+" gamma2 "+gamma2, problem);
		
		String line = new String();
		
		//******************************
		
		
		int i = 0;
		
		// evaluate initial swarm and initialise velocity vectors
		for (int j = 0; j < swarmSize; j++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension, PRNGCounter);
			for (int n = 0; n < problemDimension; n++)
			{
				swarm[j][n] = tmp[n];
				pBest[j][n] = tmp[n];
				v[j] = initVelocityVector(vInitMethod, problem, PRNGCounter);
			}
			pBestFitness[j] = problem.f(swarm[j]);
//			pFitness[j] = fitness[j];
			
			newID++;
			ids[j] = newID;
			i++;
			
			if (j == 0 || pBestFitness[j] < fBest)
			{
				fBest = pBestFitness[j];
				for (int n = 0; n < problemDimension; n++)
					gBest[n] = swarm[j][n];
				gBestID = newID;
				FT.add(i, fBest);
				
			}
			

			line = preparePopInitialationLines(this.nonPositionColumns, ids[j], pBestFitness[j], i);
			line = getCompleteLine(pBest[j],line);
			bw.write(line);
			line = null;
			line = new String();
		}



		// iterate
		while (i < maxEvaluations)
		{

			for (int j = 0; j < swarmSize && i < maxEvaluations; j++)
			{
				
				String s = "";
				
				switch (velocityUpdteStrategy)
				{
				case 'o': // --> original
					v[j] = classicVelocityUpdate(v[j], swarm[j], pBest[j], gBest, phi1, phi2, phi3, PRNGCounter);
					//s += ids[r2]+" "+ids[r3]+" "+ids[r1]; what to put here?
					s += ids[j]+" "+gBestID; //what to put here????????????????
					break;
				default:
					System.out.println("Unrecognised velocity update method!"); 
					break;
				}
				
				
				switch (perturbationStrategy)
				{
				case 'a': // --> add
					newInfeasibleX = moveParticle(swarm[j], v[j]);
//					swarm[j] = moveParticle(swarm[j], v[j]);
					break;
				case  'w': //--> weighted
					newInfeasibleX = moveParticle(swarm[j], v[j], gamma1, gamma2);
//					swarm[j] = moveParticle(swarm[j], v[j], gamma1, gamma2);
					break;
				default:
					System.out.println("Unrecognised perturbation method!"); 
					break;
				}
		
				
				
				swarm[j] = correct(newInfeasibleX, swarm[j], bounds);
				newFitness = problem.f(swarm[j]);
				i++;

				
				// update personal best position
				if (newFitness < pBestFitness[j])
				{
					newID++;
					
					
					
					//what to put in S?
					line =""+newID+" "+s+" "+formatter(newFitness)+" "+i+" "+ids[j];
					for(int n = 0; n < problemDimension; n++)
						line+=" "+formatter(swarm[j][n]);
					line+="\n";
					bw.write(line);
					line = null;
					s = null;
					line = new String();
					
					
					
					for (int n = 0; n < problemDimension; n++)
						pBest[j][n] = swarm[j][n];
					pBestFitness[j] = newFitness;
					
					ids[j] = newID;
					
					// best update
					if (newFitness < fBest)
					{
						fBest = newFitness;
						for (int n = 0; n < problemDimension; n++)
							gBest[n] = swarm[j][n];
						gBestID = newID;
						FT.add(i, fBest);
					}
				}
				
			}
			
		}
		
		closeAll();	
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRNGCounter.getCounter(), "correctionsPSO");
		
		
		finalBest = gBest;
		
		FT.add(i, fBest);
		return FT;
		
		
	}

}

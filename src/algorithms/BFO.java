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
import static utils.algorithms.Misc.cloneSolution;
import utils.MatLab;

import interfaces.Algorithm;
import utils.random.RandUtils;
import interfaces.Problem;
import utils.RunAndStore.FTrend;


/**
 * @author Fabio Caraffini (fabio.caraffini@gmal.com)
 * {@link www.tinyurl.com/FabioCaraffini}
 * 
 * Bacteria Foraging Optimisation
 * {@link http://downloads.hindawi.com/journals/aaa/2011/108269.pdf}
 * {@link http://downloads.hindawi.com/journals/ddns/2012/409478.pdf}
 */

public class BFO extends Algorithm
{
//	protected String vInitMethod = ""; 
//	protected String velocityUpdteStrategy = null;
//	protected String perturbationStrategy = null;
	
//	public BFO() {this.velocityUpdteStrategy = "classic"; this.perturbationStrategy = "classic";}
//	public BFO(String velocityUpdteStrategy) {this.velocityUpdteStrategy = velocityUpdteStrategy; this.perturbationStrategy = "classic";}
//	public BFO(String mut, String velocityUpdteStrategy, String perturbationStrategy) {{this.velocityUpdteStrategy = velocityUpdteStrategy; this.perturbationStrategy = perturbationStrategy;}}
//	
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
        int colonySize = getParameter("p0").intValue(); // 100;      //colony size; evenly divisible by 2      ---   in cBFO --> 300
        int chemotacticSteps = getParameter("p1").intValue(); //20;   //chemotactic steps, index = j
        int swimSteps = getParameter("p2").intValue(); //25;    //maximum swim steps     ---    cBFO --> 4
        int reproductiveSteps = getParameter("p3").intValue(); //28;   //reproduction steps, index = k
        int eliminationDispersalSteps = getParameter("p4").intValue(); //24;   //dispersal steps, index = l ('el')
        double dispersalProbability = getParameter("p5").doubleValue();//0.25; //probability of dispersal
        double runLengthUnit =  getParameter("p6").doubleValue();// 0.05; //basic bacteria movement increment size (for all bacteria) --- cBFO --> 0.1

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double[][] colony = new double[colonySize][problemDimension];
		double[] health = new double[colonySize];
		double[] best = new double[problemDimension];
		double[] fitness = new double[colonySize];
		double[] previousFitness = new double[colonySize];
//		double[] previousFeasibleSol;
		double fBest = Double.NaN;
//		
//		double newFitness = Double.NaN;
//		
//		double[] newInfeasibleX = new double[problemDimension];
		
		int i = 0;
		
	
	
		// initialize and evaluate a colony of bacteria
		for (int j = 0; j < colonySize; j++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				colony[j][n] = tmp[n];
			
			fitness[j] = problem.f(colony[j]); 
			
			previousFitness[j] = fitness[j];
			
			if (j == 0 || fitness[j] < fBest)
			{
				fBest = fitness[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = colony[j][n];
					FT.add(i, fBest);
			}
			
			i++;
		}


		// iterate
		while (i < maxEvaluations)
		{

			//Elimination-dispersal loop
			 for (int l = 0; l < eliminationDispersalSteps && i < maxEvaluations; l++) 
			 {
		          // reproduce-eliminate loop
		          for (int k = 0; k < reproductiveSteps && i < maxEvaluations; k++) 
		          {
		        	  // chemotactic loop; the lifespan of each bacterium
		        	  for (int j = 0; j <= chemotacticSteps && i < maxEvaluations; j++) 
		        	  {
		        		  // reset the health of each bacterium to 0.0
		        		  for (int b = 0; i < colonySize; b++)
		        			  health[b] = 0;
		
		                    //Substep
		                    // each bacterium
		                    for (int b = 0; b < colonySize; b++) 
		                    {
		                        // tumble (point in a new direction)
		                        double[] tumble = new double[problemDimension];
		                        
		                        for (int n = 0; n < problemDimension; n++) 
		                            tumble[n] = 2.0 * RandUtils.random() - 1.0;
		                        
		                        double norm = MatLab.norm2(tumble);
		          

//		                        previousFeasibleSol = cloneSolution(colony[b]);//to use with dismiss correction
		                        previousFitness[b] = fitness[b];
		                        
		                        // move in new direction (chemotactic)
		                        for (int n = 0; n < problemDimension; n++) 
		                            colony[b][n] += (runLengthUnit*tumble[n])/norm;
		                        

		                        // correct and evaluate new solution
		                        colony[b] = correct(colony[b], bounds);
		                        fitness[b] = problem.f(colony[b]);
		                        i++;
		               
		                        // update health
		                        health[b] += fitness[b]; 

		                        // update best
		                        if (fitness[b] < fBest) {
		                            fBest = fitness[b];
		                            best = cloneSolution(colony[b]);
		                            FT.add(i, fBest);
		                        }

		                        // swim 
		                        int m = 0;
		                        
		                        while (m < swimSteps && fitness[b] < previousFitness[b] && i < maxEvaluations) // we are improving
		                        {       
				                        
//		                        	 previousFeasibleSol = cloneSolution(colony[b]);//to use with dismiss correction
				                     previousFitness[b] = fitness[b];
				                     
		                            // keep moving in the same direction
		                            for (int n = 0; n < problemDimension; n++) 
			                            colony[b][n] += (runLengthUnit*tumble[n])/norm;	                  
				                     
		                            // correct and evaluate objective function
			                        colony[b] = correct(colony[b], bounds);
			                        fitness[b] = problem.f(colony[b]);
			                        i++;
			       

			                        // update best
			                        if (fitness[b] < fBest) {
			                            fBest = fitness[b];
			                            best = cloneSolution(colony[b]);
			                            FT.add(i, fBest);
			                        }

		                            
		                            m++;
		                            
		                        } // while improving
		                        
		                    } // process each each bacterium b in the chemotactic loop
		        	  
		               } // j, chemotactic loop

		              
		        	  // duplicate the healthiest half of bacteria, eliminate the other half
		               double[][] tempColony = new double[colonySize][problemDimension];
		               double[] tempFitness = new double[colonySize];
		               double[] tempPreviousFitness = new double[colonySize];
		               int half = colonySize/2;
		               
		               for (int b = 0; b< colonySize; b++) 
		               {
		            	   if(b<half)
		            	   {
		            		   int indexMin = MatLab.indexMin(health);
		            		   health[indexMin] = Double.MAX_VALUE;
		            		   tempColony[b] = cloneSolution(colony[indexMin]);
		            		   tempPreviousFitness[b] = previousFitness[indexMin];
		            		   tempFitness[b] = fitness[indexMin];
		            	   }
		            	   else
		            	   {
		            		   tempColony[b] = cloneSolution(colony[b-half]);
		            		   tempPreviousFitness[b] = previousFitness[b-half];
		            		   tempFitness[b] = fitness[b-half];
		            	   }
		               }
		               
		               previousFitness = tempPreviousFitness; tempPreviousFitness = null;
		               fitness = tempFitness; tempFitness = null;
		               colony = tempColony; tempColony = null;
		               
		            	   
		          } //k, reproduce-eliminate loop
		              
		             

		            // eliminate-disperse
		            for (int b = 0; b < colonySize && i < maxEvaluations; b++) 
		            {
		                
		                if (RandUtils.random() < dispersalProbability) 
		                {
		                    colony[b] = generateRandomSolution(problem);
		                    
		                    // evaluate
		                    previousFitness[b] = fitness[b];
		                    fitness[b] = problem.f(colony[b]);
		                    health[b] = 0;
		                    i++;

		                  
		                    // update best
	                        if (fitness[b] < fBest) {
	                            fBest = fitness[b];
	                            best = cloneSolution(colony[b]);
	                            FT.add(i, fBest);
	                        }


		                }
		            }
		        
		                
		                
		      }// l, elimination-dispersal loop, end processing           
		                    
			
		}
		
		  finalBest = best;
		
		  FT.add(i, fBest);
		  return FT;
		
		
	}

}

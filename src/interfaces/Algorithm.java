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


/** @file Algorithm.java
 *  
 * 
 *  This file contains the kernel main() function.
 *  @author Fabio Caraffini
*/
package interfaces;

import java.util.HashMap;
import java.util.Map;

import utils.RunAndStore.FTrend;
public abstract class Algorithm
{	
	private Map<String, Double> parameters = new HashMap<String, Double>();
	protected double[] initialSolution;
	protected double[] finalBest;
	protected double initialFitness;
	protected String ID = null;
	protected char correction = 't';

	/**
	 * This method executes the algorithm on a specified problem. 
	 * 
	 * @param problem the problem to solve.
	 * @param maxEvaluations the maximum number of fitness evaluations (FE).
	 * @return a FTrend object containing fitness trend and, in case, extra data.
	* @throws Exception The main method must be able to handle possible exceptions.
	 */
	public abstract FTrend execute(Problem problem, int maxEvaluations) throws Exception;
	
	/**
	 * This method sets the value of a given parameter.
	 * @param name the name of the parameter (ideally, following the notation p0, p1, p3,...)
	 * @param value the value of the parameter
	 */
	public void setParameter(String name, Double value){parameters.put(name, value);}
	/**
	 * This method gets the value of a given parameter.
	 * @param name the name of the parameter whose value must be returned
	 * @return A Double object
	 */
	public Double getParameter(String name){return parameters.get(name);}
	/**
	 * This method returns the best solution.
	 * @return finalBest the vector containing the final best solution produced by an optimisation algorithm
	 */
	public double[] getFinalBest(){return finalBest;}
	/**
	 * This method saves the final best solution.
	 * @param finalBest the vector containing the final best solution produced by an optimisation algorithm
	 */
	public void setFinalBest(double[] finalBest){this.finalBest = finalBest;}
	/**
	 * This method sets a specified initial guess.
	 * @param initialSolution the initial solution to be refined during the optimisation process
	 */
	public void setInitialSolution(double[] initialSolution){this.initialSolution = initialSolution;}
	/**
	 * This method sets the fitness value of the specified initial guess.
	 * @param initialFitness the fitness value of the first initial solution
	 */
	public void setInitialFitness(double initialFitness){this.initialFitness = initialFitness;}
	/**
	 * This method sets the value of the identifier string equal to the class name.
	 * 
	 */
	public void setID(){this.ID = this.getClass().getSimpleName();}
	/**
	 * This method allow to customise the value of the identifier with a preferred name.
	 * 
	 * @param name custom unique ID name for the algorithm.
	 * 
	 */
	public void setID(String name){this.ID = name;}
	/**
	 * Set the correction strategy.
	 * @param correction The correction strategy char identifier.
	 * 
	 */
	public void setCorrection(char correction){this.correction = correction;}
	/**
	 * Get the correction strategy.
	 * 
	 * @return  the correction strategy identifier.
	 * 
	 */
	public char getcorrection(){return this.correction;}
	/**
	 * This method returns the identifier ID
	 * 
	 * @return ID this value identifies the algorithm and is used to generate the result folder.
	 * 
	 */
	public String getID(){return this.ID;}
	/**
	 * This method return a String reporting the parameters setting.
	 * @return A String describing the parameter setting of an algorithm. 
	 */
	public String getParSetting()
	{
		String description = getID()+":";
		for(String name: this.parameters.keySet())
			description+="("+name+" "+getParameter(name)+")";
		return description;
	}
	
	
	
}

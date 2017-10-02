/** @file Algorithm.java
 *  
 *
 * BLASPHMEME: KIMEME HAS IT SHOULD BE.
 * A software platform for learning Computational Intelligence Optimisation
 * 
 * SCRIVICI QUEL CHE CAZZO TE PARE QUESTAA E@ LA DESCIZIONE PIU@ GENERICA CHE VA NELLA LISTA DEI FILES
 * LEGGI QUI https://www.cs.cmu.edu/~410/doc/doxygen.html#commands
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

	/**
	 * This method executes the algorithm on a specified problem. 
	 * 
	 * @param problem the problem to solve.
	 * @param maxEvaluations the maximum number of fitness evaluations (FE).
	 * @return a FTrend object containing fitness trend and, in case, extra data.
	 */
	public abstract FTrend execute(Problem problem, int maxEvaluations) throws Exception;
	
	/**
	 * This method sets the value of a given parameter.
	 */
	public void setParameter(String name, Double value){parameters.put(name, value);}
	/**
	 * This method gets the value of a given parameter.
	 */
	public Double getParameter(String name){return parameters.get(name);}
	/**
	 * This method returns the best solution.
	 */
	public double[] getFinalBest(){return finalBest;}
	/**
	 * This method saves the final best solution.
	 */
	public void setFinalBest(double[] finalBest){this.finalBest = finalBest;}
	/**
	 * This method sets a specified initial guess.
	 */
	public void setInitialSolution(double[] initialSolution){this.initialSolution = initialSolution;}
	/**
	 * This method sets the fitness value of the specified initial guess.
	 */
	public void setInitialFitness(double initialFitness){this.initialFitness = initialFitness;}
	/**
	 * This method sets the value of the idetifier string equal to the class name.
	 */
	public void setID(){this.ID = this.getClass().getSimpleName();}
	/**
	 * This method allow to customise the value of the idetifier with a preferred name.
	 * 
	 * @param name custom unique ID name for the algorithm.
	 * 
	 */
	public void setID(String name){this.ID = name;}
	/**
	 * This method returns the identifier ID
	 * 
	 * @return ID this value identifies the algorithm and is used to generate the result folder.
	 * 
	 */
	public String getID(){return this.ID;}
	/**
	 * This method return a String reporting the paramters setting.
	 */
	public String getParSetting()
	{
		String description = getID()+":";
		for(String name: this.parameters.keySet())
			description+="("+name+" "+getParameter(name)+")";
		return description;
	}
}

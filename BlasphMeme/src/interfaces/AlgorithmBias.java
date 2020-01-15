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
 * BLASPHMEME: KIMEME HAS IT SHOULD BE.
 * A software platform for learning Computational Intelligence Optimisation
 * 
 * SCRIVICI QUEL CHE CAZZO TE PARE QUESTAA E@ LA DESCIZIONE PIU@ GENERICA CHE VA NELLA LISTA DEI FILES
 * LEGGI QUI https://www.cs.cmu.edu/~410/doc/doxygen.html#commands
 *  This file contains the kernel main() function.
 *  @author Fabio Caraffini
*/
package interfaces;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static utils.RunAndStore.slash;
import static utils.algorithms.Misc.completeOneTailedNormal;
import static utils.algorithms.Misc.mirroring;
import static utils.algorithms.Misc.saturation;
import static utils.algorithms.Misc.toro;

import utils.RunAndStore.FTrend;
public abstract class AlgorithmBias
{	
	private Map<String, Double> parameters = new HashMap<String, Double>();
	protected double[] initialSolution;
	protected double[] finalBest;
	protected double initialFitness;
	protected String ID = null;
	
	protected int run;
	protected int numberOfCorrections = 0; 
	protected char correction;
	protected String Dir="."+slash();

	
	
	protected DecimalFormat DF = new DecimalFormat("0.00000000E00");

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
	 * This method saves the final best solution.
	 */
	public void setFinalBest(double[] finalBest){this.finalBest = finalBest;}
	/**
	 * This method returns the best solution.
	 */
	public double[] getFinalBest(){return finalBest;}
	/**
	 * This method sets a specified initial guess.
	 */
	public void setInitialSolution(double[] initialSolution){this.initialSolution = initialSolution;}
	/**
	 * This method sets the fitness value of the specified initial guess.
	 */
	public void setInitialFitness(double initialFitness){this.initialFitness = initialFitness;}
	/**
	 * This method sets the value of the identifier string equal to the class name.
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
	 * This method returns the identifier ID
	 * 
	 * @return ID this value identifies the algorithm and is used to generate the result folder.
	 * 
	 * 
	 */
	public String getID(){return ID;}
	/**
	 * This method sets the path of the directory for storing BIAS results.
	 */
	public void setDir(String Dir){this.Dir=Dir;}
	/**
	 * This method returns the path of the directory for storing BIAS results.
	 */
	public String getDir(){return this.Dir;}
	/**
	 * Store the "run" number.
	 * 
	 * @param run the number of the performed run.
	 * 
	 */
	public void setRun(int run){this.run = run;}
	/**
	 * Get the "run" number.
	 * 
	 * @return  the number of the performed run.
	 * 
	 */
	public int getRun(){return this.run;}
	
	/**
	 * Set the correction strategy.
	 * 
	 * @param run the number of the performed run.
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
	
	
	
	//**   UTILS METHODS   **//
	
	/**
	 * Generate the file "fileName".text containing 
	 */
	public void wrtiteCorrectionsPercentage(String algName, double percentage, String fileName) throws Exception
	{
		File f = new File(Dir+fileName+".txt");
		if(!f.exists()) 
			f.createNewFile();
		FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write(algName+" "+percentage+"\n");
		BW.close();
	}
	/**
	 * Generate the file "fileName".text containing 
	 */
	public void wrtiteCorrectionsPercentage(String algName, double percentage) throws Exception {wrtiteCorrectionsPercentage(algName, percentage, "corrections");}
	
	
	/**
	 *Fixes the scientific notation format 
	 */
	public String formatter(double value)
	{
		String str =""+value;
		str = this.DF.format(value).toLowerCase();
		if (!str.contains("e-"))  
			str = str.replace("e", "e+");
		return str;
	}
	
	
	/**
	 *Perform t, s and d corrections  (e=penalty must be performed separately)
	 *
	 *@param infeasible A point that might be infeasible 
	 *@param previousFeasiblePt The previous point that was surely feasible
	 *@param c The correction strategy
	 */
	public double[]  correct(double[] infeasiblePt, double[] previousFeasiblePt, double[][] bounds)
	{
		
		double[] output;
		
		if(this.correction == 't')
			output = toro(infeasiblePt, bounds);
		else if(this.correction== 's')
			output = saturation(infeasiblePt, bounds);
		else if(this.correction== 'd')
		{
			output = toro(infeasiblePt, bounds);
			if(!Arrays.equals(output, infeasiblePt)) 
				output = previousFeasiblePt;
		}
		else if(this.correction== 'm')
			output = mirroring(infeasiblePt, bounds);
		else if(this.correction== 'c')
			output = completeOneTailedNormal(infeasiblePt, bounds, 3.0);
		else
		{
			output = null;
			System.out.println("No valid bounds handling shceme seleceted");
		}

		if(!Arrays.equals(output, infeasiblePt))
		{
			infeasiblePt = output;
			output = null;
			this.numberOfCorrections++;
		}
	
		return infeasiblePt;
	}
	public double[]  correct(double[] infeasiblePt, double[] previousFeasiblePt, double[] bounds)
	{
		int n = infeasiblePt.length;
		double[][] BOUNDS = new double[n][2];
		for(int i=0; i<n; i++)
		{
			BOUNDS[i][1] = bounds[0];
			BOUNDS[i][1] = bounds[1];
		}	
		return correct(infeasiblePt, previousFeasiblePt, BOUNDS);
	}
	
}

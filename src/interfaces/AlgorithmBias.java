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
 * @author Fabio Caraffini (fabio.caraffini@gmail.com)
 *   
*/
package interfaces;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//import java.time.Instant;
import static java.time.Instant.now;
import static utils.RunAndStore.slash;
import static utils.RunAndStore.createFolder;
import static utils.RunAndStore.createPathOfFolders;
import static utils.algorithms.Misc.cloneSolution;
import static utils.algorithms.Corrections.mirroring;
import static utils.algorithms.Corrections.saturation;
import static utils.algorithms.Corrections.toro;
import static utils.algorithms.operators.ISBOp.completeOneTailedNormal;
import static utils.algorithms.operators.ISBOp.uniform;
import  utils.algorithms.ISBHelper;
import utils.algorithms.Counter;


import utils.RunAndStore.FTrend;
public abstract class AlgorithmBias
{	
	private Map<String, Double> parameters = new HashMap<String, Double>();
	/** standard Algorithms global variables **/
	protected double[] initialSolution;
	protected double[] finalBest;
	protected double initialFitness;
	protected int run;
	protected DecimalFormat DF = new DecimalFormat("0.00000000E00");
//	protected String ID = null;
	
	/**AlgorithmsBias global variables **/
	protected String ID = this.getClass().getSimpleName();
	protected int numberOfCorrections1 = 0; 
	protected int numberOfCorrections2 = 0; 
	protected int numberOfCorrections = 0; 
	protected char correction;
	protected String Dir="."+slash()+"ResultsISB"+slash();
	protected String minMaxProb = "min";
	protected int nonPositionColumns = 0;
//	protected long seed = System.currentTimeMillis();
	protected long seed = -666;
	
	/** AlgorithmsBias global variables for saving results **/
	protected File file = null;
	protected FileWriter fw = null;
	protected BufferedWriter bw = null;
//	private Instant timestamp = null;

//	protected String header = "SOS_suite hostname "+System.getProperty("user.name")+" v2 date "+String.format("%td/%<tm/%<ty", date )+" "; 
	protected String header = "# SOS_suite hostname "+System.getProperty("user.name")+" v2 ";
	

	/**
	 * This method executes the algorithm on a specified problem. 
	 * 
	 * @param problem the problem to solve.
	 * @param maxEvaluations the maximum number of fitness evaluations (FE).
	 * @return a FTrend object containing fitness trend and, in case, extra data.
	 * @throws Exception this methods must be able to handle exceptions 
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
	 * This method saves the final best solution.
	 * @param finalBest the vector containing the final best solution produced by an optimisation algorithm
	 */
	public void setFinalBest(double[] finalBest){this.finalBest = finalBest;}
	/**
	 * This method returns the best solution.
	 * @return finalBest the vector containing the final best solution produced by an optimisation algorithm
	 */
	public double[] getFinalBest(){return finalBest;}
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
	 */
	public void setID(){this.ID = this.getClass().getSimpleName();}
	/**
	 * This method allow to customise the value of the identifier with a preferred name.
	 * @param name custom unique ID name for the algorithm.
	 * 
	 */
	public void setID(String name){this.ID = name;}
	/**
	 * This method returns the identifier ID
	 * @return ID this value identifies the algorithm and is used to generate the result folder.
	 * 
	 */
	public String getID(){return ID;}
	/**
	 * This method sets the path of the directory for storing ISB results.
	 * @param Dir the string containing the path to the results folder.
	 */
//	public void setDir(String Dir){createFolder(this.Dir); this.Dir+=Dir;}
	public void setDir(String Dir){ this.Dir+=Dir; createPathOfFolders(this.Dir);}
	/**
	 * This method returns the path of the directory for storing BIAS results.
	 * @return Dir the string containing the path to the results folder
	 */
	public String getDir(){return this.Dir;}
	/**
	 * Store the "run" number.
	 * @param run the number of the performed run.
	 */
	public void setRun(int run){this.run = run;}
	/**
	 * Get the "run" number.
	 * @return  the number of the performed run.
	 */
	public int getRun(){return this.run;}
	/**
	 * Set the correction strategy.
	 * @param correction the char value identifier of the used correction strategy. 
	 */
	public void setCorrection(char correction){this.correction = correction;}
	/**
	 * Get the correction strategy.
	 * @return correction the correction strategy identifier.
	 * 
	 */
	public char getCorrection(){return this.correction;}
	/**
	 * Store the number of the first columns in the "finpos" ISB result files which are meant for saving details other than the coordinates of a solution 
	 *
	 *@param npc The required number of columns
	 */
	public void setNPC(int npc) {this.nonPositionColumns = npc;};
	/**
	 * Return the number of the first columns in the "finpos" ISB result files whih not used to store coordinate of a solution 
	 * @return npc The required number of non-position-columns
	 */
	public int getNPC() {return this.nonPositionColumns;};
	/**
	 * update the header to indicate that a maximisation process is taking place.
	 */
	public void maximisationProblem(){this.minMaxProb = "max";}
	
	/**
	 * Return the header for the ISB result file
	 */
	protected String getHeader(){return this.header;}
	/**
	 * Close all buffers
	 */
	protected void closeAll() throws Exception {this.bw.close();} 
	
	
	//**   UTILS METHODS   **//
	
	/**
	 * Generate the file "fileName".txt containing POIS
	 * @param algName the full name of the algorithm (as it has to appear in the correction txt file).
	 * @param percentage the percentage of corrected solutions.
	 * @param fileName the name of the txt file where results are stored. 
	 * @throws Exception this methods must be able to handle I/O exceptions.
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
	 * Generate the file "corrections.txt" containing POIS
	 * @param algName the full name of the algorithm (as it has to appear in the corrections.txt file).
	 * @param percentage the percentage of corrected solutions.
	 * @throws Exception this methods must be able to handle I/O exceptions.
	 */
	protected void wrtiteCorrectionsPercentage(String algName, double percentage) throws Exception {wrtiteCorrectionsPercentage(algName, percentage, "corrections");}
	

	/**
	 * Generate the file "fileName".text containing extended info
	 * @param algName the full name of the algorithm (as it has to appear in the corrections.txt file).
	 * @param percentage the percentage of corrected solutions.
	 * @param PRG the number of PRG activations during the optimisation process.
	 * @param extra a string containing all possible extra information to be added in the produced txt file (each space will results in a new column).
	 * @param fileName the name of the txt file where results are stored. 
	 * @throws Exception this methods must be able to handle I/O exceptions.
	 */
	public void writeStats(String algName, double percentage, int PRG, String extra, String fileName) throws Exception
	{
		// <output filename> <POIS value> <optionally: algorithm's parameters>  <seed value> <no of PRG calls>

		String tmp = algName+" "+percentage+" "+this.seed+" "+PRG;
		if(extra == null) tmp+="\n";
		else tmp+=" "+extra+"\n";
		
		File f = new File(Dir+fileName+".txt");
		if(!f.exists()) 
			f.createNewFile();
		FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write(tmp);
		BW.close();
	}
	/**
	 * Generate the file "fileName".text containing extended info
	 * @param algName the full name of the algorithm (as it has to appear in the corrections.txt file).
	 * @param percentage1 the percentage of corrected solutions after 1/3 of FEs.
	 * @param percentage2 the percentage of corrected solutions after 2/3 of FEs.
	 * @param percentage the percentage of corrected solutions.
	 * @param PRG the number of PRG activations during the optimisation process.
	 * @param extra a string containing all possible extra information to be added in the produced txt file (each space will results in a new column).
	 * @param fileName the name of the txt file where results are stored. 
	 * @throws Exception this methods must be able to handle I/O exceptions.
	 */
	public void writeStats(String algName, double percentage1, double percentage2, double percentage, int PRG, String extra, String fileName) throws Exception
	{
		// <output filename> <POIS value> <optionally: algorithm's parameters>  <seed value> <no of PRG calls>

		String tmp = algName+" "+percentage1+" "+percentage2+" "+percentage+" "+this.seed+" "+PRG;
		if(extra == null) tmp+="\n";
		else tmp+=" "+extra+"\n";
		
		File f = new File(Dir+fileName+".txt");
		if(!f.exists()) 
			f.createNewFile();
		FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write(tmp);
		BW.close();
	}
	/**
	 * Generate the file a txt file containing useful information, e.g. number of corrected solutions and PRG activations, on the performed optimisation process.
	 * @param algName the full name of the algorithm (as it has to appear in the correction txt file).
	 * @param percentage the percentage of corrected solutions.
	 * @param PRG the number of PRG activations during the performed run.
	 * @param fileName the name of the correction txt file where results are stored. 
	 * @throws Exception this methods must be able to handle I/O exceptions.
	 */
	public void writeStats(String algName, double percentage, int PRG, String fileName) throws Exception{ writeStats(algName, percentage, PRG, null,  fileName);}

	/**
	 * Generate the "corrections.txt" files containing useful information, e.g. number of corrected solutions and PRG activations, on the performed optimisation process.
	 * @param algName the full name of the algorithm (as it has to appear in the correction txt file).
	 * @param percentage the percentage of corrected solutions.
	 * @param PRG the number of PRG activations during the performed run.
	 * @throws Exception this methods must be able to handle I/O exceptions.
	 */
	protected void writeStats(String algName, double percentage, int PRG) throws Exception {writeStats(algName, percentage, PRG, "corrections");}
	
	/**
	 *Store the number of corrected solutions after every "period" FEs 
	 *@param period The period value.
	 *@param i The FE counter.
	 */
	protected void storeNumberOfCorrectedSolutions(int period, int i)
	{
		if(i==period) 
			numberOfCorrections1 = numberOfCorrections;
		if(i==2*period) 
			numberOfCorrections2 = numberOfCorrections; 
	}
	
	/**
	 *Fixes the scientific notation format 
	 *@param value the double number to format.
	 *@return A string containing the formatted version of the input number.
	 */
	protected String formatter(double value){return ISBHelper.formatter(value, this.DF);}
	
	
	
	public double[]  correct(double[] infeasiblePt, double[] previousFeasiblePt, double[] bounds, Counter PRNG)
	{
		int n = infeasiblePt.length;
		double[][] BOUNDS = new double[n][2];
		for(int i=0; i<n; i++)
		{
			BOUNDS[i][0] = bounds[0];
			BOUNDS[i][1] = bounds[1];
		}	
		return correct(infeasiblePt, previousFeasiblePt, BOUNDS, PRNG);
	}
	
	
	
	protected String getFullName(String name, Problem problem) {return name+"D"+problem.getDimension()+problem.getFID()+"-"+(this.run+1);}; 
	
	
	protected void createFile(String fullName) throws Exception
	{
		createFolder(Dir);
		
		file = new File(Dir+fullName+".txt");
		if (!file.exists()) 
			file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
	}
	
	protected void writeHeader(String parameters, Problem problem) throws Exception
	{  
		this.seed = System.currentTimeMillis();
		String line = this.header+"date "+now().toString()+" seed "+this.seed+" problem "+minMaxProb+" function "+problem.getFID()+" D"+problem.getDimension()+" algorithm "+this.ID+" parameters "+parameters+"\n";
		bw.write(line);
	}
	
	/**
	 * Write the coordinate values of an individual at the end of a line and terminate the line
	 *
	 *@param x A solution
	 *@param line A line previously filled with other details according to the pre-defined ISB notation
	 *@return A complete line, including the coordinates of the solution at hand, ready to be writte into a text file 
	 */
	protected String getCompleteLine(double[] x, String line) {return ISBHelper.addCoordinatesToEOL(x, line, this.DF);}

	/**
	 * Wrapper for the prepareInitialLine method of the class ISBHelper
	 */
	public String preparePopInitialationLines(int columns, int currentIndex, double currentFitness, int FECounter) {return ISBHelper.prepareInitialLine(columns, currentIndex, currentFitness, FECounter, this.DF);}
	
	
	/**
	 *Perform t, s and d corrections  (e=penalty must be performed separately)
	 *
	 *@param infeasiblePt A point that might be infeasible 
	 *@param previousFeasiblePt The previous point that was surely feasible
	 *@param bounds The boundaries of the optimisation problem.
	 *@param PRNG The PRNG activations counter
	 *@return The corrected (i.e. feasible) solution. 
	 */
	public double[]  correct(double[] infeasiblePt, double[] previousFeasiblePt, double[][] bounds, Counter PRNG)
	{
		
		double[] output; 
		double[] feasible; 
		
		if(this.correction == 't')
		{
			output = toro(infeasiblePt, bounds);
			feasible = cloneSolution(output);
		}
		else if(this.correction== 's')
		{
			output = saturation(infeasiblePt, bounds);
			feasible = cloneSolution(output);
		}
		else if(this.correction== 'd')
		{
			output = toro(infeasiblePt, bounds);
			if(!Arrays.equals(output, infeasiblePt)) 
				feasible = cloneSolution(previousFeasiblePt);
			else
				feasible = cloneSolution(previousFeasiblePt);
		}
		else if(this.correction == 'm')
		{
			output = mirroring(infeasiblePt, bounds);
			feasible = cloneSolution(output);
		}
		else if(this.correction== 'c')
		{
			output = completeOneTailedNormal(infeasiblePt, bounds, 3.0,PRNG);
			feasible = cloneSolution(output);
		}
		else if(this.correction== 'u')
		{
			// re-sampling with uniform distribution
			output = uniform(infeasiblePt, bounds, PRNG);
			feasible = cloneSolution(output);
		}
		else
		{
			output = null;
			feasible = null;
			System.out.println("No valid bounds handling scheme selected");
		}

	
		if(!Arrays.equals(output, infeasiblePt))
		{
//			infeasiblePt = output;
			output = null;
			this.numberOfCorrections++;
		}
	
		return feasible;
	}
	
	
	
}








/**
Copyright (c) 2019, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package utils.algorithms;




import java.text.DecimalFormat;


/**
 * Utility methods to help ISB studies
 *
 * @author Fabio Caraffini (fabio.caraffini@gmail.com)
 *
 */
public class ISBHelper {
	
	
	/**
	 *Fixes the scientific notation format 
	 *@param value the double number to format.
	 *@param DF Specifies the Decimal Format to use.
	 *@return A string containing the formatted version of the input number.
	 */
	public static String formatter(double value, DecimalFormat DF)
	{
		String str =""+value;
		str = DF.format(value).toLowerCase();
		if (!str.contains("e-"))  
			str = str.replace("e", "e+");
		return str;
	}
	
	
	
	/**
	 * Write the coordinate values of an individual at the end of a line and terminate the line
	 *
	 *@param x A solution
	 *@param line A line previously filled with other details according to the pre-defined ISB notation
	 *@param DF Specifies the Decimal Format to use.
	 *
	 *@return completeLine a complete line (ready to be written in a txt file)
	 */
	public static String addCoordinatesToEOL(double[] x, String line, DecimalFormat DF) 
	{
		String completeLine =""+line;
		int problemDimension = x.length;
		for(int n = 0; n < problemDimension; n++)
			completeLine+=" "+formatter(x[n], DF);
		completeLine+="\n";
		
		return completeLine;
	}
	
	/**
	 * Return a string containing the coordinate values of an individual followed by its corresponding fitness value (values are separated with a blank space " ").
	 *
	 *@param x A solution
	 *@param fx The fitness value of x.
	 *@param DF Specifies the Decimal Format to use.
	 *
	 *@return s A string containing the desired values
	 */
	public static String positionAndFitnessToString(double[] x, double fx, DecimalFormat DF) 
	{
		String s =""+formatter(x[0], DF);
		int problemDimension = x.length;
		for(int n = 1; n < problemDimension; n++)
			s+=" "+formatter(x[n], DF);
		s+=" "+formatter(fx, DF);
		
		return s;
	}

	/**
	 * Prepare the first part of the first line relative to each newly initialised individual for the ISB (finpos) result files (for DE).
	 * 
	 * The notation to follow is {ID of the new fitter individual} followed by {IDs of the individuals taking pat of the newly generate one (in this case these are all -1 since this methods is used during the initialisation of the population)} followed by {the fitness value of the newly generated individual} followed by {the fitness evaluation counter} followed by {the index of the individuals which got replaced by the new fitter one (in this case this is -1 has dueing the initialisation there are no previous individuals to replace)}
	 * As an example, this line for a DE\rand\1 algorithm would be implemented as line +=""+ids[j]+" -1 "+"-1 "+"-1 "+formatter(fitnesses[j])+" "+i+" -1";
	 *
	 * NB This method only prepare the initial part of the line which will be subsequently completed 
	 *@param mutationStrategy The employed DE mutation strategy (each strategy requires a different number of individual from the population to generate the mutant and then the offspring via crossover)
	 *@param FECounter The counter of the fitness function evaluations
	 *
	 *@return columns The number of columns required before attaching the coordinate of the newly generated individual 
	 *
	 */

	public static int getNumberOfNonPositionColumnsForDE(String mutationStrategy)
	{
		int columns;
		
		switch (mutationStrategy)
		{
			case "ro":
				// DE/rand/1
				columns = 7;
				break;
			case "ctbo":
				// DE/cur-to-best/1
				columns = 8;
				break;
			case "rt":
				// DE/rand/2
				columns = 9;
				break;
			case "ctro":
				// DE/current-to-rand/1
				columns = 8;
				break;
			case "rtbt":
				// DE/rand-to-best/2
				columns = 10;
				break;
			case "bo":
				// DE/best/1
				columns = 7;
				break;
			case "bt":
				// DE/best/2
				columns = 9;
				break;
			default:
				columns = -1;
				break;
		}	
		
		return columns;
	}
	
	/**
	 * Prepare the first part of the first line relative to each newly initialised individual for the ISB (finpos) result files (for PSO).
	 * 
	 * The notation to follow is {ID of the new fitter individual} followed by {IDs of the individuals taking pat of the newly generate one (in this case these are all -1 since this methods is used during the initialisation of the population)} followed by {the fitness value of the newly generated individual} followed by {the fitness evaluation counter} followed by {the index of the individuals which got replaced by the new fitter one (in this case this is -1 has dueing the initialisation there are no previous individuals to replace)}
	 * As an example, this line for a DE\rand\1 algorithm would be implemented as line +=""+ids[j]+" -1 "+"-1 "+"-1 "+formatter(fitnesses[j])+" "+i+" -1";
	 *
	 * NB This method only prepare the initial part of the line which will be subsequently completed 
	 *@param mutationStrategy The employed DE mutation strategy (each strategy requires a different number of individual from the population to generate the mutant and then the offspring via crossover)
	 *@param FECounter The counter of the fitness function evaluations
	 *
	 *@return columns The number of columns required before attaching the coordinate of the newly generated individual 
	 *
	 */

	public static int getNuberOfNonPositionColumnsForPSO(char velocityUpdteStrategy)
	{
		int columns;
		
		switch (velocityUpdteStrategy)
		{
			case 'o':
				columns = 6;
				break;
			case 'k':
				columns = 6;
				break;
			default:
				columns = -1;
				break;
		}	
		
		return columns;
	}
	
	/**
	 * Prepare the first part of the first line relative to each newly initialised individual for the ISB (finpos) result files.
	 * 
	 * The notation to follow is {ID of the new fitter individual} followed by {IDs of the individuals taking pat of the newly generate one (in this case these are all -1 since this methods is used during the initialisation of the population)} followed by {the fitness value of the newly generated individual} followed by {the fitness evaluation counter} followed by {the index of the individuals which got replaced by the new fitter one (in this case this is -1 has dueing the initialisation there are no previous individuals to replace)}
	 * As an example, this line for a DE\rand\1 algorithm would be implemented as line +=""+ids[j]+" -1 "+"-1 "+"-1 "+formatter(fitnesses[j])+" "+i+" -1";
	 *
	 * NB This method only prepare the initial part of the line which will be subsequently completed 
	 *@param columns the number of columns required for the part preceding the coordinate of the individual
	 *@param currentIndex The index of the newly generated individual (if fitter than its predecessor -- in this case it is always fitter as there is no predecessor yet)
	 *@param currentFitness The fitness value of the newly generated individual.
	 *@param FECounter The counter of the fitness function evaluations
	 *
	 *@return line The line containing the required amount of information
	 *
	 */
	public static String prepareInitialLine(int columns, int currentIndex, double currentFitness, int FECounter, DecimalFormat DF) 
	{
		String line =""+currentIndex+" ";
		
		for(int i=0; i < (columns-4); i++)
			line+="-1 ";
	
		return  line +=formatter(currentFitness, DF)+" "+FECounter+" -1";
	}
	
	
	
	/**
	 * Prepare the first part of each line in the violation files. NB These lines starts with the values of the counters recording how many time the corresponding design variable has been been generated outside of the search space.
	 *
	 *@return line The line containing the values of each CID.
	 *
	 */
	public static String CID2String(int[] CID) 
	{
		int n = CID.length;
		String line ="";
		for(int i=0; i < n; i++)
			line+=CID[i]+" ";
		return  line;
	}

	
	
	

}

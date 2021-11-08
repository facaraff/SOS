/** @file RunAndStore.java
 *  @author Fabio Caraffini
*/
package utils;
import java.io.File;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.DecimalFormat;
import org.apache.commons.lang3.SystemUtils;
import java.util.concurrent.Callable;
import java.util.regex.PatternSyntaxException;

import interfaces.Algorithm;
import java.util.Vector;
import interfaces.Problem;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import static utils.MatLab.mean;



/**
 * This class contains static methods for displaying information and 3 static classes for running the optimisation process through the available processors, collecting the results and save them.
*/
public class RunAndStore
{
	private static String resultsFolder = ".";
	private static DecimalFormat formatter = new DecimalFormat("0.000E00");
	private static MannWhitneyUTest mannWhitneyUTest;
		
	//--------------------------------------------------------------------------------------------------------------------------//
//	private static String acknowledge = "Acknowledgements:\n\n PAPERUNDERREVIEW";
//	public static String getAcknowledgement(){return acknowledge;}
	//---------------------------------------------------------------------------------------------------------------------------//
			
	
	
	
	
	
	/**
	 * This class contains the methods for running MULTITHREAD experiments.
	*/
	public static class AlgorithmRepetitionThread implements Callable<AlgorithmResult>
	{
		Algorithm algorithm;
		private Problem problem;
		private int repNr;
		private int budget;
		private boolean saveRowData;
		private boolean showElapsedTime = false;
		private String expFolder = ".";
		
		// TODO Binario? Database?
		/**
	     * This method sets the computational budget to be used ofr the optimisation process.
	     * 
	     * @param budget computationl budget.
	    */
		public void setBudget(int budget){this.budget = budget;}
		/**
	     * This method assigns a name to the experiment forlder.
	     * 
	     * @param folder name of the folder.
	    */
		public void setExpFolder(String folder){this.expFolder = folder;}
		/**
	     * This method returns the name of the enperiment folder.
	     * 
	     * @return expFolder experiment folder.
	    */
	    public String getExpFolder(){return this.expFolder;}
		/**
	     * This method specifies whther or not row data have to be stored.
	     * 
	     * @param saveRowData if true row data (fitness trends) are stored in files.
	    */
		public void saveRowData(boolean saveRowData){this.saveRowData = saveRowData;}
		/**
	     * This method shows the elapsed time.
	     * 
	     * @param showElapsedTime if true then the elapsed time is displayed.
	    */
		public void showElapsedTime(boolean showElapsedTime){this.showElapsedTime = showElapsedTime;}
		/**
	     * Constructor 1.
	     * 
	     * @param algorithm optimiser to be executed.
	     * @param problem problem to be solved.
	     * @param repNr run identifier.
	    */
		public AlgorithmRepetitionThread(Algorithm algorithm, Problem problem, int repNr)
		{
			this.algorithm = algorithm;
			this.problem = problem;
			this.repNr = repNr;
		}
		/**
	     * Constructor 2.
	     * 
	     * While working with real-world applications may require a customised computational budget, when dealing with benchmark problems is a common practice to use
	     * a fixed multipicative budget factor (usually = 5000 or 10000 fitness evaluations). Benchmark functions can be usually tested at different dimensionality values and the budget factor is used to allocate a budget proportional to the number of desing variables.
	     * 
	     * @param algorithm optimiser to be executed.
	     * @param problem problem to be solved.
	     * @param repNr run identifier.
	     * @param budgetFactor the computational budget will be automatically set up: budget = budgetFactor*(the dimensionality of the prpbem).
	     * @param saveRowData true to save row data.
	     * @param expFolder if saveData is set to true, results are stored in this folder within the results folder. It can be left equal to ".".
	    */
		public AlgorithmRepetitionThread(Algorithm algorithm, Problem problem, int repNr, int budgetFactor, boolean saveRowData, String expFolder)
		{
			this.algorithm = algorithm;
			this.problem = problem;
			this.repNr = repNr;
			this.budget = budgetFactor*problem.getDimension();
			this.saveRowData = saveRowData;
			this.expFolder = expFolder;
		}

		public AlgorithmResult call() throws Exception {
			double fbest = runAlgorithmRepetition(algorithm, problem, repNr);
			return new AlgorithmResult(fbest, repNr);
		}
		/**
	     * This method executes an optimiser.
	     * 
	     * @param algorithm optimiser to be executed.
	     * @param problem problem to be solved.
	     * @param runIndex run identifier.
	     * @return best solution.
	    */
		public double runAlgorithmRepetition(Algorithm algorithm, Problem problem, int runIndex) throws Exception
		{
			long t0, t1;

			t0 = System.currentTimeMillis();
			FTrend FT = algorithm.execute(problem, budget);
			t1 = System.currentTimeMillis();
			String slash = slash();
			if(saveRowData)
				saveTrend(FT, algorithm.getFinalBest(), resultsFolder+slash+expFolder+slash+algorithm.getID()+slash+getFullName(problem)+problem.getFID()+"-"+problem.getDimension()+slash+runIndex, true);
			int n = FT.size();
			if (showElapsedTime)
			{
				for (int j = n-1; j < n; j++)
					System.out.println(FT.toString(j));
				System.out.println("Elapsed time: " + (t1-t0) + " ms.");
			}
		return FT.getF(n-1);
		}
	}
	



	
	//----------------------------------//
	/**
	* Class for storing and saving the fitness trend.
	*/
	public static class FTrend
	{

	private Vector<Double> fValue;
	private Vector<Integer> index;
	
	private int extraInt;
	private double extraDouble;
	
	private Vector<Double> extraValues;
	/**
	* private boolean together: if true fitness and extra values are printed side by side in the same file
	*/
	private boolean together = false;
	/**
	* private int extraValuesColumns: if = 1 the extra values file is printed into a txt file displaying all extra values in one column vector, otherwise the latter column vector is reshaped in a matrix of 'extraValuesColumns is greater than 1' columns
	*/
	private int extraValuesColumns = 1;
	
	/**
	* Constructor I.
	*/
	public FTrend(){this.fValue = new Vector<>(); this.index =  new Vector<>(); this.extraValues =  null;}
	/**
	* Constructor II.
	*/
	public FTrend(boolean extra)
	{
		this.fValue = new Vector<>();
		this.index =  new Vector<>();
		if(extra)
			this.extraValues =  new Vector<>();
		else
			 this.extraValues =  null;
	}
	
	/**
	* Get the size of the FTrend object.
	*/
	public int getFTSize(){return this.fValue.size();}
	/**
	* Get the vector containing the fitness values.
	*/
	public Vector<Double> getF(){return this.fValue;}
	/**
	* Get the i-th fitness value.
	*/
	public double getF(int i){return this.fValue.get(i);}
	/**
	* Get the last fitness value.
	*/
	public double getLastF(){return this.fValue.get(this.fValue.size()-1);}//or equivalently {return this.fValue.lastElement;}
	/**
	* Get the vector containing the FE counter values.
	*/
	public Vector<Integer> getI(){return this.index;}
	/**
	* get the i-th FE counter value.
	*/
	public int getI(int i){return this.index.get(i);}
	/**
	* get the last FE counter value.
	*/
	public int getLastI(){return this.index.lastElement();}
	/**
	* set extraInt.
	* @param n
	*/
	public void setExtraInt(int n){this.extraInt=n;}
	/**
	* get extraInt.
	*/
	public int getExtraInt(){return this.extraInt;}
	/**
	* set extraDouble.
	* @param n
	*/
	public void setExtraDouble(double n){this.extraDouble=n;}
	/**
	* set extraValuesColumns.
	* @param n The value to be added.
	*/
	public void setExtraValuesColumns(int n){this.extraValuesColumns=n;}
	/**
	* set together.
	* @param together
	*/
	public void setTogether(boolean together){this.together=together;}
	/**
	* get extraDouble.
	*/
	public double getExtraDouble(){return this.extraDouble;}
	/**
	* Get the vector containing extra double values.
	*/
	public Vector<Double> getExtraValues(){return this.extraValues;}
	/**
	* Get the i-th extra double value.
	*/
	public double getExtraValue(int i){return this.extraValues.get(i);}
	/**
	* Get the last fitness value.
	*/
	public double getLastExtraValue(){return this.extraValues.get(this.extraValues.size()-1);}//or equivalently {return this.fValue.lastElement;}
	/**
	* add an extra value
	* 
	* @param extra The value to be added.
	*/
	public void addExtra(double extra){ this.extraValues.add(extra);}
	/**
	* check if the extra values vector is initialised (N.B. it could be empty!)
	* 
	*/
	public boolean hasExtraValues() { return extraValues!=null;}
	/**
	* check if fitness and extra values need to be saved side by side or in separate files
	* 
	*/
	public boolean separateFile() { return hasExtraValues() && !together;}
	/**
	* reshape extra (Double) values into a (String) matrix of n columns
	* 
	*/
	public String[] reshapeExtraValues(Vector<Double> extra, int n)
	{
		String s = "";
		for(int i=0; i<extra.size(); i++)
			s+=extra.get(i)+"\t";

		String[] array = s.split("\t");
		s = null; s = "";
		int counter = 0;
		for(String temp : array)
		{
			counter++;
			if(counter%n!=0)
				s+=temp+"\t";
			else
				s+=temp+"\n";
		}

		array = s.split("\n");
		
		return removeNaN(array);
	}
	/**
	* Remove NaN from double extra values (USED WHEN EXTRA VALUES ARE ADDED NEXT TO FITNESSES but there are less extra values that fitness values)
	* 
	*/
	protected String[] removeNaN(String[] s)
	{
		for(int i=0; i<s.length; i++)
			if(s[i].contains("NaN"))
				s[i]="";
		return s;
	}
	
	
	
	/**
	* add a new couple (i,f)
	* 
	* @param i i-th FE counter value.
	* @param f Fitness value at i-th FE.
	*/
	public void add(int i, double f){this.index.add(i); this.fValue.add(f);}
	/**
	* Check if index is empty
	* 
	* @return boolean value for index to be empty or non-empty.
	*/
	public boolean iIsEmpty(){return this.index.isEmpty();}
	/**
	* Merge two fitness trends.
	* 
	* Useful when an algorithms is run inside another. The fitness trend of the inner algorithm, i.e. FT, is appended to the bottom of the trend of the main algorithm.
	* The FE value is appropriately modified.
	* 
	* @param FT fitness trend to be included in the main trend.
	* @param startI last FE counter value of the main fitness trend.
	*/
//	public void merge(FTrend FT, int startI){for(int i = 0; i<this.index.size(); i++) add(startI+FT.getI(i), FT.getF(i));} CONTROLLARE!!!!! PER LORA LO RICONVERTO IN APPEND!! (mi sa che ho fatto bene , cancella questo e tieni append!!!)
	/**
	* Join two fitness trends.
	* 
	* Useful when an algorithms is run inside another. The fitness trend of the inner algorithm, i.e. FT, is appended to the bottom of the trend of the main algorithm.
	* The FE value is appropriately modified.
	* 
	* @param FT fitness trend to be included in the main trend.
	* @param startI last FE counter value of the main fitness trend.
	*/
	public void append(FTrend FT, int startI){for(int i = 0; i<FT.index.size(); i++) add(startI+FT.getI(i), FT.getF(i));}
	/**
	* Join two fitness trends so that the resulting one is monotone decreasing.
	* 
	* Useful when an algorithms is run inside another. The fitness trend of the inner algorithm, i.e. FT, is merged with in the trend of the main algorithm.
	* The FE value is appropriately modified.
	* 
	* @param FT fitness trend to be included in the main trend.
	* @param startI last FE counter value of the main fitness trend.
	*/
	public void merge(FTrend FT, int startI)
	{
		double currentBestFitness = this.getLastF();
		
		for(int i = 0; i<FT.index.size(); i++)
		{
			double currentFitness = FT.getF(i);

			if(currentFitness<currentBestFitness)
			{
//				System.out.println("starting index="+startI);
//				System.out.println("internal improvement index="+FT.getI(i));
//				System.out.println("added="+(startI+FT.getI(i)+1)+","+currentFitness);
				add(startI+FT.getI(i), currentFitness);
				currentBestFitness = currentFitness;
			}
		}
	}

	/**
	* Store the fitness trend and/or extra values into a string.
	* 
	* @return s fitness trend.
	*/
	public String toString()
	{
		String s = new String();
		
		if(hasExtraValues())
		{
			if(together)
			{
				try {
					//NB: in the algorithm you need to add as many extra value lines as fitness values... if not needed that many estradouble values that add Double.NaN!
					String[] extra = reshapeExtraValues(extraValues,extraValuesColumns);
					
					for(int i = 0; i<this.index.size()-1; i++)
						s+=this.index.get(i) + "\t" + fValue.get(i)+ "\t" +extra[i] + "\n";
					s+=this.index.get(this.index.size()-1) + "\t" + fValue.get(this.index.size()-1)+ "\t" +extra[this.index.size()-1];
					}
					catch(Exception e) {
					  System.out.println("NB: in the algorithm you need to add as many extra value lines as fitness values... if not needed that many estradouble values that add Double.NaN!");
					}
					
//
//				for(int i = 0; i<this.index.size()-1; i++)
//					s+=this.index.get(i) + "\t" + fValue.get(i)+ "\t" +extraValues.get(i) + "\n";
//				s+=this.index.get(this.index.size()-1) + "\t" + fValue.get(this.index.size()-1)+ "\t" +extraValues.get(this.index.size()-1);//THERE IS NO NEED TO SAVE THE LAST VALUE TWICE!!! CHECK!!!!! (FORSE PER NON ANDARE A CAPO?)

			}
			else
			{
				s = classicToString();
				
				s+="extras";
				
				String[] extra = reshapeExtraValues(extraValues,extraValuesColumns);
					
				for(int i = 0; i<extra.length-1; i++)
					s+=extra[i]+ "\n";
				s+=extra[extra.length-1];
				
//				for(int i = 0; i<this.extraValues.size()-1; i++)
//					s+=extraValues.get(i)+ "\n";
//				s+=extraValues.get(this.extraValues.size()-1);//THERE IS NO NEED TO SAVE THE LAST VALUE TWICE!!! CHECK!!!!! (FORSE PER NON ANDARE A CAPO? ci doveva essere un motivo)
			}
			
		}
		else
			s = classicToString();
			
		return s;
	}
	
	/**
	* Store the fitness trend into a string.
	* 
	* @return s fitness trend.
	*/
	public String classicToString()
	{
		String s = new String();
		for(int i = 0; i<this.index.size()-1; i++)
			s+=this.index.get(i) + "\t" + fValue.get(i) + "\n";
		s+=this.index.get(this.index.size()-1) + "\t" + fValue.get(this.index.size()-1);//THERE IS NO NEED TO SAVE THE LAST VALUE TWICE!!! CHECK!!!!!
		return s;
	}
	
	/**
	* Store the i-th line of the fitness trend into a String.
	* 
	* @return s String containing i-th FE coutner value and corredponding fitness values.
	*/
	public String toString(int i){return ""+this.index.get(i)+"\t"+format(this.fValue.get(i));}
	/**
	 * Return the number of fitness values in the fitness trend.
	 */
	 public int size(){return index.size();}
	
	/**
	* @todo vector containing extra info Vector<Double> extra.
	* @todo leave here the possibility of adding strings so that is possible to add extra info... in tis case duplicate the method add so that we can add those info al well and create a nother contructor.
	*/
	//	public String toString()
	//	{
	//		String retValue = i + "\t" + fBest;
	//		if (extras.size() > 0)
	//			retValue += "\t" + extras;
	//		return retValue;
	//	}
		
	}
	
	
//----------------------------------//
	
	/**
	 * This class helps handle results coming form the execution of a given optimiseer.
	*/
	public static class AlgorithmResult
	{
		public double fbest;
		public int repNr;
		/**
		* The constructor requires the solution (best) found during the corresponding run (repNr).
		*/
		public AlgorithmResult(double fbest, int repNr)
		{
			super();
			this.fbest = fbest;
			this.repNr = repNr;
		}
	}
	
	
	//----------------------------------//
	
   
	/**
	 * This method makes sure that the main results folder exists.
	*/
	public static void resultsFolder()
	{
		resultsFolder+=slash()+"results";
		createFolder(resultsFolder);
	}
	
	/**
	 * This method facilitates the creation of a generic folder.
	 * 
	 * @param s path/name of the folder.
	 * 
	*/
	public static void createFolder(String s)
	{
		try
		{
			File file = new File(s);
			if (!file.exists())
				file.mkdir();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method facilitates the creation of a generic path.
	 * 
	 * @param s path of nested folder.
	 * 
	*/
	public static void createPathOfFolders(String s)
	{
		String[] S = s.split(slash());
		String temp = resultsFolder;
	    for (int i=0; i < S.length; i++)
	    {
	    	if(!S[i].equals("."))
	    	{
		    	temp += (slash()+S[i]);
		    	createFolder(temp);
	    	}
	    }
	}
	
	/**
	 * This method creates a folder algorithm within the main result forlder.
	 * 
	 * @param s name of the folder.
	 * 
	*/
	public static void createRFolder(String s)
	{
		s = resultsFolder+slash()+s;
		try
		{
			File file = new File(s);
			if (!file.exists())
				file.mkdir();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method makes sure that the use of slash and backslash ina path is appropriate for the operating system.
	 * 
	 * @return slash contains a slash if the program is executed in a Unix systems, a backslach in case of Microsoft Windows.
	 * 
	*/
	public static String slash()
	{
		String slash = null;
		try {
			slash ="/";
			if(SystemUtils.IS_OS_WINDOWS)
				slash="\\\\";
		} catch (PatternSyntaxException ex) {
	        System.out.println("This string could not compile: "+ex.getPattern());
	        System.out.println(ex.getMessage());
	    }
		return slash;
	}
		
		
	/**
	 * This method writes results (fitness trend) either in txt or bin files.
	 * 
	 * @param FT contains the fitness trend (data).
	 * @param fileName name of the file
	 * @param saveAsText if true, data are stored in txt files, otherwise, in binary files.
	 * 
	*/
	public static void saveTrend(FTrend FT, double[] bestSolution, String fileName, boolean saveAsText)
	{
		try
		{
			// save results to file
			if (saveAsText)
			{
				
				
				FileWriter fileWriter = new FileWriter(fileName + ".txt");
				fileWriter.write("#");
				for(int i=0; i<bestSolution.length; i++)
					fileWriter.write(" "+format(bestSolution[i]));
				fileWriter.write("\n");
				
				String s = FT.toString();
				
				if(FT.separateFile())
				{
				   String[] parts = s.split("extras");
                   s = parts[0];
                   
                   FileWriter FWriter = new FileWriter(fileName+"-Extras" + ".txt");
                   FWriter.write(parts[1]);
                   FWriter.close();
                }
				
				fileWriter.write(s);
				fileWriter.close();
					
					
						
			}
			else // TO DO
			{
				DataOutputStream os = new DataOutputStream(new FileOutputStream(fileName + ".bin"));
				//fileWriter.write("#");
				for(int i=0; i<bestSolution.length; i++)
					//fileWriter.write(" "+format(Double.toString(bestSolution[i])));
				//fileWriter.write("\n"); DO THIS FOR BINARY FILES!!!
				//for (Best best : bests)
				//{
					//os.writeInt(best.getI());
					//os.writeDouble(best.getfBest());
				//}
				os.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	

	/**
	 * This method convert a double into a String making sure that the employed scientific notation format is kept.
	 * 
	 * @param value input double value to be converted.
	 * @return str outup String with appropriate format.
	*/

	public static String format(double value)
	{
        String str = formatter.format(value).toLowerCase();
        if (!str.contains("e-"))
            str = str.replace("e", "e+");
        return str;
	}
	
	/**
	 * This method print on screen a + or a - sign according to the outcome of the Wilcoxon test.
	 * 
	 * The test is performed by taking into consideration two realisations of the same optimisation process performed with two different optimisers: the reference and the comparison algorithm.
	 * The distribution of the final results, obtained by applying the reference for a fixed number of runs, is compared with that one obtained by applying the comparsion algorithm for the same amout of runs.
	 * 
	 * @param referenceValues array contating final solutions from multiple runs of the reference algorithm.
	 * @param comparisonValues array contating final solutions from multiple runs of the comparison algorithm.
	 * @param showPValue if true, the p-vlue is displayed as well.
	 * @param threshold for comparing the p-value (i.e. 1 - (confidence level), commoly equal to 1-0.95 = 0.05)
	*/
	public static void displayWilcoxon(double[] referenceValues, double[] comparisonValues, boolean showPValue, double threshold)
	{
		if(mannWhitneyUTest == null) mannWhitneyUTest= new MannWhitneyUTest();

		double pValue = mannWhitneyUTest.mannWhitneyUTest(referenceValues, comparisonValues);
		char w = '=';
		if (pValue < threshold)
		{
			if (mean(referenceValues) < mean(comparisonValues))
				w = '+';
			else
				w = '-';
		}
		System.out.print(w + "\t");
		if (showPValue)
			System.out.print(format(pValue) + "\t");
	}

	
	/**
	 * This method writes the content of a string into a txt file.
	 * 
	 * If the file already exists, the content is apended in the file, otherwise it is created.
	 * 
	 * @param name name/path of the txt file.
	 * @param content content string to be written into the file.
	*/
    public static void toText(String name, String content) throws Exception
    {
        File f = new File(name+".txt");
        if(!f.exists())
            f.createNewFile();
        FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
        BufferedWriter BW = new BufferedWriter(FW);
        BW.write(content);
        BW.close();
    }
    /**
	 * This method writes the content of a string into a txt file within the results folder.
	 * 
	 * If the file already exists, the content is apended in the file, otherwise it is created.
	 * 
	 * @param name name/path of the txt file.
	 * @param content content string to be written into the file.
	*/
    public static void toRText(String name, String content) throws Exception
    {	String slash = slash();
		toText(resultsFolder+slash+name, content);
        slash = null;
    }
     /**
	 * This returns a tring with the full name of a class.
	 * 
	 * @param o generica object instamce of a class.
	 * @return names full name of the class.
	*/
    public static String getFullName(Object o)
    {
		String name = o.getClass().getName();
		name = name.replace("$", ".");
		return name;
		
    }
     

}

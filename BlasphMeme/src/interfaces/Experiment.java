/** @file Experiment.java
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

import java.util.Vector;
import java.util.concurrent.CompletionService; ///< High-performance threading utility.
import java.util.concurrent.ExecutorCompletionService; ///< High-performance threading utility.
import java.util.concurrent.ExecutorService; ///< High-performance threading utility.
import java.util.concurrent.Executors; ///< High-performance threading utility.
import java.util.concurrent.Future; ///< High-performance threading utility.
import interfaces.Algorithm; ///< A generic optimisers .
import interfaces.Problem; ///< A generic problem.
import interfaces.Experiment;
import utils.RunAndStore; ///< Utilities for runnig algorithms and storing results.
import utils.RunAndStore.AlgorithmRepetitionThread; ///< Execure a single run in a thread.
import utils.RunAndStore.AlgorithmResult; ///< Utilities .
//TOGLI STI COMMENTI SOPRA TANTO NON SI VEDONO!!!!
import static utils.MatLab.std;
import static utils.MatLab.mean;

/**
 * Abstract class for experiments.
 */
public abstract class Experiment
{
	private int nrRuns = 100; ///< Number of desired runs for each optimiser.
	private int budgetFactor = 5000; ///< Budget factor. This coefficient is applied to the dimensionality of the problem to fix the computational budget.///<
	private int problemDimension; ///< Dimensionality if the problem.  
	private Vector<Algorithm> algorithms = new Vector<Algorithm>();// //!< List of optimisers
	private Vector<Problem> problems = new Vector<Problem>();//  //!< List of problems 
	private String expFolder = ".";
	private boolean saveRowData = true; ///< If true, solutions generated in ageneric run are stored in a separate file 
	private boolean showPValue = false; ///< If true, then p-value is displayed
	//private boolean showPValue = false; se lo vuoi impostare fai un meto startExperiment sulla classe che eredita perche' tanto capita raramente.. 
	private int nrProc = Runtime.getRuntime().availableProcessors();
	
	
	//CONSTRUCTORS
	/**
	 *Constructr 1.
	 * 
	 * Number of runs and budgect fuctor and other paramters are automatically set up to the default values. They can be modifed in a second moment by means of the provided setter methods.
	 * This constructor can be used for running experiments contaning problems with different dimensionality values.
	 * 
	 * @param s expName name for the folder where to store results.
	*/
	public Experiment(String expName){this.expFolder = expName;}//CONTROLL@A DOCUMENTAZIONE PER DISCORSO DIMKESIONALITA'
	/**
	 *Constructr 2.
	 * 
	 * Number of runs and budgect fuctor and other paramters are automatically set up to the default values. All the problems are tested at a fixed dimansionality value.
	 * This contructor can be usuefel while dealing with a complete benchmark suite. It is quite useful to test all the problems at low dimensions and, only in case, add more data by simply running the same experiment (same result follder/expName)
	 * but a different dimensionality for the problems.
	 * 
	 * @param problemDimension dimensionality value for all the  problems in the experiment.
	 * @param s expName name for the folder where to store results.
	*/
	public Experiment(int problemDimension, String expName){this.problemDimension = problemDimension; this.expFolder = expName;}
	/**
	 *Constructr 3.
	 * 
	 * @param problemDimension dimensionality value for all the  problems in the experiment. 
	 * @param budgetFactor set up the computational budget proportional to the dimensionality of the problem.
	 * @param s expName name for the folder where to store results.
	*/
	public Experiment(int problemDimension, int budgetFactor, String expName){this.problemDimension = problemDimension; this.budgetFactor = budgetFactor; this.expFolder += RunAndStore.slash()+expName;}
/**
	 *Constructr 4.
	 * 
	 * @param problemDimension dimensionality value for all the  problems in the experiment. 
	 * @param budgetFactor set up the computational budget proportional to the dimensionality of the problem.
	 * @param s expName name for the folder where to store results.
	 * @param saveRowData If true, solutions generated in ageneric run are stored in a separate file.
	 * @param showPValue If true, then p-value is displayed.
	*/
	public Experiment(int problemDimension, int budgetFactor, String expName, boolean saveRowData, boolean showPValue){this.problemDimension = problemDimension; this.budgetFactor = budgetFactor; this.expFolder += RunAndStore.slash()+expName; this.saveRowData = saveRowData; this.showPValue = showPValue;}
	
	/**
	 * This method has to be used in the case can be useful to display the p value from the Wilcoxon test on screen.
	 * 
	 * @param showPValue if true, the p value is dislayed.
	*/
	public void showPValue(boolean showPValue){this.showPValue = showPValue;}
	//GETTERS AND SETTERS
	/**
	 * This method sets the number of runs for the whole experiment.
	 * 
	 * @param nrRuns number of repetitions for each problem in the experiment.
	*/
	public void setNrRuns(int nrRuns){this.nrRuns = nrRuns;}
	/**
	 * This method sets the budeget factor.
	 * 
	 * @param budgetFactor budgent facotr for each problem in the experiment.
	*/
	public void setBudgetFactor(int budgetFactor){this.budgetFactor = budgetFactor;}
	/**
	 * This method sets the dimensionality for the problems in the experiment.
	 * 
	 * It is suggested to split up big experiments in multiple files/sub-experiments when multiple dimensionality values are used for the same bechnmark functions. In this case the dimenionality of the problem can be directly set up by means of Constructor 2, 3 or 4.
	 * This does not mean that larger experiment cannot be written within a unique file. The dimensionality of the problem can be changed via this method.
	 * 
	 * @param problemDimension dimensionality value for the problem.
	*/
	public void setProbemDimension(int problemDimension){this.problemDimension = problemDimension;}
	/**
	 * This method returns the number of runs.
	 * 
	 * @return nrRuns number for repetitions performed for each problem in the experiment.
	*/
	public int getNrRuns(){return this.nrRuns;}
	/**
	 * This method returns the budeget factor.
	 * 
	 * @return budgetFactor budgent facotr for each problem in the experiment.
	*/
	public int getBudegtFactor(){return this.budgetFactor;}
	/**
	 * This method returnd the dimensionality fof the problem.
	 * 
	 * It can be useful when the experiment include a single problem, or multiple anchmark problems tested at the same dimesionality value. 
	 * 
	 * @return problemDimension dimensionality value for the problem.
	*/
	public int getProblemDimension(){return this.problemDimension;}
	
	//HANDLE ALGORITHMS AND PROBLEMS
	/**
	 * This method returns the optimisation algorithms in the experiment.
	 * 
	 * @return algorithms vector of optimisers to be used in the experiment.
	*/
	public Vector<Algorithm> getAlgorithms(){return this.algorithms;}
	/**
	 * This method returns the problems in the experiment.
	 * 
	 * @return problems vector of problmes to be used in the experiment.
	*/
	public Vector<Problem> getProblems(){return this.problems;}
	/**
	 * This method adds a single problem to the experiment.
	 * 
	 * @param p problem to be added.
	*/
	public void add(Problem p)  {this.problems.add(p);}
	/**
	 * This method adds a single algorithm to the experiment.
	 * 
	 * @param a algorithm to be added.
	*/
	public void add(Algorithm a){this.algorithms.add(a);}
	/**
	 * This method returns the number of problems in the experiment.
	*/
	public int getNrProblems(){return this.problems.size();}
	/**
	 * This method returns the number of algorithms in the experiment.
	*/
	public int getNrAlgorithms(){return this.algorithms.size();}
	/**
	 * This method sets an ID to every algorithm in the experiment euqual to the Class name.
	 * 
	 * In case a customised ID has been previosly given to the algorith, it will not be altered.
	 * 
	*/
	public void setIDs(){for(Algorithm a : this.algorithms){if(a.getID()==null)a.setID();}}
	/**
	 * This method sets unique IDs to every algorithm in the experiment.
	 * 
	 * The ID would e euqual to the Class name, followed by a progressive number if the same class is used multiple times.
	 * In case a customised ID has been previosly given to the algorith, it will not be altered.
	 * 
	*/
	public void setUniqueIDs()
	{
		setIDs();
		Vector<Algorithm> temp = new Vector<Algorithm>();
		for(Algorithm a : this.algorithms){temp.add(a);}
		Algorithm a;
		while(temp.size()!=0)
		{
			a = temp.get(0);
			temp.remove(0);
			String name  = a.getID();
			int index = 0;
			for(Algorithm alg : temp)
			{
				if(name.equals(alg.getID()))
				{
					index++;
					alg.setID(alg.getClass().getSimpleName()+"-"+index);
				}
			}
		}
		temp = null;
	}
	/**
	 * This method prints on screen a short description of the experiment.
	 * 
	*/
	public void shortDescription()
	{
		System.out.println("This experiment contains "+getNrAlgorithms()+" optimisers and "+getNrProblems()+" problems with dimension value "+getProblemDimension());
		System.out.println("(Runs,"+getNrRuns()+" for each optimisation process, are spread over "+nrProc+" available processors)");
		System.out.println();
	}
	/**
	 * Display on screen the first row of the results table.
	 * 
	*/
	public void tableHeader()
	 {
		 String slash = RunAndStore.slash();
		 if(saveRowData)
			RunAndStore.createFolder(slash+expFolder);
		 int index = 0;
		 for (Algorithm algorithm : algorithms)
		 {
			 String algName = algorithm.getID();
			 if(saveRowData)
				RunAndStore.createRFolder(slash+expFolder+slash+algName);
				
			System.out.print("" + "\t");
			if (algName.length() >= 8)
				System.out.print(algName + "\t");
			else
				System.out.print(algName + "\t\t");

			if (index > 0)
			{
				System.out.print("\t" + "W");
				if (showPValue)
					System.out.print("\t" + "p-value" + "\t");
			}
			index++;
		}
		System.out.println();
	 }
	/**
	 * This method creates all the required folder needed to store results when saverowData is true.
	 * 
	 * It also write a report describing teh experiment.
	 * 
	*/
	public void createExperimentFolders() throws Exception
	 {
		  if(saveRowData)
		  {
			  String slash = RunAndStore.slash();
			  String catalog = "##################### "+getProblemDimension()+" D #####################\nNumber of runs: "+getNrRuns()+"\nAlgorithms: ";
			  String setting = "";
			  for(Algorithm a : algorithms){catalog+=a.getID()+" "; setting+=a.getParSetting()+"\n";} catalog+="\n"+setting+"Problems:\n"; setting = null;
			  for(Problem p : problems)catalog+=RunAndStore.getFullName(p)+p.getFID()+"\n"; catalog+="\n"; 
			RunAndStore.createRFolder(slash+expFolder);
			
			for (Algorithm algorithm : algorithms)
			{	
				String algName = algorithm.getID();
				RunAndStore.createRFolder(slash+expFolder+slash+algName);
				for(Problem p : problems)
					RunAndStore.createRFolder(slash+expFolder+slash+algName+slash+RunAndStore.getFullName(p)+p.getFID()+"-"+p.getDimension());

			
			}
			RunAndStore.toRText(expFolder, catalog);
		}
	}
	/**
	 * This method runs an experiment.
	 * 
	*/
	public void startExperiment() throws Exception
	{
		
		setUniqueIDs();
		shortDescription();
		tableHeader();
		createExperimentFolders();
		
		double[][] finalValues;
		
		ExecutorService threadPool = Executors.newFixedThreadPool(nrProc);
		CompletionService<AlgorithmResult> pool = new ExecutorCompletionService<AlgorithmResult>(threadPool);

		int problemIndex = 0;
		for (Problem problem: problems)
		{
			System.out.print("f" + (problemIndex+1) + "\t");

			finalValues = new double[algorithms.size()][nrRuns];
			int algorithmIndex = 0;
			for (Algorithm algorithm : algorithms)
			{ 
				for (int i = 0; i < nrRuns; i++) 
				{
					AlgorithmRepetitionThread thread = new AlgorithmRepetitionThread(algorithm, problem, i, budgetFactor, saveRowData, expFolder);
					pool.submit(thread);
				}
	
				for (int j = 0; j < nrRuns; j++)
				{
					Future<AlgorithmResult> result = pool.take();
					AlgorithmResult algorithmResult = result.get();
					finalValues[algorithmIndex][algorithmResult.repNr] = algorithmResult.fbest; //- bias[problemIndex];
				}

				String mean = RunAndStore.format(mean(finalValues[algorithmIndex]));
				String std = RunAndStore.format(std(finalValues[algorithmIndex]));
				System.out.print(mean + " \u00b1 " + std + "\t");
				if (algorithmIndex > 0) RunAndStore.displayWilcoxon(finalValues[0], finalValues[algorithmIndex], showPValue, 0.05);
				
				algorithmIndex++;
			}
			System.out.println();
			problemIndex++;
		}
		threadPool.shutdownNow();
		System.out.println();
	}
	
}








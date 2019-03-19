package benchmarks.problemsImplementation.COCO19;


/**
 * Benchmark Functions defined in the Real-Parameter Black-Box 
 * Optimization Benchmarking (BBOB 2019).
 */
public class BBOB2019TestFunctions 
{

	//TODO check biases for bbob2018!!!!!
	//public double[] bias = new double[] {79.48, -209.88, -462.09, -462.09, -9.21, 35.9, 92.94, 149.15, 123.83, -54.94, 76.27, -621.11, 29.97, -52.35, 1000.0, 71.35, -16.94, -16.94,-102.55, -546.5, 40.78, -1000.0, 6.87, 102.61};
	
	/**
	 * The problem to be optimized (needed in order to simplify the interface between the optimization
	 * algorithm and the COCO platform).
	 */
	public static CocoProblem PROBLEM;
	
	
	Suite suite; 
	Observer observer; 
	Benchmark benchmark = new Benchmark(suite, observer);

	
	/* Initialize the suite and observer.
     * For more details on how to change the default options, see
     * http://numbbo.github.io/coco-doc/C/#suite-parameters and
     * http://numbbo.github.io/coco-doc/C/#observer-parameters. */
	BBOB2019TestFunctions(int fNr, int dim) throws Exception
	{	
		suite = new Suite("bbob", "year: 2018", "dimensions: "+dim+" function_indices: "+fNr);
		observer =  new Observer("", "");
	}
	

    protected double[] evaluateFunctions(double[] x) {return PROBLEM.evaluateFunction(x);}
    public static double evaluateFunction(double[] x) {return PROBLEM.evaluateFunction(x)[0];}
	
    public static int getDimension() {return PROBLEM.getDimension();}
    
    public  double[] getLowerBounds() {return PROBLEM.getSmallestValuesOfInterest();}
    public  double[] getUpperBounds() {return PROBLEM.getLargestValuesOfInterest();}
    protected  static double getLowerBound(int i) {return PROBLEM.getSmallestValueOfInterest(i);}
    protected  static double getUpperBound(int i) {return PROBLEM.getLargestValueOfInterest(i);}
    public static double[][] getBounds()
    {
    	int dim = getDimension();
    	double[][] bounds = new double[dim][2];
		for (int i = 0; i < dim; i++)
		{
			bounds[i][0] = getLowerBound(i);
			bounds[i][1] = getUpperBound(i);
		}
		return bounds;
    }
    
	
  
		

	//benchmark.finalizeBenchmark();



	

	
	
}
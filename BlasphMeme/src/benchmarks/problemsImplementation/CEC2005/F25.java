package benchmarks.problemsImplementation.CEC2005;


import static utils.benchmarks.ProblemsTransformations.rotate;

public class F25 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Rotated Hybrid Composition Function 4 without bounds";
	static final private String DEFAULT_FILE_DATA = "supportData/hybrid_func4_data.txt";
	static final private String DEFAULT_FILE_MX_PREFIX = "supportData/hybrid_func4_M_D";
	static final private String DEFAULT_FILE_MX_SUFFIX = ".txt";

	// Number of functions
	static final private int NUM_FUNC = 10;

	private final MyHCJob theJob = new MyHCJob();

	// Shifted global optimum
	private final double[][] m_o;
	private final double[][][] m_M;
	private final double[] m_sigma = {
		2.0,	2.0,	2.0,	2.0,	2.0,
		2.0,	2.0,	2.0,	2.0,	2.0
	};
	private final double[] m_lambda = {
		10.0,		5.0/20.0,	1.0,	5.0/32.0,	1.0,
		5.0/100.0,	5.0/50.0,	1.0,	5.0/100.0,	5.0/100.0
	};
	private final double[] m_func_biases = {
		0.0,	100.0,	200.0,	300.0,	400.0,
		500.0,	600.0,	700.0,	800.0,	900.0
	};
	private final double[] m_testPoint;
	private final double[] m_testPointM;
	private final double[] m_fmax;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_w;
	private double[][] m_z;
	private double[][] m_zM;

	// Constructors
	public F25 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA, DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
	}
	public F25 (int dimension, String file_data, String file_m) {
		super(dimension, FUNCTION_NAME);
		
		setBias(25);
		this.bounds = new double[] {2, 5};
		

		// Note: dimension starts from 0
		m_o = new double[NUM_FUNC][dimension];
		m_M = new double[NUM_FUNC][dimension][dimension];

		m_testPoint = new double[dimension];
		m_testPointM = new double[dimension];
		m_fmax = new double[NUM_FUNC];

		m_w = new double[NUM_FUNC];
		m_z = new double[NUM_FUNC][dimension];
		m_zM = new double[NUM_FUNC][dimension];

		// Load the shifted global optimum
		loadFromFile(file_data, NUM_FUNC, dimension, m_o);
		// Load the matrix
		loadFromFile(file_m, NUM_FUNC, dimension, dimension, m_M);

		// Initialize the hybrid composition job object
		theJob.num_func = NUM_FUNC;
		theJob.num_dim = dimension;
		theJob.C = 2000.0;
		theJob.sigma = m_sigma;
		theJob.biases = m_func_biases;
		theJob.lambda = m_lambda;
		theJob.o = m_o;
		theJob.M = m_M;
		theJob.w = m_w;
		theJob.z = m_z;
		theJob.zM = m_zM;
		// Calculate/estimate the fmax for all the functions involved
		for (int i = 0 ; i < NUM_FUNC ; i ++) {
			for (int j = 0 ; j < dimension ; j ++) {
				m_testPoint[j] = (5.0 / m_lambda[i]);
			}
			rotate(m_testPointM, m_testPoint, m_M[i]);
			m_fmax[i] = Math.abs(theJob.basic_func(i, m_testPointM));
		}
		theJob.fmax = m_fmax;
	}

	private class MyHCJob extends HCJob {
		public double basic_func(int func_no, double[] x) {
			double result = 0.0;
			// This part is according to Matlab reference code
			switch(func_no) {
				case 0:
					result = weierstrass(x); 	//System.out.println("WEIER "+result);
					break;
				case 1:
					result = EScafferF6(x); 	//System.out.println("esc "+result);
					break;
				case 2:
					result = F8F2(x); 	//System.out.println("F8F2 "+result);
					break;
				case 3:
					result = ackley(x); 	//System.out.println("ackley "+result);
					break;
				case 4:
					result = rastrigin(x); 	//System.out.println("rastrigin "+result);
					break;
				case 5:
					result = griewank(x);	//System.out.println("greiw "+result);
					break;
				case 6:
					result = EScafferF6NonCont(x); 	//System.out.println("diomerda "+result);
					break;
				case 7:
					result = rastriginNonCont(x);	//System.out.println("rast non cont "+result);
					break;
				case 8:
					result = elliptic(x); 	//System.out.println("ellisse "+result);
					break;
				case 9:
					result = sphere_noise(x);	//System.out.println("sphere noise "+result);
					break;
				default:
					System.err.println("func_no is out of range.");
					System.exit(-1);
			}
			
			//System.out.println("job "+result);
			return (result);
			
		}
	}

	// Function body
	public double f(double[] x) {

		double result = 0.0;

		result = MyHCJob.hybrid_composition(x, theJob);

		result += bias;

		return (result);
	}
	
	
	
	
	

}

package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.shift;
import static utils.benchmarks.ProblemsTransformations.rotate;

public class F07 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Shifted Rotated Griewank's Function without Bounds";
	static final public String DEFAULT_FILE_DATA = "supportData/griewank_func_data.txt";
	static final public String DEFAULT_FILE_MX_PREFIX = "supportData/griewank_M_D";
	static final public String DEFAULT_FILE_MX_SUFFIX = ".txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_matrix;
	private double[] m_iSqrt = new double[MAX_SUPPORT_DIM];
	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;
	private double[] m_zM;

	// Constructors
	public F07 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA, DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
	}
	public F07 (int dimension,String file_data, String file_m) {
		super(dimension,  FUNCTION_NAME);
		
		setBias(7);
		this.bounds = new double[] {0, 600};
		
		for (int i = 0 ; i < MAX_SUPPORT_DIM ; i ++) {
			m_iSqrt[i] = Math.sqrt(((double )i) + 1.0);
		}
		
		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_matrix = new double[dimension][dimension];

		m_z = new double[dimension];
		m_zM = new double[dimension];

		// Load the shifted global optimum
		loadFromFile(file_data, dimension, m_o);
//		Benchmark.loadRowVectorFromFile(file_data, dimension, m_o);
		// Load the matrix
		loadFromFile(file_m, dimension, dimension, m_matrix);
//		Benchmark.loadMatrixFromFile(file_m, dimension, dimension, m_matrix);
	}

	// Function body
	public double f(double[] x) {

		double result = 0.0;

		shift(m_z, x, m_o);
		rotate(m_zM, m_z, m_matrix);

		result = griewank(m_zM);

		// XXX (gio) fixes -inf bug
		if (Double.isInfinite(result))
			result = Double.POSITIVE_INFINITY;
		
		result += bias;

		return (result);
	}
	
	// Griewank's function
		private double griewank(double[] x) {

			double sum = 0.0;
			double product = 1.0;

			for (int i = 0 ; i < x.length ; i ++) {
				sum += ((x[i] * x[i]) / 4000.0);
				product *= Math.cos(x[i] / m_iSqrt[i]);
			}

			return (sum - product + 1.0);
		}
	
}

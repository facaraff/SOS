package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.shift;
import static utils.benchmarks.ProblemsTransformations.rotate;

public class F03 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Shifted Rotated High Conditioned Elliptic Function";
	static final public String DEFAULT_FILE_DATA = "supportData/high_cond_elliptic_rot_data.txt";
	static final public String DEFAULT_FILE_MX_PREFIX = "supportData/Elliptic_M_D";
	static final public String DEFAULT_FILE_MX_SUFFIX = ".txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_matrix;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;
	private double[] m_zM;

	private	double constant;

	// Constructors
	public F03 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA, DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
	}
	public F03 (int dimension, String file_data, String file_m) {
		super(dimension, FUNCTION_NAME);

		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_matrix = new double[dimension][dimension];

		m_z = new double[dimension];
		m_zM = new double[dimension];
			
		setBias(3);
		
		// Load the shifted global optimum
		loadFromFile(file_data, dimension, m_o);
		// Load the matrix
		loadFromFile(file_m, dimension, dimension, m_matrix);
//		Benchmark.loadMatrixFromFile(file_m, dimension, dimension, m_matrix);

		constant = Math.pow(1.0e6, 1.0/(dimension-1.0));
	}

	// Function body
	public double f(double[] x) {
		double result = 0.0;

		shift(m_z, x, m_o);
		rotate(m_zM, m_z, m_matrix);

		double sum = 0.0;

		for (int i = 0 ; i < dimension ; i ++) {
			sum += Math.pow(constant, i) * m_zM[i] * m_zM[i];
		}

		result = sum + bias;

		return (result);
	}
}

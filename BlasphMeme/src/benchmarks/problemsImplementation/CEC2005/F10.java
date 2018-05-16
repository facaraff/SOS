package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.shift;
import static utils.benchmarks.ProblemsTransformations.rotate;

public class F10 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Shifted Rotated Rastrigin's Function";
	static final public String DEFAULT_FILE_DATA = "supportData/rastrigin_func_data.txt";
	static final public String DEFAULT_FILE_MX_PREFIX = "supportData/rastrigin_M_D";
	static final public String DEFAULT_FILE_MX_SUFFIX = ".txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_matrix;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;
	private double[] m_zM;

	// Constructors
	public F10 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA, DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
	}
	public F10 (int dimension,  String file_data, String file_m) {
		super(dimension, FUNCTION_NAME);
		
		setBias(10);
		this.bounds = new double[] {-5, 5};

		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_matrix = new double[dimension][dimension];

		m_z = new double[dimension];
		m_zM = new double[dimension];

		// Load the shifted global optimum
		loadFromFile(file_data, dimension, m_o);
		// Load the matrix
		loadFromFile(file_m, dimension, dimension, m_matrix);
	}

	// Function body
	public double f(double[] x) {

		double result = 0.0;

		shift(m_z, x, m_o);
		rotate(m_zM, m_z, m_matrix);

		result = Benchmark.rastrigin(m_zM);

		result += bias;

		return (result);
	}
}

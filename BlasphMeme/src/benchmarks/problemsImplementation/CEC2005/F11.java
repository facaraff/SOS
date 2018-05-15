package benchmarks.problemsImplementation.CEC2005;

public class F11 extends TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Shifted Rotated Weierstrass Function";
	static final public String DEFAULT_FILE_DATA = "supportData/weierstrass_data.txt";
	static final public String DEFAULT_FILE_MX_PREFIX = "supportData/weierstrass_M_D";
	static final public String DEFAULT_FILE_MX_SUFFIX = ".txt";

	static final public double PIx2 = Math.PI * 2.0;
	static final public int Kmax = 20;
	static final public double a = 0.5;
	static final public double b = 3.0;

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_matrix;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;
	private double[] m_zM;

	// Constructors
	public F11 (int dimension, double bias) {
		this(dimension, bias, DEFAULT_FILE_DATA, DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
	}
	public F11 (int dimension, double bias, String file_data, String file_m) {
		super(dimension, bias, FUNCTION_NAME);

		// Note: dimension starts from 0
		m_o = new double[m_dimension];
		m_matrix = new double[m_dimension][m_dimension];

		m_z = new double[m_dimension];
		m_zM = new double[m_dimension];

		// Load the shifted global optimum
		Benchmark.loadRowVectorFromFile(file_data, m_dimension, m_o);
		// Load the matrix
		Benchmark.loadMatrixFromFile(file_m, m_dimension, m_dimension, m_matrix);
	}

	// Function body
	public double f(double[] x) {

		double result = 0.0;

		Benchmark.shift(m_z, x, m_o);
		Benchmark.xA(m_zM, m_z, m_matrix);

		result = Benchmark.weierstrass(m_zM);

		result += m_bias;

		return (result);
	}
}

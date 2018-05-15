package benchmarks.problemsImplementation.cec2005;


public class F04 extends TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Shifted Schwefel's Problem 1.2 with Noise in Fitness";
	static final public String DEFAULT_FILE_DATA = "supportData/schwefel_102_data.txt";

	// Shifted global optimum
	private final double[] m_o;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;

	// Constructors
	public F04 (int dimension, double bias) {
		this(dimension, bias, DEFAULT_FILE_DATA);
	}
	public F04 (int dimension, double bias, String file_data) {
		super(dimension, bias, FUNCTION_NAME);

		// Note: dimension starts from 0
		m_o = new double[m_dimension];
		m_z = new double[m_dimension];

		// Load the shifted global optimum
		Benchmark.loadRowVectorFromFile(file_data, m_dimension, m_o);
	}

	// Function body
	public double f(double[] x) {
		double result = 0.0;

		Benchmark.shift(m_z, x, m_o);

		result = Benchmark.schwefel_102(m_z);

		// NOISE
		// Comment the next line to remove the noise
		result *= (1.0 + 0.4 * Math.abs(Benchmark.random.nextGaussian()));

		result += m_bias;

		return (result);
	}
}

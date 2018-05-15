package benchmarks.problemsImplementation.CEC2005;

public class F05 extends TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Schwefel's Problem 2.6 with Global Optimum on Bounds";
	static final public String DEFAULT_FILE_DATA = "supportData/schwefel_206_data.txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_A;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_B;
	private double[] m_z;

	// Constructors
	public F05 (int dimension, double bias) {
		this(dimension, bias, DEFAULT_FILE_DATA);
	}
	public F05 (int dimension, double bias, String file_data) {
		super(dimension, bias, FUNCTION_NAME);

		// Note: dimension starts from 0
		m_o = new double[m_dimension];
		m_A = new double[m_dimension][m_dimension];

		m_B = new double[m_dimension];
		m_z = new double[m_dimension];

		double[][] m_data = new double[m_dimension+1][m_dimension];

		// Load the shifted global optimum
		Benchmark.loadMatrixFromFile(file_data, m_dimension+1, m_dimension, m_data);
		for (int i = 0 ; i < m_dimension ; i ++) {
			if ((i+1) <= Math.ceil(m_dimension / 4.0))
				m_o[i] = -100.0;
			else if ((i+1) >= Math.floor((3.0 * m_dimension) / 4.0))
				m_o[i] = 100.0;
			else
				m_o[i] = m_data[0][i];
		}
		for (int i = 0 ; i < m_dimension ; i ++) {
			for (int j = 0 ; j < m_dimension ; j ++) {
				m_A[i][j] = m_data[i+1][j];
			}
		}
		Benchmark.Ax(m_B, m_A, m_o);
	}

	// Function body
	public double f(double[] x) {

		double max = Double.NEGATIVE_INFINITY;

		Benchmark.Ax(m_z, m_A, x);

		for (int i = 0 ; i < m_dimension ; i ++) {
			double temp = Math.abs(m_z[i] - m_B[i]);
			if (max < temp)
				max = temp;
		}
		
		// XXX (gio) fixes -inf bug
		if (Double.isInfinite(max))
			max = Double.POSITIVE_INFINITY;

		return (max + m_bias);
	}
}

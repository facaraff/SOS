package benchmarks.problemsImplementation.CEC2005;


public class F12 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Schwefel's Problem 2.13";
	static final private String DEFAULT_FILE_DATA = "supportData/schwefel_213_data.txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_a;
	private final double[][] m_b;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_A;
	private double[] m_B;

	// Constructors
	public F12 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA);
	}
	public F12 (int dimension, String file_data) {
		super(dimension, FUNCTION_NAME);
		
		setBias(12);
		this.bounds = new double[] {-Math.PI, Math.PI};

		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_a = new double[dimension][dimension];
		m_b = new double[dimension][dimension];

		m_A = new double[dimension];
		m_B = new double[dimension];

		// Data:
		//	1. a 		100x100
		//	2. b 		100x100
		//	3. alpha	1x100
		double[][] m_data = new double[100+100+1][dimension];

		// Load the shifted global optimum
		loadFromFile(file_data, m_data.length, dimension, m_data);
		for (int i = 0 ; i < dimension ; i ++) {
			for (int j = 0 ; j < dimension ; j ++) {
				m_a[i][j] = m_data[i][j];
				m_b[i][j] = m_data[100+i][j];
			}
			m_o[i] = m_data[100+100][i];
		}

		for (int i = 0 ; i < dimension ; i ++) {
			m_A[i] = 0.0;
			for (int j = 0 ; j < dimension ; j ++) {
				m_A[i] += (m_a[i][j] * Math.sin(m_o[j]) + m_b[i][j] * Math.cos(m_o[j]));
			}
		}
	}

	// Function body
	public double f(double[] x) {

		double sum = 0.0;

		for (int i = 0 ; i < dimension ; i ++) {
			m_B[i] = 0.0;
			for (int j = 0 ; j < dimension ; j ++) {
				m_B[i] += (m_a[i][j] * Math.sin(x[j]) + m_b[i][j] * Math.cos(x[j]));
			}

			double temp = m_A[i] - m_B[i];
			sum += (temp * temp);
		}

		return (sum + bias);
	}
}

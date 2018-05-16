package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.Ax;

public class F05 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Schwefel's Problem 2.6 with Global Optimum on Bounds";
	static final private String DEFAULT_FILE_DATA = "supportData/schwefel_206_data.txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_A;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_B;
	private double[] m_z;

	// Constructors
	public F05 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA);
	}
	public F05 (int dimension, String file_data) {
		super(dimension, FUNCTION_NAME);
		
		setBias(5);
		this.bounds = new double[] {-100, 100};
		
		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_A = new double[dimension][dimension];
		
		m_B = new double[dimension];
		m_z = new double[dimension];

		
		double[][] m_data = new double[dimension+1][dimension];

		// Load the shifted global optimum
//		Benchmark.loadMatrixFromFile(file_data, dimension+1, dimension, m_data);
		loadFromFile(file_data, dimension+1, dimension, m_data);
		for (int i = 0 ; i < dimension ; i ++) {
			if ((i+1) <= Math.ceil(dimension / 4.0))
				m_o[i] = -100.0;
			else if ((i+1) >= Math.floor((3.0 * dimension) / 4.0))
				m_o[i] = 100.0;
			else
				m_o[i] = m_data[0][i];
		}
		for (int i = 0 ; i < dimension ; i ++) {
			for (int j = 0 ; j < dimension ; j ++) {
				m_A[i][j] = m_data[i+1][j];
			}
		}
		Ax(m_B, m_A, m_o);
	}

	// Function body
	public double f(double[] x) {

		double max = Double.NEGATIVE_INFINITY;

		Ax(m_z, m_A, x);

		for (int i = 0 ; i < dimension ; i ++) {
			double temp = Math.abs(m_z[i] - m_B[i]);
			if (max < temp)
				max = temp;
		}
		
		// XXX (gio) fixes -inf bug
		if (Double.isInfinite(max))
			max = Double.POSITIVE_INFINITY;

		return (max + bias);
	}
}

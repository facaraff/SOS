package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.shift;
import static utils.benchmarks.ProblemsTransformations.rotate;

public class F08 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Shifted Rotated Ackley's Function with Global Optimum on Bounds";
	static final private String DEFAULT_FILE_DATA = "supportData/ackley_func_data.txt";
	static final private String DEFAULT_FILE_MX_PREFIX = "supportData/ackley_M_D";
	static final private String DEFAULT_FILE_MX_SUFFIX = ".txt";
	static final private double PIx2 = Math.PI * 2.0;
	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_matrix;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;
	private double[] m_zM;

	// Constructors
	public F08 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA, DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
	}
	public F08 (int dimension, String file_data, String file_m) {
		super(dimension, FUNCTION_NAME);

		setBias(8);
		this.bounds = new double[] {-32, 32};
		
		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_matrix = new double[dimension][dimension];

		m_z = new double[dimension];
		m_zM = new double[dimension];

		// Load the shifted global optimum
		//Benchmark.loadRowVectorFromFile(file_data, dimension, m_o);
		loadFromFile(file_data, dimension, m_o);
		// Load the matrix
		loadFromFile(file_data, dimension, m_o);
		//Benchmark.loadRowVectorFromFile(file_data, dimension, m_o);
		for (int i = 0 ; i < dimension ; i += 2) {
			m_o[i] = -32.0;
		}
	}

	// Function body
	public double f(double[] x) {

		double result = 0.0;

		shift(m_z, x, m_o);
		rotate(m_zM, m_z, m_matrix);

		result = ackley(m_zM);

		result += bias;

		return (result);
	}
	
	
	// Ackley's function
	private double ackley(double[] x) 
	{

		double sum1 = 0.0;
		double sum2 = 0.0;

		for (int i = 0 ; i < x.length ; i ++) 
		{
			sum1 += (x[i] * x[i]);
			sum2 += (Math.cos(PIx2 * x[i]));
		}
	
		return (-20.0 * Math.exp(-0.2 * Math.sqrt(sum1 / ((double )x.length))) - Math.exp(sum2 / ((double )x.length)) + 20.0 + Math.E);
	}
}

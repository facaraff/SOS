package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.shift;


public class F09 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Shifted Rastrigin's Function";
	static final private String DEFAULT_FILE_DATA = "supportData/rastrigin_func_data.txt";
	static final private double PIx2 = Math.PI * 2.0;
	// Shifted global optimum
	private final double[] m_o;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;

	// Constructors
	public F09 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA);
	}
	public F09 (int dimension, String file_data) {
		super(dimension, FUNCTION_NAME);
		
		setBias(9);
		this.bounds=new double[] {-5, 5};

		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_z = new double[dimension];

		// Load the shifted global optimum
		loadFromFile(file_data, dimension, m_o);
	}

	// Function body
	public double f(double[] x) {

		double result = 0.0;

		shift(m_z, x, m_o);

		result = rastrigin(m_z);

		result += bias;

		return (result);
	}
	
	// Rastrigin's function
	private double rastrigin(double[] x) {

		double sum = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += (x[i] * x[i]) - (10.0 * Math.cos(PIx2 * x[i])) + 10.0;
		}

		return (sum);
	}
	
}

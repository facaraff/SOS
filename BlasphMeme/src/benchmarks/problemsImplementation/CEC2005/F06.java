package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.shift;

public class F06 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Shifted Rosenbrock's Function";
	static final public String DEFAULT_FILE_DATA = "supportData/rosenbrock_func_data.txt";

	// Shifted global optimum
	private final double[] m_o;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;

	// Constructors
	public F06 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA);
	}
	public F06 (int dimension, String file_data) {
		super(dimension,  FUNCTION_NAME);
		
		setBias(6);	
		this.bounds = new double[] {-100, 100};
		
		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_z = new double[dimension];

		// Load the shifted global optimum
		loadFromFile(file_data, dimension, m_o);

		// z = x - o + 1 = x - (o - 1)
		// Do the "(o - 1)" part first
		for (int i = 0 ; i < dimension ; i ++) {
			m_o[i] -= 1.0;
		}
	}

	// Function body
	public double f(double[] x) {

		double result = 0.0;

		shift(m_z, x, m_o);

		result = rosenbrock(m_z);

		result += bias;

		return (result);
	}
	
	
	// Rosenbrock's function
		private double rosenbrock(double[] x) {

			double sum = 0.0;

			for (int i = 0 ; i < (x.length-1) ; i ++) {
				double temp1 = (x[i] * x[i]) - x[i+1];
				double temp2 = x[i] - 1.0;
				sum += (100.0 * temp1 * temp1) + (temp2 * temp2);
			}

			return (sum);
		}
	
}

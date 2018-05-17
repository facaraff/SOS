package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.shift;
import static utils.benchmarks.ProblemsTransformations.rotate;

public class F14 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Shifted Rotated Expanded Scaffer's F6 Function";
	static final private String DEFAULT_FILE_DATA = "supportData/E_ScafferF6_func_data.txt";
	static final private String DEFAULT_FILE_MX_PREFIX = "supportData/E_ScafferF6_M_D";
	static final private String DEFAULT_FILE_MX_SUFFIX = ".txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_matrix;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;
	private double[] m_zM;

	// Constructors
	public F14 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA, DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
	}
	public F14 (int dimension, String file_data, String file_m) {
		super(dimension, FUNCTION_NAME);
		
		setBias(14);
		this.bounds = new double[] {-100, 100};

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

		result = EScafferF6(m_zM);

		// XXX (gio) fixes -inf bug
		if (Double.isInfinite(result))
			result = Double.POSITIVE_INFINITY;
		
		result += bias;

		return (result);
	}
	
	
	// Expanded Scaffer's F6 function
		private double EScafferF6(double[] x) {

			double sum = 0.0;

			for (int i = 1 ; i < x.length ; i ++) {
				sum += ScafferF6(x[i-1], x[i]);
			}
			sum += ScafferF6(x[x.length-1], x[0]);

			return (sum);
		}
		
		// Scaffer's F6 function
		private double ScafferF6(double x, double y) {
			double temp1 = x*x + y*y;
			double temp2 = Math.sin(Math.sqrt(temp1));
			double temp3 = 1.0 + 0.001 * temp1;
			return (0.5 + ((temp2 * temp2 - 0.5)/(temp3 * temp3)));
		}
	
}

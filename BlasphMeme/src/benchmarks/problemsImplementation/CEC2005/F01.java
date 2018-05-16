package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.shift;

public class F01 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Shifted Sphere Function";
	static final public String DEFAULT_FILE_DATA = "supportData/sphere_func_data.txt";

	// Shifted global optimum
	private final double[] m_o;
	
	
	
	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;



	
	public F01 (int dimension) {this(dimension, DEFAULT_FILE_DATA);}
	
	public F01 (int dimension, String file_data) 
	{
		super(dimension, FUNCTION_NAME);
		
		this.bounds = new double[] {-100, 100};
		
		// Note: dimension starts from 0
		m_o = new double[this.dimension];
		m_z = new double[this.dimension];
		setBias(1);
		loadFromFile(file_data, this.dimension, this.m_o);//XXX fabio
	}
	

	
	
	

	// Function body
	public double f(double[] x) {
		double result = 0.0;

		shift(m_z, x, m_o);

		result = this.sphere(m_z);

		result += bias;

		return (result);
	}
	
	
	// Sphere function
	private double sphere(double[] x) {

		double sum = 0.0;

		for (int i = 0 ; i < x.length ; i ++) 
		{
			sum += x[i] * x[i];
		}

			return (sum);
	}

	
	
	
	
}

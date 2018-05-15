package benchmarks.problemsImplementation.cec2005;


public abstract class TestFunction {

	protected int		m_dimension;
	protected double	m_bias;

	protected String	m_func_name;

	// Constructors
	public TestFunction (int dimension, double bias) {
		this(dimension, bias, "undefined");
	}
	public TestFunction (int dimension, double bias, String func_name) {
		m_dimension = dimension;
		m_bias = bias;
		m_func_name = func_name;
	}

	// Function body to be defined in the child classes
	public abstract double f(double[] x);

	// Property functions common for all child classes
	public int dimension() {
		return (m_dimension);
	}

	public double bias() {
		return (m_bias);
	}

	public String name() {
		return (m_func_name);
	}
}

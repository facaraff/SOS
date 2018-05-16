package benchmarks.problemsImplementation.CEC2005;

import java.util.Scanner;

public abstract class CEC2005TestFunction {

	protected int		m_dimension;
	protected double	m_bias;

	protected String	m_func_name;

	// Constructors
	public CEC2005TestFunction (int dimension, double bias) {
		this(dimension, bias, "undefined");
		
	}
	public CEC2005TestFunction (int dimension, double bias, String func_name) {
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
	
	
	
	protected void loadLineFromFile(String file, int columns, double[] row) {
		try {
//			file = "cec2005"+slash()+file; 
			//System.out.println(this.getClass());
			System.out.println(file);
			System.out.println(this.getClass().getResourceAsStream(file));
			System.out.println(this.getClass());
			Scanner input = new Scanner(this.getClass().getResourceAsStream(file));
			
			for (int i=0;i<columns; i++)
			{
				String next = input.next(); //System.out.println(next+"\t");
				row[i]=Double.parseDouble(next);
			}
			
			input.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
}

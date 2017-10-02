package interfaces;

/**
 * Abstract class for problems.
 */
public abstract class Problem
{
	private int dimension;
	private double[][] bounds;
	private String FID = "";
	

	/**
	* Costructor for generic decision space scenario.
	* 
	* @param dimension the dimension of the problem.
	* @param bounds customised upper and lower bound for each design variable.
	*/
	public Problem(int dimension, double[][] bounds)
	{
		this.dimension = dimension;
		this.bounds = bounds;
	}
	/**
	* Costructor for hyper-parallelepiped decision space scenario.
	* 
	* @param dimension the dimension of the problem.
	* @param bounds makes use of the same upper and lower bound for all the design variables.
	*/
	public Problem(int dimension, double[] bounds)
	{
		this.dimension = dimension;
		this.bounds = new double[dimension][2];
		for (int i = 0; i < dimension; i++)
		{
			this.bounds[i][0] = bounds[0];
			this.bounds[i][1] = bounds[1];
		}
	}
	
	/**
	 * This method evaluates the fitness function.
	 * 
	 * @param x the decision variables array.
	 * @return the value of the fitness function.
	 */
	public abstract double f(double[] x) throws Exception;
	/**
	 * This method returns the dimension of the problem.
	 * 
	 * @return the dimension of the problem.
	 */
	public int getDimension(){return dimension;}
	/**
	 * This method returns the bounds of the search space.
	 * @return the bounds of the search space.
	 */
	public double[][] getBounds(){return bounds;}
	/**
	 * This method returns the function ID, in case the problem is from bechmark test suite.
	 * @return a string containing the function name/ID;
	 */
	public String getFID(){return this.FID;}
	
	/**
	 * This method allows to set up the function ID.
	 * @param FID function ID. 
	 */
	public void setFID(String FID){this.FID=FID;}
	
}

/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/

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
	* Constructor for generic decision space scenario.
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
	* Constructor for hyper-parallelepiped decision space scenario.
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
	* Constructor without boundariees (to be set up manually).
	* 
	* @param dimension the dimension of the problem.
	*/
	public Problem (int dimension){this.dimension = dimension;}
	

	
	
	/**
	 * This method evaluates the fitness function.
	 * 
	 * @param x the decision variables array.
	 * @return the value of the fitness function.
	 * @throws Exception This method must be able to handle potential exceptions.
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
	 * This method allows to stored the dimensionality of the problem passed as argument with the constructor.
	 * @param dimension Dimensionaity of the problem. 
	 */
	public void setDimension(int dimension){this.dimension=dimension;}
	/**
	 * This method sets problem-specific boundaries
	 * @param boundaries the bound of the problem.
	 */
	public void setBounds( double[][] boundaries){this.bounds=boundaries;}
	/**
	 * This method sets problem-specific boundaries (hyper-parallelepiped case)
	 * @param boundaries The boundaries of the optimisation problem.
	 */
	public void setBounds( double[] boundaries)
	{
		this.bounds = new double[dimension][2];
		for (int i = 0; i < dimension; i++)
		{
			this.bounds[i][0] = boundaries[0];
			this.bounds[i][1] = boundaries[1];
		}
	}
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

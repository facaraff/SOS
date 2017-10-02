/**
 * @CEC2015.java
 * @author  Fabio Caraffini <fabio.caraffini@dmu.ac.uk>
 * @version 1.0
 *
 * @section Description.
 * This class implements all the 15 functions forming the CEC2015 test suite.
 * The code is a modification of the original implementation by BO Zheng (email: zheng.b1988@gmail.com) Nov. 20th 2014.
 * The working principle and the implementation of rotations, base functions and other operator has been kept euql to the original version to guarantee the same behaviour, 
 * but a significant part of the code had been modified to make it compatable and runnable within this software platform.
 */
 
package benchmarks;

import interfaces.Problem;
import benchmarks.problemsImplementation.CEC2015TestFunc;
/**
 * CEC15 Test Function Suite for Single Objective Optimization.
 */
public class CEC2015 extends Problem
{ 
	CEC2015TestFunc testFunc=null;
	/**
	* Constructor.
	* Loads matrices from "./cec2015_files" folder.
	* Initialises private variables (arrays) for the specified problem to be used.
	* @param func_num index of the problem to be used (15 problems in this suite!).
	* @param dimension dimensionality of the problem (admissible vlues are: 10,30,50 and 100).
	*/

	public CEC2015(int dimension, int func_num) throws Exception
	{
		super(dimension, new double[] {-100, 100}); 
		setFID(".f"+func_num);
		
		testFunc = new CEC2015TestFunc(dimension,func_num);
	}
	
	
	public double f(double[] x){return testFunc.f(x);}
	
}

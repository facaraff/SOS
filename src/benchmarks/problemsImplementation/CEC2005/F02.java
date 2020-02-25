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
package benchmarks.problemsImplementation.CEC2005;

import static utils.benchmarks.ProblemsTransformations.shift;

public class F02 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Shifted Schwefel's Problem 1.2";
	static final public String DEFAULT_FILE_DATA = "supportData/schwefel_102_data.txt";

	// Shifted global optimum
	private final double[] m_o;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;

	

	
	
	
	// Constructors
	public F02 (int dimension) {
		this(dimension,  DEFAULT_FILE_DATA);
	}
	public F02 (int dimension, String file_data) {
		super(dimension, FUNCTION_NAME);

		this.bounds = new double[] {-100, 100};
		
		// Note: dimension starts from 0
		m_o = new double[this.dimension];
		m_z = new double[this.dimension];
		setBias(2);
		// Load the shifted global optimum
		loadFromFile(file_data, this.dimension, this.m_o);//XXX fabio
	}
	
	
	
	
	

	// Function body
	public double f(double[] x) {
		double result = 0.0;

		shift(m_z, x, m_o);

		result = this.schwefel_102(m_z);

		result += this.bias;

		return (result);
	}
	
	

	// Schwefel's problem 1.2
		 private double schwefel_102(double[] x) {

			double prev_sum, curr_sum, outer_sum;

			curr_sum = x[0];
			outer_sum = (curr_sum * curr_sum);

			for (int i = 1 ; i < x.length ; i ++) {
				prev_sum = curr_sum;
				curr_sum = prev_sum + x[i];
				outer_sum += (curr_sum * curr_sum);
			}

			return (outer_sum);
		}
	
	
}

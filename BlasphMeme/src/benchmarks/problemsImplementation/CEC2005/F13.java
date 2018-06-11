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


public class F13 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Shifted Expanded Griewank's plus Rosenbrock's Function";
	static final private String DEFAULT_FILE_DATA = "supportData/EF8F2_func_data.txt";

	// Shifted global optimum
	private final double[] m_o;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;

	// Constructors
	public F13 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA);
	}
	public F13 (int dimension,  String file_data) {
		super(dimension, FUNCTION_NAME);
		
		setBias(13);
		this.bounds = new double[] {-5, 5}; //XXX (gio) {-3,1} in Suganthan's code, {-5,5} in report

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

		result = F8F2(m_z);

		result += bias;

		return (result);
	}
	
	
	// F8F2
	 private double F8F2(double[] x) {

		double sum = 0.0;

		for (int i = 1 ; i < x.length ; i ++) {
			sum += F8(F2(x[i-1], x[i]));
		}
		sum += F8(F2(x[x.length-1], x[0]));

		return (sum);
	}
	
	// F2: Rosenbrock's Function -- 2D version
		private double F2(double x, double y) {
			double temp1 = (x * x) - y;
			double temp2 = x - 1.0;
			return ((100.0 * temp1 * temp1) + (temp2 * temp2));
		}



		// F8: Griewank's Function -- 1D version
		private double F8(double x) {
			return (((x * x) / 4000.0) - Math.cos(x) + 1.0);
		}
	
}

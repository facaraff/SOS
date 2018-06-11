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
import static utils.benchmarks.ProblemsTransformations.rotate;

public class F03 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Shifted Rotated High Conditioned Elliptic Function";
	static final public String DEFAULT_FILE_DATA = "supportData/high_cond_elliptic_rot_data.txt";
	static final public String DEFAULT_FILE_MX_PREFIX = "supportData/Elliptic_M_D";
	static final public String DEFAULT_FILE_MX_SUFFIX = ".txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_matrix;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;
	private double[] m_zM;

	private	double constant;

	// Constructors
	public F03 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA, DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
	}
	public F03 (int dimension, String file_data, String file_m) {
		super(dimension, FUNCTION_NAME);
		
		this.bounds = new double[] {-100, 100};
		
		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_matrix = new double[dimension][dimension];

		m_z = new double[dimension];
		m_zM = new double[dimension];
			
		setBias(3);
		
		// Load the shifted global optimum
		loadFromFile(file_data, dimension, m_o);
		// Load the matrix
		loadFromFile(file_m, dimension, dimension, m_matrix);
//		Benchmark.loadMatrixFromFile(file_m, dimension, dimension, m_matrix);

		constant = Math.pow(1.0e6, 1.0/(dimension-1.0));
	}

	// Function body
	public double f(double[] x) {
		double result = 0.0;

		shift(m_z, x, m_o);
		rotate(m_zM, m_z, m_matrix);

		double sum = 0.0;

		for (int i = 0 ; i < dimension ; i ++) {
			sum += Math.pow(constant, i) * m_zM[i] * m_zM[i];
		}

		result = sum + bias;

		return (result);
	}
}

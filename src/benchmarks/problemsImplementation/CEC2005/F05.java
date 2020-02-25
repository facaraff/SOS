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

import static utils.benchmarks.ProblemsTransformations.Ax;

public class F05 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Schwefel's Problem 2.6 with Global Optimum on Bounds";
	static final private String DEFAULT_FILE_DATA = "supportData/schwefel_206_data.txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_A;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_B;
	private double[] m_z;

	// Constructors
	public F05 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA);
	}
	public F05 (int dimension, String file_data) {
		super(dimension, FUNCTION_NAME);
		
		setBias(5);
		this.bounds = new double[] {-100, 100};
		
		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_A = new double[dimension][dimension];
		
		m_B = new double[dimension];
		m_z = new double[dimension];

		
		double[][] m_data = new double[dimension+1][dimension];

		// Load the shifted global optimum
//		Benchmark.loadMatrixFromFile(file_data, dimension+1, dimension, m_data);
		loadFromFile(file_data, dimension+1, dimension, m_data);
		for (int i = 0 ; i < dimension ; i ++) {
			if ((i+1) <= Math.ceil(dimension / 4.0))
				m_o[i] = -100.0;
			else if ((i+1) >= Math.floor((3.0 * dimension) / 4.0))
				m_o[i] = 100.0;
			else
				m_o[i] = m_data[0][i];
		}
		for (int i = 0 ; i < dimension ; i ++) {
			for (int j = 0 ; j < dimension ; j ++) {
				m_A[i][j] = m_data[i+1][j];
			}
		}
		Ax(m_B, m_A, m_o);
	}

	// Function body
	public double f(double[] x) {

		double max = Double.NEGATIVE_INFINITY;

		Ax(m_z, m_A, x);

		for (int i = 0 ; i < dimension ; i ++) {
			double temp = Math.abs(m_z[i] - m_B[i]);
			if (max < temp)
				max = temp;
		}
		
		// XXX (gio) fixes -inf bug
		if (Double.isInfinite(max))
			max = Double.POSITIVE_INFINITY;

		return (max + bias);
	}
}

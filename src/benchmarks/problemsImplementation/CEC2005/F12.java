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


public class F12 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Schwefel's Problem 2.13";
	static final private String DEFAULT_FILE_DATA = "supportData/schwefel_213_data.txt";

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_a;
	private final double[][] m_b;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_A;
	private double[] m_B;

	// Constructors
	public F12 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA);
	}
	public F12 (int dimension, String file_data) {
		super(dimension, FUNCTION_NAME);
		
		setBias(12);
		this.bounds = new double[] {-Math.PI, Math.PI};

		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_a = new double[dimension][dimension];
		m_b = new double[dimension][dimension];

		m_A = new double[dimension];
		m_B = new double[dimension];

		// Data:
		//	1. a 		100x100
		//	2. b 		100x100
		//	3. alpha	1x100
		double[][] m_data = new double[100+100+1][dimension];

		// Load the shifted global optimum
		loadFromFile(file_data, m_data.length, dimension, m_data);
		for (int i = 0 ; i < dimension ; i ++) {
			for (int j = 0 ; j < dimension ; j ++) {
				m_a[i][j] = m_data[i][j];
				m_b[i][j] = m_data[100+i][j];
			}
			m_o[i] = m_data[100+100][i];
		}

		for (int i = 0 ; i < dimension ; i ++) {
			m_A[i] = 0.0;
			for (int j = 0 ; j < dimension ; j ++) {
				m_A[i] += (m_a[i][j] * Math.sin(m_o[j]) + m_b[i][j] * Math.cos(m_o[j]));
			}
		}
	}

	// Function body
	public double f(double[] x) {

		double sum = 0.0;

		for (int i = 0 ; i < dimension ; i ++) {
			m_B[i] = 0.0;
			for (int j = 0 ; j < dimension ; j ++) {
				m_B[i] += (m_a[i][j] * Math.sin(x[j]) + m_b[i][j] * Math.cos(x[j]));
			}

			double temp = m_A[i] - m_B[i];
			sum += (temp * temp);
		}

		return (sum + bias);
	}
}

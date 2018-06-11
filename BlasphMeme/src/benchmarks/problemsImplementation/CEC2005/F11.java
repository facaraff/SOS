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
import static utils.benchmarks.ProblemsTransformations.xA; 

public class F11 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final private String FUNCTION_NAME = "Shifted Rotated Weierstrass Function";
	static final private String DEFAULT_FILE_DATA = "supportData/weierstrass_data.txt";
	static final private String DEFAULT_FILE_MX_PREFIX = "supportData/weierstrass_M_D";
	static final private String DEFAULT_FILE_MX_SUFFIX = ".txt";

	static final private double PIx2 = Math.PI * 2.0;
	static final private int Kmax = 20;
	static final private double a = 0.5;
	static final private double b = 3.0;

	// Shifted global optimum
	private final double[] m_o;
	private final double[][] m_matrix;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;
	private double[] m_zM;

	// Constructors
	public F11 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA, DEFAULT_FILE_MX_PREFIX + dimension + DEFAULT_FILE_MX_SUFFIX);
	}
	public F11 (int dimension, String file_data, String file_m) {
		super(dimension, FUNCTION_NAME);
		
		setBias(11);
		this.bounds = new double[] {-0.5, 0.5};

		// Note: dimension starts from 0
		m_o = new double[dimension];
		m_matrix = new double[dimension][dimension];

		m_z = new double[dimension];
		m_zM = new double[dimension];

		// Load the shifted global optimum
		loadFromFile(file_data, dimension, m_o);
		// Load the matrix
		loadFromFile(file_m, dimension, dimension, m_matrix);
	}

	// Function body
	public double f(double[] x) {

		double result = 0.0;

		shift(m_z, x, m_o);
		xA(m_zM, m_z, m_matrix);

		result = weierstrass(m_zM);

		result += bias;

		return (result);
	}
	
	
	// Weierstrass function
	private double weierstrass(double[] x) 
	{

		double sum1 = 0.0;
		for (int i = 0 ; i < x.length ; i ++) {
				for (int k = 0 ; k <= Kmax ; k ++) {
					sum1 += Math.pow(a, k) * Math.cos(PIx2 * Math.pow(b, k) * (x[i] + 0.5));
				}
			}

			double sum2 = 0.0;
			for (int k = 0 ; k <= Kmax ; k ++) {
				sum2 += Math.pow(a, k) * Math.cos(PIx2 * Math.pow(b, k) * (0.5));
			}

		return (sum1 - sum2*((double )(x.length)));
	}
	
	
}

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

import static utils.benchmarks.ProblemsTransformations.rotate;

public class F15 extends CEC2005TestFunction {

	// Fixed (class) parameters
	static final public String FUNCTION_NAME = "Hybrid Composition Function 1";
	static final public String DEFAULT_FILE_DATA = "supportData/hybrid_func1_data.txt";

	// Number of functions
	static final public int NUM_FUNC = 10;

	private final MyHCJob theJob = new MyHCJob();

	// Shifted global optimum
	private final double[][] m_o;
	private final double[][][] m_M;
	private final double[] m_sigma = {
		1.0,	1.0,	1.0,	1.0,	1.0,
		1.0,	1.0,	1.0,	1.0,	1.0
	};
	private final double[] m_lambda = {
		1.0,		1.0,		10.0,		10.0,
		5.0/60.0,	5.0/60.0,	5.0/32.0,	5.0/32.0,
		5.0/100.0,	5.0/100.0
	};
	private final double[] m_func_biases = {
		0.0,	100.0,	200.0,	300.0,	400.0,
		500.0,	600.0,	700.0,	800.0,	900.0
	};
	private final double[] m_testPoint;
	private final double[] m_testPointM;
	private final double[] m_fmax;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_w;
	private double[][] m_z;
	private double[][] m_zM;

	// Constructors
	public F15 (int dimension) {
		this(dimension, DEFAULT_FILE_DATA);
	}
	public F15 (int dimension,  String file_data) {
		super(dimension, FUNCTION_NAME);
		
		setBias(15);
		this.bounds = new double[] {-5, 5};

		// Note: dimension starts from 0
		m_o = new double[NUM_FUNC][dimension];
		m_M = new double[NUM_FUNC][dimension][dimension];

		m_testPoint = new double[dimension];
		m_testPointM = new double[dimension];
		m_fmax = new double[NUM_FUNC];

		m_w = new double[NUM_FUNC];
		m_z = new double[NUM_FUNC][dimension];
		m_zM = new double[NUM_FUNC][dimension];

		// Load the shifted global optimum
		loadFromFile(file_data, NUM_FUNC, dimension, m_o);
		// Generate identity matrices
		for (int i = 0 ; i < NUM_FUNC ; i ++) {
			for (int j = 0 ; j < dimension ; j ++) {
				for (int k = 0 ; k < dimension ; k ++) {
					m_M[i][j][k] = 0.0;
				}
			}
			for (int j = 0 ; j < dimension ; j ++) {
				m_M[i][j][j] = 1.0;
			}
		}

		// Initialize the hybrid composition job object
		theJob.num_func = NUM_FUNC;
		theJob.num_dim = dimension;
		theJob.C = 2000.0;
		theJob.sigma = m_sigma;
		theJob.biases = m_func_biases;
		theJob.lambda = m_lambda;
		theJob.o = m_o;
		theJob.M = m_M;
		theJob.w = m_w;
		theJob.z = m_z;
		theJob.zM = m_zM;
		// Calculate/estimate the fmax for all the functions involved
		for (int i = 0 ; i < NUM_FUNC ; i ++) {
			for (int j = 0 ; j < dimension ; j ++) {
				m_testPoint[j] = (5.0 / m_lambda[i]);
			}
			rotate(m_testPointM, m_testPoint, m_M[i]);
			m_fmax[i] = Math.abs(theJob.basic_func(i, m_testPointM));
		}
		theJob.fmax = m_fmax;
	}

	private class MyHCJob extends HCJob {
		public double basic_func(int func_no, double[] x) {
			double result = 0.0;
			switch(func_no) {
				case 0:
				case 1:
					result = rastrigin(x);
					break;
				case 2:
				case 3:
					result = weierstrass(x);
					break;
				case 4:
				case 5:
					result = griewank(x);
					break;
				case 6:
				case 7:
					result = ackley(x);
					break;
				case 8:
				case 9:
					result = sphere(x);
					break;
				default:
					System.err.println("func_no is out of range.");
					System.exit(-1);
			}
			return (result);
		}
	}

	// Function body
	public double f(double[] x) {

		double result = 0.0;

		result = MyHCJob.hybrid_composition(x, theJob);

		result += bias;

		return (result);
	}
}

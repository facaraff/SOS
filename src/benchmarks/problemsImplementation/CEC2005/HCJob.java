package benchmarks.problemsImplementation.CEC2005;
//
// Special Session on Real-Parameter Optimization at CEC-05
// Edinburgh, UK, 2-5 Sept. 2005
//
// Organizers:
//	Prof. Kalyanmoy Deb
//		deb@iitk.ac.in
//		http://www.iitk.ac.in/kangal/deb.htm
//	A/Prof. P. N. Suganthan
//		epnsugan@ntu.edu.sg
//		http://www.ntu.edu.sg/home/EPNSugan
//
// Java version of the test functions
//
// Matlab reference code
//	http://www.ntu.edu.sg/home/EPNSugan
//
// Java version developer:
//	Assistant Prof. Ying-ping Chen
//		Department of Computer Science
//		National Chiao Tung University
//		HsinChu City, Taiwan
//		ypchen@csie.nctu.edu.tw
//		http://www.csie.nctu.edu.tw/~ypchen/
//
// Typical use of the test functions in the benchmark:
//
//		// Create a benchmark object
// 		benchmark theBenchmark = new benchmark();
//		// Use the factory function call to create a test function object
//		//		test function 3 with 50 dimension
//		//		the object class is "test_func"
//		test_func aTestFunc = theBenchmark.testFunctionFactory(3, 50);
//		// Invoke the function with x
//		double result = aTestFunc.f(x);
//
// Version 0.90
//		Currently, this version cannot handle any numbers of dimensions.
//		It cannot generate the shifted global optima and rotation matrices
//		that are not provided with the Matlab reference code.
//		It can handle all cases whose data files are provided with
//		the Matlab reference code.
// Version 0.91
//		Revised according to the Matlab reference code and the PDF document
//		dated March 8, 2005.
//

import static utils.benchmarks.ProblemsTransformations.shift;
import static utils.benchmarks.ProblemsTransformations.rotate;
import  java.util.Random;


public abstract class HCJob {

	// Number of basic functions
	public int num_func;
	// Number of dimensions
	public int num_dim;

	// Predefined constant
	public double C;
	// Coverage range for each basic function
	public double[] sigma;
	// Biases for each basic function
	public double[] biases;
	// Stretch / compress each basic function
	public double[] lambda;
	// Estimated fmax
	public double[] fmax;
	// Shift global optimum for each basic function
	public double[][] o;
	// Linear transformation matrix for each basic function
	public double[][][] M;

	// Working areas to avoid memory allocation operations
	public double[] w;
	public double[][] z;
	public double[][] zM;

	
	
	static final private int MAX_SUPPORT_DIM = 100;
	static final private double PIx2 = Math.PI * 2.0;
	
	static final protected Random random = new Random();
	
   private static  double[] m_iSqrt = new double[MAX_SUPPORT_DIM];
	
	public HCJob() {
		initM_iSqrt();
	}

	public abstract double basic_func(int func_no, double[] x);
	
	
	private void initM_iSqrt()
	{
		for (int i = 0 ; i < MAX_SUPPORT_DIM ; i ++) 
			m_iSqrt[i] = Math.sqrt(((double )i) + 1.0);
	}	
	
	//
	// Basic functions
	//

	// Sphere function
	static protected double sphere(double[] x) {

		double sum = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += x[i] * x[i];
		}

		return (sum);
	}

	// Sphere function with noise
	static protected double sphere_noise(double[] x) {

		double sum = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += x[i] * x[i];
		}

		// NOISE
		// Comment the next line to remove the noise
		sum *= (1.0 + 0.1 * Math.abs(random.nextGaussian()));

		return (sum);
	}

	// Schwefel's problem 1.2
	static protected double schwefel_102(double[] x) {

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

	// Rosenbrock's function
	static protected double rosenbrock(double[] x) {

		double sum = 0.0;

		for (int i = 0 ; i < (x.length-1) ; i ++) {
			double temp1 = (x[i] * x[i]) - x[i+1];
			double temp2 = x[i] - 1.0;
			sum += (100.0 * temp1 * temp1) + (temp2 * temp2);
		}

		return (sum);
	}

	// F2: Rosenbrock's Function -- 2D version
	static protected double F2(double x, double y) {
		double temp1 = (x * x) - y;
		double temp2 = x - 1.0;
		return ((100.0 * temp1 * temp1) + (temp2 * temp2));
	}

	// Griewank's function
	static protected double griewank(double[] x) {
		
		double sum = 0.0;
		double product = 1.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += ((x[i] * x[i]) / 4000.0);
			product *= Math.cos(x[i] / m_iSqrt[i]);
		}

		return (sum - product + 1.0);
	}

	// F8: Griewank's Function -- 1D version
	static protected double F8(double x) {
		return (((x * x) / 4000.0) - Math.cos(x) + 1.0);
	}

	// Ackley's function
	static protected double ackley(double[] x) {

		double sum1 = 0.0;
		double sum2 = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum1 += (x[i] * x[i]);
			sum2 += (Math.cos(PIx2 * x[i]));
		}

		return (-20.0 * Math.exp(-0.2 * Math.sqrt(sum1 / ((double )x.length))) - Math.exp(sum2 / ((double )x.length)) + 20.0 + Math.E);
	}

	// Round function
	// 0. Use the Matlab version for rounding numbers
	static protected double myRound(double x) {
		return (Math.signum(x) * Math.round(Math.abs(x)));
	}
	// 1. "o" is provided
	static protected double myXRound(double x, double o) {
		return ((Math.abs(x - o) < 0.5) ? x : (myRound(2.0 * x) / 2.0));
	}
	// 2. "o" is not provided
	static protected double myXRound(double x) {
		return ((Math.abs(x) < 0.5) ? x : (myRound(2.0 * x) / 2.0));
	}

	// Rastrigin's function
	static protected double rastrigin(double[] x) {

		double sum = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += (x[i] * x[i]) - (10.0 * Math.cos(PIx2 * x[i])) + 10.0;
		}

		return (sum);
	}

	// Non-Continuous Rastrigin's function
	static protected double rastriginNonCont(double[] x) {

		double sum = 0.0;
		double currX;

		for (int i = 0 ; i < x.length ; i ++) {
			currX = myXRound(x[i]);
			sum += (currX * currX) - (10.0 * Math.cos(PIx2 * currX)) + 10.0;
		}

		return (sum);
	}

	// Weierstrass function
	static protected double weierstrass(double[] x) {
		return (weierstrass(x, 0.5, 3.0, 20));
	}

	static protected double weierstrass(double[] x, double a, double b, int Kmax) {

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

	// F8F2
	static protected double F8F2(double[] x) {

		double sum = 0.0;

		for (int i = 1 ; i < x.length ; i ++) {
			sum += F8(F2(x[i-1], x[i]));
		}
		sum += F8(F2(x[x.length-1], x[0]));

		return (sum);
	}

	// Scaffer's F6 function
	static protected double ScafferF6(double x, double y) {
		double temp1 = x*x + y*y;
		double temp2 = Math.sin(Math.sqrt(temp1));
		double temp3 = 1.0 + 0.001 * temp1;
		return (0.5 + ((temp2 * temp2 - 0.5)/(temp3 * temp3)));
	}

	// Expanded Scaffer's F6 function
	static protected double EScafferF6(double[] x) {

		double sum = 0.0;

		for (int i = 1 ; i < x.length ; i ++) {
			sum += ScafferF6(x[i-1], x[i]);
		}
		sum += ScafferF6(x[x.length-1], x[0]);

		return (sum);
	}

	// Non-Continuous Expanded Scaffer's F6 function
	static protected double EScafferF6NonCont(double[] x) {

		double sum = 0.0;
		double prevX, currX;

		currX = myXRound(x[0]);
		for (int i = 1 ; i < x.length ; i ++) {
			prevX = currX;
			currX = myXRound(x[i]);
			sum += ScafferF6(prevX, currX);
		}
		prevX = currX;
		currX = myXRound(x[0]);
		sum += ScafferF6(prevX, currX);

		return (sum);
	}

	// Elliptic
	static protected double elliptic(double[] x) {

		double sum = 0.0;
		double a = 1e6;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += Math.pow(a, (((double )i)/((double )(x.length-1)))) * x[i] * x[i];
		}

		return (sum);
	}
	
	// Hybrid composition
		static protected double hybrid_composition(double[] x, HCJob job) {

			int num_func = job.num_func;
			int num_dim = job.num_dim;

			// Get the raw weights
			double wMax = Double.NEGATIVE_INFINITY;
			for (int i = 0 ; i < num_func ; i ++) {
				double sumSqr = 0.0;
				shift(job.z[i], x, job.o[i]);
				for (int j = 0 ; j < num_dim ; j ++) {
					sumSqr += (job.z[i][j] * job.z[i][j]);
				}
				job.w[i] = Math.exp(-1.0 * sumSqr / (2.0 * num_dim * job.sigma[i] * job.sigma[i]));
				if (wMax < job.w[i])
					wMax = job.w[i];
			}

			// Modify the weights
			double wSum = 0.0;
			double w1mMaxPow = 1.0 - Math.pow(wMax, 10.0);
			for (int i = 0 ; i < num_func ; i ++) {
				if (job.w[i] != wMax) {
					job.w[i] *= w1mMaxPow;
				}
				wSum += job.w[i];
			}

			// Normalize the weights
			for (int i = 0 ; i < num_func ; i ++) {
				job.w[i] /= wSum;
			}

			double sumF = 0.0;
			for (int i = 0 ; i < num_func ; i ++) {
				for (int j = 0 ; j < num_dim ; j ++) {
					job.z[i][j] /= job.lambda[i];
				}
				rotate(job.zM[i], job.z[i], job.M[i]);
				sumF +=
					job.w[i] *
					(
						job.C * job.basic_func(i, job.zM[i]) / job.fmax[i] +
							job.biases[i]
					);
			}
			return (sumF);
		}
}

package mains.test;

import utils.linearAlgebra.EigenValueVector;
import utils.linearAlgebra.Matrix;
import utils.linearAlgebra.powerMethods.PowerIteration;
import utils.linearAlgebra.powerMethods.RegularIteration;


 class PowerIterationTest {

	public static void main(String[] args) {
		
		//Error rate
	    final double error = 0.001;
		
		
//		double[] initialEigenVector = new double[] {1,1,1};
//		double[][] initialMatrix = new double[][] { {5,3,-2},
//													{3,4,4},
//													{-2,4,6} };  
													
	    double[] initialEigenVector = new double[] {1,1};
//			double[][] initialMatrix = new double[][] { {2.2646674511543947E-30,4.812190845729975E-31},
//			{4.812190845729975E-31,3.608093979307155E-30}}; 
															
	    double[][] initialMatrix = new double[][] { {2.26467,   0.48122},
			{0.48122,   3.60809}}; 							
		
		Matrix m = new Matrix(initialMatrix);									
															
		PowerIteration method = new RegularIteration();
			
		/*Inverse Iteration */
		//PowerIteration method = new InverseIteration();
			
		/*Shifted Iteration, you can choose the shifted rate at the class */
		//PowerIteration method = new ShiftedIteration();
			
		//Call to solve method
		EigenValueVector result = method.solve(m, initialEigenVector, error);
		method.printCommandLine(result);
		
		result = method.solve(m, initialEigenVector, 10);
		method.printCommandLine(result);
	}

}

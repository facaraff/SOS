package mains.test;

//import utils.powerIteration.methods.InverseIteration;
import utils.powerIteration.methods.PowerIteration;
import utils.powerIteration.methods.RegularIteration;
//import utils.powerIteration.methods.ShiftedIteration;
import utils.powerIteration.EigenValueVector;
import utils.powerIteration.Matrix;


 class PowerIterationTest {

	public static void main(String[] args) {
		
		//Error rate
	    final double error = 0.001;
		
		
		double[] initialEigenVector = new double[] {1,1,1};
		double[][] initialMatrix = new double[][] { {5,3,-2},
													{3,4,4},
													{-2,4,6} };  
		Matrix m = new Matrix(initialMatrix);									
															
		PowerIteration method = new RegularIteration();
			
		/*Inverse Iteration */
		//PowerIteration method = new InverseIteration();
			
		/*Shifted Iteration, you can choose the shifted rate at the class */
		//PowerIteration method = new ShiftedIteration();
			
		//Call to solve method
		EigenValueVector result = method.solve(m, initialEigenVector, error);
		method.printCommandLine(result);
	}

}

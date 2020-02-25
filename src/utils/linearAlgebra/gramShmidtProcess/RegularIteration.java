package utils.linearAlgebra.gramShmidtProcess;

import utils.linearAlgebra.EigenValueVector;
import utils.linearAlgebra.Matrix;

/**
 * Regular Iteration find the largest EigenValue.
 * 
 * Algorithm: 
 * 1) Normalize x0 = V/|V| 
 * 2) Yi = M*Xi-1 
 * 3) Xi = Yi/|Yi| 
 * 4) li = * (T(Xi) * M * Xi) / (T(Xi) * Xi Repeat 2 and 4 while li - li-1 < E 
 */
public class RegularIteration implements PowerIteration {

	@Override
	public EigenValueVector solve(Matrix matrix, double[] initialEigenVector, double error) 
	{

		double lastEigenValue;
		double newEigenValue = 0;
		int i = 0;

		// Step 1:
		
		Matrix initialMatrixVector = Matrix.vectorToMatrix(initialEigenVector);
		Matrix eigenVector = Matrix.normalize(initialMatrixVector);
		do {
			lastEigenValue = newEigenValue;
			// Step 2:
			Matrix vetorY = matrix.times(eigenVector);
			// Step 3:
			eigenVector = Matrix.normalize(vetorY);
			// Step 4:
			Matrix matrixTranspostaAvetor = eigenVector.transpose();
			double avalorNumerador = matrixTranspostaAvetor.times(matrix)
					.times(eigenVector).get(0, 0);
			double avalorDenominador = matrixTranspostaAvetor.times(eigenVector)
					.get(0, 0);
			newEigenValue = avalorNumerador / avalorDenominador;
			i++;
		} while (i == 1 || Math.abs(newEigenValue - lastEigenValue) > error);

		EigenValueVector eigenValueVector = new EigenValueVector();
		eigenValueVector.eigenValue = newEigenValue;
		eigenValueVector.eigenVector = Matrix.matrixToVector(eigenVector);
		return eigenValueVector;
	}
	
	@Override
	public EigenValueVector solve(Matrix matrix, double[] initialEigenVector, int iterations) 
	{
		double newEigenValue = 0;
		// Step 1:
		Matrix initialMatrixVector = Matrix.vectorToMatrix(initialEigenVector);
		Matrix eigenVector = Matrix.normalize(initialMatrixVector);
		for(int i=0; i<iterations; i++) 
		{
			// Step 2:
			Matrix vetorY = matrix.times(eigenVector);
			// Step 3:
			eigenVector = Matrix.normalize(vetorY);
			// Step 4:
			Matrix matrixTranspostaAvetor = eigenVector.transpose();
			double avalorNumerador = matrixTranspostaAvetor.times(matrix)
					.times(eigenVector).get(0, 0);
			double avalorDenominador = matrixTranspostaAvetor.times(eigenVector)
					.get(0, 0);
			newEigenValue = avalorNumerador / avalorDenominador;
		} 

		EigenValueVector eigenValueVector = new EigenValueVector();
		eigenValueVector.eigenValue = newEigenValue;
		eigenValueVector.eigenVector = Matrix.matrixToVector(eigenVector);
		return eigenValueVector;
	}

	@Override
	public void printCommandLine(EigenValueVector ev) {
		System.out.println("Regular Iteration find the largest EigenValue.");
		System.out.println("Eigen Value:" + ev.eigenValue);
		System.out.println("Eigen Vector:");
		Matrix.vectorToMatrix(ev.eigenVector).transpose().print(4,7);
		
		System.out.println("Parametrized Results");
		System.out.println(ev.eigenVector[0]/ev.eigenVector[2]);
		System.out.println(ev.eigenVector[1]/ev.eigenVector[2]);
		System.out.println(ev.eigenVector[2]/ev.eigenVector[2]);
		
	}
}

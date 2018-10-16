package utils.linearAlgebra.gramShmidtProcess;

import utils.linearAlgebra.EigenValueVector;
import utils.linearAlgebra.Matrix;

public class ShiftedIteration implements PowerIteration {
	
	public double estimatedValue = 7;

	@Override
	public EigenValueVector solve(Matrix matrix, double[] initialEigenVector, double error) {
		/**
		 * Shifted Iteration Algorithm:
		 * 1) A' = A - u * I
		 * 2) Calculates li and Yi using the inverse iteration in A
		 * I is a identity matrix. u is a estimated value for the eigenvalue.
		 */
		Matrix identityMatrix = Matrix.identity(matrix.getRowDimension(), matrix.getColumnDimension());
		Matrix changedIdentityMatrix = identityMatrix.times(estimatedValue);
		Matrix displacementMatrix = matrix.minus(changedIdentityMatrix);
		PowerIteration inverse = new InverseIteration();
		EigenValueVector eigenValueVector = inverse.solve(displacementMatrix, initialEigenVector, error);
		eigenValueVector.eigenValue += estimatedValue;
		return eigenValueVector;
	}
	
	@Override
	public EigenValueVector solve(Matrix matrix, double[] initialEigenVector, int iterations) {
		/**
		 * Shifted Iteration Algorithm:
		 * 1) A' = A - u * I
		 * 2) Calculates li and Yi using the inverse iteration in A
		 * I is a identity matrix. u is a estimated value for the eigenvalue.
		 */
		Matrix identityMatrix = Matrix.identity(matrix.getRowDimension(), matrix.getColumnDimension());
		Matrix changedIdentityMatrix = identityMatrix.times(estimatedValue);
		Matrix displacementMatrix = matrix.minus(changedIdentityMatrix);
		PowerIteration inverse = new InverseIteration();
		EigenValueVector eigenValueVector = inverse.solve(displacementMatrix, initialEigenVector, iterations);
		eigenValueVector.eigenValue += estimatedValue;
		return eigenValueVector;
	}

	@Override
	public void printCommandLine(EigenValueVector ev) {
		System.out.println("Shifted Iteration.");
		System.out.println("Eigen Value:" + ev.eigenValue);
		System.out.println("Eigen Vector:");		
		Matrix.vectorToMatrix(ev.eigenVector).transpose().print(4,7);
		
		System.out.println("Parametrized Results");
		System.out.println(ev.eigenVector[0]/ev.eigenVector[2]);
		System.out.println(ev.eigenVector[1]/ev.eigenVector[2]);
		System.out.println(ev.eigenVector[2]/ev.eigenVector[2]);
		
	}

}

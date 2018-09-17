package utils.powerIteration.methods;

import utils.powerIteration.EigenValueVector;
import utils.powerIteration.LUDecomposition;
import utils.powerIteration.Matrix;

/*
 * Returns the smallest Eigenvalue
 */
public class InverseIteration implements PowerIteration {

	@Override
	public EigenValueVector solve(Matrix matrix, double[] initialEigenVector,
			double error) {
		/**
		 * Algorithm:
		 * 1) Normalize x0 = V/|V| 
		 * 2) Yi = M^(-1)*Xi-1 => M*yi = Xi-1
		 * 3) Xi = Yi/|Yi| 
		 * 4) li = (T(Xi) * M^(-1) * Xi) / (T(Xi) * Xi
		 * 
		 * Repeat 2 to 4 while li - li-1 < E 
		 * OBS.: Xi is an eigenvector from eigenVector li. Yi is a vector.
		 */
		double lastEigenValue;
		double newEigenValue = 0;
		int i = 0;

		// Step 1:
		Matrix matrizVetorInicial = Matrix.vectorToMatrix(initialEigenVector);
		Matrix evectorX = Matrix.normalize(matrizVetorInicial);
		//Prepare the matrices L and U to be used in 2th step
		LUDecomposition luMatrix = Matrix.getLU(matrix);
		Matrix lowerMatrix = luMatrix.getL();
		Matrix upperMatrix = luMatrix.getU();

		// Step 2:
		/*
		 * M*yi = xi-1 => L*U*yi = xi-1 a) Doing U*yi = Z, solving firstly
		 * L*Z = xi-1 b) with  Z vector, we can solve U*yi = Z
		 */
		Matrix vectorZ = Matrix.solveSubstitution(lowerMatrix, evectorX);
		Matrix vectorY = Matrix.solveRetrosubstitution(upperMatrix, vectorZ);
		do {
			lastEigenValue = newEigenValue;
			// Step 3:
			evectorX = Matrix.normalize(vectorY);

			// Calcule yi+1:
			/*
			 * M*yi = xi-1 => L*U*yi = xi-1 a) if we do U*yi = Z,
			 * we can solve firstly L*Z = xi-1 
			 * With the Z Vector, we can conclude that U*yi = Z
			 * 
			 */
			vectorZ = Matrix.solveSubstitution(lowerMatrix, evectorX);
			vectorY = Matrix.solveRetrosubstitution(upperMatrix, vectorZ);

			// Step 4:
			// li = (T(Xi) * M^(-1) * Xi) / (T(Xi) * Xi => li = (T(Xi) * yi+1) /
			// (T(Xi) * Xi)
			Matrix transposeMatrixEigenVector = evectorX.transpose();
			double evalueNumerator = transposeMatrixEigenVector.times(vectorY).get(
					0, 0);
			double avalorDenominador = transposeMatrixEigenVector.times(evectorX)
					.get(0, 0);
			newEigenValue = evalueNumerator / avalorDenominador;
			i++;
		} while (i == 1 || Math.abs(newEigenValue - lastEigenValue) > error);

		EigenValueVector eigenValueVector = new EigenValueVector();
		eigenValueVector.eigenValue = 1.0 / newEigenValue;
		eigenValueVector.eigenVector = Matrix.matrixToVector(evectorX);
		return eigenValueVector;
	}

	
	@Override
	public EigenValueVector solve(Matrix matrix, double[] initialEigenVector, int iterations) 
	{
		double newEigenValue = 0;

		// Step 1:
		Matrix matrizVetorInicial = Matrix.vectorToMatrix(initialEigenVector);
		Matrix evectorX = Matrix.normalize(matrizVetorInicial);
		//Prepare the matrices L and U to be used in 2th step
		LUDecomposition luMatrix = Matrix.getLU(matrix);
		Matrix lowerMatrix = luMatrix.getL();
		Matrix upperMatrix = luMatrix.getU();

		// Step 2:
		/*
		 * M*yi = xi-1 => L*U*yi = xi-1 a) Doing U*yi = Z, solving firstly
		 * L*Z = xi-1 b) with  Z vector, we can solve U*yi = Z
		 */
		Matrix vectorZ = Matrix.solveSubstitution(lowerMatrix, evectorX);
		Matrix vectorY = Matrix.solveRetrosubstitution(upperMatrix, vectorZ);
		for(int i=0; i<iterations; i++)
		{
			// Step 3:
			evectorX = Matrix.normalize(vectorY);

			// Calcule yi+1:
			/*
			 * M*yi = xi-1 => L*U*yi = xi-1 a) if we do U*yi = Z,
			 * we can solve firstly L*Z = xi-1 
			 * With the Z Vector, we can conclude that U*yi = Z
			 * 
			 */
			vectorZ = Matrix.solveSubstitution(lowerMatrix, evectorX);
			vectorY = Matrix.solveRetrosubstitution(upperMatrix, vectorZ);

			// Step 4:
			// li = (T(Xi) * M^(-1) * Xi) / (T(Xi) * Xi => li = (T(Xi) * yi+1) /
			// (T(Xi) * Xi)
			Matrix transposeMatrixEigenVector = evectorX.transpose();
			double evalueNumerator = transposeMatrixEigenVector.times(vectorY).get(
					0, 0);
			double avalorDenominador = transposeMatrixEigenVector.times(evectorX)
					.get(0, 0);
			newEigenValue = evalueNumerator / avalorDenominador;
			i++;
		} 

		EigenValueVector eigenValueVector = new EigenValueVector();
		eigenValueVector.eigenValue = 1.0 / newEigenValue;
		eigenValueVector.eigenVector = Matrix.matrixToVector(evectorX);
		return eigenValueVector;
	}
	
	
	
	@Override
	public void printCommandLine(EigenValueVector ev) {
		System.out.println("Inverse Iteration find the smallest EigenValue.");
		System.out.println("Eigen Value:" + ev.eigenValue);
		System.out.println("Eigen Vector:");		
		Matrix.vectorToMatrix(ev.eigenVector).transpose().print(4,7);
		
		System.out.println("Parametrized Results");
		System.out.println(ev.eigenVector[0]/ev.eigenVector[2]);
		System.out.println(ev.eigenVector[1]/ev.eigenVector[2]);
		System.out.println(ev.eigenVector[2]/ev.eigenVector[2]);
		

	}

}

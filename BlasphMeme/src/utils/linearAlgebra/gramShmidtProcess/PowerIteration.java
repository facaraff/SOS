package utils.linearAlgebra.gramShmidtProcess;

import utils.linearAlgebra.EigenValueVector;
import utils.linearAlgebra.Matrix;
/*
 * Interface to use in PowerIteration methods
 *  */
public interface PowerIteration {
	public EigenValueVector solve(Matrix matrix, double[] initialEigenVector, double error);
	public EigenValueVector solve(Matrix matrix, double[] initialEigenVector, int iteration);
	public void printCommandLine(EigenValueVector ev);
}

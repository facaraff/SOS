package utils.powerIteration.methods;

import utils.powerIteration.EigenValueVector;
import utils.powerIteration.Matrix;
/*
 * Interface to use in PowerIteration methods
 *  */
public interface PowerIteration {
	public EigenValueVector solve(Matrix matrix, double[] initialEigenVector, double error);
	public void printCommandLine(EigenValueVector ev);
}

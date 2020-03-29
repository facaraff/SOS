package utils.linearAlgebra;
/**
 * LU Decomposition.
 * 
 * For an m-by-n matrix A with m greater than or equal to n, the LU decomposition is an m-by-n unit
 * lower triangular matrix L, an n-by-n upper triangular matrix U, and a
 * permutation vector piv of length m so that A(piv,:) = L*U. If m less than n, then L
 * is m-by-m and U is m-by-n.
 */

public class LUDecomposition {

	public LUDecomposition(Matrix l, Matrix u) {
		super();
		L = l;
		U = u;
	}

	public Matrix getL() {
		return L;
	}

	public Matrix getU() {
		return U;
	}

	private Matrix L;
	private Matrix U;

}

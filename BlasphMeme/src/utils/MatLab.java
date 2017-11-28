//package utils;


//import utils.random.RandUtils;
// /**
//  * This class provides some utility functions to easily 
// * manipulate and perform specific operations on arrays.
//  */


package utils;

import utils.random.RandUtils;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.Arrays;
import java.util.HashSet;
/**
 * This class provides some utility functions to easily 
 * manipulate and perform specific operations on matrices (arrays and vectors).
 */
public class MatLab
{
	/** 
	 * Returns an n-by-n identity matrix. 
	 * @param n order of the matrix.
	 * @return x identity matrix.
	 */
	public static double[][] eye(int n)
	{
		double[][] x = new double[n][n];
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				if(j==i)
					x[i][j]=1;
				else
					x[i][j]=0;
		return x;
	}
	/** 
	 * Returns an n-by-n null matrix.
	 * @param n order of the matrix.
	 * @return x null matrix.
	 */
	public static double[][] zeros(int n)
	{
		double[][] x = new double[n][n];
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				x[i][j]=0;			
		return x;
	}
	/** 
	 * Returns an n-by-m null matrix.
	 * @param n number for rows.
	 * @param m number of columns.
	 * @return x null matrix.
	 */
	public static double[][] zeros(int n, int m)
	{
		double[][] x = new double[n][m];
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++)
				x[i][j]=0;		
		return x;
	}
	/** 
	 * Returns an array of n uniformly distributed random numbers in [0, 1).
	 * @param n array length.
	 * @return x randomly initialised array.
	 */
	public static double[] rand(int n)
	{
		double[] x = new double[n];
		for(int i=0;i<n;i++)
				x[i]= RandUtils.random();		
		return x;
	}
	/** 
	 * Returns an n-by-m matrix of m*n uniformly distributed random numbers in [0, 1).
	 * @param n number of rows.
	 * @param m number of colums.
	 * @return x n-by-m randomly initialised array.
	 */
	public static double[][] rand(int n, int m)
	{
		double[][] x = new double[n][m];
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++)
				x[i][j]= RandUtils.random();		
		return x;
	}
	/** 
	 * Multiplies an n-by-m matrix by an m-dimensional column matrix.
	 * @param matrix n-by-m array.
	 * @param columnMatrix m-dimensional array (m-by-1 row matrix)
	 * @return result n-dimensional array (n-by-1 row matrix)
	 */
	public static double[] multiply(double[][] matrix, double[] columnMatrix)
	{
		int rows = matrix.length;
		int col = matrix[0].length;
		double[] result = new double[rows];
		try {
			for(int i=0;i<rows;i++)
				for(int j=0;j<col;j++)
					result[i]+=matrix[i][j]*columnMatrix[j];
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	/** 
	 * Multiplies an n-dimensional row matrix by an n-by-m matrix by .
	 * @param rowMatrix n-dimensionl array  (1-by-n row matrix).
	 * @param matrix n-by-m array. 
	 * @return result m-dimensional array (1-by-m row matrix)
	 */
	public static double[] multiply(double[] rowMatrix, double[][] matrix)
	{
		int rows = matrix.length;
		int col = matrix[0].length;
		double[] result = new double[col];
		try {
			for(int i=0;i<col;i++)
				for(int j=0;j<rows;j++)
					result[i]+=matrix[j][i]*rowMatrix[j];
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	
	/** 
	 * Scalar product between two vectors A and B.
	 * @param vectorA n-dimensional array A.
	 * @param vectorB n-dimensional array B.
	 * @return result scalar number reulting of the multiplication.
	 */
	public static double multiply(double[] vectorA, double[] vectorB)
	{
		int length = vectorA.length;
		double result = 0;
		try {
			for(int i=0;i<length;i++)
				result += vectorA[i]*vectorB[i];
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	/** 
	 * Multiplication between a sclar and a vector.
	 * @param scalar scalar value.
	 * @param vector n-dimensional array (row/column matrix).
	 */
	public static double[] multiply(double scalar, double[] vector)
	{
		int len = vector.length;
		double[] result = new double[len];
		for(int i=0;i<len;i++)
			result[i]=scalar*vector[i];
		return result;
	}
	
	/** 
	 * Multiplication betweeen a scalar and an n_by_m matrix.
	 * @param scalar scalar vlalue.
	 * @param matrix 2D array.
	 * @return 2D array (n-by-m matrix).
	 */
	public static double[][] multiply(double scalar, double[][] matrix)
	{
		int I = matrix.length;
		int J = matrix[0].length;
		double[][] result = new double[I][J];
		for(int i=0;i<I;i++)
			for(int j=0;j<J;j++)
				result[i][j]=scalar*matrix[i][j];
		return result;
	}	
	
	/** 
	 * Multiplication between a column matrix and a row matrix.
	 * @param columnVector n-dimensional array.
	 * @param rowVector m-dimensional array.
	 * @return result n-by-m array (matrix).
	 */
	public static double[][] columnXrow(double[] columnVector, double[] rowVector)
	{
		int C = columnVector.length;
		int R = rowVector.length;
		double[][] result = new double[C][R];
		for(int c=0;c<C;c++)
			for(int r=0;r<R;r++)
				result[c][r]=columnVector[c]*rowVector[r];

		return result;
	}
	/**
	 * Performs the dot product (element by element occupying the same position) between two matrices.
	 * @param x n-by-m array.
	 * @param y  n-by-m array.
	 * @return dotProduct n-by-m array.
	 */
	public static double[][] dot(double[][] x, double[][] y)
	{
		int rows = x.length;
		int col = x[0].length;
		double[][] result = new double[rows][col];
		try {
			for(int i=0;i<rows;i++)
				for(int j=0;j<col;j++)
					result[i][j] = x[i][j]*y[i][j];
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	/**
	 * Performs the dot product (element by element occupying the same position) between two vectors (row/colum-matrices).
	 * @param x n-dimensional array.
	 * @param y  n-dimensionalarray.
	 * @return dotProduct n-dimensional array.
	 */
	public static double[] dot(double x[], double[] y)
	{
		int n=x.length;
		double[] retValue = new double[n];
		for (int i = 0; i < n; i++)
			retValue[i] = x[i]*y[i];
		return retValue;
	}	
//	/**
//	 * Dot Product
//	 * 
//	 * @param x
//	 * @param y
//	 * @return dotProduct
//	 */
//	public static double[] dot(double x[], double y)
//	{
//		int n=x.length;
//		double[] retValue = new double[n];
//		for (int i = 0; i < n; i++)
//			retValue[i] = x[i]*y;
//		return retValue;
//	}
	/** 
	 * Sums up two vectors A and B.
	 * @param vectorA n-dimensional array.
	 * @param vectorB n-dimensional  array.
	 * @return result n-dimensional array.
	 */
	public static double[] sum(double[] vectorA, double[] vectorB)
	{
		int length = vectorA.length;
		double[] result = new double[length];
		try {
			for(int i=0;i<length;i++)
				result[i]=vectorA[i]+vectorB[i];
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	/** 
	 * Sums up two matrices A and B.
	 * @param matrixA n-by-m array.
	 * @param matrixB n-by-m  array.
	 * @return result n-by-m array.
	 */
	public static double[][] sum(double[][] matrixA, double[][] matrixB)
	{
		int R = matrixA.length;
		int C = matrixA[0].length;
		double[][] result = new double[R][C];
		try {
			for(int r=0;r<R;r++)
				for(int c=0;c<C;c++)
					result[r][c]=matrixA[r][c]+matrixB[r][c];
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	/** 
	 * Subtracts two vectors A and B.
	 * @param vectorA n-dimensional array.
	 * @param vectorB n-dimensional array.
	 * @return result n-dimensional array.
	 */
	public static double[] subtract(double[] vectorA, double[] vectorB)
	{
		int length = vectorA.length;
		double[] result = new double[length];
		try {
			for(int i=0;i<length;i++)
				result[i]=vectorA[i]-vectorB[i];
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	/** 
	 * Subtracts two matrices A and B.
	 * @param matrixA n-by-m array.
	 * @param matrixB n-by-m  array.
	 * @return result n-by-m array.
	 */
	public static double[][] subtract(double[][] matrixA, double[][] matrixB)
	{
		int rows = matrixA.length;
		int col = matrixA[0].length;
		double[][] result = new double[rows][col];
		try {
			for(int i=0;i<rows;i++)
				for(int j=0;j<col;j++)
					result[i][j] = matrixA[i][j] - matrixB[i][j];
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	/** 
	 * Reshapes the input x matrix into an n-by-m matrix.
	 * @param x input matrix.
	 * @param n number of row for the output matrix.
	 * @param m number fo columns for the output matrix.
	 * @return out n-by-m ouput array.
	 */
	public static double[][] reShape(double[] x, int n, int m)
	{
		double[][] out = new double[n][m];
		int j = 0;
		try 
		{
			for(int i=0;i<x.length;i++)
			{	
				if((i%n == n-1) && (j<9)) j++;
				out[i%n][j]=x[i];
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return out;
	}
	
	/** 
	 * Build up a matrix x such that x[i][j] is minimum between A[i][j] and B[i][j] (i in [0,n-1], j in [m-1]).
	 * @param A first input n-by-m array or real numbers in double precision.
	 * @param B second input n-by-m array or real numbers in double precision.
	 * @return x output n-by-m array of array.
	 */
	public static double[][] min(double[][] A, double[][] B)
	{
		int n = A.length;
		int m = A[0].length;
		double[][] x = new double[n][m];
		try{
			for(int i=0;i<n;i++)
				for(int j=0;j<m;j++)
					x[i][j] = (A[i][j]<B[i][j]) ? A[i][j] : B[i][j];	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return x;
	}
	
	/** 
	 * Returns an array x such that x[i] is minimum between A[i] and B[i] (i in [0, n-1]).
	 * @param A first input array of n real numbers in double precision.
	 * @param B second input array of n real numbers in double precision.
	 * @return x output array of n real numbers in double precision.
	 */
	public static double[] min(double[] A, double[] B)
	{
		int n = A.length;
		double[] x = new double[n];
		try{
			for(int i=0;i<n;i++)
				x[i] = (A[i]<B[i]) ? A[i] : B[i];	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return x;
	}
	/** 
	 * Returns the minimum element in A.
	 * @param A array of real numbers in double precision.
	 * @return x minimum scalar value in A.
	 */
	public static double min(double[] A)
	{
		double x = A[0];
		int n = A.length;
		
		for (int i = 1; i < n; i++)
		{
			if (A[i] < x)
				x = A[i];
		}
		
		return x; 
	}
	/** 
	 * Returns the minimum element in A.
	 * @param A array of array of real numbers in double precision.
	 * @return x minimum scalar value in A.
	 */
//	public static double[] min(double[][] A)
//	{
//		int n = A.length;
//		double[] x = new double[n];
//		try{
//			for(int i=0;i<n;i++)
//				x[i] = min(A[i]);
//		}
//		catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return min(x);
//	}
	public static double min(double[][] A)
	{
		double x = A[0][0];
		int n = A.length;
		int m = A[0].length;
		
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < m; j++)
			{
				if (A[i][j] < x)
					x = A[i][j];
			}
		}
		
		return x; 
	}
	
	/** 
	 * Build up a matrix x such that x[i][j] is maximum between A[i][j] and B[i][j] (i in [0,n-1], j in [m-1]).
	 * @param A first input n-by-m array or real numbers in double precision.
	 * @param B second input n-by-m array or real numbers in double precision.
	 * @return x output n-by-m array of array.
	 */
	public static double[][] max(double[][] A, double[][] B)
	{
		int n = A.length;
		int m = A[0].length;
		double[][] x = new double[n][m];
		try{
			for(int i=0;i<n;i++)
				for(int j=0;j<m;j++)
					x[i][j] = (A[i][j]>B[i][j]) ? A[i][j] : B[i][j];	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return x;
	}
	
	/** 
	 * Returns an array x such that x[i] is maximum between A[i] and B[i] (i in [0, n-1]).
	 * @param A first input array of n real numbers in double precision.
	 * @param B second input array of n real numbers in double precision.
	 * @return x output array of n real numbers in double precision.
	 */
	public static double[] max(double[] A, double[] B)
	{
		int n = A.length;
		double[] x = new double[n];
		try{
			for(int i=0;i<n;i++)
				x[i] = (A[i]>B[i]) ? A[i] : B[i];	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return x;
	}
	
	/** 
	 * Returns the maximum element in A.
	 * @param A array of array of real numbers in double precision.
	 * @return x maximum scalar value in A.
	 */
	public static double max(int[][] A)
	{
		double x = A[0][0];
		int n = A.length;
		int m = A[0].length;
		
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < m; j++)
			{
				if (A[i][j] > x)
					x = A[i][j];
			}
		}
		
		return x; 
	}
	
	/** 
	 * Returns the maximum element in A.
	 * @param A array of real numbers in double precision.
	 * @return x maximum scalar value in A.
	 */
	public static double max(int[] A)
	{
		int x = A[0];
		int n = A.length;
		
		for (int i = 1; i < n; i++)
		{
			if (A[i] > x)
				x = A[i];
		}
		
		return x; 
	}
	/** 
	 * Returns the maximum element in A.
	 * @param A array of real numbers in double precision.
	 * @return x maximum scalar value in A.
	 */
	public static double max(double[] A)
	{
		double x = A[0];
		int n = A.length;
		
		for (int i = 1; i < n; i++)
		{
			if (A[i] > x)
				x = A[i];
		}
		
		return x; 
	}
	
	
	/** 
	 * Returns a copy of A after applying the absolute value to each element.
	 * @param A array real numbers in double precision.
	 * @return B output array of real numbers in double precision.
	 */
	public static double[] abs(double[] A)
	{
		int n = A.length;
		double[] B = new double[n];
		for(int i=0;i<n;i++)
			B[i] = Math.abs(A[i]);
	
		return B;
	}
	
	/** 
	 * Returns a copy of A after applying the absolute value to each element.
	 * @param A array of array of real numbers in double precision.
	 * @return B output arrya of array of real numbers in double precision.
	 */
	public static double[][] abs(double[][] A)
	{
		int n = A.length;
		int m = A[0].length;
		double[][] B = new double[n][m];
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++)
				B[i][m] = Math.abs(A[i][m]);
	
		return B;
	}
	
	private static DecimalFormat decimalFormat;

	static
	{
		decimalFormat = new DecimalFormat();
		//decimalFormat.setPositivePrefix("+");
		decimalFormat.setMaximumFractionDigits(4);
		decimalFormat.setMinimumFractionDigits(4);
	}
	
	/**
	 * Euclidean distance between two points.
	 * @ x fisrt point (array of real numbers in double precision). 
	 * @ y second point (array of real numbers in double precision). 
	**/
	public static double distance(double[] x, double[] y)
	{
		double n = 0;
		for (int i = 0; i < x.length; i++) {
			n += Math.pow((x[i]-y[i]), 2);
		}
		return Math.sqrt(n);
	}
	/**
	 * Norm of a vector.
	 * @param x input vector array of real numbers in double precision. 
	 * @return norm in double precision.
	**/
	public static double norm2(double[] x)
	{
		double y = 0;
		int n = x.length;
		
		for (int i = 0; i < n; i++)
			y += x[i]*x[i];
		
		return Math.sqrt(y);
	}
	/**
	 * Norm of a vector.
	 * @param x input vector array of integers numbers). 
	 * @return norm in double precision.
	**/
	public static double norm2(int[] x)
	{
		double y = 0;
		int n = x.length;
		
		for (int i = 0; i < n; i++)
			y += x[i]*x[i];
		
		return Math.sqrt(y);
	}
	
	/**
	 * Mean value of an array.
	 * @param x array of rreal numbers in double precision.
	 */
	public static double mean(double[] x)
	{
		int n = x.length;
		if (n != 0)
			return sum(x)/n;
		else
			return 0;
	}
	
	/**
	 * Variance of an array.
	 * @param x array of rreal numbers in double precision.
	 */
	public static double variance(double[] x)
	{
		int n = x.length;
		if (n != 0)
		{
			double mu = mean(x);
			double sum = 0;
			for (int i = 0; i < n; i++)
				sum += Math.pow((x[i]-mu),2);
			return sum/n;
		}
		else
			return 0;
	}
	
	/**
	 * Standard deviation of an array.
	 * @param x array of real numbers in double precision.
	 */
	public static double std(double[] x)
	{
		return Math.sqrt(variance(x));
	}
	
	/**
	 * Median value of an array.
	 * @param x array of real numbers in double precision.
	 */
	public static double median(double[] x)
	{
		int n = x.length;
		double[] xSorted = Arrays.copyOf(x, n);
		Arrays.sort(xSorted);
		if ((n % 2) == 0)
			return (xSorted[n/2-1] + xSorted[n/2])/2;
		else
			return xSorted[(n-1)/2];
	}
	

	


	
	/**
	 * Index of the minimum element of array.
	 */
	public static int indexMin(double[] x)
	{
		double y = x[0];
		int n = x.length;
		int index = 0;
		for (int i = 1; i < n; i++)
		{
			if (x[i] < y)
			{
				y = x[i];
				index = i;
			}
			
		}
		
		return index; 
	}
	
	/**
	 * Minimum element of pair of real numbers in double precision.
	 */
	public static double min(double a, double b)
	{
		return (a <= b) ? a : b;
	}
	
	/**
	 * Maximum element of pair of real numbers in double precision.
	 */
	public static double max(double a, double b)
	{
		return (a > b) ? a : b;
	}
	


	/**
	 * Index of the maximum element of an array.
	 * @param x array of real numbers in double precision.
	 * @return index integer variable containig the index.
	 */
	public static int indexMax(double[] x)
	{
		double y = x[0];
		int index = 0;
		int n = x.length;
		
		for (int i = 1; i < n; i++)
		{
			if (x[i] > y)
			{
				y = x[i];
				index = i;
			}
		}
		
		return index; 
	}
	/**
	 * Index of the maximum element of an array.
	 * @param x array of integers numbers.
	 * @return index integer variable containig the index.
	 */
	public static int indexMax(int[] x)
	{
		double y = x[0];
		int index = 0;
		int n = x.length;
		
		for (int i = 1; i < n; i++)
		{
			if (x[i] > y)
			{
				y = x[i];
				index = i;
			}
		}
		
		return index; 
	}
	/**
	 * Minimum element of a dynamic vector of real number in double precision.
	 */
	public static double min(Vector<Double> x)
	{
		double y = x.get(0);
		int n = x.size();
		
		for (int i = 1; i < n; i++)
		{
			if (x.get(i) < y)
				y = x.get(i);
		}
		
		return y; 
	}
	
	
	/**
	 * Product of array elements.
	 */
	public static double prod(double[] x)
	{
		double y = 1;
		int n = x.length;
		
		for (int i = 0; i < n; i++)
			y *= x[i];
		
		return y; 
	}
	
	/**
	 * Sum of array elements.
	 */
	public static double sum(double[] x)
	{
		double y = 0;
		int n = x.length;
		
		for (int i = 0; i < n; i++)
			y += x[i];
		
		return y; 
	}

	/**
	 * Sum of array elements, from startIndex to endIndex.
	 */
	public static double sum(double[] x, int startIndex, int endIndex)
	{
		double y = 0;

		for (int i = startIndex; i < endIndex; i++)
			y += x[i];
		
		return y; 
	}
	
	/**
	 * Cumulative sum of array elements.
	 */
	public static double[] cumsum(double[] x)
	{
		int n = x.length;
		double y[] = new double[n];

		for (int i = 0; i < n; i++)
		{
			y[i] = 0;
			for (int j = 0; j <= i; j++)
				y[i] += x[j];
		}
		
		return y; 
	}

	// /**
	// * Euclidean norm of array.
	// */
	//public static double norm2(double[] x)
	//{
//		double y = 0;
//		int n = x.length;
//		
//		for (int i = 0; i < n; i++)
//			y += x[i]*x[i];
//		
//		return Math.sqrt(y);
//	}
	

	
	
	/**
	 * Return true if the x and y are equals 
	 * (i.e. contain the same elements).
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isEqual(double[] x, double[] y)
	{
		int lengthX = x.length;
		int lengthY = y.length;
		
		if (lengthX != lengthY)
			return false;
		
		boolean isEqual = true;
		for (int i = 0; i < lengthX && isEqual; i++)
		{
			if (x[i] != y[i])
				isEqual = false;
		}
		return isEqual;
	}
	
	/**
	 * Return true if the x and y are equals 
	 * (i.e. contain the same elements).
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isEqual(byte[] x, byte[] y)
	{
		int lengthX = x.length;
		int lengthY = y.length;
		
		if (lengthX != lengthY)
			return false;
		
		boolean isEqual = true;
		for (int i = 0; i < lengthX && isEqual; i++)
		{
			if (x[i] != y[i])
				isEqual = false;
		}
		return isEqual;
	}
	
	/**
	 * Return true if x is an array of zeros.
	 * 
	 * @param x
	 * @return
	 */
	public static boolean isZeros(double[] x)
	{
		boolean isZeros = true;
		int lengthX = x.length;
		for (int i = 0; i < lengthX && isZeros; i++)
			isZeros &= (x[i] == 0);
		return isZeros;
	}
	
	/**
	 * Return true if x is an array of zeros.
	 * 
	 * @param x
	 * @return
	 */
	public static boolean isZeros(int[] x)
	{
		boolean isZeros = true;
		int lengthX = x.length;
		for (int i = 0; i < lengthX && isZeros; i++)
			isZeros &= (x[i] == 0);
		return isZeros;
	}
	
	/**
	 * Transpose a matrix x.
	 * 
	 * @param x
	 * @return
	 */
	public static double[][] transpose(double[][] x)
	{
		int n = x.length;
		int m = x[0].length;
		
		double [][] x_t = new double[m][n];
		
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < m; j++)
				x_t[j][i] = x[i][j];
		}
		return x_t;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static String toString(double[][] x)
	{
		StringBuffer arrayString = new StringBuffer();
		int n = x.length;
		int m = x[0].length;
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < m; j++)
				arrayString.append(x[i][j] + " ");
			arrayString.append("\n");
		}
		return arrayString.toString();
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static String toString(double[] x)
	{
		StringBuffer arrayString = new StringBuffer();
		int n = x.length;
		for (int i = 0; i < n; i++)
			arrayString.append(x[i] + " ");
		return arrayString.toString();
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static String toString(int[] x)
	{
		StringBuffer arrayString = new StringBuffer();
		int n = x.length;
		for (int i = 0; i < n; i++)
			arrayString.append(x[i] + " ");
		return arrayString.toString();
	}

	/**
	 * 
	 * @param x
	 */
	public static void print(int[] x)
	{
		int n = x.length;
		System.out.print("[ ");
		for (int i = 0; i < n; i++)
			System.out.print(x[i] + " ");
		System.out.println("]");
	}
	
	/**
	 * 
	 * @param x
	 */
	public static void print(double[] x)
	{
		int n = x.length;
		System.out.print("[ ");
		for (int i = 0; i < n; i++)
			System.out.print(decimalFormat.format(x[i]) + " ");
		System.out.println("]");
	}
	
	/**
	 * 
	 * @param x
	 */
	public static void print(double[][] x)
	{
		int n = x.length;
		int m = x[0].length;
		for (int i = 0; i < n; i++)
		{
			System.out.print("[ ");
			for (int j = 0; j < m; j++)
				System.out.print(decimalFormat.format(x[i][j]) + " ");
			System.out.println("]");
		}
	}
	
	/**
	 * 
	 * @param x
	 */
	public static void print(int[][] x)
	{
		int n = x.length;
		int m = x[0].length;
		for (int i = 0; i < n; i++)
		{
			System.out.print("[");
			for (int j = 0; j < m; j++)
				System.out.print(x[i][j] + "\t");
			System.out.println("]");
		}
	}
	
	/**
	 * 
	 * @param arr
	 * @param column
	 * @param left
	 * @param right
	 * @return
	 */
	public static int partition(double arr[][], int column, int left, int right)
	{
		int i = left, j = right;
		double[] tmp = new double[arr[0].length];
		double pivot = arr[(left + right) / 2][column];

		while (i <= j) {
			while (arr[i][column] < pivot)
				i++;
			while (arr[j][column] > pivot)
				j--;
			if (i <= j) {
				tmp = arr[i];
				arr[i] = arr[j];
				arr[j] = tmp;
				i++;
				j--;
			}
		};

		return i;
	}
	
	/**
	 * 
	 * @param arr
	 * @param column
	 */
	public static void quickSort(double arr[][], int column)
	{
		quickSort(arr, column, 0, arr.length-1);
	}

	/**
	 * 
	 * @param arr
	 * @param column
	 * @param left
	 * @param right
	 */
	public static void quickSort(double arr[][], int column, int left, int right)
	{
		int index = partition(arr, column, left, right);
		if (left < index - 1)
			quickSort(arr, column, left, index - 1);
		if (index < right)
			quickSort(arr, column, index, right);
	}
	
	/**
	 * 
	 * @param arr
	 * @param column
	 */
	public static void sortRows(double[][] arr, int column)
	{
		quickSort(arr, column);
	}
	
//	/**
//	 * 
//	 * @param x
//	 */
//	public static double[] abs(double[] x)
//	{
//		int n = x.length;
//		double[] y = new double[n];
//		for(int i = 0; i < n; i++)
//			y[i] = Math.abs(x[i]);
//		return y;
//	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static double[] unique(double[] x)
	{
		HashSet<Double> set = new HashSet<Double>();
		for (double el : x)
			set.add(el);
		double[] y = new double[set.size()];
		int i = 0;
		for (double s : set)
			y[i++] = s;
		return y;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static int[] unique(int[] x)
	{
		HashSet<Integer> set = new HashSet<Integer>();
		for (int el : x)
			set.add(el);
		int[] y = new int[set.size()];
		int i = 0;
		for (int s : set)
			y[i++] = s;
		return y;
	}
	
	/**
	 * Returns a copy of the input array  with the reverese order.
	 * @param x input array of real (double precision) numbers.
	 * @return rev reverse of x.
	 */
	public static double[] reverse(double[] x)
	{
		double[] rev = new double[x.length];
		for (int i = 0; i < x.length; i++)
			rev[i] = x[x.length-i-1];
		return rev;
	}
	
	/**
	 * Returns a copy of the input array  with the reverese order.
	 * @param x input array of integer numbers.
	 * @return rev reverse of x.
	 */
	public static int[] reverse(int[] x)
	{
		int[] rev = new int[x.length];
		for (int i = 0; i < x.length; i++)
			rev[i] = x[x.length-i-1];
		return rev;
	}
	
	/**
	 * 
	 * @param n
	 * @return
	 */
	public static String format(double n){return decimalFormat.format(n);}
	
	/** 
	 * Clone an array.
	 * @param input array.
	 * @return result a copy of the same array.
	 */
	public static double[] clone(double[] array)
	{
		int n = array.length;
		double[] clone = new double[n];
		try {
				for(int j=0;j<n;j++)
					clone[j]=array[j];
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return clone;
	}
		/** 
		 * Clone n-by-m matrix.
		 * @param matrix n-by-m array.
		 * @return clone a copy of the same n-by-m array.
		 */
		public static double[][] clone(double[][] matrix)
		{
			int rows = matrix.length;
			int col = matrix[0].length;
			double[][] clone = new double[rows][col];
			try {
				for(int i=0;i<rows;i++)
					for(int j=0;j<col;j++)
						clone[i][j]=matrix[i][j];
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			return clone;
		}
//	/**
//	 * A simple main test class.
//	 */
//	public static void main(String[] args) throws Exception
//	{
//		double[] x = new double[10];
//		for (int i = 0; i < x.length; i++)
//			x[i] = i+1;
//
//		System.out.print("x = "); print(x);
//		System.out.print("y = "); print(MathUtils.cumsum(x));
//		System.out.println("sum(x) = " + MathUtils.sum(x));
//		System.out.println("norm2(x) = " + MathUtils.norm2(x));
//		
//		int[] xint = new int[10];
//		for (int i = 0; i < x.length; i++)
//			xint[i] = i+1;
//		System.out.print("xint = "); print(xint);
//		System.out.print("perm = "); print(RandUtils.randomPermutation(xint));
//		System.out.print("perm = "); print(RandUtils.randomPermutationExcl(xint,9));
//		
//		int N = 5;
//		int M = 2;
//		double[][] test = new double[N][M];
//		
//		for (int i = 0; i < N; i++)
//		{
//			for (int j = 0; j < M; j++)
//				test[i][j] = Math.round(RandUtils.random()*9);
//		}
//		
//		for (int i = 0; i < N; i++)
//		{
//			test[i][0] = RandUtils.random();
//			test[i][1] = i;
//		}
//		
//		System.out.println("unsorted:");
//		print(test);
//		sortRows(test, 0);
//		System.out.println("sorted:");
//		print(test);
//		
//		double[] y = {2.3, 2.3, 5, 7, 5, 8, 2.3, 5};
//		print(y);
//		print(unique(y));
//		print(reverse(y));
//	}
	
}

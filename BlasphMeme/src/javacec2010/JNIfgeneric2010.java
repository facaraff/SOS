package javacec2010;

import utils.benchmarks.BenchmarkLoader;

/**
 * JNI connection class for interfacing the CEC 2010 C-functions.
 */
public class JNIfgeneric2010 {

	/** Load the library, the system-specific filename extension is added by the JVM. */
	static
	{
		try
		{
			BenchmarkLoader.loadNativeLibraryFromJar("libcec2010");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Native method declaration
	private native long initCEC2010(int funcId, int dim);
	private native void exitCEC2010(long fcnPointer);
	private native double evaluate(long fcnPointer, double[] X);

	private long fcnPointer;
	
	public JNIfgeneric2010(int funcId, int dim)
	{
		fcnPointer = initCEC2010(funcId, dim);
    }
	
	public void destroy()
	{
		exitCEC2010(fcnPointer);
    }
	
	public double evaluate(double[] x)
	{
		return evaluate(fcnPointer, x);
    }
}
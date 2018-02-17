package benchmarks.problemsImplementation.CEC2013_LSGO;

import utils.loaders.ClassLoaderHelper;

/**
 * JNI connection class for interfacing the CEC 2013 LSGO C-functions.
 */
public class JNIfgeneric2013 {

	/** Load the library, the system-specific filename extension is added by the JVM. */
	static
	{
		try
		{
			ClassLoaderHelper.loadFolderFromJar("javacec2013lsgo/cdatafiles/");
			ClassLoaderHelper.loadNativeLibraryFromJar("libcec2013lsgo");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Native method declaration
	private native long initCEC2013(int funcId, int dim);
	private native void exitCEC2013(long fcnPointer);
	private native double evaluate(long fcnPointer, double[] X);

	private long fcnPointer;
	
	public JNIfgeneric2013(int funcId, int dim)
	{
		fcnPointer = initCEC2013(funcId, dim);
    }
	
	public void destroy()
	{
		exitCEC2013(fcnPointer);
    }
	
	public double evaluate(double[] x)
	{
		return evaluate(fcnPointer, x);
    }
}
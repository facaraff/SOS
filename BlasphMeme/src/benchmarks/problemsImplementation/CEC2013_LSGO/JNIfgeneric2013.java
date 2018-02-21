package benchmarks.problemsImplementation.CEC2013_LSGO;

//import static utils.RunAndStore.slash;
import org.jblas.DoubleMatrix;
import java.lang.System;
import utils.benchmarks.LibLoader;

/**
 * JNI connection class for interfacing the CEC 2013 LSGO C-functions.
 */
public class JNIfgeneric2013 {

	/** Load the library, the system-specific filename extension is added by the JVM. */
	static
	{
		System.out.println(System.getProperty("java.library.path"));
		try
		{		
			//LibLoader.instalAndLoadNativeLibrary("cec2013lsgo");
			LibLoader.loadNativeLib("cec2013lsgo");
			
			
			//System.loadLibrary("cec2013lsgo");
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
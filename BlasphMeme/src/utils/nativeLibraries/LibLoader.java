package utils.nativeLibraries;

import java.io.File;
//import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.net.URL;
//import java.net.URLDecoder;
//import java.util.Enumeration;
import java.util.Random;
//import java.util.Scanner;
//import java.util.StringTokenizer;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
import static utils.RunAndStore.slash;

/**
 * This class provides an utility function to dynamically load native libraries 
 * contained into a jar file.
 */
public class LibLoader
{
	public static final ClassLoader loader = LibLoader.class.getClassLoader();

	private static final boolean forceReload = true;
	

	
	/**
	 * Loads a native library contained into a jar file.
	 * 
	 * @param name the name of the native library.
	 * @throws IOException
	 */
	public static void loadNativeLibrary(String name) throws Exception
	{
		// build the platform-dependent name of the native library
		String architecture = System.getProperty("os.arch");
		String osName = System.getProperty("os.name");
		String archLibDir = "";
		
		if (osName.contains("Windows"))
		{		
			if (architecture.equalsIgnoreCase("x86") || architecture.equalsIgnoreCase("i386"))
				archLibDir = "win32";
			else
				archLibDir = "win64";

			name += ".dll";
		}
		else if (osName.contains("Mac"))
		{	
			if (architecture.equalsIgnoreCase("x86") || architecture.equalsIgnoreCase("i386"))
				archLibDir = "mac32";
			else
				archLibDir = "mac64";

			name += ".jnilib";
		}
		else
		{	
			if (architecture.equalsIgnoreCase("x86") || architecture.equalsIgnoreCase("i386"))
				archLibDir = "x86";
			else
				archLibDir = "x86_64";

			name += ".so.1.0.1";
		}
		
		System.out.println("name: "+name);
		
		
		
	   try 
	   {
		   File lib = new File("lib"+slash()+archLibDir+slash()+name);
		   System.load(lib.getAbsolutePath());
	   } 
	   catch (UnsatisfiedLinkError e) 
	   {
		   System.err.println("Native code library failed to load.\n" + e);
		   System.exit(1);
	   }
			 
	}
}
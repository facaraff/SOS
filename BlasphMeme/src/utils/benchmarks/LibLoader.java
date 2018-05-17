package utils.benchmarks;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static utils.RunAndStore.slash;
import java.nio.file.Files;
import java.util.Random;
import java.util.Scanner; 


//%%%%%%%%%%%%%%%%%%%%%
/**
 * This class provides an utility function to dynamically load native libraries 
 * 
 * @author Fabio Caraffini 
 */
public class LibLoader
{
	//public static final ClassLoader loader = LibLoader.class.getClassLoader();

//	private static final boolean forceReload = true;
	
	
	
	public static final ClassLoader loader = ClassLoaderHelper.class.getClassLoader();

	private static final boolean forceReload = true;
	
	
	final  static private String toLib ="benhcmarks"+File.separator+"problemsImplementation"+File.separator+"lib";
	
	static String architecture = System.getProperty("os.arch");
	static String currentFolder = LibLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String archLibDir=""; 
	static String pnames="";
	
	/**
	 * Loads a native library
	 * 
	 * @param name the name of the native library.
	 * @throws IOException
	 */
	public static void loadNativeLib(String name) throws Exception
	{
		pickTheRightLib(name);
		System.loadLibrary(pnames);
	}
	/**
	 * Loads a native library
	 * 
	 * @param name the name of the native library.
	 * @throws IOException
	 */
	public static void instalAndLoadNativeLibrary(String name) throws Exception //non va un cazo porcodio
	{	
		installLibrary(name);
		
//	   try 
//	   {
//		  System.load(name);
//	   } 
//	   catch (UnsatisfiedLinkError e) 
//	   {
//		   System.err.println("Native code library failed to load.\n" + e);
//		   System.exit(1);
//	   }
			 
	}
	
	/**
	 * This methods return an array of strings containing the ``java.libray.path'' folders. 
	 */
	static private String[] getJavaLibPaths()
	{
		return System.getProperty("java.library.path").split(":");
	}
	
	/**
	 * This methods check whether or not a given native library is installed in the LD_LIBRARY_PATH.  
	 * 
	 * @param libName the native library name without prefix and suffix.
	 * @param libs_paths array of strings containing paths to java libraries.
	 * @return isInstalled boolean value equal to 0 is not installed, 1 if installed.
	 */
	static private boolean isInstalled(String libName, String[] libs_paths)
	{
		File file = null;
		boolean isInstalled = false;
		for(String s : libs_paths)
		{
			System.out.println("gotten paths: "+s);
			file = new File(s+slash()+libName);
		    if(file.exists()) 
		    {
		    	isInstalled = true;
		        break;
		    }
		 }
		return isInstalled;
	}
	
	static private void installLibrary(String libName) 
	{
		pickTheRightLib(libName);
		
		String[] libs_paths = getJavaLibPaths();
	    if(!isInstalled(libName, libs_paths)) 
	    {
	    	String libPath="";
	    	boolean exist = false;
			for(String s: libs_paths) 
			{    
				System.out.println(s);
				File dir = new File(s);
				if(dir.isDirectory())
				{
					exist =true;
					libPath = s;
				}
				
				if(exist) break;
	        }
			
			System.out.println("picked: "+libPath);
			try
			{
				
//				File lib = new File(currentFolder+"utils"+slash()+"nativeLibraries"+slash()+"lib"+slash()+archLibDir+slash()+pnames);
				File libFile = new File(currentFolder+"utils"+slash()+"benchmarks"+slash()+"nativeLibraries"+slash()+archLibDir+slash()+pnames);
				System.out.println(currentFolder);
				File dest = new File(libPath+slash()+pnames);
				System.out.println(libPath+slash()+libName);
				System.out.println(libPath+slash()+pnames);
				System.out.println("dest "+dest.toPath());
				if(!dest.exists())
					Files.copy(libFile.toPath(), dest.toPath());
				
//				System.load(dest.getAbsolutePath());
				System.loadLibrary(pnames);
			}
			catch(IOException e)
			{
				 System.err.println("Failed to copy the native library.\n" + e);
				   System.exit(1);
			}
			
					
	    }

	} 
	
	static private void pickTheRightLib(String name)
	{
		String osName = System.getProperty("os.name");
		pnames+=name;
		pnames="lib"+pnames; //CHECK!
			
		if (osName.contains("Windows"))
		{		
			if (architecture.equalsIgnoreCase("x86") || architecture.equalsIgnoreCase("i386"))
				archLibDir = "win32";
			else
				archLibDir = "win64";

			pnames += ".dll";
		}
		else if (osName.contains("Mac"))
		{	
			if (architecture.equalsIgnoreCase("x86") || architecture.equalsIgnoreCase("i386"))
				archLibDir = "mac32";
			else
				archLibDir = "mac64";

			pnames += ".jnilib";
		}
		else
		{	
			if (architecture.equalsIgnoreCase("x86") || architecture.equalsIgnoreCase("i386"))
				archLibDir = "x86";
			else
				archLibDir = "x86_64";

			pnames += ".so.1.0.1";
		}
		System.out.println(pnames);
		if(pnames==null)System.out.println("cazzo porcodio!");
	}
	
	
	
	/**
	 * Loads a native library contained into a jar file.
	 * 
	 * @param name the name of the native library.
	 * @throws IOException
	 */
	public void FABIOloadNativeLibraryFromJar(String name) throws Exception
	{
		boolean libraryLoaded = false;
		
		int trials = 600; // 600 x 100 ms = 60 seconds
		int i = 0;
		
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
		
		String nameMangling;
		
		// hack: try multiple times to load the native library
		while (!libraryLoaded && i < trials)
		{
			// create a random mangling
			long time = System.currentTimeMillis();
			long random = new Random(time).nextLong();
			nameMangling = name + "-" + (long)(time + random);
			
			File temp = null;
			InputStream in = null;
			FileOutputStream fos = null;
			
			//System.out.println(this.getClass().getResourceAsStream("nativeLibraries"+ File.separator + archLibDir + File.separator + name));
			//Scanner input = new Scanner(this.getClass().getResourceAsStream("nativeLibraries"+ File.separator + archLibDir + File.separator + name));
			
			
			try
			{
				// create a temporary file to store the native library
				temp = new File(new File(System.getProperty("java.io.tmpdir")), nameMangling);
				// force its deletion at jvm shutdown
				temp.deleteOnExit();
				
				
				if (!temp.exists() || temp.length() == 0 || forceReload)
				{
					// read the native library from jar file
					//in = loader.getResourceAsStream(toLib + File.separator + archLibDir + File.separator + name);
					in=this.getClass().getResourceAsStream("nativeLibraries"+ File.separator + archLibDir + File.separator + name);//XXX FABIO
					// save the native library to the temporary file
					fos = new FileOutputStream(temp);
					
					byte[] buffer = new byte[1024];
					int read = -1;

					while ((read = in.read(buffer)) != -1)
						fos.write(buffer, 0, read);
				}

			 
				
				// try to load the native library
				if (temp.exists() && temp.length() > 0)
				{
					try
					{
						System.out.println(temp.getAbsolutePath());
						System.load(temp.getAbsolutePath());
						libraryLoaded = true;
					}
					catch (UnsatisfiedLinkError ule)
					{
						libraryLoaded = false;
						// throw the exception only if the loader failed all the times
						if (i == trials-1)
							throw ule;
					}
				}
			}
			catch (Exception e)
			{
				libraryLoaded = false;
				// throw the exception only if the loader failed all the times
				if (i == trials-1)
					throw e;
			}
			finally
			{
				if (fos != null)
					fos.close();
				if (in != null)
					in.close();
			}
			
			try
			{
				//Thread.yield();
				Thread.sleep(100);
			}
			catch (Exception e) {}

			i++;
		}
	}
	
		
	
	
	
	
	
	/**
	 * Loads a native library contained into a jar file.
	 * 
	 * @param name the name of the native library.
	 * @throws IOException
	 */
	public static void ORIGINALTOROloadNativeLibraryFromJar(String name) throws Exception
	{
		boolean libraryLoaded = false;
		
		int trials = 600; // 600 x 100 ms = 60 seconds
		int i = 0;
		
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
		
		String nameMangling;
		
		// hack: try multiple times to load the native library
		while (!libraryLoaded && i < trials)
		{
			// create a random mangling
			long time = System.currentTimeMillis();
			long random = new Random(time).nextLong();
			nameMangling = name + "-" + (long)(time + random);
			
			File temp = null;
			InputStream in = null;
			FileOutputStream fos = null;
			
			try
			{
				// create a temporary file to store the native library
				temp = new File(new File(System.getProperty("java.io.tmpdir")), nameMangling);
				// force its deletion at jvm shutdown
				temp.deleteOnExit();
				
				
				if (!temp.exists() || temp.length() == 0 || forceReload)
				{
					// read the native library from jar file
					in = loader.getResourceAsStream(toLib + File.separator + archLibDir + File.separator + name);
					
					// save the native library to the temporary file
					fos = new FileOutputStream(temp);
					
					byte[] buffer = new byte[1024];
					int read = -1;

					while ((read = in.read(buffer)) != -1)
						fos.write(buffer, 0, read);
				}
				
				// try to load the native library
				if (temp.exists() && temp.length() > 0)
				{
					try
					{
						System.load(temp.getAbsolutePath());
						libraryLoaded = true;
					}
					catch (UnsatisfiedLinkError ule)
					{
						libraryLoaded = false;
						// throw the exception only if the loader failed all the times
						if (i == trials-1)
							throw ule;
					}
				}
			}
			catch (Exception e)
			{
				libraryLoaded = false;
				// throw the exception only if the loader failed all the times
				if (i == trials-1)
					throw e;
			}
			finally
			{
				if (fos != null)
					fos.close();
				if (in != null)
					in.close();
			}
			
			try
			{
				//Thread.yield();
				Thread.sleep(100);
			}
			catch (Exception e) {}

			i++;
		}
	}
	
	
	
	
	

}
	

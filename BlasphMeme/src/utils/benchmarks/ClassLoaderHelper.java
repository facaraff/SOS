package utils.benchmarks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
//import java.nio.file.Files;

//import com.sun.jna.Library;
//import com.sun.jna.Native;

/**
 * This class provides an utility function to dynamically load native libraries 
 * contained into a jar file.
 */
public class ClassLoaderHelper
{
	public static final ClassLoader loader = ClassLoaderHelper.class.getClassLoader();

	private static final boolean forceReload = true;
	
	public static void loadFolderFromJar(String path) throws Exception
	{
		boolean libraryLoaded = false;
		
		int trials = 600; // 600 x 100 ms = 60 seconds
		int i = 0;
		
		// hack: try multiple times to load the native library
		while (!libraryLoaded && i < trials)
		{
			boolean loadFromJar = false;
			
			URL url = loader.getResource(path);
			
			if (url != null)
			{
				// if the loader can the resource, check if it's loading it from a jar file
				loadFromJar = (url.toString().contains("jar:") || url.toString().contains("rsrc:"));
				 // in this case the URL starts with file:
			}
			else
			{
				// if the loader can't get the resource, try to load it from a jar file in the classpath
				loadFromJar = true;
			}
			
			if (loadFromJar)
			{
				// from jar				

				try
				{
					// get the jar file path
					String jarPath = null;
					try
					{
						//jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
						// this method should get the path of the running jar file 
						jarPath = new File(ClassLoaderHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();	
					}
					catch (Exception e) {}
					
					// if the URI-based method does not work, look for the jar file in the classpath
					if (jarPath == null)
					{
						// the name of the jar file we are looking for
						String jarName = "TestFunctions.jar";

						// first, scan all the entries in the classpath
						//String classPath = System.getProperty("java.class.path");
						String classPath = System.getenv("CLASSPATH");
						String classPathDelimiter = System.getProperty("path.separator");
						StringTokenizer tokenizer = new StringTokenizer(classPath, classPathDelimiter);
						boolean jarFound = false;
						for (int j = 0; j < tokenizer.countTokens() && !jarFound; j++)
						{
							String classPathElement = tokenizer.nextToken();
							File file = new File(classPathElement);
							if (file.isDirectory())
							{
								jarPath = classPathElement + File.separator + jarName;
								File jarFile = new File(jarPath);
								if (jarFile.exists())
								{
									jarPath = jarFile.getAbsolutePath();
									jarFound = true;
								}
							}
						}

						// if the jar file was not found, look for it in the current directory
						if (!jarFound)
						{
							jarPath = System.getProperty("user.dir") + File.separator + jarName;
							File jarFile = new File(jarPath);
							if (!jarFile.exists())
							{
								throw new Exception("Can't find the jar file " + jarName);
							}
						}
					}

					// open the jar file
					JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
					// get all entries in the jar file
					Enumeration<JarEntry> entries = jar.entries();

					libraryLoaded = true;
					while (entries.hasMoreElements())
					{
						// get the file name
						String name = entries.nextElement().getName();
						File file = new File(name);
						String fileName = file.getName();

						// filter according to the path
						if (name.startsWith(path) && !name.endsWith(File.separator))
						{
							InputStream in = null;
							FileOutputStream fos = null;

							try
							{
								File temp = new File(file.getParentFile().getName() + File.separator + fileName);
								if (!temp.exists())
								{
									// read the file from the jar file
									in = loader.getResourceAsStream(name);

									// save it to an external file
									temp.getParentFile().mkdirs();
									boolean fileCreated = temp.createNewFile();
									if (fileCreated)
									{
										fos = new FileOutputStream(temp);

										byte[] buffer = new byte[1024];
										int read = -1;

										while ((read = in.read(buffer)) != -1)
											fos.write(buffer, 0, read);
									}
									else
									{
										libraryLoaded = false;
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
						}
					}
					
					jar.close();
				}
				catch (Exception e1)
				{
					libraryLoaded = false;
					// throw the exception only if the loader failed all the times
					if (i == trials-1)
						throw e1;
				}
			}
			else
			{
				// from eclipse

				try
				{
					// get all the files from the requested URL
					File dir = new File(url.getPath());

					libraryLoaded = true;
					for (File nextFile : dir.listFiles())
					{
						InputStream in = null;
						FileOutputStream fos = null;

						try
						{
							File temp = new File(nextFile.getParentFile().getName() + File.separator + nextFile.getName());
							if (!temp.exists())
							{
								// read the file from the URL
								in = new FileInputStream(nextFile.toString());

								// save it to an external file
								temp.getParentFile().mkdirs();
								boolean fileCreated = temp.createNewFile();
								if (fileCreated)
								{
									fos = new FileOutputStream(temp);

									byte[] buffer = new byte[1024];
									int read = -1;

									while ((read = in.read(buffer)) != -1)
										fos.write(buffer, 0, read);
								}
								else
								{
									libraryLoaded = false;
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
					}
				}
				catch (Exception e1)
				{
					libraryLoaded = false;
					// throw the exception only if the loader failed all the times
					if (i == trials-1)
						throw e1;
				}
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
	public static void loadNativeLibraryFromJar(String name) throws Exception
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
				temp.setExecutable(true);
				temp.setReadable(true);
				// force its deletion at jvm shutdown
				temp.deleteOnExit();
				
				
				
				if (!temp.exists() || temp.length() == 0 || forceReload)
				{
					
					
					// read the native library from jar file
					in = loader.getResourceAsStream("utils"+File.separator+"benchmarks"+File.separator+"nativeLibraries" + File.separator + archLibDir + File.separator + name);
//					System.out.println(in);
//					System.out.println(in.available());
					
					
					// save the native library to the temporary file
					fos = new FileOutputStream(temp);
					
					byte[] buffer = new byte[1024];
					int read = -1;

					while ((read = in.read(buffer)) != -1)
						fos.write(buffer, 0, read);
				}
				
				fos.flush();//XXX fabio
				//fos.close();
				
				
				// try to load the native library
				if (temp.exists() && temp.length() > 0)
				{
					
					
			
					
					try
					{
//						 CLibrary libc = (CLibrary) Native.loadLibrary("c", CLibrary.class);
//
//						    libc.chmod(temp.getAbsolutePath(), 0755);
//						    System.out.println("ciccio");
						
						
						
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
	public static void loadNativeLibraryFromJarORIGINAL(String name) throws Exception
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
					in = loader.getResourceAsStream("utils"+File.separator+"benchmarks"+File.separator+"nativeLibraries" + File.separator + archLibDir + File.separator + name);
					System.out.println(in);
					System.out.println(in.available());
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
						System.err.println("Native code library failed to load.\n" + ule);
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
//	
//	interface CLibrary extends Library {
//	    public int chmod(String path, int mode);
//	}
}
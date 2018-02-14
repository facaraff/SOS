package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import utils.random.RandUtils;
import darwin.BenchmarkBBOB2010;
import darwin.BenchmarkBBOB2010_nonstatic;
import darwin.BenchmarkBaseFunctions;
import darwin.BenchmarkCEC2005;
import darwin.BenchmarkCEC2008;
import darwin.BenchmarkCEC2010;
import darwin.BenchmarkCEC2010_C;
import darwin.BenchmarkCEC2013;
import darwin.BenchmarkCEC2013_LSGO;
import darwin.BenchmarkCEC2014;
import darwin.BenchmarkSISC2010;

/**
 * A simple main test class.
 */
public class TestMain
{
	public static void main(final String[] args) throws Exception
	{
		for (int i = 0; i < 1; i++)
		{
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try
					{
						if (args.length == 0)
						{
							testBaseFunctions();
							testCEC2005Functions();
							testCEC2008Functions();
							testCEC2010Functions();
							testCEC2010_CFunctions();
							testSISC2010Functions();
							testBBOB2010Functions();
							testBBOB2010_nonstaticFunctions();
							testCEC2013Functions();
							testCEC2013_LSGOFunctions();
							testCEC2014Functions();
							
							/*
							NativeLibraries nativeLibraries = new NativeLibraries();
							System.out.println(nativeLibraries.getLoadedLibraries());
							System.out.println("*****");
							System.out.println(nativeLibraries.getSystemNativeLibraries());
							System.out.println("*****");
							System.out.println(nativeLibraries.getNativeLibraries(TestMain.class.getClassLoader()));
							System.out.println("*****");
							System.out.println(nativeLibraries.getTransitiveNativeLibraries(TestMain.class.getClassLoader()));
							System.out.println("*****");
							System.out.println(nativeLibraries.getTransitiveClassLoaders(TestMain.class.getClassLoader()));
							System.out.println("*****");
							*/
						}
						else
						{
							if (args[0].equals("base"))
								testBaseFunctions();
							if (args[0].equals("cec2005"))
								testCEC2005Functions();
							if (args[0].equals("cec2008"))
								testCEC2008Functions();
							if (args[0].equals("cec2010"))
								testCEC2010Functions();
							if (args[0].equals("sisc2010"))
								testSISC2010Functions();
							if (args[0].equals("bbob2010"))
								testBBOB2010Functions();
							if (args[0].equals("cec2013"))
								testCEC2013Functions();
							if (args[0].equals("cec2013lsgo"))
								testCEC2013_LSGOFunctions();
							if (args[0].equals("cec2014"))
								testCEC2014Functions();
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
	}

	// base functions (variable D)
	public static void testBaseFunctions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[10];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testFunctions = BenchmarkBaseFunctions.class.getMethods();
		int numberOfTestFunctions = testFunctions.length;
		for (int i = 0; i < numberOfTestFunctions; i++)
		{
			Method testFunction = testFunctions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()))
			{
				System.out.println((i+1) + "\t" + "Base function " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
			}
		}
	}

	// CEC2005 functions (D=2,10,30,50)
	public static void testCEC2005Functions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[50];
		for (int i = 0; i < x.length; i++)
			x[i] = -10 + 20*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testCEC2005Functions = BenchmarkCEC2005.class.getMethods();
		int numberOfCEC2005TestFunctions = testCEC2005Functions.length;
		for (int i = 0; i < numberOfCEC2005TestFunctions; i++)
		{
			Method testFunction = testCEC2005Functions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()))
			{
				System.out.println((i+1) + "\t" + "CEC2005 " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
			}
		}
	}
	
	// CEC2008 functions (D=100)
	public static void testCEC2008Functions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[100];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testCEC2008Functions = BenchmarkCEC2008.class.getMethods();
		int numberOfCEC2008TestFunctions = testCEC2008Functions.length;
		for (int i = 0; i < numberOfCEC2008TestFunctions; i++)
		{
			Method testFunction = testCEC2008Functions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()))
			{
				System.out.println((i+1) + "\t" + "CEC2008 " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
			}
		}
	}

	// CEC2010 functions (D=1000)
	public static void testCEC2010Functions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[1000];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testCEC2010Functions = BenchmarkCEC2010.class.getMethods();
		int numberOfCEC2010TestFunctions = testCEC2010Functions.length;
		for (int i = 0; i < numberOfCEC2010TestFunctions; i++)
		{
			Method testFunction = testCEC2010Functions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()))
			{
				System.out.println((i+1) + "\t" + "CEC2010 " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
			}
		}
	}
	
	// CEC2010 functions, C wrapper (D=1000)
	public static void testCEC2010_CFunctions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[1000];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testCEC2010Functions = BenchmarkCEC2010_C.class.getMethods();
		int numberOfCEC2010TestFunctions = testCEC2010Functions.length;
		for (int i = 0; i < numberOfCEC2010TestFunctions; i++)
		{
			Method testFunction = testCEC2010Functions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()) && 
					testFunction.getReturnType().equals(double.class))
			{
				System.out.println(i + "\t" + "CEC2010 (c wrapper) " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
				BenchmarkCEC2010_C.finalizeCEC2010();
			}
		}
	}
	
	// SISC2010 functions (D=100, 200, 500, 1000)
	public static void testSISC2010Functions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[1000];
		for (int i = 0; i < x.length; i++)
			x[i] = -5 + 10*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testSISC2010Functions = BenchmarkSISC2010.class.getMethods();
		int numberOfSISC2010TestFunctions = testSISC2010Functions.length;
		for (int i = 0; i < numberOfSISC2010TestFunctions; i++)
		{
			Method testFunction = testSISC2010Functions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()))
			{
				System.out.println((i+1) + "\t" + "SISC2010 " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
			}
		}
	}
	
	// BBOB2010 functions, static version (D=100, 200, 500, 1000)
	public static void testBBOB2010Functions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[200];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testBBOB2010Functions = BenchmarkBBOB2010.class.getMethods();
		int numberOfBBOB2010TestFunctions = testBBOB2010Functions.length;
		int j = 0;
		for (int i = 0; i < numberOfBBOB2010TestFunctions; i++)
		{
			Method testFunction = testBBOB2010Functions[i];
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()) && 
					testFunction.getReturnType().equals(double.class))
			{
				System.out.println((j+1) + "\t" + "BBOB2010 " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
				BenchmarkBBOB2010.finalizeBBOB();
				j++;
			}
		}
	}
	
	// BBOB2010 functions, non static version (variable D<200 - unless recompiling the library)
	public static void testBBOB2010_nonstaticFunctions() throws 
	InstantiationException, IllegalAccessException, 
	IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[200];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*RandUtils.random();

		// invoke test functions using Java reflection
		BenchmarkBBOB2010_nonstatic bbob2010Functions = new BenchmarkBBOB2010_nonstatic();
		Method[] testBBOB2010Functions = BenchmarkBBOB2010_nonstatic.class.getMethods();
		int numberOfBBOB2010TestFunctions = testBBOB2010Functions.length;
		int j = 0;
		for (int i = 0; i < numberOfBBOB2010TestFunctions; i++)
		{
			Method testFunction = testBBOB2010Functions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					testFunction.getReturnType().equals(double.class))
			{
				System.out.println((j+1) + "\t" + "BBOB2010 (nonstatic) " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(bbob2010Functions, x));
				bbob2010Functions.finalizeBBOB();
				j++;
			}
		}
	}
	
	// CEC2013 functions (D=2,10,30,50)
	public static void testCEC2013Functions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[50];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testCEC2013Functions = BenchmarkCEC2013.class.getMethods();
		int numberOfCEC2013TestFunctions = testCEC2013Functions.length;
		for (int i = 0; i < numberOfCEC2013TestFunctions; i++)
		{
			Method testFunction = testCEC2013Functions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()))
			{
				System.out.println((i+1) + "\t" + "CEC2013 " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
			}
		}
	}
	
	// CEC2013 LSGO functions (D=1000)
	public static void testCEC2013_LSGOFunctions() throws 
	InstantiationException, IllegalAccessException, 
	IllegalArgumentException, InvocationTargetException, InterruptedException
	{
		// generate random solution
		double[] x = new double[1000];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testCEC2013Functions = BenchmarkCEC2013_LSGO.class.getMethods();
		int numberOfCEC2013TestFunctions = testCEC2013Functions.length;
		for (int i = 0; i < numberOfCEC2013TestFunctions; i++)
		{
			Method testFunction = testCEC2013Functions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()) && 
					testFunction.getReturnType().equals(double.class))
			{
				System.out.println(i + "\t" + "CEC2013 (LSGO) " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
				Thread.sleep(50);
				BenchmarkCEC2013_LSGO.finalizeCEC2013();
			}
		}
	}
	
	// CEC2014 functions (D=2,10,30,50,100)
	public static void testCEC2014Functions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException
	{
		// generate random solution
		double[] x = new double[50];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*RandUtils.random();

		// invoke test functions using Java reflection
		Method[] testCEC2014Functions = BenchmarkCEC2014.class.getMethods();
		int numberOfCEC2014TestFunctions = testCEC2014Functions.length;
		for (int i = 0; i < numberOfCEC2014TestFunctions; i++)
		{
			Method testFunction = testCEC2014Functions[i];	
			if (Modifier.isPublic(testFunction.getModifiers()) &&
					Modifier.isStatic(testFunction.getModifiers()))
			{
				System.out.println((i+1) + "\t" + "CEC2014 " + testFunction.getName() + "(x) = " + 
						testFunction.invoke(null, x));
			}
		}
	}
}
package benchmarks;

import benchmarks.problemsImplementation.CEC2014.CEC2014TestFuncRotInvStudy;

//import java.text.DecimalFormat;
//import java.util.Vector;

//import algorithms.interfaces.Algorithm;
import interfaces.Problem;


public class CEC2014RotInvStudy extends Problem
{
	private CEC2014TestFuncRotInvStudy testFunc;
	
	public CEC2014RotInvStudy(int dimension, int problemNum) throws Exception
	{
		 super(dimension, new double[] {-100, 100});  
		 setFID(".f"+problemNum);
		 
		 testFunc = new CEC2014TestFuncRotInvStudy(dimension,problemNum);
		//testFunc.printRotation();
	}
	
	public CEC2014RotInvStudy(int dimension, int problemNum, int rot) throws Exception
	{
		 super(dimension, new double[] {-100, 100});  
		 setFID(".f"+problemNum);
		 
		 testFunc = new CEC2014TestFuncRotInvStudy(dimension,problemNum, rot);
		 //testFunc.printRotation();
	}
	
	public double f(double[] x)
	{
		return testFunc.f(x);
	}
	
	public void printRotationFlag()
	{
		testFunc.printRotation();
	}
}
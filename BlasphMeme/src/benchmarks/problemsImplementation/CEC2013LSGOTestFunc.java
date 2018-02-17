/*
  CEC13 Large Scale Global Optimisation Test Function Suite for Single Objective Optimization
 */
package benchmarks.problemsImplementation;

import benchmarks.problemsImplementation.CEC2013_LSGO.BenchmarkCEC2013_LSGO;


public class CEC2013LSGOTestFunc {

	final int nx, fNumber;
	
	BenchmarkCEC2013_LSGO cec13lsgo = new BenchmarkCEC2013_LSGO();

	public CEC2013LSGOTestFunc(int nx, int func_num)throws Exception
	{
		this.fNumber=func_num;
		this.nx = nx;
		
	}
	
	public double f(double[] x)
	{		
		double f =Double.NaN;
		
		switch(this.fNumber)
		{
		case 1:	
			f=BenchmarkCEC2013_LSGO.f1(x);
			//f+=100.0;
			break;
		case 2:	
			f=BenchmarkCEC2013_LSGO.f2(x);
			break;
		case 3:	
			f=BenchmarkCEC2013_LSGO.f3(x);
			break;
		case 4:	
			f=BenchmarkCEC2013_LSGO.f4(x);
			break;
		case 5:
			f=BenchmarkCEC2013_LSGO.f5(x);
			break;
		case 6:
			f=BenchmarkCEC2013_LSGO.f6(x);
			break;
		case 7:	
			f=BenchmarkCEC2013_LSGO.f7(x);
			break;
		case 8:	
			f=BenchmarkCEC2013_LSGO.f8(x);
			break;
		case 9:	
			f=BenchmarkCEC2013_LSGO.f9(x);
			break;
		case 10:	
			f=BenchmarkCEC2013_LSGO.f10(x);
			break;
		case 11:	
			f=BenchmarkCEC2013_LSGO.f11(x);
			break;
		case 12:	
			f=BenchmarkCEC2013_LSGO.f12(x);
			break;
		case 13:	
			f=BenchmarkCEC2013_LSGO.f13(x);
			break;
		case 14:	
			f=BenchmarkCEC2013_LSGO.f14(x);
			break;
		case 15:	
			f=BenchmarkCEC2013_LSGO.f15(x);
			break;
		default:
			System.out.println("\nError: There are only 15 test functions in this test suite!");
			f = 0.0;
			break;
		}
		
		return f;
	}	
		
}
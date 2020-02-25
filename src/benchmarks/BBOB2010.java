package benchmarks;

import interfaces.Problem;


import benchmarks.problemsImplementation.BBOB2010.BBOB2010TestFunctions;



public class BBOB2010 extends Problem
{
	private int fNum;
	
	
	
	public BBOB2010(int dimension, int problemNum) throws Exception
	{
		 super(dimension, BBOB2010TestFunctions.getBounds(problemNum));  
		 setFID(".f"+problemNum);
		 fNum = problemNum;
	}
	
	public double f(double[] x)
	{
		switch (fNum) {
		case 1:
			return BBOB2010TestFunctions.f1(x);
		case 2:
			return BBOB2010TestFunctions.f2(x);
		case 3:
			return BBOB2010TestFunctions.f3(x);
		case 4:
			return BBOB2010TestFunctions.f4(x);
		case 5:
			return BBOB2010TestFunctions.f5(x);
		case 6:
			return BBOB2010TestFunctions.f6(x);
		case 7:
			return BBOB2010TestFunctions.f7(x);
		case 8:
			return BBOB2010TestFunctions.f8(x);
		case 9:
			return BBOB2010TestFunctions.f9(x);
		case 10:
			return BBOB2010TestFunctions.f10(x);
		case 11:
			return BBOB2010TestFunctions.f11(x);
		case 12:
			return BBOB2010TestFunctions.f12(x);
		case 13:
			return BBOB2010TestFunctions.f13(x);
		case 14:
			return BBOB2010TestFunctions.f14(x);
		case 15:
			return BBOB2010TestFunctions.f15(x);
		case 16:
			return BBOB2010TestFunctions.f16(x);
		case 17:
			return BBOB2010TestFunctions.f17(x);
		case 18:
			return BBOB2010TestFunctions.f18(x);
		case 19:
			return BBOB2010TestFunctions.f19(x);
		case 20:
			return BBOB2010TestFunctions.f20(x);
		case 21:
			return BBOB2010TestFunctions.f21(x);
		case 22:
			return BBOB2010TestFunctions.f22(x);
		case 23:
			return BBOB2010TestFunctions.f23(x);
		case 24:
			return BBOB2010TestFunctions.f24(x);
		default:
			return Double.NaN;
	}
		
	}
}
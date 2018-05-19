/**
Copyright (c) 2013, Giovanni Iacca (giovanniiaccca@incas3.eu)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/

package benchmarks.problemsImplementation.CEC2013_LSGO;


import javacec2013.JNIfgeneric2013;


/**
 * Benchmark Functions for the IEEE CEC 2013 - Special Session 
 * and Competition on Large Scale Global Optimization.
 */
public class BenchmarkCEC2013_LSGO
{
	private static JNIfgeneric2013 fgeneric;
	
	private static void initializeCEC2013(int funcId, int dim)
	{
		fgeneric = new JNIfgeneric2013(funcId, dim);
	}

	public static void finalizeCEC2013()
	{
		if (fgeneric != null)
		{
			fgeneric.destroy();
			fgeneric = null;
		}
	}
	
	public static double f1(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(1, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f2(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(2, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f3(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(3, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f4(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(4, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f5(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(5, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f6(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(6, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f7(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(7, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f8(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(8, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f9(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(9, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f10(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(10, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f11(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(11, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f12(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(12, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f13(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(13, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f14(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(14, x.length);
		return fgeneric.evaluate(x);
	}

	public static double f15(double[] x)
	{
		if (fgeneric == null)
			initializeCEC2013(15, x.length);
		return fgeneric.evaluate(x);
	}
}
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

package mains;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import benchmarks.problemsImplementation.CEC2013_LSGO.BenchmarkCEC2013_LSGO;

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
						testCEC2013_LSGOFunctions();
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
	
	// CEC2013 LSGO functions (D=1000)
	public static void testCEC2013_LSGOFunctions() throws 
		InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException, InterruptedException
	{
		// generate random solution
		double[] x = new double[1000];
		for (int i = 0; i < x.length; i++)
			x[i] = -100 + 200*Math.random();

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
}
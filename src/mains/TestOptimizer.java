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

import benchmarks.problemsImplementation.CEC2013_LSGO.BenchmarkCEC2013_LSGO;

/**
 * A simple main test class.
 */
public class TestOptimizer
{
	public static void main(String[] args) throws Exception
	{
		int problemDimension = 1000;
		
		double[] x = new double[problemDimension];
		double[] best = new double[problemDimension];
		double fx = Double.POSITIVE_INFINITY;
		double fBest = Double.POSITIVE_INFINITY;
		
		// place your algorithm here (hopefully, more efficient than random search...)
		for (int i = 0; i < 5000*problemDimension; i++)
		{
			// generate random solution
			for (int j = 0; j < x.length; j++)
				x[j] = -100 + 200*Math.random();
			fx = BenchmarkCEC2013_LSGO.f15(x);

			// update best
			if (i == 0 || fx < fBest)
			{
				for (int j = 0; j < x.length; j++)
					best[j] = x[j];
				fBest = fx;
				System.out.println(i + "\t" + fBest);
			}
		}
	}
}
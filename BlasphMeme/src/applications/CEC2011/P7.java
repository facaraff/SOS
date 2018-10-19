/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package applications.CEC2011;

import static utils.MatLab.max;

import interfaces.Problem;

public class P7 extends Problem { //Spread Spectrum Radar Polyphase Code Design
	

	public P7() {super(20, new double[] {0,2*Math.PI}); }	
	public double f(double[] x){return codeDesign(x);}
		
		/**
		 * Fitness function (Spread Spectrum Radar Polyphase Code Design)
		 * @param x
		 * @return fitness
		 */
		private double codeDesign(double[] x)
		{   
			int d = this.getDimension();
			int var = 2*d-1;
			int dvar = 2*var;
			double[] hsum = new double[dvar];
			
			for (int kk=1; kk<=dvar; kk++)
			{
				if(kk%2!=0)
				{
					int i =(kk+1)/2;
					hsum[kk-1]=0;
					
					for(int j=i; j<=d; j++)
					{
						double summ = 0;
						
						for(int i1=Math.abs(2*i-j-1)+1; i1<=j; i1++)
							summ=x[i1- 1]+summ;
                  		
						hsum[kk-1]= Math.cos(summ)+hsum[kk-1];
					}
				}
				else
				{
					int i =kk/2;
					hsum[kk-1]=0;
				
					for(int j=(i+1); j<=d; j++)
					{
						double summ = 0;
						
						for(int i1=Math.abs(2*i-j)+1; i1<=j; i1++)
							summ=x[i1- 1]+summ;
                  		
						hsum[kk-1]= Math.cos(summ)+hsum[kk-1];
					}
					
					hsum[kk-1]+= 0.5;
				}
				
			}
			
			return max(hsum);
		}
}

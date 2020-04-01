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
package utils.benchmarks;
/******************************************************************************************************/
/*** shifting, rotation, composition , etc. for CEC2014 and CEC2015 (they both use the very same code)****/
/*****************************************************************************************************/

public class ProblemsTransformations
{
	
	
	//%%%%%%%%%%%%%%% 2013 and CECCEC2014 and CEC2015 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	
	static final double INF = 1.0e99;
	
	
	/**
	 * Shift operator
	 * @param x The solution to be shifted.
	 * @param xshift The shifting factor.
	 * @param nx The dimensionality of the problem.
	 * @param Os The origin of the system.
	 */
	public static void shiftfunc (double[] x, double[] xshift, int nx, double[] Os)
	{
		for (int i=0; i<nx; i++)
		{
			xshift[i]=x[i]-Os[i];
		}
	}

	
	/**
	 * rotation operator
	 * @param x The solution to be rotated.
	 * @param xrot The rotation centre.
	 * @param nx The dimensionality of the problem.
	 * @param Mr The rotation vector.
	 */
	public static void rotatefunc (double[] x, double[] xrot, int nx, double[] Mr)
	{
		int i,j;
		for (i=0; i<nx; i++)
		{
			xrot[i]=0;
			for (j=0; j<nx; j++)
			{
				xrot[i]=xrot[i]+x[j]*Mr[i*nx+j];
			}
		}
	}


	
	public static void sr_func (double[] x, double[] sr_x, int nx, double[] Os, double[] Mr, double sh_rate, int s_flag, int r_flag)
	{
		int i,j;
		double[] y= new double[nx];
		if (s_flag==1)
		{
			if (r_flag==1)
			{	
				shiftfunc(x, y, nx, Os);
				for (i=0; i<nx; i++)//shrink to the orginal search range
				{
					y[i]=y[i]*sh_rate;
				}
				rotatefunc(y, sr_x, nx, Mr);
			}
			else
			{
				shiftfunc(x, sr_x, nx, Os);
				for (i=0; i<nx; i++)//shrink to the orginal search range
				{
					sr_x[i]=sr_x[i]*sh_rate;
				}
			}
		}
		else
		{	

			if (r_flag==1)
			{	
				for (i=0; i<nx; i++)//shrink to the orginal search range
				{
					y[i]=x[i]*sh_rate;
				}
				rotatefunc(y, sr_x, nx, Mr);
			}
			else

			{
				for (j=0; j<nx; j++)//shrink to the orginal search range
				{
					sr_x[j]=x[j]*sh_rate;
				}
			}
		}
	}

	public static void asyfunc (double[] x, double[] xasy, int nx, double beta)
	{
		int i;
		for (i=0; i<nx; i++)
		{
			if (x[i]>0)
				xasy[i]=Math.pow(x[i],1.0+beta*i/(nx-1)*Math.pow(x[i],0.5));
		}
	}

	public static void oszfunc (double[] x, double[] xosz, int nx)
	{
		int i,sx;
		double c1,c2,xx=0;
		for (i=0; i<nx; i++)
		{
			if (i==0||i==nx-1)
			{
				if (x[i]!=0)
					xx=Math.log(Math.abs(x[i]));//xx=log(fabs(x[i]));
				if (x[i]>0)
				{	
					c1=10;
					c2=7.9;
				}
				else
				{
					c1=5.5;
					c2=3.1;
				}	
				if (x[i]>0)
					sx=1;
				else if (x[i]==0)
					sx=0;
				else
					sx=-1;
				xosz[i]=sx*Math.exp(xx+0.049*(Math.sin(c1*xx)+Math.sin(c2*xx)));
			}
			else
				xosz[i]=x[i];
		}
	}
	

	 // Evaluate composition (CEC2013, 2014,etc.)
	public static double cf_cal(double[] x, int nx, double[] Os, double[]  delta,double[] bias,double[] fit, int cf_num)
	{
		int i,j;
		double f;
		double[] w;
		double w_max=0,w_sum=0;
		w=new double[cf_num];
		for (i=0; i<cf_num; i++)
		{
			fit[i]+=bias[i];
			w[i]=0;
			for (j=0; j<nx; j++)
			{
				w[i]+=Math.pow(x[j]-Os[i*nx+j],2.0);
			}
			if (w[i]!=0)
				w[i]=Math.pow(1.0/w[i],0.5)*Math.exp(-w[i]/2.0/nx/Math.pow(delta[i],2.0));
			else
				w[i]=INF;
			if (w[i]>w_max)
				w_max=w[i];
		}

		for (i=0; i<cf_num; i++)
		{
			w_sum=w_sum+w[i];
		}
		if(w_max==0)
		{
			for (i=0; i<cf_num; i++)
				w[i]=1;
			w_sum=cf_num;
		}
		f = 0.0;
		for (i=0; i<cf_num; i++)
		{
			f=f+w[i]/w_sum*fit[i];
		}
		return f;
	}
	
	//
	// Elementary operations %%%%%%%%%%%%%%%%%%%     CEC2005   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//

	// Shift
	public static void shift(double[] results, double[] x, double[] o) {
		for (int i = 0 ; i < x.length ; i ++) {
			results[i] = x[i] - o[i];
		}
	}

	// Rotate
	static public void rotate(double[] results, double[] x, double[][] matrix) {
		xA(results, x, matrix);
	}

	//
	// Matrix & vector operations
	//

	// (1xD) row vector * (Dx1) column vector = (1) scalar
	public static double xy(double[] x, double[] y) {
		double result = 0.0;
		for (int i = 0 ; i < x.length ; i ++) {
			result += (x[i] * y[i]);
		}

		return (result);
	}

	// (1xD) row vector * (DxD) matrix = (1xD) row vector
	static public void xA(double[] result, double[] x, double[][] A) {
		for (int i = 0 ; i < result.length ; i ++) {
			result[i] = 0.0;
			for (int j = 0 ; j < result.length ; j ++) {
				result[i] += (x[j] * A[j][i]);
			}
		}
	}

	// (DxD) matrix * (Dx1) column vector = (Dx1) column vector
	public static void Ax(double[] result, double[][] A, double[] x) {
		for (int i = 0 ; i < result.length ; i ++) {
			result[i] = 0.0;
			for (int j = 0 ; j < result.length ; j ++) {
				result[i] += (A[i][j] * x[j]);
			}
		}
	}
	
}


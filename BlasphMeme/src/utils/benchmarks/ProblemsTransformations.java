package utils.benchmarks;
/******************************************************************************************************/
/*** shifting, rotation, composition , etc. for CEC2014 and CEC2015 (they both use the very same code)****/
/*****************************************************************************************************/

public class ProblemsTransformations
{
	
	
	//%%%%%%%%%%%%%%% CEC2014 and CEC2015 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	
	static final double INF = 1.0e99;
	
	
	/**
	 * Shift operator
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
	 */
	public static void rotatefunc (double[] x, double[] xrot, int nx, double[] Mr)
	{
		//System.out.println("Sto rotando come un procodecristo!");
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

	void asyfunc (double[] x, double[] xasy, int nx, double beta)
	{
		int i;
		for (i=0; i<nx; i++)
		{
			if (x[i]>0)
				xasy[i]=Math.pow(x[i],1.0+beta*i/(nx-1)*Math.pow(x[i],0.5));
		}
	}

	void oszfunc (double[] x, double[] xosz, int nx)
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
	
	/**
	 * Evaluate composition (CEC2014)
	 */
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


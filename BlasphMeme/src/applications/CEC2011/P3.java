package applications.CEC2011;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;


import interfaces.Problem;


public class P3 extends Problem implements FirstOrderDifferentialEquations{ // Bifunctional Catalyst Blend Optimal Control Problem

	//Matrix Coefficent (Table 1 - Report CEC2011)
	static double[][] c = { 
			{	0.002918487,	-0.008045787,	0.006749947,	-0.001416647 },
			{	9.509977,		-35.00994,		42.83329,		-17.33333	 },
			{	26.82093,		-95.56079,		113.0398,		-44.29997	 },
			{	208.7241,		-719.8052,		827.7466,		-316.6655	 },
			{	1.350005,		-6.850027,		12.16671,		-6.666689	 },
			{	0.01921995,		-0.0794532,		0.110566,		-0.05033333	 },
			{	0.1323596,		-0.469255,		0.5539323,		-0.2166664	 },
			{	7.339981,		-25.27328,		29.93329,		-11.99999	 },
			{	-0.3950534,		1.679353,		-1.777829,		0.4974987	 },
			{	-2.504665E-5,	0.01005854,		-0.01986696,	0.00983347	 }
		};
	
	double u;

	
	
	public P3(int probDim) //throws Exception
	{		
		super(probDim, new double[] {0.6, 0.9}); 		
	}
	
	public double f(double[] x){
		
		u=x[0];
		
		double[] y=new double[] {1,0,0,0,0,0,0};
		
		FirstOrderIntegrator intgr=new DormandPrince853Integrator(1.0e-8, 100.0, 1.0e-10, 1.0e-10);
		
		intgr.integrate(this, 0, y, 0.78, y);
		
		return y[6]*1000;
	}
	
	public int getDimension() {//XXX (Fabio) this overrided method can be removed as the dimensionaliy if hte peoblem is fixed and therefore can be specified at constructor level
		return 7;
	}
			
	private double[] equations(double[] y, double ut) 
	{
		double[] dy = new double[7];
		double[] k = new double[10];
		
		
		//Equations 14 Report CEC2011
		for(int i=0; i<10; i++) 
			k[i] = c[i][0] + c[i][1]*ut + c[i][2]*Math.pow(ut, 2) + c[i][3]*Math.pow(ut,3);
				
		//Equations 4,8-13 Report CEC2011
		dy[0]= -k[0]*y[0];
		dy[1]= k[0]*y[0] - (k[1] + k[2])*y[1] + k[3]*y[4];
		dy[2]= k[1]*y[1];
		dy[3]= -k[5]*y[3] + k[4]*y[4];
		dy[4]= -k[2]*y[1] + k[5]*y[3] - (k[3] + k[4] + k[7] + k[8])*y[4] + k[6]*y[5] + k[9]*y[6];
		dy[5]= k[7]*y[4] - k[6]*y[5];
		dy[6]= k[8]*y[4] - k[9]*y[6];
		
		return dy;
	}
	
	public void computeDerivatives(double t, double[] y, double[] yDot) {
		
		//Need to be check, matlab code just work with one value
		// u need to be consider value in t
		yDot=equations(y,u);
		
	}

}

package applications.CEC2011;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;


import interfaces.Problem;


public class P3 extends Problem { // Bifunctional Catalyst Blend Optimal Control Problem
	
	public P3() //throws Exception
	{		
		super(1, new double[] {0.6, 0.9}); 		
	}
	
	public double f(double[] x) throws Exception{
		
		//Initial State
		double[] y=new double[] {1,0,0,0,0,0,0};
		
		//Final State
		double[] yDot=new double[7];
		
		DiffEquations DE = new DiffEquations(x[0]);
		
		FirstOrderIntegrator intgr=new DormandPrince853Integrator(1.0e-8, 100.0, 1.0e-10, 1.0e-10);
		//FirstOrderIntegrator intgr=new ClassicalRungeKuttaIntegrator(1.0e-3);
				
		intgr.integrate(DE, 0, y, 0.78, yDot);
		
		return yDot[6]*1000;
	}
	
	private class DiffEquations implements FirstOrderDifferentialEquations {
		
		//Matrix Coefficient (Table 1 - Report CEC2011)
		double[][] c=new double[][] {	{	2.918487E-3,	-8.045787E-3,	6.749947E-3,	-1.416647E-3 },
										{	9.509977E0,		-3.500994E1,	4.283329E1,		-1.733333E1	 },
										{	2.682093E1,		-9.556079E1,	1.130398E2,		-4.429997E1	 },
										{	2.087241E2,		-7.198052E2,	8.277466E2,		-3.166655E2	 },
										{	1.350005E0,		-6.850027E0,	1.216671E1,		-6.666689E0	 },
										{	1.921995E-2,	-7.945320E-2,	1.105660E-1,	-5.033333E-2 },
										{	1.323596E-1,	-4.692550E-1,	5.539323E-1,	-2.166664E-1 },
										{	7.339981E0,		-2.527328E1,	2.993329E1,		-1.199999E1	 },
										{	-3.950534E-1,	1.679353E0,		-1.777829E0,	4.974987E-1	 },
										{	-2.504665E-5,	1.005854E-2,	-1.986696E-2,	9.833470E-3	 }
									}; 
		
									
		private double[] k = new double[10];
		private double u;
		
		public DiffEquations(double x) {
			u=x;
			
			//Equations 14 Report CEC2011
			for(int i=0; i<10; i++) 
				k[i] = c[i][0] + c[i][1]*u + c[i][2]*Math.pow(u, 2) + c[i][3]*Math.pow(u,3);
		}
		
		public int getDimension() {
			return 7;
		}
		
		public void computeDerivatives(double t, double[] y, double[] yDot) {
			 				
			//Equations 4,8-13 Report CEC2011
			yDot[0]= -k[0]*y[0];
			yDot[1]= k[0]*y[0] - (k[1] + k[2])*y[1] + k[3]*y[4];
			yDot[2]= k[1]*y[1];
			yDot[3]= -k[5]*y[3] + k[4]*y[4];
			yDot[4]= -k[2]*y[1] + k[5]*y[3] - (k[3] + k[4] + k[7] + k[8])*y[4] + k[6]*y[5] + k[9]*y[6];
			yDot[5]= k[7]*y[4] - k[6]*y[5];
			yDot[6]= k[8]*y[4] - k[9]*y[6];
			
		}
		
	}

}

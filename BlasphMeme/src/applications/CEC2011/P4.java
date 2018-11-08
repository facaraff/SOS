package applications.CEC2011;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
//import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import interfaces.Problem;


public class P4 extends Problem { // Optimal Control of a Non-Linear Stirred Tank Reactor
	
	double yInt,u;
	
	public P4() //throws Exception
	{		
		super(1,new double[] {0,0.5}); 		
	}
	

	
	public double f(double[] x) throws Exception{
		
		//Initial State
		double[] y=new double[] {0.09,0.09};
		
		u=x[0];
		yInt=0;
		
		DiffEquations DE = new DiffEquations();
		
		FirstOrderIntegrator intgr=new DormandPrince853Integrator(1.0e-8, 1, 1.0e-10, 1.0e-10);
		//FirstOrderIntegrator intgr=new ClassicalRungeKuttaIntegrator(1.0e-2);
	
		
		intgr.addStepHandler(stepHandler);				
		intgr.integrate(DE, 0, y, 0.72, y); // To replace final time with 0.78
	
		
		return yInt;
	}
	
	private class DiffEquations implements FirstOrderDifferentialEquations {
		
		public int getDimension() {
			return 2;
		}
		
		public void computeDerivatives(double t, double[] y, double[] yDot) {
			 				
			//Equations 16-17 Report CEC2011
			yDot[0]= -(2+u)*(y[0]+0.25)+(y[1]+0.5)*Math.exp(25*y[0]/(y[0]+2));
			yDot[1]= 0.5-y[1]-(y[1]+0.5)*Math.exp(25*y[0]/(y[0]+2));
			
		}
		
	}
	
	StepHandler stepHandler = new StepHandler() {
	    public void init(double t0, double[] y0, double t) {
	    }
	            
	    public void handleStep(StepInterpolator interpolator, boolean isLast) {
	    	
	    	double   t = interpolator.getCurrentTime();
	    	double   told= interpolator.getPreviousTime();
	    	double[] y = interpolator.getInterpolatedState();
	        
	        //Evaluate Continuously The Integration usin Riemann (Code Matlab non lo fanno)
	        yInt=yInt+(Math.pow(y[0],2)+Math.pow(y[1],2)+0.1*Math.pow(u,2))*(t-told);
	        
	    }
	};

}

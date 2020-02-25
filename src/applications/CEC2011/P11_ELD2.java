package applications.CEC2011;

import interfaces.Problem;
import utils.MatLab;

public class P11_ELD2 extends Problem {
	
	int powerDemand=1800;
	
	
	double[] pMin= {0,0,0,60,60,60,60,60,60,40,40,55,55};
	double[] pMax= {680,360,360,180,180,180,180,180,180,120,120,120,120};
	double[] a= {0.00028,0.00056,0.00056,0.00324,0.00324,0.00324,0.00324,0.00324,0.00324,0.00284,0.00284,0.00284,0.00284};
	
	double[] b= {8.10,8.10,8.10,7.74,7.74,7.74,7.74,7.74,7.74,8.60,8.60,8.60,8.60};
	double[] c= {550,309,307,240,240,240,240,240,240,126,126,126,126};
	double[] e= {300,200,200,150,150,150,150,150,150,100,100,100,100};
	double[] f= {0.035,0.042,0.042,0.063,0.063,0.063,0.063,0.063,0.063,0.084,0.084,0.084,0.084};
	
	
	public P11_ELD2() {
		
		super(13);
		
		this.setBounds(generateBounds(getDimension()));		
	}
	
	
	public double f(double[] x) throws Exception {
	
		
		double capacityLimitsPenalty = 0;
		double powerBalancePenalty =0;
		
		double currentCost=0;
					
		powerBalancePenalty += Math.abs(powerDemand-MatLab.sum(x));
					
		//Capacity Limits Penalty Calculation
		for(int i=0; i<x.length; i++) {
			capacityLimitsPenalty += Math.abs(x[i]-pMin[i]) 
					- (x[i]-pMin[i]) + Math.abs(pMax[i]-x[i]) 
					-(pMax[i]-x[i]); 
		}
		

		
		for(int i=0; i<x.length; i++) {
			currentCost+= a[i]*Math.pow(x[i],2)+b[i]*x[i]+c[i]
					+Math.abs(e[i]*Math.sin(f[i]*(pMin[i]-x[i])));
		}
	
	
		double penalty = 100000*powerBalancePenalty + 1000*capacityLimitsPenalty;
		
		return currentCost+penalty;
	}

	
	
	private double[][] generateBounds(int probDim)
	{
		double[][] bounds = new double[probDim][2];
		
		for(int i=0; i<probDim; i++) {
			bounds[i][0] = pMin[i];
			bounds[i][1] = pMax[i];
		}
				
		return bounds;
	}

}

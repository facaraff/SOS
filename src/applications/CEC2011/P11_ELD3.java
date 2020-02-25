package applications.CEC2011;

import interfaces.Problem;
import utils.MatLab;

public class P11_ELD3 extends Problem {
	
	int powerDemand=1263;
	
	//Loss Coefficient
	double[][] B1={{1.4e-5,1.2e-5,0.7e-5,-0.1e-5,-0.3e-5,-0.1e-5,-0.1e-5,-0.1e-5,-0.3e-5,-0.5e-5,-0.3e-5,-0.2e-5,0.4e-5,0.3e-5,-0.1e-5},
			{1.2e-5,1.5e-5,1.3e-5, 0.0e-5,-0.5e-5,-0.2e-5,0.0e-5,0.1e-5,-0.2e-5,-0.4e-5,-0.4e-5,0.0e-5,0.4e-5,1e-5,-0.2e-5},
			{0.7e-5,1.3e-5,7.6e-5,-0.1e-5,-1.3e-5,-0.9e-5,-0.1e-5,0.0e-5,-0.8e-5,-1.2e-5,-1.7e-5,0.0e-5,-2.6e-5,11.1e-5,-2.8e-5},
			{-0.1e-5,0.0e-5,-0.1e-5, 3.4e-5,-0.7e-5,-0.4e-5,1.1e-5,5.0e-5,2.9e-5,3.2e-5,-1.1e-5,0.0e-5,0.1e-5,0.1e-5,-2.6e-5},
			{-0.3e-5,-0.5e-5,-1.3e-5,-0.7e-5, 9.0e-5,1.4e-5,-0.3e-5,-1.2e-5,-1.0e-5,-1.3e-5,0.7e-5,-0.2e-5,-0.2e-5,-2.4e-5,-0.3e-5},
			{-0.1e-5,-0.2e-5,-0.9e-5,-0.4e-5, 1.4e-5,1.6e-5,0.0e-5,-0.6e-5,-0.5e-5,-0.8e-5,1.1e-5,-0.1e-5,-0.2e-5,-1.7e-5,0.3e-5},
			{-0.1e-5,0.0e-5,-0.1e-5, 1.1e-5,-0.3e-5,0.0e-5,1.5e-5,1.7e-5,1.5e-5,0.9e-5,-0.5e-5,0.7e-5,0.0e-5,-0.2e-5,-0.8e-5},
			{-0.1e-5,0.1e-5,0.0e-5, 5.0e-5,-1.2e-5,-0.6e-5,1.7e-5,16.8e-5,8.2e-5,7.9e-5,-2.3e-5,-3.6e-5,0.1e-5,0.5e-5,-7.8e-5},
			{-0.3e-5,-0.2e-5,-0.8e-5, 2.9e-5,-1.0e-5,-0.5e-5,1.5e-5,8.2e-5,12.9e-5,11.6e-5,-2.1e-5,-2.5e-5,0.7e-5,-1.2e-5,-7.2e-5},
			{-0.5e-5,-0.4e-5,-1.2e-5, 3.2e-5,-1.3e-5,-0.8e-5,0.9e-5,7.9e-5,11.6e-5,20.0e-5,-2.7e-5,-3.4e-5,0.9e-5,-1.1e-5,-8.8e-5},
			{-0.3e-5,-0.4e-5,-1.7e-5,-1.1e-5, 0.7e-5,1.1e-5,-0.5e-5,-2.3e-5,-2.1e-5,-2.7e-5,14.0e-5,0.1e-5,0.4e-5,-3.8e-5,16.8e-5},
			{-0.2e-5,0.0e-5,0.0e-5,0.0e-5,-0.2e-5,-0.1e-5,0.7e-5,-3.6e-5,-2.5e-5,-3.4e-5,0.1e-5,5.4e-5,-0.1e-5,-0.4e-5,2.8e-5},
			{0.4e-5,0.4e-5,-2.6e-5,0.1e-5,-0.2e-5,-0.2e-5,0.0e-5,0.1e-5,0.7e-5,0.9e-5,0.4e-5,-0.1e-5,10.3e-5,-10.1e-5,2.8e-5},
			{0.3e-5,1.0e-5,11.1e-5,0.1e-5,-2.4e-5,-1.7e-5,-0.2e-5,0.5e-5,-1.2e-5,-1.1e-5,-3.8e-5,-0.4e-5,-10.1e-5,57.8e-5,-9.4e-5},
			{-0.1e-5,-0.2e-5,-2.8e-5,-2.6e-5,-0.3e-5,0.3e-5,-0.8e-5,-7.8e-5,-7.2e-5,-8.8e-5,16.8e-5,2.8e-5,2.8e-5,-9.4e-5,128.3e-5}};
	
	double[] B2= {-0.1e-5,-0.2e-5,2.8e-5,-0.1e-5,0.1e-5,-0.3e-5,-0.2e-5,-0.2e-5,0.6e-5,3.9e-5,-1.7e-5,0,-3.2e-5,6.7e-5,-6.4e-5};
	
	double B3=5.5e-5;
	
	
	double[] pMin= {150,150,20,20,150,135,135,60,25,25,20,20,25,15,15};
	double[] pMax= {455,455,130,130,470,460,465,300,162,160,80,80,85,55,55};
	double[] a= {0.000299,0.000183,0.001126,0.001126,0.000205,0.000301,0.000364,0.000338,0.000807,0.001203,0.003586,0.005513,0.000371,0.001929,0.004447000};
	double[] b= {10.1,10.2,8.80,8.80,10.4,10.1,9.80,11.2,11.2,10.7,10.2,9.90,13.1,12.1,12.4};
	double[] c= {671,574,374,374,461,630,548,227,173,175,186,230,225,309,323};
	
	double[] po= {400,300,105,100,90,400,350,95,105,110,60,40,30,20,20};
	
	double[] upRamp= {80,80,130,130,80,80,80,65,60,60,80,80,80,55,55};
	double[] downRamp= {120,120,130,130,120,120,120,100,100,100,80,80,80,55,55};
	
	double[] poZ1LowerLim= {150,185,20,20,180,230,135,60,25,25,20,30,25,15,15};
	double[] poZ1UpperLim= {150,255,20,20,200,255,135,60,25,25,20,40,25,15,15};
	double[] poZ2LowerLim= {150,305,20,20,305,365,135,60,25,25,20,55,25,15,15};
	double[] poZ2UpperLim= {150,335,20,20,335,395,135,60,25,25,20,65,25,15,15};
	double[] poZ3LowerLim= {150,420,20,20,390,430,135,60,25,25,20,20,25,15,15};
	double[] poZ3UpperLim= {150,450,20,20,420,455,135,60,25,25,20,20,25,15,15};
	
	
	public P11_ELD3() {
		
		super(15);
		
		this.setBounds(generateBounds(getDimension()));
		
	}
	
	
	public double f(double[] x) throws Exception {
	
		
		double capacityLimitsPenalty = 0;
		double powerBalancePenalty =0;
		double rampLimitPenalty= 0;
		double pozPenality=0;
		
		double currentCost=0;
					
		//Power Balance Penalty Calculation
		double powerLoss=0;
		
		for(int i=0; i<x.length; i++) {
			powerLoss += B2[i]*x[i];
			for(int j=0; j<x.length; j++) {
				powerLoss += B1[i][j]*x[i]*x[j];
			}
		}
		powerLoss +=B3;
					
		powerBalancePenalty += Math.abs(powerDemand+powerLoss-MatLab.sum(x));
					
		//Capacity Limits Penalty Calculation
		for(int i=0; i<x.length; i++) {
			capacityLimitsPenalty += Math.abs(x[i]-pMin[i]) 
					- (x[i]-pMin[i]) + Math.abs(pMax[i]-x[i]) 
					-(pMax[i]-x[i]); 
		}
		
		//Ramp Rate Limits Penalty Calculation	
		for(int i=0; i<x.length; i++) {
			double upRampLim = Math.min(pMax[i], po[i]+upRamp[i]);
			double downRampLim = Math.max(pMin[i], po[i]-downRamp[i]);
			rampLimitPenalty += Math.abs(x[i]-downRampLim)-(x[i]-downRampLim)
					+ Math.abs(upRampLim-x[i])-(upRampLim-x[i]);
		}
		
		//Prohibited Operating Zones Penalty Calculation
		for(int i=0; i<x.length; i++) {
		
			if(poZ1LowerLim[i]<x[i]&&poZ1UpperLim[i]>x[i])
				pozPenality+=Math.min(x[i]-poZ1LowerLim[i], poZ1UpperLim[i]-x[i]);
			
			if(poZ2LowerLim[i]<x[i]&&poZ2UpperLim[i]>x[i])
				pozPenality+=Math.min(x[i]-poZ2LowerLim[i], poZ2UpperLim[i]-x[i]);
			
			if(poZ3LowerLim[i]<x[i]&&poZ3UpperLim[i]>x[i])
				pozPenality+=Math.min(x[i]-poZ3LowerLim[i], poZ3UpperLim[i]-x[i]);
		}
		
		for(int i=0; i<x.length; i++) {
			currentCost+= a[i]*Math.pow(x[i],2)+b[i]*x[i]+c[i];
		}
	
	
		double penalty = 1000*powerBalancePenalty + 1000*capacityLimitsPenalty 
				+ 100000*rampLimitPenalty + 100000*pozPenality;
		
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

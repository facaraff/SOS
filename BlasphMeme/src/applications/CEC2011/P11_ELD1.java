package applications.CEC2011;

import interfaces.Problem;
import utils.MatLab;

public class P11_ELD1 extends Problem {
	
	int powerDemand=1263;
	
	//Loss Coefficient
	double[][] B1={{1.7e-5,1.2e-5,0.7e-5,-0.1e-5,-0.5e-5,-0.2e-5},
			{1.2e-5,1.4e-5,0.9e-5,0.1e-5,-0.6e-5,-0.1e-5},
			{0.7e-5,0.9e-5,3.1e-5,0.0e-5,-1.0e-5,-0.6e-5},
			{-0.1e-5,0.1e-5,0.0e-5,0.24e-5,-0.6e-5,-0.8e-5},
			{-0.5e-5,-0.6e-5,-0.1e-5,-0.6e-5,12.9e-5,-0.2e-5},
			{0.2e-5,-0.1e-5,-0.6e-5,-0.8e-5,-0.2e-5,15.0e-5}};
	
	double[] B2= {-0.3908e-5,-0.1297e-5,0.7047e-5,0.0591e-5,0.2161e-5,-0.6635e-5};
	
	double B3=5.6e-5;
	
	double[] pMin= {100,50,80,50,50,50};
	double[] pMax= {500,200,300,150,200,120};
	
	double[] a= {0.007,0.0095,0.009,0.009,0.008,0.0075};
	double[] b= {7,10,8.50,11,10.5,12};
	double[] c= {240,200,220,200,220,190};

	
	double[] po= {440,170,200,150,190,150};
	
	double[] upRamp= {80,50,65,50,50,50};
	double[] downRamp= {120,90,100,90,90,90};
	
	double[] poZ1LowerLim= {210,90,150,80,90,75};
	double[] poZ1UpperLim= {240,110,170,90,110,85};
	double[] poZ2LowerLim= {350,140,210,110,140,100};
	double[] poZ2UpperLim= {380,160,240,120,150,105};
	
	
	public P11_ELD1() {
		
		super(6);
		
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

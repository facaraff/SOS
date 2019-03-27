package applications.CEC2011;

import interfaces.Problem;
import utils.MatLab;

public class P11_DED2 extends Problem {

	int nLoadHours=24;
	int nUnits=10;
	
	int[] powerDemand={1036,1110,1258,1406,1480,1628,1702,1776,1924,2072,2146,2220,
			2072,1924,1776,1554,1480,1628,1776,2072,1924,1628,1332,1184};
	
		
	double[] pMin= {150,135,73,60,73,57,20,47,20,55};
	double[] pMax= {470,460,340,300,243,160,130,120,80,55};
	
	double[] a= {0.00043,0.00063,0.00039,0.0007,0.00079,0.00056,0.00211,0.0048,0.10908,0.00951};
	double[] b= {21.6,21.05,20.81,23.9,21.62,17.87,16.51,23.23,19.58,22.54};
	double[] c= {958.2,1313.6,604.97,471.6,480.29,601.75,502.7,639.4,455.6,692.4};
	double[] e= {450,600,320,260,280,310,300,340,270,380};
	double[] f= {0.041,0.036,0.028,0.052,0.063,0.048,0.086,0.082,0.098,0.094};
	
	double[] upRamp= {80,80,80,50,50,50,30,30,30,30};
	double[] downRamp= {80,80,80,50,50,50,30,30,30,30};

	
	
	
	public P11_DED2() {
		
		super(216);
		
		this.setBounds(generateBounds(getDimension()));
		
	}
	
	
	public double f(double[] x) throws Exception {
	
		double[][] inGen=new double [nLoadHours][nUnits];
		
		for(int i=0; i<x.length; i++) 
			inGen[i/(nUnits-1)][i%(nUnits-1)]=x[i];
		
		for(int i=0; i<nLoadHours; i++)
			inGen[i][nUnits-1]=55;	
		
		double capacityLimitsPenalty = 0;
		double powerBalancePenalty =0;
		double rampLimitPenalty= 0;
		
		double currentCost=0;
		
		for(int i=0; i<nLoadHours; i++) {
			
			//Power Balance Penalty Calculation					
			powerBalancePenalty += Math.abs(powerDemand[i]-MatLab.sum(inGen[i]));
						
			//Capacity Limits Penalty Calculation
			for(int j=0; j<nUnits; j++) {
				capacityLimitsPenalty += Math.abs(inGen[i][j]-pMin[j]) 
						- (inGen[i][j]-pMin[j]) + Math.abs(pMax[j]-inGen[i][j]) 
						-(pMax[j]-inGen[i][j]); 
			}
			
			//Ramp Rate Limits Penalty Calculation	
			for(int j=0; j<nUnits && i>0; j++) {
				double upRampLim = Math.min(pMax[j], inGen[i-1][j]+upRamp[j]);
				double downRampLim = Math.max(pMin[j], inGen[i-1][j]-downRamp[j]);
				rampLimitPenalty += Math.abs(inGen[i][j]-downRampLim)-(inGen[i][j]-downRampLim)
						+ Math.abs(upRampLim-inGen[i][j])-(upRampLim-inGen[i][j]);
			}
			
			
			for(int j=0; j<nUnits && i>0; j++) {
				currentCost+= a[j]*Math.pow(inGen[i][j],2)+b[j]*inGen[i][j]+c[j]
						+Math.abs(e[j]*Math.sin(f[j]*(pMin[j]-inGen[i][j])));
			}
		}
		
		double penalty = 1000*powerBalancePenalty + 1000*capacityLimitsPenalty 
				+ 100000*rampLimitPenalty;
		
		return currentCost+penalty;
	}
	
	
	private double[][] generateBounds(int probDim)
	{
		double[][] bounds = new double[probDim][2];
		
		for(int i=0; i<probDim; i++) {
			bounds[i][0] = pMin[i%(nUnits-1)];
			bounds[i][1] = pMax[i%(nUnits-1)];
		}
				
		return bounds;
	}

}

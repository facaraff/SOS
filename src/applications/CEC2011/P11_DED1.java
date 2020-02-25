package applications.CEC2011;

import interfaces.Problem;
import utils.MatLab;

public class P11_DED1 extends Problem {

	int nLoadHours=24;
	int nUnits=5;
	
	int[] powerDemand={410,435,475,530,558,608,626,654,690,704,720,740,
			704,690,654,580,558,608,654,704,680,605,527,463};
	
	//Loss Coefficient
	double[][] B1={{0.000049,0.000014,0.000015,0.000015,0.000020},
			{0.000014,0.000045,0.000016,0.000020,0.000018},
			{0.000015,0.000016,0.000039,0.000010,0.000012},
			{0.000015,0.000020,0.000010,0.000040,0.000014},
			{0.000020,0.000018,0.000012,0.000014,0.000035}};
	
	double[] B2= {0,0,0,0,0};
	
	double B3=0;
		
	double[] pMin= {10,20,30,40,50};
	double[] pMax= {75,125,175,250,300};
	
	double[] a= {0.008,0.003,0.0012,0.001,0.0015};
	double[] b= {2,1.8,2.1,2,1.8};
	double[] c= {25,60,100,120,40};
	double[] e= {100,140,160,180,200};
	double[] f= {0.042,0.040,0.038,0.037,0.035};
	
	double[] upRamp= {30, 30, 40, 50, 50};
	double[] downRamp= {30, 30, 40, 50, 50};
	
	double[] poZ1LowerLim= {10, 20, 30, 40, 50};
	double[] poZ1UpperLim= {10, 20, 30, 40, 50};
	double[] poZ2LowerLim= {10, 20, 30, 40, 50};
	double[] poZ2UpperLim= {10, 20, 30, 40, 50};
	
	
	
	public P11_DED1() {
		
		super(120);
		
		this.setBounds(generateBounds(getDimension()));
		
	}
	
	
	public double f(double[] x) throws Exception {
	
		double[][] inGen=new double [nLoadHours][nUnits];
		
		for(int i=0; i<x.length; i++)
			inGen[i/nUnits][i%nUnits]=x[i];
		
		
		double capacityLimitsPenalty = 0;
		double powerBalancePenalty =0;
		double rampLimitPenalty= 0;
		double pozPenality=0;
		
		double currentCost=0;
		
		for(int i=0; i<nLoadHours; i++) {
			
			//Power Balance Penalty Calculation
			double powerLoss=0;
			
			for(int j1=0; j1<nUnits; j1++) {
				powerLoss += B2[j1]*inGen[i][j1];
				for(int j2=0; j2<nUnits; j2++) {
					powerLoss += B1[j1][j2]*inGen[i][j1]*inGen[i][j2];
				}
			}
			powerLoss +=B3;
						
			powerBalancePenalty += Math.abs(powerDemand[i]+powerLoss-MatLab.sum(inGen[i]));
						
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
			
			//Prohibited Operating Zones Penalty Calculation
			for(int j=0; j<nUnits && i>0; j++) {
			
				if(poZ1LowerLim[j]<inGen[i][j]&&poZ1UpperLim[j]>inGen[i][j])
					pozPenality+=Math.min(inGen[i][j]-poZ1LowerLim[j], poZ1UpperLim[j]-inGen[i][j]);
				
				if(poZ2LowerLim[j]<inGen[i][j]&&poZ2UpperLim[j]>inGen[i][j])
					pozPenality+=Math.min(inGen[i][j]-poZ2LowerLim[j], poZ2UpperLim[j]-inGen[i][j]);			
			}
			
			for(int j=0; j<nUnits && i>0; j++) {
				currentCost+= a[j]*Math.pow(inGen[i][j],2)+b[j]*inGen[i][j]+c[j]
						+Math.abs(e[j]*Math.sin(f[j]*(pMin[j]-inGen[i][j])));
			}
		}
		
		double penalty = 1000*powerBalancePenalty + 1000*capacityLimitsPenalty 
				+ 100000*rampLimitPenalty + 100000*pozPenality;
		
		return currentCost+penalty;
	}
	
	
	private double[][] generateBounds(int probDim)
	{
		double[][] bounds = new double[probDim][2];
		
		for(int i=0; i<probDim; i++) {
			bounds[i][0] = pMin[i%nUnits];
			bounds[i][1] = pMax[i%nUnits];
		}
				
		return bounds;
	}

}

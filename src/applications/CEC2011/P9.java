package applications.CEC2011;

import java.util.Vector;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import interfaces.Problem;
import utils.MatLab;

public class P9 extends Problem {
	
	double basemva, accuracy, maxiter, tc, kp;
	busSpecification[] bus;
	line[] linedata;
	
	Double[] pg,pd;
	Integer[] gbus,dbus;
	double[][] bt;
	
	double[][] YIbus,Xbus;
	double[][][] ptdf;
	
	double[] fc;
	
	double[] rg= {32.7290,32.1122,30.3532,33.6474,64.1156,66.8729};
    double[] rd= {7.5449,10.7964,10.9944,11.0402,11.7990,15.3803,42.6800,41.4551,
    		73.1939,57.0430,45.5920,43.6553,61.8002,59.6409,57.0279,51.0749,67.1070,
    		60.6623,198.6744,178.9956,199.9483};
	
	public P9(){
		
		super(126);
		
		//Problem Data
		
		basemva=100;
		accuracy=0.0001;
		maxiter=10;
		tc=283.4*4.52*2;
		kp=100;
		
		bus = new busSpecification[30];
		
		bus[0]= new busSpecification(0,0,1.05,0,0,0,165.9,0,0,0,0);
		bus[1]= new busSpecification(1,1,1.043,0,21.7,12.7,49.1,28.4,-40,50,0);
		bus[2]= new busSpecification(2,-1,1,0,2.4,1.2,0,0,0,0,0);
		bus[3]= new busSpecification(3,-1,1,0,7.6,1.6,0,0,0,0,0);
		bus[4]= new busSpecification(4,1,1.01,0,94.2,19,21.6,28,-40,40,0);
		bus[5]= new busSpecification(5,-1,1,0,0,0,0,0,0,0,0);
		bus[6]= new busSpecification(6,-1,1,0,22.8,10.9,0,0,0,0,0);
		bus[7]= new busSpecification(7,1,1.01,0,30,30,22.8,39.1,-6,24,0);
		bus[8]= new busSpecification(8,-1,1,0,0,0,0,0,0,0,0);
		bus[9]= new busSpecification(9,-1,1,0,5.8,2,0,0,-6,24,0);
		bus[10]= new busSpecification(10,1,1.082,0,0,0,12.4,31.6,0,0,0);
		bus[11]= new busSpecification(11,-1,1,0,11.2,7.5,0,0,0,0,0);
		bus[12]= new busSpecification(12,1,1.071,0,0,0,11.6,45.7,0,0,0);
		bus[13]= new busSpecification(13,-1,1,0,6.2,1.6,0,0,0,0,0);
		bus[14]= new busSpecification(14,-1,1,0,8.2,2.5,0,0,0,0,0);
		bus[15]= new busSpecification(15,-1,1,0,3.5,1.8,0,0,0,0,0);
		bus[16]= new busSpecification(16,-1,1,0,9,5.8,0,0,0,0,0);
		bus[17]= new busSpecification(17,-1,1,0,3.2,0.9,0,0,0,0,0);
		bus[18]= new busSpecification(18,-1,1,0,9.5,3.4,0,0,0,0,0);
		bus[19]= new busSpecification(19,-1,1,0,2.2,0.7,0,0,0,0,0);
		bus[20]= new busSpecification(20,-1,1,0,17.5,11.2,0,0,0,0,0);
		bus[21]= new busSpecification(21,-1,1,0,0,0,0,0,0,0,0);
		bus[22]= new busSpecification(22,-1,1,0,3.2,1.6,0,0,0,0,0);
		bus[23]= new busSpecification(23,-1,1,0,8.7,6.7,0,0,0,0,0);
		bus[24]= new busSpecification(24,-1,1,0,0,0,0,0,0,0,0);
		bus[25]= new busSpecification(25,-1,1,0,3.5,2.3,0,0,0,0,0);
		bus[26]= new busSpecification(26,-1,1,0,0,0,0,0,0,0,0);
		bus[27]= new busSpecification(27,-1,1,0,0,0,0,0,0,0,0);
		bus[28]= new busSpecification(28,-1,1,0,2.4,0.9,0,0,0,0,0);
		bus[29]= new busSpecification(29,-1,1,0,10.6,1.9,0,0,0,0,0);


		
		linedata= new line[41];
		
		
		linedata[0]= new line(0,1,0.0192,0.0575,0.0264,1,130);
		linedata[1]= new line(0,2,0.0452,0.1652,0.0204,1,130);
		linedata[2]= new line(1,3,0.057,0.1737,0.0184,1,65);
		linedata[3]= new line(2,3,0.0132,0.0379,0.0042,1,130);
		linedata[4]= new line(1,4,0.0472,0.1983,0.0209,1,130);
		linedata[5]= new line(1,5,0.0581,0.1763,0.0187,1,65);
		linedata[6]= new line(3,5,0.0119,0.0414,0.0045,1,90);
		linedata[7]= new line(6,4,0.046,0.116,0.0102,1,70);
		linedata[8]= new line(5,6,0.0267,0.082,0.0085,1,130);
		linedata[9]= new line(5,7,0.012,0.042,0.0045,1,32);
		linedata[10]= new line(5,8,0,0.208,0,1,65);
		linedata[11]= new line(5,9,0,0.556,0,1,32);
		linedata[12]= new line(10,8,0,0.208,0,1,65);
		linedata[13]= new line(8,9,0,0.11,0,1,65);
		linedata[14]= new line(3,11,0,0.256,0,1,65);
		linedata[15]= new line(12,11,0,0.14,0,1,65);
		linedata[16]= new line(11,13,0.1231,0.2559,0,1,32);
		linedata[17]= new line(11,14,0.0662,0.1304,0,1,32);
		linedata[18]= new line(11,15,0.0945,0.1987,0,1,32);
		linedata[19]= new line(13,14,0.221,0.1997,0,1,16);
		linedata[20]= new line(15,16,0.0524,0.1923,0,1,16);
		linedata[21]= new line(14,17,0.1073,0.2185,0,1,16);
		linedata[22]= new line(17,18,0.0639,0.1292,0,1,16);
		linedata[23]= new line(19,18,0.034,0.068,0,1,32);
		linedata[24]= new line(9,19,0.0936,0.209,0,1,32);
		linedata[25]= new line(9,16,0.0324,0.0845,0,1,32);
		linedata[26]= new line(9,20,0.0348,0.0749,0,1,32);
		linedata[27]= new line(9,21,0.0727,0.1499,0,1,32);
		linedata[28]= new line(21,20,0.0116,0.0236,0,1,32);
		linedata[29]= new line(14,22,0.1,0.202,0,1,16);
		linedata[30]= new line(21,23,0.115,0.179,0,1,16);
		linedata[31]= new line(22,23,0.132,0.27,0,1,16);
		linedata[32]= new line(24,23,0.1885,0.3292,0,1,16);
		linedata[33]= new line(24,25,0.2544,0.38,0,1,16);
		linedata[34]= new line(26,24,0.1093,0.2087,0,1,16);
		linedata[35]= new line(27,26,0,0.396,0,1,65);
		linedata[36]= new line(26,28,0.2198,0.4153,0,1,16);
		linedata[37]= new line(26,29,0.3202,0.6027,0,1,16);
		linedata[38]= new line(28,29,0.2399,0.4533,0,1,16);
		linedata[39]= new line(7,27,0.0636,0.2,0.0214,1,32);
		linedata[40]= new line(5,27,0.0169,0.0599,0.0065,1,32);

		
		
		Vector<Double> pgTemp= new Vector<Double>();
		Vector<Double> pdTemp= new Vector<Double>();
		Vector<Integer> gTemp= new Vector<Integer>();
		Vector<Integer> dTemp= new Vector<Integer>();
		
		for(int i=0; i<bus.length; i++){
			
			if(bus[i].genMW>0) {
				pgTemp.add(bus[i].genMW/100);
				gTemp.add(i);
			}
			
			if(bus[i].loadMW>0) {
				pdTemp.add(bus[i].loadMW/100);
				dTemp.add(i);
			}
			
		}
		
		pg=pgTemp.toArray(new Double[pgTemp.size()]);
		pd=pdTemp.toArray(new Double[pdTemp.size()]);
		gbus=gTemp.toArray(new Integer[gTemp.size()]);
		dbus=dTemp.toArray(new Integer[dTemp.size()]);
		
		bt=MatLab.zeros(pg.length, pd.length);
		
		//50 MW BILATERAL TRANSACTION 
		bt[0][3]=0.05;   bt[0][4]=0.1;   bt[0][5]=0.05;
		bt[1][2]=0.05;
		bt[2][20]=0.025;
		bt[3][20]=0.025; bt[3][15]=0.15;
		bt[4][11]=0.025;
		bt[5][7]=0.025;
		
		//Formation YIbus
		YIbus=MatLab.zeros(bus.length-1);
		int busA,busB;
		double yi;
		
		for(int i=0; i<linedata.length; i++) {
			
			busA=linedata[i].busnl-1; busB=linedata[i].busnr-1; 
			yi=1/linedata[i].puI;
			
			if(busA>=0)
				YIbus[busA][busA]+=yi;
			
			if(busB>=0)
				YIbus[busB][busB]+=yi;
			
			if(busA>=0&&busB>=0) {
				YIbus[busA][busB]-=yi;
				YIbus[busB][busA]-=yi;
			}						
		}
		
		RealMatrix rb=new Array2DRowRealMatrix(YIbus);
		RealMatrix rbInverse = new LUDecomposition(rb).getSolver().getInverse();
		double[][] XInverse=rbInverse.getData();
		
		Xbus=MatLab.zeros(bus.length);
		
		for(int i=0; i<bus.length-1; i++) {
			for(int j=0; j<bus.length-1; j++) {
				Xbus[i+1][j+1]=XInverse[i][j];
			}
		}
		
		//PTDF
		
		ptdf= new double[gbus.length][dbus.length][linedata.length];
		
		for(int i=0; i<gbus.length; i++) {
			for(int j=0; j<dbus.length; j++) {
				for(int k=0; k<linedata.length;k++) {
					ptdf[i][j][k]=(Xbus[linedata[k].busnl][gbus[i]]-Xbus[linedata[k].busnr][gbus[i]]
							-Xbus[linedata[k].busnl][dbus[j]]+Xbus[linedata[k].busnr][dbus[j]])/linedata[k].puI;
				}
			}
		}
		
		fc=new double[linedata.length];
		double sumFc=0;
		
		for(int i=0; i<linedata.length; i++) {
			fc[i]=100*linedata[i].puI;
			sumFc+=linedata[i].puI;
		}
		
		
		for(int i=0; i<linedata.length; i++) {
			fc[i]=100*fc[i]/sumFc;
		}
		
		this.setBounds(generateBounds(getDimension()));
		
	}
	
	
	public double f(double[] x) throws Exception {
		
		double[][] gd = MatLab.reShape(x, pg.length, pd.length);
		
		double flows;
		double[] costLine=new double[linedata.length];
		
		
		for(int k=0; k<linedata.length;k++) {
			
			flows=0;
					
			for(int i=0; i<bus.length-1; i++) {
				for(int j=0; j<bus.length-1; j++) {
					
					flows+=Math.abs(ptdf[i][j][k]*gd[i][j])
							+Math.abs(ptdf[i][j][k]*gd[i][j]);
				}
			}
			
			costLine[k]=fc[k]/flows;
		}
		
		
		double rateEbe1=0;
		double rateEbe2=0;
		double[][] costL=new double[pg.length][pd.length];
		double[] costGen=new double[pg.length];
		double[] costLoad=new double[pd.length];
		
		for(int i=0; i<gbus.length; i++) {
			costGen[i]=0;
			for(int j=0; j<dbus.length; j++) {
				costL[i][j]+=0;
				for(int k=0; k<linedata.length;k++) {
					costL[i][j]+=Math.abs(costLine[k]*ptdf[i][j][k]);
				}
				costGen[i]+=gd[i][j]*costL[i][j];
			}
			rateEbe1+=Math.pow(costGen[i]/pg[i]-rg[i], 2);
		}
		
		for(int j=0; j<dbus.length; j++) {
			costLoad[j]=0;
			for(int i=0; i<gbus.length; i++) {
				costLoad[j]=costLoad[j]+gd[i][j]*costL[i][j];
			}
			rateEbe2+=Math.pow(costLoad[j]/pd[j]-rd[j], 2);
		}
		
		double rateD=rateEbe1+rateEbe2;
		
		//Constrain Violation
		
		double[] pgx=new double[pg.length];
		double[] pdx=new double[pd.length];
		
		for(int i=0; i<pg.length; i++) {
			for(int j=0; j<pd.length; j++) {
				pgx[i]+=bt[i][j]+gd[i][j];
				pdx[j]+=bt[i][j]+gd[i][j];
			}
		}
		
		double genPen=0;
		
		for(int i=0; i<pg.length; i++) 
			genPen+=100*Math.abs(pgx[i]-pg[i]);
		
		
		double loadPen=0;
		for(int j=0; j<pd.length; j++)
			loadPen+=100*Math.abs(pdx[j]-pd[j]);
		
		return (rateD + 50*kp*(genPen+loadPen));
	}
	
	private double[][] generateBounds(int probDim)
	{
		double[][] bounds = new double[probDim][2];
		
		for(int i=0; i<pg.length; i++) {
			for(int j=0; j<pd.length; j++) {
				bounds[i*pd.length+j][0]=0;
				bounds[i*pd.length+j][1]=Math.min(pg[i]-bt[i][j], pd[j]-bt[i][j]);
			}
		}
				
		return bounds;
	}
	

	public class busSpecification{
		int busNo,busCode;
		double voltageM,angleDegree,loadMW,loadMVar,genMW,genMVar,injQmin,injQmax,injMVar;
		
		public busSpecification() {
			busNo=0;
			busCode=0;
			
			voltageM=0;
			angleDegree=0;
			loadMW=0;
			loadMVar=0;
			genMW=0;
			genMVar=0;
			injQmin=0;
			injQmax=0;
			injMVar=0;
		}
		
		public busSpecification(int busNo,int busCode, double voltageM, double angleDegree, double loadMW, 
				double loadMVar, double genMW, double genMVar, double injQmin, double injQmax, double injMVar) {
			
			this.busNo=busNo;
			this.busCode=busCode;
	
			this.voltageM=voltageM;
			this.angleDegree=angleDegree;
			this.loadMW=loadMW;
			this.loadMVar=loadMVar;
			this.genMW=genMW;
			this.genMVar=genMVar;
			this.injQmin=injQmin;
			this.injQmax=injQmax;
			this.injMVar=injMVar;
			
		}
		
		public busSpecification(busSpecification c) {
			
			this.busNo=c.busNo;
			this.busCode=c.busCode;
	
			this.voltageM=c.voltageM;
			this.angleDegree=c.angleDegree;
			this.loadMW=c.loadMW;
			this.loadMVar=c.loadMVar;
			this.genMW=c.genMW;
			this.genMVar=c.genMVar;
			this.injQmin=c.injQmin;
			this.injQmax=c.injQmax;
			this.injMVar=c.injMVar;
			
		}
		
	}
	
	public class line{
		int busnl,busnr;
		double puR,puI,puHalfB,tapSett,lineLimit;
		
		public line() {
			
			busnl=0;
			busnr=0;
			
			puR=0;
			puI=0;
			puHalfB=0;
			tapSett=0;
			lineLimit=0;
			
		}
		
		public line(int busnl, int busnr, double puR, double puI,
				double puHalfB, double tapSett, double lineLimit) {
			
			this.busnl=busnl;
			this.busnr=busnr;

			this.puR=puR;
			this.puI=puI;
			this.puHalfB=puHalfB;
			this.tapSett=tapSett;
			this.lineLimit=lineLimit;
			
		}
	}

}
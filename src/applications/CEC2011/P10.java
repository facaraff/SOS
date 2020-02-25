package applications.CEC2011;

import java.util.Collections;
import java.util.Vector;

import interfaces.Problem;

public class P10 extends Problem {
	
	double[] nullv= {0.8727,2.0944}; // Radiants
	double phiDesired=Math.PI; // Radiants
	double distance=0.5;
	
	int nQuant=300;
    double[] phi; //Radiants
    double[] yax; 
	
	
	
	public P10() {
		
		super(12);
		
		phi= new double[nQuant];
		yax= new double[nQuant];
		
		for(int i=0; i<nQuant; i++) {
			phi[i]=(i*2*Math.PI)/nQuant;
		}
		
		this.setBounds(generateBounds(getDimension()));
		
	}

	@Override
	public double f(double[] x2) throws Exception {
		
		double[] x= {0.824930791968848, 0.8792046186594125, 0.4741880679635084, 
				0.27189417829978685, 0.2860018875208836, 0.8431459364114409, 
				0.9526843536780314, -2.2616475772271625, 1.0337866789258872,
				0.4743592351308683, 3.0445459113554474, 1.4555447698324926};
		
		yax[0]=factorCir(x, phi[0]);
		
		double maxI=yax[0];
		double phiZero=phi[0];
		int phiRef=0;
		
		for(int i=1; i<nQuant; i++) {
			
			yax[i]=factorCir(x, phi[i]);
			
			if(maxI<yax[i]) {
				maxI=yax[i];
				phiZero=phi[i];
				phiRef=i;
			}
		}
		
		Vector<Double> sidelobes= new Vector<Double>();
		Vector<Double> sllphi= new Vector<Double>();
		
		if(yax[0]>yax[nQuant-1] && yax[0]>yax[1]) {
			sidelobes.add(yax[0]);
			sllphi.add(phi[0]);
		}
		
		if(yax[nQuant-1]>yax[0] && yax[nQuant-1]>yax[nQuant-2]) {
			sidelobes.add(yax[nQuant-1]);
			sllphi.add(phi[nQuant-1]);
		}
		
		for(int i=1; i<nQuant-1; i++) {
			if(yax[i]>yax[i+1] && yax[i]>yax[i-1]) {
				sidelobes.add(yax[i]);
				sllphi.add(phi[i]);
			}
		}
		
		Collections.sort(sidelobes);
		
		double upperBound=Math.PI;
		double lowerBound=Math.PI;
		
		double sllreturn=20*Math.log(sidelobes.get(1)/2);
		
		for(int i=phiRef; i<(nQuant/2+phiRef) && i<(nQuant-2); i++) {
			
			if(yax[i]<yax[i-1]&&yax[i]<yax[i+1]) {
				upperBound=phi[i]-phi[phiRef];
				break;
			}	
		}
		
		for(int i=phiRef; i>2 && i>(phiRef-nQuant/2); i--) {
			
			if(yax[i]<yax[i-1]&&yax[i]<yax[i+1]) {
				lowerBound=phi[phiRef]-phi[i];
				break;
			}			
		}
		
		double bwfn=upperBound+lowerBound;
		
		double y=0;
		
		if(bwfn>(80*Math.PI/180))
			y=bwfn-80;
		
		
		
		double y1=0;
		
		for(int i=0; i<nullv.length; i++) {
			y1+=factorCir(x, nullv[i])/maxI;
		}
		
		//double y2=Math.abs(2*Math.PI*maxI*maxI/trapezoidalCir(x,0,2*Math.PI,50));
		
		double y3=Math.abs(phiZero-phiDesired);
		
		if(y3<(5*Math.PI/180)) y3=0;
		
		return sllreturn+y+y1+y3;
	}
	
	private double[][] generateBounds(int probDim)
	{
		double[][] bounds = new double[probDim][2];
		
		for(int i=0; i<6; i++) {
			bounds[i][0]=0.2;
			bounds[i][1]=1;
		}
		
		for(int i=6; i<12; i++) {
			bounds[i][0]=-Math.PI;
			bounds[i][1]=Math.PI;
		}
				
		return bounds;
	}
	
	private double factorCir(double[] x, double phi) {
		
		int dim=this.getDimension();
		
		double dphi,shi;
		
		double y1=0;
		double y2=0;
		
		for(int i=0; i<dim/2; i++) {
			dphi=(2*Math.PI*i)/dim;
			shi=(Math.cos(phi-dphi)-Math.cos(phiDesired-dphi))*dim*distance;
			y1+=x[i]*Math.cos(shi+x[dim/2+i]);
			y2+=x[i]*Math.sin(shi+x[dim/2+i]);
		}
		
		for(int i=dim/2; i<dim; i++) {
			dphi=(2*Math.PI*i)/dim;
			shi=(Math.cos(phi-dphi)-Math.cos(phiDesired-dphi))*dim*distance;
			y1+=x[i-dim/2]*Math.cos(shi-x[i]);
			y2+=x[i-dim/2]*Math.sin(shi-x[i]);
		}
		
		return Math.sqrt(y1*y1+y2*y2);
	}
	
	/*
	private double trapezoidalCir(double[] x, double upper, double lower, int N){
		
		double h=(upper-lower)/N;
		double lowerT=lower;
		
		double[] y=new double[N+1];
		
		
		
		for(int i=0; i<=N; i++) {
			y[i]=Math.abs(Math.pow(factorCir(x, lowerT), 2)*Math.sin(lowerT-Math.PI/2));
			lowerT+=h;
		}
		
		double s=y[0]+y[N];
		
		for(int i=1; i<N; i++) {
			s+=2*y[i];
		}
		
		return (h/2)*s;	
	}
	*/
		
	

}

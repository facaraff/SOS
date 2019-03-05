package applications.CEC2011;

import java.util.HashMap;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import interfaces.Problem;
import utils.MatLab;

public class P8 extends Problem {

	line[] linedata;
	line[] candidates;
	double pGen[],pLoad[],deltaP[];
	
	int nOriginalLine;
	
	//Fix Dimension 6
	public P8() //throws Exception
	{
		super(6); 
		this.setBounds(generateBounds(getDimension()));
		
		//Data Problem ref cod Matlab CEC2011
		
		pGen = new double[] {0.5,0,1.65,0,0,5.45};
		pLoad = new double[] {0.8,2.4,0.4,1.6,2.4,0};
		
		deltaP= MatLab.subtract(pGen, pLoad);
		
		nOriginalLine=7;
		linedata = new line[nOriginalLine+getDimension()];
		
		linedata[0]= new line(0,0,1,0.4,1,1,Double.MAX_VALUE,1);
		linedata[1]= new line(1,0,3,0.6,1,0.8,Double.MAX_VALUE,0);
		linedata[2]= new line(2,0,4,0.2,1,1,Double.MAX_VALUE,0);
		linedata[3]= new line(3,1,2,0.2,1,1,Double.MAX_VALUE,0);
		linedata[4]= new line(4,1,3,0.4,1,1,Double.MAX_VALUE,1);
		linedata[5]= new line(5,2,4,0.2,1,1,20,1);
		linedata[6]= new line(6,5,1,0.3,1,1,30,1);
		
		candidates = new line[15];
				
		candidates[0]= new line(0,0,1,0.4,1,1,40,1);
		candidates[1]= new line(1,0,2,0.38,1,1,38,1);
		candidates[2]= new line(2,0,3,0.6,1,0.8,60,0);
		candidates[3]= new line(3,0,4,0.2,1,1,20,0);
		candidates[4]= new line(4,0,5,0.68,1,0.7,68,0);
		candidates[5]= new line(5,1,2,0.2,1,1,20,0);
		candidates[6]= new line(6,1,3,0.4,1,1,40,1);
		candidates[7]= new line(7,1,4,0.31,1,1,31,1);
		candidates[8]= new line(8,5,1,0.3,1,1,30,1);
		candidates[9]= new line(9,2,3,0.69,1,0.82,59,1);
		candidates[10]= new line(10,2,4,0.2,1,1,20,1);
		candidates[11]= new line(11,5,2,0.48,0,1,48,0);
		candidates[12]= new line(12,3,4,0.63,0,0.75,63,0);
		candidates[13]= new line(13,3,5,0.30,0,1,30,0);
		candidates[14]= new line(14,4,5,0.61,0,0.78,61,0);	
		
		this.setBounds(generateBounds(getDimension()));
	
	}
	
	
	@Override
	public double f(double[] x) throws Exception {
		
		int n=this.getDimension();
		
		int[] xr= new int[n];
		
		for(int i=0; i<n; i++) {
			xr[i]=(int) Math.floor(x[i]);
			linedata[nOriginalLine+i]=candidates[xr[i]];
		}
		
		double[][] b=MatLab.zeros(n);
		double[][] bInverse;
		double bline;
		int from,to;
		
		for(int i=0;i<linedata.length;i++) {
			
			bline=1/linedata[i].x;
			from=linedata[i].from; to=linedata[i].to;
			
			b[from][to]-=bline; 
			b[to][from]=b[from][to];
			
			b[from][from]+=bline;
			b[to][to]+=bline;
		}
		
		b[0][0]=10000000;
		
		RealMatrix rb=new Array2DRowRealMatrix(b);
		RealMatrix rbInverse = new LUDecomposition(rb).getSolver().getInverse();
		bInverse=rbInverse.getData();
		
		double[] delta = MatLab.multiply(bInverse, deltaP);
		
		double[] pij=new double[linedata.length];
		double pen=0;
		
		for(int i=0;i<linedata.length;i++) {
			
			from=linedata[i].from; to=linedata[i].to;
			pij[i]=(delta[from]-delta[to])/linedata[i].x;
			
			pen+=5000*Math.max((Math.abs(pij[i])-linedata[i].pijmax),0);
			
		}
		
		HashMap<Integer, Integer> no= new HashMap<Integer, Integer>();
		
		for(int i=0; i<n; i++) {
			if(!no.containsKey(xr[i]))
				no.put(xr[i], 1);
			else {
				no.replace(xr[i], no.get(xr[i])+1);
				if(no.get(xr[i])==4)
					pen+=1000;
			}
		}
		
		double f=pen+30;
		
		for(int i=nOriginalLine; i<linedata.length; i++)
			f+=linedata[i].cost;
	
		return f;
	}
	



	//Bound Variable Positive (index)
	private double[][] generateBounds(int probDim)
	{
		double[][] bounds = new double[probDim][2];
			
		for (int i = 0; i < probDim; i++)
		{
			bounds[i][0] = 0;
			bounds[i][1] = 13;			
		}
			
		return bounds;
	}
	
	public class line{
		int no,from,to,nLine,overloads;
		double x,pijmax,cost;
		
		public line() {
			no=from=to=0;
			nLine=0;
			overloads=0;
			x=pijmax=cost=0;
		}
		
		public line(line c) 
		{
			this.no=c.no;
			this.from=c.from;
			this.to=c.to;
			this.x=c.x;
			this.nLine=c.nLine;
			this.pijmax=c.pijmax;
			this.cost=c.cost;
			this.overloads=c.overloads;
		}
		
		public line(int no, int from, int to, double x, int nLine, double pijmax, double cost, int overloads)
		{
			this.no=no;
			this.from=from;
			this.to=to;
			this.x=x;
			this.nLine=nLine;
			this.pijmax=pijmax;
			this.cost=cost;
			this.overloads=overloads;
		}
	}

}

package applications.CEC2011;

import static utils.MatLab.norm2;
import static utils.MatLab.transpose;
import static utils.MatLab.reShape;
import static utils.MatLab.subtract;

import interfaces.Problem;

public class P2 extends Problem { //LENNARD-JONES POTENTIAL PROBLEM
	

	public P2(int probDim) //throws Exception
	{
		super(probDim); 
		
		if(probDim%3!=0) 
			System.out.println("The dimensionality of the problem is not a multiple of 3"); 
		else
			this.setBounds(generateBounds(probDim));	
	}
	
	public double f(double[] x){return evaluatePotential(x);}
		
	private double[][] generateBounds(int probDim)
	{
		double[][] bounds = new double[probDim][2];
		bounds[0][0] = 0;  bounds[1][0] = 0; bounds[2][0] = 0;
		bounds[0][1] = 4;  bounds[1][1] = 4; bounds[2][1] = Math.PI;
			
		for (int i = 3; i < probDim; i++)
		{
			bounds[i][0] = -4 - 0.25*Math.floor((i-3)/3);
			bounds[i][1] =  4 + 0.25*Math.floor((i-3)/3);
			
			// Bound equations CEC2011 Report Pag. 6 TO CHECK!
//			bounds[i][0] = -4 - 0.25*Math.floor((i-4)/3);
//			bounds[i][1] =  4 + 0.25*Math.floor((i-4)/3);
		}
			
		return bounds;
	}
		
	/**
	 * Fitness function (Lennard-Jones potential)
	 * @param x
	 * @return fitness
	 */
	private static double evaluatePotential(double[] x)
	{
		double v = 0;
		double r = Double.NaN;
		int n = x.length/3;
		double[][] X = reShape(x,3,n);
		X = transpose(X);
		for(int i=0; i<n-1; i++)
			for(int j=i+1; j<n; j++)
			{
				r = norm2(subtract(X[i],X[j])); //XXX can be equal to 0?
				if(r==0 || v == Double.MAX_VALUE) 
					v = Double.MAX_VALUE; 
				else
				   v += (Math.pow(r,-12) -2*Math.pow(r, -6));
			}
		return v;
	}
}

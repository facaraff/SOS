package applications.CEC2011;

import static utils.MatLab.max;

import interfaces.Problem;

public class P7 extends Problem { //Spread Spectrum Radar Polyphase Code Design
	

	public P7() {super(20, new double[] {0,2*Math.PI}); }	
	public double f(double[] x){return codeDesign(x);}
		
		/**
		 * Fitness function (Spread Spectrum Radar Polyphase Code Design)
		 * @param x
		 * @return fitness
		 */
		private double codeDesign(double[] x)
		{   
			int d = this.getDimension();
			int var = 2*d-1;
			int dvar = 2*var;
			double[] hsum = new double[dvar];
			
			for (int kk=1; kk<=dvar; kk++)
			{
				if(kk%2!=0)
				{
					int i =(kk+1)/2;
					hsum[kk-1]=0;
					
					for(int j=i; j<=d; j++)
					{
						double summ = 0;
						
						for(int i1=Math.abs(2*i-j-1)+1; i1<=j; i1++)
							summ=x[i1- 1]+summ;
                  		
						hsum[kk-1]= Math.cos(summ)+hsum[kk-1];
					}
				}
				else
				{

					int i =kk/2;
					hsum[kk-1]=0;
				
					for(int j=(i+1); j<=d; j++)
					{
						double summ = 0;
						
						for(int i1=Math.abs(2*i-j)+1; i1<=j; i1++)
							summ=x[i1- 1]+summ;
                  		
						hsum[kk-1]= Math.cos(summ)+hsum[kk-1];
					}
					
					hsum[kk-1]+= 0.5;
				}
				
			}
			
			return max(hsum);
		}
}

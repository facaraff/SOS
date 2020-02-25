/**
 * @CEC2015.java
 * @author  Fabio Caraffini <fabio.caraffini@dmu.ac.uk>
 * @version 1.0
 *
 * @section Description.
 * This class implements all the 15 functions forming the CEC2015 test suite.
 * The code is a modification of the original implementation by BO Zheng (email: zheng.b1988@gmail.com) Nov. 20th 2014.
 * The working principle and the implementation of rotations, base functions and other operator has been kept euql to the original version to guarantee the same behaviour, 
 * but a significant part of the code had been modified to make it compatable and runnable within this software platform.
 */
 
package benchmarks.problemsImplementation.CEC2015;

import static benchmarks.problemsImplementation.CEC2015.MMatrix10.getM10;
import static benchmarks.problemsImplementation.CEC2015.MMatrix30.getM30;
import static utils.RunAndStore.slash;

import static utils.benchmarks.ProblemsTransformations.sr_func;
import static utils.benchmarks.ProblemsTransformations.cf_cal;

import java.util.Scanner;

/**
 * CEC15 HELPER
 */
public class CEC2015TestFunc
{ 
	final static double INF = 1.0e99;
	final static double EPS = 1.0e-14;
	final static double E  = 2.7182818284590452353602874713526625;
	final static double PI = 3.1415926535897932384626433832795029;
	
	final public int[] bShuffle = {0, 0,0,0,0,0,1,1,1,0,1, 0, 0, 1, 0, 0};
	final public double[] OShift,M, bias;
	final public int func_num, cf_num, nx;
	final public int[] SS;
	final public String slash;

	final public static int[] cf_nums =  {0, 1,1,1,1,1,1,1,1,3,3, 5, 5, 5, 7, 10};

	final public static double[][] BIAS = {
		{0.0000000000000000e+000,2.0000000000000000e+002,1.0000000000000000e+002,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003},
		{0.0000000000000000e+000,2.0000000000000000e+002,1.0000000000000000e+002,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003},
		{0.0000000000000000e+000,1.0000000000000000e+002,2.0000000000000000e+002,4.0000000000000000e+002,3.0000000000000000e+002,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003},
		{0.0000000000000000e+000,1.0000000000000000e+002,2.0000000000000000e+002,2.0000000000000000e+002,1.0000000000000000e+002,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003},
		{2.0000000000000000e+002,4.0000000000000000e+002,3.0000000000000000e+002,1.0000000000000000e+002,0.0000000000000000e+000,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003},
		{0.0000000000000000e+000,1.0000000000000000e+002,4.0000000000000000e+002,2.0000000000000000e+002,3.0000000000000000e+002,4.0000000000000000e+002,3.0000000000000000e+002,1.0000000000000000e+003,1.0000000000000000e+003,1.0000000000000000e+003},
		{3.0000000000000000e+002,5.0000000000000000e+002,3.0000000000000000e+002,1.0000000000000000e+002,4.0000000000000000e+002,2.0000000000000000e+002,4.0000000000000000e+002,0.0000000000000000e+000,1.0000000000000000e+002,2.0000000000000000e+002}
	};
	
	 
	
	public CEC2015TestFunc(int nx, int func_num) throws Exception
	{
		this.nx=nx;
		this.func_num=func_num;
		this.cf_num=getCF_NUM(func_num);
		this.slash= slash();
		warnings();
		this.M=getM();
		this.bias=getBias();
		this.OShift=getShift();
		this.SS=getShuffle();
	}
	
	/**
	* fitness evaluation. OVERRRIDE
	* @param x input vector to be evaluated.
	* @param func_num index of the problem to be used (15 problems in this suite!).
	* return f the fitness value f=f(x).
	*/
	public double f(double[] x)
	{	
		double f = Double.NaN;
		switch(this.func_num)
		{
		case 1:	
			f=ellips_func(x,nx,this.OShift,this.M,1,1);
			f+=100.0;
			break;
		case 2:	
			f=bent_cigar_func(x,nx,this.OShift,this.M,1,1);
			f+=200.0;
			break;
		case 3:	
			f=ackley_func(x,nx,OShift,M,1,1);
			f+=300.0;
			break;
		case 4:	
			f=rastrigin_func(x,nx,this.OShift,this.M,1,1);
			f+=400.0;
			break;
		case 5:
			f=schwefel_func(x,nx,this.OShift,this.M,1,1);
			f+=500.0;
			break;
		case 6:
			f=hf01(x,nx,this.OShift,this.M,SS,1,1);
			f+=600.0;
			break;
		case 7:	
			f=hf02(x, nx,this.OShift,this.M,SS,1,1);
			f+=700.0;
		break;
		case 8:	
			f=hf03(x, nx,this.OShift,this.M,SS,1,1);
			f+=800.0;
		break;
		case 9:	
			f=cf01(x,nx,this.OShift,this.M,bias,1);
			f+=900.0;
			break;
		case 10:	
			f=cf02(x,nx,this.OShift,this.M,SS,bias,1);
			f+=1000.0;
			break;
		case 11:	
			f=cf03(x,nx,this.OShift,M,bias,1);
			f+=1100.0;
			break;
		case 12:	
			f=cf04(x,nx,this.OShift,this.M,bias,1);
			f+=1200.0;
			break;
		case 13:	
			f=cf05(x, nx,this.OShift,this.M,SS,bias,1);
			f+=1300.0;
			break;
		case 14:	
			f=cf06(x,nx,this.OShift,this.M,bias,1);
			f+=1400.0;
		break;
		case 15:	
			f=cf07(x,nx,this.OShift,this.M,bias,1);
			f+=1500.0;
			break;
			
		default:
			System.out.println("\nError: There are only 15 test functions in this test suite!");
			f = Double.NaN;
			break;
		}

		return f;
	}
	
	public  int getCF_NUM(int funNr){return cf_nums[funNr];}
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    M Data    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	public double[] getM() throws Exception
	{
		
			
		double[] em = null;

		if(nx==10 || nx== 30)
		{
			em=getM(func_num, nx);
		}
		else
		{	
			Scanner input = new Scanner(this.getClass().getResourceAsStream("files_cec2015"+slash+"M_"+func_num+"_D"+nx+".txt"));
			
			em=new double[cf_num*nx*nx]; 
					
			for (int i=0;i<cf_num*nx*nx; i++)//oppure un while(input.hasNextDouble???)
				em[i]=input.nextDouble();
			
			input.close();	
		}
		return em;
	}
	
	public static double[] getM(int funNr, int probDim)
	{
		double[] array=null;
	
		switch(probDim)
		{
		case 2:	
			System.out.println("Ma chi vuole usar questa dimensionalita? devo ancora implementarla ma mi rifiuto");
			break;
		case 10:	
			array=getM10(funNr);
			break;
		case 30:	
			array=getM30(funNr);
			break;
		case 50:	
			System.out.println("TO DO? there is no way to store the constant! they need to be generated!");
			break;
		case 100:
			System.out.println("TO DO");
			break;
		default:
			System.out.println("Error:\n This dimensionality value is not supported!");
			array=null;
			break;
		}

		return array;
		}

	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    Bias Data    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	public  double[] getBias()
	{
		double[] array = new double[cf_num];
			if(func_num==9)
				fillAWithB(array,BIAS[0],cf_num);
			else if(func_num==10)
				fillAWithB(array,BIAS[1],cf_num);
			else if(func_num==11)
				fillAWithB(array,BIAS[2],cf_num);
			else if(func_num==12)
				fillAWithB(array,BIAS[3],cf_num);
			else if(func_num==13)
				fillAWithB(array,BIAS[4],cf_num);
			else if(func_num==14)
				fillAWithB(array,BIAS[5],cf_num);
			else if(func_num==15)
				fillAWithB(array,BIAS[6],cf_num);

			return array;
	}

	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    Shift Data    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		public double[] getShift() throws Exception
		{
			Scanner input = new Scanner(this.getClass().getResourceAsStream("files_cec2015"+slash+"shift_data_"+func_num+".txt"));
			
			double[] OS=null;
			
			OS=new double[cf_num*nx];


		if(func_num<9)
		{
			for (int i=0;i<nx*cf_num;i++)
				OS[i]=input.nextDouble();
			
		}
		else
		{
			for(int i=0;i<cf_num-1;i++)
			{
				for(int j=0;j<nx;j++)
					OS[i*nx+j]=input.nextDouble();
	
			}
			for(int j=0;j<nx;j++)
				OS[(cf_num-1)*nx+j]=input.nextDouble();
				
		}
		input.close();
		
		return OS;
		}

	
		//%%%%%%%%%%%%%%%%%%%%Load Shuffle_data%%%%%%%%%%%%%%%%%%%%%%%/	
		
		public int[] getShuffle() throws Exception
		{
			

			
			
			int[] S=null;
			
			if(bShuffle[func_num]==1)
			{	
				Scanner input = new Scanner(this.getClass().getResourceAsStream("files_cec2015"+slash+"shuffle_data_"+func_num+"_D"+nx+".txt"));
				
				S = new int[cf_num*nx];
					
				for(int i=0;i<cf_num*nx;i++)
						S[i] = input.nextInt();
			
				input.close();
			}
			return S;
		}
	
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%        ELEMENTARY FUNCTIONS		%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


	public static double ellips_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag, int r_flag) /* Ellipsoidal */
	{
	    int i;
		double f = 0.0;
		double[] z=new double[nx];
	    sr_func(x,z,nx,Os,Mr,1.0,s_flag,r_flag);/*shift and rotate*/
	    for (i=0; i<nx; i++)
	    {
			f += z[i]*z[i]*Math.pow(10.0,6.0*i/(nx-1));
	    }
	    return f;
	}
	
	public static double bent_cigar_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Bent_Cigar */
	{
		int i;
		double[] z=new double[nx];
		sr_func(x,z,nx,Os,Mr,1.0,s_flag,r_flag);/*shift and rotate*/
	    
		double f = z[0]*z[0];
		for (i=1; i<nx; i++)
	    {
	        f += Math.pow(10.0,6.0)*z[i]*z[i];
	    }
		
	    return f;
	}
		
	public static double rosenbrock_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Rosenbrock's */
	{
	    int i;
		double tmp1,tmp2;
		double f = 0.0;
		double[] z=new double[nx];
	    sr_func(x,z,nx,Os,Mr,2.048/100.0,s_flag,r_flag);/*shift and rotate*/
	    z[0] +=1.0; //shift to origin
	    for (i=0; i<nx-1; i++)
	    {
			z[i+1] += 1.0; //shift to orgin
	    	tmp1=z[i]*z[i]-z[i+1];
			tmp2=z[i]-1.0;
	        f += 100.0*tmp1*tmp1 +tmp2*tmp2;
	    }
	    
	    
	    return f;
	}
	
	public static double ackley_func(double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Ackley's  */
	{
	    int i;
	    double sum1, sum2;
	    sum1 = 0.0;
	    sum2 = 0.0;
	    double[] z=new double[nx];
	    sr_func(x,z,nx,Os,Mr,1.0,s_flag,r_flag);/*shift and rotate*/ 		
	    
	    for (i=0; i<nx; i++)
	    {
	        sum1 += z[i]*z[i];
	        sum2 += Math.cos(2.0*PI*z[i]);
	    }
	    sum1 = -0.2*Math.sqrt(sum1/nx);
	    sum2 /= nx;
	    double f =  E - 20.0*Math.exp(sum1) - Math.exp(sum2) +20.0;
	    
	    return f;
	}
	
	public static double weierstrass_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Weierstrass's  */
	{
	    int i,j,k_max;
	    double sum,sum2=0, a, b;
	    double[] z=new double[nx];
	    sr_func(x,z,nx,Os,Mr,0.5/100.0,s_flag,r_flag);/*shift and rotate*/ 
		
			   
	    a = 0.5;
	    b = 3.0;
	    k_max = 20;
	    double f = 0.0;
	    for (i=0; i<nx; i++)
	    {
	        sum = 0.0;
			sum2 = 0.0;
	        for (j=0; j<=k_max; j++)
	        {
	            sum += Math.pow(a,j)*Math.cos(2.0*PI*Math.pow(b,j)*(z[i]+0.5));
				sum2 += Math.pow(a,j)*Math.cos(2.0*PI*Math.pow(b,j)*0.5);
	        }
	        f += sum;
	    }
		f -= nx*sum2;
		
		return f;
    
	}
	
	public static double griewank_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Griewank's  */
	{
	    int i;
	    double s, p;
		double[] z=new double[nx];

	    sr_func(x,z,nx,Os,Mr,600.0/100.0,s_flag,r_flag);/*shift and rotate*/ 

	    s = 0.0;
	    p = 1.0;
	    for (i=0; i<nx; i++)
	    {
	        s += z[i]*z[i];
	        p *= Math.cos(z[i]/Math.sqrt(1.0+i));
	    }
	    double f = 1.0 + s/4000.0 - p;
	    
	    return f;
	}
	
	public static double rastrigin_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Rastrigin's  */
	{
	    int i;
		double f=0.0;
		double[] z=new double[nx];
	    
		sr_func(x,z,nx,Os,Mr,5.12/100.0,s_flag,r_flag);/*shift and rotate*/ 

		for(i=0;i<nx;i++)
		{
			f += (z[i]*z[i] - 10.0*Math.cos(2.0*PI*z[i]) + 10.0);
		}
	    
	    return f;
	}
		
	public static double schwefel_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Schwefel's  */
	{
	    int i;
		double tmp;
		double[] z=new double[nx];
	
		sr_func(x,z,nx,Os,Mr,1000.0/100.0,s_flag,r_flag);/*shift and rotate*/ 
				
	    double f=0;
	    for (i=0; i<nx; i++)
		{
	    	z[i] += 4.209687462275036e+002;
			
	    	if (z[i]>500)
			{
				f-=(500.0-(z[i]%500))*Math.sin(Math.pow(500.0-(z[i]%500),0.5));
				tmp=(z[i]-500.0)/100;
				f+= tmp*tmp/nx;
			}
			else if (z[i]<-500)
			{
				f-=(-500.0+(Math.abs(z[i])%500))*Math.sin(Math.pow(500.0-(Math.abs(z[i])%500),0.5));
				tmp=(z[i]+500.0)/100;
				f+= tmp*tmp/nx;
			}
			else
				f-=z[i]*Math.sin(Math.pow(Math.abs(z[i]),0.5));

				

	    }
	    f=4.189828872724338e+002*nx+f;
	   
	    return f;
	}

	
	public static double katsuura_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Katsuura  */
	{
	    int i,j;
		double temp,tmp1,tmp2,tmp3;
		tmp3=Math.pow(1.0*nx,1.2);
		double[] z=new double[nx];
		sr_func(x,z,nx,Os,Mr,5/100.0,s_flag,r_flag);/*shift and rotate*/ 
		
	    
	    double f=1.0;
	    for (i=0; i<nx; i++)
		{
			temp=0.0;
			for (j=1; j<=32; j++)
			{
				tmp1=Math.pow(2.0,j);
				tmp2=tmp1*z[i];
				temp += Math.abs(tmp2-Math.floor(tmp2+0.5))/tmp1;
			}
			f *= Math.pow(1.0+(i+1)*temp,10.0/tmp3);
	    }
		tmp1=10.0/nx/nx;
	    f=f*tmp1-tmp1;
	    
	    return f;

	}
	
	public static double happycat_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) 
	/*HappyCat, probided by Hans-Georg Beyer (HGB)*/
	/*original global optimum: [-1,-1,...,-1]*/
	{
		int i;
		double alpha,r2,sum_z;
		alpha = 1.0/8.0;
		double[] z=new double[nx];
		sr_func(x,z,nx,Os,Mr,5/100.0,s_flag,r_flag);/*shift and rotate*/ 
		
		r2 = 0.0;
		sum_z = 0.0;
		double f = 0.0;
		for (i=0;i<nx;i++)
		{
			z[i] = z[i] - 1.0; //shift to orgin
			r2 += z[i]*z[i];
			sum_z += z[i];
			
		}
		f = Math.pow(Math.abs(r2-nx), 2*alpha) + (0.5*r2 + sum_z)/nx + 0.5;
		
		return f;
	}
	
	public static double hgbat_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) 
	/*HGBat, provided by Hans-Georg Beyer (HGB)*/
	/*original global optimum: [-1,-1,...-1]*/
	{
		int i;
		double alpha,r2,sum_z;
		alpha=1.0/4.0;
		double[] z=new double[nx];
		sr_func (x, z, nx, Os, Mr, 5.0/100.0, s_flag, r_flag); /* shift and rotate */

		r2 = 0.0;
		sum_z=0.0;
	    for (i=0; i<nx; i++)
	    {
			z[i]=z[i]-1.0;//shift to orgin
	        r2 += z[i]*z[i];
			sum_z += z[i];
	    }	
		return Math.pow(Math.abs(Math.pow(r2,2.0)-Math.pow(sum_z,2.0)),2*alpha) + (0.5*r2 + sum_z)/nx + 0.5;


	}
	public static double grie_rosen_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Griewank-Rosenbrock  */
	{
	    int i;
	    double temp,tmp1,tmp2;
	    double[] z=new double[nx];
	    sr_func (x, z, nx, Os, Mr, 5.0/100.0, s_flag, r_flag); /* shift and rotate */

		
	    double f=0.0;
	    
	    z[0] += 1.0; //shift to orgin
	    for (i=0; i<nx-1; i++)
	    {
	    	z[i+1] += 1.0; //shift to orgin
			tmp1 = z[i]*z[i]-z[i+1];
			tmp2 = z[i]-1.0;
	        temp = 100.0*tmp1*tmp1 + tmp2*tmp2;
	         f += (temp*temp)/4000.0 - Math.cos(temp) + 1.0;
	    }
		tmp1 = z[nx-1]*z[nx-1]-z[0];
		tmp2 = z[nx-1]-1.0;
	    temp = 100.0*tmp1*tmp1 + tmp2*tmp2;;
	     f += (temp*temp)/4000.0 - Math.cos(temp) + 1.0 ;
	     
	     return f;
	}
	
	public static double escaffer6_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Expanded Scaffer¡¯s F6  */
	{
	    int i;
	    double temp1, temp2;
	    double[] z=new double[nx];
	    sr_func (x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

		
	    double f = 0.0;
	    for (i=0; i<nx-1; i++)
	    {
	        temp1 = Math.sin(Math.sqrt(z[i]*z[i]+z[i+1]*z[i+1]));
			temp1 =temp1*temp1;
	        temp2 = 1.0 + 0.001*(z[i]*z[i]+z[i+1]*z[i+1]);
	        f += 0.5 + (temp1-0.5)/(temp2*temp2);
	    }
	    temp1 = Math.sin(Math.sqrt(z[nx-1]*z[nx-1]+z[0]*z[0]));
		temp1 =temp1*temp1;
	    temp2 = 1.0 + 0.001*(z[nx-1]*z[nx-1]+z[0]*z[0]);
	    f += 0.5 + (temp1-0.5)/(temp2*temp2);
	    
	    return f;
	}

	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//%%%%%%   COMPOSITE/HYBRID FUNCTIONS  %%%%
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


	public static double hf01 (double[] x, int nx, double[] Os,double[] Mr,int[] S,int s_flag,int r_flag) /* Hybrid Function 1 */
	{
		int i,tmp,cf_num=3;
		double[] fit = new double[3];
		int[] G = new int[3];
		int[] G_nx = new int[3];
		double[] Gp = {0.3,0.3,0.4};
		double[] z=new double[nx];
		tmp=0;
		for (i=0; i<cf_num-1; i++)
		{
			G_nx[i] = (int)Math.ceil(Gp[i]*nx);
			tmp += G_nx[i];
		}
		G_nx[cf_num-1]=nx-tmp;
		G[0]=0;
		for (i=1; i<cf_num; i++)
		{
			G[i] = G[i-1]+G_nx[i-1];
		}

		sr_func (x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
		double[] y = new double[nx];
		for (i=0; i<nx; i++)
		{
			y[i]=z[S[i]-1];
		}
		
		
		
		
		double[] ty,tO,tM;
		
		i=0;
		 ty = new double[G_nx[i]];
		 tO = new double[G_nx[i]];
		 tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[ii];
			tO[ii]=Os[ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=schwefel_func(ty, G_nx[i],tO,tM,0,0);
		
		i=1;
		
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[G_nx[i-1]+ii];
			tO[ii]=Os[G_nx[i-1]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=rastrigin_func(ty,G_nx[i],tO,tM,0,0);
		
		i=2;
		
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[G_nx[i-2]+G_nx[i-1]+ii];
			tO[ii]=Os[G_nx[i-2]+G_nx[i-1]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=ellips_func(ty,G_nx[i],tO,tM,0,0);
		
		double f=0.0;
		for(i=0;i<cf_num;i++)
		{
			f += fit[i];
		}
		
		return f;
	}


	
	public static double hf02 (double[] x, int nx, double[] Os,double[] Mr,int[] S,int s_flag,int r_flag) /* Hybrid Function 2 */
	{
		int i,tmp,cf_num=4;
		double[] fit = new double[4];
		int[] G_nx = new int[4];
		int[] G = new int[4];
		double[] Gp={0.2,0.2,0.3,0.3};
		double[] z=new double[nx];
		tmp=0;
		for (i=0; i<cf_num-1; i++)
		{
			G_nx[i] = (int)Math.ceil(Gp[i]*nx);
			tmp += G_nx[i];
		}
		G_nx[cf_num-1]=nx-tmp;

		G[0]=0;
		for (i=1; i<cf_num; i++)
		{
			G[i] = G[i-1]+G_nx[i-1];
		}

		sr_func (x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
		double[] y = new double[nx];
		for (i=0; i<nx; i++)
		{
			y[i]=z[S[i]-1];
		}
		
		
		double[] ty,tO,tM;
		
		i=0;
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[ii];
			tO[ii]=Os[ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=griewank_func(ty,G_nx[i],tO,tM,0,0);
		
		i=1;
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[G_nx[i-1]+ii];
			tO[ii]=Os[G_nx[i-1]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=weierstrass_func(ty,G_nx[i],tO,tM,0,0);

		i=2;
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[G_nx[i-1]+G_nx[i-2]+ii];
			tO[ii]=Os[G_nx[i-1]+G_nx[i-2]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=rosenbrock_func(ty,G_nx[i],tO,tM,0,0);


		i=3;
		
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[G_nx[i-1]+G_nx[i-2]+G_nx[i-3]+ii];
			tO[ii]=Os[G_nx[i-1]+G_nx[i-2]+G_nx[i-3]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=escaffer6_func(ty,G_nx[i],tO,tM,0,0);

		
		double f=0.0;
		for(i=0;i<cf_num;i++)
		{
			f += fit[i];
		}
		return f;

	}
	
	public static double hf03 (double[] x, int nx, double[] Os,double[] Mr,int[] S,int s_flag,int r_flag) /* Hybrid Function 3 */
	{
		int i,tmp,cf_num=5;
		double[] fit = new double[5];
		int[] G = new int[5];
		int[] G_nx = new int[5];
		double[] Gp={0.1,0.2,0.2,0.2,0.3};
		double[] z=new double[nx];
		tmp=0;
		for (i=0; i<cf_num-1; i++)
		{
			G_nx[i] = (int)Math.ceil(Gp[i]*nx);
			tmp += G_nx[i];
		}
		G_nx[cf_num-1]=nx-tmp;

		G[0]=0;
		for (i=1; i<cf_num; i++)
		{
			G[i] = G[i-1]+G_nx[i-1];
		}
				
		  
		sr_func (x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

		
		double[] y = new double[nx];
		for (i=0; i<nx; i++)
		{
			y[i]=z[S[i]-1];
		}
		
					 
		
		
        double[] ty,tO,tM;
		
		i=0;
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[ii];
			tO[ii]=Os[ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=escaffer6_func(ty,G_nx[i],tO,tM,0,0);
		
		i=1;
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[G_nx[i-1]+ii];
			tO[ii]=Os[G_nx[i-1]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=hgbat_func(ty,G_nx[i],tO,tM,0,0);
		
		i=2;
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[G_nx[i-1]+G_nx[i-2]+ii];
			tO[ii]=Os[G_nx[i-1]+G_nx[i-2]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		
		fit[i]=rosenbrock_func(ty, G_nx[i],tO,tM,0,0);

		i=3;
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[G_nx[i-1]+G_nx[i-2]+G_nx[i-3]+ii];
			tO[ii]=Os[G_nx[i-1]+G_nx[i-2]+G_nx[i-3]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=schwefel_func(ty, G_nx[i],tO,tM,0,0);
		i=4;
		ty = new double[G_nx[i]];
		tO = new double[G_nx[i]];
		tM = new double[G_nx[i]];
		for(int ii=0;ii<G_nx[i];ii++)
		{
			ty[ii]=y[G_nx[i-1]+G_nx[i-2]+G_nx[i-3]+G_nx[i-4]+ii];
			tO[ii]=Os[G_nx[i-1]+G_nx[i-2]+G_nx[i-3]+G_nx[i-4]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=ellips_func(ty, G_nx[i],tO,tM,0,0);
		
		
		double f=0.0;
		for(i=0;i<cf_num;i++)
		{
			f += fit[i];
		}
		return f;

	}
	
	

	
	public static double cf01 (double[] x, int nx, double[] Os,double[] Mr,double[] bias,int r_flag) /* Composition Function 1 */
	{
		int i,j,cf_num=3;
		double[] fit = new double[3];
		double[] delta = {20,20,20};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,1,0);

			
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=rastrigin_func(x,nx,tOs,tMr,1,r_flag);
		
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=hgbat_func(x,nx,tOs,tMr,1,r_flag);
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	public static double cf02 (double[] x, int nx, double[] Os,double[]Mr,int[] SS,double[] bias,int r_flag) /* Composition Function 2 */
	{
		int i,j,cf_num=3;
		double[] fit=new double[3];
		double[] delta = {10,30,50};	
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		int[] tSS = new int[nx];
		
		
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf01(x, nx,tOs,tMr,tSS,1,r_flag);
			
			
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf02(x, nx,tOs,tMr,tSS,1,r_flag);
		
		
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf03(x, nx,tOs,tMr,tSS,1,r_flag);
		
		
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	public static double cf03 (double[] x, int nx, double[] Os,double[] Mr,double[] bias,int r_flag) /* Composition Function 3 */
	{
		int i,j,cf_num=5;
		double[] fit=new double[5];
		double[] delta = {10,10,10,20,20};
	
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
			
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
			
		}
		fit[i]=hgbat_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1000;
		
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		
		}
		fit[i]=rastrigin_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+3;
		
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=weierstrass_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/400;
		
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=ellips_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+10;
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	public static double cf04 (double[] x, int nx, double[] Os,double[] Mr,double[] bias,int r_flag) /* Composition Function 4 */
	{
		int i,j,cf_num=5;
		double[] fit=new double[5];
		double[] delta = {10,20,20,30,30};
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=rastrigin_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+3;
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=ellips_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+10;
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=escaffer6_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=fit[i]*10;
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=happycat_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+3;
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	public static double cf05 (double[] x, int nx, double[] Os,double[] Mr,int[] SS,double[] bias,int r_flag) /* Composition Function 5 */
	{
		int i,j,cf_num=5;
		double[] fit=new double[5];
		double[] delta = {10,10,10,20,20};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		int[] tSS = new int[nx];
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf03(x, nx,tOs,tMr,tSS,1,r_flag);
		
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=rastrigin_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+3;
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf01(x,nx,tOs,tMr,tSS,1,r_flag);
		
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=escaffer6_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=fit[i]*10;
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	public static double cf06 (double[] x, int nx, double[] Os,double[] Mr,double[] bias,int r_flag) /* Composition Function 6 */
	{
		int i,j,cf_num=7;
		double[] fit=new double[7];
		double[] delta = {10,20,30,40,50,50,50};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
				
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
			
		fit[i]=happycat_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+3;
			
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=grie_rosen_func(x, nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=schwefel_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=escaffer6_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=fit[i]*10;
		
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=ellips_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+10;
		
		i=5;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=bent_cigar_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+10;
		
		i=6;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=rastrigin_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+3;
		
		
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	public static double cf07 (double[] x, int nx, double[] Os,double[]Mr,double[] bias,int r_flag) /* Composition Function 7 */
	{
		int i,j,cf_num=10;
		double[] fit=new double[10];
		double[] delta = {10,10,20,20,30,30,40, 40, 50, 50};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
			
		fit[i]=rastrigin_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=100*fit[i]/1e+3;
			
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=weierstrass_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=100*fit[i]/400;
		
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=happycat_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=100*fit[i]/1e+3;
		
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=schwefel_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=100*fit[i]/4e+3;
		
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=rosenbrock_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=100*fit[i]/1e+5;
		
		i=5;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=hgbat_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=100*fit[i]/1000;
		
		i=6;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=katsuura_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=100*fit[i]/1e+7;
		
		i=7;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=escaffer6_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=fit[i]*10;
		
		i=8;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=grie_rosen_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=100*fit[i]/4e+3;
		
		i=9;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		
		fit[i]=ackley_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=100*fit[i]/1e+5;
						
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	


	public static void fillAWithB(double[] A, double[] B, int n)
	{
		for(int i=0; i<n; i++)
			A[i]=B[i];
	}
	
	public void warnings() 
	{
		if (!(nx==2||nx==10||nx==30||nx==50||nx==100)) 
		{
			System.out.println("\nError: Test functions are only defined for 10,30,50,100.");
		}
	}


}


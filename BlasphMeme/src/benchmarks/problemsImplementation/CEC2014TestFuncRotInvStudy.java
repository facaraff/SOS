/*
  CEC14 Test Function Suite for Single Objective Optimization
  BO Zheng (email: zheng.b1988@gmail.com) 
  Dec. 5th 2013
 */
package benchmarks.problemsImplementation;

import java.io.InputStreamReader;

import java.io.BufferedReader;

import java.util.Scanner;

import static utils.RunAndStore.slash;
import static utils.benchmarks.ProblemsTransformations.sr_func;
import static utils.benchmarks.ProblemsTransformations.cf_cal;

public class CEC2014TestFuncRotInvStudy {
	//final double INF = 1.0e99;
	final double EPS = 1.0e-14;
	final double E  = 2.7182818284590452353602874713526625;
	final double PI = 3.1415926535897932384626433832795029;
	final double[] OShift, M;
	final int[] SS;
	final int nx, fNumber;
	int rotation=1;
	String slash = slash();


	public CEC2014TestFuncRotInvStudy(int nx, int func_num, int rot)throws Exception
	{
		this.fNumber=func_num;
		this.nx = nx;
		int cf_num=10;
		this.M = getM(nx,func_num, cf_num);
		this.OShift = getShift(nx, func_num, cf_num);
		this.rotation = rot;
		warnings(nx, func_num);
		
		if ((func_num>=17&&func_num<=22)||func_num==29||func_num==30)
			SS=getShuffle(nx,func_num, cf_num);
		else
			SS=null;
		
	}
	
	public CEC2014TestFuncRotInvStudy(int nx, int func_num)throws Exception
	{
		this.fNumber=func_num;
		this.nx = nx;
		int cf_num=10;
		this.M = getM(nx,func_num, cf_num);
		this.OShift = getShift(nx, func_num, cf_num);
		warnings(nx, func_num);
		
		if ((func_num>=17&&func_num<=22)||func_num==29||func_num==30)
			SS=getShuffle(nx,func_num, cf_num);
		else
			SS=null;
		
	}
	
	public void setRotation(int rot) { this.rotation=rot;}
	
	public void printRotation() { System.out.println("rotation flag = "+rotation);}
	
	
	public double f(double[] x)
	{		
		double f = Double.NaN;
		
		switch(this.fNumber)
		{
		case 1:	
			f=ellips_func(x,1,rotation);
			f+=100.0;
			break;
		case 2:	
			f=bent_cigar_func(x,1,rotation);
			f+=200.0;
			break;
		case 3:	
			f=discus_func(x,1,rotation);
			f+=300.0;
			break;
		case 4:	
			f=rosenbrock_func(x,1,rotation);
			f+=400.0;
			break;
		case 5:
			f=ackley_func(x,1,rotation);
			f+=500.0;
			break;
		case 6:
			f=weierstrass_func(x,1,rotation);
			f+=600.0;
			break;
		case 7:	
			f=griewank_func(x,1,rotation);
			f+=700.0;
			break;
		case 8:	
			//f=rastrigin_func(x,1,0);
			//f+=800.0;
			f=Double.NaN;
			break;
		case 9:	
			f=rastrigin_func(x,1,rotation);
			f+=900.0;
			break;
		case 10:	
//			f=schwefel_func(x,1,0);
//			f+=1000.0;
			f=Double.NaN;
			break;
		case 11:	
			f=schwefel_func(x,1,rotation);
			f+=1100.0;
			break;
		case 12:	
			f=katsuura_func(x,1,rotation);
			f+=1200.0;
			break;
		case 13:	
			f=happycat_func(x,1,rotation);
			f+=1300.0;
			break;
		case 14:	
			f=hgbat_func(x,1,rotation);
			f+=1400.0;
			break;
		case 15:	
			f=grie_rosen_func(x,1,rotation);
			f+=1500.0;
			break;
		case 16:	
			f=escaffer6_func(x,1,rotation);
			f+=1600.0;
			break;
		case 17:
			f=hf01(x,1,rotation);
			f+=1700.0;
			break;
		case 18:	
			f=hf02(x,1,rotation);
			f+=1800.0;
			break;
		case 19:	
			f=hf03(x,1,rotation);
			f+=1900.0;
			break;
		case 20:	
			f=hf04(x,1,rotation);
			f+=2000.0;
			break;
		case 21:	
			f=hf05(x,1,rotation);
			f+=2100.0;
			break;
		case 22:	
			f=hf06(x,1,rotation);
			f+=2200.0;
			break;
		case 23:	
			f=cf01(x,rotation);
			f+=2300.0;
			break;
		case 24:	
			f=cf02(x,rotation);
			f+=2400.0;
			break;
		case 25:	
			f=cf03(x,rotation);
			f+=2500.0;
			break;
		case 26:
			f=cf04(x,rotation);
			f+=2600.0;
			break;
		case 27:
			f=cf05(x,rotation);
			f+=2700.0;
			break;
		case 28:
			f=cf06(x,rotation);
			f+=2800.0;
			break;
		case 29:
			f=cf07(x,rotation);
			f+=2900.0;
			break;
		case 30:
			f=cf08(x,rotation);
			f+=3000.0;
			break;

		default:
			System.out.println("\nError: There are only 30 test functions in this test suite!");
			f = 0.0;
			break;
		}
		
		return f;
	}
		
		
		
	private double[] getM(int nx, int func_num, int cf_num) throws Exception
	{
		//LOAD M MATRIX		
		double[] M;
		
		
		Scanner input = new Scanner(this.getClass().getResourceAsStream("files_cec2014"+slash+"M_"+func_num+"_D"+nx+".txt"));
		
		
		if (func_num<23)
		{
			M=new double[nx*nx]; 

			for (int i=0;i<nx*nx; i++)
			{
				String next = input.next();
				M[i]=Double.parseDouble(next);
			}
		}
		else
		{
			M=new double[cf_num*nx*nx]; 

			for (int i=0; i<cf_num*nx*nx; i++)
			{
				String next = input.next();
				M[i]=Double.parseDouble(next);
			}

		}
		input.close();
		
		return M;
	}
	
	private double[] getShift(int nx, int func_num, int cf_num) throws Exception
	{
		//Load shift_data
		//File fpt; 
		Scanner input; 
		double[] OShift;
		if (func_num<23)
		{
			 input = new Scanner(this.getClass().getResourceAsStream("files_cec2014"+slash+"shift_data_"+func_num+".txt"));
			

			OShift=new double[nx];
			for(int i=0;i<nx;i++)
			{
				String next = input.next();
				OShift[i]=Double.parseDouble(next);
			}
			input.close();
		}
		else
		{
			InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream("files_cec2014"+slash+"shift_data_"+func_num+".txt"));
			OShift=new double[nx*cf_num];


			BufferedReader br = new BufferedReader(isr);
			String[] s = new String[100];

			for (int i=0;i<cf_num;i++){
				s[i] = br.readLine();
				String[] array = s[i].split("\\s+");
				double[] temp = new double[array.length-1];

				for ( int k = 0; k < array.length-1; k++) {
					temp[k]= Double.parseDouble(array[k+1]);
				}

				for (int j=0;j<nx;j++){
					OShift[i*nx+j] = temp[j];
				}

			}	
			br.close();

			isr.close();
		
		}
		return OShift;
	}
	
	private int[] getShuffle(int nx, int func_num, int cf_num) throws Exception
	{
		int[] SS=null;
		Scanner input = new Scanner(this.getClass().getResourceAsStream("files_cec2014"+slash+"shuffle_data_"+func_num+"_D"+nx+".txt"));
		//File fpt; 
		if (func_num>=17&&func_num<=22)
		{
			


			SS = new int[nx];

			for(int i=0;i<nx;i++)
				SS[i] = input.nextInt();	
		}
		else if (func_num==29||func_num==30)
		{

			
			SS = new int[nx*cf_num];

			for(int i=0;i<nx*cf_num;i++)
			{
				SS[i] = input.nextInt();
			}input.close();

		}else 
		{
			System.out.println("No shuffle file for  this problem!");
			input.close();
		}
		
		return SS;
	}

	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//%%%%%%%%%%%%%%%%%%          TEST FUNCTIONS IMPLEMENTATION
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	/**************** Ellipsoidal *********************/
	
	double ellips_func(double[] x, int s_flag, int r_flag) 
	{
		return ellips_func(x, nx, OShift, M, s_flag,r_flag);
	}

	double ellips_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Ellipsoidal */
	{
		int i;
		double f = 0.0;
		double[] z=new double[nx];
		sr_func(x,z,nx,Os,Mr,1.0,s_flag,r_flag);/*shift and rotate*/
		for (i=0; i<nx; i++)
			f += Math.pow(10.0,6.0*i/(nx-1))*z[i]*z[i];
		return f;
	}

	/**************** Bent_Cigar *********************/
		
	// OVERLOADED BENT CIGAR
	double bent_cigar_func (double[] x,int s_flag,int r_flag) /* Bent_Cigar */
	{
		return bent_cigar_func(x, nx, OShift, M, s_flag,r_flag);
	}
	
	//BENT CIGAR FUNCTION
	double bent_cigar_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) 
	{
		int i;
		double f=0;
		double[] z = new double[nx];
		sr_func(x,z,nx,Os,Mr,1.0,s_flag,r_flag);/*shift and rotate*/
		f = z[0]*z[0];
		for (i=1; i<nx; i++)
		{
			f += Math.pow(10.0,6.0)*z[i]*z[i];
		}
		return f;
	}
	

	/**************** Discus *********************/
	
	double discus_func(double[] x,int s_flag,int r_flag) 
	{
		return discus_func(x, nx, OShift, M, s_flag,r_flag);
	}
	
	double discus_func (double[] x,  int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Discus */
	{
		int i;
		double[] z = new double[nx];
		sr_func(x,z,nx,Os,Mr,1.0,s_flag,r_flag);/*shift and rotate*/
		double f = Math.pow(10.0,6.0)*z[0]*z[0];
		for (i=1; i<nx; i++)
		{
			f += z[i]*z[i];
		}

		return f;
	}
	
	/**************** Rosenbrock's *********************/

	double rosenbrock_func (double[] x, int s_flag,int r_flag) 
	{
		return rosenbrock_func(x, nx, OShift, M, s_flag,r_flag);
	}
	
	double rosenbrock_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Rosenbrock's */
	{
		int i;
		double tmp1,tmp2;
		double f = 0.0;
		double[] z = new double[nx];
		sr_func(x,z,nx,Os,Mr,2.048/100.0,s_flag,r_flag);/*shift and rotate*/
		z[0] +=1.0; //shift to origin
		for (i=0; i<nx-1; i++)
		{
			z[i+1] += 1.0; //shift to orgin
			tmp1=z[i]*z[i]-z[i+1];
			tmp2=z[i]-1.0;
			f +=100.0*tmp1*tmp1 +tmp2*tmp2;
		}

		return f;
	}
	
	/**************** Ackley's *********************/

	double ackley_func(double[] x, int s_flag,int r_flag) 
	{
		return ackley_func(x, nx, OShift, M, s_flag,r_flag);
	}

	
	double ackley_func(double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Ackley's  */
	{
		int i;
		double sum1, sum2;
		double[] z = new double[nx];
		sum1 = 0.0;
		sum2 = 0.0;

		sr_func(x,z,nx,Os,Mr,1.0,s_flag,r_flag);/*shift and rotate*/ 		

		for (i=0; i<nx; i++)
		{
			sum1 += z[i]*z[i];
			sum2 += Math.cos(2.0*PI*z[i]);
		}
		sum1 = -0.2*Math.sqrt(sum1/nx);
		sum2 /= nx;	  

		return (E - 20.0*Math.exp(sum1) - Math.exp(sum2) +20.0);
	}

	/**************** Weierstrass's *********************/

	double weierstrass_func(double[] x, int s_flag,int r_flag) 
	{
		return weierstrass_func(x, nx, OShift, M, s_flag,r_flag);
	}
	
	double weierstrass_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Weierstrass's  */
	{
		int i,j,k_max;
		double sum,sum2=0, a, b;
		double f=0;
		double[] z = new double[nx];

		sr_func(x,z,nx,Os,Mr,0.5/100.0,s_flag,r_flag);/*shift and rotate*/ 

		a = 0.5;
		b = 3.0;
		k_max = 20;
		f = 0.0;
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
	
	/**************** Griewank's *********************/

	double griewank_func (double[] x, int s_flag,int r_flag) 
	{
		return griewank_func(x, nx, OShift, M, s_flag,r_flag);
	}

	double griewank_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Griewank's  */
	{
		int i;
		double f = 0;
		double[] z = new double[nx];
		double s, p;

		sr_func(x,z,nx,Os,Mr,600.0/100.0,s_flag,r_flag);/*shift and rotate*/ 

		s = 0.0;
		p = 1.0;
		for (i=0; i<nx; i++)
		{
			s += z[i]*z[i];
			p *= Math.cos(z[i]/Math.sqrt(1.0+i));
		}
		f = 1.0 + s/4000.0 - p;

		return f;
	}

	/**************** Rastrigin's *********************/

	double rastrigin_func (double[] x, int s_flag,int r_flag) /* Rastrigin's  */
	{
		return rastrigin_func(x, nx, OShift, M, s_flag,r_flag);
	}

	double rastrigin_func (double[] x,  int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Rastrigin's  */
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
	
	/**************** Schwefel's *********************/

	double schwefel_func (double[] x, int s_flag,int r_flag) /* Schwefel's  */
	{

		return schwefel_func(x, nx, OShift, M, s_flag,r_flag);
	}
	
	double schwefel_func(double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Schwefel's  */
	{
		int i;
		double[] z=new double[nx];
		double tmp;

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
	
	/**************** Katsuura *********************/
	
	double katsuura_func(double[] x, int s_flag,int r_flag) /* Katsuura  */
	{
		return katsuura_func(x, nx, OShift, M, s_flag,r_flag);
	}

	double katsuura_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Katsuura  */
	{
		int i,j;
		double[] z = new double[nx];
		double temp,tmp1,tmp2,tmp3;
		tmp3=Math.pow(1.0*nx,1.2);

		sr_func(x,z, nx, Os, Mr, 5/100.0,s_flag,r_flag);/*shift and rotate*/ 


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
	
	/**************** HappyCat *********************/
	/*HappyCat, provided by Hans-Georg Beyer (HGB)*/
	/*original global optimum: [-1,-1,...,-1]*/

	double happycat_func(double[] x, int s_flag,int r_flag) 
	{
		return happycat_func(x, nx, OShift, M, s_flag,r_flag);
	}
	//OVERLOAD
	double happycat_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) 
	{
		int i;
		double alpha,r2,sum_z;
		double[] z = new double[nx];
		alpha = 1.0/8.0;

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

	/**************** HGBat *********************/
	/*HGBat, provided by Hans-Georg Beyer (HGB)*/
	/*original global optimum: [-1,-1,...-1]*/
	
	double hgbat_func (double[] x, int s_flag,int r_flag) 
	{
		return hgbat_func(x, nx, OShift, M, s_flag,r_flag);

	}

	double hgbat_func(double[] x,  int nx, double[] Os,double[] Mr,int s_flag,int r_flag) 
	{
		int i;
		double f=0.0;
		double[] z = new double[nx];
		double alpha,r2,sum_z;
		alpha=1.0/4.0;

		sr_func (x, z, nx, Os, Mr, 5.0/100.0, s_flag, r_flag); /* shift and rotate */

		r2 = 0.0;
		sum_z=0.0;
		for (i=0; i<nx; i++)
		{
			z[i]=z[i]-1.0;//shift to orgin
			r2 += z[i]*z[i];
			sum_z += z[i];
		}
		f=Math.pow(Math.abs(Math.pow(r2,2.0)-Math.pow(sum_z,2.0)),2*alpha) + (0.5*r2 + sum_z)/nx + 0.5;
		return f;
	}

	/**************** Griewank-Rosenbrock  *********************/
	
	double grie_rosen_func(double[] x, int s_flag,int r_flag) /* Griewank-Rosenbrock  */
	{
		return grie_rosen_func(x, nx, OShift, M, s_flag,r_flag);
	}

	double grie_rosen_func (double[] x, int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Griewank-Rosenbrock  */
	{
		int i;
		double temp,tmp1,tmp2;
		double[] z = new double[nx];
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
	
	/**************** Expanded Scaffer's  *********************/

	double escaffer6_func(double[] x, int s_flag,int r_flag) /* Expanded Scaffer's F6  */
	{
		return escaffer6_func(x, nx, OShift, M, s_flag,r_flag);
	}

	double escaffer6_func(double[] x,  int nx, double[] Os,double[] Mr,int s_flag,int r_flag) /* Expanded Scaffer��s F6  */
	{
		int i;
		double temp1, temp2;
		double[] z = new double[nx];

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

	
	/**************** Hybrid Function 1  *********************/
	
	double hf01(double[] x, int s_flag,int r_flag) /* Hybrid Function 1 */
	{
		return hf01(x, nx, OShift, M, SS, s_flag,r_flag);
	}
	
	double hf01(double[] x, int nx,  double[] Os,double[] Mr,int[] S,int s_flag,int r_flag) /* Hybrid Function 1 */
	{
		int i,tmp,cf_num=3;
		double[] fit = new double[3];
		int[] G = new int[3];
		int[] G_nx = new int[3];
		double[] Gp = {0.3,0.3,0.4};
		double[] z = new double[nx];
		double[] y = new double[nx];
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
		fit[i]=schwefel_func(ty,G_nx[i],tO,tM,0,0);

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


	/**************** Hybrid Function 2  *********************/
	
	double hf02(double[] x, int s_flag,int r_flag) /* Hybrid Function 2 */
	{
		return hf02(x, nx, OShift, M, SS,s_flag,r_flag);
	}
	
	double hf02 (double[] x, int nx, double[] Os,double[] Mr,int[] S,int s_flag,int r_flag) /* Hybrid Function 2 */
	{
		int i,tmp,cf_num=3;
		double[] fit = new double[3];
		int[] G = new int[3];
		int[] G_nx = new int[3];
		double[] Gp={0.3,0.3,0.4};
		double[] z = new double[nx];
		double[] y = new double[nx];

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
		fit[i]=bent_cigar_func(ty,G_nx[i],tO,tM,0,0);

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
			ty[ii]=y[G_nx[i-2]+G_nx[i-1]+ii];
			tO[ii]=Os[G_nx[i-1]+G_nx[i-2]+ii];
			tM[ii]=Mr[i*nx+ii];
		}
		fit[i]=rastrigin_func(ty,G_nx[i],tO,tM,0,0);

		double f=0.0;
		for(i=0;i<cf_num;i++)
		{
			f += fit[i];
		}
		return f;
	}
	
	
	/**************** Hybrid Function 3  *********************/
	
	double hf03(double[] x,int s_flag,int r_flag) /* Hybrid Function 3 */
	{
		return hf03(x, nx, OShift, M, SS, s_flag,r_flag);
	}
	
	double hf03 (double[] x, int nx, double[] Os,double[] Mr,int[] S,int s_flag,int r_flag) /* Hybrid Function 3 */
	{
		int i,tmp,cf_num=4;
		double[] fit = new double[4];
		int[] G_nx = new int[4];
		int[] G = new int[4];
		double[] Gp={0.2,0.2,0.3,0.3};
		double[] z = new double[nx];
		double[] y = new double[nx];

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

	
	
	/**************** Hybrid Function 4  *********************/

	double hf04(double[] x,int s_flag,int r_flag) /* Hybrid Function 4 */
	{
		return hf04(x, nx, OShift, M, SS, s_flag,r_flag);
	}
	
	double hf04 (double[] x, int nx, double[] Os,double[] Mr,int[] S,int s_flag,int r_flag) /* Hybrid Function 4 */
	{
		int i,tmp,cf_num=4;
		double[] fit = new double[4];
		int[] G = new int[4];
		int[] G_nx = new int[4];
		double[] Gp={0.2,0.2,0.3,0.3};
		double[] z = new double[nx];
		double[] y = new double[nx];

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
		fit[i]=hgbat_func(ty,G_nx[i],tO,tM,0,0);

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
		fit[i]=discus_func(ty,G_nx[i],tO,tM,0,0);

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
		fit[i]=grie_rosen_func(ty,G_nx[i],tO,tM,0,0);
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
		fit[i]=rastrigin_func(ty,G_nx[i],tO,tM,0,0);

		double f=0.0;
		for(i=0;i<cf_num;i++)
		{
			f += fit[i];
		}
		return f;
	}

	
	/**************** Hybrid Function 5  *********************/

	double hf05(double[] x, int s_flag,int r_flag) /* Hybrid Function 5 */
	{
		return hf05(x, nx, OShift, M, SS, s_flag,r_flag);
	}
	
	double hf05 (double[] x,int nx, double[] Os,double[] Mr,int[] S,int s_flag,int r_flag) /* Hybrid Function 5 */
	{
		int i,tmp,cf_num=5;
		double[] fit = new double[5];
		int[] G = new int[5];
		int[] G_nx = new int[5];
		double[] Gp={0.1,0.2,0.2,0.2,0.3};
		double[] z = new double[nx];
		double[] y = new double[nx];

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
		fit[i]=schwefel_func(ty,G_nx[i],tO,tM,0,0);
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
		fit[i]=ellips_func(ty,G_nx[i],tO,tM,0,0);

		double f=0.0;
		for(i=0;i<cf_num;i++)
		{
			f += fit[i];
		}
		return f;
	}

	
	/**************** Hybrid Function 6  *********************/
	
	double hf06(double[] x,int s_flag,int r_flag) /* Hybrid Function 6 */
	{
		return hf06(x, nx, OShift, M, SS, s_flag,r_flag);
	}
	
	double hf06 (double[] x,  int nx, double[] Os,double[] Mr,int[] S,int s_flag,int r_flag) /* Hybrid Function 6 */
	{
		int i,tmp,cf_num=5;
		double[] fit = new double[5];
		int[] G = new int[5];
		int[] G_nx = new int[5];
		double[] Gp={0.1,0.2,0.2,0.2,0.3};
		double[] z = new double[nx];
		double[] y = new double[nx];

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
		fit[i]=katsuura_func(ty,G_nx[i],tO,tM,0,0);

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
		fit[i]=happycat_func(ty,G_nx[i],tO,tM,0,0);

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
		fit[i]=grie_rosen_func(ty,G_nx[i],tO,tM,0,0);
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
		fit[i]=schwefel_func(ty,G_nx[i],tO,tM,0,0);
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
		fit[i]=ackley_func(ty,G_nx[i],tO,tM,0,0);

		double f=0.0;
		for(i=0;i<cf_num;i++)
		{
			f += fit[i];
		}
		return f;
	}

	
	
	/**************** Composition Function 1  *********************/

	double cf01 (double[] x,int r_flag) /* Composition Function 1 */
	{
		int i,j,cf_num=5;
		double[] fit = new double[5];// fit[5];
		double[] delta = {10, 20, 30, 40, 50};
		double[] bias = {0, 100, 200, 300, 400};

		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];

		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=rosenbrock_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+4;

		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		//fit[i]=ellips_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=ellips_func(x,nx,tOs,tMr,1,0);
		fit[i]=10000*fit[i]/1e+10;

		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=bent_cigar_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+30;

		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=discus_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+10;

		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=ellips_func(x,nx,tOs,tMr,1,0);
//		fit[i]=10000*fit[i]/1e+5;
		fit[i]=10000*fit[i]/1e+10;//FABIO

		return cf_cal(x, nx, OShift, delta,bias,fit,cf_num);
	}

	double cf02 (double[] x,int r_flag) /* Composition Function 2 */
	{
		int i,j,cf_num=3;
		double[] fit = new double[3];
		double[] delta = {20,20,20};
		double[] bias = {0, 100, 200};

		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];

		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,1,0);

		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=rastrigin_func(x,nx,tOs,tMr,1,r_flag);

		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=hgbat_func(x,nx,tOs,tMr,1,r_flag);

		return cf_cal(x, nx, OShift, delta,bias,fit,cf_num);
	}

	double cf03 (double[] x, int r_flag) /* Composition Function 3 */
	{
		int i,j,cf_num=3;
		double[] fit=new double[3];
		double[] delta = {10,30,50};
		double[] bias = {0, 100, 200};

		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];

		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=1000*fit[i]/4e+3;

		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=rastrigin_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=1000*fit[i]/1e+3;

		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=ellips_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=1000*fit[i]/1e+10;

		return cf_cal(x, nx, OShift, delta,bias,fit,cf_num);
	}

	double cf04 (double[] x, int r_flag) /* Composition Function 4 */
	{
		int i,j,cf_num=5;
		double[] fit=new double[5];
		double[] delta = {10,10,10,10,10};
		double[] bias = {0, 100, 200, 300, 400};

		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];

		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];

		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=1000*fit[i]/(4e+3);

		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];

		}
		fit[i]=happycat_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=1000*fit[i]/(1e+3);

		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=ellips_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=1000*fit[i]/1e+10;

		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=weierstrass_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=1000*fit[i]/400;

		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=griewank_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=1000*fit[i]/100;

		return cf_cal(x, nx, OShift, delta,bias,fit,cf_num);
	}

	double cf05 (double[] x, int r_flag) /* Composition Function 5 */
	{
		int i,j,cf_num=5;
		double[] fit=new double[5];
		double[] delta = {10,10,10,20,20};
		double[] bias = {0, 100,200,300,400};

		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];

		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=hgbat_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1000;
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=rastrigin_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+3;
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=weierstrass_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/400;
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=ellips_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+10;

		return cf_cal(x, nx, OShift, delta,bias,fit,cf_num);
	}

	double cf06 (double[] x, int r_flag) /* Composition Function 6 */
	{
		int i,j,cf_num=5;
		double[] fit=new double[5];
		double[] delta = {10,20,30,40,50};
		double[] bias = {0, 100,200,300,400};

		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];

		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=grie_rosen_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=happycat_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+3;
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=escaffer6_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/2e+7;
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		fit[i]=ellips_func(x,nx,tOs,tMr,1,r_flag);
		fit[i]=10000*fit[i]/1e+10;

		return cf_cal(x, nx, OShift, delta,bias,fit,cf_num);
	}

	double cf07 (double[] x,int r_flag) /* Composition Function 7 */
	{
		int i,j,cf_num=3;
		double[] fit=new double[3];
		double[] delta = {10,30,50};
		double[] bias = {0, 100, 200};

		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		int[] tSS = new int[nx];

		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf01(x, nx,tOs,tMr,tSS,1,r_flag);

		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf02(x, nx,tOs,tMr,tSS,1,r_flag);

		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf03(x,nx,tOs,tMr,tSS,1,r_flag);

		return cf_cal(x, nx, OShift, delta,bias,fit,cf_num);
	}

	double cf08 (double[] x,int r_flag) /* Composition Function 8 */
	{
		int i,j,cf_num=3;
		double[] fit=new double[3];
		double[] delta = {10,30,50};
		double[] bias = {0, 100, 200};

		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		int[] tSS = new int[nx];

		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf04(x,nx,tOs,tMr,tSS,1,r_flag);


		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf05(x,nx,tOs,tMr,tSS,1,r_flag);

		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = OShift[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = M[i*nx*nx+j];
		}
		for(j=0;j<nx;j++){
			tSS[j] = SS[i*nx+j];
		}
		fit[i]=hf06(x,nx,tOs,tMr,tSS,1,r_flag);

		return cf_cal(x, nx, OShift, delta,bias,fit,cf_num);
	}


	
	private void warnings(int nx, int func_num)
	{
		if (!(nx==2||nx==10||nx==20||nx==30||nx==50||nx==100))
		{
			System.out.println("\nError: Test functions are only defined for D=2,10,20,30,50,100.");
		}

		if (nx==2&&((func_num>=17&&func_num<=22)||(func_num>=29&&func_num<=30)))
		{
			System.out.println("\nError: hf01,hf02,hf03,hf04,hf05,hf06,cf07&cf08 are NOT defined for D=2.\n");
		}
	}	
}
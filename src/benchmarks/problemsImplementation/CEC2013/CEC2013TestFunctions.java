package benchmarks.problemsImplementation.CEC2013;

import static utils.benchmarks.ProblemsTransformations.cf_cal;
import static utils.benchmarks.ProblemsTransformations.shiftfunc;
import static utils.benchmarks.ProblemsTransformations.rotatefunc;
import static utils.benchmarks.ProblemsTransformations.oszfunc;
import static utils.benchmarks.ProblemsTransformations.asyfunc;


public class CEC2013TestFunctions {
	final static double INF = 1.0e99;
	final static double EPS = 1.0e-14;
	final static double E  = 2.7182818284590452353602874713526625;
	final static  double PI = 3.1415926535897932384626433832795029;		
	
	public static double sphere_func (double[] x, int nx, double[] Os,double[] Mr,int r_flag) /* Sphere */
	{
		double[] y=new double[nx];
		double[] z =new double[nx];
		
		shiftfunc(x, y, nx, Os);
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (int i=0; i<nx; i++){
			z[i]=y[i];
		}
						
		double f = 0.0;
	    for (int i=0; i<nx; i++)
	    {
	    	f += z[i]*z[i];
	    }
	    
	    return f;
	}
	
	
	public static double ellips_func (double[] x, int nx, double[] Os,double[] Mr,int r_flag) /* Ellipsoidal */
	{
		double[] y=new double[nx];
		double[] z =new double[nx];
		
		shiftfunc(x, y, nx, Os);
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (int i=0; i<nx; i++)
			z[i]=y[i];
	    oszfunc (z, y, nx);
		double f = 0.0;
	    for (int i=0; i<nx; i++)
	    {
	        f += Math.pow(10.0,6.0*i/(nx-1))*y[i]*y[i];
	    }
	    return f;
	}
	
	
	public static double bent_cigar_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Bent_Cigar */
	{
		double[] y=new double[nx];
		double[] z =new double[nx];
		
		double beta=0.5;
		shiftfunc(x, y, nx, Os);
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (int i=0; i<nx; i++)
			z[i]=y[i];
	    asyfunc (z, y, nx,beta);
		if (r_flag==1){
			double[] t = new double[nx*nx];
			for(int i=0;i<nx*nx;i++){
				t[i] = Mr[nx*nx+i];
			}
			rotatefunc(y,z,nx,t);
		}
		else
	    for (int i=0; i<nx; i++)
			z[i]=y[i];

		double f = z[0]*z[0];
	    for (int i=1; i<nx; i++)
	    {
	        f += Math.pow(10.0,6.0)*z[i]*z[i];
	    }
	    
	    return f;
	}
	
	
	public static  double discus_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Discus */
	{
		double[] y=new double[nx];
		double[] z =new double[nx];
		
		shiftfunc(x, y, nx, Os);
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (int i=0; i<nx; i++)
			z[i]=y[i];
	    oszfunc (z, y, nx);

		double f = Math.pow(10.0,6.0)*y[0]*y[0];
	    for (int i=1; i<nx; i++)
	    {
	        f += y[i]*y[i];
	    }
	    
	    return f;
	}
	
	
	public static  double dif_powers_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Different Powers */
	{
		int i;
		double[] y=new double[nx];
		double[] z =new double[nx];
		
		shiftfunc(x, y, nx, Os);
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];
		double f = 0.0;
	    for (i=0; i<nx; i++)
	    {
	        f += Math.pow(Math.abs(z[i]),2+4*i/(nx-1));
	    }
		f=Math.pow(f,0.5);
		
		return f;
	}
	
	
	public static  double rosenbrock_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Rosenbrock's */
	{
	    int i;
		double[] y=new double[nx];
		double[] z =new double[nx];
	    
		double tmp1,tmp2;
		shiftfunc(x, y, nx, Os);//shift
		for (i=0; i<nx; i++)//shrink to the orginal search range
	    {
	        y[i]=y[i]*2.048/100;
	    }
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);//rotate
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];
		for (i=0; i<nx; i++)//shift to orgin
	    {
	        z[i]=z[i]+1;
	    }

	    double f = 0.0;
	    for (i=0; i<nx-1; i++)
	    {
			tmp1=z[i]*z[i]-z[i+1];
			tmp2=z[i]-1.0;
	        f += 100.0*tmp1*tmp1 +tmp2*tmp2;
	    }
	    
	    return f;
	}
	
	
	public static  double schaffer_F7_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Schwefel's 1.2  */
	{
	    int i;
		double[] y=new double[nx];
		double[] z =new double[nx];
		double tmp;
		shiftfunc(x, y, nx, Os);
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];
		asyfunc (z, y, nx, 0.5);
		for (i=0; i<nx; i++)
			z[i] = y[i]*Math.pow(10.0,1.0*i/(nx-1)/2.0);
		if (r_flag==1){
			double[] t = new double[nx*nx];
			for(i=0;i<nx*nx;i++){
				t[i] = Mr[nx*nx+i];
			}
						rotatefunc(z, y, nx, t);
		}
		else
	    for (i=0; i<nx; i++)
			y[i]=z[i];

		for (i=0; i<nx-1; i++)
			z[i]=Math.pow(y[i]*y[i]+y[i+1]*y[i+1],0.5);
	    double f = 0.0;
	    for (i=0; i<nx-1; i++)
	    {
		  tmp=Math.sin(50.0*Math.pow(z[i],0.2));
	      f += Math.pow(z[i],0.5)+Math.pow(z[i],0.5)*tmp*tmp ;
	    }
		f = f*f/(nx-1)/(nx-1);
		
		return f;
	}
	
	
	public static  double ackley_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Ackley's  */
	{
	    int i;
		double[] y=new double[nx];
		double[] z =new double[nx];
	    double sum1, sum2;

		shiftfunc(x, y, nx, Os);
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

		asyfunc (z, y, nx, 0.5);
		for (i=0; i<nx; i++)
			z[i] = y[i]*Math.pow(10.0,1.0*i/(nx-1)/2.0);
		if (r_flag==1){
			double[] t = new double[nx*nx];
			for(i=0;i<nx*nx;i++){
				t[i] = Mr[nx*nx+i];
			}
						rotatefunc(z, y, nx, t);
		}
		else
	    for (i=0; i<nx; i++)
			y[i]=z[i];

	    sum1 = 0.0;
	    sum2 = 0.0;
	    for (i=0; i<nx; i++)
	    {
	        sum1 += y[i]*y[i];
	        sum2 += Math.cos(2.0*PI*y[i]);
	    }
	    sum1 = -0.2*Math.sqrt(sum1/nx);
	    sum2 /= nx;
	    double f =  E - 20.0*Math.exp(sum1) - Math.exp(sum2) +20.0;
	    
	    return f;
	}
	
	
	public static  double weierstrass_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Weierstrass's  */
	{
	    int i,j,k_max;
	    double sum,sum2=0, a, b;
		double[] y=new double[nx];
		double[] z =new double[nx];

		shiftfunc(x, y, nx, Os);
		for (i=0; i<nx; i++)//shrink to the orginal search range
	    {
	        y[i]=y[i]*0.5/100;
	    }
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

		asyfunc (z, y, nx, 0.5);
		for (i=0; i<nx; i++)
			z[i] = y[i]*Math.pow(10.0,1.0*i/(nx-1)/2.0);
		if (r_flag==1){
			double[] t = new double[nx*nx];
			for(i=0;i<nx*nx;i++){
				t[i] = Mr[nx*nx+i];
			}
						rotatefunc(z, y, nx, t);
		}
		else
	    for (i=0; i<nx; i++)
			y[i]=z[i];

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
	            sum += Math.pow(a,j)*Math.cos(2.0*PI*Math.pow(b,j)*(y[i]+0.5));
				sum2 += Math.pow(a,j)*Math.cos(2.0*PI*Math.pow(b,j)*0.5);
	        }
	        f += sum;
	    }
		f -= nx*sum2;
		
		return f;
	}
	
	
	public static  double griewank_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Griewank's  */
	{
	    int i;
	    double s, p;

	    double[] y=new double[nx];
		double[] z =new double[nx];
		
		shiftfunc(x, y, nx, Os);
		for (i=0; i<nx; i++)//shrink to the orginal search range
	    {
	        y[i]=y[i]*600.0/100.0;
	    }
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

		for (i=0; i<nx; i++)
			z[i] = z[i]*Math.pow(100.0,1.0*i/(nx-1)/2.0);


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
	
	
	public static  double rastrigin_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Rastrigin's  */
	{
	    int i;
	    double[] y=new double[nx];
		double[] z =new double[nx];
	    
		double alpha=10.0,beta=0.2;
		shiftfunc(x, y, nx, Os);
		for (i=0; i<nx; i++)//shrink to the orginal search range
	    {
	        y[i]=y[i]*5.12/100;
	    }

		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

	    oszfunc (z, y, nx);
	    asyfunc (y, z, nx, beta);

	    if (r_flag==1){
			double[] t = new double[nx*nx];
			for(i=0;i<nx*nx;i++){
				t[i] = Mr[nx*nx+i];
			}
						rotatefunc(z, y, nx, t);
		}
		else
	    for (i=0; i<nx; i++)
			y[i]=z[i];

		for (i=0; i<nx; i++)
		{
			y[i]*=Math.pow(alpha,1.0*i/(nx-1)/2);
		}

		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

	    double f = 0.0;
	    for (i=0; i<nx; i++)
	    {
	        f += (z[i]*z[i] - 10.0*Math.cos(2.0*PI*z[i]) + 10.0);
	    }
	    
	    return f;
	}
	
	
	public static double step_rastrigin_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Noncontinuous Rastrigin's  */
	{
	    int i;
	    double[] y=new double[nx];
		double[] z =new double[nx];
		
		double alpha=10.0,beta=0.2;
		shiftfunc(x, y, nx, Os);
		for (i=0; i<nx; i++)//shrink to the orginal search range
	    {
	        y[i]=y[i]*5.12/100;
	    }

		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

	    for (i=0; i<nx; i++)
		{
			if (Math.abs(z[i])>0.5)
			z[i]=Math.floor(2*z[i]+0.5)/2;
		}

	    oszfunc (z, y, nx);
	    asyfunc (y, z, nx, beta);

	    if (r_flag==1){
			double[] t = new double[nx*nx];
			for(i=0;i<nx*nx;i++){
				t[i] = Mr[nx*nx+i];
			}
						rotatefunc(z, y, nx, t);
		}
		else
	    for (i=0; i<nx; i++)
			y[i]=z[i];

		for (i=0; i<nx; i++)
		{
			y[i]*=Math.pow(alpha,1.0*i/(nx-1)/2);
		}

		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

	    double f = 0.0;
	    for (i=0; i<nx; i++)
	    {
	        f += (z[i]*z[i] - 10.0*Math.cos(2.0*PI*z[i]) + 10.0);
	    }
	    
	    return f;
	}
	
	
	public static double schwefel_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Schwefel's  */
	{
	    int i;
	    double[] y=new double[nx];
		double[] z =new double[nx];
		
		double tmp;
		shiftfunc(x, y, nx, Os);
		for (i=0; i<nx; i++)//shrink to the orginal search range
	    {
	        y[i]*=1000/100;
	    }
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

		for (i=0; i<nx; i++)
			y[i] = z[i]*Math.pow(10.0,1.0*i/(nx-1)/2.0);

		for (i=0; i<nx; i++)
			z[i] = y[i]+4.209687462275036e+002;
		
	    double f=0;
	    for (i=0; i<nx; i++)
		{
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
	
	
	public static double katsuura_func (double[] x,  int nx, double []Os,double []Mr,int r_flag) /* Katsuura  */
	{
	    int i,j;
	    double[] y=new double[nx];
		double[] z =new double[nx];
		
		double temp,tmp1,tmp2,tmp3;
		tmp3=Math.pow(1.0*nx,1.2);
		shiftfunc(x, y, nx, Os);
		for (i=0; i<nx; i++)//shrink to the orginal search range
	    {
	        y[i]*=5.0/100.0;
	    }
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

		for (i=0; i<nx; i++)
			z[i] *=Math.pow(100.0,1.0*i/(nx-1)/2.0);

		if (r_flag==1){
			double[] t = new double[nx*nx];
			for(i=0;i<nx*nx;i++){
				t[i] = Mr[nx*nx+i];
			}
						rotatefunc(z, y, nx, t);
		}
		else
	    for (i=0; i<nx; i++)
			y[i]=z[i];

	    double f=1.0;
	    for (i=0; i<nx; i++)
		{
			temp=0.0;
			for (j=1; j<=32; j++)
			{
				tmp1=Math.pow(2.0,j);
				tmp2=tmp1*y[i];
				temp += Math.abs(tmp2-Math.floor(tmp2+0.5))/tmp1;
			}
			f *= Math.pow(1.0+(i+1)*temp,10.0/tmp3);
	    }
		tmp1=10.0/nx/nx;
	    f=f*tmp1-tmp1;
	    
	    return f;

	}
	
	
	public static double bi_rastrigin_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Lunacek Bi_rastrigin Function */
	{
	    int i;
	    double f;
	    double[] y=new double[nx];
		double[] z =new double[nx];
		double mu0=2.5,d=1.0,s,mu1,tmp,tmp1,tmp2;
		double[] tmpx;
		tmpx=new double[nx];
		s=1.0-1.0/(2.0*Math.pow(nx+20.0,0.5)-8.2);
		mu1=-Math.pow((mu0*mu0-d)/s,0.5);

		shiftfunc(x, y, nx, Os);
		for (i=0; i<nx; i++)//shrink to the orginal search range
	    {
	        y[i]*=10.0/100.0;
	    }

		for (i = 0; i < nx; i++)
	    {
			tmpx[i]=2*y[i];
	        if (Os[i] < 0.)
	            tmpx[i] *= -1.;
	    }

		for (i=0; i<nx; i++)
		{
			z[i]=tmpx[i];
			tmpx[i] += mu0;
		}
		if (r_flag==1)
		rotatefunc(z, y, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			y[i]=z[i];

		for (i=0; i<nx; i++)
			y[i] *=Math.pow(100.0,1.0*i/(nx-1)/2.0);
		if (r_flag==1){
			double[] t = new double[nx*nx];
			for(i=0;i<nx*nx;i++){
				t[i] = Mr[nx*nx+i];
			}
						rotatefunc(z, y, nx, t);
		}
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

	    tmp1=0.0;tmp2=0.0;
	    for (i=0; i<nx; i++)
		{
			tmp = tmpx[i]-mu0;
			tmp1 += tmp*tmp;
			tmp = tmpx[i]-mu1;
			tmp2 += tmp*tmp;
	    }
		tmp2 *= s;
		tmp2 += d*nx;
		tmp=0;
		for (i=0; i<nx; i++)
		{
			tmp+=Math.cos(2.0*PI*z[i]);
	    }
		
		if(tmp1<tmp2)
			f = tmp1;
		else
			f = tmp2;
		f += 10.0*(nx-tmp);
		
		return f;
	}
	
	
	public static double grie_rosen_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Griewank-Rosenbrock  */
	{
	    int i;
	    double[] y=new double[nx];
		double[] z =new double[nx];
	    double temp,tmp1,tmp2;

		shiftfunc(x, y, nx, Os);
		for (i=0; i<nx; i++)//shrink to the orginal search range
	    {
	        y[i]=y[i]*5/100;
	    }
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

		for (i=0; i<nx; i++)//shift to orgin
	    {
	        z[i]=y[i]+1;
	    }

	    double f=0.0;
	    for (i=0; i<nx-1; i++)
	    {
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
	
	
	public static double escaffer6_func (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Expanded Scaffer��s F6  */
	{
	    int i;
	    double[] y=new double[nx];
		double[] z =new double[nx];
	    double temp1, temp2;
		shiftfunc(x, y, nx, Os);
		if (r_flag==1)
		rotatefunc(y, z, nx, Mr);
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

		asyfunc (z, y, nx, 0.5);
		if (r_flag==1){
			double[] t = new double[nx*nx];
			for(i=0;i<nx*nx;i++){
				t[i] = Mr[nx*nx+i];
			}
						rotatefunc(z, y, nx, t);
		}
		else
	    for (i=0; i<nx; i++)
			z[i]=y[i];

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
	
	
	public static double cf01 (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Composition Function 1 */
	{
		int i,j,cf_num=5;
		double[] fit = new double[5];// fit[5];
		double[] delta = {10, 20, 30, 40, 50};
		double[] bias = {0, 100, 200, 300, 400};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=rosenbrock_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/1e+4;
		
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=dif_powers_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/1e+10;
		
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=bent_cigar_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/1e+30;
		
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=discus_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/1e+10;
		
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=sphere_func(x,nx,tOs,tMr,0);
		fit[i]=10000*fit[i]/1e+5;
	
	
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
		
	}
	
	
	public static double cf02 (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Composition Function 2 */
	{
		int i,j,cf_num=3;
		

		double[] fit = new double[3];
		double[] delta = {20,20,20};
		double[] bias = {0, 100, 200};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		for(i=0;i<cf_num;i++)
		{
			for(j=0;j<nx;j++){
				tOs[j] = Os[i*nx+j];
			}
			for(j=0;j<nx*nx;j++){
				tMr[j] = Mr[i*nx*nx+j];
			}
			fit[i]=schwefel_func(x,nx,tOs,tMr,r_flag);
		}
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	
	public static double cf03 (double[] x,  int nx, double[] Os,double[]Mr,int r_flag) /* Composition Function 3 */
	{
		int i,j,cf_num=3;

		double[] fit=new double[3];
		double[] delta = {20,20,20};
		double[] bias = {0, 100, 200};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		for(i=0;i<cf_num;i++)
		{
			
			for(j=0;j<nx;j++){
				tOs[j] = Os[i*nx+j];
			}
			for(j=0;j<nx*nx;j++){
				tMr[j] = Mr[i*nx*nx+j];
			}
			fit[i]=schwefel_func(x,nx,tOs,tMr,r_flag);
		}
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	
	public static double cf04 (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Composition Function 4 */
	{
		int i,j,cf_num=3;
		double[] fit=new double[3];
		double[] delta = {20,20,20};
		double[] bias = {0, 100, 200};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
			
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
			
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/(4e+3);
		
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		
		}
		fit[i]=rastrigin_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/(1e+3);
		
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=weierstrass_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/400;
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	
	public static double cf05 (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Composition Function 4 */
	{
		int i,j,cf_num=3;
		double[] fit=new double[3];
		double[] delta = {10,30,50};
		double[] bias = {0, 100, 200};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/4e+3;
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=rastrigin_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/1e+3;
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=weierstrass_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/400;
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}

		
	public static double cf06 (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Composition Function 6 */
	{
		int i,j,cf_num=5;
		double[] fit=new double[5];
		double[] delta = {10,10,10,10,10};
		double[] bias = {0, 100, 200, 300, 400};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/4e+3;
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=rastrigin_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/1e+3;
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=ellips_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/1e+10;
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=weierstrass_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/400;
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=griewank_func(x,nx,tOs,tMr,r_flag);
		fit[i]=1000*fit[i]/100;
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);

	}

		
	public static double cf07 (double[] x,  int nx, double[] Os,double[] Mr,int r_flag) /* Composition Function 7 */
	{
		int i,j,cf_num=5;
		double[] fit=new double[5];
		double[] delta = {10,10,10,20,20};
		double[] bias = {0, 100, 200, 300, 400};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=griewank_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/100;
		
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=rastrigin_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/1e+3;
		
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/4e+3;
	
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=weierstrass_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/400;
		
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=sphere_func(x,nx,tOs,tMr,0);
		fit[i]=10000*fit[i]/1e+5;
	
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}

		
	public static double cf08 (double[] x, int nx, double[] Os,double[] Mr,int r_flag) /* Composition Function 8 */
	{
		int i,j,cf_num=5;
		double[] fit=new double[5];
		double[] delta = {10,20,30,40,50};
		double[] bias = {0, 100, 200, 300, 400};
		
		double[] tOs = new double[nx];
		double[] tMr = new double[cf_num*nx*nx];
		
		i=0;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=grie_rosen_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		
		i=1;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=schaffer_F7_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/4e+6;
		
		i=2;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=schwefel_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/4e+3;
		
		i=3;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=escaffer6_func(x,nx,tOs,tMr,r_flag);
		fit[i]=10000*fit[i]/2e+7;
		
		i=4;
		for(j=0;j<nx;j++){
			tOs[j] = Os[i*nx+j];
		}
		for(j=0;j<cf_num*nx*nx;j++){
			tMr[j] = Mr[i*nx*nx+j];
		}
		fit[i]=sphere_func(x,nx,tOs,tMr,0);
		fit[i]=10000*fit[i]/1e+5;
		
		
		return cf_cal(x, nx, Os, delta,bias,fit,cf_num);
	}
	
	public static AbstractCEC2013 initFunction(int funcNum, int dim)
	{
		
		AbstractCEC2013 f = null;
			switch(funcNum)
			{
			case 1:	
				f = new  F1(dim); break;
			case 2:	
				f = new F2(dim); break;
			case 3:	
				f = new F3(dim); break;
			case 4:	
				f = new F4(dim); break;
			case 5:
				f = new F5(dim); break;
			case 6:
				f = new F6(dim); break;
			case 7:	
				f = new F7(dim); break;
			case 8:	
				f = new F8(dim); break;
			case 9:	
				f = new F9(dim); break;
			case 10:	
				f = new F10(dim); break;
			case 11:	
				f = new F11(dim); break;
			case 12:	
				f = new F12(dim); break;
			case 13:	
				f = new F13(dim); break;
			case 14:	
				f = new F14(dim); break;
			case 15:	
				f = new F15(dim); break;
			case 16:	
				f = new F16(dim); break;
			case 17:	
				f = new F17(dim); break;
			case 18:	
				f = new F18(dim); break;
			case 19:	
				f = new F19(dim); break;		
			case 20:	
				f = new F20(dim); break;	 
			case 21:	
				f = new F21(dim); break;
			case 22:	
				f = new F22(dim); break;
			case 23:	
				f = new F23(dim); break;
			case 24:	
				f = new F24(dim); break;
			case 25:	
				f = new F25(dim); break;		
			case 26:
				f = new F26(dim); break;	 		
			case 27:
				f = new F27(dim); break;	
			case 28:
				f = new F28(dim); break;			
			default:
				System.out.println("\nError: There are only 28 test functions in this test suite!");
				f = null;
				break;
			}
				
			return f;
		}
	
}

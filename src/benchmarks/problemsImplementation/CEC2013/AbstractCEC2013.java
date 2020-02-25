package benchmarks.problemsImplementation.CEC2013;

import java.util.Scanner;
import static utils.RunAndStore.slash;


abstract public class AbstractCEC2013 {

	
	double[] OShift,M,y,z;
	
	int ini_flag=0,n_flag,func_flag, nx,func_num;

	
	
	
//	public AbstractCEC2013( int nx,int func_num) 
	public AbstractCEC2013(int nx) 
	{
		this.nx = nx;
		//this.func_num=func_num;
		this.y=new double[nx];
		this.z=new double[nx];
		int cf_num=10;	
		
		if (!(nx==2||nx==5||nx==10||nx==20||nx==30||nx==40||nx==50||nx==60||nx==70||nx==80||nx==90||nx==100))
		{
			System.out.println("\nError: Test functions are only defined for D=2,5,10,20,30,40,50,60,70,80,90,100.");
		}
		
		Scanner input = new Scanner(this.getClass().getResourceAsStream("input_data"+slash()+"M_D"+nx+".txt"));
//		File fpt = new File("input_data/M_D"+nx+".txt");//* Load M data *
		
		M=new double[cf_num*nx*nx]; 
		
		for (int i=0; i<cf_num*nx*nx; i++)
		{
			String next = input.next();
			M[i]=Double.parseDouble(next);
		}
		input.close();
		

		
		input =  new Scanner(this.getClass().getResourceAsStream("input_data"+slash()+"shift_data.txt"));
				//new File("input_data/shift_data.txt");
		OShift=new double[nx*cf_num];
		for(int i=0;i<cf_num*nx;i++)
		{
			String next = input.next();
			OShift[i]=Double.parseDouble(next);
		}
		input.close();
		
	
	}
	
	 abstract public double f(double[] x);
	

}

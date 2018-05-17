package benchmarks.problemsImplementation.CEC2005;

import static utils.RunAndStore.slash;

import java.util.Scanner;

public abstract class CEC2005TestFunction {

	protected int		dimension = -1;
	protected double	bias = Double.NaN;
	protected double[] bounds = null;
	protected String	func_name = null;

	static final public int NUM_TEST_FUNC = 25;
	static final public String DEFAULT_FILE_BIAS = "supportData"+slash()+"fbias_data.txt";

	
	static final public int MAX_SUPPORT_DIM = 100;
//	static final public double PIx2 = Math.PI * 2.0;
//
//	// Formatter for the number representation
//	static final public DecimalFormat scientificFormatter = new DecimalFormat("0.0000000000000000E00");
//	static final public DecimalFormat numberFormatter = scientificFormatter;
//	static final public DecimalFormat percentageFormatter = new DecimalFormat("0.0000000000");
//	
	
	
//	static final public ClassLoader loader = Benchmark.class.getClassLoader(); //ClassLoader.getSystemClassLoader();	
//	
	
	// Constructors
	public CEC2005TestFunction (int dimension) {
		this(dimension, "undefined");
		
	}
	public CEC2005TestFunction (int dimension, String func_name) {
		this.dimension = dimension;
		this.func_name = func_name;
		if(dimension>MAX_SUPPORT_DIM)System.out.println("Maximums dimensionality of the proboem is"+MAX_SUPPORT_DIM);
	} 

	// Function body to be defined in the child classes
	public abstract double f(double[] x);

	// Property functions common for all child classes
	public int getDimension() 
	{
		if(dimension<0)System.out.println("Dimension not yet initialised!");
		return (dimension);
	}

	public double getBias() 
	{
		if(bias==Double.NaN)System.out.println("Bias not yet initialised!");
		return (bias);
	}

	public String getName() 
	{
		if(func_name==null)System.out.println("Name not yet initialised!");
		return (func_name);
	}
	
	
	
	
	protected double setBias(int fNum) {return setBias(fNum,DEFAULT_FILE_BIAS);}
	
	protected double setBias(int fNum, String file_bias)
	{
		double bias = Double.NaN;
		if(!((fNum>NUM_TEST_FUNC)&&(fNum<1)))System.out.println("This bias does not exists, there are 25 test be problems from index 1 to 25!");
		double[] biases = new double[NUM_TEST_FUNC];
		loadFromFile(file_bias, NUM_TEST_FUNC, biases);
		bias = biases[NUM_TEST_FUNC-1];
		return bias;
	}
	
	
	
	public CEC2005TestFunction initiliaseFunction(int fNum, int dim)
	{
		CEC2005TestFunction F = null;

		switch (fNum) {
		case 1:
			F = new F01(dim);
		case 2:
			F = new F02(dim);
		case 3:
			F = new F03(dim);
		case 4:
			F = new F04(dim);
		case 5:
			F = new F05(dim);
		case 6:
			F = new F06(dim);
		case 7:
			F = new F07(dim);
		case 8:
			F = new F08(dim);
		case 9:
			F = new F09(dim);
		case 10:
			F = new F10(dim);
		case 11:
			F = new F11(dim);
		case 12:
			F = new F12(dim);
		case 13:
			F = new F13(dim);
		case 14:
			F = new F14(dim);
		case 15:
			F = new F15(dim);
		case 16:
			F = new F16(dim);
		case 17:
			F = new F17(dim);
		case 18:
			F = new F18(dim);
		case 19:
			F = new F19(dim);
		case 20:
			F = new F20(dim);
		case 21:
			F = new F21(dim);
		case 22:
			F = new F22(dim);
		case 23:
			F = new F23(dim);
		case 24:
			F = new F24(dim);
		case 25:
			F = new F25(dim);
		default:
			if(!((fNum>NUM_TEST_FUNC)&&(fNum<1)))System.out.println("This proboem does not exists, there are 25 test be problems from index 1 to 25!");
		}
			
		return F;
	}
	
	
	
	
	//Fabio's reimplementations of utilities for loading data from txt files (also once archived!)
	
	protected void loadFromFile(String file, int columns, double[] row) {
		try {
//			file = "cec2005"+slash()+file; 
			//System.out.println(this.getClass());
			System.out.println(file);
			System.out.println(this.getClass().getResourceAsStream(file));
			System.out.println(this.getClass());
			Scanner input = new Scanner(this.getClass().getResourceAsStream(file));
			
			for (int i=0;i<columns; i++)
			{
				String next = input.next(); //System.out.println(next+"\t");
				row[i]=Double.parseDouble(next);
			}
			
			input.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	protected void loadFromFile(String file, int rows, int columns, double[][] matrix) 
	{
		try 
		{
//			file = "cec2005"+slash()+file; 
			//System.out.println(this.getClass());
			System.out.println(file);
			System.out.println(this.getClass().getResourceAsStream(file));
			System.out.println(this.getClass());
			Scanner input = new Scanner(this.getClass().getResourceAsStream(file));
			
			for (int r=0; r<rows; r++)
			{				
				//loadFromFile(file, columns, matrix[r]);
				
				for(int c=0; c<columns; r++)
				{
					String next = input.next(); //System.out.println(next+"\t");
					matrix[r][c]=Double.parseDouble(next);
				}
			}
			
			input.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	protected void loadFromFile(String file, int N, int rows, int columns, double[][][] matrix)
	{
		try 
		{
//			file = "cec2005"+slash()+file; 
			//System.out.println(this.getClass());
			System.out.println(file);
			System.out.println(this.getClass().getResourceAsStream(file));
			System.out.println(this.getClass());
			Scanner input = new Scanner(this.getClass().getResourceAsStream(file));
			
			for (int i = 0 ; i < N ; i ++) {
				for (int r=0; r<rows; r++)
				{				
					//loadFromFile(file, columns, matrix[r]);
				
					for(int c=0; c<columns; r++)
					{
						String next = input.next(); //System.out.println(next+"\t");
						matrix[i][r][c]=Double.parseDouble(next);
					}
				}
			}
			
			input.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	

	
}

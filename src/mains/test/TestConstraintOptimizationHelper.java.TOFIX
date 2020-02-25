package test;

import utils.MathUtils;
import algorithms.interfaces.Problem;

public class TestConstraintOptimizationHelper
{
	static boolean showViolation = false;
	static double PENALTY = 1e6;
		
	// f1 (ok)
	Problem g06 = new Problem(2, new double[][] {{13,100},{0,100}}) {
		@Override
		public double f(double[] x) throws Exception {
			double f = Math.pow(x[0]-10,3) + Math.pow(x[1]-20,3);

			double violation = 0;
			for (double c : checkConstraint(x))
			{
				if (c > 0)
					violation += c;
			}

			if (violation > 0)
				return PENALTY;
			else
				return f;
		}

		private double[] checkConstraint(double[] x)
		{
			double[] c = new double[2];
			c[0] = -Math.pow(x[0]-5,2) - Math.pow(x[1]-5,2) + 100;
			c[1] = Math.pow(x[0]-6,2) + Math.pow(x[1]-5,2) - 82.81;
			if (showViolation)
				System.out.println(MathUtils.toString(c));
			return c;
		}
	}; 

	// f2 (ok)
	Problem g07 = new Problem(10, new double[] {-10,10}) {
		@Override
		public double f(double[] x) throws Exception {
			double f = Math.pow(x[0],2) + 
					Math.pow(x[1],2) + x[0]*x[1] - 14*x[0] -
					16*x[1] + Math.pow(x[2]-10,2) + 
					4*Math.pow(x[3]-5,2) + Math.pow(x[4]-3,2) + 
					2*Math.pow(x[5]-1,2) + 5*Math.pow(x[6],2) + 
					7*Math.pow(x[7]-11,2) + 2*Math.pow(x[8]-10,2) + 
					Math.pow(x[9]-7,2) + 45;

			double violation = 0;
			for (double c : checkConstraint(x))
			{
				if (c > 0)
					violation += c;
			}

			if (violation > 0)
				return PENALTY;
			else
				return f;
		}

		private double[] checkConstraint(double[] x)
		{
			double[] c = new double[8];
			c[0] = 4*x[0] + 5*x[1] - 3*x[6] + 9*x[7] - 105;
			c[1] = 10*x[0] - 8*x[1] - 17*x[6] + 2*x[7];
			c[2] = -8*x[0] + 2*x[1] + 5*x[8] - 2*x[9] - 12;
			c[3] = -3*x[0] + 6*x[1] + 12*Math.pow(x[8]-8,2) - 7*x[9];
			c[4] = 3*Math.pow(x[0]-2,2) + 4*Math.pow(x[1]-3,2) + 2*Math.pow(x[2],2) - 7*x[3] - 120;
			c[5] = Math.pow(x[0],2) + 2*Math.pow(x[1]-2,2) - 2*x[0]*x[1] + 14*x[4] - 6*x[5];
			c[6] = 5*Math.pow(x[0],2) + 8*x[1] + Math.pow(x[2]-6,2) - 2*x[3] - 40;
			c[7] = Math.pow(x[0]-8,2) + 4*Math.pow(x[1]-4,2) + 6*Math.pow(x[4],2) - 2*x[5] - 60;
			if (showViolation)
				System.out.println(MathUtils.toString(c));
			return c;
		}
	}; 

	// f3 (ok)
	Problem g09 = new Problem(7, new double[] {-10,10}) {
		@Override
		public double f(double[] x) throws Exception {
			double f = Math.pow(x[0]-10,2) + 5*Math.pow(x[1]-12,2) +
					Math.pow(x[2],4) + 3*Math.pow(x[3]-11,2) + 10*Math.pow(x[4],6) + 
					7*Math.pow(x[5],2) + Math.pow(x[6],4)- 4*x[5]*x[6] - 10*x[5] - 8*x[6];

			double violation = 0;
			for (double c : checkConstraint(x))
			{
				if (c > 0)
					violation += c;
			}

			if (violation > 0)
				return PENALTY;
			else
				return f;
		}

		private double[] checkConstraint(double[] x)
		{
			double[] c = new double[4];
			c[0] = -127 + 2*Math.pow(x[0],2) + 3*Math.pow(x[1],4) + x[2] + 4*Math.pow(x[3],2) + 5*x[4];
			c[1] = -196 + 23*x[0] + Math.pow(x[1],2) + 6*Math.pow(x[5],2) - 8*x[6];
			c[2] = -282 + 7*x[0] + 3*x[1] + 10*Math.pow(x[2],2) + x[3] - x[4];
			c[3] = 4*Math.pow(x[0],2) + Math.pow(x[1],2) - 3*x[0]*x[1] + 2*Math.pow(x[2],2) + 5*x[5] - 11*x[6];
			if (showViolation)
				System.out.println(MathUtils.toString(c));
			return c;
		}
	}; 

	// f4 (ok)
	Problem g10 = new Problem(8, 
			new double[][] {{100,10000},
			{1000,10000},
			{1000,10000},
			{10,1000},
			{10,1000},
			{10,1000},
			{10,1000},
			{10,1000}}) {
		@Override
		public double f(double[] x) throws Exception {
			double f = x[0] + x[1] + x[2];

			double violation = 0;
			for (double c : checkConstraint(x))
			{
				if (c > 0)
					violation += c;
			}

			if (violation > 0)
				return PENALTY;
			else
				return f;
		}

		private double[] checkConstraint(double[] x)
		{
			double[] c = new double[6];
			c[0] = 0.0025*(x[3]+x[5]) - 1;
			c[1] = 0.0025*(x[4]+x[6]-x[3]) - 1;
			c[2] = 0.01*(x[7]-x[4]) - 1;
			c[3] = -x[0]*x[5] + 833.33252*x[3] + 100*x[0] - 83333.333;
			c[4] = -x[1]*x[6] + 1250*x[4] + x[1]*x[3] - 1250*x[3];
			c[5] = -x[2]*x[7] + 1250000 + x[2]*x[4] - 2500*x[4];
			if (showViolation)
				System.out.println(MathUtils.toString(c));
			return c;
		}
	};

	// f5 (ok)
	Problem weldedBeam = new Problem(4, 
			new double[][] {{0.1,2.0},
			{0.1,10.0},
			{0.1,10.0},
			{0.1,2.0}}) {

		// material properties
		double P = 6000;
		double L = 14;
		double rho_max = 0.25;
		double t_max = 13600;
		double sigma_max = 30000;
		double E = 30*1e6;
		double G = 12*1e6;

		private double t(double[] x)
		{
			double M = P*(L + x[1]/2);
			double R = Math.sqrt((Math.pow(x[1],2)/4) + Math.pow((x[0]+x[2])/2,2));
			double J = 2*(Math.sqrt(2)*x[0]*x[1]*(Math.pow(x[1],2)/12 + Math.pow((x[0]+x[2])/2,2)));
			double t1 = P/(Math.sqrt(2)*x[0]*x[1]);
			double t2 = (M*R)/J;

			return Math.sqrt(Math.pow(t1,2) + (2*t1*t2*x[1])/(2*R) + Math.pow(t2,2));
		}

		private double sigma(double[] x)
		{
			return (6*P*L)/(x[3]*Math.pow(x[2],2));
		}

		private double rho(double[] x)
		{	
			return (4*P*Math.pow(L,3))/(E*x[3]*Math.pow(x[2],3));
		}

		private double Pc(double[] x)
		{
			return 4.013*E*Math.sqrt(Math.pow(x[2],2)*Math.pow(x[3],6)/36)/Math.pow(L,2) * (1-(x[2]/(2*L)) * Math.sqrt(E/(4*G)));
		}

		@Override
		public double f(double[] x) throws Exception {
			double f = 1.10471*Math.pow(x[0],2)*x[1] + 0.04811*x[2]*x[3]*(14.0 + x[1]);

			double violation = 0;
			for (double c : checkConstraint(x))
			{
				if (c > 0)
					violation += c;
			}

			if (violation > 0)
				return PENALTY;
			else
				return f;
		}

		private double[] checkConstraint(double[] x)
		{
			double[] c = new double[7];
			c[0] = t(x)-t_max;
			c[1] = sigma(x)-sigma_max;
			c[2] = x[0]-x[3];
			c[3] = 0.10471*Math.pow(x[0],2)+0.04811*x[2]*x[3]*(14+x[1])-5;
			c[4] = 0.125-x[0]; 
			c[5] = rho(x)-rho_max;
			c[6] = P-Pc(x);
			if (showViolation)
				System.out.println(MathUtils.toString(c));
			return c;
		}
	};

	// f6 (ok)
	Problem speedReducer = new Problem(7, 
			new double[][] {{2.6, 3.6},
			{0.7, 0.8},
			{17.0, 28.0},
			{7.3, 8.3},
			{7.3, 8.3},
			{2.9, 3.9},
			{5.0, 5.5}}) {
		@Override
		public double f(double[] x) throws Exception {
			x[2] = (int)x[2]; // XXX (gio) x[2] should be integer

			double f = 0.7854*x[0]*Math.pow(x[1],2)*(3.3333*Math.pow(x[2],2) + 	14.9334*x[2] - 43.0934) - 
					1.508*x[0]*(Math.pow(x[5],2) + Math.pow(x[6],2)) + 
					7.4777*(Math.pow(x[5],3) + Math.pow(x[6],3)) + 
					0.7854*(x[3]*Math.pow(x[5],2) + x[4]*Math.pow(x[6],2));

			double violation = 0;
			for (double c : checkConstraint(x))
			{
				if (c > 0)
					violation += c;
			}

			if (violation > 0)
				return PENALTY;
			else
				return f;
		}

		private double[] checkConstraint(double[] x)
		{
			double[] c = new double[11];
			c[0] = 27.0/(x[0]*Math.pow(x[1],2)*x[2]) - 1;
			c[1] = 397.5/(x[0]*Math.pow(x[1],2)*Math.pow(x[2],2)) - 1;
			c[2] = 1.93*Math.pow(x[3],3)/(x[1]*Math.pow(x[2],2)*Math.pow(x[5],4)) - 1;
			c[3] = 1.93*Math.pow(x[4],3)/(x[1]*x[2]*Math.pow(x[6],4)) - 1;
			c[4] = Math.sqrt(Math.pow((745*x[3])/(x[1]*x[2]),2)+16.9*1e6)/(110*Math.pow(x[5],3)) - 1;
			c[5] = Math.sqrt(Math.pow((745*x[4])/(x[1]*x[2]),2)+157.5*1e6)/(85*Math.pow(x[6],3)) - 1;
			c[6] = x[1]*x[2]/40.0 - 1;
			c[7] = 5*x[1]/x[0] - 1;
			c[8] = x[0]/(12*x[1]) - 1;
			c[9] = (1.5*x[5]+1.9)/x[3] - 1;
			c[10] = (1.1*x[6]+1.9)/x[4] - 1;
			if (showViolation)
				System.out.println(MathUtils.toString(c));
			return c;
		}
	};

	// f7 (ok)
	Problem treeBarTruss = new Problem(2, new double[] {0.0,1.0}) {
		double l = 100;
		double 	P = 2;
		double sigma = 2;

		@Override
		public double f(double[] x) throws Exception {
			double f = (2*Math.sqrt(2)*x[0] + x[1])*l;	// weight

			double violation = 0;
			for (double c : checkConstraint(x))
			{
				if (c > 0)
					violation += c;
			}

			if (violation > 0)
				return PENALTY;
			else
				return f;
		}

		private double[] checkConstraint(double[] x)
		{
			double[] c = new double[3];
			c[0] = (Math.sqrt(2)*x[0] + x[1])/(Math.sqrt(2)*Math.pow(x[0],2)+2*x[0]*x[1])*P - sigma;
			c[1] = (x[1]/(Math.sqrt(2)*Math.pow(x[0],2)+2*x[0]*x[1]))*P - sigma;
			c[2] = (1/(Math.sqrt(2)*x[1]+x[0]))*P - sigma;
			if (showViolation)
				System.out.println(MathUtils.toString(c));
			return c;
		}
	};

	// f8 (ok)
	Problem pressureVessel = new Problem(4, 
			new double[][] {{1,99},
			{1,99},
			{10,200},
			{10,200}}) {

		double[] tmpX = new double[4];

		@Override
		public double f(double[] x) throws Exception {
			// x[0], x[1] are integer multiples of 0.0625 inch
			x[0] = Math.round(x[0]);
			x[1] = Math.round(x[1]);

			tmpX[0] = x[0]*0.0625;
			tmpX[1] = x[1]*0.0625;
			tmpX[2] = x[2];
			tmpX[3] = x[3];

			double f = 0.6224*tmpX[0]*tmpX[2]*tmpX[3] + 1.7781*tmpX[1]*Math.pow(tmpX[2],2) + 
					3.1661*Math.pow(tmpX[0],2)*tmpX[3] + 19.84*Math.pow(tmpX[0],2)*tmpX[2];

			double violation = 0;
			for (double c : checkConstraint(tmpX))
			{
				if (c > 0)
					violation += c;
			}

			if (violation > 0)
				return PENALTY;
			else
				return f;
		}

		private double[] checkConstraint(double[] x)
		{
			double[] c = new double[4];
			c[0] = -x[0] + 0.0193*x[2];
			c[1] = -x[1] + 0.00954*x[2];
			c[2] = -Math.PI*Math.pow(x[2],2)*x[3] - 4.0/3*Math.PI*Math.pow(x[2],3) + 1296000;
			c[3] = x[3]-240;
			if (showViolation)
				System.out.println(MathUtils.toString(c));
			return c;
		}
	};

	// f9 (ok)
	Problem spring = new Problem(3, 
			new double[][] {{0.25, 1.3},
			{0.05, 2.0},
			{2.0, 15.0}}) {
		@Override
		public double f(double[] x) throws Exception {
			//D, d, N
			double f = (x[2]+2)*x[0]*Math.pow(x[1],2);

			double violation = 0;
			for (double c : checkConstraint(x))
			{
				if (c > 0)
					violation += c;
			}

			if (violation > 0)
				return PENALTY;
			else
				return f;
		}

		private double[] checkConstraint(double[] x)
		{
			double[] c = new double[4];
			c[0] = 1 - (Math.pow(x[0],3)*x[2])/(71785*Math.pow(x[1],4));
			c[1] = (4*Math.pow(x[0],2)-x[0]*x[1])/(12566*(x[0]*Math.pow(x[1],3)-Math.pow(x[1],4))) + 1/(5108*Math.pow(x[1],2)) - 1;
			c[2] = 1 - 140.45*x[1]/(Math.pow(x[0],2)*x[2]);
			c[3] = (x[0]+x[1])/1.5 - 1;
			if (showViolation)
				System.out.println(MathUtils.toString(c));
			return c;
		}
	};
	
	public void test() throws Exception
	{
		System.out.println("*****************************");
		
		showViolation = false;
	
		double[] knownBest;
		double knownFBest;
		double myFBest;
		
		// f1
		knownFBest = -6961.813875580166495638;
		knownBest = new double[] {14.0949999999999882049906,  0.8429607892154535875306};
		myFBest = g06.f(knownBest);
		System.out.println(knownFBest + "\t" + myFBest + "\t" + Math.abs(knownFBest-myFBest));

		// f2
		knownFBest = 24.30620928918499146221;
		knownBest = new double[] {2.1720135078740585576895, 2.3636390198188546030167, 8.7739148674734313004819, 5.0959646365485244956517, 0.9907434539608839241254, 1.4308009929055143238941, 1.3216782864133398422979, 9.8287539694810650559020, 8.2801135410166537553778, 8.3758688567496104582233};
		myFBest = g07.f(knownBest);
		System.out.println(knownFBest + "\t" + myFBest + "\t" + Math.abs(knownFBest-myFBest));
		
		// f3
		knownFBest = 680.6300585382734880113;
		knownBest = new double[] {2.3303140672961184520773,  1.9514072380327540656708, -0.4774908147616070408503,  4.3656791874457141489074, -0.6244447822378322854320,  1.0381873668363716944896,  1.5940183149730771106078};
		myFBest = g09.f(knownBest);
		System.out.println(knownFBest + "\t" + myFBest + "\t" + Math.abs(knownFBest-myFBest));
		
		// f4
		knownFBest = 7049.262792797353540664;
		knownBest = new double[] {574.8616002951911241325, 1360.3134194545548325550, 5114.0877730476076976629,  181.6452235231378722347,  295.4364906162094825959,  218.3546629180166291917,  286.2087119713898459850,  395.4364899222125018241};
		myFBest = g10.f(knownBest);
		System.out.println(knownFBest + "\t" + myFBest + "\t" + Math.abs(knownFBest-myFBest));
		
		// f5
		knownFBest = 1.724852308597364825005;
		knownBest = new double[] {0.2057296397860794157086, 3.4704886656280016232756, 9.0366239103576333491219, 0.2057296397860794989754};
		myFBest = weldedBeam.f(knownBest);
		System.out.println(knownFBest + "\t" + myFBest + "\t" + Math.abs(knownFBest-myFBest));

		// f6
		knownFBest = 2994.471067799157481204;
		knownBest = new double[] {3.5000000006288836118529,  0.6999999999999999555911, 17.5345071107680432476172,  7.2999999999999998223643,  7.7153199189313363959286,  3.3502146705483593080999,  5.2866544651489828510194};
		myFBest = speedReducer.f(knownBest);
		System.out.println(knownFBest + "\t" + myFBest + "\t" + Math.abs(knownFBest-myFBest));
		
		// f7
		knownFBest = 263.8958433771500722287;
		knownBest = new double[] {0.7886754602195364949324, 0.4082473694648784401373};
		myFBest = treeBarTruss.f(knownBest);
		System.out.println(knownFBest + "\t" + myFBest + "\t" + Math.abs(knownFBest-myFBest));
		
		// f8
		knownFBest = 6059.71433554777649988;
		knownBest = new double[] {13.033204584928379077269, 6.733384085007243946563, 42.098445593669666209280, 176.636595879493228267165};
		myFBest = pressureVessel.f(knownBest);
		System.out.println(knownFBest + "\t" + myFBest + "\t" + Math.abs(knownFBest-myFBest));
		
		// f9
		knownFBest = 0.01266547178463529356451;
		knownBest = new double[] {0.35403596762210054471609,  0.05157734351144603601735, 11.44794877119210418925377};
		myFBest = spring.f(knownBest);
		System.out.println(knownFBest + "\t" + myFBest + "\t" + Math.abs(knownFBest-myFBest));
		
		showViolation = false;
		
		System.out.println("*****************************");
	}
	
	public static void main(String[] args)
	{
		try {
			new TestConstraintOptimizationHelper().test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
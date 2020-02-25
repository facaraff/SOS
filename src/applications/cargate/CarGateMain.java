package applications.cargate;

//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.StringTokenizer;


import utils.random.RandUtils;

import interfaces.Problem;

public class CarGateMain
{
	// penalty factor
	private static int penaltyFactor = 0; // 500 ok, 1000 not ok

	// car data
	private static double[] tMeas, xMeas, yMeas, zMeas;
	
	// parameter bounds
	private static double[][] bounds = {{1,15}, 
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6},
		{-100,100},{1e-3,2},{0,2},{0,6}};
	
	// magnetic axis' identifiers
	private static enum Axis {x, y, z};
	
//	private static void prepareFile(String file)
//	{
//		readData(file);
//
//		int n = tMeas.length;
//
//		double xAverage = 0;
//		double yAverage = 0;
//		double zAverage = 0;
//
//		for (int i = (n-20); i < n; i++)
//		{
//			xAverage += xMeas[i];
//			yAverage += yMeas[i];
//			zAverage += zMeas[i];
//		}
//		xAverage /= 20.0;
//		yAverage /= 20.0;
//		zAverage /= 20.0;
//
//		for (int i = 0; i < n; i++)
//		{
//			tMeas[i] /= 10000.0;
//			xMeas[i] -= xAverage;
//			yMeas[i] -= yAverage;
//			zMeas[i] -= zAverage;
//		}
//
//		xMeas = CarGateUtils.bwFilter(xMeas, tMeas, 1.2);
//		yMeas = CarGateUtils.bwFilter(yMeas, tMeas, 1.2);
//		zMeas = CarGateUtils.bwFilter(zMeas, tMeas, 1.2);
//	}

//	private static void readData(String file)
//	{
//		try
//		{
//			BufferedReader br;
//			String strLine;
//			int i;
//
//			br= new BufferedReader(new FileReader(file));
//			i = 0;
//			while ((strLine = br.readLine()) != null)
//				i++;
//			br.close();
//
//			tMeas = new double[i];
//			xMeas = new double[i];
//			yMeas = new double[i];
//			zMeas = new double[i];
//
//			br= new BufferedReader(new FileReader(file));
//			i = 0;
//			while ((strLine = br.readLine()) != null)
//			{
//				StringTokenizer tokenizer = new StringTokenizer(strLine, ",");
//				double[] tokens = new double[tokenizer.countTokens()];
//				int j = 0;
//				while (tokenizer.hasMoreTokens())
//					tokens[j++] = Double.parseDouble(tokenizer.nextToken());
//				tMeas[i] = tokens[2];
//				xMeas[i] = tokens[4];
//				yMeas[i] = tokens[5];
//				zMeas[i] = tokens[6];
//
//				i++;
//			}
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//	}

	/**
	 * Magnetic dipole along x axis
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static double[] dipoleX(double a, double b, double c, double d)
	{
		int n = tMeas.length;
		double x[] = new double[n];
		for (int i = 0; i < n; i++)
			x[i] = 3*1e-1 * a * b * (tMeas[i]-d)/Math.pow((Math.pow(tMeas[i]-d,2) + Math.pow(b,2) + Math.pow(c,2)),2.5); 
		return x;
	}

	/**
	 * Magnetic dipole along y axis
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static double[] dipoleY(double a, double b, double c, double d)
	{
		int n = tMeas.length;
		double y[] = new double[n];
		for (int i = 0; i < n; i++)
			y[i] =  3e-1 * a * b * c /Math.pow((Math.pow((tMeas[i]-d),2) + Math.pow(b,2) + Math.pow(c,2)),2.5);
		return y;
	}

	/**
	 * Magnetic dipole along z axis
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static double[] dipoleZ(double a, double b, double c, double d)
	{
		int n = tMeas.length;
		double z[] = new double[n];
		for (int i = 0; i < n; i++)
			z[i] = 1e-1 * (a/Math.pow((Math.pow(tMeas[i]-d,2) + Math.pow(b,2) + Math.pow(c,2)), 1.5) + a*Math.pow(b,2)/Math.pow((Math.pow(tMeas[i]-d,2) + Math.pow(b,2) + Math.pow(c,2)),2.5));
		return z;
	}

	/**
	 * Sum of dipoles along the selected axis
	 * @param p
	 * @return
	 */
	private static double[] sumOfDipoles(double[] p, Axis axis)
	{
		p[0] = (int)Math.floor(p[0]);
		int n = (int)p[0];
		double[][] tmp = new double[n][tMeas.length];
		for (int i = 0; i < n; i++)
		{
			if (p[1+4*i] == 0)
				p[1+4*i] = bounds[1+4*i][0] + RandUtils.random()*(bounds[1+4*i][1]-bounds[1+4*i][0]);
			if (p[1+4*i+1] == 0)
				p[1+4*i+1] = bounds[1+4*i+1][0] + RandUtils.random()*(bounds[1+4*i+1][1] - bounds[1+4*i+1][0]);
			
			if (axis == Axis.x)
				tmp[i] = dipoleX(p[1+4*i], p[1+4*i+1], p[1+4*i+2], p[1+4*i+3]);
			else if (axis == Axis.y)
				tmp[i] = dipoleY(p[1+4*i], p[1+4*i+1], p[1+4*i+2], p[1+4*i+3]);
			else if (axis == Axis.z)
				tmp[i] = dipoleZ(p[1+4*i], p[1+4*i+1], p[1+4*i+2], p[1+4*i+3]);
		}

		int l = tMeas.length;
		double[] y = new double[l];
		for (int i = 0; i < l; i++)
		{
			double tmpsum = 0;
			for (int j = 0; j < n; j++)
				tmpsum += tmp[j][i];
			y[i] = tmpsum;
		}
		return y;
	}

	/**
	 * Residual function
	 * @param p
	 * @return
	 */
	private static double[] residuals(double[] p)
	{
		int n = tMeas.length;

		int numSignals = 3;
		double[][] err = new double[numSignals][n];
		double[] fitX = sumOfDipoles(p, Axis.x);
		double[] fitY = sumOfDipoles(p, Axis.y);
		double[] fitZ = sumOfDipoles(p, Axis.z);

		for (int i = 0; i < n; i++)
		{
			err[0][i] = xMeas[i] - fitX[i];
			err[1][i] = yMeas[i] - fitY[i];
			err[2][i] = zMeas[i] - fitZ[i];
		}

		double[] res = new double[n];
		for (int i = 0; i < n; i++)
		{
			double tmp = 0;
			for (int j = 0; j < numSignals; j++)
				tmp += Math.pow(err[j][i],2);
			res[i] = Math.sqrt(tmp);
		}

		return res;
	}

	/**
	 * Error function (sum of squares of residuals)
	 * @param p
	 * @return
	 */
	private static double errorFunction(double[] p)
	{
		double[] res = residuals(p);
		double err = 0;
		int n = res.length;
		for (int i = 0; i < n; i++)
			err += Math.pow(res[i],2);
		return err + penaltyFactor * p[0];
	}
	
	/**
	 * The car model optimization problem
	 *
	 */
	public static class CarModelFit extends Problem 
	{
		public CarModelFit(int dimension, double[][] bounds) { super(dimension, bounds); }

		public double f(double[] x)
		{ 
			return errorFunction(x);		
		}
	}
}
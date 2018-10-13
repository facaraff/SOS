//package mains.applications;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.StringTokenizer;
//import java.util.Vector;
//import java.util.concurrent.Callable;
//import java.util.concurrent.CompletionService;
//import java.util.concurrent.ExecutorCompletionService;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//import utils.random.RandUtils;
//import algorithms.CLPSO;
//import algorithms.CMAES;
//
//
//public class CarGateMaintoFinish
//{
//	// penalty factor
//	private static int penaltyFactor = 0; // 500 ok, 1000 not ok
//
//	// number of repetitions per algorithm
//	private static int repetitionNumber = 24;
//	
//	// enable multithreading
//	private static boolean multiThread = true;
//
//	// enable debug
//	private static boolean debug = false;
//
//	// car data
//	private static double[] tMeas, xMeas, yMeas, zMeas;
//	
//	// parameter bounds
//	private static double[][] bounds = {{1,15}, 
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6},
//		{-100,100},{1e-3,2},{0,2},{0,6}};
//	
//	// magnetic axis' identifiers
//	private static enum Axis {x, y, z};
//	
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
//
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
//
//	/**
//	 * Magnetic dipole along x axis
//	 * @param a
//	 * @param b
//	 * @param c
//	 * @param d
//	 * @return
//	 */
//	private static double[] dipoleX(double a, double b, double c, double d)
//	{
//		int n = tMeas.length;
//		double x[] = new double[n];
//		for (int i = 0; i < n; i++)
//			x[i] = 3*1e-1 * a * b * (tMeas[i]-d)/Math.pow((Math.pow(tMeas[i]-d,2) + Math.pow(b,2) + Math.pow(c,2)),2.5); 
//		return x;
//	}
//
//	/**
//	 * Magnetic dipole along y axis
//	 * 
//	 * @param a
//	 * @param b
//	 * @param c
//	 * @param d
//	 * @return
//	 */
//	private static double[] dipoleY(double a, double b, double c, double d)
//	{
//		int n = tMeas.length;
//		double y[] = new double[n];
//		for (int i = 0; i < n; i++)
//			y[i] =  3e-1 * a * b * c /Math.pow((Math.pow((tMeas[i]-d),2) + Math.pow(b,2) + Math.pow(c,2)),2.5);
//		return y;
//	}
//
//	/**
//	 * Magnetic dipole along z axis
//	 * @param a
//	 * @param b
//	 * @param c
//	 * @param d
//	 * @return
//	 */
//	private static double[] dipoleZ(double a, double b, double c, double d)
//	{
//		int n = tMeas.length;
//		double z[] = new double[n];
//		for (int i = 0; i < n; i++)
//			z[i] = 1e-1 * (a/Math.pow((Math.pow(tMeas[i]-d,2) + Math.pow(b,2) + Math.pow(c,2)), 1.5) + a*Math.pow(b,2)/Math.pow((Math.pow(tMeas[i]-d,2) + Math.pow(b,2) + Math.pow(c,2)),2.5));
//		return z;
//	}
//
//	/**
//	 * Sum of dipoles along the selected axis
//	 * @param p
//	 * @return
//	 */
//	private static double[] sumOfDipoles(double[] p, Axis axis)
//	{
//		p[0] = (int)Math.floor(p[0]);
//		int n = (int)p[0];
//		double[][] tmp = new double[n][tMeas.length];
//		for (int i = 0; i < n; i++)
//		{
//			if (p[1+4*i] == 0)
//				p[1+4*i] = bounds[1+4*i][0] + RandUtils.random()*(bounds[1+4*i][1]-bounds[1+4*i][0]);
//			if (p[1+4*i+1] == 0)
//				p[1+4*i+1] = bounds[1+4*i+1][0] + RandUtils.random()*(bounds[1+4*i+1][1] - bounds[1+4*i+1][0]);
//			
//			if (axis == Axis.x)
//				tmp[i] = dipoleX(p[1+4*i], p[1+4*i+1], p[1+4*i+2], p[1+4*i+3]);
//			else if (axis == Axis.y)
//				tmp[i] = dipoleY(p[1+4*i], p[1+4*i+1], p[1+4*i+2], p[1+4*i+3]);
//			else if (axis == Axis.z)
//				tmp[i] = dipoleZ(p[1+4*i], p[1+4*i+1], p[1+4*i+2], p[1+4*i+3]);
//		}
//
//		int l = tMeas.length;
//		double[] y = new double[l];
//		for (int i = 0; i < l; i++)
//		{
//			double tmpsum = 0;
//			for (int j = 0; j < n; j++)
//				tmpsum += tmp[j][i];
//			y[i] = tmpsum;
//		}
//		return y;
//	}
//
//	/**
//	 * Residual function
//	 * @param p
//	 * @return
//	 */
//	private static double[] residuals(double[] p)
//	{
//		int n = tMeas.length;
//
//		int numSignals = 3;
//		double[][] err = new double[numSignals][n];
//		double[] fitX = sumOfDipoles(p, Axis.x);
//		double[] fitY = sumOfDipoles(p, Axis.y);
//		double[] fitZ = sumOfDipoles(p, Axis.z);
//
//		for (int i = 0; i < n; i++)
//		{
//			err[0][i] = xMeas[i] - fitX[i];
//			err[1][i] = yMeas[i] - fitY[i];
//			err[2][i] = zMeas[i] - fitZ[i];
//		}
//
//		double[] res = new double[n];
//		for (int i = 0; i < n; i++)
//		{
//			double tmp = 0;
//			for (int j = 0; j < numSignals; j++)
//				tmp += Math.pow(err[j][i],2);
//			res[i] = Math.sqrt(tmp);
//		}
//
//		return res;
//	}
//
//	/**
//	 * Error function (sum of squares of residuals)
//	 * @param p
//	 * @return
//	 */
//	private static double errorFunction(double[] p)
//	{
//		double[] res = residuals(p);
//		double err = 0;
//		int n = res.length;
//		for (int i = 0; i < n; i++)
//			err += Math.pow(res[i],2);
//		return err + penaltyFactor * p[0];
//	}
//
//	private static void runAlgorithmRepetition(Algorithm a, int repNr) throws Exception
//	{	
//		// optimization problem
//		Problem problem = new CarModelFit(61, bounds);
//
//		// run the optimization algorithm
//		long t0 = System.currentTimeMillis();
//
//		if (a.getClass().equals(CLPSO.class))
//		{
//			a = new CLPSO();
//			a.pushParameter("p0", 60.0);
//		}
//		else if (a.getClass().equals(JADE.class))
//		{
//			a = new JADE();
//			a.pushParameter("p0",60.0);
//			a.pushParameter("p1",0.05);
//			a.pushParameter("p2",0.1);
//		}
//		else if (a.getClass().equals(MALSChSSW.class)){
//			a = new MALSChSSW();
//			a.pushParameter("p0",60.0);
//			a.pushParameter("p1",3.0);
//			a.pushParameter("p2",0.5);
//			a.pushParameter("p3",0.125);
//			a.pushParameter("p4",0.5);
//			a.pushParameter("p5",100.0);
//			a.pushParameter("p6",1E-8);
//		}
//		else if (a.getClass().equals(MDE_pBX.class)){
//			a = new MDE_pBX();
//			a.pushParameter("p0",100.0);
//			a.pushParameter("p1",0.15);
//		}
//		else if (a.getClass().equals(PMS.class)){
//			a = new PMS();
//			a.pushParameter("p0", 0.95);
//			a.pushParameter("p1", 150.0);
//			a.pushParameter("p2", 0.4);
//			a.pushParameter("p3", 10e-5);
//			a.pushParameter("p4", 2.0);
//			a.pushParameter("p5", 0.5);
//		}
//		else if (a.getClass().equals(ThreeSome.class)){	
//			a = new ThreeSome();
//			a.pushParameter("p0", 0.95);
//			a.pushParameter("p1", 4.0);
//			a.pushParameter("p2", 0.1);
//			a.pushParameter("p3", 150.0);
//			a.pushParameter("p4", 0.4);
//		}
//		
//		// budget
//		Vector<Best> bests = a.execute(problem, 5000*problem.getDimension());
//
//		if (debug)
//		{
//			for (Best best : bests)
//				System.out.println(best.getI() + "\t" + best.getfBest());
//
//			System.out.println("***************************");
//		}
//
//		int problemDimension = problem.getDimension();
//		double[] best = new double[problemDimension];
//		for (int k = 0; k < problemDimension; k++)
//			best[k] = bests.get(bests.size()-1).getExtra(k);
//
//		double f = problem.f(best);
//		if (debug)
//			System.out.println(CarGateUtils.toStringSolution(best, f));
//
//		System.out.println("Elapsed time algorithm " + a.getClass().getSimpleName() + " repetition " + repNr + ": " +(long)(System.currentTimeMillis()-t0) + " ms.");
//
//		// save results
//		String dir = "./src/applications/incas3/cargate/data/";
//		String fileName = dir + "results_" + a.getClass().getSimpleName() + "_" + repNr;
//		ResultsUtils.saveBests(bests, fileName , true);
//
//		FileWriter fileWriter = new FileWriter(fileName + ".best");
//		fileWriter.write(CarGateUtils.toStringSolution(best, f));
//		fileWriter.close();
//		
//		System.gc();
//	}
//	
//	private static class AlgorithmRepetitionThread implements Callable<Boolean>
//	{
//		Algorithm algorithm;
//		int repNr;
//		
//		public AlgorithmRepetitionThread(Algorithm algorithm, int repNr)
//		{
//			this.algorithm = algorithm;
//			this.repNr = repNr;
//		}
//		
//		@Override
//		public Boolean call() throws Exception {
//			runAlgorithmRepetition(algorithm, repNr);
//			return true;
//		}		
//	};
//	
//	/**
//	 * The car model optimization problem
//	 *
//	 */
//	public static class CarModelFit extends Problem 
//	{
//		public CarModelFit(int dimension, double[][] bounds) { super(dimension, bounds); }
//
//		public double f(double[] x)
//		{ 
//			return errorFunction(x);		
//		}
//	}
//
//	public static void main(String[] args) throws Exception
//	{	
//		// XXX (gio) put here the car magnetic file
//		String file = "./src/applications/incas3/cargate/data/event001.csv";
//		prepareFile(file);
//
//		int n = tMeas.length;
//
//		// time-base rescaling
//		double baseTime = tMeas[0];
//		for (int i = 0; i < n; i++)
//			tMeas[i] -= baseTime;
//
//		// remove left and right tails of the signals based on threshold
//		double mag;
//		// set this threshold based on noise standard deviation
//		double threshold = 3.0;
//		int startIndex = -1;
//		int endIndex = -1;
//		for (int i = 0; i < n;  i++)
//		{
//			mag = Math.sqrt(Math.pow(xMeas[i],2)+Math.pow(yMeas[i],2)+Math.pow(zMeas[i],2));
//			if (mag > threshold)
//			{
//				if (startIndex < 0)
//					startIndex = i;
//				if (i > endIndex)
//					endIndex = i;
//			}
//		}
//
//		double[] tmpArray;
//
//		tmpArray = new double[endIndex-startIndex];
//		System.arraycopy(tMeas, startIndex, tmpArray, 0, endIndex-startIndex);
//		tMeas = tmpArray;
//
//		tmpArray = new double[endIndex-startIndex];
//		System.arraycopy(xMeas, startIndex, tmpArray, 0, endIndex-startIndex);
//		xMeas = tmpArray;
//
//		tmpArray = new double[endIndex-startIndex];
//		System.arraycopy(yMeas, startIndex, tmpArray, 0, endIndex-startIndex);
//		yMeas = tmpArray;
//
//		tmpArray = new double[endIndex-startIndex];
//		System.arraycopy(zMeas, startIndex, tmpArray, 0, endIndex-startIndex);
//		zMeas = tmpArray;
//		
//		FileWriter fileWriter = new FileWriter("./src/applications/incas3/cargate/data/data.csv");
//		n = xMeas.length;
//		for (int i = 0; i < n; i++)
//		{
//			fileWriter.write(Double.toString(tMeas[i]) + "," +
//							Double.toString(xMeas[i]) + "," +
//							Double.toString(yMeas[i]) + "," +
//							Double.toString(zMeas[i]) + "\n");
//		}
//		fileWriter.close();
//		
//		// initial solution (currently NOT used)
//		/*
//		double[] p0 = new double[61];
//		for (int i = 0; i < 61; i++)
//			p0[i] = 0.0;
//		double[] tmp =  {5, 58.,0.3,0,1.2, 71.,0.3,0,1.6, 50.,0.3,0,2., 38.,0.3,0,2.3, -27.,0.8,0,2.8};
//		System.arraycopy(tmp, 0, p0, 0, tmp.length);
//		printSolution(p0, problem.f(p0));
//		*/
//
//		// optimization algorithms
//		Vector<Algorithm> algorithms = new Vector<Algorithm>();
//
//		Algorithm a;
//
//		a = new CLPSO();
//		a.pushParameter("p0", 60.0);
//		algorithms.add(a);
//
//		a = new JADE();
//		a.pushParameter("p0",60.0);
//		a.pushParameter("p1",0.05);
//		a.pushParameter("p2",0.1);
//		algorithms.add(a);
//		
//		a = new MALSChSSW();
//		a.pushParameter("p0",60.0);
//		a.pushParameter("p1",3.0);
//		a.pushParameter("p2",0.5);
//		a.pushParameter("p3",0.125);
//		a.pushParameter("p4",0.5);
//		a.pushParameter("p5",100.0);
//		a.pushParameter("p6",1E-8);
//		algorithms.add(a);
//
//		a = new MDE_pBX();
//		a.pushParameter("p0",100.0);
//		a.pushParameter("p1",0.15);
//		algorithms.add(a);
//
//		a = new PMS();
//		a.pushParameter("p0", 0.95);
//		a.pushParameter("p1", 150.0);
//		a.pushParameter("p2", 0.4);
//		a.pushParameter("p3", 10e-5);
//		a.pushParameter("p4", 2.0);
//		a.pushParameter("p5", 0.5);
//		algorithms.add(a);
//		
//		a = new ThreeSome();
//		a.pushParameter("p0", 0.95);
//		a.pushParameter("p1", 4.0);
//		a.pushParameter("p2", 0.1);
//		a.pushParameter("p3", 150.0);
//		a.pushParameter("p4", 0.4);
//		algorithms.add(a);
//		
//		a = new CMAES();
//		algorithms.add(a);
//
//		a = new ISPO();
//		a.pushParameter("p0",1.0);
//		a.pushParameter("p1",10.0);
//		a.pushParameter("p2",2.0);
//		a.pushParameter("p3",4.0);
//		a.pushParameter("p4",1e-5);	
//		a.pushParameter("p5",30.0);
//		algorithms.add(a);
//
//		a = new MALSChCMA();
//		a.pushParameter("p0",60.0);
//		a.pushParameter("p1",3.0);
//		a.pushParameter("p2",0.5);
//		a.pushParameter("p3",0.125);
//		a.pushParameter("p4",0.5);
//		a.pushParameter("p5",100.0);
//		a.pushParameter("p6",1E-8);
//		algorithms.add(a);
//
//		a = new SADE();
//		a.pushParameter("p0", 50.0);
//		a.pushParameter("p1", 4.0);
//		a.pushParameter("p2", 20.0);
//		algorithms.add(a);		
//
//		int nrProc = Runtime.getRuntime().availableProcessors();
//		ExecutorService threadPool = Executors.newFixedThreadPool(nrProc);
//		CompletionService<Boolean> pool = new ExecutorCompletionService<Boolean>(threadPool);
//
//		for (int i = 0; i < algorithms.size(); i++)
//		{
//			for (int j = 0; j < repetitionNumber; j++)
//			{
//				if (multiThread)
//				{
//					AlgorithmRepetitionThread thread = new AlgorithmRepetitionThread(algorithms.get(i), j);
//					pool.submit(thread);
//				}
//				else
//					runAlgorithmRepetition(algorithms.get(i), j);
//			}
//		}
//
//		if (multiThread)
//		{
//			for (int i = 0; i < algorithms.size(); i++)
//			{
//				for (int j = 0; j < repetitionNumber; j++)
//				{
//					Future<Boolean> result = pool.take();
//					result.get();
//				}
//			}
//		}
//		
//		threadPool.shutdownNow();
//	}
//}
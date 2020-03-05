package benchmarks;

import interfaces.Problem;
import utils.random.RandUtils;

public class Noise extends Problem 
{
	public Noise(int dimension, double[][] bounds) { super(dimension, bounds); setFID("f0");}
	
	public void setProblemFID(String string) {setFID(string);}

	public double f(double[] x){return RandUtils.random();}
}

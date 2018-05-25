package algorithms.students;
//
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.MatLab.*;
//import static utils.MatLab.min;
//
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import java.util.Random;
//
import static utils.algorithms.Misc.*;

/**
 * Flower Pollination Algorithm
 * https://arxiv.org/pdf/1312.5673.pdf
 */
public class FPA extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		//
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		// Input Params
		int pop_size = getParameter("pop_size").intValue();
		double probability = getParameter("probability"); //0.8 recommended
		//double levy_scale = getParameter("scale"); // 1.0 is default used
		//double levy_mu = getParameter("mu"); // can be  between 1 and 3

		int count = 0;

		FlowerPollination FPA = new FlowerPollination(pop_size, problem);

		// Store initial best fitness
		FPA.EvaluateAndStoreBestFound();
		FT.add(count, FPA.bestFound.objFitness);
		count++;

		double[] newSolution = new double[problemDimension];

		//main loop
		while (count < maxEvaluations)
		{
			//
			for(int idx=0; idx < pop_size;idx++)
			{
				if(new Random(System.currentTimeMillis()).nextDouble() < probability)
				{
					// Global pollination (large scale, large distance)
					 newSolution = FPA.DoGlobalPollination(idx);
				}
				else
				{
					// Local pollination (small scale. small distance)
					newSolution = FPA.DoLocalPollination(idx);
				}

				//Evaluate & Update
				double fNew = problem.f(newSolution);

				// is the new potential solution better?
				if(fNew < FPA.flowers[idx].objFitness)
				{
					// yes - Update Flower
					FPA.flowers[idx].params = newSolution;
					FPA.flowers[idx].objFitness = fNew;
				}

			}

			FPA.EvaluateAndStoreBestFound();
			FT.add(count, FPA.bestFound.objFitness);

			count++;				
		}

		//
		finalBest = FPA.bestFound.params;
		FT.add(count, FPA.bestFound.objFitness);

		return FT; //return the fitness trend
	}
	
}

//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

 // Main FP Algorithm Class
class FlowerPollination
{
	 private Problem problem;
	 private int problemDimension;
	 private double[][] bounds;

	 int POP_SIZE;

	 public Flower bestFound;
	 public Flower[] flowers;

	 public FlowerPollination(int popSize, Problem p) throws  Exception
	 {
		 this.POP_SIZE = popSize;
		 this.problem = p;
		 this.problemDimension = problem.getDimension();
		 this.bounds = problem.getBounds();

		 this.flowers = new Flower[POP_SIZE];

		 this.InitialisePopulation();

		 bestFound = new Flower(this.flowers[0]);
	 }

	 public void InitialisePopulation() throws Exception
	 {
		 for (int i = 0; i < POP_SIZE; i++)
		 {
			 this.flowers[i] = new Flower(bounds, problemDimension);
			 this.flowers[i].objFitness = problem.f(flowers[i].params);
		 }
	 }

	public double EvaluateAndStoreBestFound()
	{
		for (int i = 0; i < POP_SIZE; i++)
		{
			if (this.flowers[i].objFitness < this.bestFound.objFitness)
			{
				this.bestFound = new Flower(flowers[i]);
			}
		}

		return this.bestFound.objFitness;
	}

	//
	public double[] DoGlobalPollination(int n) throws Exception
	{
		//double[] levy  = GetLevyFlightSample(); // get really weird results with this version.
		double[] levy  = Levy(); //

		double[] newSolution = new double[problemDimension];

		for (int i = 0; i < problemDimension; i++)
		{
			// x^[t+1]_[i] = x^[t]_[i] + L*(g_* -x^[t]_[i])
			newSolution[i] = this.flowers[n].params[i] + levy[i] * ( this.bestFound.params[i] - this.flowers[n].params[i]);
		}

		newSolution = toro(newSolution, bounds);

		return newSolution;
	}

	// local mutation with neighbours
	public double[] DoLocalPollination(int n) throws Exception
	{
		double epsilon = new Random(System.currentTimeMillis()).nextDouble(); //double in range [0,1]
		// j,k are random indexes

		int sol_j = new Random(System.currentTimeMillis()).nextInt(problemDimension);
		while(sol_j == n)
		{
			sol_j = new Random(System.currentTimeMillis()).nextInt(problemDimension);
		}

		int sol_k = new Random().nextInt(problemDimension);
		while(sol_k == sol_j || sol_k == n)
		{
			sol_k = new Random(System.currentTimeMillis()).nextInt(problemDimension);
		}

		double[] newSolution = new double[problemDimension];

		for (int i = 0; i < problemDimension; i++)
		{
			// x^[t+1]_[i] = x^[t]_[i] + epsilon*(x^t_j - x^t_j)
			newSolution[i] =  this.flowers[n].params[i] + (epsilon * (this.flowers[sol_j].params[i] - this.flowers[sol_k].params[i]));
		}

		newSolution = toro(newSolution, bounds);

		return newSolution;
	}

	//---------------------------------
	// Misc Helper Methods
	//---------------------------------

	//standard Gamma func (Stirling approximation: taken from https://rosettacode.org/wiki/Gamma_function#Java)
	 public double StirlingGamma(double x)
	 {
		 return Math.sqrt(2*Math.PI/x)*Math.pow((x/Math.E), x);
	 }

	/** creates Levy flight samples */
	//https://github.com/fum968/FPA/blob/master/src/fpa/FPA.java
	private double[] Levy()
	{
		// ND: Normal distribution
		double meanND = 0.0;
		double stdDevND = 1.0;
		double lambda = 1.5; // this should/could be an input param....

		double[] step = new double[problemDimension];

		double sigma = Math.pow(Gamma.gamma(1 + lambda) * Math.sin(Math.PI * lambda / 2)
				/ (Gamma.gamma((1 + lambda) / 2) * lambda * Math.pow(2, (lambda - 1) / 2)), (1 / lambda));

		for (int i = 0; i < problemDimension; i++) {

			double u = Distribution.normal(meanND, stdDevND) * sigma;
			double v = Distribution.normal(meanND, stdDevND);

			step[i] = 0.01 * u / (Math.pow(Math.abs(v), (1 - lambda)));
		}
		return step;
	}

	//
	public double[] GetLevyFlightSample()
	{
		double[] steps  = new double[problemDimension];

		for (int i = 0; i < problemDimension; i++)
		{
			//TODO: Not really happy with this......
			steps[i]  = LevyDistribution.sample_positive(1.5); // param can be in range [1,3]
		}

		return steps;
	 }
 }

//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

// Each Flower == A potential Solution
class Flower
{
	double[] params;
	double objFitness = Double.NaN;

	public Flower(double[][] bounds, int problemDimension)
	{
		this.params = new double[problemDimension];
		this.params = generateRandomSolution(bounds, problemDimension);
		this.params  = toro(params, bounds);
	}

	public Flower(Flower other)
	{
		this.params = new double[other.params.length];

		for(int i=0;i < params.length;i++)
		{
			this.params[i] = other.params[i];
		}

		this.objFitness = other.objFitness;
	}
}
//---------------------------------------------------------------------------------
// https://github.com/fum968/FPA/blob/master/src/fpa/Distribution.java
//---------------------------------------------------------------------------------
class Distribution {
	private static double spare;
	private static boolean isSpareReady = false;
	//
	private static Random rng = new Random(System.currentTimeMillis());

	/** Polar-Method */
	public static double normal(double mean, double stdDev) {

		if (isSpareReady) {
			isSpareReady = false;
			return spare * stdDev + mean;
		}
		else
		{
			double u, v, s;
			do {
				u = rng.nextDouble() * 2 - 1;
				v = rng.nextDouble() * 2 - 1;
				s = u * u + v * v;
			} while (s >= 1 || s == 0);

			double mul = StrictMath.sqrt(-2.0 * StrictMath.log(s) / s);

			spare = v * mul;
			isSpareReady = true;

			return mean + stdDev * u * mul;
		}
	}
}

//---------------------------------------------------------------------------------
// http://introcs.cs.princeton.edu/java/91float/Gamma.java
// http://introcs.cs.princeton.edu/java/91float/Gamma.java.html
//---------------------------------------------------------------------------------
class Gamma {

	static double logGamma(double x) {
		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);

		double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1) + 24.01409822
						/ (x + 2) - 1.231739516 / (x + 3) + 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);

		return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	}

	static double gamma(double x) {
		return Math.exp(logGamma(x));
	}
}

//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

// Taken From http://markread.info/2016/08/code-to-generate-a-levy-distribution
// Slightly modified to use standard(less good...) random number generation
// Not my Code!
/**
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Mark N. Read, 2016.
 *
 * Represents a Levy distributed random variable. This is calculated using the method outlined in
 * Jacobs's "Stochastic Processes for Physicists". 2010, Cambridge University Press.
 *
 * Makes use of MASON's MersenneTwisterFast random number generator. The MASon simulation framework can be obtained
 * for free from http://cs.gmu.edu/~eclab/projects/mason/
 *
 * @author Mark N. Read
 *
 */
class LevyDistribution
{
	private static Random rng = new Random(System.currentTimeMillis());

	private static double bounded_uniform(double low, double high)
	{
		// returns a double in interval (0,1). IE, neither zero nor one will be returned.
		double x = rng.nextDouble();

		double range = high - low;
		x *= range;	// scale onto the required range of values
		x += low;	// translate x onto the values requested

		return x;
	}

	/**
	 * Samples a Levy distribution wherein the power law decay can be adjusted between 1/x and 1/x^3.
	 *
	 * This method is based on that found in section 9.2.2 of
	 * Jacobs's "Stochastic Processes for Physicists". 2010, Cambridge University Press.
	 *
	 * Note that this sampling method can return negative values. Values are symmetrical around zero.
	 *
	 * @param mu must lie between 1 and 3. Corresponds to 1/x and 1/x^3
	 * @return
	 */
	public static double sample(double mu)
	{
		double X = bounded_uniform(-Math.PI/2.0, Math.PI/2.0);
		double Y = -Math.log(rng.nextDouble());
		double alpha = mu - 1.0;
		// there's a lot going on here, written over several lines to aid clarity.
		double Z = 	(Math.sin(alpha * X) / Math.pow( Math.cos(X) , 1.0 / alpha ))
				* Math.pow(Math.cos((1.0-alpha) * X) / Y, (1.0 - alpha) / alpha);
		return Z;
	}

	/**
	 * Same as above, but ensures all values are positive. Negative values are simply negated, as the Levy distribution
	 * represented is symmetrical around zero.
	 * @param mu
	 * @return
	 */
	public static double sample_positive(double mu, double scale)
	{
		double l = sample(mu) * scale;
		if (l < 0.0)
		{	return -1.0 * l;	}
		return l;
	}

	/** Default value case, scale=1 */
	public static double sample_positive(double mu)
	{	return sample_positive(mu, 1.0);		}
}


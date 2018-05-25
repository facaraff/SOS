package algorithms.students;
//
import static utils.algorithms.Misc.*;
import static utils.MatLab.max;
import static utils.MatLab.min;
//
import utils.MatLab;
import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

import java.util.ArrayList;
import java.util.Random;
//

/**
 * Memetic Artificial Bee Colony Algorithm
 * (ABC+SPSA+Nadam)
 */
public class MABC extends Algorithm
{
    @Override
    public FTrend execute(Problem problem, int maxEvaluations) throws Exception
    {
        //
        FTrend FT = new FTrend();
        int count = 0;

        // Input Params
        int colony_size = getParameter("colony_size").intValue(); // Max 100?
        int max_trials = getParameter("limit").intValue(); // Number onlookers * problem Dim

        //
        double max_local = getParameter("max_local"); // max local epochs

        //SPSA
        double c_param = getParameter("c");   //

        //Adam
        double learn_rate = getParameter("lr"); // 0.001
        double beta_1 = getParameter("b1"); //0.9
        double beta_2 = getParameter("b2"); // 0.999
        double decay = getParameter("decay"); // 0.0

        double[] adam_params = {max_local, c_param, learn_rate, beta_1, beta_2, decay};

        MemeticArtificalBeeColony MABC = new MemeticArtificalBeeColony(colony_size, max_trials, adam_params, problem);
        // Store initial best fitness
        MABC.MemoriseBestFoodSource();

        //
        //MABC.ImproveBestCandidate();

        FT.add(count, MABC.bestFound.objFitness);
       // count++;

        //
        while (count < maxEvaluations)
        {
            MABC.SendEmployedBees();
            MABC.CalculateProbabilities();
            MABC.SendOnlookerBees();

            // Find best bee in current population
            MABC.MemoriseBestFoodSource();
            FT.add(count, MABC.bestFound.objFitness);

            // Try to improve the best bee found so far (super fit individual)
            MABC.bestFound = new FoodSource(MABC.LocalImproveBee(MABC.bestFound));
            //MABC.bestFound = MABC.LocalImproveBee(MABC.bestFound);

            MABC.SendScoutBees();

            count++;
        }

        // Save the final values
        finalBest = MABC.bestFound.theta;
        FT.add(count, MABC.bestFound.objFitness);

        // Return the fitness trend
        return FT;
    }
}

//--------------------------------------------------------------------------------------------------\\
// Simultaneous Perturbation Stochastic Approximate Nesterov-accelerated Adaptive Moment Estimation \\
//--------------------------------------------------------------------------------------------------\\
class SPSANAdam
{
    int MAX_EVALS;

    double[] mBias;
    double[] vBias;
    double[] mHat;
    double[] vHat;
    double[] gradient;

    double[] theta;
    double[] theta_new;

    double localBest;

    //adam inputs
    double learn_rate; // 0.001
    double beta_1; //0.9
    double beta_2; // 0.999
    double decay; // 0.0

    //spsa input
    double c_param;   //

    Problem problem;
    double[][] bounds;
    int problemDimension;

    //std Ctor
    public SPSANAdam(double[] solutions, double[] params, Problem problem)
    {
        theta = solutions;
        this.problem = problem;
        this.bounds = problem.getBounds();
        this.problemDimension = problem.getDimension();

        mBias = new double[theta.length];
        vBias= new double[theta.length];
        mHat = new double[theta.length];
        vHat = new double[theta.length];
        theta_new = new double[theta.length];

        MAX_EVALS = (int)params[0];
        c_param = params[1];
        learn_rate = params[2];
        beta_1 = params[3];
        beta_2 = params[4];
        decay = params[5];

        // Zero these out.
        for (int i = 0; i < theta.length; i++){
            mBias[i] = 0.0;
            vBias[i] = 0.0;
        }

        gradient = null;
        localBest = Double.MAX_VALUE;

    }

    // Nadam
    public double[][] Run() throws Exception
    {
        int count = 0;
        double epsilon = 1e-10; // from paper defaults

        while (count < MAX_EVALS)
        {
            double[] theta_saved = theta;

            // ----------------------- Run SPSA2 algorithm ----------------------- \\
            //double[] batch = theta; //GetMiniBatch(theta);
            //CalcCurrentApproxGradient(double c_param, double[] theta, int k, Problem problem)
            gradient = CalcCurrentApproxGradient(c_param, theta, count, problem);

            // ----------------------- then Adam algorithm as normal. ----------------------- \\
            //Update biased 1st moment estimate
            mBias = MatLab.sum(MatLab.multiply(beta_1, mBias), MatLab.multiply(1 - beta_1, gradient));
            //Update biased 2nd raw moment estimate
            vBias = MatLab.sum(MatLab.multiply(beta_2, vBias), MatLab.multiply((1 - beta_2), MatLab.dot(gradient, gradient)));

            for (int i = 0; i < theta.length; i++) {
                //Compute bias-corrected 1st moment estimate
                mHat[i] = mBias[i] / (1 - Math.pow(beta_1, count));
                //Compute bias-corrected 2nd raw moment estimate
                vHat[i] = vBias[i] / (1 - Math.pow(beta_2, count));
            }

            //Nesterov Momentum Variant:
            for (int i = 0; i < theta.length; i++) {
                theta_new[i] = theta[i] - ((learn_rate * mHat[i]) / (Math.sqrt(vHat[i]) + epsilon)) * ((beta_1 * mHat[i]) + ((1 - beta_1) * gradient[i]) / (1 - Math.pow(beta_1, i)));
            }

            //Toroidal correction.
            theta = toro(theta_new, bounds);
            // Fitness Checks
            double fTheta = problem.f(theta);

            // is new theta better?
            if (fTheta < localBest) {
                // Yes: update the fitness
                localBest = fTheta;
            } else {
                theta = theta_saved;
            }

            count++;
        }

        //lazy way....
        double[][] tmp = {theta, {localBest}};
        return tmp;
    }

    // SPSA part.
    private double[] CalcCurrentApproxGradient(double c_param, double[] theta, int k, Problem problem) throws Exception
    {
        double gamma = 0.101;
        double ck = Double.NaN;
        double[] delta = new double[theta.length];
        double noise = 0.0;

        // Gain sequence
        ck = c_param / (Math.pow((k+1), gamma)); // perturbation magnitude

        // Generation of simultaneous perturbation vector
        for (int j = 0; j < theta.length; j++)
        {
            // Bernouilli +-1 distribution (Each value along the array has a 50% chance of being either a -1 or 1)
            delta[j] = 2.0 * Math.round(new Random().nextDouble()) - 1;
        }

        // 2x function evaluations
        double[] theta_plus = MatLab.sum(theta , MatLab.multiply(ck ,delta));
        double[] theta_minus = MatLab.subtract(theta , MatLab.multiply(ck ,delta));

        // This assumes Zero Noise (for now) so loss function is just problem function to be minimised.
        double yplus = problem.f(theta_plus) + noise;
        double yminus = problem.f(theta_minus) + noise;

        // Gradient approximation (via the 2 observations of fitness above)
        double[] gHat = new double[theta.length];

        for (int j = 0; j < theta.length; j++)
        {
            gHat[j] = (yplus - yminus) / (2.0 * ck * delta[j]);
        }
        //
        return gHat;
    }
}


//
class MemeticArtificalBeeColony
{
    private Problem problem = null;
    private int problemDimension = 0;
    private double[][] bounds = null;

    private int COLONY_SIZE = 0;
    private int NUM_FOOD_SOURCES = 0;
    private int MAX_TRIALS = 50;

    FoodSource bestFound = null;
    private FoodSource[] foodSources = null;

    private double[] spsanadam_params;

    public MemeticArtificalBeeColony(int colony_size, int max_trials, double[] params, Problem problem) throws Exception
    {
        spsanadam_params = params;

        this.problem = problem;
        this.problemDimension = problem.getDimension();
        this.bounds = problem.getBounds();
        //
        this.COLONY_SIZE = colony_size;
        this.NUM_FOOD_SOURCES = colony_size/2;
        this.MAX_TRIALS = max_trials;

        //Initialisation
        bestFound = new FoodSource(problemDimension);
        //bestFound.theta = generateRandomSolution(bounds, problemDimension);
        //bestFound.objFitness = problem.f(foodSources[i].theta);
        //bestFound.Initialise();

        foodSources = new FoodSource[NUM_FOOD_SOURCES];

        // Init FoodSources.
        for(int i=0;i<NUM_FOOD_SOURCES;i++)
        {
            foodSources[i] = new FoodSource(problemDimension);
            foodSources[i].theta = generateRandomSolution(bounds, problemDimension);

            // Attempt Higher Quality Starting Positions
            foodSources[i] = new FoodSource(LocalImproveBee(foodSources[i]));

            //foodSources[i].objFitness = problem.f(foodSources[i].theta);
            //foodSources[i].Initialise();
        }

        //TODO: FIX THIS..
        bestFound = new FoodSource(foodSources[0]);
    }

    // Update bestFound
    public double MemoriseBestFoodSource()
    {
        for(int i=0;i < NUM_FOOD_SOURCES;i++)
        {
            if(foodSources[i].objFitness < bestFound.objFitness )
            {
                bestFound = new FoodSource(foodSources[i]);
            }
        }

        return bestFound.objFitness;
    }

    //local search on current solution to see if it can be improved further.
    public FoodSource LocalImproveBee(FoodSource current) throws Exception
    {
        FoodSource candidate = new FoodSource(current);
        double[][] improved = new SPSANAdam(candidate.theta, spsanadam_params, problem).Run();

        candidate.theta = improved[0];
        candidate.objFitness = improved[1][0];
        candidate.CalcFitness();

        //this.probability =
        //candidate.trials = 0; //reset here?

        if(candidate.objFitness < current.objFitness)
        {
            //local search must have made it better!
            current = new FoodSource(candidate);
        }

        return current;
    }

    //
    public void SendEmployedBees() throws Exception
    {
        //
        int neighbourBeeIndex = 0; // //= new Random().nextInt(problemDimension);  // zero to problemDimension -1
        int paramIndex = 0;

        // for each Employed Bee...
        for(int i=0;i < NUM_FOOD_SOURCES;i++)
        {
            // Randomly choose a parameter to modify
            paramIndex = new Random().nextInt(problemDimension);

            // Randomly select a solution to mutate with (must not be current solution 'i')
            // (i.e. This Bee cannot mutate with itself..)
            neighbourBeeIndex = new Random().nextInt(NUM_FOOD_SOURCES);
            while( i == neighbourBeeIndex)
            {
                //TODO: Do this a bit better?
                neighbourBeeIndex = new Random().nextInt(NUM_FOOD_SOURCES);
            }

            //
            FoodSource currentBeeFS = new FoodSource(foodSources[i]);
            FoodSource neighbourBeeFS = new FoodSource(foodSources[neighbourBeeIndex]);

            double phi = (new Random().nextDouble() - 0.5)*2;   // random number in the range [-1,1]

            // Mutate and handle bounds ( v_{ij} = x_{ij} + (phi * (x_ij - x_{kj})) )
            FoodSource newBee = new FoodSource(currentBeeFS);
            newBee.theta[paramIndex] = currentBeeFS.theta[paramIndex] + (phi *(currentBeeFS.theta[paramIndex] - neighbourBeeFS.theta[paramIndex]));

            // TODO: Toroidal bounds correction (inefficient - currently whole theta at once)
            // TODO: correct a single value?
            // OR Move this part else where...
            double[] tmp = toro(newBee.theta, bounds);
            newBee.theta = tmp;

            //Evaluate this solution & calculate its fitness score.
            newBee.objFitness = this.problem.f(newBee.theta);
            newBee.CalcFitness();

            // is the mutated new solution better?
            if(newBee.fitness > currentBeeFS.fitness)
            {
                // yes - replace with new solution
                foodSources[i] = new FoodSource(newBee);
                // reset trials counter
                foodSources[i].trials = 0;
            }
            else
            {
                // not better ->  increase trials counter
                foodSources[i].trials++;
                // should not have to restore anything.
            }
        }
    }

    //
    public void CalculateProbabilities()
    {
        // prob_{i} = fit_{i} / sum_{n=1,n=CS/2} fit_{n}
        double sumFitness = 0.0;
        for(FoodSource source : foodSources)
        {
            sumFitness += source.fitness;
        }

        // Calculate & set probability of selection for each food source
        for(FoodSource source : foodSources)
        {
            //source.probability = source.fitness / sumFitness ;
            //paper has above, however all implementations i have seen are more like the below with added magic numbers....
            source.probability = (0.9*(source.fitness / sumFitness)) + 0.1 ;
        }
    }

    //
    public void SendOnlookerBees() throws Exception
    {
        //
        int i = 0;  // food source index
        int t = 0;  // onlooker bee food source index
        int neighbourBeeIndex = 0;
        int paramIndex = 0;

        while(t < NUM_FOOD_SOURCES)
        {
            //take a deep copy.
            FoodSource currentBeeFS = new FoodSource(foodSources[i]);

            if(new Random().nextDouble() < currentBeeFS.probability) // TODO:  check this.
            {
                t++;
                neighbourBeeIndex = new Random().nextInt(NUM_FOOD_SOURCES);
                while( i == neighbourBeeIndex)
                {
                    //TODO: Do this a bit better?
                    neighbourBeeIndex = new Random().nextInt(NUM_FOOD_SOURCES);
                }

                FoodSource neighbourBeeFS = new FoodSource(foodSources[neighbourBeeIndex]);

                // Randomly choose a parameter to modify
                paramIndex = new Random().nextInt(problemDimension);

                double phi = (new Random().nextDouble() - 0.5)*2;   // random number in the range [-1,1]
                // Mutate and handle bounds ( v_{ij} = x_{ij} + (phi * (x_ij - x_{kj})) )

                FoodSource newBee = new FoodSource(currentBeeFS);
                newBee.theta[paramIndex] = currentBeeFS.theta[paramIndex] + (phi *(currentBeeFS.theta[paramIndex] - neighbourBeeFS.theta[paramIndex]));

                // TODO: Toroidal bounds correction (inefficient - currently whole theta at once)
                // TODO: correct a single value?
                // OR Move this part else where...
                double[] tmp = toro(newBee.theta, bounds);
                newBee.theta = tmp;

                //Evaluate this solution & calculate its fitness score.
                newBee.objFitness = problem.f(newBee.theta);
                newBee.CalcFitness();

                // is the mutated new solution better?
                if(newBee.fitness > currentBeeFS.fitness)
                {
                    // yes - replace with new solution
                    foodSources[i] = new FoodSource(newBee);
                    // reset trials counter
                    foodSources[i].trials = 0;
                }
                else
                {
                    // not better ->  increase trials counter
                    foodSources[i].trials++;

                    // as we took a deep copy to modify & test we should not have to restore anything.
                }
            }

            i++;

            if(i == NUM_FOOD_SOURCES)
            {
                i = 0; //reset
            }
        }
    }

    //
    public void SendScoutBees() throws Exception
    {
        int maxIndex = 0;

        for(int i=0; i < NUM_FOOD_SOURCES; i++)
        {
            if(foodSources[i].trials > foodSources[maxIndex].trials )
            {
                maxIndex = i;
            }

            // Re-Init this food source.
            // TODO:  make it a single call to initialise.........
            if(foodSources[maxIndex].trials >= MAX_TRIALS)
            {
                //foodSources[maxIndex].Initialise();
                foodSources[maxIndex].theta = generateRandomSolution(bounds, problemDimension);
                foodSources[maxIndex].objFitness = problem.f(foodSources[maxIndex].theta);
                foodSources[maxIndex].Initialise();
            }
        }
    }

}

//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

// possible solutions are denoted as food sources
/*
class FoodSource
{
    double[] theta; // the params to be optimised
    double objFitness; // the value returned from objective (problem) function
    double fitness;  // the fitness of this food source
    double probability; // the selection probability for this food source
    int trials; //

    // Normal
    public FoodSource(int problemDimension)
    {
        this.theta = new double[problemDimension];
        //this.theta = generateRandomSolution(bounds, problemDimension);

        this.objFitness = Double.MAX_VALUE; //
        this.fitness = 0.0;
        this.probability = 0.0;
        this.trials = 0;
    }

    // Copy
    public FoodSource(FoodSource other)
    {
        this.theta = new double[other.theta.length];

        for(int i=0;i < theta.length;i++)
        {
            this.theta[i] = other.theta[i];
        }

        this.objFitness = other.objFitness;
        this.fitness = other.fitness;
        this.probability = other.probability;
        this.trials = other.trials;
    }

    // Initialise this food source.
    public void Initialise()
    {
        fitness = CalculateFitness(this.objFitness);
    }

    //
    public void CalcFitness()
    {
        this.fitness = CalculateFitness(this.objFitness);
    }

    // food source fitness score.
    private double CalculateFitness(double objectiveFitness)
    {
        double fitness  = 0.0;
        if(objectiveFitness >= 0)
        {
            fitness = 1 / (objectiveFitness + 1);
        }
        else
        {
            fitness = 1 + Math.abs(objectiveFitness);
        }

        return fitness;
    }
}
*/
//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

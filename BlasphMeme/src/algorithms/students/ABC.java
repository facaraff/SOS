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
 * Artificial Bee Colony Algorithm
 * "Not the Bees!!!" : https://www.youtube.com/watch?v=EVCrmXW6-Pk
 * Refs:
 */
public class ABC extends Algorithm
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

/*
        if (initialSolution != null)
        {
            theta = initialSolution;
            fBest = initialFitness;
        }
        else
        {
            // Generate first guess...
            theta = generateRandomSolution(bounds, problemDimension);
            fBest = problem.f(theta);
            FT.add(count, fBest);
            count++;
        }
*/

        //
        ArtificalBeeColony ABC = new ArtificalBeeColony(colony_size, max_trials, problem);

        // Store initial best fitness
        ABC.MemoriseBestFoodSource();
        FT.add(count, ABC.bestFound.objFitness);
        count++;

        //
        while (count < maxEvaluations)
        {
            ABC.SendEmployedBees();
            ABC.CalculateProbabilities();
            ABC.SendOnlookerBees();

            //ABC.ImproveBestCandidates();

            ABC.MemoriseBestFoodSource();
            FT.add(count, ABC.bestFound.objFitness);
            ABC.SendScoutBees();

            count++;
        }

        // Save the final values
        finalBest = ABC.bestFound.theta;
        FT.add(count, ABC.bestFound.objFitness);

        // Return the fitness trend
        return FT;
    }
}

//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

//
class ArtificalBeeColony
{

    private Problem problem = null;
    private int problemDimension = 0;
    private double[][] bounds = null;

    private int COLONY_SIZE = 0;
    private int NUM_FOOD_SOURCES = 0;
    private int MAX_TRIALS = 50;

    FoodSource bestFound = null;
    private FoodSource[] foodSources = null;

    public ArtificalBeeColony(int colony_size, int max_trials, Problem problem) throws Exception
    {
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
            foodSources[i].objFitness = problem.f(foodSources[i].theta);
            foodSources[i].Initialise();
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

//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

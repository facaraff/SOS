package algorithms;

import SearchProblem.SimpleAgentSearchProblem;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.MatLab;
import utils.RunAndStore;
import utils.algorithms.TopNPicker;
import utils.random.RandUtils;

import java.util.ArrayList;

import static utils.MatLab.max;
import static utils.MatLab.min;

/**
 * Created by Mikolaj on 06/05/2018.
 */
public class JADEDEWExponentialFreezing extends Algorithm {
    @Override
    public RunAndStore.FTrend execute(Problem problem, int budget) throws Exception {

        int computations = 0;
        SimpleAgentSearchProblem searchProblem = (SimpleAgentSearchProblem)problem;

        int populationSize = getParameter("PopulationSize").intValue();
        double topPercentage = getParameter("p");
        int archiveSize = getParameter("ArchiveSize").intValue();
        double cAdaptionParameter = getParameter("c");
        double freezingConstant = getParameter("freeze");

        double totalGenerations = (double)budget/(double)populationSize;
        double generationCount = 1;

        int numberTopPercentage = (int)(topPercentage*populationSize);

        double muF = 0.5; double muCR = 0.5;
        ArrayList<double[]> archive = new ArrayList<double[]>();
        ArrayList<double[][]> archiveWaypoints = new ArrayList<double[][]>();

        double[][] population = new double[populationSize][problem.getDimension()];
        double[][][] populationWaypoints = new double[populationSize][problem.getDimension()][2];
        double[] populationFitness = new double[populationSize];
        populationFitness[0] = Double.MAX_VALUE;
        TopNPicker populationBestN = new TopNPicker(numberTopPercentage);

        for (int i=0;i<populationSize;i++) {
            //TODO: get start points from problem definition?
            double[] previousLocation = new double[]{searchProblem.startXPos,searchProblem.startYPos};
            double previousHeading = 0;

            for (int j = 0; j < problem.getDimension(); j++){
                population[i][j] = RandUtils.uniform(problem.getBounds()[j][0], problem.getBounds()[j][1]);
                previousHeading = previousHeading + population[i][j];

                populationWaypoints[i][j][0] = previousLocation[0] + (Math.sin(previousHeading) * searchProblem.waypointDistance);
                populationWaypoints[i][j][1] = previousLocation[1] + (Math.cos(previousHeading) * searchProblem.waypointDistance);

                previousLocation = populationWaypoints[i][j];
            }
            populationFitness[i] = problem.f(population[i]); computations++;

            populationBestN.add(populationFitness[i], i);
        }

        // Create the FTrend to store the fitness trend
        RunAndStore.FTrend fTrend = new RunAndStore.FTrend();
        int fitnessTrendCount = 0;
        fTrend.add(fitnessTrendCount++, populationBestN.getBestFitness());

        while (computations < budget)
        {
            double sCR = 0.0;
            double countCR = 0.0;
            double sSqrF = 0.0;
            double sF = 0.0;

            double[][] newPopulation = new double[populationSize][problem.getDimension()];
            double[][][] newPopulationWaypoints = new double[populationSize][problem.getDimension()][2];
            double[] newPopulationFitness = new double[populationSize];
            TopNPicker newTopN = new TopNPicker(numberTopPercentage);

            for (int i = 0; i < populationSize; i++) {

                double CR = RandUtils.gaussian(muCR, 0.1);
                double F = RandUtils.cauchy(muF, 0.1);

                //MUTATION STEPS

                int randomBestIndex = populationBestN.getRandomTopIndex();
                int randomPopulationIndex;
                int randomArchiveIndex;

                do {
                    randomPopulationIndex = RandUtils.randomInteger(populationSize-1);
                } while (randomPopulationIndex == randomBestIndex || randomPopulationIndex == i);

                do {
                    randomArchiveIndex = RandUtils.randomInteger(populationSize+archive.size()-1);
                } while (randomArchiveIndex == randomPopulationIndex || randomArchiveIndex == randomBestIndex || randomArchiveIndex == i);

                double[][] randomBestWaypoints = populationWaypoints[randomBestIndex];
                double[][] randomPopulationWaypoints = populationWaypoints[randomPopulationIndex];
                double[][] randomArchiveWaypoints;

                // get the solution from the archive if the index is larger than population
                if (randomArchiveIndex < populationSize)
                    randomArchiveWaypoints = populationWaypoints[randomArchiveIndex];
                else
                    randomArchiveWaypoints = archiveWaypoints.get(randomArchiveIndex - populationSize);

                double[][] mutatedwaypoints = MatLab.sum(populationWaypoints[i],
                        MatLab.sum(MatLab.multiply(F, MatLab.subtract(randomBestWaypoints, populationWaypoints[i])),
                                MatLab.multiply(F, MatLab.subtract(randomPopulationWaypoints, randomArchiveWaypoints))));


                int selectedIndex = RandUtils.randomInteger(mutatedwaypoints.length);
                double[][] crossedOverWaypoints = new double[mutatedwaypoints.length][2];

                //CROSSOVER AND DECODING

                double[] crossedOver = new double[mutatedwaypoints.length];
                double[] previousLocation = new double[]{searchProblem.startXPos,searchProblem.startYPos};
                double previousHeading = 0;
                for (int w = 0; w < crossedOver.length; w++)
                {
                    double dX = mutatedwaypoints[w][0] - previousLocation[0];
                    double dY = mutatedwaypoints[w][1] - previousLocation[1];

                    if (!(w == selectedIndex || RandUtils.random() < CR)) {
                        dX = populationWaypoints[i][w][0] - previousLocation[0];
                        dY = populationWaypoints[i][w][1] - previousLocation[1];
                    }

                    double saturationFactor = 2*Math.exp(-((1-((double)w/(double)crossedOver.length))*freezingConstant*(generationCount/totalGenerations)));

                    double newBearing = Math.atan2(dX, dY);

                    double newRelativeToOldBearing = newBearing - previousHeading + population[i][w];

                    //saturate relative to old population
                    double adjustedRelativeToOldBearing = min(max(newRelativeToOldBearing,
                                    problem.getBounds()[w][0]*saturationFactor),
                            problem.getBounds()[w][1]*saturationFactor);

                    //saturate actual
                    crossedOver[w] = min(max(adjustedRelativeToOldBearing - population[i][w],
                                    problem.getBounds()[w][0]),
                            problem.getBounds()[w][1]);


                    previousHeading = crossedOver[w] + previousHeading;

                    previousLocation[0] = previousLocation[0] + (Math.sin(previousHeading) * searchProblem.waypointDistance);
                    previousLocation[1] = previousLocation[1] + (Math.cos(previousHeading) * searchProblem.waypointDistance);

                    crossedOverWaypoints[w] = previousLocation;

                }

                double crossedOverFitness = problem.f(crossedOver); computations++;

                if (populationFitness[i] < crossedOverFitness) {
                    newPopulation[i] = population[i];
                    newPopulationWaypoints[i] = populationWaypoints[i];
                    newPopulationFitness[i] = populationFitness[i];
                    newTopN.add(populationFitness[i], i);
                }
                else {
                    newPopulation[i] = crossedOver;
                    newPopulationWaypoints[i] = crossedOverWaypoints;
                    newPopulationFitness[i] = crossedOverFitness;
                    newTopN.add(crossedOverFitness, i);

                    archive.add(population[i]);
                    archiveWaypoints.add(populationWaypoints[i]);
                    sCR += CR;
                    countCR++;
                    sSqrF += F*F;
                    sF += F;
                }
            }

            while (archive.size() > archiveSize) {
                int randomIndex = RandUtils.randomInteger(archive.size() - 1);
                archive.remove(randomIndex);
                archiveWaypoints.remove(randomIndex);
            }

            population = newPopulation;
            populationWaypoints = newPopulationWaypoints;
            populationFitness = newPopulationFitness;
            populationBestN = newTopN;

            // UPDATE MU PARAMS
            double meanSuccessfulCR;
            if (sCR > 0)
                meanSuccessfulCR = sCR/countCR;
            else
                meanSuccessfulCR = 0.0;

            double lehmerMeanSuccessfulF;
            if (sSqrF > 0)
                lehmerMeanSuccessfulF = sSqrF/sF;
            else
                lehmerMeanSuccessfulF = 0.0;

            muCR = ((1-cAdaptionParameter)*muCR)+(cAdaptionParameter*meanSuccessfulCR);
            muF = ((1-cAdaptionParameter)*muCR)+(cAdaptionParameter*lehmerMeanSuccessfulF);

            fTrend.add(fitnessTrendCount++, populationBestN.getBestFitness());

            generationCount++;
        }


        finalBest = population[populationBestN.getBestIndex()];

        return fTrend;
    }

    public double[] binomialXO(double[] old, double[] mutated, double crScalar){
        double[] xo = new double[old.length];

        int selectedIndex = RandUtils.randomInteger(old.length);

        for (int i = 0; i < old.length; i++){
            if (i == selectedIndex || RandUtils.random() < crScalar){
                xo[i] = mutated[i];
            }
            else {
                xo[i] = old[i];
            }
        }

        return  xo;
    }
}

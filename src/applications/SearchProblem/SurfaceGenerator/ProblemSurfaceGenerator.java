package applications.SearchProblem.SurfaceGenerator;

import interfaces.Problem;
import org.sf.surfaceplot.ISurfacePlotModel;

/**
 * Created by Mikolaj on 18/03/2018.
 */
public class ProblemSurfaceGenerator implements ISurfacePlotModel
{
    private final float minFitness;
    private final float maxFitness;
    private Problem problem;
    private double[] inputArray;

    public ProblemSurfaceGenerator(Problem problem, float minFitness, float maxFitness)
    {
        this.problem = problem;
        this.minFitness = minFitness;
        this.maxFitness = maxFitness;
        inputArray = new double[problem.getDimension()];

        for (int i = 0; i<inputArray.length; i++)
            inputArray[i] = 0.0;
    }

    @Override
    public int getPlotMode() {
        return ISurfacePlotModel.PLOT_MODE_SPECTRUM;
    }

    @Override
    public float calculateZ(float x_1, float x_2) {
        double fitness = 0;
        inputArray[0] = x_1;
        inputArray[1] = x_2;

        try {
            fitness = problem.f(inputArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (float)fitness;
    }

    @Override
    public boolean isBoxed() {
        return false;
    }

    @Override
    public boolean isMesh() {
        return true;
    }

    @Override
    public boolean isScaleBox() {
        return false;
    }

    @Override
    public boolean isDisplayXY() {
        return true;
    }

    @Override
    public boolean isDisplayZ() {
        return true;
    }

    @Override
    public boolean isDisplayGrids() {
        return true;
    }

    @Override
    public int getCalcDivisions() {
        return 100;
    }

    @Override
    public int getDispDivisions() {
        return 100;
    }

    @Override
    public float getXMin() {
        return (float)problem.getBounds()[0][0];
    }

    @Override
    public float getXMax() {
        return (float)problem.getBounds()[0][1];
    }

    @Override
    public float getYMin() {
        return (float)problem.getBounds()[1][0];
    }

    @Override
    public float getYMax() {
        return (float)problem.getBounds()[1][1];
    }

    @Override
    public float getZMin() {
        return minFitness;
    }

    @Override
    public float getZMax() {
        return maxFitness;
    }

    @Override
    public String getXAxisLabel() {
        return "X1";
    }

    @Override
    public String getYAxisLabel() {
        return "X2";
    }

    @Override
    public String getZAxisLabel() {
        return "Fitness";
    }
}

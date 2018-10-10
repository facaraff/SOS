package mains.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import javabbob.JNIfgeneric;

/** Wrapper class running an entire BBOB experiment.
 * It illustrates the benchmarking of MY_OPTIMIZER on the noise-free testbed
 * or the noisy testbed (change the ifun loop in this case as given below).
 * This class reimplements the content of exampleexperiment.c from the original
 * C version of the BBOB code.
 */
public class TestBBOB2010 {

    /** Example optimiser.
     * In the following, the pure random search optimization method is
     * implemented as an example.
     * Please include/insert any code as suitable.<p>
     * This optimiser takes as argument an instance of JNIfgeneric
     * which have all the information on the problem to solve.
     * Only the methods getFtarget() and evaluate(x) of the class JNIfgeneric
     * are used.<p>
     * This method also takes as argument an instance of Random since one
     * might want to set the seed of the random search.<p>
     * The optimiser generates random vectors evaluated on fgeneric until
     * the number of function evaluations is greater than maxfunevals or
     * a function value smaller than the target given by fgeneric.getFtarget()
     * is attained.
     * The parameter maxfunevals to avoid problem when comparing it to
     * 1000000000*dim where dim is the dimension of the problem.
     * @param fgeneric an instance JNIfgeneric object
     * @param dim an integer giving the dimension of the problem
     * @param maxfunevals the maximum number of function evaluations
     * @param rand an instance of Random
     */
    public static void MY_OPTIMIZER(JNIfgeneric fgeneric, int dim, double maxfunevals, Random rand) {

        double[] x = new double[dim];

        /* Obtain the target function value, which only use is termination */
        double ftarget = fgeneric.getFtarget();
        double f;

        if (maxfunevals > 1e9 * dim) {
             maxfunevals = 1e9 * dim;
        }

        for (double iter = 0.; iter < maxfunevals; iter++) {
            /* Generate individual */
            for (int i = 0; i < dim; i++) {
                x[i] = 10. * rand.nextDouble() - 5.;
            }

            /* evaluate X on the objective function */
            f = fgeneric.evaluate(x);

            if (f < ftarget) {
                break;
            }
        }
    }

    /** Main method for running the whole BBOB experiment.
     *  Executing this method runs the experiment.
     *  The first command-line input argument is interpreted: if given, it
     *  denotes the data directory to write in (in which case it overrides
     *  the one assigned in the preamble of the method).
     */
    public static void main(String[] args) {

        /* run variables for the function/dimension/instances loops */
        final int dim[] = {2, 3, 5, 10, 20, 40};
        final int instances[] = {1, 2, 3, 4, 5, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        int idx_dim, ifun, idx_instances, independent_restarts;
        double maxfunevals;
        String outputPath;

        JNIfgeneric fgeneric = new JNIfgeneric();
        /* The line above loads the library cjavabbob at the core of
         * JNIfgeneric. It will throw runtime errors if the library is not
         * found.
         */

        /**************************************************
         *          BBOB Mandatory initialization         *
         *************************************************/
        JNIfgeneric.Params params = new JNIfgeneric.Params();
        /* Modify the following parameters, choosing a different setting
         * for each new experiment */
        params.algName = "PUT ALGORITHM NAME";
        params.comments = "PUT MORE DETAILED INFORMATION, PARAMETER SETTINGS ETC";
        outputPath = "PUT_MY_BBOB_DATA_PATH";

        if (args.length > 0) {
            outputPath = args[0]; // Warning: might override the assignment above.
        }

        /* Creates the folders for storing the experimental data. */
        if ( JNIfgeneric.makeBBOBdirs(outputPath, false) ) {
            System.out.println("BBOB data directories at " + outputPath
                    + " created.");
        } else {
            System.out.println("Error! BBOB data directories at " + outputPath
                    + " was NOT created, stopping.");
            return;
        };


        /* External initialization of MY_OPTIMIZER */
        long seed = System.currentTimeMillis();
        Random rand = new Random(seed);
        System.out.println("MY_OPTIMIZER seed: "+ seed);

        /* record starting time (also useful as random number generation seed) */
        long t0 = System.currentTimeMillis();

        /* To make the noise deterministic, uncomment the following block. */
        /* int noiseseed = 30; // or (int)t0
         * fgeneric.setNoiseSeed(noiseseed);
         * System.out.println("seed for the noise set to: "+noiseseed); */

        /* now the main loop */
        for (idx_dim = 0; idx_dim < 6; idx_dim++) {
            /* Function indices are from 1 to 24 (noiseless) or from 101 to 130 (noisy) */
            /* for (ifun = 101; ifun <= 130; ifun++) { // Noisy testbed */
            for (ifun = 1; ifun <= 24; ifun++) { //Noiseless testbed
                for (idx_instances = 0; idx_instances < 15; idx_instances++) {

                    /* Initialize the objective function in fgeneric. */
                    fgeneric.initBBOB(ifun, instances[idx_instances],
                                      dim[idx_dim], outputPath, params);
                    /* Call to the optimizer with fgeneric as input */
                    maxfunevals = 5. * dim[idx_dim]; /* PUT APPROPRIATE MAX. FEVALS */
                                                     /* 5. * dim is fine to just check everything */
                    independent_restarts = -1;
                    while (fgeneric.getEvaluations() < maxfunevals) {
                        independent_restarts ++;
                        MY_OPTIMIZER(fgeneric, dim[idx_dim],
                                     maxfunevals - fgeneric.getEvaluations(), rand);
                        if (fgeneric.getBest() < fgeneric.getFtarget())
                            break;
                    }

                    System.out.printf("  f%d in %d-D, instance %d: FEs=%.0f with %d restarts,", ifun, dim[idx_dim],
                                      instances[idx_instances], fgeneric.getEvaluations(), independent_restarts);
                    System.out.printf(" fbest-ftarget=%.4e, elapsed time [h]: %.2f\n", fgeneric.getBest() - fgeneric.getFtarget(),
                                      (double) (System.currentTimeMillis() - t0) / 3600000.);

                    /* call the BBOB closing function to wrap things up neatly */
                    fgeneric.exitBBOB();
                }

                System.out.println("\ndate and time: " + (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(
                        (Calendar.getInstance()).getTime()));

            }
            System.out.println("---- dimension " + dim[idx_dim] + "-D done ----");
        }
    }
}


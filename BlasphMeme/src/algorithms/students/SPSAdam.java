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

import java.util.Random;
//

/**
 * Adam + SPSA Hybrid. for blackbox optimisation.
 * Ref: Adam - A Method for Stochastic Optimization http://arxiv.org/abs/1412.6980v8
 */
public class SPSAdam extends Algorithm
{
    @Override
    public FTrend execute(Problem problem, int maxEvaluations) throws Exception
    {
        //
        FTrend FT = new FTrend();
        int problemDimension = problem.getDimension();
        double[][] bounds = problem.getBounds();

        // Input Params SPSA
       // double alpha = getParameter("alpha"); //
       // double gamma = getParameter("gamma"); //
        double a_param = getParameter("a");   //
        double c_param = getParameter("c");   //


        // Input Params Adam
        double learn_rate = getParameter("lr"); // 0.001
        double beta_1 = getParameter("b1"); //0.9
        double beta_2 = getParameter("b2"); // 0.999
        //double epsilon = getParameter("epsilon"); // 0.0
        double decay = getParameter("decay"); // 0.0

        // Step 1: Initialisation and coefficient selection
        double[] theta = new double[problemDimension]; // Array of values to be minimised
        //double[] theta_prev = new double[problemDimension]; // last time step theta values
        double fBest = Double.NaN; // Fitness value, i.e. "f(x)" of the above

        double[] mBias = new double[theta.length];
        double[] vBias= new double[theta.length];
        double[] mHat = new double[theta.length];
        double[] vHat = new double[theta.length];

        double[] theta_new = new double[theta.length];

        // Zero these out.
        for (int i = 0; i < theta.length; i++){
            mBias[i] = 0.0;
            vBias[i] = 0.0;
        }

        double[] gradient = null;

        int count = 0;

        if (initialSolution != null)
        {
            theta = initialSolution;
            fBest = initialFitness;
        }
        else
        {
            // Generate first guess...
            theta = generateRandomSolution(bounds, problemDimension);
            //theta_prev = theta;
            fBest = problem.f(theta);
            FT.add(count, fBest);
            //count++;
        }

        //
        double A = maxEvaluations/10; // stability constant @ 10%
        //epsilon = Math.sqrt(2.2204e-16); // from matlab docs
        double epsilon = 1e-10; // from paper defaults

        //main loop
        while (count < maxEvaluations)
        { // add convergence check

            double[] theta_saved = theta;

            //get the approx gradient via SPSA2, could also use mini-batch of theta params.
            //double[] batch = theta; // for now. // GetBatch(theta);

            //CalcCurrentApproxGradient(double c_param, double[] theta, int k, Problem problem)
            gradient = CalcCurrentApproxGradient(c_param, theta, count, problem);

            // ----------------------- then Adam algorithm as normal. ----------------------- \\

            //Update biased 1st moment estimate
            //m = beta1.*m + (1 - beta1) .* optimValues.gradient(:);  // .* element-wise multiplication
            mBias = MatLab.sum(MatLab.multiply(beta_1, mBias) , MatLab.multiply(1 - beta_1, gradient));

            //Update biased 2nd raw moment estimate
            //v = beta2.*v + (1 - beta2) .* (optimValues.gradient(:).^2);
            vBias = MatLab.sum(MatLab.multiply(beta_2, vBias) , MatLab.multiply((1 - beta_2), MatLab.dot(gradient, gradient)));


            for (int i = 0; i < theta.length; i++)
            {
                //Compute bias-corrected 1st moment estimate
                //mHat = m./(1 - beta1^nIter);
                mHat[i] = mBias[i] / (1 - Math.pow(beta_1, count));

                //Compute bias-corrected 2nd raw moment estimate
                //vHat = v./(1 - beta2^nIter);
                vHat[i] = vBias[i] / (1 - Math.pow(beta_2, count));
            }

            //Determine step to take at this iteration
            //vfStep = stepSize.*mHat./(sqrt(vHat) + epsilon);

            //save theta before update
            //theta_prev = theta;

            //Standard Adam
            for (int i = 0; i < theta.length; i++)
            {
                theta_new[i] = theta[i] - (learn_rate * mHat[i]) / (Math.sqrt(vHat[i]) + epsilon);
            }

            //Nesterov Momentum Variant:
            /*
            for (int i = 0; i < theta.length; i++)
            {
                theta_new[i] = theta[i] - ((learn_rate * mHat[i]) / (Math.sqrt(vHat[i]) + epsilon)) * ((beta_1 * mHat[i])+ ((1 - beta_1) * gradient[i]) / (1 - Math.pow(beta_1,i)));
            }
            */

            //Toroidal correction.
            theta = toro(theta_new, bounds);
            double fTheta = problem.f(theta);

            // is new theta better?
            if (fTheta < fBest)
            {
                // Yes: update the fitness
                fBest = fTheta;
                FT.add(count, fBest);
            }
            else
            {
                // No: restore and try again next iteration
                theta  = theta_saved;
            }

            count++;
        }

        //
        finalBest = theta; //save the final values
        FT.add(count, fBest); //add it to the txt file (row data)

        return FT; //return the fitness trend
    }

    // SPSA part.
    private double[] CalcCurrentApproxGradient(double c_param, double[] theta, int k, Problem problem) throws Exception
    {
        double alpha = 0.602;
        double gamma = 0.101;
        // double ak = Double.NaN;
        double ck = Double.NaN;
        double[] delta = new double[theta.length];
        double noise = 0.0d;

        // Gain sequences - same for all so no array needed.
        //ak = a_param / (Math.pow(((k+1) + A), alpha));  // step size
        ck = c_param / (Math.pow((k+1), gamma)); // perturbation magnitude

        // Step 2: generation of simultaneous perturbation vector
        for (int j = 0; j < theta.length; j++)
        {
            // Bernouilli +-1 distribution (Each value along the array has a 50% chance of being either a -1 or 1)
            delta[j] = 2.0d * Math.round(new Random().nextDouble()) - 1;
        }

        // Step 3: 2x function evaluations
        double[] theta_plus = MatLab.sum(theta , MatLab.multiply(ck ,delta));
        double[] theta_minus = MatLab.subtract(theta , MatLab.multiply(ck ,delta));

        // This assumes Zero Noise (for now) so loss function is just problem function to be minimised.
        double yplus = problem.f(theta_plus) + noise;
        double yminus = problem.f(theta_minus) + noise;

        // Step 4: gradient approximation (via the 2 observations of fitness above)
        double[] ghat = new double[theta.length];

        for (int j = 0; j < theta.length; j++)
        {
            ghat[j] = (yplus - yminus) / (2.0d * ck * delta[j]);
        }

        //
        return ghat;
    }

}




	/*
	// add some noise...
	private double noise_function(double[] input)
	{
		double[] output;
		double noise = 1.0;

		//just use zero loss to start?
		for(int i = 0 ; i < input.length; i++) {
			output[i] = input[i] * noise;
		}

		return output;
	}
	*/
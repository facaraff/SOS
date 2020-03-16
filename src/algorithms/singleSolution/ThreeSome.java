package algorithms.singleSolution;

import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.operators.MemesLibrary.intermediatePerturbation;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Three Stage Optimal Memetic Exploration 
 */
public class ThreeSome extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double globalAlpha = getParameter("p0").doubleValue(); //0.95
		double intermediateAlpha = 1-globalAlpha;
		int intermediateLSStepsFactor = getParameter("p1").intValue(); //4 
		double intermediateLSRadius = getParameter("p2").doubleValue(); //0.1
		int deepLSSteps = getParameter("p3").intValue(); //150;
		double deepLSRadius = getParameter("p4").doubleValue();//0.4;

		FTrend FT =new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// temp solution
		double[] solution = new double[problemDimension];
		double fSolution;
		// current best
		double[] best = new double[problemDimension];
		double fBest;

		if (initialSolution != null)
			solution = initialSolution;
		else
			solution = generateRandomSolution(bounds, problemDimension);		
		fSolution = problem.f(solution);

		best = solution;
		fBest = fSolution;
		FT.add(0, fBest);

		double globalCR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
		double intermediateCR = Math.pow(0.5, (1/(problemDimension*intermediateAlpha)));
		int intermediateLSSteps = intermediateLSStepsFactor * problemDimension;


		int nextStep = 0;

		int i = 1;
		while (i < maxEvaluations)
		{
			if (nextStep == 0)
			{
				while ((i < maxEvaluations) && (nextStep == 0))
				{


					solution = generateRandomSolution(bounds, problemDimension);
					solution = crossOverExp(best, solution, globalCR);
					fSolution = problem.f(solution);
					i++;


					// best update
					if (fSolution < fBest)
					{
						fBest = fSolution;
						best = solution;

						nextStep = 1;
					}
					if(i%problemDimension == 0)
						FT.add(i,fBest);
				}
			}
			else if (nextStep == 1)
			{
				boolean bestChanged = false;
				double j = 0;

				while ((j < intermediateLSSteps) && (i < maxEvaluations))
				{
					solution = intermediatePerturbation(bounds, best, intermediateLSRadius);
					solution = crossOverExp(best, solution, intermediateCR);
					solution = correct(solution, bounds);
					fSolution = problem.f(solution);
					//iCounter++;
					i++;

					// best update
					if (fSolution < fBest)
					{
						fBest = fSolution;
						best = solution;

						bestChanged = true;
					}

					j++;
					if(i%problemDimension == 0)
						FT.add(i,fBest);
				}

				if (!bestChanged)
					nextStep = 2;   // -> deep LS
				else
					nextStep = 1;   // -> intermediate LS
			}
			else if (nextStep == 2)
			{
				boolean bestChanged = false;

				double[] SR = new double[problemDimension];
				for (int k = 0; k < problemDimension; k++)
					SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;

				boolean improve = true;
				int j = 0;
				while ((j < deepLSSteps) && (i < maxEvaluations))
				{;

					double[] Xk = new double[problemDimension];
					double[] Xk_orig = new double[problemDimension];
					for (int k = 0; k < problemDimension; k++)
					{
						Xk[k] = best[k];
						Xk_orig[k] = best[k];
					}

					double fXk_orig = fBest;

					if (!improve)
					{
						for (int k = 0; k < problemDimension; k++)
							SR[k] = SR[k]/2;
					}

					improve = false;
					int k = 0;
					while ((k < problemDimension) && (i < maxEvaluations))
					{
						Xk[k] = Xk[k] - SR[k];
						Xk = correct(Xk, bounds);
						double fXk = problem.f(Xk);
						i++;

						// best update
						if (fXk < fBest)
						{
							fBest = fXk;
							for (int n = 0; n < problemDimension; n++)
								best[n] = Xk[n];
							bestChanged = true;
						}

						if (i < maxEvaluations)
						{
							if (fXk == fXk_orig)
							{
								for (int n = 0; n < problemDimension; n++)
									Xk[n] = Xk_orig[n];
							}
							else
							{
								if (fXk > fXk_orig)
								{
									Xk[k] = Xk_orig[k];
									Xk[k] = Xk[k] + 0.5*SR[k];
									Xk = correct(Xk, bounds);
									fXk = problem.f(Xk);
									i++;

									// best update
									if (fXk < fBest)
									{
										fBest = fXk;
										for (int n = 0; n < problemDimension; n++)
											best[n] = Xk[n];
										bestChanged = true;
									}

									if (fXk >= fXk_orig)
										Xk[k] = Xk_orig[k];
									else
										improve = true;
								}
								else
									improve = true;
							}
						}

						k++;
					}

					j++;
				}

				if (!bestChanged)
					nextStep = 0;   // -> global perturbation
				else
					nextStep = 1;   // -> intermediate LS

				if (i%problemDimension == 0)
					FT.add(i,fBest);
			}
		}

		finalBest = best;

		//Best currentBest=new Best(i,fBest);
		//currentBest.setExtra(new Double(gCounter));
		//currentBest.setExtra(new Double(iCounter));
		//currentBest.setExtra(new Double(dCounter));
		//bests.add(currentBest);
		FT.add(i,fBest);
		NumberFormat decimalFormat = DecimalFormat.getInstance();
		decimalFormat.setMaximumFractionDigits(2);
		decimalFormat.setMinimumFractionDigits(2);

		/*
		System.out.println(gCounter + " " + iCounter + " " + dCounter);
		System.out.println(gCounter + iCounter + dCounter);
		 */		
		//System.out.println("global rate = " + decimalFormat.format(((double)gCounter/maxEvaluations)*100) + " %");
		//System.out.println("intermediate rate = " + decimalFormat.format(((double)iCounter/maxEvaluations)*100) + " %");
		//System.out.println("deep rate = " + decimalFormat.format(((double)dCounter/maxEvaluations)*100) + " %");

		return FT;
	}
}
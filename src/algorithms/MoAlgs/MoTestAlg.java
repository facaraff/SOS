package algorithms.MoAlgs;

import static utils.MatLab.cloneArray;
import static utils.MatLab.indexMin;
import static utils.MatLab.min;
import static utils.MatLab.multiply;
import static utils.MatLab.subtract;
import static utils.MatLab.sum;
import static utils.MatLab.transpose;
import static utils.algorithms.Misc.Cov;
import static utils.algorithms.Misc.generateRandomSolution;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.algorithms.Corrections;
import utils.algorithms.Modality.TestModality;
/*
 * MoTest
 */
public class MoTestAlg extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
//		int populationSize = this.getParameter("p0").intValue();//50

		int modalityPopulation = this.getParameter("p0").intValue();
		
		int deepLSSteps = 150;//etParameter("p1").intValue();  //150;
		double deepLSRadius = 0.4;//getParameter("p2").doubleValue();//0.4;
		
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
//
//		double[][] population = new double[populationSize][problemDimension];
//		double[] fitnesses = new double[populationSize];
//
		double[] best = new double[problemDimension];
		double fBest = Double.NaN;
		
		int i = 0;
		
		double[] sol = generateRandomSolution(bounds,problemDimension);
		double solFitness = problem.f(sol);
		best = cloneArray(sol); fBest =solFitness;
		i++; FT.add(i, solFitness);
		
		//***MODALITY STUFF STARTS****
		
		//Evaluate function modality
		int modality = -1;
		int modalityEvaluations = maxEvaluations/3; //33%
		modalityEvaluations = min(modalityEvaluations, maxEvaluations-i);

		TestModality TM = new TestModality();
		TM.basinEstimate(problem, modalityPopulation, 2, modalityEvaluations);
		i = i + modalityEvaluations;
		modality = TM.getModality();
		System.out.println("modality = "+modality);
		
		double[][] centroids = TM.getClusterCentroids();
		
		//System.out.println("Cluster = "+centroids.length);
		
//		for (int ii = 0; ii < centroids.length; ii++)
//		{
//			for (int j = 0; j < centroids[0].length; j++)
//				System.out.print(centroids[ii][j]+" ");
//			System.out.println(" ");
//		}
		
		double[] centroidFtness = new double[centroids.length];
		for (int k = 0; k < centroids.length && i< maxEvaluations; k++)
		{
			centroidFtness[k] = problem.f(centroids[k]);
			i++;
				
		}
		
		int index = indexMin(centroidFtness);
		
		best = cloneArray(centroids[index]);
		fBest = centroidFtness[index];
		FT.add(i,fBest);
 
		//Cluster<EuclideanDoublePoint> bestCluster = TM.getCluster(index);
		
		double[][] samples = TM.getArrayCluster(index);
		System.out.println("samplesSize="+samples.length);
		
		System.out.println("samples");
		for(int m=0; m<samples.length; m++)
		{
			for(int q=0; q<samples[0].length; q++)
				System.out.print(samples[m][q]+"\t");
			System.out.println();
		}
		
		//ROTATED S BEGINS
		
		initialSolution = cloneArray(best);
		initialFitness = fBest;
		
		
		
		double[] SR = new double[problemDimension];
//		for (int k = 0; k < problemDimension; k++)
//			SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;
		
		while (i < maxEvaluations)
		{
		
			for (int k = 0; k < problemDimension; k++)
				SR[k] = (bounds[k][1] - bounds[k][0]) * deepLSRadius;
			
			boolean improve = true;
			int j = 0;
			
//			double[] samplesFitnesses = new double[samplesNr];
			
			
//			double[][] cov = Cov(samples);
//			for(int m=0; m<cov.length; m++)
//			{
//				for(int q=0; q<cov[0].length; q++)
//					System.out.println(cov[m][q]);
//				System.out.println();
//			}
//
			//generate the P matrix and free memory
			EigenDecomposition E =  new EigenDecomposition(new Array2DRowRealMatrix(Cov(samples)));

			double[][] P = E.getV().getData();
			E = null;
			

			//scale P columns with the corresponding perturbation radius
			double[][] R = scale(P,SR);
			
			//transpose R so that rows can be selected to act as perturbation vectors
			R = transpose(R);

			//Execute S along rotated axes
			while ((j < deepLSSteps) && (i < maxEvaluations))
			{
				double[] Xk = new double[problemDimension];
				double[] Xk_orig = new double[problemDimension];
				for (int k = 0; k < problemDimension; k++)
				{
					Xk[k] = best[k];//temp[k];
					Xk_orig[k] = best[k];//temp[k];
				}

				if (!improve) //@fabio this can be done on each dimension
				{
					for(int k=0;k<problemDimension;k++)
						half(R,k);
				}
				
				improve = false;
				int k = 0;
				while ((k < problemDimension) && (i < maxEvaluations))
				{
					Xk = subtract(Xk,R[k]);
					Xk = Corrections.toro(Xk, bounds);
					double fXk = problem.f(Xk);
					i++;
					
					// FT update
					if (fXk < fBest) //< or <= ?????????
					{
						fBest = fXk;
						best = cloneArray(Xk);
						Xk_orig = cloneArray(Xk);
						FT.add(i,fBest);
						improve = true;
					}
					else if(i<maxEvaluations)
					{
						Xk = cloneArray(Xk_orig);
						Xk = sum(Xk,multiply(0.5, R[k]));
						Xk = Corrections.toro(Xk, bounds);
						fXk = problem.f(Xk);
						i++;
						
						// FT update
						if (fXk < fBest)
						{
							fBest = fXk;
							best = cloneArray(Xk);
							Xk_orig = cloneArray(Xk);
							improve = true;
							FT.add(i,fBest);
						}
						else
							Xk = cloneArray(Xk_orig);
					}
					
					k++;
				}
				
				j++;
			}
			
		}




		finalBest = best;
		FT.add(i, fBest);
		return FT;
	}
	
	
	//helpers methods
	
	protected double[][] scale(double[][] P, double[] SR)
	{
		double[][] R = new double[SR.length][SR.length];
		
		for(int c=0;c<SR.length;c++)
			for(int r=0;r<SR.length; r++)
				R[r][c] = P[r][c]*SR[c];
		
		return R;
		
	}
	
	protected void half(double[][] PT, int k)
	{
		for(int c=0;c<PT.length;c++)
			PT[k][c] = PT[k][c]/2;
	}
	
	
}
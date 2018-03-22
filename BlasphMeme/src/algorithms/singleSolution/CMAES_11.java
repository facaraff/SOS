package algorithms.singleSolution;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import static utils.MatLab.norm2;
import static utils.MatLab.columnXrow;
import static utils.MatLab.eye;
import static utils.MatLab.multiply;
import static utils.MatLab.sum;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Covariance Matrix Adaptation Evolutionary Strategy (1+1)
 */
public class CMAES_11 extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double p_target_succ = getParameter("p0").doubleValue(); // 2/11
		double c_p = getParameter("p1").doubleValue(); // 1/12
		double p_thresh = getParameter("p2").doubleValue();// 0.44
		double sigma0 = getParameter("p3").doubleValue();// 1 --> problem dependent!!

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		double d = 1+problemDimension/2;
		double c_cov = 2/(Math.pow(problemDimension,2)+6);
		double c_a = Math.sqrt(1-c_cov);
		//double c_c = 2/(problemDimension+2); 
		double sigma=sigma0;
		double p_succ_sign = p_target_succ;
		int lambda_succ;
		//offspring
		double[] x_offspring; //= new double[problemDimension];
		double f_offspring;

		//compute and evaluate initial solution
		double[] x_parent = generateRandomSolution(bounds, problemDimension);
		double f_parent = problem.f(x_parent);
		FT.add(0, f_parent);

		double[] z;
		double[][] A = eye(problemDimension);
		double[] Az;
		int i = 1;
		int improvements=0;
		while (i < maxEvaluations)
		{	
			z=newZ(problemDimension);
			Az = multiply(A,z);
			x_offspring = sum(x_parent,multiply(sigma,Az));
			//System.out.println(x_offspring[0]);
			x_offspring = toro(x_offspring, bounds);
			f_offspring=problem.f(x_offspring);

			if(f_offspring <= f_parent)
				lambda_succ=1;
			else
				lambda_succ=0;
			//update step size procedure
			p_succ_sign = (1-c_p)*p_succ_sign + c_p*lambda_succ;
			sigma = sigma*Math.exp( (1/d)*(p_succ_sign-(p_target_succ/(1-p_target_succ))*(1-p_succ_sign)) );
			if(f_offspring <= f_parent)
			{
				f_parent=f_offspring;
				for(int dim=0;dim<problemDimension;dim++)
					x_parent[dim]=x_offspring[dim];
				improvements++;
				if(improvements%10==0)
					FT.add(i, f_parent);
				//update cholesky
				if(p_succ_sign < p_thresh)
					A=updateCholesky(A,z,c_a);
			}
			i++;
		}

		finalBest = x_parent;
				
		FT.add(i, f_parent);

		return FT;
	}

	public double[] newZ(int dimension)
	{
		double[] x = new double[dimension];
		for(int i=0;i<dimension;i++)
			x[i] = RandUtils.gaussian(0, 1);
		return x;
	}

	public  double[][] updateCholesky(double[][] A, double[] z, double c_a)
	{   
		//scalars factors
		double z2 = norm2(z); z2=z2*z2; 
		double ca2 = c_a*c_a;
		double factor = (c_a / z2)*(Math.sqrt(1+((1-ca2)*z2)/ca2)-1); 
		//System.out.println(factor);
		//matrix A*z*z'
		double[][] temp = columnXrow(multiply(A,z),z); 
		return sum( multiply(c_a,A) , multiply(factor,temp) );
	}
}
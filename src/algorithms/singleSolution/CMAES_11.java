package algorithms.singleSolution;

/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.

**\



import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.correct;
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



import static utils.algorithms.Misc.generateRandomSolution;
import static utils.MatLab.norm2;
import static utils.MatLab.columnXrow;
import static utils.MatLab.eye;
import static utils.MatLab.multiply;
import static utils.MatLab.sum;

import utils.random.RandUtils;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

public class CMAES_11 extends Algorithm
{   
	
	private boolean saveMatrix = false;
	private double[][] matrix = null;
	public void setSavematrix(boolean saveMatrix){this.saveMatrix= saveMatrix;}
	public void setMatrix(double[][] matrix) {this.matrix = matrix;}
	public double[][] getMatrix() {return this.matrix;}
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

		
		
		int i=0;
		double[] x_parent = null;
		double f_parent = Double.NaN;
		if (initialSolution != null)
		{
			
			x_parent = initialSolution;
			f_parent = initialFitness;
		}
		else
		{
			//compute and evaluate initial solution
			x_parent = generateRandomSolution(bounds, problemDimension);
			f_parent = problem.f(x_parent);
			i++;
		}
		FT.add(i, f_parent);

		double[] z;
		double[][] A = eye(problemDimension);
		double[] Az;
		
		int improvements=0;
		while (i < maxEvaluations)
		{	
			z=newZ(problemDimension);
			Az = multiply(A,z);
			x_offspring = sum(x_parent,multiply(sigma,Az));
			//System.out.println(x_offspring[0]);
			x_offspring = correct(x_offspring, bounds);
			f_offspring=problem.f(x_offspring);

			if(f_offspring <= f_parent)
				lambda_succ=1;
			else
				lambda_succ=0;
			//update step size procedure
			p_succ_sign = (1-c_p)*p_succ_sign + c_p*lambda_succ;
			sigma = sigma*Math.exp( (1/d)*(p_succ_sign-(p_target_succ/(1-p_target_succ))*(1-p_succ_sign)));
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
				
//controllato se il determinante fosse unitario, in effetti sto accrocchio funziona				
//				for(int c=0; c<A.length;c++)
//				{
//					for(int b=0; b<A.length;b++)
//						System.out.print(A[c][b]+"\t");
//					System.out.println();
//				}
//				System.out.println();
			}

				
			i++;
		}
		if(saveMatrix)
				this.matrix = A;
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
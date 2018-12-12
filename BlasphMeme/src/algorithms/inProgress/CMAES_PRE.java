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
*/
package algorithms.inProgress;

import static utils.algorithms.Misc.Cov;
import static utils.algorithms.Misc.AVGDesignVar;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;


import static utils.MatLab.min;
import static utils.MatLab.abs;
import static utils.MatLab.multiply;

import utils.algorithms.cmaes.CMAEvolutionStrategy;
import interfaces.Algorithm;
import interfaces.Problem;

//import static utils.MatLab.transpose;
import static utils.RunAndStore.FTrend;
import static utils.RunAndStore.slash;

/*
 * Covariance Matrix Adaptation Evolutionary Strategy 
 */
public class CMAES_PRE extends Algorithm
{
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		double budgetFactor = getParameter("p0"); 
		String problemName = problem.getClass().getSimpleName();
		String name = "covariance-"+budgetFactor+"-"+problemName+problemDimension+"D"+".txt";
		String nameIntCov = "CMAES-INTERNAL-COV-"+budgetFactor+"-"+problemName+problemDimension+"D"+".txt";
		String nameIntNomCov = "CMAES-INTERNAL-NORMALISED-COV-"+budgetFactor+"-"+problemName+problemDimension+"D"+".txt";
		String nameP = "P-"+budgetFactor+"-"+problemName+problemDimension+"D"+".txt";
		String mean = "mean-"+budgetFactor+"-"+problemName+problemDimension+"D"+".txt";
		String spop = "sampledPopulation-"+budgetFactor+"-"+problemName+problemDimension+"D"+".txt";
		
		double[] best = new double[problemDimension];
		if (initialSolution != null)
			best = initialSolution;
		else
			best = generateRandomSolution(bounds, problemDimension);
		double fBest = Double.NaN;

		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		//cma.parameters.setPopulationSize(10);
		cma.setRepairMatrix(true);				// repair ill-conditioned matrix
		cma.setDimension(problemDimension);		// overwrite some loaded properties
		cma.setInitialX(best);					// in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.2);	// also a mandatory setting 
		cma.options.verbosity = -1;
		cma.options.writeDisplayToFile = -1;
		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();
		
		// iteration loop
		int j = 0; 
		int budget = (int)(budgetFactor*maxEvaluations);
		while (j < budget)
		{
			// get a new population of solutions
			double[][] pop = cma.samplePopulation();
			
			for(int i = 0; i < pop.length && j < maxEvaluations; ++i)
			{ 
				// saturate solution inside bounds 
				pop[i] = toro(pop[i], bounds);
				
				// compute fitness/objective value	
				fitness[i] = problem.f(pop[i]);
				
				// save best
				if (j == 0 || fitness[i] < fBest)
				{
					fBest = fitness[i];
					for (int n = 0; n < problemDimension; n++)
						best[n] = pop[i][n];
					FT.add(j, fBest);
				}
				
				j++;
			}

			// pass fitness array to update search distribution
			cma.updateDistribution(fitness);
		}
		
		double[][] pop = cma.samplePopulation();
		double[][] cov = Cov(pop);
		double[] myMu = AVGDesignVar(pop);
		
		double small = min(abs(cov));
		
		double[][] PC = multiply(1/small,cov);
	
		
		
		//generate the P matrix and free memory
		EigenDecomposition E =  new EigenDecomposition(new Array2DRowRealMatrix(PC));
		double[][] P = E.getV().getData();
		
		double[] mu = cma.getMeanX();
		
//		double[] eigen1 = E.getEigenvector(0).toArray(); 
//		for(int n=0;n<eigen1.length;n++)
//			System.out.println(eigen1[n]+" ");
		
		
		double[][] InternalC = cma.getCovMat();
		
		
		for(int i=1; i<problemDimension; i++) 
			for(j=0; j<i; j++)
				InternalC[j][i] = InternalC[i][j];
		
		
		
		double smallCmaes = min(abs(InternalC));//if we have a 0 then it would be a problem when performing the division!!!! I never go t0 though
		double[][] InternalProcessedC = multiply(1/smallCmaes,InternalC);
		
		
		
		cma = null;		
		
		
		String C = "";
		String PP = "";
		String PPC = "";
		String MU = "";
		String myMU = "";
		String POP = "";
		String cmaesC = "";
		String cmaesProcessedC = "";
		for(int i=0; i<problemDimension; i++)
		{
			for(int k=0; k<problemDimension; k++)
			{
				C += cov[i][k]+"\t";
				cmaesProcessedC += InternalProcessedC[i][k]+"\t";
				cmaesC += InternalC[i][k]+"\t";
				PPC += PC[i][k]+"\t";
				PP += P[i][k]+"\t";
			}
			MU+=mu[i]+" ";
			myMU+=myMu[i]+" ";
			C +="\n"; 
			cmaesC+="\n";
			PP+="\n";
			cmaesProcessedC+="\n";
		}
		
		for(int i=0; i<pop.length; i++)
		{
			for(int k=0; k<problemDimension; k++)
				POP += pop[i][k]+"\t";
			POP+="\n";
		}
		
		
		MU="cmaes mu = "+MU+"\n my mu = "+myMU;
		
		File file = new File("." +slash()+name);
		// if file doesn't exists, then create it
		if (!file.exists()) 
			file.createNewFile();

//				C = cma.getDataC();
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(C);
		bw.close();
		
		
		file = new File("." +slash()+nameIntCov);
		// if file doesn't exists, then create it
		if (!file.exists()) 
			file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(cmaesC);
		bw.close();
		
		file = new File("." +slash()+nameIntNomCov);
		// if file doesn't exists, then create it
		if (!file.exists()) 
			file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(cmaesProcessedC);
		bw.close();
		
		
		file = new File("." +slash()+"processed-"+name);
		// if file doesn't exists, then create it
		if (!file.exists()) 
			file.createNewFile();

//				C = cma.getDataC();
		
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(PPC);
		bw.close();
		


		file = new File("." +slash()+nameP);
		// if file doesn't exists, then create it
		if (!file.exists()) 
			file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(PP);
		bw.close();

		file = new File("." +slash()+mean);
		// if file doesn't exists, then create it
		if (!file.exists()) 
			file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(MU);
		bw.close();

		
		file = new File("." +slash()+spop);
		// if file doesn't exists, then create it
		if (!file.exists()) 
			file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(POP);
		bw.close();
		
		finalBest = best;

		FT.add(j, fBest);
		
		return FT;
	}
	

	
	
	
}
package algorithms.AlgorithmicBehaviour.ISB_SingleSolutions;

import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.cloneSolution;
import static utils.algorithms.Misc.fillAWithB;

import utils.MatLab;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
import utils.algorithms.Counter;

/**
 * A Nelder-Mead simplex search.
 */
public final class NelderMeadBias extends AlgorithmBias {

	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double alpha = getParameter("p0").doubleValue();	// 1
		double beta  = getParameter("p1").doubleValue();	// 0.5
		double gamma = getParameter("p2").doubleValue();	// 2
		double delta = getParameter("p3").doubleValue();	// 0.5
		 // t --> toroidal   s-->saturation   d--->discard
	
		
		FTrend FT = new FTrend();
		
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		int populationSize = problemDimension+1;
		double[][] simplex = new double[populationSize][problemDimension]; double[] prevSimplex = new double[problemDimension];
		double[] fSimplex = new double[populationSize];
		
		double[][] iSimplex = new double[populationSize][2];

		double[] best = new double[problemDimension];
		double fBest = Double.POSITIVE_INFINITY;
        int[] ids = new int[populationSize];
        int killed = -1;

		String FullName = getFullName("NMA"+this.correction+"p"+populationSize,problem); 
		Counter PRGCounter = new Counter(0);
		createFile(FullName);
        
        String line = new String();
		
		
		int newID = 0;
		//NMA{correction}p31D30f0
		writeHeader("pop (verteces: n+1) "+populationSize+" alpha "+alpha+" beta "+beta+" gamma "+gamma+" delta "+delta, problem);
		//bw.close();
		
		int i = 0;

		// evaluate the function at each point in simplex,
		for (int j = 0; j < (problemDimension+1); j++)
		{
			if (initialSolution != null && j == 0)
			{
				simplex[0] = initialSolution;
				fSimplex[0]=initialFitness;
			}
			else
			{
				simplex[j] = generateRandomSolution(problem.getBounds(), problemDimension);
				fSimplex[j] = problem.f(simplex[j]);
			}
			i++;
			iSimplex[j][0] = fSimplex[j];
			iSimplex[j][1] = j;
			newID++;
			ids[j] = newID;
			
			if (j == 0 || fSimplex[j] < fBest)
			{
				fBest = fSimplex[j];
				for (int n = 0; n < problemDimension; n++)
					best[n] = simplex[j][n];
				FT.add(j, fBest);
				
				line =""+ids[j]+" "+ids[j]+" -1 "+" -1 "+formatter(fSimplex[j])+" "+i+" "+killed;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(simplex[j][n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				killed = ids[j];
				
			}
		}

		double[] reflect = cloneSolution(best); double[] prevReflect = cloneSolution(best);
		double[] expand = cloneSolution(best); double[] prevExpand = cloneSolution(best);
		double[] contract = cloneSolution(best); double[] prevContract = cloneSolution(best);
		double reflectVal = Double.NaN;
		double expandVal = Double.NaN;
		double contractVal = Double.NaN;
		
		double[] mean = new double[problemDimension];
		
		int l;
		int h;
		int s;
		double total;
		
		
		
		this.numberOfCorrections = 0;
		
		while (i < maxEvaluations)
		{
			for (int k = 0; k < (problemDimension+1); k++)
			{
				iSimplex[k][0] = fSimplex[k];
				iSimplex[k][1] = k;
			}
			
			// sort the points and get the best, worst, and next to worst
			MatLab.sortRows(iSimplex, 0);
			
			l  = (int)iSimplex[0][1];					// best
			h  = (int)iSimplex[problemDimension][1];	// worst
			s = (int)iSimplex[problemDimension-1][1];	// second worst

			// sum all but the worst point
			for (int j = 0; j < problemDimension; j++)
			{
				total = 0;
				for (int k = 0; k < (problemDimension+1); k++)
				{
					if (k != h)
						total += simplex[k][j];
				}

				mean[j] = total/problemDimension;
			}

			fillAWithB(prevReflect,reflect);
			
			// reflect
			for (int j = 0; j < problemDimension; j++)
				reflect[j] = mean[j] + alpha*(mean[j] - simplex[h][j]);
			//reflect = saturateToro(reflect, bounds);
			
			
			reflect = correct(reflect,prevReflect,bounds, PRGCounter);
			reflectVal = problem.f(reflect);
			i++;
			
			
			if (reflectVal < fBest)  
			{
				fBest = reflectVal;
				for (int j = 0; j < problemDimension; j++)
					best[j] = reflect[j];
				FT.add(i, fBest);
			}
			
			if (i >= maxEvaluations)
				break;

			
			if (reflectVal < iSimplex[l][0])
			{
				
				// expand
				fillAWithB(prevExpand,expand);
				
				for (int j = 0; j < problemDimension; j++)
					expand[j] = mean[j] + gamma*(reflect[j] - mean[j]);
				
				//expand = saturateToro(expand, bounds);
				
				reflect = correct(expand,prevExpand,bounds, PRGCounter);
				expandVal = problem.f(expand);
				i++;
				
				if (expandVal < fBest)
				{
					fBest = expandVal;
					for (int j = 0; j < problemDimension; j++)
						best[j] = expand[j];
					FT.add(i, fBest);
				}
				
				if (i >= maxEvaluations)
					break;
				
				// check if reflect or expand is better
				if (expandVal < reflectVal) 
				{
					for (int j = 0; j < problemDimension; j++)
						simplex[h][j] = expand[j];
					fSimplex[h] = expandVal;
					
					newID++;
					killed = ids[h];
					ids[h] = newID; //REPLACE THE WORST
					
					for (int j = 0; j < problemDimension; j++)
						simplex[h][j] = contract[j];
					fSimplex[h] = contractVal;
					   
					///////////////I HAD TO ADD THIS TO UPDATE THE INDEX FOR ANNA'S NOTATION (remove otherwise because it really slows down)
					for (int n = 0; n < (problemDimension+1); n++)
					{
						iSimplex[n][0] = fSimplex[n];
						iSimplex[n][1] = n;
					}
					MatLab.sortRows(iSimplex, 0);
					l  = (int)iSimplex[0][1];					// best
					h  = (int)iSimplex[problemDimension][1];	// worst
					s = (int)iSimplex[problemDimension-1][1];	// second worst
					///////////////////
					line =""+newID+" "+ids[l]+" "+ids[s]+" "+ids[h]+" "+formatter(contractVal)+" "+i+" "+killed;
					for(int n = 0; n < problemDimension; n++)
						line+=" "+formatter(simplex[h][n]);
					line+="\n";
					bw.write(line);
					line = null;
					line = new String();
					
				}
				else //DONE
				{
					newID++;
					killed = ids[h];
					ids[h] = newID; //REPLACE THE WORST
					
					for (int j = 0; j < problemDimension; j++)
						simplex[h][j] = reflect[j];
					fSimplex[h] = reflectVal;
					
					///////////////I HAD TO ADD THIS TO UPDATE THE INDEX FOR ANNA'S NOTATION (remove otherwise because it really slows down)
					for (int n = 0; n < (problemDimension+1); n++)
					{
						iSimplex[n][0] = fSimplex[n];
						iSimplex[n][1] = n;
					}
					MatLab.sortRows(iSimplex, 0);
					l  = (int)iSimplex[0][1];					// best
					h  = (int)iSimplex[problemDimension][1];	// worst
					s = (int)iSimplex[problemDimension-1][1];	// second worst
					///////////////////
					line =""+newID+" "+ids[l]+" "+ids[s]+" "+ids[h]+" "+formatter(reflectVal)+" "+i+" "+killed;
					for(int n = 0; n < problemDimension; n++)
						line+=" "+formatter(simplex[h][n]);
					line+="\n";
					bw.write(line);
					line = null;
					line = new String();
				}
			}
			else if (reflectVal >= iSimplex[l][0] && reflectVal < iSimplex[s][0]) //DONE
			{
				newID++;
				killed = ids[h];
				ids[h] = newID; //REPLACE THE WORST
				
				// accept reflect point
				for (int j = 0; j < problemDimension; j++)
					simplex[h][j] = reflect[j];
				fSimplex[h] = reflectVal;
				
				///////////////I HAD TO ADD THIS TO UPDATE THE INDEX FOR ANNA'S NOTATION (remove otherwise because it really slows down)
				for (int n = 0; n < (problemDimension+1); n++)
				{
					iSimplex[n][0] = fSimplex[n];
					iSimplex[n][1] = n;
				}
				MatLab.sortRows(iSimplex, 0);
				l  = (int)iSimplex[0][1];					// best
				h  = (int)iSimplex[problemDimension][1];	// worst
				s = (int)iSimplex[problemDimension-1][1];	// second worst
				///////////////////
				line =""+newID+" "+ids[l]+" "+ids[s]+" "+ids[h]+" "+formatter(reflectVal)+" "+i+" "+killed;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(simplex[h][n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
			}
			else if (reflectVal >= iSimplex[s][0] && reflectVal < iSimplex[h][0]) 
			{
				// contract outside
				fillAWithB(prevContract,contract);
				for (int j = 0; j < problemDimension; j++)
					contract[j] = mean[j] + beta*(reflect[j] - mean[j]);
				
				
				contract = correct(contract,prevContract,bounds,PRGCounter);
				contractVal = problem.f(contract);
				i++;

				if (contractVal < fBest)
				{
					fBest = contractVal;
					for (int j = 0; j < problemDimension; j++)
						best[j] = contract[j];
					FT.add(i, fBest);
				}

				if (i >= maxEvaluations)
					break;

				if (contractVal <= reflectVal)
				{
					newID++;
					killed = ids[h];
					ids[h] = newID; //REPLACE THE WORST
					
					for (int j = 0; j < problemDimension; j++)
						simplex[h][j] = contract[j];
					fSimplex[h] = contractVal;
					
					///////////////I HAD TO ADD THIS TO UPDATE THE INDEX FOR ANNA'S NOTATION (remove otherwise because it really slows down)
					for (int n = 0; n < (problemDimension+1); n++)
					{
						iSimplex[n][0] = fSimplex[n];
						iSimplex[n][1] = n;
					}
					MatLab.sortRows(iSimplex, 0);
					l  = (int)iSimplex[0][1];					// best
					h  = (int)iSimplex[problemDimension][1];	// worst
					s = (int)iSimplex[problemDimension-1][1];	// second worst
					///////////////////
					line =""+newID+" "+ids[l]+" "+ids[s]+" "+ids[h]+" "+formatter(contractVal)+" "+i+" "+killed;
					for(int n = 0; n < problemDimension; n++)
						line+=" "+formatter(simplex[h][n]);
					line+="\n";
					bw.write(line);
					line = null;
					line = new String();
				}
				else///DA QUIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
				{
					// shrink
					for (int k = 0; k < (problemDimension+1) && i < maxEvaluations; k++)
					{
						if (k != l)
						{
							killed = ids[k];
							// This should never generate infeasible points as it shrinks! (Fabio)
							fillAWithB(prevSimplex, simplex[k]);
							for (int j = 0; j < problemDimension; j++)
								simplex[k][j] = simplex[l][j]+delta*(simplex[k][j]-simplex[l][j]);
							//simplex[k] = saturateToro(simplex[k], bounds);
								
								
							simplex[k] = correct(simplex[k], prevSimplex,bounds, PRGCounter);
							fSimplex[k] = problem.f(simplex[k]);
								
							i++;
							newID++;
							ids[k] = newID; 
								
							///////////////I HAD TO ADD THIS TO UPDATE THE INDEX FOR ANNA'S NOTATION (remove otherwise because it really slows down)
							for (int n = 0; n < (problemDimension+1); n++)
							{
								iSimplex[n][0] = fSimplex[n];
								iSimplex[n][1] = n;
							}
							MatLab.sortRows(iSimplex, 0);
							l  = (int)iSimplex[0][1];					// best
							h  = (int)iSimplex[problemDimension][1];	// worst
							s = (int)iSimplex[problemDimension-1][1];	// second worst
							///////////////////
								
							line =""+newID+" "+ids[l]+" "+ids[s]+" "+ids[h]+" "+formatter(fSimplex[k])+" "+i+" "+killed;
							for(int n = 0; n < problemDimension; n++)
								line+=" "+formatter(simplex[h][n]);
							line+="\n";
							bw.write(line);
							line = null;
							line = new String();
								
								
							
							
							if (fSimplex[k] < fBest)
							{
								fBest = fSimplex[k];
								for (int j = 0; j < problemDimension; j++)
									best[j] = simplex[k][j];
								FT.add(i, fBest);
							}
						}
					}
				}
			}
			else if (reflectVal >= iSimplex[h][0])//SERPE
			{
				// contract inisde
				fillAWithB(prevContract,contract);
				for (int j = 0; j < problemDimension; j++)
					contract[j] = mean[j] + beta*(simplex[h][j] - mean[j]);
				
				contract = correct(contract, prevContract, bounds, PRGCounter);
				
				contractVal = problem.f(contract);
				i++;

				if (contractVal < fBest)
				{
					fBest = contractVal;
					for (int j = 0; j < problemDimension; j++)
						best[j] = contract[j];
					FT.add(i, fBest);
				}

				if (i >= maxEvaluations)
					break;

				if (contractVal <= iSimplex[h][0])//WORST
				{
					newID++;
					killed = ids[h];
					ids[h] = newID; //REPLACE THE WORST
					
					for (int j = 0; j < problemDimension; j++)
						simplex[h][j] = contract[j];
					fSimplex[h] = contractVal;                                                                        //DONE
					
					line =""+newID+" "+ids[l]+" "+ids[s]+" "+ids[h]+" "+formatter(fSimplex[h])+" "+i+" "+killed;
					for(int n = 0; n < problemDimension; n++)
						line+=" "+formatter(simplex[h][n]);
					line+="\n";
					bw.write(line);
					line = null;
					line = new String();
					
				}
				else//DONE
				{
					// shrink
					for (int k = 0; k < (problemDimension+1) && i < maxEvaluations; k++)
					{
						if (k != l)
						{
							killed = ids[k];
							fillAWithB(prevSimplex,simplex[k]);
							for (int j = 0; j < problemDimension; j++)
								simplex[k][j] = simplex[l][j]+delta*(simplex[k][j]-simplex[l][j]);
							
							simplex[k] = correct(simplex[k], prevSimplex, bounds,PRGCounter);
							
							fSimplex[k] = problem.f(simplex[k]);
							
							i++;
							newID++;
							ids[k] = newID; 
							
							///////////////I HAD TO ADD THIS TO UPDATE THE INDEX FOR ANNA'S NOTATION (remove otherwise because it really slows down)
							for (int n = 0; n < (problemDimension+1); n++)
							{
								iSimplex[n][0] = fSimplex[n];
								iSimplex[n][1] = n;
							}
							MatLab.sortRows(iSimplex, 0);
							l  = (int)iSimplex[0][1];					// best
							h  = (int)iSimplex[problemDimension][1];	// worst
							s = (int)iSimplex[problemDimension-1][1];	// second worst
							///////////////////
							
							
							line =""+newID+" "+ids[l]+" "+ids[s]+" "+ids[h]+" "+formatter(fSimplex[k])+" "+i+" "+killed;
							for(int n = 0; n < problemDimension; n++)
								line+=" "+formatter(simplex[h][n]);
							line+="\n";
							bw.write(line);
							line = null;
							line = new String();
							
							if (fSimplex[k] < fBest)
							{
								fBest = fSimplex[k];
								for (int j = 0; j < problemDimension; j++)
									best[j] = simplex[k][j];
								FT.add(i, fBest);
							}
						}
					}
				}
			}
		}
		
		finalBest = best;
		FT.add(i, fBest);
		
		bw.close();
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(), "correctionsSingleSol");
		
		return FT;
	}


	public int[] getIndices(int popSize)
	{
		int[] indices = new int[popSize];
		for(int n=0; n<popSize; n++)
			indices[n] = n;
		return indices;
	}


}	
	
	
	
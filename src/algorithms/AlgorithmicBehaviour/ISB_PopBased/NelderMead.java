package algorithms.AlgorithmicBehaviour.ISB_PopBased;

import static utils.algorithms.Misc.generateRandomSolution;


import java.text.DecimalFormat;

import utils.random.RandUtils;

import interfaces.AlgorithmBias;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static utils.algorithms.Corrections.toro;


import java.util.Arrays;


import utils.MatLab;


/**
 * A Nelder-Mead simplex search.
 */
public final class NelderMead extends AlgorithmBias {

	static String Dir = "/home/facaraff/Desktop/KONODATA/NelderMead/";
	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");

	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		double alpha = getParameter("p0").doubleValue();	// 1
		double beta  = getParameter("p1").doubleValue();	// 0.5
		double gamma = getParameter("p2").doubleValue();	// 2
		double delta = getParameter("p3").doubleValue();	// 0.5
		char correctionStrategy = 'e';  // t --> toro   s-->saturation e---> penalise  d--->discard
	
		
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		
		int populationSize = problemDimension+1;
		double[][] simplex = new double[populationSize][problemDimension];
		double[] fSimplex = new double[populationSize];
		
		double[][] iSimplex = new double[populationSize][2];

		double[] best = new double[problemDimension];
		double fBest = Double.POSITIVE_INFINITY;
        int[] ids = new int[populationSize];
        int killed = -1;
			
		
        String fileName = "NMA"+correctionStrategy+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1);
		File file = new File(Dir+fileName+".txt");
		//File file = new File(Dir+"/DEroe"+"p"+populationSize+"D"+problem.getDimension()+"f0-"+(run+1)+".txt");
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtils.setSeed(seed);
		String line = "# function 0 dim "+problemDimension+" pop (verteces: n+1) "+populationSize+" alpha "+alpha+" beta "+beta+" gamma "+gamma+" delta "+delta+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
		bw.write(line);
		line = null;
		line = new String();
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
			
			
//			newID++;
//			ids[j] = newID;
//
//			line =""+ids[j]+" -1 "+"-1 "+bestID+" "+formatter(fSimplex[j])+" "+i+" -1";
//			for(int n = 0; n < problemDimension; n++)
//				line+=" "+formatter(simplex[j][n]);
//			line+="\n";
//			bw.write(line);
//			line = null;
//			line = new String();
			
		}

		double[] reflect = new double[problemDimension];
		double[] expand = new double[problemDimension];
		double[] contract = new double[problemDimension];
		double reflectVal = Double.NaN;
		double expandVal = Double.NaN;
		double contractVal = Double.NaN;
		
		double[] mean = new double[problemDimension];
		
		int l;
		int h;
		int s;
		double total;
		
		
		int infCounter = 0;
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

			// reflect
			for (int j = 0; j < problemDimension; j++)
				reflect[j] = mean[j] + alpha*(mean[j] - simplex[h][j]);
			
			//reflect = toro(reflect, bounds);
			double[] output = new double[problemDimension];
			if(correctionStrategy == 't')
			{
				//System.out.println("toro");
				output = toro(reflect, bounds);
				
				if(!Arrays.equals(output, reflect))
				{
					reflect = output;
					infCounter++;
				}
				reflectVal = problem.f(reflect);
			}
			else if(correctionStrategy== 's')
			{
				//System.out.println("SAT");
				output = saturation(reflect, bounds);
				
				if(!Arrays.equals(output, reflect))
				{
					reflect = output;
					infCounter++;
				}
				reflectVal = problem.f(reflect);
				
			}
			else if(correctionStrategy== 'e')
			{
				output = saturation(reflect, bounds);
				if(!Arrays.equals(output, reflect))
				{
					infCounter++;
					reflectVal = 2;
				}
			}
			else
				System.out.println("No bounds handling shceme seleceted");
			
//			if(!Arrays.equals(output, reflect))
//			{
//				reflect = output;
//				infCounter++;
//			}
//			reflectVal = problem.f(reflect);
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
				for (int j = 0; j < problemDimension; j++)
					expand[j] = mean[j] + gamma*(reflect[j] - mean[j]);
				
				//expand = toro(expand, bounds);
				output = new double[problemDimension];
				if(correctionStrategy == 't')
				{
					//System.out.println("toro");
					output = toro(expand, bounds);
					
					if(!Arrays.equals(output, expand))
					{
						expand = output;
						infCounter++;
					}
					expandVal = problem.f(expand);
				}
				else if(correctionStrategy== 's')
				{
					//System.out.println("SAT");
					output = saturation(expand, bounds);
					
					if(!Arrays.equals(output, expand))
					{
						expand = output;
						infCounter++;
					}
					expandVal = problem.f(expand);
					
				}
				else if(correctionStrategy== 'e')
				{
					output = saturation(expand, bounds);
					if(!Arrays.equals(output, expand))
					{
						infCounter++;
						expandVal = 2;
					}
				}
				else
					System.out.println("No bounds handling shceme seleceted");
				//expandVal = problem.f(expand);
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
				for (int j = 0; j < problemDimension; j++)
					contract[j] = mean[j] + beta*(reflect[j] - mean[j]);
				
				
				//contract = toro(contract, bounds);
				output = new double[problemDimension];
				if(correctionStrategy == 't')
				{
					//System.out.println("toro");
					output = toro(contract, bounds);
					
					if(!Arrays.equals(output, contract))
					{
						contract = output;
						infCounter++;
					}
					contractVal = problem.f(contract);
				}
				else if(correctionStrategy== 's')
				{
					//System.out.println("SAT");
					output = saturation(contract, bounds);
					
					if(!Arrays.equals(output, contract))
					{
						contract = output;
						infCounter++;
					}
					contractVal = problem.f(contract);
					
				}
				else if(correctionStrategy== 'e')
				{
					output = saturation(contract, bounds);
					if(!Arrays.equals(output, contract))
					{
						infCounter++;
						contractVal = 2;
					}
					
				}
				else
					System.out.println("No bounds handling shceme seleceted");
				//contractVal = problem.f(contract);
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
							
							for (int j = 0; j < problemDimension; j++)
								simplex[k][j] = simplex[l][j]+delta*(simplex[k][j]-simplex[l][j]);
							//simplex[k] = toro(simplex[k], bounds);
							output = new double[problemDimension];
							if(correctionStrategy == 't')
							{
								//System.out.println("toro");
								output = toro(simplex[k], bounds);
								
								if(!Arrays.equals(output, simplex[k]))
								{
									simplex[k] = output;
									infCounter++;
								}
								fSimplex[k] = problem.f(simplex[k]);
							}
							else if(correctionStrategy== 's')
							{
								//System.out.println("SAT");
								output = saturation(simplex[k], bounds);
								
								if(!Arrays.equals(output, simplex[k]))
								{
									simplex[k] = output;
									infCounter++;
								}
								fSimplex[k] = problem.f(simplex[k]);
								
							}
							else if(correctionStrategy== 'e')
							{
								output = saturation(simplex[k], bounds);
								if(!Arrays.equals(output, simplex[k]))
								{
									infCounter++;
									fSimplex[k] = 2;
								}
							}
							else
								System.out.println("No bounds handling shceme seleceted");
							//fSimplex[k] = problem.f(simplex[k]);
								
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
			else if (reflectVal >= iSimplex[h][0])
			{
				// contract inside
				for (int j = 0; j < problemDimension; j++)
					contract[j] = mean[j] + beta*(simplex[h][j] - mean[j]);
				//contract = toro(contract, bounds);
				output = new double[problemDimension];
				if(correctionStrategy == 't')
				{
					//System.out.println("toro");
					output = toro(contract, bounds);
					
					if(!Arrays.equals(output, contract))
					{
						contract = output;
						infCounter++;
					}
					contractVal = problem.f(contract);
				}
				else if(correctionStrategy== 's')
				{
					//System.out.println("SAT");
					output = saturation(contract, bounds);
					
					if(!Arrays.equals(output, contract))
					{
						contract = output;
						infCounter++;
					}
					contractVal = problem.f(contract);
					
				}
				else if(correctionStrategy== 'e')
				{
					output = saturation(contract, bounds);
					if(!Arrays.equals(output, contract))
					{
						infCounter++;
						contractVal = 2;
					}
					
				}
				else
					System.out.println("No bounds handling shceme seleceted");
				//contractVal = problem.f(contract);
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
					fSimplex[h] = contractVal;
					
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

							for (int j = 0; j < problemDimension; j++)
								simplex[k][j] = simplex[l][j]+delta*(simplex[k][j]-simplex[l][j]);
							
							//simplex[k] = toroSameBounds(simplex[k], bounds);
							output = new double[problemDimension];
							if(correctionStrategy == 't')
							{
								//System.out.println("toro");
								output = toro(simplex[k], bounds);
								
								if(!Arrays.equals(output, simplex[k]))
								{
									simplex[k] = output;
									infCounter++;
								}
								fSimplex[k] = problem.f(simplex[k]);
							}
							else if(correctionStrategy== 's')
							{
								//System.out.println("SAT");
								output = saturation(simplex[k], bounds);
								
								if(!Arrays.equals(output, simplex[k]))
								{
									simplex[k] = output;
									infCounter++;
								}
								fSimplex[k] = problem.f(simplex[k]);
								
							}
							else if(correctionStrategy== 'e')
							{
								output = saturation(simplex[k], bounds);
								if(!Arrays.equals(output, simplex[k]))
								{
									infCounter++;
									fSimplex[k] = 2;
								}
								
							}
							else
								System.out.println("No bounds handling scheme seleceted");
							//fSimplex[k] = problem.f(simplex[k]);
							
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
//		wrtiteCorrectionsPercentage(fileName, (double) infCounter/maxEvaluations);
		wrtiteCorrectionsPercentage(fileName, (double) infCounter/maxEvaluations, fSimplex, correctionStrategy);
		
		return FT;
	}
	

public String formatter(double value)
{
	String str =""+value;
	str = this.DF.format(value).toLowerCase();
	if (!str.contains("e-"))
		str = str.replace("e", "e+");
	return str;
}

public int[] getIndices(int popSize)
{
	int[] indices = new int[popSize];
	for(int n=0; n<popSize; n++)
		indices[n] = n;
	return indices;
}

public double[] saturation(double[] x, double[][] bounds)
{
	double[] xs = new double[x.length];
	for(int i=0; i<x.length; i++)
	{
		if(x[i]>bounds[i][1])
			xs[i] = bounds[i][1];
		else if(x[i]<bounds[i][0])
			xs[i] = bounds[i][0];
		else
			xs[i] = x[i];
	}
	return xs;
}



public void wrtiteCorrectionsPercentage(String name, double percentage) throws Exception
{
	File f = new File(Dir+"corrections.txt");
	if(!f.exists())
		f.createNewFile();
	FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
	BufferedWriter BW = new BufferedWriter(FW);
	BW.write(name+" "+percentage+"\n");
	BW.close();
}

public void wrtiteCorrectionsPercentage(String name, double percentage, double[] finalFitnesses, char boundaHendler) throws Exception
{
	if(boundaHendler != 'e')
		wrtiteCorrectionsPercentage(name, percentage);
	else
	{
		int counter = 0;
		for(int n=0; n<finalFitnesses.length; n++)
			if(finalFitnesses[n]==2)
				counter++;
		File f = new File(Dir+"corrections.txt");
		if(!f.exists())
			f.createNewFile();
		FileWriter FW = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write(name+" "+percentage+" "+formatter((double)counter/finalFitnesses.length)+"\n");
		BW.close();
	}
}
	
	
	
}
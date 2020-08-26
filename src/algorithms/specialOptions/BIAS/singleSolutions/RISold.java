package algorithms.specialOptions.BIAS.singleSolutions;

import static utils.algorithms.Misc.cloneSolution;
import static utils.algorithms.operators.ISBOp.crossOverExp;
import static utils.algorithms.operators.ISBOp.generateRandomSolution;
import static utils.algorithms.Corrections.torus;
import static utils.algorithms.Corrections.saturation;
import static utils.algorithms.Corrections.completeOneTailedNormal;
import static utils.algorithms.Corrections.mirroring;
import static utils.MatLab.max;

import java.util.Arrays;


import utils.algorithms.Counter;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

public class RISold extends AlgorithmBias
{
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();
		FTrend FT = new FTrend();
		double globalAlpha = getParameter("p0").doubleValue(); // 0.5
		double radius = getParameter("p1").doubleValue(); // 0.4
		double xi = getParameter("p2").doubleValue(); // 0.000001
		double CR = Math.pow(0.5, (1/(problemDimension*globalAlpha)));
			
		double[] best;
		double fBest;
		double[] temp;
		
		
		
		
		char correctionStrategy = this.correction;  // t --> torus   s --> saturation  d -->  discard  e ---> penalty
		
		
		
		String FullName = getFullName("RIS"+correctionStrategy,problem);
		Counter PRGCounter = new Counter(0);
		createFile(FullName);

		
		int i = 0;
		int prevID = -1;
		int newID = 0;
		
		writeHeader("globalAlpha "+globalAlpha+" radius "+radius+" xi "+xi, problem);
			
		
		best = generateRandomSolution(bounds, problemDimension,PRGCounter);
		fBest = problem.f(best);
		FT.add(0, fBest);
		i++; newID++;
		double[] x = new double[problemDimension];
		for(int k=0; k < problemDimension; k++)
		  x[k] = best[k];
		
		String line = new String();
		line =""+newID+" "+formatter(fBest)+" "+i+" "+prevID;
		for(int n = 0; n < problemDimension; n++)
			line+=" "+formatter(best[n]);
		line+="\n";
		bw.write(line);
		line = null;
		line = new String();
		prevID = newID;
		
		
	
		
		
		while (i < maxEvaluations)
		{
			temp = generateRandomSolution(bounds, problemDimension,PRGCounter);
			x = crossOverExp(best, temp, CR,PRGCounter);
			
			double fx = problem.f(x);

			i++;
//			newID++;
						
			if(fx < fBest)
			{
				fBest = fx;
				for(int n=0;n<problemDimension;n++)
					best[n] = x[n];
				FT.add(i, fBest);
				
				newID++;
				
				line =""+newID+" "+formatter(fBest)+" "+i+" "+prevID;
				for(int n = 0; n < problemDimension; n++)
					line+=" "+formatter(best[n]);
				line+="\n";
				bw.write(line);
				line = null;
				line = new String();
				prevID = newID;
			}
			
			//System.out.print(best[0]+" ");System.out.print(best[1]+" "); System.out.println(best[2]+" ");
			
			
					
			//temp = ThreeSome_ShortDistance(x, fx, radius, xi, problem, maxEvaluations,i);#
		

			double[] SR = new double[problemDimension];
			for (int k = 0; k < problemDimension; k++)
				SR[k] = (bounds[k][1] - bounds[k][0])*radius;

			boolean improve = true;

			//while ((SR[0] > precision) && (iter < totalBudget))
			while ((max(SR) > xi) && (i < maxEvaluations))
			{
				double[] Xk = new double[problemDimension];
				double[] Xk_orig = new double[problemDimension];
				for (int k = 0; k < problemDimension; k++)
				{
					Xk[k] = x[k];
					Xk_orig[k] = x[k];
				}

				double fXk_orig = fx;

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
					
					double[] output = new double[problemDimension];
					if(correctionStrategy == 't')
					{
						//System.out.println("TORUS");
						output = torus(Xk, bounds);
					}
					else if(correctionStrategy== 's')
					{
						//System.out.println("SAT");
						output = saturation(Xk, bounds);
					}
					else if(correctionStrategy== 'd')
					{
						output = torus(Xk, bounds);
						
						if(!Arrays.equals(output, Xk))
						{
							output = cloneSolution(Xk_orig);
							//output[k] = Xk_orig[k];
							
							for(int n = 0; n < problemDimension; n++)
								if(output[n]<0 || output[n]>1) System.out.println("OUT!");
						}
							
					}
					else if(correctionStrategy== 'c')
					{
						//System.out.println("SAT");
						output = completeOneTailedNormal(Xk, bounds, 3.0);
					}
					else if(correctionStrategy== 'm')
					{
						//System.out.println("SAT");
						output = mirroring(Xk, bounds);
					}
					else
						System.out.println("No bounds handling scheme selected");
					
					if(!Arrays.equals(output, Xk))
					{
						Xk = output;
						output = null;
						this.numberOfCorrections++;
					}
					
					
					double fXk = problem.f(Xk);
					i++;

					// best update
					if (fXk < fx)
					{
						fx = fXk;
						for (int n = 0; n < problemDimension; n++)
							x[n] = Xk[n];
						
						if (fXk < fBest)
						{
							newID++;
							
							fBest = fx;
							for(int n=0;n<problemDimension;n++)
								best[n] = x[n];
							FT.add(i, fBest);
							
							
//							for(int n = 0; n < problemDimension; n++)
//								if(best[n]<0 || best[n]>1) System.out.println("OUT!");
							
							line =""+newID+" "+formatter(fBest)+" "+i+" "+prevID;
							for(int n = 0; n < problemDimension; n++)
								line+=" "+formatter(best[n]);
							line+="\n";
							bw.write(line);
							line = null;
							line = new String();
							prevID = newID;
						}

					}

					if (i < maxEvaluations)
					{
						if (fXk == fXk_orig)
						{
							for (int n = 0; n < problemDimension; n++)
							{
								Xk[n] = Xk_orig[n];
								//x[n] = Xk_orig[n];
							}
						}
						else
						{
							if (fXk > fXk_orig)
							{
								Xk[k] = Xk_orig[k];
								Xk[k] = Xk[k] + 0.5*SR[k];
								
								output = new double[problemDimension];
								if(correctionStrategy == 't')
								{
									//System.out.println("TORUS");
									output = torus(Xk, bounds);
								}
								else if(correctionStrategy== 's')
								{
									//System.out.println("SAT");
									output = saturation(Xk, bounds);
								}
								else if(correctionStrategy== 'd')
								{
									output = torus(Xk, bounds);
									if(!Arrays.equals(output, Xk))
									{
										output = cloneSolution(Xk_orig);
										//Xk[k] = Xk_orig[k];
									}
								}
								else if(correctionStrategy== 'c')
								{
									//System.out.println("SAT");
									output = completeOneTailedNormal(Xk, bounds, 3.0);
								}
								else if(correctionStrategy== 'm')
								{
									//System.out.println("SAT");
									output = mirroring(Xk, bounds);
								}
								else
									System.out.println("No bounds handling shceme seleceted");
								
								if(!Arrays.equals(output, Xk))
								{
									Xk = output;
									output = null;
									this.numberOfCorrections++;
								}
								
								fXk = problem.f(Xk);
								i++;

					
								if (fXk < fx)
								{
									fx = fXk;
									for (int n = 0; n < problemDimension; n++)
										x[n] = Xk[n];
									
									if (fXk < fBest)
									{
										newID++;
										
										fBest = fx;
										for(int n=0;n<problemDimension;n++)
											best[n] = x[n];
										FT.add(i, fBest);
										
										line =""+newID+" "+formatter(fBest)+" "+i+" "+prevID;
										for(int n = 0; n < problemDimension; n++)
											line+=" "+formatter(best[n]);
										line+="\n";
										bw.write(line);
										line = null;
										line = new String();
										prevID = newID;
									}
								}

								if (fXk >= fXk_orig)
								{
									Xk[k] = Xk_orig[k];
									//x[k] = Xk_orig[k];
								}
								else
									improve = true;
							}
							else
								improve = true;
						}
					}

					k++;
				}

			}
							


		}
		
		finalBest = best;
		
		FT.add(i,fBest);

		bw.close();
		
		//wrtiteCorrectionsPercentage(fileName, (double) this.numberOfCorrections/maxEvaluations, "correctionsSingleSol");
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(), "correctionsSingleSol");
		return FT;
	}
	
}
package algorithms.specialOptions.BIAS.singleSolutions;

import static utils.algorithms.operators.DEOp.crossOverExp;
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.MatLab.max;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;

import utils.random.RandUtilsISB;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

import utils.algorithms.Counter;

public class RIS extends AlgorithmBias
{	
	DecimalFormat DF = new DecimalFormat("0.00000000E00");
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
		this.numberOfCorrections = 0;
		Counter PRGCounter = new Counter(0);
		
		char correctionStrategy = this.correction;  // t --> toroidal   s --> saturation  d -->  discard  e ---> penalty
		String fileName = "RIS"+correctionStrategy; 
		
		fileName+="D"+problem.getDimension()+"f0-"+(run+1);
		File file = new File(Dir+fileName+".txt");
		if (!file.exists()) 
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		int i = 0;
		int prevID = -1;
		int newID = 0;
		long seed = System.currentTimeMillis();
		RandUtilsISB.setSeed(seed);
		RandUtilsISB.setSeed(seed);	
		String line = "# function 0 dim "+problemDimension+" globalAlpha "+globalAlpha+" radius "+radius+" xi "+xi+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
		bw.write(line);
		line = null;
		line = new String();
		
		
		best = generateRandomSolution(bounds, problemDimension);
		fBest = problem.f(best);
		FT.add(0, fBest);
		i++; newID++;
		double[] x = new double[problemDimension];
		for(int k=0; k < problemDimension; k++)
		  x[k] = best[k];
			
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
			temp = generateRandomSolution(bounds, problemDimension);
			x = crossOverExp(best, temp, CR);
			
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
			
//				for(int n = 0; n < problemDimension; n++)
//					if(best[n]== 0.0) System.out.println("zero!");
					
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
					//double[] prevXk = cloneSolution(Xk);
					Xk[k] = Xk[k] - SR[k];
					
					
//					//Xk = saturateToro(Xk, bounds);
//					double[] output = new double[problemDimension];
//					if(correctionStrategy == 't')
//					{
//						//System.out.println("TORO");
//						output = toro(Xk, bounds);
//					}
//					else if(correctionStrategy== 's')
//					{
//						//System.out.println("SAT");
//						output = saturation(Xk, bounds);
//					}
//					else if(correctionStrategy== 'd')
//					{
//						output = toro(Xk, bounds);
//						if(!Arrays.equals(output, Xk))
//						{
//							output = cloneSolution(Xk);
//							output[k] = Xk_orig[k]; 
//						}
//							
//					}
//					else
//						System.out.println("No bounds handling shceme seleceted");
//					
//					if(!Arrays.equals(output, Xk))
//					{
//						Xk = output;
//						output = null;
//						ciccio++;
//					}
					
				
					
					Xk = correct(Xk,Xk_orig,bounds, PRGCounter);
					
					
					
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
							
							for(int n = 0; n < problemDimension; n++)
								if(best[n]<0 || best[n]>1) System.out.println("OUT!");
							
							
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
								
								//prevXk = cloneSolution(Xk);
								
								Xk[k] = Xk[k] + 0.5*SR[k];
								
								
//								//Xk = saturateToro(Xk, bounds);
//								output = new double[problemDimension];
//								if(correctionStrategy == 't')
//								{
//									//System.out.println("TORO");
//									output = toro(Xk, bounds);
//								}
//								else if(correctionStrategy== 's')
//								{
//									//System.out.println("SAT");
//									output = saturation(Xk, bounds);
//								}
//								else if(correctionStrategy== 'd')
//								{
//									output = toro(Xk, bounds);
//									if(!Arrays.equals(output, Xk))
//									{
//										output = cloneSolution(Xk);
//										output[k] = Xk_orig[k];
//									}
//										
//								}
//								else
//									System.out.println("No bounds handling shceme seleceted");
//								
//								if(!Arrays.equals(output, Xk))
//								{
//									Xk = output;
//									output = null;
//									ciccio++;
//								}
								
								Xk = correct(Xk,Xk_orig,bounds, PRGCounter);
								
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
//										
//										for(int n = 0; n < problemDimension; n++)
//										if(best[n]<0 || best[n]>1) System.out.println("OUT!");
										
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
		
		FT.add(i, fBest);

		bw.close();
//		System.out.println(numberOfCorrections+" over "+maxEvaluations+" i = "+i);
		wrtiteCorrectionsPercentage(fileName, (double) this.numberOfCorrections/maxEvaluations);
		return FT;
	}
}
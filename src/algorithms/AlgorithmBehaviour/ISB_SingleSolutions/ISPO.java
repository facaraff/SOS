package algorithms.AlgorithmBehaviour.ISB_SingleSolutions;

import static utils.algorithms.operators.ISBOp.generateRandomSolution;
import static utils.algorithms.Misc.cloneSolution;
import static utils.algorithms.Misc.fillAWithB;


import utils.algorithms.Counter;
import utils.random.RandUtilsISB;
import interfaces.AlgorithmBias;
import interfaces.Problem;
import utils.RunAndStore.FTrend;

/*
 * Intelligent Single Particle Optimization
 */
public class ISPO extends AlgorithmBias
{
	
//	static String Dir = "/home/facaraff/Desktop/KONODATA/SINGLESOLUTION/ISPO/";
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		// NB: A and P are problem-dependent
		double A = getParameter("p0");							//1
		double P = getParameter("p1");							//10
		int B = this.getParameter("p2").intValue();			//2
		int S = this.getParameter("p3").intValue();			//4
		double E = this.getParameter("p4");					//1e-5
		int PartLoop = this.getParameter("p5").intValue();		//30

		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension();
		double[][] bounds = problem.getBounds();

		
		char correctionStrategy = this.correction;  // t --> torus   s --> saturation  d -->  discard  e ---> penalty
		
		
		String FullName = getFullName("ISPO"+correctionStrategy,problem);
		Counter PRGCounter = new Counter(0);
		createFile(FullName);
		
		
//
//		fileName+="D"+problem.getDimension()+"f0-"+(run+1);
//		File file = new File(Dir+fileName+".txt");
//		if (!file.exists())
//			file.createNewFile();
//		FileWriter fw = new FileWriter(file.getAbsoluteFile());
//		BufferedWriter bw = new BufferedWriter(fw);
		
		int i = 0;
		int prevID = -1;
		int newID = 0;
//		long seed = System.currentTimeMillis();
//		RandUtilsISB.setSeed(seed);
//		String line = "# function 0 dim "+problemDimension+" A "+A+" P "+P+" B "+B+" S "+S+" E "+E+" PartLoop "+PartLoop+" max_evals "+maxEvaluations+" SEED  "+seed+"\n";
//		bw.write(line);
//		line = null;
//		line = new String();
		
		writeHeader("A "+A+" P "+P+" B "+B+" S "+S+" E "+E+" PartLoop "+PartLoop, problem);
		String line = new String();
		// particle
		double[] particle = new double[problemDimension];
		double fParticle;
		//int i = 0;
		// initial solution
		if (initialSolution != null)
		{
			particle = initialSolution;
		    fParticle = initialFitness;
		}
		else
		{
			particle = generateRandomSolution(bounds, problemDimension, PRGCounter);
			fParticle = problem.f(particle);
			newID++; i++;
			
			line =""+newID+" "+formatter(fParticle)+" "+i+" "+prevID;
			for(int n = 0; n < problemDimension; n++)
				line+=" "+formatter(particle[n]);
			line+="\n";
			bw.write(line);
			line = null;
			line = new String();
			prevID = newID;
		}
		FT.add(0, fParticle);
		
		// temp variables
		double L = 0;
		double velocity = 0;
		double oldfFParticle = fParticle;
		double posOld;
		double[] particleOld = cloneSolution(particle);
		
		while (i < maxEvaluations)
		{
			for (int j = 0; j < problemDimension && i < maxEvaluations; j++)
			{
				// init learning factor
				L = 0;

				// for each part loop
				for (int k = 0; k < PartLoop && i < maxEvaluations; k++)
				{
					// old fitness value
					oldfFParticle = fParticle;
					// old particle position
					posOld = particle[j];
					fillAWithB(particleOld,particle);

					// calculate velocity
					velocity = A/Math.pow(k+1,P)*(-0.5+RandUtilsISB.random(PRGCounter))+B*L;

					// calculate new position
					particle[j] += velocity;
					//particle[j] = min(max(particle[j], bounds[j][0]), bounds[j][1]);
					 				
					
					particle = correct(particle, particleOld, bounds, PRGCounter);

					// calculate new fitness
					fParticle = problem.f(particle);
					i++;
//					newID++;

					// estimate performance
					if (oldfFParticle < fParticle)
					{
						// adjust learning factor
						if (L != 0)
							L /= S;
						if (Math.abs(L) < E)
							L = 0;
						// use old position
						particle[j] = posOld;
						fParticle = oldfFParticle;
					}
					else
					{
						// use current velocity as learning factor
						L = velocity;
						
						newID++;
						
						line =""+newID+" "+formatter(fParticle)+" "+i+" "+prevID;
						for(int n = 0; n < problemDimension; n++)
							line+=" "+formatter(particle[n]);
						line+="\n";
						bw.write(line);
						line = null;
						line = new String();
						prevID = newID;
					}
					
					//if(i%problemDimension==0)
						FT.add(i, fParticle);
				}
			}
		}
		
		finalBest = particle;
		FT.add(i, fParticle);
		bw.close();
		
		//wrtiteCorrectionsPercentage(fileName, (double) this.numberOfCorrections/maxEvaluations,"correctionsSingleSol");
		writeStats(FullName, (double) this.numberOfCorrections/maxEvaluations, PRGCounter.getCounter(), "correctionsSingleSol");
		return FT;
	}
}
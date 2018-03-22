package compact;

import static utils.algorithms.operators.CompactAlgorithms.bin2dec;
import static utils.algorithms.operators.CompactAlgorithms.generateIndividual;

import java.io.FileWriter;
import java.io.IOException;

import utils.MatLab;
import interfaces.Algorithm;
import interfaces.Problem;
import static utils.RunAndStore.FTrend;

/*
 * Binary coded compact Genetic Algorithm
 */
public class cGA_bin extends Algorithm
{
	boolean debugPV = false;
	
	@Override
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		int virtualPopulationSize = this.getParameter("p0").intValue();
		int eta = this.getParameter("p1").intValue();
		boolean isPersistent = this.getParameter("p2").intValue()!=0;
		int numberOfBitsPerVariable = this.getParameter("p3").intValue();
				
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();
		
		int i = 0;
		int teta = 0;

		double[] PV = new double[problemDimension*numberOfBitsPerVariable];
		int lengthPV = PV.length;
		for (int j = 0; j < lengthPV; j++)
			PV[j] = 0.5;

		byte[] best = new byte[lengthPV];
		double fBest = Double.NaN;
		
		// evaluate initial solutions
		byte [] a = new byte[lengthPV];
		byte [] b = new byte[lengthPV];
		double [] aScaled = new double[problemDimension];
		double [] bScaled = new double[problemDimension];
		
		a = generateIndividual(PV);
		b = generateIndividual(PV);
		aScaled = bin2dec(a, numberOfBitsPerVariable, bounds);
		bScaled = bin2dec(b, numberOfBitsPerVariable, bounds);

		double fA = problem.f(aScaled);
		double fB = problem.f(bScaled);
		if (fA < fB)
		{
			fBest = fA;
			for (int n = 0; n < lengthPV; n++)
				best[n] = a[n];
			FT.add(0, fA);
		}
		else
		{
			fBest = fB;
			for (int n = 0; n < lengthPV; n++)
				best[n] = b[n];
			FT.add(0, fB);			
		}
		i += 2;

		byte[] winner = new byte[lengthPV];
		byte[] loser = new byte[lengthPV];
		
		FileWriter fileWriter = null;
		if (debugPV)
		{
			try {
				fileWriter = new FileWriter("mean.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// iterate
		while (i < maxEvaluations)
		{
			b = generateIndividual(PV);
			bScaled = bin2dec(b, numberOfBitsPerVariable, bounds);
			fB = problem.f(bScaled);
			i++;

			if (fB < fBest)
			{
				for (int n = 0; n < lengthPV; n++)
				{
					winner[n] = b[n];
					loser[n] = best[n];
				}
				fBest = fB;

				if (isPersistent)
					// log best fitness (persistent elitism)
					FT.add(i, fBest);
				else
				{
					// log best fitness (non persistent elitism)
					if (fBest < FT.getF(FT.getLastI()))
						FT.add(i, fBest);
				}
			}
			else
			{
				for (int n = 0; n < lengthPV; n++)
				{
					winner[n] = best[n];
					loser[n] = b[n];
				}
			}
			
			if (!isPersistent)
			{
				if ((teta < eta) && (MatLab.isEqual(winner, best)))
					teta++;
				else
				{
					teta = 0;
					for (int n = 0; n < lengthPV; n++)
						winner[n] = b[n];
					fBest = fB;
				}
			}
			
			for (int n = 0; n < lengthPV; n++)
				best[n] = winner[n];

			// mean update
			int[] signs = new int[lengthPV];
			for (int n = 0; n < lengthPV; n++)
			{
				if (winner[n] == 0)
					signs[n] = 1;
				else
					signs[n] = -1;
				
				if (winner[n] != loser[n])
					PV[n] += ((double)signs[n])/virtualPopulationSize; 
			}

			if (debugPV)
			{
				try {
					fileWriter.write(MatLab.toString(PV) + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (isPersistent)
			// log best fitness (persistent elitism)
			FT.add(i, fBest);
		else
		{
			// log best fitness (non persistent elitism)
			double lastFBest = FT.getF(FT.getLastI()); 
			if (fBest < lastFBest)
				FT.add(i, fBest);
			else
				FT.add(i, lastFBest);
		}
		
		if (debugPV)
		{
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		finalBest = bin2dec(best, numberOfBitsPerVariable, bounds);
		
		return FT;
	}
}
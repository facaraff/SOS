/**
Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package mains.AlgorithmicBehaviour.temp;


import java.util.Vector;

import algorithms.AlgorithmBehaviour.ISB_PopBased.GA;
//import benchmarks.ISBSuite;
import benchmarks.BBOB2010;
import utils.ExperimentHelper;
import interfaces.AlgorithmBias;
import interfaces.ISBMain;
import interfaces.Problem;

import static utils.RunAndStore.slash;
	
public class GA2021BBOB extends ISBMain
{	
	public static void main(String[] args) throws Exception
	{	
		AlgorithmBias a;
//		Problem p;

		
		Vector<AlgorithmBias> algorithms = new Vector<AlgorithmBias>();
		Vector<Problem> problems = new Vector<Problem>();
	
		ExperimentHelper expSettings = new ExperimentHelper();
//		expSettings.setBudgetFactor(10);
//		expSettings.setNrRepetition(1);
		
		int n = expSettings.getProblemDimension();

		
//		p = new ISBSuite("f0",n);
//		problems.add(p); p = null;
//		p = new ISBSuite("g0",n);
//		problems.add(p); p = null;
//		p = new ISBSuite("h0",n);
//		problems.add(p); p = null;
//		p = new ISBSuite("i0",n);
//		problems.add(p); p = null;
		
		
		for(int i = 1; i<=24; i++)
			problems.add(new BBOB2010(n, i));
		
		
//		problems.add(new BBOB2010(n,1));

		
		char[] corrections = {'s','t','d','c'};	//'m',	
		double[] populationSizes = {5, 20, 100};
		char[] GAParentSelections = {'r','t'};
		char[] GACrossOvers = {'a','d'};
		char[] GAMutations = {'c','g'};
		
		
		
		for (double popSize : populationSizes)
		{
			for (char correction : corrections)
			{			
				for (char selection : GAParentSelections)
					for (char oxer : GACrossOvers)
						for (char mutation : GAMutations)
						{
							a = new GA(selection, oxer, mutation);
							a.setDir("GA"+slash());
							a.setCorrection(correction);
							a.setParameter("p0", popSize); //Population size
							a.setParameter("p1", 2.0); //tournament size
							a.setParameter("p2", 2.0); //selection probability for stochastic tournament
							a.setParameter("p3", 0.5); //CR
							a.setParameter("p4", 0.25); //d
							a.setParameter("p5", 0.01); //md
							algorithms.add(a);		
							a = null;
						}
			}
			
		}
		
		execute(algorithms, problems, expSettings);		
	}
}

//GA{g|c}{d|a}{r|t}{t|s|e}p{5|20|100}D30
//i.e. {mutation} {xover} {parentselection} {correction}..
//
//
//and rGA{g|c}{d|a}{x}{t|s|d}{x|y}p{5|20|100}D…
//i.e. {mutation} {xover} {parentselection} {correction}{survivor selection}..

//a = new SimplifiedGA();
//a.setDir("GA-TELO"+slash());
//a.setCorrection(correction);
//a.setParameter("p0", popSize); //Population size
//a.setParameter("p1", 666.0); //FIND PARAMETER
//a.setParameter("p2", 666.0); //FIND PARAMETER
//a.setParameter("p3", 666.0); //FIND PARAMETER
//algorithms.add(a);		
//a = null;
		
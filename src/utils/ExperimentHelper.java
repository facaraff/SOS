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

package utils;


public class ExperimentHelper 
{
	protected int nrRepetitions; 
	protected int budgetFactor; 
	protected int problemDimension;
	
	public ExperimentHelper() {this.nrRepetitions = 50; this.budgetFactor = 10000; this.problemDimension = 30;}
	public ExperimentHelper(int nrRepetitions, int budgetFactor, int problemDimension) {this.nrRepetitions = nrRepetitions; this.budgetFactor = 10000; this.problemDimension = 30;}
	
	public void setNrRepetition(int nrRepetitions) {this.nrRepetitions=nrRepetitions;}; 
	public void setBudgetFactor(int budgetFactor) {this.budgetFactor=budgetFactor;}; 
	public void setProblemDimension(int problemDimension) {this.problemDimension=problemDimension;}; 
	
	public int getNrRepetitions() {return this.nrRepetitions;}; 
	public int getBudgetFactor() {return this.budgetFactor;}; 
	public int getProblemDimension() {return this.problemDimension;}; 
	

}

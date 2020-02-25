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
package utils.algorithms.operators.aos;

import java.util.Random;


public abstract class OperatorSelection {
	
	protected int n_operators;
	protected int times;
	protected int best_op;
	public CreditAssignment [] credit;
	public Random rng;
	
	public OperatorSelection(int _n_operators, long seed){
		n_operators = _n_operators;
		rng = new Random(seed);
		times = 0;
		best_op = 0;				
	}
	
	public void InitCreditAssign(int W, int type, boolean norm, double decay){
		if(type < 3){
			credit = new CreditAssignment[n_operators];
			for(int i=0; i<n_operators; i++) 
				credit[i] = new CreditAssignment(W, type, norm);
		} 
//		else if(type==5 || type==6){
//			credit = new CreditAssignment[1];
//			if(type==5) credit[0] = new AucRankCredit(W, n_operators, true, false, 0, decay); //AUC			
//			else credit[0] = new SumRankCredit(W, n_operators, true, false, 0, decay); // SUM
//		} 
	else{
			System.err.println("Credit Assignment type "+type+" does not exist! Please check it!");
			System.exit(1);
		}
	}
	
	public abstract double ApplyReward(int op);
	protected abstract int OptionNotTried();
	public abstract int SelectOperator();
	public abstract double getProbability(int op) ;
	
	protected double GetReward(int op){
		double reward;
		if(credit[0].getType()<3){
			verifyRewNormal(op);
			reward = credit[op].getReward(op, false);
		} else {
			reward = credit[0].getReward(op, false);
		}
		return reward;		
	}
	
	
	private void verifyRewNormal(int op){
		// in case "normalization" is on, checks which is the highest "output" (according to the chosen credit assignment)
		// between all operators, to set it in the used operator, so that the reward will be normalized when asked.
		if(credit[0].isNorm()){
			double maxrew=0, temprew=0;
			for (int i=0; i<n_operators; i++){
				temprew = credit[i].getReward(i, true);
				if(temprew > maxrew) maxrew = temprew;
			}
			credit[op].setMaxReward(maxrew);
		}
	}

}

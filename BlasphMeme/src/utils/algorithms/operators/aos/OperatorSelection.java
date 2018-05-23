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

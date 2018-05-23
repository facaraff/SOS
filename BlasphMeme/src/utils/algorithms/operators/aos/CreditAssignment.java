package utils.algorithms.operators.aos;

import java.util.LinkedList;

public class CreditAssignment {
	protected int W; //size of the sliding window
	protected LinkedList<Double> wRewards;
	private double sumReward;
	private double bestReward;
	private double maxReward;
	protected int type;
	protected boolean normalized;
	
	public CreditAssignment(int _W, int _type, boolean _normalized){
		W = _W;
		type = _type;
		normalized = _normalized;
		sumReward = bestReward = maxReward = 0;
		wRewards = new LinkedList<Double>();
	}

	public int getType(){
		return type;
	}
	
	public boolean isNorm(){
		return normalized;
	}

	public int getTimesOp(int op){
		return wRewards.size();
	}

	//generic one
	public void addFitnessImprovLS(int op, double fit_old, double fit_new, long elapsed_time){
		//cout << "addFitnessImprov1" << endl;
		double diff = (fit_old - fit_new)/(double)elapsed_time;
		
		//if(delta_fitness)
			updateQueue(op, diff);
		//else{
			//if (diff <= 0) fit_new = 0;
			//updateQueue(op, fit_new);
		//}
	}
	
	//to use with mutation
	public void addFitnessImprovM(int op, double fit_old, double fit_new){
		//cout << "addFitnessImprov1" << endl;
		double diff = fit_old - fit_new;
		//if(delta_fitness)
			updateQueue(op, diff);
		//else{
			//if (diff <= 0) fit_new = 0;
			//updateQueue(op, fit_new);
		//}
	}
	
	//to use with crossover that generates one solution
	public void addFitnessImprovX1(int op, double fit_old1, double fit_old2, double fit_new){
		//cout << "addFitnessImprov2" << endl;
		double best_old;

		if(fit_old1 < fit_old2) best_old = fit_old1;
		else best_old = fit_old2;

		double diff = best_old - fit_new;
		//if(delta_fitness)
			updateQueue(op, diff);
		//else{
			//if(diff <= 0) best_new = 0;
			//updateQueue(op, best_new);
		//}
	}
	
	//to use with crossover that generates two solutions
	public void addFitnessImprovX2(int op, double fit_old1, double fit_old2, double fit_new1, double fit_new2){
		//cout << "addFitnessImprov2" << endl;
		double best_old, best_new;

		if(fit_old1 < fit_old2) best_old = fit_old1;
		else best_old = fit_old2;

		if(fit_new1 < fit_new2) best_new = fit_new1;
		else best_new = fit_new2;

		double diff = best_old - best_new;
		//if(delta_fitness)
			updateQueue(op, diff);
		//else{
			//if(diff <= 0) best_new = 0;
			//updateQueue(op, best_new);
		//}
	}
	
	public void setMaxReward(double _max){
		//cout << "setMaxReward" << endl;
		if(_max < 0) maxReward = 0;
		else maxReward = _max;
	}

	public double getMaxReward(){
		//cout << "getMaxReward" << endl;
		return maxReward;
	}


	public double getReward(int op, boolean nonorm){
		double reward;
		if(type==0) reward=getBestReward(nonorm); //Extreme
		else if(type==1) reward=getAvgReward(nonorm); //Average
		else if(type==2) reward=getLastReward(op, nonorm); //Instantaneous
		else reward=0;
		return reward;
	}


	private double getLastReward(int op, boolean nonorm){
		//cout << "getLastReward" << endl;
		double instRew, divide;

		if(!normalized || nonorm || maxReward==0) divide=1.0;
		else divide = maxReward;

		if(wRewards.size()>0){
			instRew = wRewards.getFirst(); //using FIFO order
			double reward = instRew/divide;
			if(reward > 0) return reward;
		}

		return 0;
	}

	private double getBestReward(boolean nonorm){
		//cout << "getBestReward" << endl;
		double divide;

		if(!normalized || nonorm || maxReward==0) divide=1.0;
		else divide=maxReward;

		double reward=bestReward/divide;
		if(reward > 0)	return reward;
		return 0;
	}

	private double getAvgReward(boolean nonorm){
		//cout << "getAvgReward" << endl;
		double divide, avg = (double)sumReward/(double)wRewards.size();

		if(!normalized || nonorm || maxReward==0) divide=1.0;
		else divide=maxReward;

		double reward = avg/divide;
		if(reward>0) return reward;
		return 0;
	}
	
	public void Print(){
		System.out.print(wRewards.size()+":\t");
		for(int i=0; i<wRewards.size(); i++)
			 System.out.print(wRewards.get(i)+"\t");
        System.out.println();        
	}
	
	protected void updateQueue(int op, double reward){
		
		//System.out.println("# Enqueueing reward "+reward+" for operator "+op+" ("+thisop+")");
		//System.out.println("The queue was like this:");
		//Print();
		
		sumReward += reward; //update the sum, used to extract the average, counts also the negative rewards.
		wRewards.addFirst(reward); //insert the latest reward in the queue

		if(reward > bestReward)	//if it is better than the actual best,
			bestReward = reward;//update the best

		if(wRewards.size() > W){ 		//if the queue becomes bigger than W
			double back = wRewards.removeLast(); //keeps the last one's value
			sumReward -= back;			//used to extract the average
			if(back == bestReward){		//if last was the best
				double val=0;
				bestReward = 0;

				for(int i=0; i<wRewards.size(); i++){
					val = wRewards.get(i);
					if (val > bestReward) //searches for the actual best
						bestReward = val;
				}
			}
		}
		//System.out.println("And now it is like this:");
		//Print();
	}

}

package algorithms.memes;

import algorithms.abstractAlgorithms.LongDistanceExploration;

/*
 * Long distance exploration with exit condition based on a specified computational budget
 */
public class L_fixed_budget extends LongDistanceExploration
{
	protected  boolean stopCondition(int i, int budget, boolean improved, double fx, double accuracy)
	{
		return i<budget;
	}
	
}










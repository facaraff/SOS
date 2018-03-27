package algorithms.memes;

import algorithms.abstractAlgorithms.LongDistanceExploration;

/*
 * Long distance exploration with exit condition based on the first improvement upon the fitness
 */
public class L extends LongDistanceExploration
{
	protected  boolean stopCondition(int i, int budget, boolean improved, double fx, double accuracy)
	{
		return i<budget && !improved;
	}
	
}










package utils;


public class ExperimentHelper 
{
	protected int nrRepetitions; 
	protected int budgetFactor; 
	protected int problemDimension;
	
	public ExperimentHelper() {this.nrRepetitions = 50; this.budgetFactor = 10000; this.problemDimension = 30;}

	public void setNrRepetition(int nrRepetitions) {this.nrRepetitions=nrRepetitions;}; 
	public void setBudgetFactor(int budgetFactor) {this.budgetFactor=budgetFactor;}; 
	public void setProblemDimension(int problemDimension) {this.problemDimension=problemDimension;}; 
	
	public int getNrRepetitions() {return this.nrRepetitions;}; 
	public int getBudgetFactor() {return this.budgetFactor;}; 
	public int getProblemDimension() {return this.problemDimension;}; 
	

}

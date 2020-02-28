package utils.algorithms;

public class Counter {
	
	private int counter;
	
	private int success;
	private int failure;
	
	private double score;

	
	public Counter() {this.counter = 0; this.success = 0; this.success = 0; this.success = 0; this.score = 0;};
	public Counter(int counter) {this.counter = counter;}
	public Counter(int success, int failure) {this.success = success; this.failure = failure;}
	public Counter(int counter, int success, int failure) {this.counter = counter; this.success = success; this.failure = failure;}
	public Counter(int counter, int success, int failure, double score) {this.counter = counter; this.success = success; this.failure = failure; this.score = score;}
	public Counter(double score) {this.score = score;}
	
	
	public void setCounter(int counter) {this.counter = counter;}
	public void setSuccess(int success) {this.success = success;}
	public void setFailure(int failure) {this.failure = failure;}
	public void setsSore(double score) {this.score = score;}
	
	public int getCounter() {return this.counter;}
	public int getSuccess() {return this.success;}
	public int getFailure() {return this.failure;}
	public double getScore() {return this.score;}
	
	public void incrementCounter(){this.counter++;}
	public void incrementSuccess(){this.success++;}
	public void incrementFailure(){this.failure++;}
	public void incrementCounter(int increment){this.counter+=increment;}
	public void incrementSuccess(int increment){this.success+=increment;}
	public void incrementFailure(int increment){this.failure+=-increment;}
	public void incrementScorer(double increment){this.score+=score;}
	
}

/**
Copyright (c) 2019, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package utils.algorithms;

public class Counter {
	
	private int counter;
	
	private int success;
	private int failure;
	
	private double score;
	
	private String s;

	
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
	public void setS(String s) {this.s = s;}
	
	public int getCounter() {return this.counter;}
	public int getSuccess() {return this.success;}
	public int getFailure() {return this.failure;}
	public double getScore() {return this.score;}
	public String getS() {return this.s;}
	
	public void incrementCounter(){this.counter++;}
	public void incrementSuccess(){this.success++;}
	public void incrementFailure(){this.failure++;}
	public void incrementCounter(int increment){this.counter+=increment;}
	public void incrementSuccess(int increment){this.success+=increment;}
	public void incrementFailure(int increment){this.failure+=-increment;}
	public void incrementScorer(double increment){this.score+=score;}
	

	
	
	
}

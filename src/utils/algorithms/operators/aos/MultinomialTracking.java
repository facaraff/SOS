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

public class MultinomialTracking extends OperatorSelection {

	protected double p_min;
	private double alpha;
	protected double[] quality;
	protected double[] prob;
	private int[] op_times;
	private double best_quality;
	MultinomialDistribution mtracker;
	double lambda_;
	
	public MultinomialTracking(int _n_operators, double _p_min, double _alpha, long _seed, double lambda) {
		super(_n_operators, _seed);
		p_min = _p_min;
		alpha = _alpha;
		quality = new double[_n_operators];
		prob = new double[_n_operators];
		op_times = new int[_n_operators];
		best_quality = 0;
		
		for(int i=0; i<_n_operators; i++){
			prob[i] = 1.0/(double)_n_operators;
			quality[i] = 1.0;
			op_times[i] = 0;
		}
		lambda_ = lambda;
		mtracker = new MultinomialDistribution(_n_operators, lambda);
	}
	
	public double ApplyReward(int op){
		double reward = GetReward(op);
		UpdateQualityVector(op, reward);
		UpdateProbabilityVector();
		UpdateBestOp(op);
		op_times[op]++;
		times++;
		return reward;
	}
	
	public void UpdateBestOp(int op){
		double exploit_op = quality[op];
		//if the one that was the best has a lower \hat p, check between all; or if the previous best changed
		if((best_op==op && exploit_op < best_quality)||(best_op>=0 && quality[best_op] != best_quality)){
			best_quality = -1; best_op = 0;
			double exploit_aux = -1;
			for(int i=0; i<n_operators; i++){
				exploit_aux = quality[i];
				if(exploit_aux > best_quality){
					best_quality = exploit_aux;
					best_op = i;
				}
			}
		}
		else if (exploit_op >= best_quality){ //else if it is best than the previous best
			best_op = op;
			best_quality = exploit_op;
			
		}
	}
	
	protected int OptionNotTried(){
		
		//check how to handle random numbers here
		int i = rng.nextInt(n_operators);
		for (int j=0; j < n_operators; j++) {
			int k = (i + j) % n_operators;			
			if (op_times[k] == 0){
				//System.out.println("Returned Op. "+k);
				return k;
			}
		}
		System.out.println(" We are in big trouble \n");
		System.exit(1);
		return 0;
	}

	public int SelectOperator(){
		if(times < n_operators)
			return OptionNotTried();
		else{
			int op;
			double sorted = rng.nextDouble();
			double sum = 0;
			for(op = 0; op < n_operators-1; op++){
				sum += prob[op];
				if(sum > sorted){
					//System.out.println("Selected Op. "+op+" from "+n_operators+" operators");
					return op;
				}
			}
			//System.out.println("Selected Op. "+op+" from "+n_operators+" operators");
			return op;
		}
	}
	
	protected void UpdateQualityVector(int op, double reward){
		if(credit[0].getType()<3)
			quality[op] = quality[op] + (alpha * (reward - quality[op]) );
		else
			quality[op] = reward;
	}

	protected void UpdateProbabilityVector(){
		double sum = 0.0;
		for(int i=0; i<n_operators; i++)	
			sum += quality[i];
//		for(int i=0; i<n_operators; i++)
//			prob[i] = p_min + ((1.0 - ((double)n_operators) * p_min) * (quality[i]/sum));
		/*************************************************/
		double [] probs_mike = new double[n_operators];
		for(int i=0; i<n_operators; i++) {
			probs_mike[i] = quality[i]/sum;
		}
		int [] koko = new int[n_operators];int s3 = 0;
		for(int i=0; i<n_operators; i++) {
			koko[i] = (int)Math.round( Math.rint(Math.floor((probs_mike[i]*1000*n_operators))) ) + n_operators;
			s3 += koko[i];
		}
		koko[n_operators-1] = 1000*n_operators - s3;
		mtracker.update_lambda_grad(koko, koko);
		for(int i=0; i<n_operators; i++) {
			prob[i] = (mtracker.p_[i]<0? 0.001:mtracker.p_[i]);
		}
	}
	
	@Override
	public double getProbability(int op) {
		if (op >=0 && op < n_operators)
			return prob[op];
		else return -1.0;
	}
}
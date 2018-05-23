package utils.algorithms.operators.aos;

public class AdaptPursuit extends ProbMatching {
	
	private double beta;
	private double p_max;
	
	public AdaptPursuit(int _n_operators, double _p_min, double _alpha, double _beta, long _seed){
		super(_n_operators, _p_min, _alpha, _seed);
		beta = _beta;
		p_max = 1.0 - (((double)_n_operators - 1.0) * _p_min);		
	//public ProbMatching(int _n_operators, double _p_min, double _alpha, long seed)
	}
	
	@Override
	protected void UpdateProbabilityVector(){
		int opmax=0;
		double qualmax=0.0;
		for(int i=0; i<n_operators; i++)
			if(quality[i]>qualmax){
				qualmax = quality[i];
				opmax=i;
			}
		prob[opmax] = prob[opmax] + ( beta * (p_max-prob[opmax]) );
		for(int i=0; i<n_operators; i++)
			if(i!=opmax)
				prob[i] = prob[i] + ( beta * (p_min-prob[i]) );
	}

}

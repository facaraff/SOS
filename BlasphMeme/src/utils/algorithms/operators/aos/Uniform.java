package utils.algorithms.operators.aos;

public class Uniform extends OperatorSelection {

	protected double[] prob;
	private int[] op_times;
	
	public Uniform(int _n_operators, long _seed) {
		super(_n_operators, _seed);
		
		prob = new double[_n_operators];
		op_times = new int[_n_operators];
		
		for(int i=0; i<_n_operators; i++){
			prob[i] = 1.0/(double)_n_operators;
			op_times[i] = 0;
		}
	}
	
	public double ApplyReward(int op){
		op_times[op]++;
		times++;
		return -1;
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
	
	@Override
	public double getProbability(int op) {
		if (op >=0 && op < n_operators)
			return prob[op];
		else return -1.0;
	}
}
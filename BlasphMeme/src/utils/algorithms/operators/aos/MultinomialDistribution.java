package utils.algorithms.operators.aos;

/**
 * @author mikeagn
 * Multinomial Distribution tracker
 */
public class MultinomialDistribution {
	public int kappa_;              // (number of variables)
	public double n_total_;         // total number of observations
	public double[] n_;             // N : number of observations per variable
	public double[] p_;             // probabilities
	public double[] c_;             // incremental help
	public double lambda_;          // lambda forgetting factor
	public double[] ng_;            // n_k'(T)
	public double delta_;           // lambda = lambda + delta* lambdag
	public boolean debug_;
	public double lambdag_; // gradient of lambda
	public long count_;
	
	public MultinomialDistribution () {
		kappa_ = 0;
		n_total_ = 1.0;
		lambda_ = 0.0;
		delta_ = 1.0e-3;
		debug_ = true;
		lambdag_ = 0.0;
		count_ = 0;
	}
	public MultinomialDistribution (int k) {
		kappa_ = k;
		n_total_ = 1.0;
		if (kappa_>=0) {
			n_ = new double[kappa_];
			p_ = new double[kappa_];
			c_ = new double[kappa_];
			ng_ = new double[kappa_];
		}
		lambda_ = 0.0;
		delta_ = 1.0e-3;
		debug_ = true;
		lambdag_ = 0.0;
		count_ = 0;
		for (int i=0; i<kappa_; i++) {
			p_[i] = 1.0/(1.0*kappa_);
			n_[i] = 1;
			c_[i] = 0;
			ng_[i] = 0;
		}
	}
	
	public MultinomialDistribution (int k, double l0) {
		kappa_ = k;
		n_total_ = 1.0;
		if (kappa_>=0) {
			n_ = new double[kappa_];
			p_ = new double[kappa_];
			c_ = new double[kappa_];
			ng_ = new double[kappa_];
		}
		lambda_ = l0;
		delta_ = 1.0e-3;
		debug_ = true;
		lambdag_ = 0.0;
		count_ = 0;
		for (int i=0; i<kappa_; i++) {
			p_[i] = 1.0/(1.0*kappa_);
			n_[i] = 1;
			c_[i] = 0;
			ng_[i] = 0;
		}
	}

	public void update_normal(int []xin, int []n_in)
	{
        if (xin.equals(null) || n_in.equals(null)) {
        	System.err.println("ERROR x does not exist");
        	throw new IllegalArgumentException("ERROR x does not exist");
        }

        n_total_=0;
        for (int i=0; i<kappa_; i++) {
                n_[i] = n_[i] + n_in[i];
                n_total_ += n_[i];
                c_[i] = c_[i] + xin[i];
        }
        for (int i=0; i<kappa_; i++) {
                //p[i] = c[i]/n[i];
                p_[i] = c_[i]/n_total_;
        }
	}
	
	public void update_lambda(int []xin, int []n_in)
	{
        if (xin.equals(null) || n_in.equals(null)) {
        	System.err.println("ERROR x does not exist");
        	throw new IllegalArgumentException("ERROR x does not exist");
        }
        double dom = 0;
        for (int i=0; i<kappa_; i++) {
                //cout << "llll: " << lambda << " n: " << n[i] << "nn: " << n_in[i] << endl;
                n_[i] = lambda_*n_[i] + n_in[i];
                //cout << "llll: " << lambda << " n: " << n[i] << "nn: " << n_in[i] << endl;
                dom += n_[i];
        }
        for (int i=0; i<kappa_; i++) {
                p_[i] = n_[i]/dom;
        }
	}
	
	public void update_lambda_grad(int []xin, int []n_in)
	{
        if (xin.equals(null) || n_in.equals(null)) {
        	System.err.println("ERROR x does not exist");
        	throw new IllegalArgumentException("ERROR x does not exist");
        }
        //cout << "\n ====================================================" << endl;
        double sum_n = 0.0, sum_ng = 0.0;//, sum_ck=0.0;
        for (int i=0; i<kappa_; i++) {
                ng_[i] = lambda_*ng_[i] + n_[i];
                n_[i]  = lambda_*n_[i] + n_in[i];
                sum_n  += n_[i];
                sum_ng += ng_[i];
                //sum_ck += n_in[i];
                //cout << i << " : ng[i] : " << ng[i] << " : n[i] : " << n[i] << endl;
        }
        //cout << "\n========: " << sum_n << " : " << sum_ng << endl;
        //cout << "PPP\t";double ss=0;
        for (int i=0; i<kappa_; i++) {
                p_[i] = (sum_n == 0 ? 0 : n_[i]/sum_n );
                //p[i] = n[i]/sum_n ;
                //recursively 
                //p[i] = p[i]*(1.0-sum_ck/sum_n) + n_in[i]/sum_n;

                //cout << p[i] << "\t" << p[i]*(1.0-sum_ck/sum_n) + n_in[i]/sum_n <<"\t";
                //koko[i] = koko[i]*(1.0-sum_ck/sum_n) + n_in[i]/sum_n;
                //cout << p[i] << "\t" << koko[i] <<"\t";
                //ss += koko[i];

                if (p_[i]<0) p_[i] = 0;
                if (p_[i]>1.0) p_[i] = 1.0;
        }
        //cout<<"\tss:: " << ss << endl;

        lambdag_ = 0;
        for (int i=0; i<kappa_; i++) {
                lambdag_ += n_in[i] * (ng_[i]/n_[i] - sum_ng/sum_n);
        }
        lambdag_ = -lambdag_;
//      /* Adapt lambda */
//      lambda_ = lambda_ + delta_*lambdag_;
	}
	protected double mysign(double koko) { return (koko>=0 ? 1.0 : -1.0); }
} // end of class

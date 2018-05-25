package applications.students;

import applications.students.ESA_Misc.ESAMisc;
import applications.students.ESA_Misc.ESAProblem;
import interfaces.Problem;

public class Messenger extends Problem
{
    //Input Vector is 18D
    //Custom Bounds per x
    ESAProblem mProblem;
    ESAMisc Misc ;

    public Messenger(int dimension, double[][] bounds) throws Exception
    {
        super(dimension, bounds);
        //
    }

    public double f(double[] x)
    {
        int[] seq = {3, 3, 2, 2, 1};
        mProblem = new ESAProblem(seq);
        Misc = new ESAMisc(mProblem);

        return MGA_DSM(x);
    }

    //state variable    lb      ub      units
    //x(1)(0)	t0	        1000	4000	MJD2000  // Time of departure
    //x(2)(1)	Vinf	    1	    5	    km/sec   // Hyperbolic escape velocity (km/sec)
    //x(3)(2)	u	        0	    1	    n/a      // Hyperbolic escape velocity var1 (non dim)
    //x(4)(3)	v	        0	    1	    n/a      // Hyperbolic escape velocity var2 (non dim)
    //x(5)(4)	T1	        200	    400	    days     // Time of flight //tof[0]
    //x(6)(5)	T2	        30	    400	    days     // Time of flight //tof[1]
    //x(7)(6)	T3	        30	    400	    days     // Time of flight //tof[2]
    //x(8)(7)	T4	        30	    400	    days     // Time of flight //tof[3]
    //x(9)(8)	eta1	    0.01	0.99	days     // alpha[0]
    //x(10)(9)	eta3	    0.01	0.99	n/a      // alpha[1]
    //x(11)(10)	eta3	    0.01	0.99	n/a      // alpha[2]
    //x(12)(11)	eta4	    0.01	0.99	n/a      // alpha[3]
    //x(13)(12)	r_p1	    1.1	    6	    n/a      // rp_non_dim[0] // non-dim perigee fly-by radius of planets P2..Pn(-1) (i=1 refers to the second planet)
    //x(14)(13)	r_p2	    1.1	    6	    n/a      // rp_non_dim[1]
    //x(15)(14)	r_p3	    1.1	    6	    n/a      // rp_non_dim[2]
    //x(16)(15)	b_incl1	    -pi	    pi	    n/a      // gamma[0]
    //x(17)(16)	b_incl2	    -pi	    pi	    n/a      // gamma[1]
    //x(18)(17)	b_incl3	    -pi	    pi	    n/a      // gamma[2]
    //
    private double MGA_DSM(double[] t)
    {
        final int n = 5;
        int i; //loop counter

        double[][][] tmp =  Misc.precalculate_ers_and_vees(t);
        mProblem.r = tmp[0];
        mProblem.v = tmp[1];
        //mProblem.DV =

        //inter-hop velocities
        double[] inter_pl_in_v = new double[3];
        double[] inter_pl_out_v = new double[3];

        // FIRST BLOCK
        double[][] tmp1 = Misc.first_block(t, mProblem.r, mProblem.v, mProblem.DV);

        mProblem.DV = tmp1[0];
        inter_pl_out_v = tmp1[0];

        // INTERMEDIATE BLOCK
        for (int i_count=0; i_count < n - 2; i_count++)
        {
            //copy previous output velocity to current input velocity
            inter_pl_in_v[0] = inter_pl_out_v[0];
            inter_pl_in_v[1] = inter_pl_out_v[1];
            inter_pl_in_v[2] = inter_pl_out_v[2];

           double[][] tmp2  = Misc.intermediate_block(t, mProblem.r, mProblem.v, i_count, inter_pl_in_v, mProblem.DV, inter_pl_out_v);

           mProblem.DV = tmp2[0];
           inter_pl_out_v = tmp2[1];

        }

        //copy previous output velocity to current input velocity
        inter_pl_in_v[0] = inter_pl_out_v[0];
        inter_pl_in_v[1] = inter_pl_out_v[1];
        inter_pl_in_v[2] = inter_pl_out_v[2];

        // FINAL BLOCK
        mProblem.DV = Misc.final_block(mProblem.r, mProblem.v, inter_pl_in_v, mProblem.DV);

        // **************************************************************************
        // Evaluation of total DV spent by the propulsion system
        // **************************************************************************
        double DVtot = 0.0;

        for (i = 0; i < n; i++)
        {
            DVtot += mProblem.DV[i];
        }

	    final double VINF = t[1];         // Variable renaming: Hyperbolic escape velocity (km/sec)

        for (i = n; i > 0; i--)
        {
            mProblem.DV[i] = mProblem.DV[i - 1];
        }

        mProblem.DV[0] = VINF;

        return (DVtot + VINF);

    }

}

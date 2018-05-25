package applications.students.ESA_Misc;

import applications.students.ESA_Misc.ESAProblem;

import java.util.Arrays;

public class ESAMisc
{
    ESAProblem problem;

    // Gravitational Constants
    final static double[] MU = {
            1.32712428e11,          // SUN                                  = 0
            22321,					// Gravitational constant of Mercury	= 1
            324860,					// Gravitational constant of Venus		= 2
            398601.19,				// Gravitational constant of Earth		= 3
            42828.3,				// Gravitational constant of Mars		= 4
            126.7e6,				// Gravitational constant of Jupiter	= 5
            0.37939519708830e8,		// Gravitational constant of Saturn		= 6
            5.78e6,					// Gravitational constant of Uranus		= 7
            6.8e6					// Gravitational constant of Neptune	= 8
    };

    //  Definition of planetaru radii
    final static double[] RPL = {
            2440,   // Mercury
            6052,   // Venus
            6378,   // Earth
            3397,   // Mars
            71492,  // Jupiter
            60330   // Saturn
    };


    public ESAMisc(ESAProblem problem)
    {
        this.problem = problem;

    }


    public double[] zeroArray(double[] input)
    {
        for (int i=0;i < input.length;i++)
        {
            input[i] = 0.0;
        }

        return input;
    }

    public double[][] zeroArray(double[][] input)
    {
        for (int i=0;i < input.length;i++)
        {
            for (int j=0;j < input[i].length;j++)
            {
                input[i][j] = 0.0;
            }
        }

        return input;
    }

    //
    public double[] VectorNormalise(double[] in)
    {
        double[] out = new double[3];
        double norm = Norm2(in);

        for(int i = 0; i < 3; i++) {
            out[i] = in[i] / norm;
        }

        return out;
    }

    //
    public static double Norm2(double[] vet1)
    {
        double temp = 0.0;
        for (int i = 0; i < 3; i++)
        {
            temp += vet1[i] * vet1[i];
        }
        return Math.sqrt(temp);
    }

    /**
     * Get gravitational constant of an celestial object of interest.
     *
     * i_count - hop number (starting from 0)
     */
    public double get_celobj_mu(int i_count)
    {
        return MU[i_count];
    }

    /**
     * Precomputes all velocities and positions of celestial objects of interest for the problem.
     * Before calling this function, r and v vectors must be pre-allocated with sufficient amount of entries.
     *
     * problem - concerned problem
     * r       - [output] array of position vectors
     * v       - [output] array of velocity vectors
     */
    public double[][][] precalculate_ers_and_vees(double[] t)
    {
        double T = t[0]; //time of departure

        double[][] r = new double[5][3];
        double[][] v = new double[5][3];

        for(int i_count = 0; i_count < this.problem.sequence.length; i_count++)
        {
           double[][] tmp = get_celobj_r_and_v(T, i_count);
           r[i_count] = tmp[0];
           v[i_count] = tmp[1];

            T += t[4 + i_count]; //time of flight
        }

        double[][][] tmp = {r, v};

        return tmp;

    }
    // FIRST BLOCK (P1 to P2)
    /**
     * t          - decision vector
     * problem    - problem parameters
     * r          - planet positions
     * v          - planet velocities
     * DV         - [output] velocity contributions table
     * v_sc_pl_in - [output] next hop input speed
     */
    public double[][] first_block(double[] t, double[][] r, double[][] v, double[] DVin)
    {
        double[] DV = DVin;
        double[] v_sc_nextpl_in  = new double[3];

        //First, some helper constants to make code more readable
        final int n = problem.sequence.length;
        final double VINF = t[1];         // Hyperbolic escape velocity (km/sec)
        final double udir = t[2];         // Hyperbolic escape velocity var1 (non dim)
        final double vdir = t[3];         // Hyperbolic escape velocity var2 (non dim)

        final double[] tof = Arrays.copyOfRange(t,4,8);        // const double *tof = &t[4]; pointer to a double at the address of t[4] //makes tof = t[4] to t[17]
        final double[] alpha = Arrays.copyOfRange(t,8,12);    //

        int i; //loop counter

        // Spacecraft position and velocity at departure
        double[] vtemp = new double[3];
        vtemp = Cross(r[0], v[0]);

        double[] zP1 = new double[3];
        zP1 = VectorNormalise(vtemp);

        double[] iP1  = new double[3];
        iP1 = VectorNormalise(v[0]);

        double[] jP1 = new double[3];
        jP1 = Cross(zP1, iP1);

        double theta, phi;
        theta = 2 * Math.PI * udir;             // See Picking a Point on a Sphere
        phi = Math.acos(2 * vdir - 1) - Math.PI / 2; // In this way: -pi/2<phi<pi/2 so phi can be used as out-of-plane rotation

        double[] vinf = new double[3];
        for (i = 0; i < 3; i++)
            vinf[i] = VINF * (Math.cos(theta) * Math.cos(phi) * iP1[i] + Math.sin(theta) * Math.cos(phi) * jP1[i] + Math.sin(phi) * zP1[i]);

        double[] v_sc_pl_out = new double[3]; // Spacecraft absolute outgoing velocity at P1
        for (i = 0; i < 3; i++)
        {
            v_sc_pl_out[i] = v[0][i] + vinf[i];
        }

        // Computing S/C position and absolute incoming velocity at DSM1
        double[] rd = new double[3];
        double[] v_sc_dsm_in = new double[3];

        double[][] tmp = PropagateKEP(r[0], v_sc_pl_out, alpha[0] * tof[0] * 86400, MU[0]);
        rd = tmp[0];
        v_sc_dsm_in = tmp[1];

        // Evaluating the Lambert arc from DSM1 to P2
        double[] Dum_Vec = new double[3]; // [MR] Rename it to something sensible...
        Dum_Vec = vett(rd, r[1]);

        int lw = (Dum_Vec[2] > 0) ? 0 : 1;
       //double a, p, theta2;

        double[] v_sc_dsm_out = new double[3]; // DSM output speed

        double[][] params = LambertI(rd, r[1], tof[0] * (1 - alpha[0]) * 86400, MU[0], lw,v_sc_dsm_out, v_sc_nextpl_in);

        v_sc_dsm_out = params[0];
        v_sc_nextpl_in = params[1];
        //a = params[2][0];
        //p = params[3][0];
        //theta2 = params[4][0];
        //iter_unused = (int)params[5][0];

        // First Contribution to DV (the 1st deep space maneuver)
        for (i = 0; i < 3; i++)
        {
            Dum_Vec[i] = v_sc_dsm_out[i] - v_sc_dsm_in[i]; // [MR] Temporary variable reused. Dirty.
        }

        DV[0] = Norm2(Dum_Vec);

        double[][] tmp_out = {DV, v_sc_nextpl_in};

        return tmp_out;
    }

    // INTERMEDIATE BLOCK
    public double[][] intermediate_block(double[] t, double[][] r, double[][] v, int i_count, double[] v_sc_pl_in, double[] DVin, double[] v_sc_nextpl_in)
    {
        double[] DV = DVin;

        //[MR] A bunch of helper variables to simplify the code
        final int n = problem.sequence.length;
        final double[] tof = Arrays.copyOfRange(t,4,8);
        final double[] alpha = Arrays.copyOfRange(t,8,12);
        final double[] rp_non_dim = Arrays.copyOfRange(t,((2*n)+2),15); // non-dim perigee fly-by radius of planets P2..Pn(-1) (i=1 refers to the second planet)
        final double[] gamma = Arrays.copyOfRange(t,(3*n),18);        // rotation of the bplane-component of the swingby outgoing
        final int[] sequence = problem.sequence;

        int i; //loop counter

        // Evaluation of the state immediately after Pi
        double[] v_rel_in = new double[3];
        double vrelin = 0.0;

        for (i = 0; i < 3; i++)
        {
            v_rel_in[i] = v_sc_pl_in[i] - v[i_count+1][i];
            vrelin += v_rel_in[i] * v_rel_in[i];
        }

        // Hop object's gravitional constant
        double hopobj_mu = get_celobj_mu(i_count + 1);

        double e = 1.0 + rp_non_dim[i_count] * RPL[sequence[i_count + 1] - 1] * vrelin / hopobj_mu;

        double beta_rot = 2 * Math.asin(1 / e); // velocity rotation

        double[] ix = new double[3];
        ix = VectorNormalise(v_rel_in);

        double[] vpervnorm = new double[3];
        vpervnorm = VectorNormalise(v[i_count+1]);

        double[] iy = new double[3];
        iy = vett(ix, vpervnorm);
        iy = VectorNormalise(iy);

        double[] iz = new double[3];
        iz = vett(ix, iy);

        double v_rel_in_norm = Norm2(v_rel_in);
        double[] v_sc_pl_out  = new double[3]; // TODO: document me!

        for (i = 0; i < 3; i++)
        {
            double iVout = Math.cos(beta_rot) * ix[i] + Math.cos(gamma[i_count]) * Math.sin(beta_rot) * iy[i] + Math.sin(gamma[i_count]) * Math.sin(beta_rot) * iz[i];
            double v_rel_out = v_rel_in_norm * iVout;
            v_sc_pl_out[i] = v[i_count + 1][i] + v_rel_out;
        }

        // Computing S/C position and absolute incoming velocity at DSMi
        double[] rd = new double[3];
        double[] v_sc_dsm_in = new double[3];

        double[][] tmp = PropagateKEP(r[i_count + 1], v_sc_pl_out, alpha[i_count+1] * tof[i_count+1] * 86400, MU[0]);
        rd = tmp[0];
        v_sc_dsm_in = tmp[1];

        // Evaluating the Lambert arc from DSMi to Pi+1
        double[] Dum_Vec = new double[3]; // [MR] Rename it to something sensible...
        Dum_Vec = vett(rd, r[i_count + 2]);

        int lw = (Dum_Vec[2] > 0) ? 0 : 1;
        double a, p, theta;

        double[] v_sc_dsm_out = new double[3]; // DSM output speed

        double[][] params = LambertI(rd, r[i_count + 2], tof[i_count + 1] * (1 - alpha[i_count + 1]) * 86400, MU[0], lw,v_sc_dsm_out, v_sc_nextpl_in);

        v_sc_dsm_out = params[0];
        v_sc_nextpl_in = params[1];
        //a = params[2][0];
        //p = params[3][0];
        //theta = params[4][0];
        //iter_unused = (int)params[5][0];

        // DV contribution
        for (i = 0; i < 3; i++) {
            Dum_Vec[i] = v_sc_dsm_out[i] - v_sc_dsm_in[i]; // [MR] Temporary variable reused. Dirty.
        }

        DV[i_count + 1] = Norm2(Dum_Vec);

        double[][] tmp_out = {DV, v_sc_nextpl_in};

        return tmp_out;
    }

    // FINAL BLOCK
    public double[] final_block(double[][] r, double[][] v, double v_sc_pl_in[], double[] DVin)
    {
        double[] DV = DVin;
	    final int n = problem.sequence.length;

        // Evaluation of the arrival DV
        double[] Dum_Vec = new double[3];
        for (int i = 0; i < 3; i++)
        {
            Dum_Vec[i] = v[n-1][i] - v_sc_pl_in[i];
        }

        double DVrel, DVarr;
        DVrel = Norm2(Dum_Vec);  // Relative velocity at target planet
        DVarr = DVrel;

        DV[n - 1] = DVarr;

        return DV;
    }

    /*
     Origin: MATLAB code programmed by Dario Izzo (ESA/ACT)

     C++ version by Tamas Vinko (ESA/ACT)

     Inputs:
               r0:    column vector for the non dimensional position
               v0:    column vector for the non dimensional velocity
               t:     non dimensional time

     Outputs:
               r:    column vector for the non dimensional position
               v:    column vector for the non dimensional velocity

     Comments:  The function works in non dimensional units, it takes an
     initial condition and it propagates it as in a kepler motion analytically.
    */

    private double[][] PropagateKEP(double[] r0_in, double[] v0_in, double t, double mu)
    {
        /*
           The matrix DD will be almost always the unit matrix, except for orbits
           with little inclination in which cases a rotation is performed so that
           par2IC is always defined
        */

        double[] r = new double[3];
        double[] v = new double[3];

        double[] DD = {1, 0, 0, 0, 1, 0, 0, 0, 1};

        double[] h = new double[3];
        double[] ih = {0,0,0};
        double[] temp1  = {0,0,0};
        double[] temp2 = {0,0,0};
        double[] E = new double[6];
        double normh, M, M0;
        double[] r0 = new double[3];
        double[] v0 = new double[3];

        int i;

        for (i=0; i<3; i++)
        {
            r0[i] = r0_in[i];
            v0[i] = v0_in[i];
        }

        h = vett(r0, v0);

        normh = Norm2(h);

        for (i=0; i<3; i++)
            ih[i] = h[i]/normh;

        if (Math.abs(Math.abs(ih[2])-1.0) < 1e-3)     // the abs is needed in cases in which the orbit is retrograde,
        {                                 // that would held ih=[0,0,-1]!!
            DD[0] = 1; DD[1] =  0;  DD[2] = 0;
            DD[3] = 0; DD[4] =  0;  DD[5] = 1;
            DD[6] = 0; DD[7] = -1; DD[8] = 0;

            // Random rotation matrix that make the Euler angles well defined for the case
            // For orbits with little inclination another ref. frame is used.

            for (int j=0; j<3; j++)
            {
                temp1[0] += DD[j]*r0[j];
                temp1[1] += DD[j+3]*r0[j];
                temp1[2] += DD[j+6]*r0[j];
                temp2[0] += DD[j]*v0[j];
                temp2[1] += DD[j+3]*v0[j];
                temp2[2] += DD[j+6]*v0[j];
            }
            for (int j=0; j<3; j++)
            {
                r0[j] = temp1[j];
                temp1[j] = 0.0;
                v0[j] = temp2[j];
                temp2[j] = 0.0;
            }
            // for practical reason we take the transpose of the matrix DD here (will be used at the end of the function)
            DD[0] = 1; DD[1] =  0;  DD[2] = 0;
            DD[3] = 0; DD[4] =  0;  DD[5] = -1;
            DD[6] = 0; DD[7] =  1;  DD[8] = 0;
        }

        E = IC2par(r0, v0, mu);
        if (E[1] < 1.0)
        {
            M0 = E[5] - E[1]*Math.sin(E[5]);
            M=M0+Math.sqrt(mu/Math.pow(E[0],3))*t;
        }
        else
        {
            M0 = E[1]*Math.tan(E[5]) - Math.log(Math.tan(0.5*E[5] + 0.25*Math.PI));
            M=M0+Math.sqrt(mu/Math.pow(-E[0],3))*t;
        }

        E[5]=Mean2Eccentric(M, E[1]);
        double[][] tmp1 = par2IC(E, mu);
        r = tmp1[0];
        v = tmp1[1];

        for (int j=0; j<3; j++)
        {
            temp1[0] += DD[j]*r[j];
            temp1[1] += DD[j+3]*r[j];
            temp1[2] += DD[j+6]*r[j];
            temp2[0] += DD[j]*v[j];
            temp2[1] += DD[j+3]*v[j];
            temp2[2] += DD[j+6]*v[j];
        }
        for (int k=0; k<3; k++)
        {
            r[k] = temp1[k];
            v[k] = temp2[k];
        }

        double[][] tmp2 = {r, v};

        return tmp2;
    }

    /*
	Origin: MATLAB code programmed by Dario Izzo (ESA/ACT)

	C++ version by Tamas Vinko (ESA/ACT) 12/09/2006

	Inputs:
           r0:    column vector for the position
           v0:    column vector for the velocity

	Outputs:
           E:     Column Vectors containing the six keplerian parameters,
                  (a,e,i,OM,om,Eccentric Anomaly (or Gudermannian whenever e>1))

	Comments:  The parameters returned are, of course, referred to the same
	ref. frame in which r0,v0 are given. Units have to be consistent, and
	output angles are in radians
	The algorithm used is quite common and can be found as an example in Bate,
	Mueller and White. It goes singular for zero inclination
*/

    private double[] IC2par(double[] r0, double[] v0, double mu)
    {
        double[] E = new double[6];

        double[] k = new double[3];
        double[] h = new double[3];
        double[] Dum_Vec = new double[3];
        double[] n = new double[3];
        double[] evett = new double[3];

        double p = 0.0;
        double temp =0.0;
        double R0, ni;
        int i;

        h = vett(r0, v0);

        for (i=0; i<3; i++)
            p += h[i]*h[i];

        p/=mu;

        k[0] = 0; k[1] = 0; k[2] = 1;
        n = vett(k, h);

        for (i=0; i<3; i++)
            temp += Math.pow(n[i], 2);

        temp = Math.sqrt(temp);

        for (i=0; i<3; i++)
            n[i] /= temp;

        R0 = Norm2(r0);

        Dum_Vec = vett(v0, h);

        for (i=0; i<3; i++)
            evett[i] = Dum_Vec[i]/mu - r0[i]/R0;

        double e = 0.0;
        for (i=0; i<3; i++)
            e += Math.pow(evett[i], 2);

        E[0] = p/(1-e);
        E[1] = Math.sqrt(e);
        e = E[1];

        E[2] = Math.acos(h[2]/Norm2(h));

        temp = 0.0;
        for (i=0; i<3; i++)
            temp+=n[i]*evett[i];

        E[4] = Math.acos(temp/e);

        if (evett[2] < 0) E[4] = 2*Math.PI - E[4];

        E[3] = Math.acos(n[0]);
        if (n[1] < 0) E[3] = 2*Math.PI-E[3];

        temp = 0.0;
        for (i=0; i<3; i++)
            temp+=evett[i]*r0[i];

        ni = Math.acos(temp/e/R0);  // danger, the argument could be improper.

        temp = 0.0;
        for (i=0; i<3; i++)
            temp+=r0[i]*v0[i];

        if (temp<0.0) ni = 2*Math.PI - ni;

        if (e<1.0)
            E[5] = 2.0*Math.atan(Math.sqrt((1-e)/(1+e))*Math.tan(ni/2.0));  // algebraic kepler's equation
        else
            E[5] = 2.0*Math.atan(Math.sqrt((e-1)/(e+1))*Math.tan(ni/2.0));   // algebraic equivalent of kepler's equation in terms of the Gudermannian

        return E;
    }

/*
	Origin: MATLAB code programmed by Dario Izzo (ESA/ACT)

	C++ version by Tamas Vinko (ESA/ACT)

	Usage: [r0,v0] = IC2par(E,mu)

	Outputs:
           r0:    column vector for the position
           v0:    column vector for the velocity

	Inputs:
           E:     Column Vectors containing the six keplerian parameters,
                  (a (negative for hyperbolas),e,i,OM,om,Eccentric Anomaly)
           mu:    gravitational constant

	Comments:  The parameters returned are, of course, referred to the same
	ref. frame in which r0,v0 are given. a can be given either in kms or AUs,
	but has to be consistent with mu.All the angles must be given in radians.
	This function does work for hyperbolas as well.
*/

    private double[][] par2IC(double[] E, double mu)
    {
        double[] r0 = new double[3];
        double[] v0 = new double[3];

        double a = E[0];
        double e = E[1];
        double i = E[2];
        double omg = E[3];
        double omp = E[4];
        double EA = E[5];
        double b, n, xper, yper, xdotper, ydotper;
        double[][] R = new double[3][3];
        double cosomg, cosomp, sinomg, sinomp, cosi, sini;
        double dNdZeta;

        // Grandezze definite nel piano dell'orbita
        if (e<1.0)
        {
            b = a*Math.sqrt(1-e*e);
            n = Math.sqrt(mu/(a*a*a));
            xper=a*(Math.cos(EA)-e);
            yper=b*Math.sin(EA);

            xdotper = -(a*n*Math.sin(EA))/(1-e*Math.cos(EA));
            ydotper=(b*n*Math.cos(EA))/(1-e*Math.cos(EA));
        }
        else
        {
            b = -a*Math.sqrt(e*e-1);
            n = Math.sqrt(-mu/(a*a*a));

            dNdZeta = e * (1+Math.tan(EA)*Math.tan(EA))-(0.5+0.5*Math.pow(Math.tan(0.5*EA + 0.25*Math.PI),2))/Math.tan(0.5*EA+ 0.25*Math.PI);

            xper = a/Math.cos(EA) - a*e;
            yper = b*Math.tan(EA);

            xdotper = a*Math.tan(EA)/Math.cos(EA)*n/dNdZeta;
            ydotper = b/Math.pow(Math.cos(EA), 2)*n/dNdZeta;
        }

        // Matrice di trasformazione da perifocale a ECI

        cosomg = Math.cos(omg);
        cosomp = Math.cos(omp);
        sinomg = Math.sin(omg);
        sinomp = Math.sin(omp);
        cosi = Math.cos(i);
        sini = Math.sin(i);


        R[0][0]=cosomg*cosomp-sinomg*sinomp*cosi;
        R[0][1]=-cosomg*sinomp-sinomg*cosomp*cosi;
        R[0][2]=sinomg*sini;
        R[1][0]=sinomg*cosomp+cosomg*sinomp*cosi;
        R[1][1]=-sinomg*sinomp+cosomg*cosomp*cosi;
        R[1][2]=-cosomg*sini;
        R[2][0]=sinomp*sini;
        R[2][1]=cosomp*sini;
        R[2][2]=cosi;

        double[] temp = {xper, yper, 0.0};
        double[] temp2 = {xdotper, ydotper, 0};

        for (int j = 0; j<3; j++)
        {
            r0[j] = 0.0;
            v0[j] = 0.0;
            for (int k = 0; k<3; k++)
            {
                r0[j]+=R[j][k]*temp[k];
                v0[j]+=R[j][k]*temp2[k];
            }
        }

        double[][] tmp = {r0, v0};

        return tmp;
    }

    // Returns the cross product of the vectors X and Y.
// That is, z = X x Y.  X and Y must be 3 element
// vectors.
    private double[] Cross(double[] x, double[] y)
    {
        double[] z = new double[3];

        z[0] = x[1]*y[2] - x[2]*y[1];
        z[1] = x[2]*y[0] - x[0]*y[2];
        z[2] = x[0]*y[1] - x[1]*y[0];

        return z;
    }



    private double Mean2Eccentric (double M, double e)
    {
        double tolerance = 1e-13;
        int n_of_it = 0; // Number of iterations
        double Eccentric,Ecc_New;
        double err = 1.0;

        if (e < 1.0)
        {
            Eccentric = M + e* Math.cos(M); // Initial guess
            while ( (err > tolerance) && (n_of_it < 100))
            {
                Ecc_New = Eccentric - (Eccentric - e* Math.sin(Eccentric) -M )/(1.0 - e * Math.cos(Eccentric));
                err = Math.abs(Eccentric - Ecc_New);
                Eccentric = Ecc_New;
                n_of_it++;
            }
        }
        else
        {
            CZF FF = new CZF();  // function to find its zero point
            FZero fz = new FZero(-0.5*3.14159265358979 + 1e-8, 0.5*3.14159265358979-1e-8);
            FF.SetParameters(e, M);
            Ecc_New = fz.FindZero(FF);
            Eccentric = Ecc_New;
        }
        return Eccentric;
    }

    /**
     * Compute velocity and position of an celestial object of interest at specified time.
     *
     * problem - concerned problem
     * T       - time
     * i_count - hop number (starting from 0)
     * r       - [output] object's position
     * v       - [output] object's velocity
     */
    private double[][] get_celobj_r_and_v(double T, int i_count)
    {
        //if (problem.sequence[i_count] < 10) { //normal planet
            return Planet_Ephemerides_Analytical (T, problem.sequence[i_count]); // r and v in heliocentric coordinate system
       // }
    }

    //
    private double[] PowSwingByInv (double Vin, double Vout, double alpha)
    {
        final int maxiter = 30;
        int i = 0;
        double err = 1.0;
        double f,df;                    // function and its derivative
        double rp_new;
	    final double tolerance = 1e-8;

        double aIN  = 1.0/Math.pow(Vin,2);     // semimajor axis of the incoming hyperbola
        double aOUT = 1.0/Math.pow(Vout,2);    // semimajor axis of the incoming hyperbola

        double DV;
        double rp = 1.0;

        while ((err > tolerance)&&(i < maxiter))
        {
            i++;
            f = Math.asin(aIN/(aIN + rp)) + Math.asin(aOUT/(aOUT + rp)) - alpha;
            df = -aIN/Math.sqrt((rp + 2 * aIN) * rp)/(aIN+rp) -
                    aOUT/Math.sqrt((rp + 2 * aOUT) * rp)/(aOUT+rp);
            rp_new = rp - f/df;

            if (rp_new > 0)
            {
                err = Math.abs(rp_new - rp);
                rp = rp_new;
            }
            else {
                rp /= 2.0;
            }
        }

        // Evaluation of the DV
        DV = Math.abs(Math.sqrt(Vout*Vout + (2.0/rp)) - Math.sqrt(Vin*Vin + (2.0/rp)));

        double[] tmp = {DV, rp};

        return tmp;
    }

    // ------------------------------------------------------------------------ //
    // This source file is part of the 'ESA Advanced Concepts Team's			//
    // Space Mechanics Toolbox' software.                                       //
    //                                                                          //
    // The source files are for research use only,                              //
    // and are distributed WITHOUT ANY WARRANTY. Use them on your own risk.     //
    //                                                                          //
    // Copyright (c) 2004-2007 European Space Agency                            //
    // ------------------------------------------------------------------------ //


    /*
     This routine implements a new algorithm that solves Lambert's problem. The
     algorithm has two major characteristics that makes it favorable to other
     existing ones.

       1) It describes the generic orbit solution of the boundary condition
       problem through the variable X=log(1+cos(alpha/2)). By doing so the
       graphs of the time of flight become defined in the entire real axis and
       resembles a straight line. Convergence is granted within few iterations
       for all the possible geometries (except, of course, when the transfer
       angle is zero). When multiple revolutions are considered the variable is
       X=tan(cos(alpha/2)*pi/2).

       2) Once the orbit has been determined in the plane, this routine
       evaluates the velocity vectors at the two points in a way that is not
       singular for the transfer angle approaching to pi (Lagrange coefficient
       based methods are numerically not well suited for this purpose).

       As a result Lambert's problem is solved (with multiple revolutions
       being accounted for) with the same computational effort for all
       possible geometries. The case of near 180 transfers is also solved
       efficiently.

       We note here that even when the transfer angle is exactly equal to pi
       the algorithm does solve the problem in the plane (it finds X), but it
       is not able to evaluate the plane in which the orbit lies. A solution
       to this would be to provide the direction of the plane containing the
       transfer orbit from outside. This has not been implemented in this
       routine since such a direction would depend on which application the
       transfer is going to be used in.

     Inputs:
               r1=Position vector at departure (column)
               r2=Position vector at arrival (column, same units as r1)
               t=Transfer time (scalar)
               mu=gravitational parameter (scalar, units have to be consistent with r1,t units)
               lw=1 if long way is chosen


     Outputs:
               v1=Velocity at departure        (consistent units)
               v2=Velocity at arrival
               a=semi major axis of the solution
               p=semi latus rectum of the solution
               theta=transfer angle in rad
               iter=number of iteration made by the newton solver (usually 6)
    */

    private double[][] LambertI (double[] r1_in, double[] r2_in, double t, double mu, int lw, double[] v_sc_dsm_out, double[] v_sc_nextpl_in)
    {
        double[] v1 = v_sc_dsm_out;
        double[] v2 = v_sc_nextpl_in;
        double a, p, theta, iter;

        double	V,T,
                r2_mod = 0.0,    // R2 module
                dot_prod = 0.0, // dot product
                c,		        // non-dimensional chord
                s,		        // non dimensional semi-perimeter
                am,		        // minimum energy ellipse semi major axis
                lambda,	        //lambda parameter defined in Battin's Book
                x,x1,x2,y1,y2,x_new=0,y_new,err,alfa,beta,psi,eta,eta2,sigma1,vr1,vt1,vt2,vr2,R=0.0;
        int i_count, i;
	    final double tolerance = 1e-11;
        double[] r1 = new double[3];
        double[] r2= new double[3];
        double[] r2_vers = new double[3];

        double[] ih_dum = new double[3];
        double[] ih = new double[3];
        double[] dum = new double[3];

        // Increasing the tolerance does not bring any advantage as the
        // precision is usually greater anyway (due to the rectification of the tof
        // graph) except near particular cases such as parabolas in which cases a
        // lower precision allow for usual convergence.

        if (t <= 0)
        {
           // double[][] error = new double[1][1];
            double[][] error = {
                    v1,
                    v2,
            };
        //    //"ERROR in Lambert Solver: Negative Time in input."
           return error;
        }

        for (i = 0; i < 3; i++)
        {
            r1[i] = r1_in[i];
            r2[i] = r2_in[i];
            R += r1[i]*r1[i];
        }

        R = Math.sqrt(R);
        V = Math.sqrt(mu/R);
        T = R/V;

        // working with non-dimensional radii and time-of-flight
        t /= T;
        for (i = 0;i <3;i++)  // r1 dimension is 3
        {
            r1[i] /= R;
            r2[i] /= R;
            r2_mod += r2[i]*r2[i];
        }

        // Evaluation of the relevant geometry parameters in non dimensional units
        r2_mod = Math.sqrt(r2_mod);

        for (i = 0;i < 3;i++)
            dot_prod += (r1[i] * r2[i]);

        theta = Math.acos(dot_prod/r2_mod);

        if (lw == 1) {
            theta=2*Math.acos(-1.0)-theta;
        }

        c = Math.sqrt(1 + r2_mod*(r2_mod - 2.0 * Math.cos(theta)));
        s = (1 + r2_mod + c)/2.0;
        am = s/2.0;
        lambda = Math.sqrt (r2_mod) * Math.cos (theta/2.0)/s;

        // We start finding the log(x+1) value of the solution conic:
        // NO MULTI REV --> (1 SOL)
        //	inn1=-.5233;    //first guess point
        //  inn2=.5233;     //second guess point
        x1=Math.log(0.4767);
        x2=Math.log(1.5233);
        y1=Math.log(x2tof(-.5233,s,c,lw)) - Math.log(t);
        y2=Math.log(x2tof(.5233,s,c,lw)) - Math.log(t);

        // Newton iterations
        err=1;
        i_count=0;
        while ((err>tolerance) && (y1 != y2))
        {
            i_count++;
            x_new=(x1*y2-y1*x2)/(y2-y1);
            y_new=Math.log(x2tof(Math.exp(x_new)-1,s,c,lw))- Math.log(t);
            x1=x2;
            y1=y2;
            x2=x_new;
            y2=y_new;
            err = Math.abs(x1-x_new);
        }
        iter = i_count;
        x = Math.exp(x_new)-1; //[MR] Same here... expf -> exp

        // The solution has been evaluated in terms of log(x+1) or tan(x*pi/2), we
        // now need the conic. As for transfer angles near to pi the lagrange
        // coefficient technique goes singular (dg approaches a zero/zero that is
        // numerically bad) we here use a different technique for those cases. When
        // the transfer angle is exactly equal to pi, then the ih unit vector is not
        // determined. The remaining equations, though, are still valid.

        a = am/(1 - x*x);

        // psi evaluation
        if (x < 1)  // ellipse
        {
            beta = 2 * Math.asin (Math.sqrt( (s-c)/(2*a) ));
            if (lw == 1) { beta = -beta; }
            alfa=2*Math.acos(x);
            psi=(alfa-beta)/2;
            eta2=2*a*Math.pow(Math.sin(psi),2)/s;
            eta=Math.sqrt(eta2);
        }
        else       // hyperbola
        {
            beta = 2*asinh(Math.sqrt((c-s)/(2*a)));
            if (lw == 1) {beta = -beta; }
            alfa = 2*acosh(x);
            psi = (alfa-beta)/2;
            eta2 = -2 * a * Math.pow(Math.sinh(psi),2)/s;
            eta = Math.sqrt(eta2);
        }

        // parameter of the solution
        p = ( r2_mod / (am * eta2) ) * Math.pow (Math.sin (theta/2),2);
        sigma1 = (1/(eta * Math.sqrt(am)) )* (2 * lambda * am - (lambda + x * eta));
        ih_dum = vett(r1,r2);
        ih = vers(ih_dum) ;

        if (lw == 1)
        {
            for (i = 0; i < 3;i++) {
                ih[i] = -ih[i];
            }
        }

        vr1 = sigma1;
        vt1 = Math.sqrt(p);
        dum = vett(ih,r1);

        for (i = 0;i < 3 ;i++) {
            v1[i] = vr1 * r1[i] + vt1 * dum[i];
        }

        vt2 = vt1 / r2_mod;
        vr2 = -vr1 + (vt1 - vt2)/Math.tan(theta/2);
        r2_vers = vers(r2);
        dum = vett(ih,r2_vers);
        for (i = 0;i < 3 ;i++)
            v2[i] = vr2 * r2[i] / r2_mod + vt2 * dum[i];

        for (i = 0;i < 3;i++)
        {
            v1[i] *= V;
            v2[i] *= V;
        }
        a *= R;
        p *= R;

        /*
        v1=Velocity at departure        (consistent units)
        v2=Velocity at arrival
        a=semi major axis of the solution
        p=semi latus rectum of the solution
        theta=transfer angle in rad
        iter=number of iteration made by the newton solver (usually 6)
        */

        double[][] tmp = {
                            v1,
                            v2,
                            {a},
                            {p},
                            {theta},
                            {iter}
                         };
        return tmp;
    }

    private double[][] Planet_Ephemerides_Analytical(double mjd2000, int planet)
    {
	    final double pi = Math.acos(-1.0);
	    final double RAD = pi/180.0;
	    final double AU = 149597870.66; // Astronomical Unit
        final double KM = AU;
	    final double MuSun = 1.327124280000000e+011;  //Gravitational constant of Sun);

        double[] Kepl_Par = new double[6];
        double XM;

        double T =(mjd2000 + 36525.00)/36525.00;

        switch (planet)
        {
            case(1):// Mercury
                Kepl_Par[0]=(0.38709860);
                Kepl_Par[1]=(0.205614210 + 0.000020460*T - 0.000000030*T*T);
                Kepl_Par[2]=(7.002880555555555560 + 1.86083333333333333e-3*T - 1.83333333333333333e-5*T*T);
                Kepl_Par[3]=(4.71459444444444444e+1 + 1.185208333333333330*T + 1.73888888888888889e-4*T*T);
                Kepl_Par[4]=(2.87537527777777778e+1 + 3.70280555555555556e-1*T +1.20833333333333333e-4*T*T);
                XM   = 1.49472515288888889e+5 + 6.38888888888888889e-6*T;
                Kepl_Par[5]=(1.02279380555555556e2 + XM*T);
                break;
            case(2):// Venus
                Kepl_Par[0]=(0.72333160);
                Kepl_Par[1]=(0.006820690 - 0.000047740*T + 0.0000000910*T*T);
                Kepl_Par[2]=(3.393630555555555560 + 1.00583333333333333e-3*T - 9.72222222222222222e-7*T*T);
                Kepl_Par[3]=(7.57796472222222222e+1 + 8.9985e-1*T + 4.1e-4*T*T);
                Kepl_Par[4]=(5.43841861111111111e+1 + 5.08186111111111111e-1*T -1.38638888888888889e-3*T*T);
                XM =5.8517803875e+4 + 1.28605555555555556e-3*T;
                Kepl_Par[5]=(2.12603219444444444e2 + XM*T);
                break;
            case(3):// Earth
                Kepl_Par[0]=(1.000000230);
                Kepl_Par[1]=(0.016751040 - 0.000041800*T - 0.0000001260*T*T);
                Kepl_Par[2]=(0.00);
                Kepl_Par[3]=(0.00);
                Kepl_Par[4]=(1.01220833333333333e+2 + 1.7191750*T + 4.52777777777777778e-4*T*T + 3.33333333333333333e-6*T*T*T);
                XM   = 3.599904975e+4 - 1.50277777777777778e-4*T - 3.33333333333333333e-6*T*T;
                Kepl_Par[5]=(3.58475844444444444e2 + XM*T);
                break;
            case(4):// Mars
                Kepl_Par[0]=(1.5236883990);
                Kepl_Par[1]=(0.093312900 + 0.0000920640*T - 0.0000000770*T*T);
                Kepl_Par[2]=(1.850333333333333330 - 6.75e-4*T + 1.26111111111111111e-5*T*T);
                Kepl_Par[3]=(4.87864416666666667e+1 + 7.70991666666666667e-1*T - 1.38888888888888889e-6*T*T - 5.33333333333333333e-6*T*T*T);
                Kepl_Par[4]=(2.85431761111111111e+2 + 1.069766666666666670*T +  1.3125e-4*T*T + 4.13888888888888889e-6*T*T*T);
                XM   = 1.91398585e+4 + 1.80805555555555556e-4*T + 1.19444444444444444e-6*T*T;
                Kepl_Par[5]=(3.19529425e2 + XM*T);
                break;
            case(5):// Jupiter
                Kepl_Par[0]=(5.2025610);
                Kepl_Par[1]=(0.048334750 + 0.000164180*T  - 0.00000046760*T*T -0.00000000170*T*T*T);
                Kepl_Par[2]=(1.308736111111111110 - 5.69611111111111111e-3*T +  3.88888888888888889e-6*T*T);
                Kepl_Par[3]=(9.94433861111111111e+1 + 1.010530*T + 3.52222222222222222e-4*T*T - 8.51111111111111111e-6*T*T*T);
                Kepl_Par[4]=(2.73277541666666667e+2 + 5.99431666666666667e-1*T + 7.0405e-4*T*T + 5.07777777777777778e-6*T*T*T);
                XM   = 3.03469202388888889e+3 - 7.21588888888888889e-4*T + 1.78444444444444444e-6*T*T;
                Kepl_Par[5]=(2.25328327777777778e2 + XM*T);
                break;
            case(6):// Saturn
                Kepl_Par[0]=(9.5547470);
                Kepl_Par[1]=(0.055892320 - 0.00034550*T - 0.0000007280*T*T + 0.000000000740*T*T*T);
                Kepl_Par[2]=(2.492519444444444440 - 3.91888888888888889e-3*T - 1.54888888888888889e-5*T*T + 4.44444444444444444e-8*T*T*T);
                Kepl_Par[3]=(1.12790388888888889e+2 + 8.73195138888888889e-1*T -1.52180555555555556e-4*T*T - 5.30555555555555556e-6*T*T*T);
                Kepl_Par[4]=(3.38307772222222222e+2 + 1.085220694444444440*T + 9.78541666666666667e-4*T*T + 9.91666666666666667e-6*T*T*T);
                XM   = 1.22155146777777778e+3 - 5.01819444444444444e-4*T - 5.19444444444444444e-6*T*T;
                Kepl_Par[5]=(1.75466216666666667e2 + XM*T);
                break;
            case(7):// Uranus
                Kepl_Par[0]=(19.218140);
                Kepl_Par[1]=(0.04634440 - 0.000026580*T + 0.0000000770*T*T);
                Kepl_Par[2]=(7.72463888888888889e-1 + 6.25277777777777778e-4*T + 3.95e-5*T*T);
                Kepl_Par[3]=(7.34770972222222222e+1 + 4.98667777777777778e-1*T + 1.31166666666666667e-3*T*T);
                Kepl_Par[4]=(9.80715527777777778e+1 + 9.85765e-1*T - 1.07447222222222222e-3*T*T - 6.05555555555555556e-7*T*T*T);
                XM   = 4.28379113055555556e+2 + 7.88444444444444444e-5*T + 1.11111111111111111e-9*T*T;
                Kepl_Par[5]=(7.26488194444444444e1 + XM*T);
                break;
            case(8)://Neptune
                Kepl_Par[0]=(30.109570);
                Kepl_Par[1]=(0.008997040 + 0.0000063300*T - 0.0000000020*T*T);
                Kepl_Par[2]=(1.779241666666666670 - 9.54361111111111111e-3*T - 9.11111111111111111e-6*T*T);
                Kepl_Par[3]=(1.30681358333333333e+2 + 1.0989350*T + 2.49866666666666667e-4*T*T - 4.71777777777777778e-6*T*T*T);
                Kepl_Par[4]=(2.76045966666666667e+2 + 3.25639444444444444e-1*T + 1.4095e-4*T*T + 4.11333333333333333e-6*T*T*T);
                XM   = 2.18461339722222222e+2 - 7.03333333333333333e-5*T;
                Kepl_Par[5]=(3.77306694444444444e1 + XM*T);
                break;
            case(9):// Pluto
                //Fifth order polynomial least square fit generated by Dario Izzo
                //(ESA ACT). JPL405 ephemerides (Charon-Pluto barycenter) have been used to produce the coefficients.
                //This approximation should not be used outside the range 2000-2100;
                T =mjd2000/36525.00;
                Kepl_Par[0]=(39.34041961252520 + 4.33305138120726*T - 22.93749932403733*T*T + 48.76336720791873*T*T*T - 45.52494862462379*T*T*T*T + 15.55134951783384*T*T*T*T*T);
                Kepl_Par[1]=(0.24617365396517 + 0.09198001742190*T - 0.57262288991447*T*T + 1.39163022881098*T*T*T - 1.46948451587683*T*T*T*T + 0.56164158721620*T*T*T*T*T);
                Kepl_Par[2]=(17.16690003784702 - 0.49770248790479*T + 2.73751901890829*T*T - 6.26973695197547*T*T*T + 6.36276927397430*T*T*T*T - 2.37006911673031*T*T*T*T*T);
                Kepl_Par[3]=(110.222019291707 + 1.551579150048*T - 9.701771291171*T*T + 25.730756810615*T*T*T - 30.140401383522*T*T*T*T + 12.796598193159 * T*T*T*T*T);
                Kepl_Par[4]=(113.368933916592 + 9.436835192183*T - 35.762300003726*T*T + 48.966118351549*T*T*T - 19.384576636609*T*T*T*T - 3.362714022614 * T*T*T*T*T);
                Kepl_Par[5]=(15.17008631634665 + 137.023166578486*T + 28.362805871736*T*T - 29.677368415909*T*T*T - 3.585159909117*T*T*T*T + 13.406844652829 * T*T*T*T*T);
                break;

        }

        // conversion of AU into KM
        Kepl_Par[0] *= KM;

        // conversion of DEG into RAD
        Kepl_Par[2] *= RAD;
        Kepl_Par[3] *= RAD;
        Kepl_Par[4] *= RAD;
        Kepl_Par[5] *= RAD;
        //Kepl_Par[5] =  fmod(Kepl_Par[5], 2.0*pi);
        Kepl_Par[5] =  Kepl_Par[5] % 2.0*pi;

        // Conversion from Mean Anomaly to Eccentric Anomaly via Kepler's equation
        Kepl_Par[5] = Mean2Eccentric(Kepl_Par[5], Kepl_Par[1]);

        // Position and Velocity evaluation according to j2000 system
         double[][] tmp = Conversion(Kepl_Par, MuSun);
         double[] position = tmp[0];
         double[] velocity = tmp[1];

         double[][] output = {position,velocity};

         return output;
    }

    private double[][] Conversion (double[] E, double mu)
    {
        double a,e,i,omg,omp,theta;
        double b,n;
        double[] X_per = new double[3];
        double[] X_dotper = new double[3];
        double[][] R = new double[3][3];

        a = E[0];
        e = E[1];
        i = E[2];
        omg = E[3];
        omp = E[4];
        theta = E[5];

        b = a * Math.sqrt (1 - e*e);
        n = Math.sqrt(mu/(Math.pow(a,3)));

	    final double sin_theta = Math.sin(theta);
	    final double cos_theta = Math.cos(theta);

        X_per[0] = a * (cos_theta - e);
        X_per[1] = b * sin_theta;

        X_dotper[0] = -(a * n * sin_theta)/(1 - e * cos_theta);
        X_dotper[1] = (b * n * cos_theta)/(1 - e * cos_theta);

	    final double cosomg = Math.cos(omg);
        final double cosomp = Math.cos(omp);
        final double sinomg = Math.sin(omg);
        final double sinomp = Math.sin(omp);
        final double cosi = Math.cos(i);
        final double sini = Math.sin(i);

        R[0][0] =  cosomg*cosomp-sinomg*sinomp*cosi;
        R[0][1] =  -cosomg*sinomp-sinomg*cosomp*cosi;

        R[1][0] =  sinomg*cosomp+cosomg*sinomp*cosi;
        R[1][1] =  -sinomg*sinomp+cosomg*cosomp*cosi;

        R[2][0] =  sinomp*sini;
        R[2][1] =  cosomp*sini;

        //
        double[] pos = new double[3];
        double[] vel = new double[3];

        // evaluate position and velocity
        for (int j = 0;j < 3;j++)
        {
            pos[j] = 0;
            vel[j] = 0;
            for (int k = 0;k < 2;k++)
            {
                pos[j] += R[j][k] * X_per[k];
                vel[j] += R[j][k] * X_dotper[k];
            }
        }

        double tmp[][] = {pos, vel};

        return tmp;
    }


    public double Norm(double[] vet1, double[] vet2)
    {
        double Vin = 0;
        for (int i = 0; i < 3; i++)
        {
            Vin += (vet1[i] - vet2[i])*(vet1[i] - vet2[i]);
        }
        return Math.sqrt(Vin);
    }


    //subfunction that evaluates vector product
    private double[] vett(double[] vet1, double[] vet2)
    {
        double[] prod = new double[3];

        prod[0]=(vet1[1]*vet2[2]-vet1[2]*vet2[1]);
        prod[1]=(vet1[2]*vet2[0]-vet1[0]*vet2[2]);
        prod[2]=(vet1[0]*vet2[1]-vet1[1]*vet2[0]);

        return prod;
    }

    private double asinh (double x) { return Math.log(x + Math.sqrt(x*x + 1)); };

    private double acosh (double x) { return Math.log(x + Math.sqrt(x*x - 1)); };

    private double x2tof(double x, double s, double c, int lw)
    {
        double am,a,alfa,beta;

        am = s/2;
        a = am/(1-x*x);
        if (x < 1)//ellipse
        {
            beta = 2 * Math.asin (Math.sqrt((s - c)/(2*a)));
            if (lw == 1)
            {
                beta = -beta;
            }
            alfa = 2 * Math.acos(x);
        }
        else
        {
            alfa = 2 * acosh(x);
            beta = 2 * asinh(Math.sqrt ((s - c)/(-2 * a)));
            if (lw == 1)
            {
                beta = -beta;
            }
        }

        if (a > 0)
        {
            return (a * Math.sqrt (a)* ( (alfa - Math.sin(alfa)) - (beta - Math.sin(beta)) ));
        }
        else
        {
            return (-a * Math.sqrt(-a)*( (Math.sinh(alfa) - alfa) - ( Math.sinh(beta) - beta)) );
        }

    }

    // Subfunction that evaluates the time of flight as a function of x
    private double tofabn(double sigma, double alpha, double beta)
    {
        if (sigma > 0)
        {
            return (sigma * Math.sqrt (sigma)* ( (alpha - Math.sin(alpha)) - (beta -Math.sin(beta)) ));
        }
        else
        {
            return (-sigma * Math.sqrt(-sigma)*( (Math.sinh(alpha) - alpha) - ( Math.sinh(beta) - beta)) );
        }
    }

    // subfunction that evaluates unit vectors
    private double[] vers(double[] V_in)
    {
        double[] Ver_out = new double[3];
        double v_mod = 0;
        int i;

        for (i = 0;i < 3;i++)
        {
            v_mod += V_in[i]*V_in[i];
        }

        double sqrtv_mod = Math.sqrt(v_mod);

        for (i = 0;i < 3;i++)
        {
            Ver_out[i] = V_in[i]/sqrtv_mod;
        }

        return Ver_out;
    }


    /*
    %Inputs:
    %           r0:    column vector for the position (mu=1)
    %           v0:    column vector for the velocity (mu=1)
    %           rtarget: distance to be reached
    %
    %Outputs:
    %           t:     time taken to reach a given distance
    %
    %Comments:  everything works in non dimensional units
    */

    private double time2distance(double[] r0, double[] v0, double rtarget)
    {
        double temp = 0.0;
        double[] E = new double[6];
        double r0norm = Norm2(r0);
        double a, e, E0, p, ni, Et;
        int i;

        if (r0norm < rtarget)
        {
            for (i=0; i<3; i++) {
                temp += r0[i] * v0[i];
            }

            E = IC2par(r0,v0,1);

            a = E[0];
            e = E[1];
            E0 = E[5];
            p = a * (1-e*e);
            // If the solution is an ellipse
            if (e<1)
            {
                double ra = a * (1+e);
                if (rtarget>ra)
                    return -1; // NaN;
                else // we find the anomaly where the target distance is reached
                {
                    ni = Math.acos((p/rtarget-1)/e);         //in 0-pi
                    Et = 2*Math.atan(Math.sqrt((1-e)/(1+e))*Math.tan(ni/2)); // algebraic kepler's equation

                    if (temp>0)
                        return Math.sqrt(Math.pow(a,3))*(Et-e*Math.sin(Et)-E0 + e*Math.sin(E0));
                    else
                    {
                        E0 = -E0;
                        return Math.sqrt(Math.pow(a,3))*(Et-e*Math.sin(Et)+E0 - e*Math.sin(E0));
                    }
                }
            }
            else // the solution is a hyperbolae
            {
                ni = Math.acos((p/rtarget-1)/e);         // in 0-pi
                Et = 2*Math.atan(Math.sqrt((e-1)/(e+1))*Math.tan(ni/2)); // algebraic equivalent of kepler's equation in terms of the Gudermannian

                if (temp>0) // out==1
                    return Math.sqrt(Math.pow((-a),3))*(e*Math.tan(Et)-Math.log(Math.tan(Et/2+Math.PI/4))-e*Math.tan(E0)+Math.log(Math.tan(E0/2+Math.PI/4)));
                else
                {
                    E0 = -E0;
                    return Math.sqrt(Math.pow((-a),3))*(e*Math.tan(Et)-Math.log(Math.tan(Et/2+Math.PI/4))+e*Math.tan(E0)-Math.log(Math.tan(E0/2+Math.PI/4)));
                }
            }
        }
        else
            return 12; // what is 12...
    }


}

//
class customobject
{
    double[] keplerian = new double[6];
    double epoch;
    double mu;
};

/// Class for one dimensional functions
//  with some parameters
/**  The ()-operator with one double argument
 *  has to be overloaded for a derived class
 *  The return value is the ordinate computed for
 *  the abscissa-argument.
 */
abstract class Function1D
{
    abstract double myFunc(double x);

    // parameters
    public double p1;
    public double p2;

    public void SetParameters(double a, double b)
    {
        p1 = a;
        p2 = b;
    }
};

class CZF extends Function1D
{
        @Override
        double myFunc(double x)
        {
            return p1*Math.tan(x) - Math.log(Math.tan(0.5*x + 0.25*Math.PI)) - p2;
        }
 };

class FZero
{
    private double a;
    private double c; // lower and upper bound

    public FZero(double a, double b)
    {
        SetBounds(a, b);
    }

    void SetBounds(double lb, double ub)
    {
        a = lb;
        c = ub;
    }

    // fzero procedure
    double FindZero(Function1D f)
    {
        int max_iterations = 500;
        double tolerance = 1e-15;

        double fa = f.myFunc(a);
        double b = 0.5 * ( a + c );
        double fc = f.myFunc(c);
        double fb = fa * fc;
        double delta, dab, dcb;
        int i;

        // If the initial estimates do not bracket a root, set the err flag and //
        // return.  If an initial estimate is a root, then return the root.     //

        double err = 0;
        if ( fb >= 0.0 )
            if ( fb > 0.0 )  { err = -1; return 0.0; }
            else return ( fa == 0.0 ) ? a : c;

        // Insure that the initial estimate a < c. //

        if ( a > c ) {
            delta = a;
            a = c;
            c = delta;
            delta = fa;
            fa = fc;
            fc = delta;
        }

        // If the function at the left endpoint is positive, and the function //
        // at the right endpoint is negative.  Iterate reducing the length    //
        // of the interval by either bisection or quadratic inverse           //
        // interpolation.  Note that the function at the left endpoint        //
        // remains nonnegative and the function at the right endpoint remains //
        // nonpositive.                                                       //

        if ( fa > 0.0 )
            for ( i = 0; i < max_iterations; i++) {

                // Are the two endpoints within the user specified tolerance ?

                if ( ( c - a ) < tolerance ) return 0.5 * ( a + c);

                // No, Continue iteration.

                fb = f.myFunc(b);

                // Check that we are converging or that we have converged near //
                // the left endpoint of the inverval.  If it appears that the  //
                // interval is not decreasing fast enough, use bisection.      //
                if ( ( c - a ) < tolerance ) return 0.5 * ( a + c);
                if ( ( b - a ) < tolerance )
                    if ( fb > 0 ) {
                        a = b; fa = fb; b = 0.5 * ( a + c ); continue;
                    }
                    else return b;

                // Check that we are converging or that we have converged near //
                // the right endpoint of the inverval.  If it appears that the //
                // interval is not decreasing fast enough, use bisection.      //

                if ( ( c - b ) < tolerance )
                    if ( fb < 0 ) {
                        c = b; fc = fb; b = 0.5 * ( a + c ); continue;
                    }
                    else return b;

                // If quadratic inverse interpolation is feasible, try it. //

                if (  ( fa > fb ) && ( fb > fc ) ) {
                    delta = Denominator(fa,fb,fc);
                    if ( delta != 0.0 ) {
                        dab = a - b;
                        dcb = c - b;
                        delta = Numerator(dab,dcb,fa,fb,fc) / delta;

                        // Will the new estimate of the root be within the   //
                        // interval?  If yes, use it and update interval.    //
                        // If no, use the bisection method.                  //

                        if ( delta > dab && delta < dcb ) {
                            if ( fb > 0.0 ) { a = b; fa = fb; }
                            else if ( fb < 0.0 )  { c = b; fc = fb; }
                            else return b;
                            b += delta;
                            continue;
                        }
                    }
                }

                // If not, use the bisection method. //

                //fb > 0 ? ( a = b, fa = fb ) : ( c = b, fc = fb );
                if(fb > 0)
                {
                    a = b;
                    fa = fb;
                }
                else
                {
                    c = b;
                    fc = fb;
                }

                b = 0.5 * ( a + c );
            }
        else

            // If the function at the left endpoint is negative, and the function //
            // at the right endpoint is positive.  Iterate reducing the length    //
            // of the interval by either bisection or quadratic inverse           //
            // interpolation.  Note that the function at the left endpoint        //
            // remains nonpositive and the function at the right endpoint remains //
            // nonnegative.                                                       //

            for ( i = 0; i < max_iterations; i++) {
                if ( ( c - a ) < tolerance ) return 0.5 * ( a + c);
                fb = f.myFunc(b);

                if ( ( b - a ) < tolerance )
                    if ( fb < 0 ) {
                        a = b; fa = fb; b = 0.5 * ( a + c ); continue;
                    }
                    else return b;

                if ( ( c - b ) < tolerance )
                    if ( fb > 0 ) {
                        c = b; fc = fb; b = 0.5 * ( a + c ); continue;
                    }
                    else return b;

                if (  ( fa < fb ) && ( fb < fc ) ) {
                    delta = Denominator(fa,fb,fc);
                    if ( delta != 0.0 ) {
                        dab = a - b;
                        dcb = c - b;
                        delta = Numerator(dab,dcb,fa,fb,fc) / delta;
                        if ( delta > dab && delta < dcb ) {
                            if ( fb < 0.0 ) { a = b; fa = fb; }
                            else if ( fb > 0.0 )  { c = b; fc = fb; }
                            else return b;
                            b += delta;
                            continue;
                        }
                    }
                }

                //fb < 0 ? ( a = b, fa = fb ) : ( c = b, fc = fb );

                if(fb < 0)
                {
                    a = b;
                    fa = fb;
                }
                else
                {
                    c = b;
                    fc = fb;
                }


                b = 0.5 * ( a + c );
            }
        err = -2;
        return  b;

    }

    private double Numerator(double dab, double dcb, double fa, double fb, double fc )
    {
        return fb*(dab*fc*(fc-fb)-fa*dcb*(fa-fb));
    }

    private double Denominator(double fa, double fb, double fc)
    {
        return (fc-fb)*(fa-fb)*(fa-fc);
    }

};





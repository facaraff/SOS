package applications.students.ESA_Misc;



public class ESAProblem
{
    //int type;							//problem type
    public int[] sequence;				//fly-by sequence (ex: 3,2,3,3,5,is Earth-Venus-Earth-Earth-Jupiter)
    public double e;							//insertion e (only in case total_DV_orbit_insertion)
    public double rp;							//insertion rp in km (only in case total_DV_orbit_insertion)
    //CustomObject asteroid;				//asteroid data (in case fly-by sequence has a final number = 10)
    //public double AUdist;						//Distance to reach in AUs (only in case of time2AUs)
    //public double DVtotal;						//Total DV allowed in km/s (only in case of time2AUs)
    //public double DVonboard;					//Total DV on the spacecraft in km/s (only in case of time2AUs)

    //Pre-allocated memory, in order to remove allocation of heap space in MGA_DSM calls
    //The DV vector serves also as output containing all the values for the impulsive DV
    public double[][] r;
    public double[][] v;
    public double[] DV;

    //
    public ESAProblem(int[] seq)
    {
        this.sequence = seq; // sequence of planets

        r = new double[sequence.length][3];
        v = new double[sequence.length][3];
        DV = new double[sequence.length +1];

        //Zero out arrays
        /*
        for (int i=0;i < sequence.length +1;i++)
        {
            DV[i] = 0.0;
        }

        for (int i=0;i < sequence.length;i++)
        {
            for (int j=0;j <3;j++)
            {
                r[i][j] = 0.0;
                v[i][j] = 0.0;
            }
        }
        */
    }


};

package applications.SearchProblem;

/**
 * Created by Mikolaj on 29/04/2018.
 */
public class Particle {
    static double waypointInterval = 2.0;
    static double glimpseDetectionProbability = 5.0e2;
    static double searchHeight = 50;
    double x,y;

    public Particle (double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }

    public double getY()
    {
        return y;
    }

    public double calculateDetectionScore(double[] solution)
    {
        double wp1x = solution[0];
        double wp1y = solution[1];

        double cumulativeProbability = 0.0;

        for (int i = 2; i < solution.length; i += 2)
        {
            double wp2X = solution[i];
            double wp2y = solution[i+1];

            double a = Math.abs(getX() - wp1x);
            double b = Math.abs(getX() - wp2X);
            double c = Math.abs(getY() - wp1y);
            double d = Math.abs(getY() - wp2y);

            double numerator1 = -((a*b) + (c*d) + (2*waypointInterval));

            double denominator1 = (((a*a)*((b*b)-2))+(2*a*b*c*d)+((c*c)*((d*d)-2))-(2*(searchHeight*searchHeight)))*
                    Math.sqrt((a*a)+(2*a*b*waypointInterval)+(c*c)+(2*c*d*waypointInterval)+(searchHeight*searchHeight)+(2*waypointInterval*waypointInterval));

            double numerator2 = -((a*b) + (c*d));

            double denominator2 = (((a*a)*((b*b)-2))+(2*a*b*c*d)+((c*c)*((d*d)-2))-(2*(searchHeight*searchHeight)))*
                    Math.sqrt((a*a)+(c*c)+(searchHeight*searchHeight));

            //TODO: Check for no movement as this causes NaNs
            //TODO: you can see that the equation could potentially be altered to take into account drifting particles and changing altitude. Look into this?

            if (denominator1 == Double.NaN)
                denominator1 = Double.MAX_VALUE;
            if (denominator2 == Double.NaN)
                denominator2 = Double.MAX_VALUE;

            cumulativeProbability += (numerator1/denominator1 - numerator2/denominator2)*glimpseDetectionProbability*searchHeight;

            wp1x = wp2X;
            wp1y = wp2y;
        }

        return Math.exp(-cumulativeProbability);
    }

    public static void main(String[] args)
    {
        Particle p = new Particle(20,20);

        double[] solution1 = new double[]{20.0,20.8,20.0,0.9,20.8,20.1,18.0,19.0,21.0,28.0,29.2,10.9,20.2,20.0,20.0,20.8,20.0,0.9,20.8,20.1,18.0,19.0,21.0,28.0,29.2,10.9,20.2,20.0};
        double[] solution2 = new double[]{0.0,0.0,0.0,0.0,0.0,0.0};
        double[] solution3 = new double[]{19.0,19.0,19.0,19.9,19.0,19.0};
        double[] solution4 = new double[]{19.0,19.0,19.0,19.9,19.0,18.0};
        double[] solution5 = new double[]{-19.0,-19.0,-19.0,-19.9,-19.0,-19.0};
        double[] solution6 = new double[]{19.0,19.0,19.0,19.9,19.0,18.0,19.0,19.0,19.0,19.9,19.0,18.0,19.0,19.0,19.0,19.9,19.0,18.0,19.0,19.0,19.0,19.9,19.0,18.0,19.0,19.0,19.0,19.9,20.8,20.0,0.9,20.8,20.1,18.0,19.0,21.0,28.0,29.2,10.9,20.2,20.0,20.0};


        System.out.println(p.calculateDetectionScore(solution1));
        System.out.println(p.calculateDetectionScore(solution2));
        System.out.println(p.calculateDetectionScore(solution3));
        System.out.println(p.calculateDetectionScore(solution4));
        System.out.println(p.calculateDetectionScore(solution5));
        System.out.println(p.calculateDetectionScore(solution6));

    }
}

package utils.algorithms;

import utils.random.RandUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Mikolaj on 06/05/2018.
 */
public class TopNPicker {
    ArrayList<DoubleInt> orderedDoubleInts;
    int maxCount;
    public TopNPicker(int n)
    {
        orderedDoubleInts = new ArrayList<DoubleInt>(n);
        maxCount = n;
    }

    public void add(double d, int index)
    {
        DoubleInt newDoubleInt = new DoubleInt(d, index);

        if (orderedDoubleInts.size() < maxCount || d < orderedDoubleInts.get(maxCount-1).d) {
            orderedDoubleInts.add(newDoubleInt);
            Collections.sort(orderedDoubleInts);
        }

        if (orderedDoubleInts.size() > maxCount)
            orderedDoubleInts.subList(maxCount, orderedDoubleInts.size()).clear();
    }

    public int getRandomTopIndex()
    {
        return orderedDoubleInts.get(RandUtils.randomInteger(maxCount-1)).index;
    }

    public double getBestFitness()
    {
        return  orderedDoubleInts.get(0).d;
    }

    public int getBestIndex()
    {
        return orderedDoubleInts.get(0).index;
    }

    class DoubleInt implements Comparable<DoubleInt>{
        public Double d;
        public int index;
        public DoubleInt(double d, int index)
        {
            this.d = d;
            this.index = index;
        }

        @Override
        public int compareTo(DoubleInt o) {
            return this.d.compareTo(o.d);
        }
    }
}

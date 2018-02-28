package com.disnodeteam.dogecv.math;

import java.util.ArrayList;
import java.util.List;

public class MathFTC {

    /**
     * A simple clamp function, which assumes a valid range.
     * @param value The double being clamped
     * @param min The minimum value of the range (inclusive)
     * @param max The maximum value of the range (inclusive)
     * @return
     */
    public static double clip(double value, double min, double max) {
        if (value > max) return max;
        else if (value < min) return min;
        else return value;
    }

    /**
     * A recursive function which return a list of lists, where each list is a combination of length k.
     * @param list The input list to be chosen from
     * @param k The sample size
     * @return
     */
    public static <T> List<List<T>> combinations(List<T> list, int k) {
        int n = list.size();
        List<List<T>> combos = new ArrayList<List<T>>();
        if (k == 0 ) {
            combos.add(new ArrayList<T>());
            return combos;
        }
        if ( n < k || n == 0) {
            return combos;
        }
        T last = list.get(n-1);
        combos.addAll(combinations(list.subList(0, n-1),k));
        for (List<T> subCombo : combinations(list.subList(0, n-1), k-1)) {
            subCombo.add(last);
            combos.add(subCombo);
        }
        return combos;
    }

    public static double getStdDev(List<Double> samples) {
        if(samples.size() == 0) return 0;
        double mean = 0;
        for (Double point : samples) {
            mean += point;
        }
        mean = mean/samples.size();
        double sigma = 0;
        for (Double point : samples) {
            sigma += Math.pow(point-mean, 2);
        }
        return Math.sqrt(sigma/(samples.size() - 1));
    }

    public static double normalizeAngle(double angle) {
        angle = angle % 180;
        if (angle > 0) return angle;
        else return angle+180;
    }
}

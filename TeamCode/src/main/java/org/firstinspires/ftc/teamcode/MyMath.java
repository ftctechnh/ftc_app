package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.Arrays;

public class MyMath {

    static double median(ArrayList<Double> values)
    {
        Double[] copy = values.toArray(new Double[values.size()]);
        Arrays.sort(copy);


        int size = values.size();
        int half = size / 2;

        if(size % 2 == 1) //if the size is odd
            return copy[half]; //return the middle value

        return (copy[half-1] + copy[half]) / 2;

    }

    static double ave(ArrayList<Double> values)
    {
        double sum = 0;
        for (double v:values) {
            sum += v;
        }
        return sum / values.size();
    }

    static double dotProduct(double[] distances, double[] coefficients)
    {
        double sum = 0;
        for(int i = 0; i < distances.length; i++)
        {
            sum += distances[i] * coefficients[i];
        }
        return sum;
    }


    /**
     *
     * @param target angle
     * @param current angle
     * @return the smallest signed difference between the two angles
     */
    static double loopAngle(double target, double current)
    {
        double d = target - current;
        if(d < -Math.PI) d += 2 * Math.PI; //keeps it between -pi and pi
        if(d >  Math.PI) d -= 2 * Math.PI;
        return d;
    }

    static double max(double... numbers)
    {
        double max = numbers[0];
        for (double n: numbers) {
            if(n > max)
                max = n;
        }
        return max;
    }

    static double absoluteMax(double... numbers)
    {
        double max = 0;
        for (double n: numbers)
            if(Math.abs(n) > max)
                max = Math.abs(n);
        return max;
    }

    static double farthestFromZero(double... numbers)
    {
        double max = 0;
        for (double n: numbers)
            if(Math.abs(n) > Math.abs(max))
                max = n;
        return max;
    }

    static double closestToZero(double... numbers)
    {
        double min = 0;
        if(numbers.length > 0)
            min = numbers[0];

        for (double n: numbers)
            if(Math.abs(n) < Math.abs(min))
                min = n;
        return min;
    }

    static double radians(double degrees)
    {
        return degrees * Math.PI / 180;
    }

    static double degrees(double radians)
    {
        return radians * 180 / Math.PI;
    }
}

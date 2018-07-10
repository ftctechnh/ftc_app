package com.disnodeteam.dogecv.math;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

import java.util.List;

public class Points {
    public static boolean inBounds(Point point, Size size) {
        if(point.x < size.width - 1 && point.x >= 0 && point.y < size.height-1 && point.y >= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static Point getMeanPoint(List<Point> points) {
        if (points.size() == 0) return null;
        double x = 0;
        double y = 0;
        for(Point point : points) {
            x += Math.pow(point.x, 2);
            y += Math.pow(point.y, 2);
        }
        return new Point(Math.sqrt(x/points.size()), Math.sqrt(y/points.size()));
    }
}
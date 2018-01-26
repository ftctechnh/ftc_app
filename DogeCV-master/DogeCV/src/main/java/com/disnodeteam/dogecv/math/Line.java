package com.disnodeteam.dogecv.math;

import org.opencv.core.Point;

public class Line implements Comparable<Line> {

    public Point point1;
    public Point point2;

    public double x1,y1,x2,y2;

    public Line(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        x1 = point1.x;
        x2 = point2.x;
        y1 = point1.y;
        y2 = point2.y;
    }

    public double length() {
        return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    public Point center() {
        return new Point((int) (0.5*(x1 + x2)),(int) (0.5*(y1 + y2)));
    }

    public double angle() {
        return Math.atan2(y2-y1,x2-x1);
    }

    public void resize(double scale) {
        this.x1 = scale*x1;
        this.y1 = scale*y1;
        this.x2 = scale*x2;
        this.y2 = scale*y2;
        this.point1 = new Point(x1,y1);
        this.point2 = new Point(x2,y2);
    }

    @Override
    public int compareTo(Line other) {
        return Double.compare(this.length(), other.length());
    }

    @Override
    public String toString() {
        return "{" + Double.toString(x1) + "," + Double.toString(y1) + "} to {" + Double.toString(x2) + "," + Double.toString(y2) + "}";
    }

}

package com.qualcomm.ftcrobotcontroller.containers;

public class Location {
    private double x, y, theta, spd;
    private int left_encoder, right_encoder;
    private long time;

    public Location() {
        this(0,0,0);
    }

    public Location(double x, double y, double theta) {
        this(x,y,theta,0,0,System.currentTimeMillis(),0);
    }

    public Location(double x, double y, double theta, int left_encoder, int right_encoder, long time, double spd) {
        set(x,y,theta,left_encoder,right_encoder,time, spd);
    }

    public void set(double x, double y, double theta, int left_encoder, int right_encoder, long time, double spd) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.left_encoder = left_encoder;
        this.right_encoder = right_encoder;
        this.time = time;
    }

    public double distanceTo(Location location) {
        return Math.sqrt(Math.pow(x-location.getX(),2)+ Math.pow(y-location.getY(),2));
    }

    public double angleTo(Location location) {
        return Math.abs(theta-location.getTheta());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getSpd() {
        return spd;
    }

    public void setSpd(double spd) {
        this.spd = spd;
    }

    public int getLeft_encoder() {
        return left_encoder;
    }

    public void setLeft_encoder(int left_encoder) {
        this.left_encoder = left_encoder;
    }

    public int getRight_encoder() {
        return right_encoder;
    }

    public void setRight_encoder(int right_encoder) {
        this.right_encoder = right_encoder;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

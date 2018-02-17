package org.firstinspires.ftc.teamcode.math.vector;

/**
 * Created by Derek on 12/7/2017.
 */

public class Vec2 {

    public double x;
    public double y;

    public Vec2(double x,double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 clamp(double v) {
        this.x = clamp1(v,this.x);
        this.y = clamp1(v,this.y);
        return this;
    }

    //v is the value to clamp by, p the value to be clamped
    //since this is unreadable it does this:
    //assert v > 0; (fails silently though)
    //if (p > v) p = v;
    //if (p < -v) p = -v;
    //return p


    @Override
    public String toString() {
        return "" + this.x + ", " +this.y;
    }

    double clamp1(double v, double p) {
        if (v > 0) if (p > v) return v;
        else return (p < -v) ? -v : p;
        else return p;
    }
}

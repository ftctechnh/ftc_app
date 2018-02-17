package org.firstinspires.ftc.teamcode.math.vector;

/**
 * Created by Derek on 12/7/2017.
 */

public class Vec3 extends Vec2 {
    public double z;

    public Vec3(double x, double y,double z) {
        super(x, y);
        this.z = z;
    }

    public Vec3(Vec3 vec) {
        this(vec.x,vec.y,vec.z);
    }

    @Override
    public String toString() {
        return super.toString() + ", " + this.z;
    }

    public Vec3 clamp(double v) {
        super.clamp(v);
        this.z = super.clamp1(v,this.z);
        return this;
    }
}

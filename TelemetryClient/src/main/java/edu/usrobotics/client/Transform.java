package edu.usrobotics.client;

/**
 * Created by Max on 9/17/2016.
 */
public class Transform {
    public float x;
    public float y;
    public float z;
    public float rx;
    public float ry;
    public float rz;

    public Transform(float x, float y, float z, float rx, float ry, float rz) {
        set(x, y, z, rx, ry, rz);
    }

    public Transform(float x, float y, float z) {
        set(x, y, z, 0, 0, 0);
    }

    public void set (float x, float y, float z, float rx, float ry, float rz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    public void set (float x, float y, float z) {
        set(x, y, z, rx, ry, rz);
    }

    public void scale (float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }

    public Transform scaled (float s) {
        scale(s);
        return this;
    }
}

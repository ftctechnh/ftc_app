package edu.usrobotics.client;

/**
 * Created by Max on 9/17/2016.
 */
public class Transform {
    public float x;
    public float y;
    public float z;
    public float rx = 0;
    public float ry = 0;
    public float rz = 0;

    public Transform(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set (float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void multiply (float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }

    public Transform mutliplied (float s) {
        multiply(s);
        return this;
    }
}

package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

/**
 * Created by ftc6347 on 2/9/17.
 */

public class EncoderValues {
    private double frontLeft = 0;
    private double frontRight = 0;
    private double backLeft = 0;
    private double backRight = 0;

    public EncoderValues(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }

    public double getFrontLeft() {
        return frontLeft;
    }

    public double getFrontRight() {
        return frontRight;
    }

    public double getBackLeft() {
        return backLeft;
    }

    public double getBackRight() {
        return backRight;
    }

    public boolean isGreater(EncoderValues initial, double distance) {
        return (this.backLeft - initial.backLeft > distance
                || this.backRight - initial.backRight > distance
                || this.frontLeft - initial.frontLeft > distance
                || this.frontRight - initial.frontRight > distance);
    }

    public boolean isLessThan(EncoderValues other) {
        return (this.backLeft < other.backLeft
                || this.backRight < other.backRight
                || this.frontLeft < other.frontLeft
                || this.frontRight < other.frontRight);
    }

    @Override
    public String toString() {
        return "EncoderValues{" +
                "frontLeft=" + frontLeft +
                ", frontRight=" + frontRight +
                ", backLeft=" + backLeft +
                ", backRight=" + backRight +
                '}';
    }
}

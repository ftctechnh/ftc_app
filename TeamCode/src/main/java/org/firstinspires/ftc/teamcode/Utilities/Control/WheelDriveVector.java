package org.firstinspires.ftc.teamcode.Utilities.Control;

public class WheelDriveVector {
    public double turnSpeed;
    public double forwardSpeed;
    public double translateSpeed;

    public WheelDriveVector (double forwardSpeed, double translateSpeed, double turnSpeed) {
        this.forwardSpeed = forwardSpeed;
        this.translateSpeed = translateSpeed;
        this.turnSpeed = turnSpeed;
    }

    public void scale(double translateFactor, double turnFactor) {
        forwardSpeed *= translateFactor;
        translateSpeed *= translateFactor;
        turnSpeed *= turnFactor;
    }

    public double[] getDrivePowers() {
        double[] powers = {
                -forwardSpeed - translateSpeed - turnSpeed,
                -forwardSpeed + translateSpeed + turnSpeed,
                -forwardSpeed + translateSpeed - turnSpeed,
                -forwardSpeed - translateSpeed + turnSpeed
        };

        double maxMagnitude = 0;
        for (double p : powers) {maxMagnitude = Math.max(Math.abs(p), maxMagnitude);}

        // Scale to max magnitude
        if (maxMagnitude > 1) {
            for (int i = 0; i < 4; i++) {powers[i] /= maxMagnitude;}
        }
        return powers;
    }
}

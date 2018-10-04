package org.firstinspires.ftc.teamcode.components;

public interface EncoderBasedDriveable {

    // The EncoderBasedDriveable should be calibrated because the "ticks" are the
    // ticks from the minimum position
    abstract void driveToPostionTicks(int ticks, double power);
}

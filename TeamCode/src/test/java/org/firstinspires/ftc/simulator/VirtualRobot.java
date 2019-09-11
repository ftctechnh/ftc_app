package org.firstinspires.ftc.simulator;

import org.firstinspires.ftc.teamcode.common.math.Pose;

public interface VirtualRobot {
    // Elapse a certain amount of time on the robot
    void elapse(double ms);

    Pose getStampedPosition();
}
package org.firstinspires.ftc.teamcode.systems.marker;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.systems.base.System;

/**
 * Runs a servo to release a marker attached to the side of the robot.
 */
public class Marker extends System {
    private Servo servo;

    /**
     * Creates a marker system that releases a marker attched to the robot
     * that runs in the current opmode
     * @param opMode
     */
    public Marker(OpMode opMode) {
        super(opMode, "marker");
        this.servo = hardwareMap.servo.get("marker");
    }

    /**
     * Releases the marker from the robot
     */
    public void place() {
        servo.setPosition(0.0);
    }

    /**
     * Resets the position of the arm marker
     */
    public void reset() {
        servo.setPosition(90.0);
    }

}


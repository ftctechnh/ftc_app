package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Derek on 10/24/2017.
 *
 * Used to store hardware elements and to provide more controlled access to the low level elements
 */


public class Robot {
    private final HardwareMap hardwareMap;

    /**
     * Creates the robot for the op modes to use
     * @param hardwareMap hardware map within the parent op mode's context
     */
    public Robot(final HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    Claw claw = new Claw();


    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }
}

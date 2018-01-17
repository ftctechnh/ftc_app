package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.locomotion.DriveInfo;
import org.firstinspires.ftc.teamcode.robot.locomotion.DriveMode;

import java.io.File;

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
    public Robot(final HardwareMap hardwareMap,DriveMode driveMode) {
        this.hardwareMap = hardwareMap;
        this.driveMode = driveMode;
        this.getClass().getResource(File.separator + "res" + "strings.xml");
    }

    private Claw claw;
    private DriveMode driveMode;

    public void update (DriveInfo driveInfo) {
        driveMode.update(driveInfo);
    }

    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }
}

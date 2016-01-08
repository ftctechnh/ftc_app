package com.powerstackers.resq.common;

import org.swerverobotics.library.SynchronousOpMode;

/**
 * @author Jonathan Thomas
 */
public class AutonomousProgram extends SynchronousOpMode {

    AllianceColor allianceColor;
    Robot robot;

    public AutonomousProgram(AllianceColor allianceColor) {
        this.allianceColor = allianceColor;
        this.robot = new Robot(this);
    }

    /**
     * Run the actual program.
     */
    @Override
    protected void main() throws InterruptedException {
        // Initialize any sensors and servos

        // Wait for the start of the match
        this.waitForStart();

        // Run any actions we desire
        robot.tapBeacon(allianceColor);
    }
}

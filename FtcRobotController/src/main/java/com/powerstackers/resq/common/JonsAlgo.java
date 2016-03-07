/*
 * Copyright (C) 2015 Powerstackers
 *
 * Code for autonomous.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.powerstackers.resq.common;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

/**
 * Contains basic utility functions for autonomous control.
 * Things like turning, going in straight lines, and moving various manipulators can be controlled
 * from inside this class.
 *
 * @author Jonathan Thomas
 */
public class JonsAlgo {

    RobotAuto robot;
    OpMode mode;

    /**
     * Create a new JonsAlgo object. RobotAuto passed in must have a working parent opmode.
     * @param robot Robot object to use.
     */
    public JonsAlgo(RobotAuto robot) {
        this.robot = robot;
        this.mode = robot.getParentOpMode();
    }

    /*
    *	GLOBAL CONSTANTS
    */
    /**
     * Stores the number of encoder ticks in one motor revolution.
     * For AndyMark Neverest 40's, it's 280. The encoder tick count is actually 7 pulses per
     * revolution. Since the gearbox increases the number of rotations by a factor of 40, the final
     * count is 7 * 40 = 280. For 20 or 60 reduction motors, the  number would be different.
     */
    double ticksPerRevolution = 1120; // Number of encoder ticks per motor rotation
    double wheelDiameter = 4;         // Diameter of your wheels in inches
    double driveGearMultiplier = 1.0; // Drive gear multiplier.
    // EXAMPLE: If your drive train is geared 2:1 (1 motor rotation = 2 wheel rotations), set this to 2
//    double turnOvershootThreshold = 0.1;

    /**
     * Converts a distance in inches to a distance in encoder ticks.
     * <p>We calculate this by taking the number of wheel rotations (inches/(PI*wheelDiameter))
     * multiplied by the inverse of the gear ratio, to get the number of motor rotations. Multiply
     * one more time by the number of motor encoder ticks per one motor revolution.
     * @param  inches double containing the distance you want to travel.
     * @return        that distance in encoder ticks.
     */
    public long inchesToTicks(double inches) {
        return (long) ((1/driveGearMultiplier)*ticksPerRevolution*(inches/(PI*wheelDiameter)));
    }

    /**
     * Converts a distance in encoder ticks to a distance in inches.
     * <p>We calculate this by taking the number of ticks traveled, divided by the number of ticks
     * per revolution, and then multiplied by the gear ratio multiplier to get the number of wheel
     * rotations. Multiply one more time by the circumference of the wheels (PI*wheelDiameter).
     * @param  ticks long representing the distance in ticks.
     * @return       that distance in inches.
     */
    public double ticksToInches(long ticks) {
        return (ticks/ticksPerRevolution)*driveGearMultiplier*(PI*wheelDiameter);
    }

    /**
     * Move the robot across the playing field.
     * Indicating a negative speed or distance will cause the robot to move in reverse.
     * @param  ticks The distance that we want to travel.
     * @param  speed The speed at which to travel.
     */
    public void goTicks(long ticks, double speed) {

//        long startLeft = robot.getLeftEncoder();
        long startRight = robot.getRightEncoder();

        // Target encoder values for the left and right motors
        long targetRight = startRight + ticks;
//        long targetLeft = startLeft + ticks;

        double leftCorrect	= 1.0;
        double rightCorrect	= 0.95;

        if (ticks < 0) {
            // Set the drive motors to the given speed
            robot.setPowerLeft(speed * leftCorrect);
            robot.setPowerRight(speed * rightCorrect);

            // Wait until both motors have reached the target
            while ( robot.getRightEncoder() > targetRight) {
                mode.telemetry.addData("Data", robot.getRightEncoder());
                mode.telemetry.addData("Encoder target", targetRight);
            }

            // Stop the drive motors here
            robot.setPowerLeft(0);
            robot.setPowerRight(0);
        } else if (ticks > 0){
            // Set the drive motors to the speed (in reverse)
            robot.setPowerLeft(-speed * leftCorrect);
            robot.setPowerRight(-speed * rightCorrect);

            // Wait until both motors have reached the target
            while( robot.getRightEncoder() < targetRight) {
                mode.telemetry.addData("Data2", robot.getRightEncoder());
                mode.telemetry.addData("Encoder target", targetRight);
            }

            // Turn off the drive motors here
            robot.setPowerLeft(0);
            robot.setPowerRight(0);
        }
    }

    /**
     * Turn the robot a certain number of degrees.
     * Indicating a negative degree number will turn the robot clockwise. A positive number will
     * turn the robot counterclockwise.
     * @param  degrees  The distance in degrees to turn.
     * @param  speed    The speed at which to turn.
     */
    public void turnDegrees(double degrees, double speed) throws InterruptedException {

        double degreesSoFar = robot.getGyroHeading();

//        if (Range.clip(degrees ))

        if (degrees > 180) {                                            //left
            robot.setPowerLeft(-1 * speed);
            robot.setPowerRight(speed);
//            mode.telemetry.addData("gyro1", robot.getGyroHeading());
        } else if (degrees < 180) {                                     //right
            robot.setPowerLeft(speed);
            robot.setPowerRight(-1 * speed);
//            mode.telemetry.addData("gyro2", robot.getGyroHeading());
        } else {
            robot.setPowerAll(0);
        }

        // For as long as the current degree measure doesn't equal the target. This will work in the clockwise and
        // counterclockwise directions, since we are comparing the absolute values
        while ((degreesSoFar) > (degrees)) {
            mode.telemetry.addData("gyrocompare", robot.getGyroHeading());
        }

        // Stop all drive motors
        robot.setPowerAll(0);

//        if (abs(degreesSoFar - degrees) > turnOvershootThreshold) {
//            turnDegrees(-1*(degreesSoFar - degrees), 0.50);
//        }
    }

    /**
     * Use the walls of the playing field to square up the robot.
     * @param forwardBackward Boolean true if we are facing the wall, false if we are not.
     */
    void wallAlign(boolean forwardBackward) throws InterruptedException {

        double alignSpeed = 40;

        // If we are going to move forward into the wall
        if (forwardBackward) {
            robot.setPowerAll(alignSpeed);
        } else {
            robot.setPowerAll(-alignSpeed);
        }

        // Store whether the left and right sides are finished aligning
        boolean rightDone = false;
        boolean leftDone = false;

        long leftPrevValue = robot.getLeftEncoder();
        long rightPrevValue = robot.getRightEncoder();
        long stopThreshold = 300;

        while (!rightDone && !leftDone) {
            // Wait for 1 second to give the motors time to move, if they're going to
            wait(100);

            // If the left motor hasn't changed an acceptable amount in the last second, then it has met resistance
            if (abs(leftPrevValue - robot.getLeftEncoder()) < stopThreshold && !leftDone) {
                // Turn the motor off, and indicate that this side is aligned
                robot.setPowerLeft(0);
                leftDone = true;

                // Set the opposite drive motor to full power to finish aligning the robot
                if (!rightDone) {
                   robot.setPowerRight(forwardBackward?100:-100);
                }
            }
            // If the right motor hasn't changed an acceptable amount in the last second, then it has met resistance
            if (abs(rightPrevValue - robot.getRightEncoder()) < stopThreshold && !rightDone) {
                // Turn the motor off, and indicate that this side is aligned
                robot.setPowerRight(0);
                rightDone = true;

                // Set the opposite drive motor to full poewr to finish aligning the robot
                if (!leftDone) {
                    robot.setPowerLeft(forwardBackward?100:-100);
                }
            }

            leftPrevValue = robot.getLeftEncoder();
            rightPrevValue = robot.getRightEncoder();
        }
    }
}

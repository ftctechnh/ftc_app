package org.firstinspires.ftc.teamcode.algorithms.impl;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.IDirectionalDriveTrain;

/**
 * An algorithm that drives to a specified target distance in inches
 * (driving in reverse if necessary) using a distance sensor.
 * This algorithm supports driving forward and backward or left and right to
 * or from the target distance using an {@link IDirectionalDriveTrain}
 *
 * @see DistanceSensor the interface for all distance sensors
 */

public class DistanceSensorDriveAlgorithm {
    private OpMode opMode;

    private IDirectionalDriveTrain driveTrain;
    private DistanceSensor distanceSensor;

    private RobotSide sensorRobotSide;

    private double targetDistance;

    private double speedX;
    private double speedY;

    /**
     * An enumeration type that represents the side of the robot the distance sensor is located.
     */
    public enum RobotSide {
        FRONT(0, 1), BACK(0, -1), LEFT(-1, 0), RIGHT(1, 0);

        private int x;
        private int y;

        RobotSide(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Create a new instance of this algorithm with a reference to the robot,
     * a directional drive train, the distance sensor, and the side of the robot
     * the distance sensor is positioned.
     *
     * @param robot the robot utilizing this algorithm
     * @param distanceSensor the distance sensor that detects the distance to drive to
     * @param sensorRobotSide the side of the robot the distance sensor is located
     */
    public DistanceSensorDriveAlgorithm(Robot robot, IDirectionalDriveTrain driveTrain,
                                        DistanceSensor distanceSensor,
                                        RobotSide sensorRobotSide) {
        this.opMode = robot.getCurrentOpMode();
        this.driveTrain = driveTrain;

        this.distanceSensor = distanceSensor;
        this.sensorRobotSide = sensorRobotSide;
    }

    /**
     * Execute the algorithm to drive to {@code targetDistance} inches
     * at the specified speed using the distance sensor. The algorithm will drive the
     * robot in reverse if {@code targetDistance} is greater than the detected robot distance.
     *
     * @param targetDistance the distance the distance sensor should be from the target
     * @param speed the speed at which to drive
     * @param nonBlocking whether this method should block. If this method is called by a
     *                    non-{@link LinearOpMode}, this method will always be non-blocking, regardless
     *                    of the value for this parameter, due to the nature of non-{@link LinearOpMode}.
     */
    public void driveToDistance(int targetDistance, double speed, boolean nonBlocking) {
        this.speedX = sensorRobotSide.x * speed;
        this.speedY = sensorRobotSide.y * speed;

        this.targetDistance = targetDistance;

        if(nonBlocking || !(opMode instanceof LinearOpMode)) {
            driveToDistanceNonBlocking();
        } else {
            driveToDistanceBlocking();
        }
    }

    private void executionLoop(double distance) {
        if(distance > targetDistance) {
            driveTrain.drive(speedX, speedY);
        } else {
            // change both to negative for opposite direction
            driveTrain.drive(-speedX, -speedY);
        }
    }

    private void driveToDistanceBlocking() {
        double distance;

        LinearOpMode linearOpMode = (LinearOpMode)opMode;
        do {
            distance = distanceSensor.getDistance(DistanceUnit.INCH);
            executionLoop(distance);
        } while(linearOpMode.opModeIsActive() && !(distance == targetDistance));
    }

    private void driveToDistanceNonBlocking() {
        double distance = distanceSensor.getDistance(DistanceUnit.INCH);
        executionLoop(distance);
    }
}

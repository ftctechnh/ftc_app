package org.firstinspires.ftc.teamcode.algorithms.impl;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.algorithms.IGyroPivotAlgorithm;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.IDriveTrain;
import org.firstinspires.ftc.teamcode.mechanism.impl.BNO055IMUWrapper;

/**
 * An implementation of {@link IGyroPivotAlgorithm}
 * utilizing an {@link BNO055IMUWrapper} as the gyroscopic sensor.
 */

public class BNO055IMUGyroPivotAlgorithm implements IGyroPivotAlgorithm {

    private BNO055IMUWrapper imu;
    private IDriveTrain driveTrain;

    private OpMode opMode;

    private double targetAngle;
    private double relativeAngle;
    private double speed;
    private boolean absolute;

    private static final double GYRO_DEGREE_THRESHOLD = 1.0;
    private static final double P_GYRO_TURN_COEFF = 0.01;

    /**
     * Create a new instance of this algorithm implementation that will use the specified robot.
     *
     * @param robot the Robot instance using this gyro-pivot algorithm
     * @param imu the imu wrapper object to be used as the gyroscope sensor
     */
    public BNO055IMUGyroPivotAlgorithm(Robot robot, IDriveTrain driveTrain, BNO055IMUWrapper imu) {
        this.opMode = robot.getCurrentOpMode();
        this.driveTrain = driveTrain;

        this.imu = imu;
    }

    @Override
    public void pivot(double speed, double angle, boolean absolute, boolean nonBlocking) {
        this.speed = speed;
        this.targetAngle = angle;
        this.absolute = absolute;

        if(nonBlocking || !(opMode instanceof LinearOpMode)) {
            pivotNonBlocking();
        } else {
            pivotBlocking();
        }
    }

    private double getRelativeAngle(double targetAngle, boolean absolute) {
        double result = targetAngle - imu.getHeading();

        // compensate from robot's targetAngle to the zero degree
        if(!absolute) {
            result -= getRelativeAngle(0, false);
        }

        return result;
    }

    private void executionLoop() {
        double steer = Range.clip(relativeAngle * P_GYRO_TURN_COEFF , -1, 1);
        driveTrain.pivot(speed * steer);

        opMode.telemetry.addData("targetAngle to target", relativeAngle);
        opMode.telemetry.addData("Z axis difference from targetAngle", relativeAngle);
        opMode.telemetry.update();
    }

    private void pivotBlocking() {
        LinearOpMode linearOpMode = (LinearOpMode)opMode;
        do {
            relativeAngle = getRelativeAngle(targetAngle, absolute);
            executionLoop();
        } while(linearOpMode.opModeIsActive() && Math.abs(relativeAngle) > GYRO_DEGREE_THRESHOLD);

        // when we're on target, stop the robot
        driveTrain.stopDriveMotors();
    }

    private void pivotNonBlocking() {
        relativeAngle = getRelativeAngle(targetAngle, absolute);
        if(Math.abs(relativeAngle) > GYRO_DEGREE_THRESHOLD) {
            executionLoop();
        }
    }
}

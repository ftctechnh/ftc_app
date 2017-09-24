package org.firstinspires.ftc.teamcode.mechanism.drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HDriveTrain implements IDirectionalDriveTrain {

    private static final int WHEEL_DIAMETER_INCHES = 4;

    private static final int COUNTS_PER_MOTOR_REV = 1120;

    private static final double GYRO_ERROR_THRESHOLD = 1.0;

    private static final double P_GYRO_TURN_COEFF = 0.01;
    private static final double P_GYRO_DRIVE_COEFF = 0.008;

    protected static final int COUNTS_PER_INCH = (int)(COUNTS_PER_MOTOR_REV /
            (WHEEL_DIAMETER_INCHES * Math.PI));


    private DcMotor frontLeft, frontRight, backLeft, backRight, middleLeft, middleRight;

    @Override
    public void pivot(double pivotSpeed) {

    }

    @Override
    public void drive(double speedY, int targetDistance) {

    }

    @Override
    public void stopDriveMotors() {

    }

    @Override
    public void enableEncoders(boolean enable) {

    }

    @Override
    public boolean encodersEnabled() {
        return false;
    }

    @Override
    public void initialize(LinearOpMode opMode) {
        HardwareMap hWMap = opMode.hardwareMap;

        frontLeft = hWMap.dcMotor.get("fl");
        frontRight = hWMap.dcMotor.get("fr");
        backRight = hWMap.dcMotor.get("br");
        backLeft = hWMap.dcMotor.get("bl");
        middleLeft = hWMap.dcMotor.get("ml");
        middleRight = hWMap.dcMotor.get("mr");

    }

    @Override
    public void directionalDrive(double speedX, double speedY, double pivotSpeed, int targetDistance) {
        middleLeft.setPower(speedX);
        middleRight.setPower(speedX);
        frontLeft.setPower(speedY);
        frontRight.setPower(speedY);
        backRight.setPower(speedY);
        backLeft.setPower(speedY);


        int encoderTargetCounts = COUNTS_PER_INCH * targetDistance;



    }
}

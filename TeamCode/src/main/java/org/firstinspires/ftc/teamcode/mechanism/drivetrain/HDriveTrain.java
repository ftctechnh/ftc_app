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
    private LinearOpMode opMode;
    private DcMotor.RunMode mode;

    @Override
    public void pivot(double pivotSpeed) {
        frontLeft.setPower(pivotSpeed);
        frontRight.setPower(-pivotSpeed);
        backLeft.setPower(pivotSpeed);
        backRight.setPower(-pivotSpeed);

        middleLeft.setPower(0);
        middleRight.setPower(0);
    }

    @Override
    public void drive(double speedY, int targetDistance) {
        this.drive(0, speedY);
    }

    @Override
    public void stopDriveMotors() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        middleLeft.setPower(0);
        middleRight.setPower(0);
    }

    @Override
    public void setRunMode(DcMotor.RunMode runMode) {
        frontLeft.setMode(runMode);
        frontRight.setMode(runMode);
        backLeft.setMode(runMode);
        backRight.setMode(runMode);

        middleRight.setMode(runMode);
        middleLeft.setMode(runMode);
        this.mode = runMode;
    }

    @Override
    public DcMotor.RunMode getRunMode() {
        return mode;
    }

    @Override
    public void initialize(LinearOpMode opMode) {
        this.opMode = opMode;
        HardwareMap hWMap = opMode.hardwareMap;

        frontLeft = hWMap.dcMotor.get("fl");
        frontRight = hWMap.dcMotor.get("fr");
        backRight = hWMap.dcMotor.get("br");
        backLeft = hWMap.dcMotor.get("bl");
        middleLeft = hWMap.dcMotor.get("ml");
        middleRight = hWMap.dcMotor.get("mr");

    }


    @Override
    public void directionalDrive(double angleDegrees, double speed, int targetDistance) {

        int encoderTargetCounts = COUNTS_PER_INCH * targetDistance;
        double angleRadians = Math.toRadians(angleDegrees);
        int lateralCounts= encoderTargetCounts * (int)Math.sin(angleRadians);
        int axialCounts = encoderTargetCounts * (int)Math.cos(angleRadians);

        // RUN_TO_POSITION)=run to a set distance rather than
        // using encoders or run with a certain voltage
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        // setting wheels to a distance defined by a custom unit of "encoder counts"
        frontLeft.setTargetPosition(lateralCounts);
        frontRight.setTargetPosition(lateralCounts);
        backRight.setTargetPosition(lateralCounts);
        backLeft.setTargetPosition(lateralCounts);

        middleLeft.setTargetPosition(axialCounts);
        middleRight.setTargetPosition(axialCounts);

        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);
        middleLeft.setPower(speed);
        middleRight.setPower(speed);

        while(opMode.opModeIsActive() && areDriveMotorsBusy()) {
            opMode.idle();
        }

        stopDriveMotors();
    }

    private boolean areDriveMotorsBusy() {
        return frontLeft.isBusy() && frontRight.isBusy()
                && backLeft.isBusy() && backRight.isBusy()
                && middleLeft.isBusy() && middleRight.isBusy();
    }

    @Override
    public void drive(double speedX, double speedY) {
        frontLeft.setPower(speedY);
        frontRight.setPower(speedY);
        backLeft.setPower(speedY);
        backRight.setPower(speedY);

        middleRight.setPower(speedX);
        middleLeft.setPower(speedX);
    }
}

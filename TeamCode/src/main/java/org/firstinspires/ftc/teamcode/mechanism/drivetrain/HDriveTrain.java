package org.firstinspires.ftc.teamcode.mechanism.drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HDriveTrain implements IDirectionalDriveTrain {

    private static final int WHEEL_DIAMETER_INCHES = 4;

    private static final int COUNTS_PER_MOTOR_REV = 1120;

    protected static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV /
            (WHEEL_DIAMETER_INCHES * Math.PI);

    private DcMotor frontLeft, frontRight, backLeft, backRight, middleLeft, middleRight;
    private OpMode opMode;
    private DcMotor.RunMode mode;
    private boolean isRunningToPosition;

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
    public void initialize(OpMode opMode) {
        this.opMode = opMode;
        HardwareMap hWMap = opMode.hardwareMap;

        frontLeft = hWMap.dcMotor.get("fl");
        frontRight = hWMap.dcMotor.get("fr");
        backRight = hWMap.dcMotor.get("br");
        backLeft = hWMap.dcMotor.get("bl");
        middleLeft = hWMap.dcMotor.get("ml");
        middleRight = hWMap.dcMotor.get("mr");
    }

    private void setDirectionalTargetPosition(double angleDegrees, double speed, int targetDistance) {

        double encoderTargetCounts = COUNTS_PER_INCH * targetDistance;
        double angleRadians = Math.toRadians(angleDegrees);
        int lateralCounts = (int)(encoderTargetCounts * Math.sin(angleRadians));
        int axialCounts = (int)(encoderTargetCounts * Math.cos(angleRadians));

        // RUN_TO_POSITION = run to a set distance rather than
        // using encoders or run with a certain voltage
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        // setting wheels to a distance defined by a custom unit of "encoder counts"
        frontLeft.setTargetPosition(lateralCounts);
        frontRight.setTargetPosition(lateralCounts);
        backRight.setTargetPosition(lateralCounts);
        backLeft.setTargetPosition(lateralCounts);

        middleLeft.setTargetPosition(axialCounts);
        middleRight.setTargetPosition(axialCounts);

        // set motor powers
        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);
        middleLeft.setPower(speed);
        middleRight.setPower(speed);
    }

    @Override
    public void directionalDrive(double angleDegrees, double speed, int targetDistance) {
        if(!(opMode instanceof LinearOpMode)) {
            directionalDriveAsync(angleDegrees, speed, targetDistance);
        } else {
            directionalDriveSync(angleDegrees, speed, targetDistance);
        }
    }

    private void directionalDriveAsync(double angleDegrees, double speed, int targetDistance) {
        if(!this.isRunningToPosition) {
            setDirectionalTargetPosition(angleDegrees, speed, targetDistance);
            this.isRunningToPosition = true;
        } else if(!areDriveMotorsBusy()) {
            stopDriveMotors();
            this.isRunningToPosition = false;
        }
    }

    private void directionalDriveSync(double angleDegrees, double speed, int targetDistance) {
        setDirectionalTargetPosition(angleDegrees, speed, targetDistance);

        LinearOpMode linearOpMode = (LinearOpMode)opMode;
        while(linearOpMode.opModeIsActive() && areDriveMotorsBusy()) {
            linearOpMode.idle();
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

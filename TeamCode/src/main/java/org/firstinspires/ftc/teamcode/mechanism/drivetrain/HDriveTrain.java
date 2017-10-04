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

    private DcMotor leftDrive, rightDrive, middleDrive;
    private OpMode opMode;
    private DcMotor.RunMode mode;
    private boolean isRunningToPosition;

    @Override
    public void pivot(double pivotSpeed) {
        leftDrive.setPower(pivotSpeed);
        rightDrive.setPower(-pivotSpeed);

        middleDrive.setPower(0);
    }

    @Override
    public void drive(double speedY, int targetDistance) {
        this.drive(0, speedY);
    }

    @Override
    public void stopDriveMotors() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        middleDrive.setPower(0);
    }

    @Override
    public void setRunMode(DcMotor.RunMode runMode) {
        leftDrive.setMode(runMode);
        rightDrive.setMode(runMode);
        middleDrive.setMode(runMode);

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

        leftDrive = hWMap.dcMotor.get("l");
        rightDrive = hWMap.dcMotor.get("r");
        middleDrive = hWMap.dcMotor.get("m");
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
        leftDrive.setTargetPosition(lateralCounts);
        rightDrive.setTargetPosition(lateralCounts);

        middleDrive.setTargetPosition(axialCounts);

        // set motor powers
        leftDrive.setPower(speed);
        rightDrive.setPower(speed);
        middleDrive.setPower(speed);
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
        return leftDrive.isBusy() && rightDrive.isBusy() && middleDrive.isBusy();
    }

    @Override
    public void drive(double speedX, double speedY) {
        leftDrive.setPower(speedY);
        rightDrive.setPower(speedY);

        middleDrive.setPower(speedX);
    }
}

package org.firstinspires.ftc.teamcode.mechanism.drivetrain.impl;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.IDirectionalDriveTrain;

/**
 * This class implements control of an H-drive train, which has four wheels parallel to the
 * sides of the robot and one (or sometimes two) wheels in the middle perpendicular to the sides of the robot.
 * These wheels are driven by three motors.
 */

public class HDriveTrain implements IDirectionalDriveTrain {

    private DcMotor leftDrive, rightDrive, middleDrive;
    private DcMotor.RunMode mode;

    private OpMode opMode;

    private boolean isRunningToPosition;

    // currentSpeedX isn't need because it is only handled in the drive() method
    private double currentSpeedY;
    private double currentPivot;

    private double lateralCountsPerInch;
    private double axialCountsPerInch;

    private double insideWheelGearing;
    private double outsideWheelGearing;

    /**
     * Construct a new {@link HDriveTrain} with a reference to the utilizing robot.
     *
     * @param robot the robot using this drive train
     */
    public HDriveTrain(Robot robot, int wheelDiameterInches,
                       double insideWheelGearing, double outsideWheelGearing) {

        this.opMode = robot.getCurrentOpMode();
        HardwareMap hWMap = opMode.hardwareMap;

        leftDrive = hWMap.dcMotor.get("l");
        rightDrive = hWMap.dcMotor.get("r");
        middleDrive = hWMap.dcMotor.get("m");

        this.outsideWheelGearing = outsideWheelGearing;
        this.insideWheelGearing = insideWheelGearing;

        this.lateralCountsPerInch = (leftDrive.getMotorType().getTicksPerRev() /
                (wheelDiameterInches * Math.PI)) / outsideWheelGearing;

        this.axialCountsPerInch = middleDrive.getMotorType().getTicksPerRev() /
                (wheelDiameterInches * Math.PI) / insideWheelGearing;

        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set all motors to brake
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        middleDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void drive(double speedY, int targetDistance) {
        this.drive(0, speedY);
    }

    @Override
    public void pivot(double pivotSpeed) {
        this.currentPivot = pivotSpeed;

        leftDrive.setPower(this.currentSpeedY + pivotSpeed);
        rightDrive.setPower(this.currentSpeedY - pivotSpeed);
    }

    @Override
    public void stopDriveMotors() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        middleDrive.setPower(0);
    }

    private void setRunMode(DcMotor.RunMode runMode) {
        leftDrive.setMode(runMode);
        rightDrive.setMode(runMode);
        middleDrive.setMode(runMode);

        this.mode = runMode;
    }

    private void setDirectionalTargetPosition(double angleDegrees, double speed, int targetDistance) {
        double encoderTargetCountsLateral = lateralCountsPerInch * -targetDistance;
        double encoderTargetCountsAxial = axialCountsPerInch * -targetDistance;

        double angleRadians = Math.toRadians(angleDegrees);
        int lateralCounts = (int)(encoderTargetCountsLateral * Math.sin(angleRadians));
        int axialCounts = (int)(encoderTargetCountsAxial * Math.cos(angleRadians));

        opMode.telemetry.addData("lateral counts", lateralCounts);
        opMode.telemetry.addData("axial counts", axialCounts);
        opMode.telemetry.update();

        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setTargetPosition(lateralCounts);
        rightDrive.setTargetPosition(lateralCounts);

        middleDrive.setTargetPosition(axialCounts);

        // set motor powers
        leftDrive.setPower(Range.clip(speed * insideWheelGearing, -1, 1));
        rightDrive.setPower(Range.clip(speed * insideWheelGearing, -1, 1));
        middleDrive.setPower(Range.clip(speed * outsideWheelGearing, -1, 1));
    }

    @Override
    public void directionalDrive(double angleDegrees, double speed, int targetDistance, boolean nonBlocking) {
        if(nonBlocking || !(opMode instanceof LinearOpMode)) {
            directionalDriveNonBlocking(angleDegrees, speed, targetDistance);
        } else {
            directionalDriveBlocking(angleDegrees, speed, targetDistance);
        }
    }

    private void directionalDriveNonBlocking(double angleDegrees, double speed, int targetDistance) {
        if(!this.isRunningToPosition) {
            setDirectionalTargetPosition(angleDegrees, speed, targetDistance);
            this.isRunningToPosition = true;
        } else if(!isDriveTrainBusy()) {
            this.isRunningToPosition = false;
            stopDriveMotors();
        }
    }

    private void directionalDriveBlocking(double angleDegrees, double speed, int targetDistance) {
        setDirectionalTargetPosition(angleDegrees, speed, targetDistance);

        LinearOpMode linearOpMode = (LinearOpMode)opMode;
        while(linearOpMode.opModeIsActive() && isDriveTrainBusy()) {
            linearOpMode.idle();
        }
        stopDriveMotors();
    }

    @Override
    public boolean isDriveTrainBusy() {
        return (leftDrive.isBusy() && rightDrive.isBusy()) || middleDrive.isBusy();
    }

    @Override
    public void drive(double speedX, double speedY) {
        this.currentSpeedY = speedY;

        leftDrive.setPower(this.currentPivot + speedY);
        rightDrive.setPower(-this.currentPivot + speedY);

        middleDrive.setPower(speedX);
    }
}

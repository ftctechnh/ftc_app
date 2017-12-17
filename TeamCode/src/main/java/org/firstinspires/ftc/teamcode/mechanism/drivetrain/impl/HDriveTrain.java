package org.firstinspires.ftc.teamcode.mechanism.drivetrain.impl;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.IDirectionalDriveTrain;

/**
 * This class implements control of an H-drive train, which has four wheels parallel to the
 * sides of the robot and one (or two) wheels in the middle perpendicular to the sides of the robot.
 * These wheels are driven by three motors.
 */

public class HDriveTrain implements IDirectionalDriveTrain {

    private DcMotor leftDrive, rightDrive, middleDrive;
    private DcMotor.RunMode mode;

    private OpMode opMode;

    private boolean isRunningToPosition;

    private double currentSpeedY;
    private double currentPivot;

    private double lateralCountsPerInch;
    private double axialCountsPerInch;

    private double insideWheelGearing;
    private double outsideWheelGearing;
    private double wheelDiameterInches;

    /**
     * A nested class used to construct {@link HDriveTrain} instances.
     * The builder pattern is utilized here to simplify the construction of rather complex
     * {@link HDriveTrain} objects with differing gearing ratios and motor directions.
     */
    public static class Builder {
        private Robot robot;
        private HDriveTrain hDriveTrain;

        /**
         * Create a new builder object with a reference to the utilizing robot.
         *
         * @param robot the robot that will using the resulting {@link HDriveTrain}
         *              instance returned by calling {@link #build()}.
         */
        public Builder(Robot robot) {
            this.robot = robot;
            this.hDriveTrain = new HDriveTrain(robot);
        }

        private void updateCountsPerInch() {
            hDriveTrain.lateralCountsPerInch = (hDriveTrain.leftDrive.getMotorType().getTicksPerRev() /
                    (hDriveTrain.wheelDiameterInches * Math.PI)) / hDriveTrain.outsideWheelGearing;

            hDriveTrain.axialCountsPerInch = hDriveTrain.middleDrive.getMotorType().getTicksPerRev() /
                    (hDriveTrain.wheelDiameterInches * Math.PI) / hDriveTrain.insideWheelGearing;
        }

        /**
         * Return the built {@link HDriveTrain} object.
         *
         * @return the final, constructed {@link HDriveTrain} object.
         */
        public HDriveTrain build() {
            return hDriveTrain;
        }

        /**
         * Set the gear ratio for the inside (i.e. middle) wheel(s), which will effect encoder
         * distance calculations, for example in {@link #directionalDrive(double, double, int, boolean)}.
         *
         * @param insideWheelGearing the inside gearing ratio represented as a double. For example, a 1:2 gear
         *                           ratio would be {@code 0.5}. Likewise, a 2:1 ratio is {@code 2.0}.
         * @return this builder object so that method calls can be chained
         */
        public Builder setInsideWheelGearingRatio(double insideWheelGearing) {
            hDriveTrain.insideWheelGearing = insideWheelGearing;
            updateCountsPerInch();
            return this;
        }

        /**
         * Set the gear ratio for the outside (i.e. front left, back right, etc) wheels, which will effect encoder
         * distance calculations, for example in {@link #directionalDrive(double, double, int, boolean)}.
         *
         * @param outSideWheelGearing the outside gearing ratio represented as a double. For example, a 1:2 gear
         *                           ratio would be {@code 0.5}. Likewise, a 2:1 ratio is {@code 2.0}.
         * @return this builder object so that method calls can be chained
         */
        public Builder setOutsideWheelGearingRatio(double outSideWheelGearing) {
            hDriveTrain.outsideWheelGearing = outSideWheelGearing;
            updateCountsPerInch();
            return this;
        }

        /**
         * Set the diameter in inches of the drive wheels.
         *
         * @param wheelDiameterInches the diameter in inches of each drive wheel
         *
         * @return this builder object so that method calls can be chained
         */
        public Builder setWheelDiameterInches(double wheelDiameterInches) {
            hDriveTrain.wheelDiameterInches = wheelDiameterInches;
            updateCountsPerInch();
            return this;
        }

        /**
         * Set the direction of the left drive train motor.
         *
         * @see DcMotor.Direction for more documentation on the direction enumeration type
         * @param direction the direction the left motor should drive. Please note that reverse does not
         *                  necessarily correspond to counterclockwise and likewise for clockwise.
         *
         * @return this builder object so that method calls can be chained
         */
        public Builder setLeftMotorDirection(DcMotor.Direction direction) {
            hDriveTrain.leftDrive.setDirection(direction);
            return this;
        }

        /**
         * Set the direction of the right drive train motor.
         *
         * @see DcMotor.Direction for more documentation on the direction enumeration type
         * @param direction the direction the right motor should drive. Please note that reverse does not
         *                  necessarily correspond to counterclockwise and likewise for clockwise.
         *
         * @return this builder object so that method calls can be chained
         */
        public Builder setRightMotorDirection(DcMotor.Direction direction) {
            hDriveTrain.leftDrive.setDirection(direction);
            return this;
        }

        /**
         * Set the direction of the middle drive train motor.
         *
         * @see DcMotor.Direction for more documentation on the direction enumeration type
         * @param direction the direction the middle motor should drive. Please note that reverse does not
         *                  necessarily correspond to counterclockwise and likewise for clockwise.
         *
         * @return this builder object so that method calls can be chained
         */
        public Builder setMiddleMotorDirection(DcMotor.Direction direction) {
            hDriveTrain.leftDrive.setDirection(direction);
            return this;
        }
    }

    private HDriveTrain(Robot robot) {
        this.opMode = robot.getCurrentOpMode();
        HardwareMap hWMap = opMode.hardwareMap;

        leftDrive = hWMap.dcMotor.get("l");
        rightDrive = hWMap.dcMotor.get("r");
        middleDrive = hWMap.dcMotor.get("m");

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

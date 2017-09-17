package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_Strafe implements StepInterface {

    private final Integer distanceEncoderCounts;
    private final Robot.StrafeEnum strafeDirection;
    private final Robot.MotorPowerEnum motorPowerEnum;

    private Robot robot;

    private boolean resetMotors_DoneFlag = false;
    private boolean initializedMotors_DoneFlag = false;

    private final int DONE_TOLERANCE = 100;


    private static final double MOTOR_POWER_BALANCE_FACTOR = 1.0;


    // Constructor, called to create an instance of this class.
    public Step_Strafe(Integer distanceEncoderCounts, Robot.StrafeEnum strafeDirection) {
        this.distanceEncoderCounts = distanceEncoderCounts;
        this.strafeDirection = strafeDirection;
        this.motorPowerEnum = Robot.MotorPowerEnum.Low;
    }


    @Override
    public String getLogKey() {
        return "Step_Strafe";
    }


    @Override
    public void runStep() {

        resetEncodersAndStopMotors_Only_Once();

        logEncoders("run");

        if (robot.isStillWaiting()) {
            return;
        }


        initializeMotors_Only_Once();

        robot.runWithEncoders_MAINTAINS_SPEED();

        if (robot.isStillWaiting()) {
            return;
        }

        strafe();

    }


    private DcMotor getEncoderMotor() {

        if (Robot.StrafeEnum.RIGHT.equals(this.strafeDirection)) {

            return robot.motorRightRear;
        } else {

            return robot.motorLeftRear;
        }
    }

    private void strafe() {

        double motorPower = determineMotorPower();

        if (Robot.StrafeEnum.RIGHT.equals(this.strafeDirection)) {

            robot.motorRightFront.setPower(-motorPower);
            robot.motorRightRear.setPower(motorPower);
            robot.motorLeftFront.setPower(motorPower);
            robot.motorLeftRear.setPower(-motorPower);

        } else {

            robot.motorRightFront.setPower(motorPower);
            robot.motorRightRear.setPower(-motorPower);
            robot.motorLeftFront.setPower(-motorPower);
            robot.motorLeftRear.setPower(motorPower);
        }

    }

    private double determineMotorPower() {


        return this.motorPowerEnum.getValue();

    }


    private void resetEncodersAndStopMotors_Only_Once() {

        if (resetMotors_DoneFlag == false) {

            robot.resetEncodersAndStopMotors();
            resetMotors_DoneFlag = true;

            robot.setLoopDelay();
        }

    }


    private void initializeMotors_Only_Once() {


        if (initializedMotors_DoneFlag == false) {

            robot.setDriveMotorForwardDirection();

            robot.runWithEncoders_MAINTAINS_SPEED();

            robot.setLoopDelay();
            initializedMotors_DoneFlag = true;

            logIt("initializeMotors_Only_Once ...");
        }
    }


    private int getRemainingDistance() {

        DcMotor encoderMotor = getEncoderMotor();

        return Math.abs(distanceEncoderCounts - encoderMotor.getCurrentPosition());
    }


    @Override
    public boolean isStepDone() {

        if (robot.isStillWaiting() || resetMotors_DoneFlag == false || initializedMotors_DoneFlag == false) {
            return false;
        }


        if (isDistanceDone() || robot.touchSensor.isPressed()) {

            logIt("Step is Done:");

            logEncoders(">done");

            robot.resetEncodersAndStopMotors();

            robot.motorRightFront.setPower(0);
            robot.motorRightRear.setPower(0);
            robot.motorLeftFront.setPower(0);
            robot.motorLeftRear.setPower(0);
            logEncoders("<done");
            return true;
        }

        return false;
    }

    private boolean isDistanceDone() {

        return Math.abs(getRemainingDistance()) <= DONE_TOLERANCE;
    }


    private void logEncoders(String keySuffix) {

        String key = getLogKey() + keySuffix;

        Log.i(key, "--------------------------------------------------------------------------------------");
        Log.i(key, " CurrentPosition:" + robot.motorLeftFront.getCurrentPosition() + " --- " + robot.motorRightFront.getCurrentPosition());
        Log.i(key, " CurrentPosition:" + robot.motorLeftRear.getCurrentPosition() + " --- " + robot.motorRightRear.getCurrentPosition());
        Log.i(key, "----------------------");
        Log.i(key, " speed:" + robot.motorLeftFront.getPower() + " --- " + robot.motorRightFront.getPower());
        Log.i(key, " speed:" + robot.motorLeftRear.getPower() + " --- " + robot.motorRightRear.getPower());
        Log.i(key, "--------------------------------------------------------------------------------------");
    }


    private void logIt(String keySuffix) {

        String key = getLogKey() + keySuffix;

        StringBuilder sb = new StringBuilder();
        sb.append(" Remaining:" + this.getRemainingDistance());
        sb.append(" speed:" + this.motorPowerEnum.getValue());
        Log.i(key, sb.toString());

    }


    @Override
    public boolean isAborted() {
        return false;
    }


    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }


}

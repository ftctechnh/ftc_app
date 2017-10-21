package org.firstinspires.ftc.teamcode.sabbotage.relic.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.sabbotage.relic.autonomous.internal.AutonomousOp;
import org.firstinspires.ftc.teamcode.sabbotage.relic.robot.Robot;

public class Step_Straight implements AutonomousOp.StepInterface {

    private final Integer distanceEncoderCounts;
    private final Robot.DirectionEnum direction;
    private final Robot.MotorPowerEnum motorPowerEnum;

    private Robot robot;

    private boolean resetMotors_DoneFlag = false;
    private boolean initializedMotors_DoneFlag = false;

    private final int SLOW_MODE_REMAINING_DISANCE = 1100;

    private final int DONE_TOLERANCE = 100;


    private static final double MOTOR_POWER_BALANCE_FACTOR = 1.0;

    // Constructor, called to create an instance of this class.
    public Step_Straight(Integer distanceEncoderCounts, Robot.DirectionEnum direction) {

        this.distanceEncoderCounts = distanceEncoderCounts;
        this.direction = direction;
        this.motorPowerEnum = Robot.MotorPowerEnum.High;
    }


    @Override
    public String getLogKey() {
        return "Step_Straight";
    }




    @Override
    public void runStep() {

        resetEncodersAndStopMotors_Only_Once();

        logEncoders("run");

        if (robot.isStillWaiting()) {
            return;
        }

        initializeMotors_Only_Once();

        if (robot.isStillWaiting()) {
            return;
        }

        goStraight();


    }
    private DcMotor getEncoderMotor() {

        return robot.motorDriveRight;

    }
    private void goStraight() {

        double motorPower = determineMotorPower();

        robot.motorDriveLeft.setPower(motorPower);
        robot.motorDriveRight.setPower(motorPower);

    }


    private double determineMotorPower() {


        int remainingDistance = getRemainingDistance();


        if (remainingDistance < SLOW_MODE_REMAINING_DISANCE) {

            return limitMinValue(this.motorPowerEnum.getValue() * remainingDistance / SLOW_MODE_REMAINING_DISANCE);
        }


        return this.motorPowerEnum.getValue();

    }

    private double limitMinValue(double input) {

        if (input < .1) {

            return .1;
        }

        return input;
    }

    private void resetEncodersAndStopMotors_Only_Once() {

        if (!resetMotors_DoneFlag) {

            robot.resetEncodersAndStopMotors();
            resetMotors_DoneFlag = true;

            robot.setLoopDelay();
        }

    }


    private void initializeMotors_Only_Once() {


        if (!initializedMotors_DoneFlag) {

            initializeMotorDirection();


            robot.runWithEncoders_MAINTAINS_SPEED();

            robot.setLoopDelay();
            initializedMotors_DoneFlag = true;

            logIt("initializeMotors_Only_Once ...");


        }
    }

    private void initializeMotorDirection() {

        if (Robot.DirectionEnum.FORWARD.equals(this.direction)) {

            robot.setDriveMotorForwardDirection();

        } else {

            robot.setDriveMotorReverseDirection();

        }

    }

    private int getRemainingDistance() {

        DcMotor encoderMotor = getEncoderMotor();

        return Math.abs(distanceEncoderCounts - encoderMotor.getCurrentPosition());
    }


    @Override
    public boolean isStepDone() {

        if (robot.isStillWaiting() || !resetMotors_DoneFlag || !initializedMotors_DoneFlag) {
            return false;
        }


        if (isDistanceDone()) {

            logIt("Step is Done:");

            logEncoders(">done");
            robot.resetEncodersAndStopMotors();

            robot.motorDriveRight.setPower(0);
            robot.motorDriveLeft.setPower(0);
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
        Log.i(key, " CurrentPosition:" + robot.motorDriveRight.getCurrentPosition() + " --- " + robot.motorDriveLeft.getCurrentPosition());
        Log.i(key, "----------------------");
        Log.i(key, " speed:" + robot.motorDriveLeft.getPower() + " --- " + robot.motorDriveRight.getPower());
        Log.i(key, "--------------------------------------------------------------------------------------");
    }

    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName);
        sb.append(" CurrentPosition:" + robot.motorDriveLeft.getCurrentPosition());
        sb.append(" Target:" + robot.motorDriveLeft.getTargetPosition());
        sb.append(" Remaining:" + this.getRemainingDistance());
        Log.i(getLogKey(), sb.toString());

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

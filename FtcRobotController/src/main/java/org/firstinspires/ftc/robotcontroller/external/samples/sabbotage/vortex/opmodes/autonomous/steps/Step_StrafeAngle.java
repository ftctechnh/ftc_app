package org.firstinspires.ftc.robotcontroller.external.samples.sabbotage.vortex.opmodes.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.external.samples.sabbotage.vortex.robot.Robot;

public class Step_StrafeAngle implements StepInterface {

    private final Integer distanceEncoderCounts;
    private final Robot.DirectionEnum direction;
    private final Robot.StrafeEnum strafe;
    private final Robot.MotorPowerEnum motorPowerEnum;

    private Robot robot;

    private boolean resetMotors_DoneFlag = false;
    private boolean initializedMotors_DoneFlag = false;


    private final int DONE_TOLERANCE = 100;


    // Constructor, called to create an instance of this class.
    public Step_StrafeAngle(Integer distanceEncoderCounts, Robot.DirectionEnum direction, Robot.StrafeEnum strafe) {

        this.distanceEncoderCounts = distanceEncoderCounts;
        this.direction = direction;
        this.motorPowerEnum = Robot.MotorPowerEnum.Med;
        this.strafe = strafe;
    }


    @Override
    public String getLogKey() {
        return "Step_StrafeAngle";
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

        strafe();


    }

    private DcMotor getEncoderMotor() {

        if (Robot.StrafeEnum.RIGHT.equals(this.strafe)) {

            return robot.motorRightRear;
        } else {

            return robot.motorLeftRear;
        }
    }

    private void strafe() {

        double motorPower = determineMotorPower();

        if (Robot.StrafeEnum.RIGHT.equals(this.strafe)) {

            robot.motorRightFront.setPower(0);
            robot.motorRightRear.setPower(motorPower);
            robot.motorLeftFront.setPower(motorPower);
            robot.motorLeftRear.setPower(0);
        } else {
            getRobot().motorRightFront.setPower(motorPower);
            getRobot().motorRightRear.setPower(0);
            getRobot().motorLeftFront.setPower(0);
            getRobot().motorLeftRear.setPower(motorPower);

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

        if (robot.isStillWaiting() || resetMotors_DoneFlag == false || initializedMotors_DoneFlag == false) {
            return false;
        }


        if (isDistanceDone()) {

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

    protected void logIt(String keySuffix) {

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


    protected Robot getRobot() {
        return this.robot;
    }

}

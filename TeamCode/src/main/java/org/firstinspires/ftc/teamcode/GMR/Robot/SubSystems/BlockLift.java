package org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by pston on 11/12/2017
 */

public class BlockLift {

    private DcMotor liftMotor;
    private DcMotor leftGrab;
    private DcMotor rightGrab;

    private int slidePosition = 1;
    private boolean buttonPressed = false;

    private int currentLiftPosition;
    private int automaticLiftPosition;

    private LiftMode liftMode = LiftMode.AUTOMATIC;

    private boolean topButtonPress = false;
    private boolean bottomButtonPress = false;

    private boolean autoLift = false;

    public BlockLift(DcMotor liftMotor, DcMotor leftGrab, DcMotor rightGrab) {

        this.liftMotor = liftMotor;
        this.leftGrab = leftGrab;
        this.rightGrab = rightGrab;

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftGrab.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightGrab.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void slideHeight(boolean bumper, float trigger, boolean a, boolean b, boolean y, Telemetry telemetry) {

            if (bumper && liftMotor.getCurrentPosition() < 4400) {
                currentLiftPosition = liftMotor.getCurrentPosition();
                liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                liftMotor.setPower(1);
                autoLift = false;
            } else if (trigger > 0 && liftMotor.getCurrentPosition() > 0) {
                currentLiftPosition = liftMotor.getCurrentPosition();
                liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                liftMotor.setPower(-1);
                autoLift = false;
            } else if (a) {
                currentLiftPosition = 0;
                autoLift = true;
            } else if (b) {
                currentLiftPosition = 2062;
                autoLift = true;
            } else if (y) {
                currentLiftPosition = 4050;
                autoLift = true;
            } else {
                if (autoLift) {
                    liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    liftMotor.setPower(1);
                    liftMotor.setTargetPosition(currentLiftPosition);
                } else if ((Math.abs(Math.abs(currentLiftPosition) - Math.abs(liftMotor.getCurrentPosition()))) > 10) {
                    currentLiftPosition = liftMotor.getCurrentPosition();
                    liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    liftMotor.setTargetPosition(currentLiftPosition);
                } else {
                    liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    liftMotor.setTargetPosition(currentLiftPosition);
                }
            }
            telemetry.addData("Current Lift Goal: ", currentLiftPosition);
            telemetry.addData("Current Actual Position", liftMotor.getCurrentPosition());
    }

    public void lift(boolean bumper, float trigger, Telemetry telemetry) {
        if (bumper /*&& liftMotor.getCurrentPosition() < 0*/) {
            currentLiftPosition = liftMotor.getCurrentPosition();
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftMotor.setPower(1);
        } else if (trigger > 0 /*&& liftMotor.getCurrentPosition() > -4400*/) {
            currentLiftPosition = liftMotor.getCurrentPosition();
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftMotor.setPower(-1);
        } else {
            if ((Math.abs(Math.abs(currentLiftPosition) - Math.abs(liftMotor.getCurrentPosition()))) > 10)  {
                currentLiftPosition = liftMotor.getCurrentPosition();
                liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftMotor.setTargetPosition(currentLiftPosition);
            } else {
                liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftMotor.setTargetPosition(currentLiftPosition);
            }
        }
        telemetry.addData("Current Lift Goal: ", currentLiftPosition);
        telemetry.addData("Current Actual Position", liftMotor.getCurrentPosition());
    }

    public void setLift(int height) {
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(-1);
        liftMotor.setTargetPosition(liftMotor.getCurrentPosition() - height);
    }

    public void grab(boolean bumper, float trigger) {
        if(bumper  ) {
            leftGrab.setPower(1);
            rightGrab.setPower(-1);
        } else if (trigger > 0) {
            leftGrab.setPower(-1);
            rightGrab.setPower(1);
        } else {
            leftGrab.setPower(0);
            rightGrab.setPower(0);
        }

    }

    public void hold(boolean hold) {
        if(hold){
            leftGrab.setPower(-0.05);
            rightGrab.setPower(0.05);
        }
        if(!hold){
            leftGrab.setPower(0);
            rightGrab.setPower(0);
        }
    }

    public void brake(boolean brake) {
        if(brake){
            leftGrab.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightGrab.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }
}

enum LiftMode {
    MANUAL,
    AUTOMATIC
}
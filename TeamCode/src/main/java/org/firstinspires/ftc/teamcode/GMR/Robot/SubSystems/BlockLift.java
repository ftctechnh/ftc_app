package org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by pston on 11/12/2017
 */

public class BlockLift {

    public DcMotor liftMotor;
    public Servo topLeftGrab;
    public Servo topRightGrab;
    public Servo bottomLeftGrab;
    public Servo bottomRightGrab;

    private int slidePosition = 1;
    private boolean buttonPressed = false;

    private int currentLiftPosition;

    private double topLeftPosition = 0.37; //Open: 0.37, Close: 0
    private double topRightPosition = 0.26; //Open: 0.26, Close: 0.65
    private double bottomLeftPosition = 0.55; //Open: 0.117, Close: 0.55
    private double bottomRightPosition = 0.2; //Open: 0.2, Close: 0.67

    private boolean topButtonPress = false;
    private boolean bottomButtonPress = false;

    public BlockLift(DcMotor liftMotor, Servo topLeftGrab, Servo topRightGrab, Servo bottomLeftGrab, Servo bottomRightGrab) {

        this.liftMotor = liftMotor;
        this.topLeftGrab = topLeftGrab;
        this.topRightGrab = topRightGrab;
        this.bottomLeftGrab = bottomLeftGrab;
        this.bottomRightGrab = bottomRightGrab;

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        topLeftGrab.setPosition(0.37);
        topRightGrab.setPosition(0.26);
        bottomLeftGrab.setPosition(0.117);
        bottomRightGrab.setPosition(0.2);

    }

    public void currentServoPositions(Telemetry telemetry) {
        telemetry.addData("Top Left Servo Position", topLeftGrab.getPosition());
        telemetry.addData("Top Right Servo Position", topRightGrab.getPosition());
        telemetry.addData("Bottom Left Servo Position", bottomLeftGrab.getPosition());
        telemetry.addData("Bottom Right Servo Position", bottomRightGrab.getPosition());
    }

    public void slideHeight(boolean dpadUp, boolean dpadDown, Telemetry telemetry) {

        if ((dpadUp || dpadDown) && ((dpadUp || dpadDown) != buttonPressed)) {
            if (dpadUp && !(slidePosition > 3)) {
                slidePosition++;
            } else if (!(slidePosition < 1)) {
                slidePosition--;
            }
        }

        buttonPressed = (dpadUp || dpadDown);

        if (slidePosition == 1) {
            liftMotor.setTargetPosition(0);
            telemetry.addData("Current Position", slidePosition);
        } else if (slidePosition == 2) {
            liftMotor.setTargetPosition(-1768);
            telemetry.addData("Current Position", slidePosition);
        } else if (slidePosition == 3) {
            liftMotor.setTargetPosition(-4410);
            telemetry.addData("Current Position", slidePosition);
        }
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

    public void clamp(boolean a, boolean x, boolean y, boolean b) {
        if (a) {
            bottomRightPosition = 0.2;
            bottomLeftPosition = 0.55;
        } else if (x) {
            bottomRightPosition = 0.67;
            bottomLeftPosition = 0.117;
        }

        if (y) {
            topLeftPosition = 0.37;
            topRightPosition = 0.26;
        } else if (b) {
            topLeftPosition = 0;
            topRightPosition = 0.65;
        }

        topButtonPress = b;
        bottomButtonPress = a;

        topLeftGrab.setPosition(topLeftPosition); //Open: 0.37, Close: 0
        topRightGrab.setPosition(topRightPosition); //Open: 0.26, Close: 0.65
        bottomLeftGrab.setPosition(bottomLeftPosition); //Open: 0.55, Close: 0.117
        bottomRightGrab.setPosition(bottomRightPosition); //Open: 0.2, Close: 0.67
    }
}

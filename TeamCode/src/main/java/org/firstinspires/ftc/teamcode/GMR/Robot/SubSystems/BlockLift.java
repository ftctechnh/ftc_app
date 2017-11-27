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
        bottomLeftGrab.setPosition(0.55);
        bottomRightGrab.setPosition(0.5);

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
        /*
        else if (slidePosition == 4) {
            telemetry.addData("Current Position", slidePosition);
        }
        */
    }
}
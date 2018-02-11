package org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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

    private boolean topButtonPress = false;
    private boolean bottomButtonPress = false;

    public BlockLift(DcMotor liftMotor, DcMotor leftGrab, DcMotor rightGrab) {

        this.liftMotor = liftMotor;
        this.leftGrab = leftGrab;
        this.rightGrab = rightGrab;

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftGrab.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightGrab.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void slideHeight(boolean a, boolean b, boolean y, Telemetry telemetry) {

        if ((a || b || y) && ((a || b || y) != buttonPressed)) {
            if (a) {
                slidePosition = 1;
            } else if (b) {
                slidePosition = 2;
            } else if (y) {
                slidePosition = 3;
            }
        }

        buttonPressed = (a || b || y);

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
}

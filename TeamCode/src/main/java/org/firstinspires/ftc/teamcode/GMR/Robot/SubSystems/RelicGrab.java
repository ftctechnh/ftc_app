package org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by pston on 12/14/2017
 */

public class  RelicGrab {

    private DcMotor relicLift;
    private Servo relicTilt;
    private Servo relicClamp;

    private double tiltPosition; // Stored: 0.539, Upper Bound: 0.531, Lower Bound: 0.59
    private double clampPosition; // Stored: 0, Upper Bound: 0.55, Lower Bound: 0

    private int relicLiftPosition;

    private boolean fast = false;
    private boolean isXPressed;
    private double speed = 0.8;

    public RelicGrab(DcMotor relicLift, Servo relicTilt, Servo relicClamp) {
        this.relicLift = relicLift;
        this.relicTilt = relicTilt;
        this.relicClamp = relicClamp;

        this.relicLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        clampPosition = 0.9;
        tiltPosition = 0.2;

        relicLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        relicLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void relicLift(boolean leftBumper, float leftTrigger, Telemetry telemetry, boolean x) {

        if (x && isXPressed) {
            fast = !fast;
            if (fast) {
                speed = 0.8;
            } else {
                speed = 0.2;
            }
        }

        isXPressed = x;

        if (leftBumper) {
            relicLiftPosition = relicLift.getCurrentPosition();
            relicLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            relicLift.setPower(-speed);
        } else if (leftTrigger > 0) {
            relicLiftPosition = relicLift.getCurrentPosition();
            relicLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            relicLift.setPower(speed);
        } else {
            if ((Math.abs(Math.abs(relicLiftPosition) - Math.abs(relicLift.getCurrentPosition()))) > 10) {
                relicLiftPosition = relicLift.getCurrentPosition();
                relicLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                relicLift.setTargetPosition(relicLiftPosition);
            } else {
                relicLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                relicLift.setTargetPosition(relicLiftPosition);
            }
        }
        telemetry.addData("Current Relic Slide Position", relicLiftPosition);
    }

    public void setTilt(boolean y, boolean a, boolean b) {
        if (a && tiltPosition <= 1) {
            tiltPosition += 0.01;
        } else if (y && tiltPosition >= 0) {
            tiltPosition = 0.2;
        } else if (b) {
            tiltPosition = 0.915;
        }
        relicTilt.setPosition(tiltPosition);
    }

    public void setClamp(boolean rightBumper, float rightTrigger) {
        if (rightBumper && clampPosition <= 1) {
            clampPosition = 0.4;
        } else if (rightTrigger > 0) {
            clampPosition = 1;
        }
        relicClamp.setPosition(clampPosition);
    }

    public void relicGrab(boolean leftBumper, float leftTrigger, boolean y, boolean a, boolean rightBumper, float rightTrigger, boolean b, Telemetry telemetry, boolean x) {
        relicLift(leftBumper, leftTrigger, telemetry, x);
        setTilt(y, a, b);
        setClamp(rightBumper, rightTrigger);
    }

    public void servoInfo(Telemetry telemetry) {
        telemetry.addData("Tilt Servo Position", tiltPosition);
        telemetry.addData("Clamp Servo Position", clampPosition);
    }

}
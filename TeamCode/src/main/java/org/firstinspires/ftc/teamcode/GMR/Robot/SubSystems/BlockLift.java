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

    private int goalposition;
    private double dynamicPower = 0;

    public BlockLift(DcMotor liftMotor, Servo topLeftGrab, Servo topRightGrab, Servo bottomLeftGrab, Servo bottomRightGrab) {

        this.liftMotor = liftMotor;
        this.topLeftGrab = topLeftGrab;
        this.topRightGrab = topRightGrab;
        this.bottomLeftGrab = bottomLeftGrab;
        this.bottomRightGrab = bottomRightGrab;

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        topLeftGrab.setPosition(0.37);
        topRightGrab.setPosition(0.26);
        bottomLeftGrab.setPosition(0.55);
        bottomRightGrab.setPosition(0.5);

    }

    public void holdMotor(int goalPosition) {

        this.goalposition = goalPosition;
        if (liftMotor.getCurrentPosition() < goalPosition) {
            dynamicPower += 0.01;
        } else {
            dynamicPower = 0;
        }
        liftMotor.setPower(dynamicPower);

    }

    public void currentServoPositions(Telemetry telemetry) {
        telemetry.addData("Top Left Servo Position", topLeftGrab.getPosition());
        telemetry.addData("Top Right Servo Position", topRightGrab.getPosition());
        telemetry.addData("Bottom Left Servo Position", bottomLeftGrab.getPosition());
        telemetry.addData("Bottom Right Servo Position", bottomRightGrab.getPosition());
    }

}

package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;

@Config
public class Arm {
    public static double MAX_POWER = 0.5;

    public static int POS_DIFFERENCE = 1520;

    private HoldingPIDMotor leftFlipper, rightFlipper;

    public Arm (DcMotorEx leftMotor, DcMotorEx rightMotor) {
        leftFlipper  = new HoldingPIDMotor(leftMotor, MAX_POWER);
        rightFlipper = new HoldingPIDMotor(rightMotor, MAX_POWER);
    }

    public int getCurrentPosition() {
        return (leftFlipper.getCurrentPosition() +
                rightFlipper.getCurrentPosition()) / 2;
    }

    public void setPower(double p) {
        leftFlipper.setPower(p);
        rightFlipper.setPower(p);
    }

    public void goToPosition(int position) {
        leftFlipper.setTargetPos(position);
        rightFlipper.setTargetPos(position);
    }

    public void collect() {
        leftFlipper.setTargetPos(leftFlipper.getCurrentPosition() + POS_DIFFERENCE);
        rightFlipper.setTargetPos(rightFlipper.getCurrentPosition() + POS_DIFFERENCE);
    }

    public void deposit() {
        leftFlipper.setTargetPos(leftFlipper.getCurrentPosition() - POS_DIFFERENCE);
        rightFlipper.setTargetPos(rightFlipper.getCurrentPosition() - POS_DIFFERENCE);
    }


}

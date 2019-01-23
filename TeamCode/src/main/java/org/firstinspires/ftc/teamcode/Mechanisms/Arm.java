package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;

@Config
public class Arm {
    public static double MAX_POWER = 0.5;

    public static int POS_DIFFERENCE = 1520;
    public static int COLLECT_THRESHOLD = 2500;

    private HoldingPIDMotor leftFlipper, rightFlipper;
    private DcMotorEx extender;

    public Arm (DcMotorEx leftMotor, DcMotorEx rightMotor, DcMotorEx extender) {
        leftFlipper  = new HoldingPIDMotor(leftMotor, MAX_POWER);
        rightFlipper = new HoldingPIDMotor(rightMotor, MAX_POWER);
        this.extender = extender;
    }

    public void setPower(double p) {
        leftFlipper.setPower(p);
        rightFlipper.setPower(p);
    }

    public int getCurrentPosition() {
        return (leftFlipper.getCurrentPosition() +
                rightFlipper.getCurrentPosition()) / 2;
    }

    public boolean isCollecting() {
        return getCurrentPosition() > COLLECT_THRESHOLD;
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

package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Config
public class Intake {
    public static double DEPOSIT_DIST = 0.13;
    public static double COLLECT_DIST = 0.38;
    public static double LOWER_ARM_DIST = 0.6;

    public static double MAX_INTAKE_SPEED = 0.8;

    private boolean enabled;

    public ServoImplEx leftIntakeFlipper;
    public ServoImplEx rightIntakeFlipper;
    public CRServoImplEx leftIntakeRoller;
    public CRServoImplEx rightIntakeRoller;

    public Intake(ServoImplEx leftIntakeFlipper, ServoImplEx rightIntakeFlipper,
                  CRServoImplEx leftIntakeRoller, CRServoImplEx rightIntakeRoller) {
        this.leftIntakeFlipper = leftIntakeFlipper;
        this.rightIntakeFlipper = rightIntakeFlipper;

        this.leftIntakeRoller = leftIntakeRoller;
        this.rightIntakeRoller = rightIntakeRoller;

        // In case we are mocking this
        try {
            leftIntakeFlipper.setPwmDisable();
            rightIntakeFlipper.setPwmDisable();

            rightIntakeRoller.setDirection(CRServoImplEx.Direction.REVERSE);
            leftIntakeFlipper.setDirection(ServoImplEx.Direction.FORWARD);
            rightIntakeFlipper.setDirection(ServoImplEx.Direction.REVERSE);
        } catch (NullPointerException e) {}
    }

    public void setIntakeSpeed(double s) {
        leftIntakeRoller.setPower(s * MAX_INTAKE_SPEED);
        rightIntakeRoller.setPower(s * MAX_INTAKE_SPEED);
    }

    public void deposit() {
        setPos(DEPOSIT_DIST);
    }

    public void collect() {
        setPos(COLLECT_DIST);
    }

    public void prepWinchLower() { setPos(LOWER_ARM_DIST); }

    public void goToMin() {
        setPos(1);
    }

    public void setPos(double pos) {
        leftIntakeFlipper.setPosition(pos);
        rightIntakeFlipper.setPosition(pos);
    }

    public void enableFlippers() {
        if (!enabled) {
            leftIntakeFlipper.setPwmEnable();
            rightIntakeFlipper.setPwmEnable();
            enabled = true;
        }
    }

    public void disableFlippers() {
        if (enabled) {
            leftIntakeFlipper.setPwmDisable();
            rightIntakeFlipper.setPwmDisable();
            enabled = false;
        }
    }
}

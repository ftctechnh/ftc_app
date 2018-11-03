package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Config
public class Intake {
    public static double FLIPPER_START = 0;
    public static double DEPOSIT_DIST = 0.35;
    public static double COLLECT_DIST = 0;

    public static double MAX_INTAKE_SPEED = 0.8;
    public static double MAX_PWN = 2800;
    public static double MIN_PWM = 500;

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
        leftIntakeFlipper.setPwmDisable();
        rightIntakeFlipper.setPwmDisable();

        rightIntakeRoller.setDirection(CRServoImplEx.Direction.REVERSE);
        leftIntakeFlipper.setDirection(ServoImplEx.Direction.FORWARD);
        rightIntakeFlipper.setDirection(ServoImplEx.Direction.REVERSE);
    }

    public void setIntakeSpeed(double s) {
        leftIntakeRoller.setPower(s * MAX_INTAKE_SPEED);
        rightIntakeRoller.setPower(s * MAX_INTAKE_SPEED);
    }

    public void deposit() {
        leftIntakeFlipper.setPosition(DEPOSIT_DIST);
        rightIntakeFlipper.setPosition(DEPOSIT_DIST);
    }

    public void collect() {
        leftIntakeFlipper.setPosition(COLLECT_DIST);
        rightIntakeFlipper.setPosition(COLLECT_DIST);
    }

    public void goToMin() {
        leftIntakeFlipper.setPosition(0);
        rightIntakeFlipper.setPosition(0);
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

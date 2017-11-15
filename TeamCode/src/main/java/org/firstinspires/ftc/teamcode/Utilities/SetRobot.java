package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by 72710 on 11/14/2017.
 */

public class SetRobot {
    // -------------------------- Objects ---------------------------
    Telemetry telemetry;
    // ------------------------ Constructor -------------------------
    public SetRobot(Telemetry tele) {
        telemetry = tele;
    }
    // ----------------------- Public Methods -----------------------
    // ---------------- DcMotors ----------------
    public void power(DcMotor motor, double power, String name) {
        telemetry.addData(name + " power", power);
        try {
            motor.setPower(power);
        } catch (Exception opModeException) {
            //telemetry.addData("Can't power", name);
        }
    }
    // ------------ Standard Servos -------------
    public void position(Servo servo, double position, String name) {
        telemetry.addData(name + " power", position);
        try {
            servo.setPosition(position);
        } catch (Exception opModeException) {
            //telemetry.addData("Can't position", name);
        }
    }
    // ------- Continuous Rotation Servos -------
    public void position(CRServo crServo, double power, String name) {
        telemetry.addData(name + " power", power);
        try {
            crServo.setPower(power);
        } catch (Exception opModeException) {
            //telemetry.addData("Can't power", name);
        }
    }
}

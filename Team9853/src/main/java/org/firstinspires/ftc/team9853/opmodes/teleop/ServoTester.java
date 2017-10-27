package org.firstinspires.ftc.team9853.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.utils.RobotLogger;

import java.util.Map;

/**
 * Created by carsonstorm on 10/25/2017.
 */

@TeleOp(name = "ServoTester", group = "TEST")
public class ServoTester extends OpMode {
    private double position = 0;
    private double power = 0;
    private long lastPressA;
    private long lastPressB;
    private long lastPressX;
    private long lastPressY;
    private long pressBuffer = 500;
    private RobotLogger logger;

    @Override
    public void init() {
        logger = new RobotLogger("ServoTester", telemetry);
    }

    @Override
    public void start() {
        setServoPositions(position);
        setCrServoPowers(power);
    }

    @Override
    public void loop() {
        if (isA(gamepad1) && !(Math.abs(position - 1) <= 1e-10)) {
            position += 0.1;
        }

        if (isB(gamepad1) && !(Math.abs(position) <= 1e-10)) {
            position -= 0.1;
        }

        if (isX(gamepad1) && !(Math.abs(power - 1) <= 1e-10)) {
            power += 0.1;
        }

        if (isY(gamepad1) && !(Math.abs(power + 1) <= 1e-10)) {
            power -= 0.1;
        }

        setServoPositions(position);
        setCrServoPowers(power);

        logger.debug("position", position);
        logger.debug("power", power);
    }

    private void setServoPositions(double pos) {
        for (Map.Entry<String, Servo> servoEntry : hardwareMap.servo.entrySet()) {
            servoEntry.getValue().setPosition(pos);
        }
    }

    private void setCrServoPowers(double pow) {
        for (Map.Entry<String, CRServo> servoEntry : hardwareMap.crservo.entrySet()) {
            servoEntry.getValue().setPower(pow);
        }
    }

    private boolean isA(Gamepad gp) {
        if (gp.a && System.currentTimeMillis() - pressBuffer >= lastPressA) {
            lastPressA = System.currentTimeMillis();
            return true;
        };

        return false;
    }

    private boolean isB(Gamepad gp) {
        if (gp.b && System.currentTimeMillis() - pressBuffer >= lastPressB) {
            lastPressB = System.currentTimeMillis();
            return true;
        };

        return false;
    }

    private boolean isX(Gamepad gp) {
        if (gp.x && System.currentTimeMillis() - pressBuffer >= lastPressX) {
            lastPressX = System.currentTimeMillis();
            return true;
        };

        return false;
    }

    private boolean isY(Gamepad gp) {
        if (gp.y && System.currentTimeMillis() - pressBuffer >= lastPressY) {
            lastPressY = System.currentTimeMillis();
            return true;
        };

        return false;
    }
}

package org.firstinspires.ftc.team9853.opmodes.teleop;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.Controller;
import org.chathamrobotics.common.opmode.TeleOpTemplate;
import org.chathamrobotics.common.robot.RobotLogger;

import java.util.Map;

/**
 * Tests servo values
 */
@SuppressWarnings("unused")
@TeleOp(name = "ServoTester", group = "TEST")
public class ServoTester extends TeleOpTemplate {
    private double position = 0;
    private double power = 0;
    private RobotLogger logger;

    @Override
    public void init() {
        super.init();
        logger = new RobotLogger("ServoTester", telemetry);
    }

    @Override
    public void start() {
        setServoPositions(position);
        setCrServoPowers(power);
    }

    @Override
    public void loop() {
        if (controller1.aState == Controller.ButtonState.TAPPED && !(Math.abs(position - 1) <= 1e-10)) {
            position += 0.1;
        }

        if (controller1.bState == Controller.ButtonState.TAPPED && !(Math.abs(position) <= 1e-10)) {
            position -= 0.1;
        }

        if (controller1.xState == Controller.ButtonState.TAPPED && !(Math.abs(power - 1) <= 1e-10)) {
            power += 0.1;
        }

        if (controller1.yState == Controller.ButtonState.TAPPED && !(Math.abs(power + 1) <= 1e-10)) {
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
}

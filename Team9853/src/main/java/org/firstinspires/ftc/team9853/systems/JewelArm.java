package org.firstinspires.ftc.team9853.systems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 10/5/2017
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JewelArm {

    public static final double ARM_UP_POSITION = 1;
    public static final double ARM_DOWN_POSITION = 0;

    private Servo armServo;
    private CRServo armShifter;
    private ColorSensor colorSensor;

    private boolean isArmUp;

    public JewelArm(Servo armServo, CRServo armShifter, ColorSensor colorSensor) {
        this.armServo = armServo;
        this.armShifter = armShifter;
        this.colorSensor = colorSensor;

        // TODO: come up with a better way to compare these values
        this.isArmUp = Math.abs(armServo.getPosition() - ARM_UP_POSITION) > 0.0000000000000000000000001;
    }

    public void toggleArmPosition() {
        if (isArmUp) {
            raiseArm();
        } else {
            lowerArm();
        }
    }

    public void raiseArm() {
        armServo.setPosition(ARM_UP_POSITION);
        this.isArmUp = true;
    }

    public void lowerArm() {
        armServo.setPosition(ARM_DOWN_POSITION);
        this.isArmUp = false;
    }
}

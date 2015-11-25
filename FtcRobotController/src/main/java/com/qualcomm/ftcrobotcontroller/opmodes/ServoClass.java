package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Robotics on 11/18/2015.
 */
public class ServoClass extends OpMode {
    public class RoboticArm {
        Servo armBase;
        Servo primaryArmOne;
        Servo primaryArmTwo;
        Servo secondaryArmOne;
        Servo secondaryArmTwo;
        Servo armHatch;

        public void callDuringInit() {
            armBase = hardwareMap.servo.get("armBase");
            primaryArmOne = hardwareMap.servo.get("primaryArmOne");
            primaryArmTwo = hardwareMap.servo.get("primaryArmTwo");
            secondaryArmOne = hardwareMap.servo.get("secondaryArmOne");
            secondaryArmTwo = hardwareMap.servo.get("secondaryArmTwo");
            armHatch = hardwareMap.servo.get("armHatch");
        }

        public void zeroServos() {
            primaryArmOne.setPosition(0.0);
            primaryArmTwo.setPosition(0.0);
            secondaryArmOne.setPosition(0.0);
            secondaryArmTwo.setPosition(0.0);
            armHatch.setPosition(0.0);
        }

        public void update(double joystickLeftX, double joystickRightX, boolean leftTrigger, boolean rightTrigger, boolean buttonA) {

            // base control (triggers)
            if(leftTrigger) armBase.setPosition(1.0);
            else if(rightTrigger) armBase.setPosition(0.0);

            // primary joint control (left joystick)
            primaryArmOne.setPosition((joystickLeftX + 1.0) / 2.0);
            primaryArmTwo.setPosition(1.0 - (joystickLeftX + 1.0) / 2.0);

            // secondary joint control (right joystick)
            secondaryArmOne.setPosition((joystickRightX + 1.0) / 2.0);
            secondaryArmTwo.setPosition(1.0 - (joystickRightX + 1.0) / 2.0);

            // hatch control (A button)
            if(buttonA) armHatch.setPosition(1.0);
            else armHatch.setPosition(0.0);
        }

    }

    Servo armBase;
    Servo primaryArmOne;
    Servo primaryArmTwo;
    Servo secondaryArmOne;
    Servo secondaryArmTwo;

    @Override
    public void init() {
        armBase = hardwareMap.servo.get("armBase");
        primaryArmOne = hardwareMap.servo.get("primaryArmOne");
        primaryArmTwo = hardwareMap.servo.get("primaryArmTwo");
        secondaryArmOne = hardwareMap.servo.get("secondaryArmOne");
        secondaryArmTwo = hardwareMap.servo.get("secondaryArmTwo");
    }

    @Override
    public void start() {
    }
    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {
        update(gamepad1.right_stick_x, gamepad1.left_stick_x, gamepad1.y, gamepad1.a);
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

    public void update(double joystickLeftX, double joystickRightX, boolean buttonY, boolean buttonA) {

        // base control
        armBase.setPosition((joystickRightX + 1.0) / 2);

        // primary joint control
        primaryArmOne.setPosition((joystickRightX + 1.0) / 2);
        primaryArmTwo.setPosition(1.0 - (joystickRightX + 1.0) / 2);

        // secondary joint control
        if(buttonY) {
            secondaryArmOne.setPosition(1.0);
            secondaryArmTwo.setPosition(0.0);
        }
        else if(buttonA) {
            secondaryArmOne.setPosition(0.0);
            secondaryArmTwo.setPosition(1.0);
        }
    }

}

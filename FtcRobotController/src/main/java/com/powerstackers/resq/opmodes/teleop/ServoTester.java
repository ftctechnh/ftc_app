package com.powerstackers.resq.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * @author Jonathan
 */
public class ServoTester extends OpMode {

    Servo servoHopperLeft;
    Servo servoHopperRight;
    Servo servoClimbers;

    @Override
    public void init() {
        servoClimbers = hardwareMap.servo.get("servoClimbers");
        servoHopperLeft = hardwareMap.servo.get("servoHopperLeft");
        servoHopperRight = hardwareMap.servo.get("servoHopperRight");
    }

    double hopperLeftPosition = 0.5;
    double hopperRightPosition = 0.5;
    double climbersPosition = 0.5;

    @Override
    public void loop() {
        climbersPosition = servoClimbers.getPosition();
        hopperLeftPosition = servoHopperLeft.getPosition();
        hopperRightPosition = servoHopperRight.getPosition();

        if (gamepad1.y) {
            climbersPosition += 0.01;
        } else if (gamepad1.a) {
            climbersPosition -= 0.01;
        }

        if (gamepad1.x) {
            hopperRightPosition += 0.01;
        } else if (gamepad1.b) {
            hopperRightPosition -= 0.01;
        }

        if (gamepad1.right_bumper) {
            hopperLeftPosition += 0.01;
        } else if (gamepad1.left_bumper) {
            hopperLeftPosition -= 0.01;
        }

        servoHopperRight.setPosition(hopperRightPosition);
        servoHopperLeft.setPosition(hopperLeftPosition);
        servoClimbers.setPosition(climbersPosition);

        telemetry.addData("hopperLeft", hopperLeftPosition);
        telemetry.addData("hopperRight", hopperRightPosition);
        telemetry.addData("climbers", climbersPosition);
    }
}

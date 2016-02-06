package com.powerstackers.resq.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.swerverobotics.library.interfaces.TeleOp;

/**
 * @author Jonathan
 */
@TeleOp(name = "Servo Tester Op", group = "Powerstackers")
public class ServoTester extends OpMode {

    Servo servoHopperLeft;
    Servo servoHopperRight;
    Servo servoClimbers;

    @Override
    public void init() {
        servoClimbers = hardwareMap.servo.get("servoClimbers");
        servoHopperLeft = hardwareMap.servo.get("servoHopperLeft");
        servoHopperRight = hardwareMap.servo.get("servoHopperRight");

        servoClimbers.setPosition(1.0);
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

        hopperLeftPosition = Range.clip(hopperLeftPosition, 0.0, 1.0);
        hopperRightPosition = Range.clip(hopperRightPosition, 0.0, 1.0);
        climbersPosition = Range.clip(climbersPosition, 0.0, 1.0);

        servoHopperRight.setPosition(hopperRightPosition);
        servoHopperLeft.setPosition(hopperLeftPosition);
        servoClimbers.setPosition(climbersPosition);

        telemetry.addData("hopperLeft", hopperLeftPosition);
        telemetry.addData("hopperRight", hopperRightPosition);
        telemetry.addData("climbers", climbersPosition);
    }
}

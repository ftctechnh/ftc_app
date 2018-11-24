package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * ☺ Hi! Esto es el codigo para el 24 Febrero! ☺
 */
@TeleOp(name = "Test Rotation Servo", group = "Tests")
//@Disabled
public class Test_Rotation_Servo extends LinearOpMode {

    /* Declare all objects */
    CRServo testServo;

    /* Define 2 Position Values */
    static double directionOne = -.5;
    static double directionTwo = .5;

    @Override
    public void runOpMode() {
        testServo = hardwareMap.crservo.get("gemservo");

        /* Wait for the start button */
        telemetry.addLine("!☺ Ready to Run ☺!");
        telemetry.update();
        waitForStart();

    /* While OpMode is Active Loop */

        while (opModeIsActive()) {

            /* Talk to the drivers & coach */
            telemetry.addLine("Hi~♪");
            telemetry.addLine("Push A to move one way");
            telemetry.addLine("Push B to move the other");
            /* Put the servo arm up */
            if (gamepad1.a) {
                testServo.setPower(directionOne);
            }
            if (gamepad1.b) {
                testServo.setPower(directionTwo);
            }
        }
    }
}
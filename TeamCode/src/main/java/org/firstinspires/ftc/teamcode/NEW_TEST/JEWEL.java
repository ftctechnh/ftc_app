package org.firstinspires.ftc.teamcode.NEW_TEST;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by shiva on 24-05-2018.
 */

@TeleOp(name = "Jewel Test", group = "prototype")

public class JEWEL extends LinearOpMode {

    //GRAB
    private Servo jewelArm;
    private Servo jewelKnock;

    private static ColorSensor jColor;

    private double pos;
    private double pos2;

    private static double jaUP = 0.635;
    private static double jaDOWN = 0.08;

    private static double jkCENTER = 0.5;
    private static double jkRIGHT = 0.27;
    private static double jkLEFT = 0.69;
    private static double jkINITIAL = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        jewelArm = hardwareMap.get(Servo.class, "JA");
        jewelKnock = hardwareMap.get(Servo.class, "JK");

        jColor = hardwareMap.colorSensor.get("colF");
        jColor.enableLed(true);

        jewelArm.setPosition(0.5);
        jewelKnock.setPosition(0.5);

        pos = 0;
        pos2 = 0;

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a && (gamepad1.left_trigger < 0.5)) {
                pos += 0.005;
            }

            if (gamepad1.b && (gamepad1.left_trigger < 0.5)) {
                pos -= 0.005;
            }

            pos = Range.clip(pos, 0, 1);
            jewelArm.setPosition(pos);

            if (gamepad1.x) {
                pos2 += 0.01;
            }

            if (gamepad1.y) {
                pos2 -= 0.01;
            }

            if (gamepad1.dpad_right){
                jewelArm.setPosition(jkRIGHT);
            }

            if (gamepad1.dpad_left){
                jewelArm.setPosition(jkLEFT);
            }

            if (gamepad1.right_stick_button){
                telemetry.addData("Blue side code running", jColor.red());
                if (jColor.red() < 3){
                    jewelKnock.setPosition(jkLEFT);
                }
                else {
                    jewelKnock.setPosition(jkRIGHT);
                }
            }

            if (gamepad1.left_stick_button){
                telemetry.addData("Red side code running", jColor.red());
                if (jColor.blue() < 3){
                    jewelKnock.setPosition(jkLEFT);
                }
                else {
                    jewelKnock.setPosition(jkRIGHT);
                }
            }

            pos2 = Range.clip(pos2, 0, 1);
            jewelKnock.setPosition(pos2);

            telemetry.addData("JA: ", jewelArm.getPosition());
            telemetry.addData("JK: ", jewelKnock.getPosition());
            telemetry.addData("Blue value: ", jColor.blue());
            telemetry.addData("Red value: ", jColor.red());

            telemetry.update();

        }

    }
}

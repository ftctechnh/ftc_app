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

            pos2 = Range.clip(pos2, 0, 1);
            jewelKnock.setPosition(pos2);

            telemetry.addData("JA: ", jewelArm.getPosition());
            telemetry.addData("JK: ", jewelKnock.getPosition());
            telemetry.addData("Color sensor value: ", jColor.blue());

            telemetry.update();

        }

    }
}

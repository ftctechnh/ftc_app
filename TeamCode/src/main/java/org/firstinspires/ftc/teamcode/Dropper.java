

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Dropper", group="Linear Opmode")
//@Disabled
public class Dropper extends LinearOpMode {

    double servoPosition1 = 0;
    double servoPosition2 = 0.99;
    double servoDelta = 0.01;
    Servo s1, s2;

    @Override
    public void runOpMode() {

        s1 = hardwareMap.get(Servo.class, "Servo1");
        s2 = hardwareMap.get(Servo.class, "Servo2");


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.y) {
                servoPosition1 -= servoDelta;
            }
            if (gamepad1.a) {
                servoPosition1 += servoDelta;
            }
            if (gamepad1.y) {
                servoPosition2 += servoDelta;
            }
            if (gamepad1.a) {
                servoPosition2 -= servoDelta;
            }

            // clip the position values so that they never exceed 0..1
            servoPosition1 = Range.clip(servoPosition1, 0, 1);
            servoPosition2 = Range.clip(servoPosition2, 0, 1);

            // write position values to the servo
            s1.setPosition(servoPosition1);
            s2.setPosition(servoPosition2);



        }
    }
}

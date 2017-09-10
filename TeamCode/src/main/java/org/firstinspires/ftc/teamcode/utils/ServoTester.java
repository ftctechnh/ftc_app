package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by ftc6347 on 9/10/17.
 */
@Autonomous(name = "Servo Tester", group = "utils")
public class ServoTester extends LinearOpMode {

    private CRServo servoRight;
    private CRServo servoLeft;

    @Override
    public void runOpMode() throws InterruptedException {
        servoRight = (CRServo)hardwareMap.get("crl");
        servoLeft = (CRServo)hardwareMap.get("crr");

        waitForStart();

        while(opModeIsActive()) {
            if(Math.abs(gamepad1.right_stick_x) > 0.5) {
                servoRight.setPower(gamepad1.right_stick_x);
                servoLeft.setPower(gamepad1.right_stick_x);
            } else {
                servoRight.setPower(-gamepad1.right_stick_y);
                servoLeft.setPower(gamepad1.right_stick_y);
            }
        }
    }
}

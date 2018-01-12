
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp

public class FindEncoder extends LinearOpMode {

    private DcMotor m;
    private Servo s1;

    double currentAngle = 0.0;
    double interval = 0.05;


    @Override
    public void runOpMode() {

        m = hardwareMap.get(DcMotor.class,"Motor1");
        s1 = hardwareMap.get(Servo.class,"Servo1");

        m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        s1.setPosition(0.0);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            double power = Range.clip(gamepad1.left_stick_y,-0.5,0.5);
            m.setPower(power);
            int encoder = m.getCurrentPosition();

            String data = "Encoder:" + encoder;
            telemetry.addData("Status:",data);
            telemetry.update();


            if (gamepad1.a) {
                while (currentAngle < 0.5){
                    s1.setPosition(currentAngle);
                    currentAngle += interval;
                }
            }
            else if (gamepad1.x){
                while (currentAngle > 0.05){
                    s1.setPosition(currentAngle);
                    currentAngle -= interval;
                };
            }

        }
    }
}

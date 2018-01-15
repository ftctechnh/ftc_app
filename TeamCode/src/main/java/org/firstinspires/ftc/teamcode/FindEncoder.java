
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
    private Servo s1,s2;

    double angle1 = 0;
    double angle2 = 1;
    double interval = 0.01;
    boolean pressed = false;


    @Override
    public void runOpMode() {

        m = hardwareMap.get(DcMotor.class,"Motor1");
        s1 = hardwareMap.get(Servo.class,"Servo1");
        s2 = hardwareMap.get(Servo.class,"Servo2");

        m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();



        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            double power = Range.clip(gamepad1.left_stick_y,-0.5,0.5);
            m.setPower(power);
            int encoder = m.getCurrentPosition();

            String data = "Encoder:" + encoder;
            telemetry.addData("Status:",data);
            telemetry.update();


            if (gamepad1.a && !pressed) {
                while ((angle1 < 0.5)){
                    angle1 = (angle1 < 0.5)? angle1 + interval:angle1;
                    angle2 = (angle2 > 0.5)? angle2 - interval:angle2;
                    s1.setPosition(angle1);
                    s2.setPosition(angle2);

                }
                pressed = true;
            }
            else if (gamepad1.a && pressed){
                while ((angle1 > 0)){
                    angle1 = (angle1 > 0)? angle1 - interval:angle1;
                    angle2 = (angle2 < 1)? angle2 + interval:angle2;
                    s1.setPosition(angle1);
                    s2.setPosition(angle2);

                };
                pressed = false;
            }



        }
    }
}
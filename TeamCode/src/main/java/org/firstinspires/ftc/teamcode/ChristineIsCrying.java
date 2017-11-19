package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;
import java.util.List;
//Baby Balancing Robot Iteration 1(BBBot I1) created by Eric on 8/29/2017.

@TeleOp (name="ChristineIsCrying",group="Christine" )
//TODO console Christine
public class ChristineIsCrying extends LinearOpMode {
    //aka are my encoders playing nice
    Servo fin;
    double ready = 1;
    public void runOpMode() throws InterruptedException {
        fin = hardwareMap.servo.get("fin");
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_up && ready == 1) {
                fin.setPosition(fin.getPosition() + .1);
                ready = 0;
            } else if (gamepad1.dpad_down && ready == 1) {
                fin.setPosition(fin.getPosition() - .1);
                ready = 0;
            }else if(gamepad1.b && ready == 1){
                if (fin.getDirection()== Servo.Direction.FORWARD){
                    fin.setDirection(Servo.Direction.REVERSE);
                }else {
                    fin.setDirection(Servo.Direction.FORWARD);
                }
            }else if (!gamepad1.dpad_down && !gamepad1.dpad_up && !gamepad1.b && ready == 0){
                ready = 1;
            }
            telemetry.addData("servo", fin.getPosition());
            telemetry.addData("ready", ready);
            telemetry.update();
        }
    }
}
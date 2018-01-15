package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;



@TeleOp
//@Disabled
public class Lift_System extends LinearOpMode {

    private Servo s1,s2;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        s1 = hardwareMap.get(Servo.class, "Servo1");
        s2 = hardwareMap.get(Servo.class, "Servo2");
        
        

        boolean pressed = false;
        double angle1 = 0;
        double angle2 = 1;
        double interval = 0.01;
        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


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
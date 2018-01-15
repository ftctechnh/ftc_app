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
        double currentAngle = 0;
        double interval = 0.05;
        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if ((gamepad1.x) && (!pressed)){
                while (currentAngle < 0.5){
                    s1.setPosition(currentAngle);
                    s2.setPosition(currentAngle);
                    currentAngle += interval;

                }
                pressed = true;
            }
            else if((gamepad1.x) && (pressed)){
                while (currentAngle > 0.05) {
                    s1.setPosition(currentAngle);
                    s2.setPosition(currentAngle);
                    currentAngle -= interval;
                }
                pressed = false;
            }



        }
    }
}
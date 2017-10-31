package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/**
 * Created by BeehiveRobotics-3648 on 9/19/2017.
 */
@TeleOp(name = "ServoTest", group = "linear OpMode")
@Disabled
public class ServoTemplate extends OpMode{
    Servo Servo1;

    @Override

    public void init(){
        Servo1 = hardwareMap.servo.get("s1" );

    }

    @Override
    public void loop(){
        if (gamepad1.a){
            Servo1.setPosition(1.0);
            Servo1.setPosition(1.0);
        }
        if (gamepad1.b){
            Servo1.setPosition(0);
            Servo1.setPosition(0);
        }
    }
}

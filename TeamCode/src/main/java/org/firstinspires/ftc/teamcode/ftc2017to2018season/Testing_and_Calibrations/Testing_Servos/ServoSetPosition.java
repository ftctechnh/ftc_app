package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Testing_Servos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Inspiration Team on 4/8/2018.
 */
@TeleOp(name = "ServoSetPosition")
public class ServoSetPosition extends OpMode {

    Servo testServo;

    public void init(){
        testServo = hardwareMap.servo.get("testServo");
        testServo.setPosition(1);
    }

    public void loop(){
        //testServo.setPosition(0);
    }

}

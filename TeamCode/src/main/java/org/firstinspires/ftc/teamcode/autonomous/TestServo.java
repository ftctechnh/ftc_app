package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by BAbel on 11/2/2017.
 */

@Disabled
@Autonomous(name = "Test Servo", group = "Test")
public class TestServo extends LinearOpMode {

    public Servo servo;

    @Override
    public void runOpMode(){

        servo = hardwareMap.servo.get("servo");

        servo.setPosition(1);

        sleep(5000);

        servo.setPosition(0.5);

        sleep(5000);

        servo.setPosition(0);

    }
}

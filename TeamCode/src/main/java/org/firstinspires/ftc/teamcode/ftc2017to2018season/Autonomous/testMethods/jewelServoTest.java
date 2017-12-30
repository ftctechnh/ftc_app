package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.testMethods;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General;

/**
 * Created by Inspiration Team on 12/29/2017.
 */
@Autonomous(name = "VuforiaTest")
public class jewelServoTest extends Autonomous_General {

    public void runOpMode(){
        jewelServo.setPosition(1);

        waitForStart();
       sleep(1000);
       jewelServo.setPosition(0);
    }
}

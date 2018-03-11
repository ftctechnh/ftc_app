package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie Miner on 2/9/2018.
 */
@Autonomous(name="Preciousss: headingTest", group="Preciousss")

public class headingTest extends superAuto {

    public void runOpMode() {
        setUp();
        servoTapper.setPosition(.2);
        Wait(1);
        servoFlicker.setPosition(.3);
        Wait(2);
        servoFlicker.setPosition(0);
        Wait(2);
        servoFlicker.setPosition(.6);
        Wait(2);
        servoTapper.setPosition(.7);


    }
}
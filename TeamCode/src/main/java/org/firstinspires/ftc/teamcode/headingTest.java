package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie Miner on 2/9/2018.
 */
@Autonomous(name="Preciousss: headingTest", group="Preciousss")

public class headingTest extends superAuto {

    public void runOpMode() {
        setUp();
        //Tapper
        servoTapper.setPosition(0);
        Wait(2);
        servoTapper.setPosition(.1);
        Wait(2);
        servoTapper.setPosition(.2);
        Wait(2);
        servoTapper.setPosition(.3);
        Wait(2);
        servoTapper.setPosition(.4);
        Wait(2);
        servoTapper.setPosition(.5);
        Wait(2);
        servoTapper.setPosition(.6);
        Wait(2);

        Wait(4);
        //Flicker
        servoFlicker.setPosition(0);
        Wait(2);
        servoFlicker.setPosition(.25);
        Wait(2);
        servoFlicker.setPosition(.5);
        Wait(2);
        servoFlicker.setPosition(.75);
        Wait(2);
        servoFlicker.setPosition(1);
    }
}
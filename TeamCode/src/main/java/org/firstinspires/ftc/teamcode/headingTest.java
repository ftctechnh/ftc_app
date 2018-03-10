package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie Miner on 2/9/2018.
 */
@Autonomous(name="Preciousss: translateTest", group="Preciousss")

public class headingTest extends superAuto {

    public void runOpMode() {
        configureGyro();

        mapHardware();

        composeTelemetry();

        waitForStart();

        servoTapper.setPosition(0);
        Wait(2);
        servoTapper.setPosition(.25);
        Wait(2);
        servoTapper.setPosition(.75);
        Wait(2);
        servoTapper.setPosition(1);
        Wait(2);
        servoFlicker.setPosition(0);
        Wait(2);
        servoFlicker.setPosition(.5);
        Wait(2);
        servoFlicker.setPosition(1);



    }
}
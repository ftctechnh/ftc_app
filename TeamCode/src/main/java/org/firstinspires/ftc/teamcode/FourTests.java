package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Recharged Orange on 10/9/2018.
 */

@Autonomous(name = "Four Tests")

public class FourTests extends superClass {

    //red line
    public void runOpMode() {

        initialization(true);

        waitForStart(); //driver hits play



        driveForwardEncoders(500, .89);
        driveright(1900, 1);
        rotateLeft(45, .1);
        driveForwardEncoders(2500, 1);
        sleep(1000);
        //drop of team marker
        driveBackwardEncoders(6000, 1);

    }

    //blue line
   /* public void runOpMode() {

        initialization(true);

        waitForStart();


        driveForwardEncoders(3500, 1);
        rotateLeft(45, .5);
        driveForwardEncoders(500, .6);
        //drop of team marker
        rotateLeft(90, .5);
        driveForwardEncoders(4000, 1);
    }

    //green line
    public void runOpMode() {

        initialization(true);


        driveForwardEncoders(500, .5);
        //use the camera to knock of the cube
        driveForwardEncoders(1500, 1);
        //activate intake
        rotateLeft(90, .5);
        driveForwardEncoders(1700, .7);
        rotateLeft(45, 1);
        driveForwardEncoders(5000, 1);
        //drop of the team marker
        rotateLeft(90, .5);
        driveForwardEncoders(5500, 1);


    }

    //black line
    public void runOpMode() {

        initialization(true);


        driveForwardEncoders(400, .56);
        //knock off cube
        rotateRight(45, 1);
        driveForwardEncoders(1100, 1);
        rotateLeft(45, .8);
        driveForwardEncoders(900, 9);
        rotateLeft(90, 1);
        //drop of team marker
        driveForwardEncoders(550, .9);


    }*/
}

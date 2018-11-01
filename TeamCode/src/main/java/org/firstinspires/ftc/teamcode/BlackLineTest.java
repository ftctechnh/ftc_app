package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Recharged Orange on 10/18/2018.
 */

@Autonomous (name = "Black Line Test")

public class BlackLineTest extends superClass {

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


    }

    }

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Recharged Orange on 10/18/2018.
 */

@Autonomous (name = "Green Line Test")

public class GreenLineTest extends superClass {

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
}

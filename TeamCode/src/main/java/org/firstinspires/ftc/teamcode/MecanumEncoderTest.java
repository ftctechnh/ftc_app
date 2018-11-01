package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Recharged Orange on 10/7/2018.
 */

@Autonomous(name ="Mecanum Encoder Test")

public class MecanumEncoderTest extends superClass {


    public void runOpMode() {

        initialization(true);

        waitForStart(); //driver hits play


    driveForwardEncoders(500, .89);
    driveright(3000, 1);
    rotateLeft(45, .4);
    driveForwardEncoders(3000, 1);
    //drop of team marker
        driveBackwardEncoders(6000, 1);

    }
    }

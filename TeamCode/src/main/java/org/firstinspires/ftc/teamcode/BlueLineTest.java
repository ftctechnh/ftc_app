package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Recharged Orange on 10/18/2018.
 */

@Autonomous (name = "Blue Line Test")

public class BlueLineTest extends superClass {


//blue line

    ElapsedTime timer = new ElapsedTime();

    public void runOpMode() {

        initialization(true);

        waitForStart();


        driveForwardEncoders(3150, 1);
        rotateLeft(45, .5);
        driveForwardEncoders(500, .6);
        timer.reset();
        while (timer.milliseconds()< 3000 && opModeIsActive()){
            idle();
        }
        //drop of team marker
        rotateLeft(90, .5);
        driveForwardEncoders(4000, 1);
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by 111 on 9/29/2016.
 */
@Autonomous(name = "Concept: NullOp", group = "Concept")
public class AutonomousTestOp extends LinearOpMode {


    DriveTrainInterface drive;


    public void tunOpMode()
    {
        drive.driveStraight(1, 1);

    }



}


package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "ConceptAutonomousDepot" )
public class ConceptAutonomousDepot extends LinearOpMode
{

    ParadeBot walle;
    public void runOpMode()
    {
        walle = new ParadeBot(hardwareMap);
        // have servos move robot down to the ground
        walle.driveStraight_In(36);
        while(walle.getDistFromFrontLeft_In() > 8 && walle.getDistFromFrontRight_In() > 8)
        {
            walle.driveMotors(.7f, .7f);
        }
        walle.stopDriveMotors();
        walle.driveStraight_In(5f);
        //drop marker into depot
        walle.pivot_IMU(-35f);
        //insert wall hug program
        // stop robot when distance sensor says the robot is X inches from wall



    }
}




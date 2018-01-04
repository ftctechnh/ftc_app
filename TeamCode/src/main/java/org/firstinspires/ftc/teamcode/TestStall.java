package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi on 1/3/18.
 */
@Autonomous(name = "Test Stall",group = "Auto")
public class TestStall extends LinearOpMode
{
    NewRobotFinal newRobot;
    public void runOpMode()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.initVuforia(hardwareMap);
        {
            stopDriveMotors();
            float encTarget = neverrestEncCountsPerRev / wheelCircIn * inches;
            //You get the number of encoder counts per unit and multiply it by how far you want to go

            float absPow = (float)Math.abs(pow);
            resetDriveEncoders();
            //Notes: We are using Andymark Neverrest 40
            // 1120 counts per rev

            if(pow < 0)
            {
                inches *= -1;
            }
            if(inches < 0)
            {
                driveMotorsAuto(-absPow, -absPow);

                while (driveLeftOne.getCurrentPosition() < -encTarget && driveRightOne.getCurrentPosition() > encTarget)
                {
                    // if (Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES) <  *.75 )
                }
            }
            else
            {
                driveMotorsAuto(absPow, absPow);

                while(driveLeftOne.getCurrentPosition() > -encTarget && driveRightOne.getCurrentPosition() < encTarget){}
            }

            stopDriveMotors();
        }
    }
}

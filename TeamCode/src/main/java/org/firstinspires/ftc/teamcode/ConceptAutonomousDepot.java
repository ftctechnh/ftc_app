package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "ConceptAutonomousDepot")
public class ConceptAutonomousDepot extends LinearOpMode
{

    ParadeBot walle;

    public void runOpMode()
    {

        walle = new ParadeBot(hardwareMap, this);
        waitForStart();

        // have servos move robot down to the ground

       /* walle.driveStraight_In(36, .4f);
        walle.stopDriveMotors();
        sleep(500);

        while (walle.getDistFromFront_In() > 16 && walle.getDistFromRight_In() > 16)
        {
            telemetry.addData("leftdist", walle.getDistFromFront_In());
            telemetry.addData("righdist", walle.getDistFromRight_In());
            telemetry.update();

            walle.driveMotorsAuto(.4f, .4f);
        }
        walle.stopDriveMotors();

        //drop marker into depot
        walle.pivot(120); */

        float thetaDeg;
        double initialD, finalD;
        boolean isInCenter = false;
        float distToTravel = 12;

        initialD = walle.getDistFromRight_In();
        walle.driveStraight_In(distToTravel);
        finalD = walle.getDistFromRight_In();
        thetaDeg = (float) ((Math.asin((finalD - initialD)/distToTravel)) * 180/Math.PI);

        walle.pivot(-thetaDeg);
        double frontDist, rightDist;
        while (walle.getDistFromFront_In() > 18)
        {
            frontDist = walle.getDistFromFront_In();
            if(frontDist < 12)
            {
                walle.stopAllMotors();
                break;
            }
            else
            {
                walle.driveStraight_In(12);
            }

            if (frontDist < 18)
            {
                rightDist = walle.getDistFromRight_In();
                if (rightDist < 4)
                {
                    walle.pivot(15);
                    walle.driveStraight_In(6);
                    walle.pivot(-15);
                }
                else if (rightDist > 7)
                {
                    walle.pivot(-15);
                    walle.driveStraight_In(6);
                    walle.pivot(15);
                }
            }

        }

        telemetry.addData("Stopped", null);
        telemetry.update();
        while (!isStopRequested())
        {
            walle.stopAllMotors();
        }
    }
}




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
        walle.driveStraight_In(36, .4f);
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
        walle.pivot_IMU(120f);

        double thetaDeg;
        double initialD, finalD;
        boolean isInCenter = false;
        float distToTravel = 12;

        initialD = walle.getDistFromRight_In();
        walle.driveStraight_In(distToTravel);
        finalD = walle.getDistFromRight_In();
        thetaDeg = (Math.asin((finalD - initialD)/distToTravel)) * 180/Math.PI;

        while (walle.getDistFromFront_In() > 18)
        {
            sleep(200);
            initialD = walle.getDistFromRight_In();
            telemetry.addData("Theta = ", thetaDeg);
            if( initialD > 8)
            {
                if (thetaDeg > 15)
                {
                    walle.pivot_IMU(-15);
                }
                else if (thetaDeg < 15 && thetaDeg > 0)
                {
                    walle.pivot_IMU(-7);
                }
                isInCenter = false;
            }
            else if ( initialD < 4)
            {
                if (thetaDeg < -15)
                {
                    walle.pivot_IMU(15);
                }
                else if (thetaDeg > -15 && thetaDeg < 0)
                {
                    walle.pivot_IMU(7);
                }
                isInCenter = false;
            }
            else
            {
                if(!isInCenter)
                {
                    walle.pivot_IMU((float)(-thetaDeg));
                    isInCenter = true;
                }
            }
            sleep(300);
            walle.driveStraight_In(distToTravel);
            finalD = walle.getDistFromRight_In();
            thetaDeg =  (Math.asin((finalD - initialD)/distToTravel)) * 180/Math.PI;
            telemetry.addData("Inital dist ", initialD);
            telemetry.addData("Final dist ", finalD);
            telemetry.update();
        }

        telemetry.addData("Stopped", null);
        telemetry.update();
        while (!isStopRequested())
        {
        }
    }
}




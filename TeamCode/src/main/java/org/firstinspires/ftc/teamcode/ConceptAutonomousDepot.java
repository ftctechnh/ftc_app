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

        //walle.pivot(-thetaDeg);
        while (walle.getDistFromFront_In() > 18)
        {
            while (!gamepad1.a){}
            sleep(336);
            initialD = walle.getDistFromRight_In();

            telemetry.addData("Distance = ", initialD);
            if (initialD < 4)
            {
                telemetry.addData("I'm in the <4 Case!", null);
                walle.pivot(14);
                while (walle.getDistFromRight_In() < 5) //doing this drive forward until the robot is not too close so the statement is false
                {
                    walle.driveMotorsAuto(.16f,.16f);
                }
                walle.stopAllMotors();
                walle.pivot(-14);
            }
            else if (initialD > 7)
            {
                telemetry.addData("I'm in the >7 Case!", null);
                walle.pivot(-14);
                //walle.driveStraight_In(4);
                while (walle.getDistFromRight_In() > 7) //doing this drive forward until the robot is not too far so the statement is false
                {
                    walle.driveMotorsAuto(.16f,.16f);
                }
                walle.stopAllMotors();
                walle.pivot(14);
            }
            else
            {
                telemetry.addData("I'm in the default Case!", null);
                walle.driveStraight_In(distToTravel);
                walle.getDistFromFront_In();
                telemetry.addData("", walle.getDistFromFront_In());
            }
            telemetry.update();
        }

        telemetry.addData("Stopped", null);
        telemetry.update();
        while (!isStopRequested())
        {
            walle.stopAllMotors();
        }
    }
}




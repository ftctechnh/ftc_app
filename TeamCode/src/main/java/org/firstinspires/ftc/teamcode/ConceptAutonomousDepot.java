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

        walle.pivot_IMU(-thetaDeg);
        while (walle.getDistFromFront_In() > 18)
        {
            while (!gamepad1.a){} //edit this to be while the forward dist is less than 12
            sleep(336);
            initialD = walle.getDistFromRight_In();

            telemetry.addData("Distance = ", initialD);
            if (initialD < 4)
            {
                telemetry.addData("I'm in the <4 Case!", null);
                walle.pivot_IMU(14);
                while (walle.getDistFromRight_In() > 5) //flipped the signs since the anything less than 4 is also less than 5
                {
                    walle.driveMotorsAuto(.16f,.16f);
                }
                walle.stopAllMotors();
                walle.pivot_IMU(-14);
                //check for dist forward, if less than 12 then stop if greater than 12 keep going
            }
            else if (initialD > 7)
            {
                telemetry.addData("I'm in the >7 Case!", null);
                walle.pivot_IMU(-14);
                //walle.driveStraight_In(4);
                while (walle.getDistFromRight_In() < 7) //flipped signs since anything greater than 7 is going to be greater than 5
                {
                    walle.driveMotorsAuto(.16f,.16f);
                }
                walle.stopAllMotors();

                walle.pivot_IMU(14);
                //check for dist forward, if less than 12 then stop if greater than 12 keep going
            }
            else
            {
                telemetry.addData("I'm in the default Case!", null);
                walle.driveStraight_In(distToTravel);
                //check for dist forward, if less than 12 then stop if greater than 12 keep going
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




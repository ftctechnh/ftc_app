package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name= "PictureFirstAuto")
public class PictureFirstAuto extends LinearOpMode
{
    ParadeBot walle;
    VuforiaFunctions vuforiaFunctions;
   public void runOpMode()
    {       walle = new ParadeBot(hardwareMap, this);
            vuforiaFunctions = new VuforiaFunctions(this);
            double yawAngle = 90.0;
            double yawAngleTurn;
            waitForStart();
            walle.driveStraight_In(10,.6);
            walle.pivot(-40,-.6);
            while (walle.getDistFromFront_In() > 24)
                walle.driveStraight_In(6);

        {
                if (vuforiaFunctions.hasSeenTarget())
                {
                    telemetry.addData(vuforiaFunctions.getCurrentNameOfTargetSeen(), null);
                    telemetry.addData("X (in): ", vuforiaFunctions.getXPosIn());
                    telemetry.addData("Y (in): ", vuforiaFunctions.getYPosIn());
                    telemetry.addData("X (ft): ", vuforiaFunctions.getXPosIn()/12f);
                    telemetry.addData("Y (ft): ", vuforiaFunctions.getYPosIn()/12f);
                    telemetry.addData("YAW ", vuforiaFunctions.getYawDeg());
                    sleep(1000);
                    yawAngle = vuforiaFunctions.getYawDeg();
                    yawAngleTurn = 100.0- yawAngle;
                    walle.pivot(-yawAngleTurn, .6);
                }
                else
                {
                    telemetry.addData("Such target is not in my sight!",null);
                    walle.pivot(-90, .6);
                }

                telemetry.update();

        }

        double frontDist, rightDist;


        while (walle.getDistFromFront_In() > 18)
        {
            sleep(400);
            frontDist = walle.getDistFromFront_In();
            if(frontDist < 12)
            {
                walle.stopAllMotors();
                break;
            }
            else
            {
                telemetry.addData("Going forawrd 9", null);
                walle.driveStraight_In(11);
            }

            telemetry.addData("front Dist: ", frontDist);

            if (frontDist > 18)
            {
                rightDist = walle.getDistFromRight_In();
                telemetry.addData("frontDist>18", null);
                telemetry.addData("leftDist ", rightDist);
                if (rightDist < 4)
                {
                    telemetry.addData("leftdist < 4", null);
                    walle.pivot(-15);
                    walle.driveStraight_In(11);
                    walle.pivot(15);
                }
                else if (rightDist > 7)
                {
                    telemetry.addData("leftdist > 7", null);
                    walle.pivot(15);
                    walle.driveStraight_In(11);
                    walle.pivot(-15);
                }
            }
            telemetry.update();
            while(!gamepad1.a)
            {}
        }

        telemetry.addData("Stopped", null);
        telemetry.update();
        while (!isStopRequested())
        {
            walle.stopAllMotors();
        }
    }

}
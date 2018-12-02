package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "PictureFirstAuto")
public class PictureFirstAuto extends LinearOpMode
{
    ParadeBot walle;
    VuforiaFunctions vuforiaFunctions;

    public void runOpMode()
    {
        walle = new ParadeBot(hardwareMap, this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        double yawAngle = 90.0;
        double sensorDepth = 4;
        double yawAngleTurn;
        waitForStart();
        walle.driveStraight_In(10, .6);
        walle.pivot(-40, -.6);
        while (walle.getDistFromFront_In() > 18)
            walle.driveStraight_In(6);

        {
            if (vuforiaFunctions.hasSeenTarget())
            {
                telemetry.addData(vuforiaFunctions.getCurrentNameOfTargetSeen(), null);
                telemetry.addData("X (in): ", vuforiaFunctions.getXPosIn());
                telemetry.addData("Y (in): ", vuforiaFunctions.getYPosIn());
                telemetry.addData("X (ft): ", vuforiaFunctions.getXPosIn() / 12f);
                telemetry.addData("Y (ft): ", vuforiaFunctions.getYPosIn() / 12f);
                telemetry.addData("YAW ", vuforiaFunctions.getYawDeg());
                sleep(1000);
                yawAngle = vuforiaFunctions.getYawDeg();
                yawAngleTurn = 115.0 - yawAngle;
                walle.pivot(-yawAngleTurn, .6);
            }
            else
            {
                telemetry.addData("Such target is not in my sight!", null);
                walle.pivot(-45, .8);
            }

            telemetry.update();

        }

        double frontDist, rightDist;

        while (walle.getDistFromFront_In() > 18)
        {
            sleep(400);
            frontDist = walle.getDistFromFront_In();
            if (frontDist < 12)
            {
                walle.stopAllMotors();
                break;
            }
            else
            {
                telemetry.addData("Going forward 9", null);
                walle.driveStraight_In(11);
                sleep(400);
            }

            telemetry.addData("front Dist: ", frontDist);

            if (frontDist > 18)
            {
                rightDist = walle.getDistFromRight_In();
                telemetry.addData("front Dist>18", null);
                telemetry.addData("left Dist ", rightDist);
                sleep(400);
                if (rightDist < 6 + sensorDepth)
                {
                    telemetry.addData("left dist < 4", null);
                    walle.pivot(-7, .8);
                    walle.driveStraight_In(8);
                    walle.pivot(7, .8);
                    sleep(400);
                }
                else if (rightDist > 9 + sensorDepth)
                {
                    telemetry.addData("left dist > 7", null);
                    walle.pivot(7, .8);
                    walle.driveStraight_In(8);
                    walle.pivot(-7, .8);
                    sleep(400);
                }
                else if ((6 + sensorDepth < rightDist) & (rightDist < 9 + sensorDepth))
                {
                    telemetry.addData("neutral zone, 4-7 in", null);
                    walle.driveStraight_In(8, .6);
                    sleep(400);
                }
            }
            telemetry.update();
            while (!gamepad1.a)
            {
            }
        }
        telemetry.addData("Stopped", null);
        sleep(2000); //drop team marker into depot
        telemetry.update();
        walle.pivot(-90, .8);
        walle.driveStraight_In(96, .6);
        walle.stopAllMotors();
    }
}
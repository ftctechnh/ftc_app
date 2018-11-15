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
            boolean dummy = true;
            waitForStart();
            walle.driveStraight_In(8,.6);
            walle.pivot(-40,-.6);
            while (walle.getDistFromFront_In() > 24)
                walle.driveStraight_In(6);

        {
            while (dummy)
            {
                if (vuforiaFunctions.hasSeenTarget())
                {
                    telemetry.addData(vuforiaFunctions.getCurrentNameOfTargetSeen(), null);
                    telemetry.addData("X (in): ", vuforiaFunctions.getXPosIn());
                    telemetry.addData("Y (in): ", vuforiaFunctions.getYPosIn());
                    telemetry.addData("X (ft): ", vuforiaFunctions.getXPosIn()/12f);
                    telemetry.addData("Y (ft): ", vuforiaFunctions.getYPosIn()/12f);
                    telemetry.addData("YAW ", vuforiaFunctions.getYawDeg());
                }
                else
                {
                    telemetry.addData("Such target is not in my sight!",null);
                }

                telemetry.update();
            }
        }



        walle.stopAllMotors();
        sleep(10000);
    }
}
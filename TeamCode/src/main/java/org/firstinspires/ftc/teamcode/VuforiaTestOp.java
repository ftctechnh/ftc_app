package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Jeremy on 9/23/2018.
 */
@TeleOp(name = "VuforiaTestOp", group = "Test")
public class VuforiaTestOp extends OpMode
{
    VuforiaFunctions vuforiaFunctions;
    public void init()
    {
        vuforiaFunctions = new VuforiaFunctions(this);
    }

    public void start()
    {

    }

    public void loop()
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

    public void stop()
    {

    }
}

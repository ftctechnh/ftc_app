package org.firstinspires.ftc.teamcode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.VuforiaFunctions;

/**
 * Created by Jeremy on 9/23/2018.
 */
@TeleOp(name = "VuforiaTestOp", group = "Test")
@Disabled
public class VuforiaTestOp extends OpMode
{
    VuforiaFunctions vuforiaFunctions;
    public void init()
    {
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
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

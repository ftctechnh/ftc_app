package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
public abstract class VoltageBaseAutonomous extends VoltageBase
{
    public abstract void DriveTheRobot ();

    //if using vuforia, here is the place to initialize it.  I will figure that out soon.

    @Override
    public void DefineOpMode () {
        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }

        DriveTheRobot();
    }
}

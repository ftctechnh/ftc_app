package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Peter G on 12/1/2016.
 */
@Autonomous(name = "BeaconAutoNoNav", group = "Autonomous")
public class BeaconAutoNoNav extends LinearOpMode
{
    private OmniDriveBot robot = new OmniDriveBot();

    public void runOpMode()
    {
        robot.init(hardwareMap);
        waitForStart();
        robot.driveStraight(61, 130);
        sleep(100);
        robot.driveStraight(17, 180);
        sleep(100);
        robot.spin(92);
        telemetry.addData("RED", robot.getSensorBlue());
        telemetry.addData("BLUE", robot.getSensorRed());
        telemetry.update();
        sleep(10000);
    }
}

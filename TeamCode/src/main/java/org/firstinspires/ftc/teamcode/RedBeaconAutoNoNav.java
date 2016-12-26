package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Peter G on 12/1/2016.
 */
@Autonomous(name = "RedBeaconAutoNoNav", group = "Autonomous")
public class RedBeaconAutoNoNav extends LinearOpMode
{
    private OmniDriveBot robot = new OmniDriveBot();

    public void runOpMode()
    {
        robot.init(hardwareMap);
        waitForStart();
        robot.driveStraight(63, 130);
        sleep(100);
        //makes robot wait until a is pressed
        while(!gamepad1.a){}
        robot.driveStraight(14, 180);
        sleep(100);
        while(!gamepad1.a){}
        robot.spin(92);
        telemetry.addData("Hue", robot.getSensorHue());
        telemetry.update();
        sleep(10000);
    }
}

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
        while(opModeIsActive())
        {
            robot.driveStraightPow(58, 130, .75);
            sleep(100);
            //makes robot wait until a is pressed
            while (!gamepad1.a) {
            }
            robot.driveStraightPow(17, 180, .75);
            sleep(100);
            while (!gamepad1.a) {
            }
            robot.spin(92);
            sleep(100);
            telemetry.addData("RED?", robot.isDetectingRed());
            telemetry.addData("BLUE?", robot.isDetectingBlue());
            telemetry.update();
            sleep(10000);
        }
    }
}

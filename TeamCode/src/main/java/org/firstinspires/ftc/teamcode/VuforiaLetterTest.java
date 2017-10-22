package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 10/15/2017.
 */

@Autonomous(name = "Vuforia letters", group = "Test")
public class VuforiaLetterTest extends LinearOpMode
{
    private NewRobot robot;

    public void runOpMode() throws InterruptedException
    {
        robot = new NewRobot(hardwareMap);
        waitForStart();
        while(opModeIsActive())
        {
            telemetry.addData("Cipher", robot.getGlyphCipher());
            telemetry.update();
        }

    }
}

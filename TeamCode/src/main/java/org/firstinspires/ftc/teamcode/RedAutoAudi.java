package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 10/29/2017.
 */
@Autonomous(name = "RedAutoAudi", group = "Auto")
public class RedAutoAudi extends LinearOpMode
{
    private TankBase robot; //Used for the driving functions
    private NewRobot newRobot; //Will be used for vision tracking and/or getting the glyph position

    public void runOpMode()
    {
        robot = new TankBase(hardwareMap);
        newRobot = new NewRobot(hardwareMap);
        waitForStart(); //stops the code running until after pressing the play button on phone
        // Close doors
        // Lift up doors to get glyph
        // Lower the right wing
        // Look at the color of platform
        // Sense color of the jewel
        // if the jewel has the same color as the platform; move backwards; return to original position
        // else if the jewel isn't same color; move forward; return to original position
        // else (no color sense); do nothing
        // raise/retract arm arm
        // decode pictogram
        telemetry.addData("Pos ", newRobot.getGlyphCipher());
        telemetry.update();
        switch (newRobot.getGlyphCipher())
        {
            case 'r': robot.driveStraight_In(26.5f);
                break;
            case 'c': robot.driveStraight_In(34.5f);
                break;
            case 'l': robot.driveStraight_In(41);
                break;
            default: robot.driveStraight_In(35);
                break;
        }
        robot.pivot_IMU(-83, .25);
        robot.driveStraight_In(8,.1);
        //lower door attachment to ground
        //open door to release glyph
        robot.driveStraight_In(-2);
        robot.stopAllMotors();
    }
}

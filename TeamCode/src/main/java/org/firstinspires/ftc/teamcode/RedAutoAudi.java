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
        float x = 0; //Unbreaks it represents unknown values
        float center = 0; //distance for center in in
        robot = new TankBase(hardwareMap);
        newRobot = new NewRobot(hardwareMap);
        waitForStart(); //stops the code running until after pressing the play button on phone
        // Close doors
        // Lift up doors to get glyph
        // Lower the right arm
        // Look at the color of platform
        // Sense color of the jewel
        // if the jewel has the same color as the platform; move backwards; return to original position
        // else if the jewel isn't same color; move forward; return to orignal position
        // else (no color sense); do nothing
        // raise/retract arm arm
        // decode pictogram
        switch (newRobot.getGlyphCipher()) //Switch statement; it also decodes the image at the same time and
                                            //returns the column in a character
        {
            case 'l': robot.driveStraight_In(30); //lowercase L represents left;
                break;
            case 'c': robot.driveStraight_In(36); // represents the center; robot drives distance to center column
                break;
            case 'r': robot.driveStraight_In(42);//represents right; robot drives dist to right column
                break;
            default: robot.driveStraight_In(center); //robot goes into center if it can't find a glyph
                break;
        }
        robot.pivot_IMU(-90, .25);
        robot.driveStraight_In(16); //Drive to the glyph to column at normal speed first, but not into it
        robot.driveStraight_In(8, .25);
        //lower door attachment to ground
        //open door attachment to release glyph
        robot.driveStraight_In(-2); // back up
        robot.stopAllMotors();
    }
}

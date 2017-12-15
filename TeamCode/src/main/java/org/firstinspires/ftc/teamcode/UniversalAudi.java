package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy and Sahithi on 11/12/2017.
 */
@Autonomous(name = "UniversalAudi", group = "Auto")
public class UniversalAudi extends LinearOpMode
{
    NewRobot newRobot;
    TankBase robot;
    public void runOpMode()
    {
        newRobot = new NewRobot(hardwareMap);
        robot = new TankBase(hardwareMap);
        waitForStart();
        sleep(300);
        char colorOfPlatform = newRobot.getColor(newRobot.getFloorColorSens());
        telemetry.addData("color = ", colorOfPlatform);
        telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getFloorColorSens()));
        telemetry.update();
        sleep(3000);
        switch (colorOfPlatform)
        {
            case 'b':
                //Close the door to grab glyph
                //Lift doors to get glyph
                //Lower left arm
                //Sense color of platform
                //Sense color of front jewel
                //If the jewel is the same color as platform, then move backwards; return to original position
                //else if the jewel isn’t the same color; move forward; return to original position
                //else (no color sense); do nothing
                //raise/retract arm
                //decode pictogram (below)

                telemetry.addData("Pos ", newRobot.getGlyphCipher());
                telemetry.update();
                switch (newRobot.getGlyphCipher())
                {
                    case 'l': robot.driveStraight_In(28);
                        break;
                    case 'c': robot.driveStraight_In(39);
                        break;
                    case 'r': robot.driveStraight_In(45);
                        break;
                    default: robot.driveStraight_In(39);
                        break;
                }
                robot.pivot_IMU(83, .25);
                robot.driveStraight_In(12);
                robot.driveStraight_In(8,.2);
                //lower door attachment to ground
                //open door to release glyph
                robot.driveStraight_In(-2);
                robot.stopAllMotors();
                //INSERT CODE TO RAISE
                break;
            case'r':
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
                //INSERT CODE TO RAISE WING
                break;
            default:
                robot.driveStraight_In(30, .4);
                break;
        }
    }
}

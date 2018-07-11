package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi on 11/15/2017.
 */
@Disabled
@Autonomous(name = "!UniversalRef", group = "Auto")
public class UniversalRef extends LinearOpMode
{
    NewRobot newRobot;
    ParadeBot robot;
    public void runOpMode() {
        char cipher;
        newRobot = new NewRobot(hardwareMap);
        ParadeBot robot = new ParadeBot(hardwareMap);
        waitForStart();
        sleep(300);
        char colorOfPlatform = newRobot.getColor(newRobot.getFloorColorSens());
        telemetry.addData("platform color = ", colorOfPlatform);
        telemetry.addData("hue platform = ", newRobot.getHueValue(newRobot.getFloorColorSens()));
        telemetry.update();
        sleep(2000);
        //INSERT CODE CODE TO CLENCH GLYPH AND LIFT IT
        switch (colorOfPlatform) {
            case 'b':
                // Close doors
                // Lift up doors to get glyph
                // Lower the left arm
                cipher = newRobot.getGlyphCipher();
                telemetry.addData("Cipherr = ", cipher);
                telemetry.update();
                // Look at the color of platform
                // Sense color of the jewel
                // if the jewel has the same color as the platform; move backwards; return to original position
                // else if the jewel isn't same color; move forward; return to original position
                // else (no color sense); do nothing
                // raise/retract arm
                robot.driveStraight_In(24);
                robot.pivot_IMU(-90, .25);
                switch (cipher) {
                    case 'l':
                        robot.driveStraight_In(22);
                        break;
                    case 'c':
                        robot.driveStraight_In(11.5f);
                        break;
                    case 'r':
                        robot.driveStraight_In(5);
                        break;
                    default:
                        robot.driveStraight_In(11.5f);
                        break;
                }

                robot.pivot_IMU(83, .25);
                robot.driveStraight_In(8, .1);
                robot.driveStraight_In(8, .1);
                // lower door attachment to the ground
                //lower door attachment to ground
                //open door attachment to release glyph
                robot.driveStraight_In(-2); // back up
                robot.stopAllMotors();
                //INSERT CODE TO RAISE WING
                break;
            case 'r':
                // Close doors
                // Lift up doors to get glyph
                // Lower the right arm
                cipher = newRobot.getGlyphCipher();
                telemetry.addData("Cipherr = ", cipher);
                telemetry.update();
                // Look at the color of platform
                // Sense color of the jewel
                // if the jewel has the same color as the platform; move backwards; return to original position
                // else if the jewel isn't same color; move forward; return to original position
                // else (no color sense); do nothing
                // raise/retract arm
                robot.driveStraight_In(26);
                robot.pivot_IMU(82, .25);
                switch (cipher) {
                    case 'l':
                        robot.driveStraight_In(22); //needs testing, fix on 11/12
                        break;
                    case 'c':
                        robot.driveStraight_In(11.5f);
                        break;
                    case 'r':
                        robot.driveStraight_In(5); //positioning is really off, need to fix on 11/12
                        break;
                    default:
                        robot.driveStraight_In(11.5f);
                        break;
                }

                robot.pivot_IMU(-83, .25);
                robot.driveStraight_In(8, .1);
                robot.driveStraight_In(8, .1);
                // lower door attachment to the ground
                //lower door attachment to ground
                //open door attachment to release glyph
                robot.driveStraight_In(-2); // back up
                robot.stopAllMotors();
                //INSERT CODE TO RAISE WING
                break;
            default:
                robot.driveStraight_In(29, .4);
                break;
        }
    }
}

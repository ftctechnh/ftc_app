package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi on 12/8/17.
 */
@Autonomous(name = "FinalUniversalRef",group = "Auto")
public class FinalUniversalRef extends LinearOpMode
{
    NewRobotFinal newRobot;
    public void runOpMode()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.initVuforia(hardwareMap);
        sleep(300);
        char colorOfPlatform = newRobot.getColor(newRobot.getFloorColorSens());
        telemetry.addData("color = ", colorOfPlatform);
        telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getFloorColorSens()));
        telemetry.update();
        waitForStart();
        sleep(300);
        switch (colorOfPlatform)
        {
            case 'b':
                waitForStart();
                newRobot.openOrCloseDoor(true);
                newRobot.moveWing(true);
                newRobot.moveLift(1);
                sleep(1000);
                newRobot.getrightWingColorSens();
                char colorOfJewel = newRobot.getColor(newRobot.getrightWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getrightWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case'r':
                        newRobot.driveStraight_In(-8);
                        sleep(500);
                        newRobot.driveStraight_In(8);
                        break;
                    case 'b':
                        newRobot.driveStraight_In(8);
                        sleep(500);
                        newRobot.driveStraight_In(-8);
                        break;
                    default:
                        break;
                }
                newRobot.moveWing(false);
                sleep(500);
                char cipher = newRobot.getGlyphCipher();
                telemetry.addData("Cipherr = ", cipher);
                telemetry.addData("Pos ", newRobot.getGlyphCipher());
                telemetry.update();
                switch (newRobot.getGlyphCipher())
                {
                    case 'l':
                        newRobot.driveStraight_In(22);
                        break;
                    case 'c':
                        newRobot.driveStraight_In(11.5f);
                        break;
                    case 'r':
                        newRobot.driveStraight_In(5);
                        break;
                    default:
                        newRobot.driveStraight_In(11.5f);
                        break;
                }
                newRobot.pivot_IMU(83, .25);
                newRobot.driveStraight_In(8, .1);
                newRobot.driveStraight_In(8, .1);
                newRobot.moveLift(-1);
                newRobot.openOrCloseDoor(false);
                newRobot.driveStraight_In(-2); // back up
                newRobot.driveStraight_In(3,.2);
                newRobot.driveStraight_In(-2,1);
                newRobot.stopAllMotors();
            case 'r':
                waitForStart();
                newRobot.openOrCloseDoor(true);
                newRobot.moveWing(true);
                newRobot.moveLift(1);
                sleep(1000);
                newRobot.getrightWingColorSens();
                colorOfJewel = newRobot.getColor(newRobot.getrightWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getrightWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case 'r':
                        newRobot.driveStraight_In(8);
                        sleep(500);
                        newRobot.driveStraight_In(-8);
                    case 'b':
                        newRobot.driveStraight_In(-8);
                        sleep(500);
                        newRobot.driveStraight_In(8);
                        break;
                    default:
                        break;
                }
                newRobot.moveWing(false);
                sleep(500);
                cipher = newRobot.getGlyphCipher();
                telemetry.addData("Cipherr = ", cipher);
                telemetry.addData("Pos ", newRobot.getGlyphCipher());
                telemetry.update();
                newRobot.driveStraight_In(26);
                newRobot.pivot_IMU(82, .25);
                switch (newRobot.getGlyphCipher())
                {
                    case 'l':
                        newRobot.driveStraight_In(22); //needs testing, fix on 11/12
                        break;
                    case 'c':
                        newRobot.driveStraight_In(11.5f);
                        break;
                    case 'r':
                        newRobot.driveStraight_In(5); //positioning is really off, need to fix on 11/12
                        break;
                    default:
                        newRobot.driveStraight_In(11.5f);
                        break;
                }
                newRobot.pivot_IMU(-83, .25);
                newRobot.driveStraight_In(8,.1);
                newRobot.driveStraight_In(8,.1);
                newRobot.moveLift(-1);
                newRobot.openOrCloseDoor(false);
                newRobot.driveStraight_In(-2); // back up
                newRobot.driveStraight_In(3,.2);
                newRobot.driveStraight_In(-2,1);
                newRobot.stopAllMotors();
            default:
                newRobot.driveStraight_In(29, .4);
                newRobot.openOrCloseDoor(true);
                newRobot.moveLift(1);
                break;
        }
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi on 12/8/17.
 */
@Autonomous(name = "FinalUniversalAudi",group = "Auto")
public class FinalUniversalAudi extends LinearOpMode
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
                //drive is functional, still needs positional tweaking 12/11
        {
            case 'b':
                newRobot.openOrCloseDoor(true);
                newRobot.moveWing(true);
                newRobot.oldMoveLift(1);
                sleep(500);
                char cipher = newRobot.getGlyphCipher();
                char colorOfJewel = newRobot.getColor(newRobot.getleftWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getleftWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case'r':
                        newRobot.driveStraight_In(8);
                        sleep(500);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(-8);
                        break;
                    case 'b':
                        newRobot.driveStraight_In(-8);
                        sleep(500);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(8);
                        break;
                    default:
                        newRobot.moveWing(false);
                        break;
                }
                telemetry.addData("Cipherr = ", cipher);
                telemetry.update();
                switch (cipher)
                {
                    case 'r': newRobot.driveStraight_In(46);
                        break;
                    case 'c': newRobot.driveStraight_In(36);
                        break;
                    case 'l': newRobot.driveStraight_In(25);
                        break;
                    default: newRobot.driveStraight_In(36);
                        break;
                }
                newRobot.pivot_IMU(86, .25);
                newRobot.oldMoveLift(-1);
                newRobot.openOrCloseDoor(false);
                newRobot.driveStraight_In(23);
                newRobot.driveStraight_In(-5,1);
                newRobot.stopAllMotors();
            case 'r':
                newRobot.openOrCloseDoor(true);
                newRobot.moveWing(true);
                newRobot.oldMoveLift(1);
                sleep(500);
                cipher = newRobot.getGlyphCipher();
                colorOfJewel = newRobot.getColor(newRobot.getleftWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getleftWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case 'r':
                        newRobot.driveStraight_In(-8);
                        sleep(500);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(8);
                        break;
                    case 'b':
                        newRobot.driveStraight_In(8);
                        sleep(500);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(-8);
                        break;
                    default:
                        newRobot.moveWing(false);
                        break;
                }
                telemetry.addData("Cipherr = ", cipher);
                telemetry.update();
                switch (newRobot.getGlyphCipher())
                {
                    case 'r': newRobot.driveStraight_In(26.5f);
                        break;
                    case 'c': newRobot.driveStraight_In(34.5f);
                        break;
                    case 'l': newRobot.driveStraight_In(41);
                        break;
                    default: newRobot.driveStraight_In(34.5f);
                        break;
                }
                newRobot.pivot_IMU(-86, .25);
                newRobot.oldMoveLift(-1);
                newRobot.openOrCloseDoor(false);
                /*newRobot.driveStraight_In(8,.1);
                newRobot.driveStraight_In(-2);
                newRobot.driveStraight_In(3,.2);
                newRobot.driveStraight_In(-10,1);*/
                newRobot.stopAllMotors();
            default:
                newRobot.openOrCloseDoor(true);
                sleep(500);
                newRobot.oldMoveLift(1);
                newRobot.driveStraight_In(35, .4);
                break;
        }
        newRobot.stopDriveMotors();
    }
}

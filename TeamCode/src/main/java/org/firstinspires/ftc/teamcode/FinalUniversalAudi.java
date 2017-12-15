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
        waitForStart();
        sleep(300);
        char colorOfPlatform = newRobot.getColor(newRobot.getFloorColorSens());
        telemetry.addData("color = ", colorOfPlatform);
        telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getFloorColorSens()));
        telemetry.update();
        sleep(3000);
        switch (colorOfPlatform)
                //drive is functional, still needs positional tweaking 12/11
        {
            case 'b':
                waitForStart();
                newRobot.openOrCloseDoor(true);
                //newRobot.moveXEncoderCounts(50,1,true);
                newRobot.moveWing(true);
                sleep(1000);
                char cipher = newRobot.getGlyphCipher();
                newRobot.getleftWingColorSens();
                char colorOfJewel = newRobot.getColor(newRobot.getleftWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getleftWingColorSens()));
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
                telemetry.addData("Cipherr = ", cipher);
                telemetry.update();
                switch (cipher)
                {
                    case 'r': newRobot.driveStraight_In(45);
                        break;
                    case 'c': newRobot.driveStraight_In(39);
                        break;
                    case 'l': newRobot.driveStraight_In(28);
                        break;
                    default: newRobot.driveStraight_In(39);
                        break;
                }
                newRobot.pivot_IMU(83, .25);
                newRobot.driveStraight_In(12);
                newRobot.driveStraight_In(8,.2);
                newRobot.moveXEncoderCounts(100,1,false);
                newRobot.openOrCloseDoor(false);
                newRobot.driveStraight_In(-2);
                newRobot.driveStraight_In(3,.2);
                newRobot.stopAllMotors();
            case 'r':
                waitForStart();
                newRobot.openOrCloseDoor(true);
                newRobot.moveXEncoderCounts(100,1,true);
                newRobot.moveWing(true);
                sleep(1000);
                newRobot.getleftWingColorSens();
                colorOfJewel = newRobot.getColor(newRobot.getleftWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getleftWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case 'r':
                        newRobot.driveStraight_In(8);
                        sleep(500);
                        newRobot.driveStraight_In(-8);
                        break;
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
                switch (newRobot.getGlyphCipher())
                {
                    case 'r': newRobot.driveStraight_In(26.5f);
                        break;
                    case 'c': newRobot.driveStraight_In(34.5f);
                        break;
                    case 'l': newRobot.driveStraight_In(41);
                        break;
                    default: newRobot.driveStraight_In(35);
                        break;
                }
                newRobot.pivot_IMU(-83, .25);
                newRobot.driveStraight_In(8,.1);
                newRobot.moveXEncoderCounts(100,1,false);
                newRobot.openOrCloseDoor(false);
                newRobot.driveStraight_In(-2);
                newRobot.driveStraight_In(3,.2);
                newRobot.stopAllMotors();
            default:
                newRobot.driveStraight_In(29, .4);
                break;
        }
    }
}

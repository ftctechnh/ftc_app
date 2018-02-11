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
        float adjustment = 0;
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.initAutoFunctions(hardwareMap, this);
        char colorOfPlatform = newRobot.getColor(newRobot.getFloorColorSens());
        telemetry.addData("color = ", colorOfPlatform);
        telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getFloorColorSens()));
        telemetry.update();
        waitForStart();
        char cipher = newRobot.getGlyphCipher();
        switch (colorOfPlatform)
        {
            case 'b':
                newRobot.openOrCloseDoor(true);
                newRobot.moveWing(true);
                newRobot.oldMoveLift(1);
                char colorOfJewel = newRobot.getColor(newRobot.getleftWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getleftWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case'r':
                        adjustment = 4;
                        newRobot.driveStraight_In(adjustment);
                        break;
                    case 'b':
                        adjustment = -3;
                        newRobot.driveStraight_In(adjustment);
                        adjustment = -6;
                        break;
                    default:
                        adjustment = -2;
                        break;
                }
                newRobot.moveWing(false);
                telemetry.addData("Cipher = ", cipher);
                telemetry.update();
                switch (cipher)
                {
                    case 'l':
                        newRobot.driveStraight_In(18f - adjustment,.6);
                        newRobot.pivot(85, .6);
                        break;
                    case 'r':
                        newRobot.driveStraight_In(30 - adjustment,.6);
                        newRobot.pivot(85, .6);
                        break;
                    case 'c':
                    default:
                        newRobot.driveStraight_In(24 - adjustment,.6);
                        newRobot.pivot(85, .6);
                        break;
                }
                newRobot.oldMoveLift(-1);
                newRobot.openOrCloseDoor(false);
                newRobot.driveStraight_In_Stall(14, .25);
                newRobot.driveStraight_In(-6,1);
                //newRobot.pivot(-180,.7);
                newRobot.stopAllMotors();
                break;
            case 'r':
                newRobot.openOrCloseDoor(true);
                newRobot.moveWing(true);
                newRobot.oldMoveLift(1);
                cipher = newRobot.getGlyphCipher();
                colorOfJewel = newRobot.getColor(newRobot.getrightWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getrightWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case 'r':
                        adjustment = -3;
                        newRobot.driveStraight_In(adjustment);
                        adjustment = -7;
                        break;
                    case 'b':
                        adjustment = 6;
                        newRobot.driveStraight_In(adjustment);
                        break;
                    default:
                        break;
                }
                newRobot.moveWing(false);
                telemetry.addData("Cipher = ", cipher);
                telemetry.update();
                switch (cipher)
                {
                    case 'r':
                        newRobot.driveStraight_In(20f - adjustment,.6);
                        newRobot.pivot(-85, .6);
                        break;
                    case 'l':
                        newRobot.driveStraight_In(24 - adjustment,.6);
                        newRobot.pivot(-85, .6);
                        break;
                    case 'c':
                    default:
                        newRobot.driveStraight_In(18.5f - adjustment,.6);
                        newRobot.pivot(-85, .6);
                        break;
                }
                newRobot.oldMoveLift(-1);
                newRobot.openOrCloseDoor(false);
                newRobot.driveStraight_In_Stall(16,.25);
                newRobot.driveStraight_In(-6);
                //newRobot.pivot(180,.7);
                newRobot.stopAllMotors();
                break;
            default:
                newRobot.openOrCloseDoor(true);
                newRobot.oldMoveLift(1);
                newRobot.driveStraight_In(35, .4);
                break;
        }
        newRobot.stopDriveMotors();
    }
}
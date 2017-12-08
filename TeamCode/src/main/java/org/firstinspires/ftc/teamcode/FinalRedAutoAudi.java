package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi on 12/6/17.
 */
@Autonomous(name = "FinalRedAutoAudi", group = "Auto")
public class FinalRedAutoAudi extends LinearOpMode
{
    private NewRobotFinal newRobot;

    public void runOpMode()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        waitForStart();
        newRobot.openOrCloseDoor(true);
        //raise lift slightly off ground
        newRobot.moveWing(true);
        sleep(1000);
        newRobot.getrightWingColorSens();
        char colorOfJewel = newRobot.getColor(newRobot.getrightWingColorSens());
        telemetry.addData("jewel color = ", colorOfJewel);
        telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getrightWingColorSens()));
        telemetry.update();
        switch (colorOfJewel)
        {
            case 'r':
                newRobot.driveStraight_In(-6);
                sleep(500);
                newRobot.driveStraight_In(6);
                break;
            case 'b':
                newRobot.driveStraight_In(6);
                sleep(500);
                newRobot.driveStraight_In(-6);
                break;
            default:
                break;
        }
        newRobot.moveWing(false);
        sleep(500);
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
        //lower lift back to ground
        newRobot.openOrCloseDoor(false);
        newRobot.driveStraight_In(-2);
        newRobot.stopAllMotors();
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi on 12/8/17.
 */
@Disabled
@Autonomous(name = "FinalBlueAutoAudi",group = "Auto")
public class FinalBlueAutoAudi extends LinearOpMode
{
    private NewRobotFinal newRobot;

    public void runOpMode()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.initVuforia(hardwareMap);
        waitForStart();
        newRobot.openOrCloseDoor(true);
        newRobot.moveXEncoderCounts(100,1,true);
        newRobot.moveWing(true);
        sleep(1000);
        newRobot.getleftWingColorSens();
        char colorOfJewel = newRobot.getColor(newRobot.getleftWingColorSens());
        telemetry.addData("jewel color = ", colorOfJewel);
        telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getrightWingColorSens()));
        telemetry.update();
        switch (colorOfJewel)
        {
            case'r':
                newRobot.driveStraight_In(6);
                sleep(500);
                newRobot.driveStraight_In(-6);
                break;
            case 'b':
                newRobot.driveStraight_In(-6);
                sleep(500);
                newRobot.driveStraight_In(6);
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
    }
}

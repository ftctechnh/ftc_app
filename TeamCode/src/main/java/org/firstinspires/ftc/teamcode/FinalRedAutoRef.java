package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi on 12/8/17.
 */
@Autonomous(name = "FinalRedAutoRef",group = "Auto")
public class FinalRedAutoRef extends LinearOpMode
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
        char cipher = newRobot.getGlyphCipher();
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
        newRobot.moveXEncoderCounts(100,1,false);
        newRobot.openOrCloseDoor(false);
        newRobot.driveStraight_In(-2); // back up
        newRobot.driveStraight_In(3,.2);
        newRobot.stopAllMotors();
    }
}
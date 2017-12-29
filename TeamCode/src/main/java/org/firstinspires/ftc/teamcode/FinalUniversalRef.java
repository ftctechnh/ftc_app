package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Harshini on 12/8/17.
 */
@Autonomous(name = "FinalUniversalRef",group = "Auto")
public class FinalUniversalRef extends LinearOpMode
{
    NewRobotFinal newRobot;
    public void runOpMode()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.initVuforia(hardwareMap);
        sleep(250);
        char colorOfPlatform = newRobot.getColor(newRobot.getFloorColorSens());
        telemetry.addData("color = ", colorOfPlatform);
        telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getFloorColorSens()));
        telemetry.update();
        waitForStart();
        sleep(300);
        switch (colorOfPlatform)
        {
            case 'b':
                newRobot.openOrCloseDoor(true);
                newRobot.moveWing(true);
                newRobot.oldMoveLift(1);
                sleep(250);
                char cipher = newRobot.getGlyphCipher();
                char colorOfJewel = newRobot.getColor(newRobot.getleftWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getleftWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case'r':
                        newRobot.driveStraight_In(8);
                        //sleep(100);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(-8,.5);
                        sleep(100);
                        break;
                    case 'b':
                        newRobot.driveStraight_In(-8);
                        //sleep(100);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(8,.5);
                        sleep(100);
                        break;
                    default:
                        newRobot.moveWing(false);
                        break;
                }
                telemetry.addData("Cipherr = ", cipher);
                telemetry.update();
                switch (newRobot.getGlyphCipher())
                {
                    case 'l':
                        newRobot.driveStraight_In(5);
                        break;
                    case 'c':
                        newRobot.driveStraight_In(11.5f);
                        break;
                    case 'r':
                        newRobot.driveStraight_In(22);
                        break;
                    default:
                        newRobot.driveStraight_In(11.5f);
                        break;
                }
                newRobot.pivot_IMU(80, .25);
                newRobot.oldMoveLift(-1);
                newRobot.openOrCloseDoor(false);
                newRobot.driveStraight_In(20);
                newRobot.driveStraight_In(3,.2);
                newRobot.driveStraight_In(-5,1);
                newRobot.stopAllMotors();
                break;
            case 'r':
                newRobot.openOrCloseDoor(true);
                newRobot.moveWing(true);
                newRobot.oldMoveLift(1);
                sleep(250);
                cipher = newRobot.getGlyphCipher();
                colorOfJewel = newRobot.getColor(newRobot.getrightWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getrightWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case 'r':
                        newRobot.driveStraight_In(-8);
                        sleep(100);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(8,.5);
                        sleep(100);
                    case 'b':
                        newRobot.driveStraight_In(8);
                        sleep(100);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(-8,.5);
                        sleep(100);
                        break;
                    default:
                        newRobot.moveWing(false);
                        break;
                }
                telemetry.addData("Cipherr = ", cipher);
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
                        newRobot.driveStraight_In(32);
                        break;
                }
                newRobot.pivot_IMU(-80, .25);
                newRobot.oldMoveLift(-1);
                newRobot.openOrCloseDoor(false);
                /*newRobot.driveStraight_In(16,.2);
                newRobot.driveStraight_In(-5,1); */
                newRobot.driveStraight_In(20);
                newRobot.driveStraight_In(3,.2);
                newRobot.driveStraight_In(-5,1);
                newRobot.stopAllMotors();
                break;
            default:
                newRobot.driveStraight_In(29, .4);
                newRobot.openOrCloseDoor(true);
                sleep(200);
                newRobot.oldMoveLift(1);
                break;
        }
    }
}

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
        float adjustment = 0;
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.initAutoFunctions(hardwareMap);
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
                        adjustment = 3;
                        newRobot.driveStraight_In(adjustment);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(2);
                        break;
                    case 'b':
                        adjustment = 3;
                        newRobot.driveStraight_In(0 - adjustment);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(adjustment + 4);
                        break;
                    default:
                        newRobot.moveWing(false);
                        break;
                }
                telemetry.addData("Cipherr = ", cipher);
                telemetry.update();
                switch (cipher)
                {
                    case 'l':
                        newRobot.pivot(-50,.6);
                        newRobot.driveStraight_In(18.5f);
                        newRobot.openOrCloseDoor(false);
                        newRobot.driveStraight_In(18.5f);
                        break;
                    case 'c':
                        newRobot.pivot(-35,.6);
                        newRobot.driveStraight_In(17);
                        newRobot.openOrCloseDoor(false);
                        newRobot.driveStraight_In(17);
                        break;
                    case 'r':
                        newRobot.pivot(-20,.6);
                        newRobot.driveStraight_In(15);
                        newRobot.openOrCloseDoor(false);
                        newRobot.driveStraight_In(15);
                        break;
                    default:
                        newRobot.pivot(-35,.6);
                        newRobot.driveStraight_In(17);
                        newRobot.openOrCloseDoor(false);
                        newRobot.driveStraight_In(17);
                        break;
                }
                newRobot.driveStraight_In(-5,1);
                newRobot.stopAllMotors();
                break;

            case 'r':
                newRobot.openOrCloseDoor(true);
                newRobot.moveWing(true);
                newRobot.oldMoveLift(1);
                colorOfJewel = newRobot.getColor(newRobot.getrightWingColorSens());
                telemetry.addData("jewel color = ", colorOfJewel);
                telemetry.addData("Hue value", newRobot.getHueValue(newRobot.getrightWingColorSens()));
                telemetry.update();
                switch (colorOfJewel)
                {
                    case 'r':
                        adjustment = 3;
                        newRobot.driveStraight_In(0 - adjustment);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(adjustment + 4);
                        break;
                    case 'b':
                        adjustment = 3;
                        newRobot.driveStraight_In(adjustment);
                        newRobot.moveWing(false);
                        newRobot.driveStraight_In(1);
                        break;
                    default:
                        newRobot.moveWing(false);
                        break;
                }
                telemetry.addData("Cipherr = ", cipher);
                telemetry.update();
                /*newRobot.driveStraight_In(26);
                newRobot.pivot_IMU(82, .25);*/
                switch (cipher)
                {
                    case 'l':
                        newRobot.pivot(35, .5);
                        newRobot.driveStraight_In_Stall(37.5f, .5, telemetry);
                        break;
                    case 'c':
                        newRobot.pivot(20, .5);
                        newRobot.driveStraight_In_Stall(34, .5, telemetry);
                        break;
                    case 'r':
                        newRobot.pivot(12, .5);
                        newRobot.driveStraight_In_Stall(30.5f, .5, telemetry); //positioning is really off, need to fix on 11/12
                        break;
                    default:
                        newRobot.pivot(20, .5);
                        newRobot.driveStraight_In_Stall(30.5f, .5, telemetry);
                        break;
                }
                /*newRobot.pivot_IMU(-80, .25);
                newRobot.oldMoveLift(-1);
                newRobot.openOrCloseDoor(false);
                newRobot.driveStraight_In(16,.2);
                newRobot.driveStraight_In(-5,1);
                newRobot.driveStraight_In(20);
                newRobot.driveStraight_In(3,.2);
                newRobot.driveStraight_In(-5,1);
                newRobot.stopAllMotors();*/
                break;
            default:
                newRobot.driveStraight_In(29, .4);
                newRobot.openOrCloseDoor(true);
                newRobot.oldMoveLift(1);
                break;
        }
    }
}
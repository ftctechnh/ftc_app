package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Sahithi on 1/3/18.
 */
@Disabled
@TeleOp(name = "Test Stall Prog Base",group = "Auto")
public class TestStall extends LinearOpMode
{
    TankBase robot;
    public void runOpMode()
    {
        robot = new TankBase(hardwareMap);
        waitForStart();
        robot.driveStraight_In_Stall(35, .5, telemetry);
        while(!gamepad1.a)
        {

        }
        telemetry.addData("RightVel ", robot.getDriveRightOne().getVelocity(AngleUnit.DEGREES));
        telemetry.addData("RightVel ", robot.getDriveRightOne().getVelocity(AngleUnit.DEGREES));

    }/*
    NewRobotFinal newRobot;
    public void runOpMode()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.initVuforia(hardwareMap);
        {
            //newRobot.driveStraight_In(20);

            float encTarget = newRobot.neverrestEncCountsPerRev / newRobot.wheelCircIn * 20;
            //You get the number of encoder counts per unit and multiply it by how far you want to go
            double pow = 0.75;
            float absPow = (float) Math.abs(pow);
            newRobot.resetDriveEncoders();
            //Notes: We are using Andymark Neverrest 40
            // 1120 counts per rev

            if (pow < 0) {
                inches *= -1;
            }
            if (inches < 0)
            {
                newRobot.driveMotorsAuto(-absPow, -absPow);
                //left is opposite because drive motors flips it
                while (newRobot.driveLeftOne.getCurrentPosition() < -encTarget && newRobot.driveRightOne.getCurrentPosition() > encTarget) {
                    // if (Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES) <  *.75 )
                    if (driveRightOne.getVelocity(AngleUnit.DEGREES) > -5 || driveLeftOne.getVelocity(AngleUnit.DEGREES) < 5)
                        break;
                }
            } else {
                newRobot.driveMotorsAuto(absPow, absPow);

                while (newRobot.driveLeftOne.getCurrentPosition() > -encTarget && newRobot.driveRightOne.getCurrentPosition() < encTarget) {
                    if (driveRightOne.getVelocity(AngleUnit.DEGREES) < 5 || driveLeftOne.getVelocity(AngleUnit.DEGREES) > -5) {
                        break;
                    }
                }

                newRobot.stopDriveMotors();
            }

        }
    }
    */
}

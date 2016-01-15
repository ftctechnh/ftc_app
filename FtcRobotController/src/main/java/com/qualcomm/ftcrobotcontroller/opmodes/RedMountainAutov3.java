package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


/**
 * Created by Kaitlin on 1/5/16.
 */
public class RedMountainAutov3 extends LinearOpMode
{
    CompBot drive;

    @Override
    public void runOpMode()  throws InterruptedException
    {
        drive = new CompBot(hardwareMap);

        telemetry.addData("moving robot", 2);

        //drive.moveStraightEncoders(24,1);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(1);
        sleep(2500);

        //drive.spinOnCenter(-45, (float) 0.25); //leftTurn(45, .25);//(-45,.25);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(750);

       //drive.moveStraightEncoders(46, 1);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(49000);

        //drive.spinOnCenter(-100, (float) 0.25);//leftTurn(90, 0.25); //should only be 90 but calibration
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(9900);

        //drive.moveStraightEncoders(47, 1);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(4000);

        //......pitch front tracks
        //drive.pitchFrontTracks(2000, 1);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(750);

        //........pitch back tracks
        //.....telemetry.addData("(7)Before pitching back tracks first time",2);
        //drive.pitchBackTracks(1000,1);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(750);

        //......move forward 4 inches
        //...... telemetry.addData("(8)move forward 4 inches",2);
       // drive.moveStraightEncoders(4,1);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(750);

        //.....pitch front tracks
        //drive.pitchFrontTracks(4000,1);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(750);

        //.,.....pitch back tracks
        //drive.pitchBackTracks(2000,1);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(750);

        //move forward 4 inches
        //drive.moveStraightEncoders(4,1);
        drive.getRightMotor().setPower(1);
        drive.getLeftMotor().setPower(-.5);
        sleep(750);
    }
}

package org.firstinspires.ftc.teamcode.Main;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaCodecInfo;
import android.view.View;
import android.widget.Button;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.AutonomousGeneral;

/**
 * Created by adityamavalankar on 1/5/17.
 */

@Autonomous(name="Autonomous Far Start")
public class AutonomousFarStart extends AutonomousGeneral {

    //blue: tangent to blue line
    //red: tangent to red line

    private ElapsedTime runtime = new ElapsedTime();

    static  int             INITIAL_SHOOTERPOS;

    @Override
    public void runOpMode(){

        initiate();

        INITIAL_SHOOTERPOS = shooting_motor.getCurrentPosition();

        waitForStart();

        encoderDrive(0.5, 80, 80, 10);

       sleep(500);

        //gyro_leftTurn(70, 0.1);

        //drive foward to line up in front of beacon
        encoderDrive(0.5,-43 ,43,3.7);

        //wait for motors to settle
        sleep(250);

        //shoot first ball we start with,start intake in case have more balls
        shootingDrive(1, 900);
        intakeDrive(0.8, 1500);
        sleep(300);
        //shoot again
        shootingDrive(1, 900);
        intakeDrive(0.8, 1500);
        sleep(300);

        shootingDrive(1, 900);



        sleep(150);

        //gyro_rightTurn(70, 0.1);
        //turn back to line up for vortex
        encoderDrive(0.5, 45 ,-45,3.7);
        //wait 0.5 sec
        sleep(150);

        encoderDrive(0.5, 100, 100, 8);
        sleep(100);
        encoderDrive(0.3, 50, 50, 8);
        telemetry.addData("it is done!", "");
        telemetry.update();
    }
}

package org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by adityamavalankar on 1/5/17.
 */

@Autonomous(name="Autonomous Far Start")
@Disabled
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

        encoderDrive(0.3, 87, 87, 10);

       sleep(500);

        //gyro_leftTurn(70, 0.1);

        //drive foward to line up in front of beacon
        encoderDrive(0.2,-31 ,31,3.7);

        //wait for motors to settle
        sleep(250);

        //shoot first ball we start with,start intake in case have more balls
        shootingDrive(1, 900);
        intakeDrive(0.8, 1500);
        sleep(300);
        //shoot again
        shootingDrive(1, 900);
        sleep(300);



        sleep(150);

        //gyro_rightTurn(70, 0.1);
        //turn back to line up for vortex
        encoderDrive(0.2, 29 ,-29,3.7);
        //wait 0.5 sec
        sleep(150);

        encoderDrive(0.3, 100, 100, 8);
        sleep(100);
        encoderDrive(0.3, -10, -10, 8);
        sleep(300);
        encoderDrive(0.3, 30, 30, 8);
        telemetry.addData("it is done!", "");
        telemetry.update();
    }
}

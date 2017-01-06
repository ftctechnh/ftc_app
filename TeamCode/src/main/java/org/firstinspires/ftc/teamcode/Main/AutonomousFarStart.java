package org.firstinspires.ftc.teamcode.Main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by adityamavalankar on 1/5/17.
 */

@Autonomous(name="AutonomousBlueTwo")
public class AutonomousFarStart extends AutonomousGeneral {

    private ElapsedTime runtime = new ElapsedTime();

    static  int             INITIAL_SHOOTERPOS;

    @Override
    public void runOpMode(){

        initiate();

        INITIAL_SHOOTERPOS = shooting_motor.getCurrentPosition();

        waitForStart();

        encoderDrive(0.5, 120, 120, 10);

        sleep(500);

        //gyro_leftTurn(70, 0.1);

        //drive foward to line up in front of beacon
        encoderDrive(0.2,-55,55,8);

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
        encoderDrive(0.2,55,-55,8);
        //wait 0.5 sec
        sleep(150);

        encoderDrive(0.75, 55, 55, 8);
        encoderDrive(0.75, -15, -15, 8);
        encoderDrive(0.75, 15, 15, 8);
        telemetry.addData("it is done!", "");
        telemetry.update();
    }
}

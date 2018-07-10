package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Inspiration Team on 4/10/2018.
 */

@Autonomous(name = "encoderMecanumDriveStraight")
public class encoderMecanumDriveStraight extends Autonomous_General_George_ {

    public void runOpMode(){
        initiate(false);

        waitForStart();

        sleep(500);
        encoderMecanumDrive(0.5, 25, 25, 5000, 0);
        sleep(100);

        sleep(500);
        encoderMecanumDrive(0.5, -25, -25, 5000, 0);
        sleep(100);
    }
}

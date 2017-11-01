package org.firstinspires.ftc.teamcode.ftc2016to2017season.CharlieTestCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.AutonomousGeneral_charlie;

/**
 * Created by inspirationteam on 2/19/2017.
 */
@Autonomous(name = "autonomous")
@Disabled
public class CharlieTestWheels extends AutonomousGeneral_charlie
{

    public void runOpMode(){
        initiate();
        setMotorsModeToEncDrive();
        waitForStart();

       /* sleep(1000);
        strafeLeft(0.8);
        sleep(1000);
        strafeRight(0.8);
        sleep(1000);
        straightDrive(0.8);
        sleep(1000);
        straightDrive(-0.8);
        sleep(1000);
        encoderMecanumCrossDrive(0.8,50,50,10,1);
        sleep(1000);
        encoderMecanumCrossDrive(0.8,50,50,10,2);
        sleep(1000);
        encoderMecanumCrossDrive(0.8,50,50,10,3);
        sleep(1000);
        encoderMecanumCrossDrive(0.8,50,50,10,4);*/

        sleep(1000);
        encoderMecanumDrive(0.6,50,50,10,1);
        sleep(1000);
        encoderMecanumDrive(0.6,50,50,10,-1);
        sleep(1000);
        encoderMecanumDrive(0.6,50,50,10,0);
        sleep(1000);
        encoderMecanumDrive(0.6,-50,-50,10,0);
        sleep(1000);
        encoderMecanumCrossDrive(0.6,50,50,10,1);
        sleep(1000);
        encoderMecanumCrossDrive(0.6,50,50,10,2);
        sleep(1000);
        encoderMecanumCrossDrive(0.6,50,50,10,3);
        sleep(1000);
        encoderMecanumCrossDrive(0.6,50,50,10,4);



    }

    public void Turn360Right(){
        setMotorsToEnc(29,29,2);
        encoderDrive(.7,132,-132,8);
        stopMotors();
    }
    public void Turn360Left(){
        setMotorsToEnc(29,29,2);
        encoderDrive(.7,-132,132,8);
        stopMotors();
    }

}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral_charlie;
import org.firstinspires.ftc.teamcode.Main.beta.AutonomousGeneral;

/**
 * Created by inspirationteam on 2/19/2017.
 */
@Autonomous(name = "autonomous")
///@Disabled
public class TestMode extends AutonomousGeneral_charlie
{

    public void runOpMode(){
        initiate();
        waitForStart();
        sleep(1000);
        encoderMecanumDrive(0.8,50,50,10,1);
        sleep(1000);
        encoderMecanumDrive(0.8,50,50,10,-1);
//        initiate();
//        waitForStart();
//        setMotorsModeToEncDrive();
//        stopMotors();
//        encoderDrive(.7,100,100,3);
//        sleep(5000);
//        Turn360Right();
//        sleep(5000);
//        Turn360Left();
//        sleep(5000);
//        setMotorsModeToEncDrive();
//        stopMotors();
//        encoderDrive(.7,-100,-100,3);


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

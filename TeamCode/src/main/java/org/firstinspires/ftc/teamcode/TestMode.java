package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral;

/**
 * Created by inspirationteam on 2/19/2017.
 */
@Autonomous(name = "autonomous")
public class TestMode extends AutonomousGeneral {

    public void runOpMode(){
        initiate();
        waitForStart();
        encoderDrive(1,10,10,3);
        sleep(5000);
        Turn360Right();
        sleep(5000);
        Turn360Left();
        sleep(5000);
        encoderDrive(1,-10,-10,3);


    }

    public void Turn360Right(){
        setMotorsToEnc(29,29,2);
        newEncoderDrive(1,120,-120,8);
        stopMotors();
    }
    public void Turn360Left(){
        setMotorsToEnc(29,29,2);
        newEncoderDrive(1,-120,120,8);
        stopMotors();
    }

}

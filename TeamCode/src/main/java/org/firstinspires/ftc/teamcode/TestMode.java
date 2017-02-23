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
        setMotorsModeToEncDrive();
        stopMotors();
        encoderDrive(.7,100,100,3);
        sleep(5000);
        Turn360Right();
        sleep(5000);
        Turn360Left();
        sleep(5000);
        setMotorsModeToEncDrive();
        stopMotors();
        encoderDrive(.7,-100,-100,3);


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

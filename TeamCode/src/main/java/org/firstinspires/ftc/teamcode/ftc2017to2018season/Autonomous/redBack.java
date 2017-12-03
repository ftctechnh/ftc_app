package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name="Autonomous Red Test Back")
public class redBack extends Autonomous_General {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    public double rsBuffer = 20.00;


    @Override
    public void runOpMode() {


        vuforiaInit(true, true);
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();
        initiate();
        telemetry.addData("--->", "Gyro Calibrating");
        telemetry.update();
        gyro.calibrate();


        while(gyro.isCalibrating()){
            sleep(50);
            idle();

        }

        telemetry.addData("---->","Gyro Calibrated. Good to go...");
        telemetry.update();

        waitForStart();

        gyro.resetZAxisIntegrator();

        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();

        while(!vuMarkFound()){

        }
        //returnImage();
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();

        encoderMecanumDrive(0.4, -55, -55, 1000, 0);
        gyroTurn(0.3,180);
        sleep(250);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            simpleRangeDistance(140,0.2,rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            simpleRangeDistance(124,0.2,rsBuffer);

        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            simpleRangeDistance(158,0.2,rsBuffer);

        }

        gyroTurn(0.3,90);

        sleep(750);

        encoderMecanumDrive(0.65,45,45,1000,0);


    }


}

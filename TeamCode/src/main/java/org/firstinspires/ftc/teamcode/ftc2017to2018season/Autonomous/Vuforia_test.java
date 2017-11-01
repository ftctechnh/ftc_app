package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name="Autonomous Blue Test")
public class Vuforia_test extends Autonomous_General {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;


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

        encoderMecanumDrive(0.5,65,65,1000,0);
        sleep(1000);
        gyroTurn(0.3,89);
        sleep(1000);
        encoderMecanumDrive(0.75,45,45,1000,0);
        if (vuMark == RelicRecoveryVuMark.CENTER){
            strafeRangeDistance(88, 0.3);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            strafeRangeDistance(71, 0.8);

        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            strafeRangeDistance(108, 0.8);

        }


        sleep(1000);



    }


}

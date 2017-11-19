package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name="Autonomous Red Test Back")
public class red_BackCorner extends Autonomous_General {

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

        encoderMecanumDrive(0.6, -55, -55, 1000, 0);

        sleep(250);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            RangeDistance(177,0.3,rsBuffer, false, false);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            RangeDistance(156,0.3,rsBuffer, false, false);

        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            RangeDistance(187,0.3,rsBuffer, false, false);

        }


        sleep(750);

        encoderMecanumDrive(0.65,45,45,1000,0);


    }


}

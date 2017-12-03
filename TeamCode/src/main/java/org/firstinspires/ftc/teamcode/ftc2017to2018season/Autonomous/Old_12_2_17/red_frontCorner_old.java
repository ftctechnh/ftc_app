package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Old_12_2_17;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General;

//10-28-17
@Autonomous(name="Autonomous Red Test")
@Disabled

public class red_frontCorner_old extends Autonomous_General_old {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    public double rsBuffer = 20.00;


    @Override
    public void runOpMode() {

//Before start is pressed?
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
//reset the gyro sensor
        gyro.resetZAxisIntegrator();

        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();

        while(!vuMarkFound()){

        }
        //returnImage();
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();

        encoderMecanumDrive(0.5,-55,-55,5000,0);
        sleep(1000);
        gyroTurn(0.3,-88);
        sleep(1000);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            simpleRangeDistance(88, 0.2, rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            simpleRangeDistance(71, 0.2, rsBuffer);

        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            simpleRangeDistance(108, 0.2, rsBuffer);

        }


        sleep(1000);

        gyroTurn(0.3,-180);

        encoderMecanumDrive(0.65,45,45,1000,0);


    }


}

package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Old_12_2_17;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General;

//10-28-17
@Autonomous(name="Autonomous Blue Test Back")
@Disabled
public class blue_BackCorner extends Autonomous_General_old {

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
//reseting gyro sensor
        gyro.resetZAxisIntegrator();

        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();

        while(!vuMarkFound()){

        }
        jewelServo.setPosition(0);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        sleep(1000);
        readColor();
        //returnImage();
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();
        closeGlyphManipulator();
        sleep(1000);
        //moveUpGlyph(2.54);
        sleep(1000);

        if(ballColor.equals("red")){
            encoderMecanumDrive(0.9, -10,-10,5000,0);
            jewelServo.setPosition(1);
            sleep(1000);
            encoderMecanumDrive(0.9,25,25,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("blue")){
            encoderMecanumDrive(0.9,25,25,5000,0);
            jewelServo.setPosition(1);
            sleep(1000);
        }
        else{
            jewelServo.setPosition(1);
            sleep(1000);
            encoderMecanumDrive(0.9,25,25,5000,0);
        }

        //encoderMecanumDrive(0.4, 55, 55, 1000, 0);
        sleep(100);
        encoderMecanumDrive(0.9,10,10,5000,0);

        gyroTurn(0.3,0);

        sleep(250);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            simpleRangeDistance(115,2,rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            simpleRangeDistance(107,2,rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            simpleRangeDistance(128,2,rsBuffer);

        }

        gyroTurn(0.3,90);
        sleep(750);
        openGlyphManipulator();

        encoderMecanumDrive(0.65,55,55,1000,0);



    }


}

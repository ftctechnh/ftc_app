package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name="Autonomous Blue Test")
public class blue_frontCorner extends Autonomous_General {

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


        /*while(!vuMarkFound()){

        }
        //returnImage();
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();
        closeGlyphManipulator();*/

        jewelServo.setPosition(0);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        sleep(1000);
        readColor();
        if(ballColor.equals("blue")){
            encoderMecanumDrive(0.7, -10,-10,5000,0);
            jewelServo.setPosition(1);
            sleep(1000);
            encoderMecanumDrive(0.7,65,65,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("red")){
            encoderMecanumDrive(0.7,65,65,5000,0);
            jewelServo.setPosition(1);
            sleep(1000);
        }
        else{
            jewelServo.setPosition(1);
            sleep(1000);
            encoderMecanumDrive(0.7,65,65,5000,0);
        }
/*

        gyroTurn(0.3,-88);
        sleep(1000);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            simpleRangeDistance(64, 0.4, rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            simpleRangeDistance(46, 0.4, rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            simpleRangeDistance(82, 0.4, rsBuffer);

        }


        sleep(1000);

        gyroTurn(0.3,0);

        sleep(750);

        openGlyphManipulator();

        encoderMecanumDrive(0.65,45,45,1000,0);*/
    }


}

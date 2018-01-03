package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name="Autonomous Red Test Front Final")
public class redFront extends Autonomous_General {

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


        toggleLight(true);
        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();


        while(!vuMarkFound()){

        }
        toggleLight(false);
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();

        jewelServo.setPosition(0);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        sleep(1000);
        readColor();
        //returnImage();
        closeGlyphManipulator();
        sleep(1000);
        //moveUpGlyph(2.54);
        sleep(1000);

        if(ballColor.equals("red")){
            encoderMecanumDrive(0.9, 10,10,5000,0);
            jewelServo.setPosition(0.9);
            sleep(1000);
            encoderMecanumDrive(0.9,-65,-65,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("blue")){
            encoderMecanumDrive(0.9,-65,-65,5000,0);
            jewelServo.setPosition(0.9);
            sleep(1000);
        }
        else{
            jewelServo.setPosition(0.9);
            sleep(1000);
            encoderMecanumDrive(0.9,-65,-65,5000,0);
        }


        gyroTurn(0.3,-90);
        sleep(1000);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            simpleRangeDistance(59, 0.6, rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            simpleRangeDistance(76, 0.6, rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            simpleRangeDistance(42, 0.6, rsBuffer);

        }


        sleep(1000);

        gyroTurn(0.3,180);

        sleep(750);

        openGlyphManipulator();

        encoderMecanumDrive(0.3,30,30,1000,0);
    }


}

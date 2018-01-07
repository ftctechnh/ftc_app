package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name="Autonomous Red Test Back Final")
public class redBack extends Autonomous_General {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {



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

        sleep(500);

        vuforiaInit(true, true);
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();


        waitForStart();

        gyro.resetZAxisIntegrator();
        closeGlyphManipulator();
        toggleLight(true);
        light.setPower(1);
        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();

        double begintime= runtime.seconds();
        while(!vuMarkFound() && runtime.seconds() - begintime <= waitTime){


        }
        toggleLight(false);
        //returnImage();
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();
        jewelServo.setPosition(0);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        sleep(1000);
        readColor();
        light.setPower(0);
        //returnImage();

        sleep(1000);
        moveUpGlyph(2.54);
        sleep(1000);

        if(ballColor.equals("blue")){
            encoderMecanumDrive(0.9, 10,10,5000,0);
            jewelServo.setPosition(0.9);
            sleep(1000);
            encoderMecanumDrive(0.9,-65,-65,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("red")){
            encoderMecanumDrive(0.9,-55,-55,5000,0);
            jewelServo.setPosition(0.9);
            sleep(1000);
        }
        else{
            jewelServo.setPosition(0.9);
            sleep(1000);
            encoderMecanumDrive(0.9,-55,-55,5000,0);
        }

        encoderMecanumDrive(0.4, -55, -55, 1000, 0);
        gyroTurn(0.3,-180);
        sleep(250);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            simpleRangeDistance(112,0.2,rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            simpleRangeDistance(123,0.2,rsBuffer);

        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            simpleRangeDistance(104,0.2,rsBuffer);

        }

        gyroTurn(0.3,90);

        sleep(750);
        moveDownGlyph(1.54);
        sleep(500);
        openGlyphManipulator();
        sleep(500);

        encoderMecanumDrive(0.65,45,45,1000,0);


    }


}

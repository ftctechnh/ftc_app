package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name="Red Front George")
@Disabled
public class redFront_George extends Autonomous_General_George {

    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {


        vuforiaInit(true, true);
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();
        initiate(false);
//        telemetry.addData("--->", "Gyro Calibrating");
//        telemetry.update();
//        gyro.calibrate();


        telemetry.addData("---->","Good to go...");
        telemetry.update();

        waitForStart();

//        gyro.resetZAxisIntegrator();


        toggleLight(true);
        //light.setPower(1);
        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();

        double begintime= runtime.seconds();
        while(!vuMarkFound() && runtime.seconds() - begintime <= waitTime){


        }
        toggleLight(false);
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();
        sleep(250);

        moveUpGlyph(0.9);//change distances once we lower the stress of the glyph manipulator
        sleep(250);
        middleGlyphManipulator();
        sleep(250);
        moveDownGlyph(1.5);
        sleep(250);
        closeGlyphManipulator();
        sleep(250);
        moveUpGlyph(1.5);
        sleep(250);
        jewelServo.setPosition(0.9);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        sleep(1000);
        readColor();
        sleep(1500);
        //light.setPower(0);
        telemetry.addData("right jewel color", ballColor);
        telemetry.update();

        if(ballColor.equals("blue")){
            encoderMecanumDrive(0.9, 10,10,5000,0);
            jewelServo.setPosition(0);
            sleep(1000);
            encoderMecanumDrive(0.9,-35,-35,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("red")){
            encoderMecanumDrive(0.9,-25,-25,5000,0);
            jewelServo.setPosition(0);
            sleep(1000);
        }
        else{
            jewelServo.setPosition(0);
            sleep(1500);
            jewelServo.setPosition(0.9);
            sleep(500);
            readColor();
            sleep(1000);
            if(ballColor.equals("blue")){
                encoderMecanumDrive(0.9, 10,10,5000,0);
                jewelServo.setPosition(0);
                sleep(1000);
                encoderMecanumDrive(0.9,-35,-35,5000,0);
                sleep(1000);
            }
            else if(ballColor.equals("red")){
                encoderMecanumDrive(0.9,-25,-25,5000,0);
                jewelServo.setPosition(0);
                sleep(1000);
            }
            else {
                jewelServo.setPosition(0);
                sleep(1000);
                encoderMecanumDrive(0.9, -25, -25, 5000, 0);
            }
        }

        encoderMecanumDrive(0.9, -21, -21, 5000, 0);
        gyroTurnREV(0.9,-90);
        sleep(400);
        encoderMecanumDrive(0.4,-75,-75,5000,0);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            encoderMecanumDrive(0.9, 57, 57, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            encoderMecanumDrive(0.9, 37, 37, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            encoderMecanumDrive(0.9, 77, 77, 5000, 0);

        }

        else if (vuMark == RelicRecoveryVuMark.UNKNOWN){
            encoderMecanumDrive(0.9, 57, 57, 5000, 0);

        }


        sleep(1000);

        gyroTurnREV(0.3,180);
        sleep(250);

        encoderMecanumDrive(0.3,35,35,1000,0);
        sleep(500);
        encoderMecanumDrive(0.3, -5, -5, 1000, 0);
    }


}

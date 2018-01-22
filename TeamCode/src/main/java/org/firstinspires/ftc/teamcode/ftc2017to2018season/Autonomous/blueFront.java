package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(group = "Blue Front No Gyro")
public class blueFront extends Autonomous_General {

    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {


        vuforiaInit(true, true);
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();
        initiate(true);
        sleep(500);
        telemetry.addData("","GOOD TO GO! :)");
        telemetry.update();

        waitForStart();
//reseting gyro sensor

        //toggleLight(false);
        light.setPower(0.5);
        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();

        double begintime= runtime.seconds();
        while(!vuMarkFound() && runtime.seconds() - begintime <= waitTime){


        }
        //toggleLight(false);

        telemetry.addData("Vumark" , vuMark);
        telemetry.update();
        sleep(250);

        moveUpGlyph(0.5);
        sleep(250);
        middleGlyphManipulator();
        sleep(250);
        moveDownGlyph(0.9);
        sleep(200);
        closeGlyphManipulator();
        sleep(200);
        moveUpGlyph(0.7);
        jewelServo.setPosition(1);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        sleep(1000);
        readColor();
        sleep(1500);
        telemetry.addData("right jewel color", ballColor);
        telemetry.update();
        //returnImage();



        if(ballColor.equals("blue")){
            encoderMecanumDrive(0.9, -10,-10,5000,0);
            jewelServo.setPosition(0);
            sleep(1000);
            encoderMecanumDrive(0.9,35,35,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("red")){
            encoderMecanumDrive(0.9,25,25,5000,0);
            jewelServo.setPosition(0);
            sleep(1000);
        }
        else if (ballColor.equals("blank")){
            jewelServo.setPosition(0);
            sleep(1500);
            jewelServo.setPosition(1);
            sleep(500);
            readColor();
            sleep(1000);
            if(ballColor.equals("blue")){
                encoderMecanumDrive(0.9, -10,-10,5000,0);
                jewelServo.setPosition(0);
                sleep(1000);
                encoderMecanumDrive(0.9,35,35,5000,0);
                sleep(1000);
            }
            else if(ballColor.equals("red")){
                encoderMecanumDrive(0.9,25,25,5000,0);
                jewelServo.setPosition(0);
                sleep(1000);
            }
            else {
                jewelServo.setPosition(0);
                sleep(1000);
                encoderMecanumDrive(0.9, 25, 25, 5000, 0);
            }
        }

        light.setPower(0);

        //encoderMecanumDrive(0.4, 55, 55, 1000, 0);
        sleep(100);
        encoderMecanumDrive(0.3,15,15,5000,0);
        sleep(250);

        encoderTurn(-87, 0.5);
        sleep(1000);

        encoderMecanumDrive(0.9, -60, -60, 5000, 0);


        if (vuMark == RelicRecoveryVuMark.CENTER){
            encoderMecanumDrive(0.9, 68, 68, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            encoderMecanumDrive(0.9, 83, 83, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            encoderMecanumDrive(0.9, 103, 103, 5000, 0);

        }

        else if (vuMark == RelicRecoveryVuMark.UNKNOWN){
            encoderMecanumDrive(0.9, 72, 72, 5000, 0);

        }


        sleep(1000);

        encoderTurn(90, 0.5);

        sleep(750);

        moveDownGlyph(0.5);
        sleep(250);
        openGlyphManipulator();
        sleep(250);

        encoderMecanumDrive(0.3,35,35,1000,0);
        sleep(500);
        encoderMecanumDrive(0.3, -5, -5, 1000, 0);
    }


}

package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name = "wall align test")
//@Disabled
public class wallAlignTest extends Autonomous_General_George {

    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {



//        vuforiaInit(true, true);
//        telemetry.addData("","Vuforia Initiated");
//        telemetry.update();
        initiate(false);
//        sleep(500);
        telemetry.addData("","GOOD TO GO! :)");
        telemetry.update();
//
        waitForStart();
////reseting gyro sensor
//
        wallAlign(0.5, 30, 1);
//        toggleLight(true);
//        //light.setPower(0.5);
//        startTracking();
//        telemetry.addData("","READY TO TRACK");
//        telemetry.update();
//
//        double begintime= runtime.seconds();
//        while(!vuMarkFound() && runtime.seconds() - begintime <= waitTime){
//
//
//        }
//        toggleLight(false);
//
//        telemetry.addData("Vumark" , vuMark);
//        telemetry.update();
//        sleep(250);
//
//        moveUpGlyph(0.7);//change distances once we lower the stress of the glyph manipulator
//        sleep(250);
//        middleGlyphManipulator();
//        sleep(250);
//        moveDownGlyph(1.45);
//        sleep(250);
//        closeGlyphManipulator();
//        sleep(250);
//        moveUpGlyph(1.25);
//        sleep(250);
//        jewelServo.setPosition(1);
//        telemetry.addData("jewelServo Position", jewelServo.getPosition());
//        telemetry.update();
//        sleep(1000);
//        readColorRev();
//        sleep(1500);
//        //light.setPower(0);
//        telemetry.addData("right jewel color", ballColor);
//        telemetry.update();
//        //returnImage();
//
//
//
//        if(ballColor.equals("blue")){
//            encoderMecanumDrive(0.6, -10,-10,5000,0);
//            jewelServo.setPosition(0);
//            sleep(1000);
//            encoderMecanumDrive(0.6,46,46,5000,0);
//            sleep(1000);
//        }
//        else if(ballColor.equals("red")){
//            encoderMecanumDrive(0.6,40,40,5000,0);
//            jewelServo.setPosition(0);
//            sleep(1000);
//        }
//        else if (ballColor.equals("blank")){
//            jewelServo.setPosition(0);
//            sleep(1500);
//            jewelServo.setPosition(0.6);
//            sleep(500);
//            readColorRev();
//            sleep(1000);
//            if(ballColor.equals("blue")){
//                encoderMecanumDrive(0.6, -10,-10,5000,0);
//                jewelServo.setPosition(0);
//                sleep(1000);
//                encoderMecanumDrive(0.6,46,46,5000,0);
//                sleep(1000);
//            }
//            else if(ballColor.equals("red")){
//                encoderMecanumDrive(0.6,40,40,5000,0);
//                jewelServo.setPosition(0);
//                sleep(1000);
//            }
//            else {
//                jewelServo.setPosition(0);
//                sleep(1000);
//                encoderMecanumDrive(0.6, 40, 40, 5000, 0);
//            }
//        }
//
//        sleep(75);
//        jewelServo.setPosition(0.1);
//        sleep(100);
//        gyroTurnREV(0.4,0);
//        sleep(100);
//        wallAlign(0.5,25, 1);
//        //the last value is 1 for the blue auto and 2 for the red auto
//        sleep(200);
//        gyroTurnREV(0.5, -85);
//        sleep(100);
//
//        encoderMecanumDrive(0.3, -20, -20, 5000, 0);
//
//
//        if (vuMark == RelicRecoveryVuMark.LEFT){
//            encoderMecanumDrive(0.5, 19, 19, 5000, 0);
//        }
//        else if (vuMark == RelicRecoveryVuMark.CENTER){
//            encoderMecanumDrive(0.5, 36, 36, 5000, 0);
//        }
//        else if (vuMark == RelicRecoveryVuMark.RIGHT){
//            encoderMecanumDrive(0.5, 51, 51, 5000, 0);
//
//        }
//
//        else if (vuMark == RelicRecoveryVuMark.UNKNOWN){
//            encoderMecanumDrive(0.5, 36, 36, 5000, 0);
//
//        }
//
//        columnAlign();
//
//        sleep(100);
//
//        gyroTurnREV(0.5, 1);
//
//        sleep(750);
//
//        moveDownGlyph(0.5);
//        sleep(100);
//        encoderMecanumDrive(0.3, 5, 5, 1000, 0);
//        sleep(250);
//        openGlyphManipulator();
//        sleep(250);
//
//        encoderMecanumDrive(0.3,35,35,1000,0);
//        sleep(250);
//        encoderMecanumDrive(0.3, -20, -20, 1000, 0);
//        sleep(100);
//        gyroTurnREV(0.3, 179);
//        sleep(100);
//    }


    }
}

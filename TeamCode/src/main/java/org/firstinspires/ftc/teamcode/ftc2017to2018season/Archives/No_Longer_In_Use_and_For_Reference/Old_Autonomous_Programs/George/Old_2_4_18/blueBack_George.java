package org.firstinspires.ftc.teamcode.ftc2017to2018season.Archives.No_Longer_In_Use_and_For_Reference.Old_Autonomous_Programs.George.Old_2_4_18;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name="Blue Back George")
@Disabled
public class blueBack_George extends Autonomous_General_George {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {


        vuforiaInit(true, true);
        telemetry.addData("", "Vuforia Initiated");
        telemetry.update();
        initiate(false);
//        telemetry.addData("--->", "Gyro Calibrating");
//        telemetry.update();
        //gyro.calibrate();


//        while(gyro.isCalibrating()){
//            sleep(50);
//            idle();
//
//        }

        telemetry.addData("---->", "Gyro Calibrated. Good to go...");
        telemetry.update();

        waitForStart();
//reseting gyro sensor
//        gyro.resetZAxisIntegrator();

        toggleLight(true);
        //light.setPower(1);
        startTracking();
        telemetry.addData("", "READY TO TRACK");
        telemetry.update();

        double begintime = runtime.seconds();
        while (!vuMarkFound() && runtime.seconds() - begintime <= waitTime) {


        }
        toggleLight(false);

        telemetry.addData("Vumark", vuMark);
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
        //returnImage();


        if (ballColor.equals("blue")) {
            encoderMecanumDrive(0.9, -10, -10, 5000, 0);
            jewelServo.setPosition(0);
            sleep(1000);
            encoderMecanumDrive(0.9, 20, 20, 5000, 0);
            sleep(1000);
        } else if (ballColor.equals("red")) {
            encoderMecanumDrive(0.9, 10, 10, 5000, 0);
            jewelServo.setPosition(0);
            sleep(1000);
        } else if (ballColor.equals("blank")) { //this means the color sensor didn't see the color of the jewel
            jewelServo.setPosition(0);//raising the jewel manipulator
            sleep(1500);
            jewelServo.setPosition(0.9);//putting the jewel manipulator back down
            sleep(500);
            readColor();
            sleep(1000);
            if (ballColor.equals("blue")) {
                encoderMecanumDrive(0.9, -10, -10, 5000, 0);
                jewelServo.setPosition(0);
                sleep(1000);
                encoderMecanumDrive(0.9, 20, 20, 5000, 0);
                sleep(1000);
            } else if (ballColor.equals("red")) {
                encoderMecanumDrive(0.9, 10, 10, 5000, 0);
                jewelServo.setPosition(0);
                sleep(1000);
            } else {//if the jewel manipulator doesn't see it a second time
                jewelServo.setPosition(0);
                sleep(1000);
                encoderMecanumDrive(0.9, 10, 10, 5000, 0);
            }
        }
        //encoderMecanumDrive(0.4, 15, 15, 1000, 0);
        gyroTurnREV(0.8, 0);
        //encoderMecanumDrive(0.4, 55, 55, 1000, 0);
        sleep(100);
        //encoderMecanumDrive(0.3, 26, 25, 5000, 0);


        //if the range sensor gives a value less than 90 or greater than 200, it is most likely wrong
        if (true) {
            telemetry.addData("", "rangeSensor malfunctioned");
            telemetry.update();
            sleep(250);
            //robot should end up 100 cm away from the wall, based on this and experiments
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                //this method uses encoders to drive a specified distance
                encoderMecanumDrive(0.7, 20, 20, 500, 0);
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                encoderMecanumDrive(0.7, 2, 2, 500, 0);
            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                encoderMecanumDrive(0.7, 40, 40, 500, 0);
            } else {
                encoderMecanumDrive(0.7, 20, 20, 500, 0);
            }
//        }else {
//            //enter this section of code if the range sensor is working
//            gyroTurnREV(0.3, 0);
//
//            sleep(250);
//
//            if (vuMark == RelicRecoveryVuMark.CENTER) {
//                //this method uses the range sensors to end a certain distance away from the wall
//                simpleRangeDistance(87, 0.35, rsBuffer);
//            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
//                simpleRangeDistance(79, 0.35, rsBuffer);
//            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
//                simpleRangeDistance(98, 0.35, rsBuffer);
//            }
//            else{
//                simpleRangeDistance(87,0.35,rsBuffer);
//            }
//        }

            sleep(1000);
            gyroTurnREV(0.5, 90);
            sleep(750);
            moveDownGlyph(0.3);
            sleep(500);
            openGlyphManipulator();

            encoderMecanumDrive(0.3, 35, 35, 1000, 0);
            sleep(250);
            encoderMecanumDrive(0.65,-5,-5,1000,0);
        }


    }
}

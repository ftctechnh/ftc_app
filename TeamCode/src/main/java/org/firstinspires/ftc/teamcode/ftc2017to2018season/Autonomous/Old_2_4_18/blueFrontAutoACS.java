package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Old_2_4_18;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George;

//10-28-17
@Autonomous(name = "Blue Front George ACS HC")
//@Disabled
public class blueFrontAutoACS extends Autonomous_General_George {

    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {


        vuforiaInit(true, true);
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();
        initiate(false);
        sleep(500);
        telemetry.addData("","GOOD TO GO! :)");
        telemetry.update();

        waitForStart();
//reseting gyro sensor

        toggleLight(true);
        //light.setPower(0.5);
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

        moveUpGlyph(0.7);//change distances once we lower the stress of the glyph manipulator
        sleep(250);
        middleGlyphManipulator();
        sleep(250);
        moveDownGlyph(1.1);
        sleep(250);
        closeGlyphManipulator();
        sleep(250);
        moveUpGlyph(1.25);
        sleep(250);
        jewelServo.setPosition(1);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        sleep(1000);
        readColorRev();
        sleep(1500);
        //light.setPower(0);
        telemetry.addData("right jewel color", ballColor);
        telemetry.update();
        //returnImage();



        if(ballColor.equals("blue")){
            encoderMecanumDrive(0.4, -10,-10,5000,0);
            jewelServo.setPosition(0);
            sleep(1000);
            encoderMecanumDrive(0.4,36,37,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("red")){
            encoderMecanumDrive(0.6,45,47,5000,0);
            jewelServo.setPosition(0);
            sleep(1000);
        }
        else if (ballColor.equals("blank")){
            jewelServo.setPosition(0);
            sleep(1500);
            jewelServo.setPosition(0.6);
            sleep(500);
            readColorRev();
            sleep(1000);
            if(ballColor.equals("blue")){
                encoderMecanumDrive(0.4, -10,-10,5000,0);
                jewelServo.setPosition(0);
                sleep(1000);
                encoderMecanumDrive(0.4,45,47,5000,0);
                sleep(1000);
            }
            else if(ballColor.equals("red")){
                encoderMecanumDrive(0.6,40,40,5000,0);
                jewelServo.setPosition(0);
                sleep(1000);
            }
            else {
                jewelServo.setPosition(0);
                sleep(1000);
                encoderMecanumDrive(0.6, 40, 40, 5000, 0);
            }
        }

        sleep(75);
        jewelServo.setPosition(0.1);
        sleep(100);
        gyroTurnREV(0.4,0);
        sleep(100);
        wallAlign(0.5,32, 0);
        //the last value is 1 for the blue auto and 2 for the red auto
        sleep(200);
        gyroTurnREV(0.5, -86);
        sleep(100);

        encoderMecanumDrive(0.3, -25, -25, 5000, 0);


        if (vuMark == RelicRecoveryVuMark.LEFT){
            encoderMecanumDrive(0.5, 16, 16, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER){
            encoderMecanumDrive(0.5, 33, 33, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            encoderMecanumDrive(0.5, 48, 48, 5000, 0);

        }

        else if (vuMark == RelicRecoveryVuMark.UNKNOWN){
            encoderMecanumDrive(0.5, 33, 33, 5000, 0);

        }

        columnAlign();

        sleep(100);

        gyroTurnREV(0.5, 0);

        sleep(750);

        moveDownGlyph(0.5);
        sleep(100);
        encoderMecanumDrive(0.3, 5, 5, 1000, 0);
        sleep(250);
        openGlyphManipulator();
        sleep(250);

        encoderMecanumDrive(0.3,35,35,1000,0);
        sleep(250);
        encoderMecanumDrive(0.3, -7, -7, 1000, 0);
    }


}

package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name = "Blue Front George HIGH CONTROL")
//@Disabled
public class blueFront_George extends Autonomous_General_George {

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
        moveDownGlyph(1.45);
        sleep(250);
        closeGlyphManipulator();
        sleep(250);
        moveUpGlyph(1.45);
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
            encoderMecanumDrive(0.6, -10,-10,5000,0);
            jewelServo.setPosition(0);
            sleep(1000);
            encoderMecanumDrive(0.6,46,46,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("red")){
            encoderMecanumDrive(0.6,40,40,5000,0);
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
                encoderMecanumDrive(0.6, -10,-10,5000,0);
                jewelServo.setPosition(0);
                sleep(1000);
                encoderMecanumDrive(0.6,46,46,5000,0);
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

        sleep(100);
        gyroTurnREV(0.4,0);
        sleep(100);
        wallAlign(0.5,28, 0);//since the columns of the cryptobox are protruding,
                                                    // the range sensor is actually using the distance from the protruding columns
                                                    //the last value is 0 for the blue auto and 1 for the red auto
        sleep(200);
        gyroTurnREV(0.5, -84);
        sleep(100);



        if (vuMark == RelicRecoveryVuMark.LEFT){//should be 20 cm away from wall for left
            wallAlign(0.4, 20, 1);
            //encoderMecanumDrive(0.5, 16, 16, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER){
            wallAlign(0.4, 20, 1);
            //encoderMecanumDrive(0.5, 33, 33, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            wallAlign(0.4, 40, 1);
            //encoderMecanumDrive(0.5, 48, 48, 5000, 0);

        }

        else if (vuMark == RelicRecoveryVuMark.UNKNOWN){
            wallAlign(0.4, 20, 1);
            //encoderMecanumDrive(0.5, 33, 33, 5000, 0);

        }

        //columnAlign();

        sleep(100);

        gyroTurnREV(0.5, -45);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall

        sleep(750);

        moveDownGlyph(1.05);
        sleep(100);
        /*encoderMecanumDrive(0.3, 5, 5, 1000, 0);
        sleep(250);*/
        openGlyphManipulator();
        sleep(250);

        encoderMecanumDrive(0.3,35,35,1000,0);
        sleep(250);
        encoderMecanumDrive(0.3, -10, -10, 1000, 0);
        /*sleep(100);
        gyroTurnREV(0.3, 179);
        sleep(100);*/
    }


}

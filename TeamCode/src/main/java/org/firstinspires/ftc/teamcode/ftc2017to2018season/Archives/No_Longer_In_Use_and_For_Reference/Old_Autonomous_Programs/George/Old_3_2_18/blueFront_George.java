package org.firstinspires.ftc.teamcode.ftc2017to2018season.Archives.No_Longer_In_Use_and_For_Reference.Old_Autonomous_Programs.George.Old_3_2_18;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
//import org.firstinspires.ftc.teamcode.ftc2017to2018season.Archives.No_Longer_In_Use_and_For_Reference.Old_Autonomous_Programs.George.Old_3_25_18.Autonomous_General_George;

//10-28-17
@Autonomous(name = "Blue Front George Super-Regionals")
@Disabled
public class blueFront_George extends Autonomous_General_George_old {

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
        jewelServo.setPosition(0.2);
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
            //move the jewel manipulator to the right to knock off the ball
            jewelServoRotate.setPosition(0.5);
            sleep(300);
            jewelServo.setPosition(0.8);
            sleep(750);
            //move it back to the original posititon
            jewelServoRotate.setPosition(0.79);
            //Add code to swing the jwele arm
        }
        else if(ballColor.equals("red")){
            //move the jewel manipulator to the left to knock off the ball
            jewelServoRotate.setPosition(1);
            sleep(300);
            jewelServo.setPosition(0.8);
            sleep(750);
            //move the jewel manipulator to the original position
            jewelServoRotate.setPosition(0.79);
            sleep(1000);
        }
        else if (ballColor.equals("blank")){
            jewelServo.setPosition(1);
            sleep(1500);
            jewelServo.setPosition(0.2);
            sleep(500);
            readColorRev();
            sleep(1000);
            if(ballColor.equals("blue")){
                //move the jewel manipulator to the right to knock off the ball
                jewelServoRotate.setPosition(0.5);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move it back to the original posititon
                jewelServoRotate.setPosition(0.79);
                //Add code to swing the jwele arm
            }
            else if(ballColor.equals("red")){
                //move the jewel manipulator to the left to knock off the ball
                jewelServoRotate.setPosition(1);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move the jewel manipulator to the original position
                jewelServoRotate.setPosition(0.79);
                sleep(1000);
            }
            else {
                jewelServo.setPosition(1);
                sleep(1000);
            }
        }
        encoderMecanumDrive(0.6,40,40,5000,0);
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
            encoderMecanumDrive(0.3, -12, -12, 5000, 0);
            sleep(100);

            gyroTurnREV(0.5, -45);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER){
            wallAlign(0.4, 20, 1);
            //encoderMecanumDrive(0.5, 33, 33, 5000, 0);
            sleep(100);

            gyroTurnREV(0.5, -45);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            //wallAlign(0.4, 48, 1);
            encoderMecanumDrive(0.5, 6, 6, 5000, 0);
            sleep(100);

            gyroTurnREV(0.5, -53.5);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall

        }

        else if (vuMark == RelicRecoveryVuMark.UNKNOWN){
            wallAlign(0.4, 20, 1);
            //encoderMecanumDrive(0.5, 33, 33, 5000, 0);
            sleep(100);

            gyroTurnREV(0.5, -45);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall

        }

        //columnAlign();


        sleep(750);

        moveDownGlyph(1.05);
        sleep(100);
        /*encoderMecanumDrive(0.3, 5, 5, 1000, 0);
        sleep(250);*/
        openGlyphManipulator();
        sleep(250);

        encoderMecanumDrive(0.3,33,33,1000,0);
        sleep(250);
        encoderMecanumDrive(0.3,15,-15,1000,0);
        sleep(500);
        encoderMecanumDrive(0.3, -10, -10, 1000, 0);
        /*sleep(100);
        gyroTurnREV(0.3, 179);
        sleep(100);*/
    }


}

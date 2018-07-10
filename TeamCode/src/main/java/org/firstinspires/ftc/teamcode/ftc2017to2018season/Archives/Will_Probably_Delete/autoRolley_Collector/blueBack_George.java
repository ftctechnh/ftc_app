package org.firstinspires.ftc.teamcode.ftc2017to2018season.Archives.Will_Probably_Delete.autoRolley_Collector;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name = "Blue Back George Test Rolley")
@Disabled
public class blueBack_George extends Autonomous_General_Rolley {

    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {


        vuforiaInit(true, true);
        //intiates the vuforia sdk and camera
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();
        //tell driver that vuforia is ready
        initiate(false);
        //intiate hardware
        sleep(500);
        telemetry.addData("","GOOD TO GO! :)");
        telemetry.update();
        //tell driver that we are good to go

        waitForStart();
//reseting gyro sensor

        jewelServoRotate.setPosition(0.74);
        sleep(100);
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
        sleep(1000);
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
            else if(ballColor.equals("red")) {
                //move the jewel manipulator to the left to knock off the ball
                jewelServoRotate.setPosition(1);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move the jewel manipulator to the original position
                jewelServoRotate.setPosition(0.79);
                sleep(1000);
            }
        }
        sleep(1000);
        encoderMecanumDrive(0.6,50,50,5000,0);
        sleep(100);
        gyroTurnREV(0.4,0);
        sleep(100);




        if (vuMark == RelicRecoveryVuMark.LEFT){
            encoderMecanumDrive(0.4,4.25,4.25,5000,0);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN){
            encoderMecanumDrive(0.4,-4,-4,5000,0);

        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            encoderMecanumDrive(0.4,9.25,9.25,5000,0);
        }


        sleep(100);

        if (vuMark == RelicRecoveryVuMark.LEFT){
            gyroTurnREV(0.5, 102);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall

        }
        else {
            gyroTurnREV(0.5, 60);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall
        }


        sleep(750);

        moveDownGlyph(1.05);
        sleep(100);
        /*encoderMecanumDrive(0.3, 5, 5, 1000, 0);
        sleep(250);*/
        openGlyphManipulator();
        sleep(250);

        encoderMecanumDrive(0.3,35,35,1000,0);
        sleep(250);

        if (vuMark == RelicRecoveryVuMark.LEFT){
            encoderMecanumDrive(0.3,-15,15,1000,0);

        }
        else {
            encoderMecanumDrive(0.3,15,-15,1000,0);
        }

        sleep(500);
        encoderMecanumDrive(0.3, -10, -10, 1000, 0);
    }


}

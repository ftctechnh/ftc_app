package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George;

//10-28-17
@Autonomous(name="Red Back George Super-Regionals")
//3/2/18 edit by Steven Chen: getting rid of unnecessary turns (this is new version of the regional code)
public class redBack_George extends Autonomous_General_George {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        //enable vuforia and load its sdk and camera
        vuforiaInit(true, true);
        //tell driver that it loaded
        telemetry.addData("", "Vuforia Initiated");
        telemetry.update();
        //ititiate the hardware for rev robotics
        initiate(false);
        //tell robot that robot is ready to go
        telemetry.addData("The robot is loaded","You are ready to go! :D");
        telemetry.update();
        //wait for driver to click play
        waitForStart();
        jewelServoRotate.setPosition(0.74);
        sleep(100);
        //turn on the phone flashlight
        toggleLight(true);
        //start looking for the image and tell the driver that the phone is tracking the image
        startTracking();
        telemetry.addData("", "READY TO TRACK");
        telemetry.update();

        //FAIL-OVER for the vuforia image; if not found within waitTime (5 sec.) default to center column
        double begintime = runtime.seconds();
        while (!vuMarkFound() && runtime.seconds() - begintime <= waitTime) {


        }
        //turn off the phone light
        toggleLight(false);

        //tell the driver the column
        telemetry.addData("Vumark", vuMark);
        telemetry.update();
        sleep(250);

        //the next functions are used to grab the block
        //we move up the manipulator a few cm
        moveUpGlyph(0.9);
        sleep(250);
        //we move the servos to the middle position
        middleGlyphManipulator();
        sleep(250);
        //move the glyph servos to middle position
        moveDownGlyph(1.5);
        //move down the glyph manipulator
        sleep(250);
        closeGlyphManipulator();
        //grab the block
        sleep(250);
        moveUpGlyph(1.5);
        //move the block up
        sleep(250);
        jewelServo.setPosition(0.2);
        //move down the jewel arm
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        //read the color of the ball to the right of the arm
        readColorRev();
        sleep(100);
        //tell the driver what the color of the ball is
        telemetry.addData("right jewel color", ballColor);
        telemetry.update();




        if(ballColor.equals("red")){
            //move the jewel manipulator to the right to knock off the ball
            jewelServoRotate.setPosition(0.5);
            sleep(300);
            jewelServo.setPosition(0.8);
            sleep(750);
            //move it back to the original posititon
            jewelServoRotate.setPosition(0.79);

        }
        else if(ballColor.equals("blue")){
            //move the jewel manipulator to the left to knock off the ball
            jewelServoRotate.setPosition(1);
            sleep(300);
            jewelServo.setPosition(0.8);
            sleep(750);
            //move the jewel manipulator to the original position
            jewelServoRotate.setPosition(0.79);
            sleep(500);
        }
        //redo what was done before in the case that the ball was unable to be read
        else if (ballColor.equals("blank")){
            jewelServo.setPosition(1);
            sleep(1500);
            jewelServo.setPosition(0.2);
            sleep(500);
            readColorRev();
            sleep(1000);
            if(ballColor.equals("red")){
                jewelServoRotate.setPosition(0.5);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                jewelServoRotate.setPosition(0.79);
                sleep(500);
            }
            else if(ballColor.equals("blue")) {
                jewelServoRotate.setPosition(1);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                jewelServoRotate.setPosition(0.79);
                sleep(500);
            }
        }
        //move the jewel servi back up

        jewelServo.setPosition(1);
        sleep(100);
        //drive off the plate (we drive backwards since robot was backwards)
        encoderMecanumDrive(0.3, -45,-45,5000,0);
        sleep(100);

        //gyroTurnREV(0.5, -180);


        if (vuMark == RelicRecoveryVuMark.RIGHT){
            //if the right image was read we move back 4.25 cm
            encoderMecanumDrive(0.4,-11,-11,5000,0);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN){
            //if the center or unkown image was read we move forward 4 cm
            encoderMecanumDrive(0.4,-2,-2,5000,0);

        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            //if the left image was read we move back 9.25 cm
            encoderMecanumDrive(0.4,-9.5,-9.5,5000,0);
        }


        sleep(100);

        if (vuMark == RelicRecoveryVuMark.RIGHT){
            //if the image was right we turn to 60º counterclockwise from origin angle to push block in angled
            gyroTurnREV(0.5, 65);

        }
        else {
            //if the image was right we turn to 102º counterclockwise from origin angle to push block in angled
            gyroTurnREV(0.5, 117);
        }


        sleep(100);

        //we move the glyph manipulator down
        moveDownGlyph(1.05);
        sleep(100);
        //we let go of the block
        openGlyphManipulator();
        sleep(250);

        //we drive forward 35 cm to push the block in
        encoderMecanumDrive(0.3,16,16,1000,0);
        sleep(250);

        if (vuMark == RelicRecoveryVuMark.RIGHT){
            //to push the block in more, we turn left while pushing in forward
            encoderMecanumDrive(0.3,10,-10,1000,0);

        }
        else {
            //to push the block in more, we turn right while pushing in forward
            encoderMecanumDrive(0.3,-10,10,1000,0);
        }

        sleep(100);
        //we back up 10 cm to park

        //code to get second glyph
        encoderMecanumDrive(0.3, -20, -20, 1000, 0);
        gyroTurnREV(0.5,-90);
        encoderMecanumDrive(0.6,20,20,5000,0);
        closeGlyphManipulator();
        moveUpGlyph(3.4);
        gyroTurnREV(0.5,90);
        moveDownGlyph(2);
        openGlyphManipulator();
        encoderMecanumDrive(0.5,35,35,5000,0);
        encoderMecanumDrive(0.3,10,10,5000,0);


    }
}

//package org.firstinspires.ftc.teamcode.ftc2017to2018season.Archives.No_Longer_In_Use_For_Reference.Old_Autonomous_Programs.George.Old_3_9_18;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
//
////10-28-17
//@Autonomous(name = "Blue Front George Super-Regionals_Old")
////@Disabled
//public class blueFront_George_Old extends Autonomous_General_George {
//
//    public double rsBuffer = 20.00;
//    private ElapsedTime runtime = new ElapsedTime();
//
//
//    @Override
//    public void runOpMode() {
//
//
//        vuforiaInit(true, true);
//        telemetry.addData("","Vuforia Initiated");
//        telemetry.update();
//        initiate(false);
//        sleep(500);
//        telemetry.addData("","GOOD TO GO! :)");
//        telemetry.update();
//
//        waitForStart();
////reseting gyro sensor
//        jewelServoRotate.setPosition(0.74);
//        sleep(100);
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
//        moveUpGlyph(1.45);
//        sleep(250);
//        jewelServo.setPosition(0.2);
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
//            //move the jewel manipulator to the right to knock off the ball
//            jewelServoRotate.setPosition(0.5);
//            sleep(300);
//            jewelServo.setPosition(0.8);
//            sleep(750);
//            //move it back to the original posititon
//            jewelServoRotate.setPosition(0.79);
//            //Add code to swing the jwele arm
//        }
//        else if(ballColor.equals("red")){
//            //move the jewel manipulator to the left to knock off the ball
//            jewelServoRotate.setPosition(1);
//            sleep(300);
//            jewelServo.setPosition(0.8);
//            sleep(750);
//            //move the jewel manipulator to the original position
//            jewelServoRotate.setPosition(0.79);
//            sleep(1000);
//        }
//        else if (ballColor.equals("blank")){
//            jewelServo.setPosition(1);
//            sleep(1500);
//            jewelServo.setPosition(0.2);
//            sleep(500);
//            readColorRev();
//            sleep(1000);
//            if(ballColor.equals("blue")){
//                //move the jewel manipulator to the right to knock off the ball
//                jewelServoRotate.setPosition(0.5);
//                sleep(300);
//                jewelServo.setPosition(0.8);
//                sleep(750);
//                //move it back to the original posititon
//                jewelServoRotate.setPosition(0.79);
//                //Add code to swing the jwele arm
//            }
//            else if(ballColor.equals("red")){
//                //move the jewel manipulator to the left to knock off the ball
//                jewelServoRotate.setPosition(1);
//                sleep(300);
//                jewelServo.setPosition(0.8);
//                sleep(750);
//                //move the jewel manipulator to the original position
//                jewelServoRotate.setPosition(0.79);
//                sleep(1000);
//            }
//            else {
//                jewelServo.setPosition(1);
//                sleep(1000);
//            }
//        }
//
//        jewelServo.setPosition(1);
//        sleep(200);
//
//        encoderMecanumDrive(0.4,40,40,5000,0);
//        sleep(100);
//        gyroTurnREV(0.4,0);
//        sleep(100);
//        wallAlign(0.5,28, 0);//since the columns of the cryptobox are protruding,
//                                                    // the range sensor is actually using the distance from the protruding columns
//                                                    //the last value is 0 for the blue auto and 1 for the red auto
//        sleep(200);
//        gyroTurnREV(0.5, -84);
//        sleep(100);
//
//
//
//        if (vuMark == RelicRecoveryVuMark.LEFT){//should be 20 cm away from wall for left
//            //goes to given distance away from the wall
//            //wallAlign(0.3, 35, 1);
//            encoderMecanumDrive(0.3, -14, -14, 5000, 0);
//        }
//        else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN){
//            encoderMecanumDrive(0.3,-5,-5,5000,0);
//            //wallAlign(0.4, 35, 1);
//            //encoderMecanumDrive(0.5, 33, 33, 5000, 0);
//        }
//        else if(vuMark == RelicRecoveryVuMark.RIGHT){
//            encoderMecanumDrive(0.3,3,3,5000,0);
//            //wallAlign(0.4, 50, 1);
//            //encoderMecanumDrive(0.5, 48, 48, 5000, 0);
//
//        }
//        //if we didn't detect the image, automatically put the glyph in the center
//
//        gyroTurnREV(0.5,-45);
//
//        sleep(750);
//
//        moveDownGlyph(1.05);
//        sleep(100);
//        /*encoderMecanumDrive(0.3, 5, 5, 1000, 0);
//        sleep(250);*/
//        openGlyphManipulator();
//        sleep(250);
//
//        encoderMecanumDrive(0.3,20,20,1000,0);
//        sleep(250);
//        encoderMecanumDrive(0.3,10,-10,1000,0);
//        sleep(500);
//        encoderMecanumDrive(0.3, -10, -10, 1000, 0);
//
//        if(vuMark == RelicRecoveryVuMark.LEFT){
//            encoderMecanumDrive(0.3, 10, 10, 1000, 1);
//            encoderMecanumDrive(0.3,2,2,1000,0);
//        }
//        /*sleep(100);
//        gyroTurnREV(0.3, 179);
//        sleep(100);*/
//    }
//
//
//}

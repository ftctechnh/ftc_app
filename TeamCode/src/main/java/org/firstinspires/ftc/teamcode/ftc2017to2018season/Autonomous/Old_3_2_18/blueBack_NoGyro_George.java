package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Old_3_2_18;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George;

//10-28-17
@Autonomous(name="Blue Back No Gyro George")
@Disabled
public class blueBack_NoGyro_George extends Autonomous_General_George {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
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

        //toggleLight(true);
        //light.setPower(1);
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
        //light.setPower(0);
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

        //encoderMecanumDrive(0.4, 55, 55, 1000, 0);
        sleep(100);
        encoderMecanumDrive(0.3,26,25,5000,0);


//        if(rangeSensor.getDistance(DistanceUnit.CM)< 90 || rangeSensor.getDistance(DistanceUnit.CM)> 200){
            telemetry.addData("", "rangeSensor malfunctioned");
            telemetry.update();
            sleep(250);
            //robot should end up 100 cm away from the wall
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                encoderMecanumDrive(0.7,30,30,500,0);
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                encoderMecanumDrive(0.7,10,10,500,0);
            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                encoderMecanumDrive(0.7,50,50,500,0);
            }
            else{
                encoderMecanumDrive(0.7,30,30,500,0);
            }
//        }
//        else {
//            telemetry.addData("", "Doing Simple Range Distance");
//            telemetry.update();
//            sleep(250);
//
//            if (vuMark == RelicRecoveryVuMark.CENTER) {
//                simpleRangeDistance(112, 0.35, rsBuffer);
//            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
//                simpleRangeDistance(104, 0.35, rsBuffer);
//            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
//                simpleRangeDistance(123, 0.35, rsBuffer);
//
//            }
//            else{
//                simpleRangeDistance(112,0.35,rsBuffer);
//            }
//        }


        encoderTurn(95, 0.3);
        sleep(750);
        moveDownGlyph(0.4);
        sleep(500);
        openGlyphManipulator();

        encoderMecanumDrive(0.65,55,55,1000,0);
        sleep(500);
        encoderMecanumDrive(0.3, -5, -5, 1000, 0);


        }


}

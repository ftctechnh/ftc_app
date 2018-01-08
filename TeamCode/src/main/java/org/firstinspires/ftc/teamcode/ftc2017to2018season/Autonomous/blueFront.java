package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Final.Autonomous_General_final;

import static org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral.runtime;

//10-28-17
@Autonomous(group = "Blue Front")
public class blueFront extends Autonomous_General {

    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {


        initiate();
        sleep(100);

        vuforiaInit(true, true);
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();

        telemetry.addData("--->", "Gyro Calibrating");
        telemetry.update();
        gyro.calibrate();

        while(gyro.isCalibrating()){
            sleep(50);
            idle();

        }

        telemetry.addData("---->","Gyro Calibrated. Good to go...");
        telemetry.update();

        waitForStart();

        gyro.resetZAxisIntegrator();


        toggleLight(false);
        light.setPower(1);
        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();

        double begintime= runtime.seconds();
        while(!vuMarkFound() && runtime.seconds() - begintime <= waitTime){


        }
        toggleLight(false);

        openGlyphManipulator();
        sleep(250);
        moveDownGlyph(1.5);
        sleep(250);
        closeGlyphManipulator();
        sleep(250);
        moveUpGlyph(1.5);
        sleep(250);
        //here, we move the glyph manipulator down, grab the glyph, and then move the manipulator back up


        jewelServo.setPosition(0);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        sleep(1000);
        readColor();
        light.setPower(0);
        //returnImage();
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();

        if (ballColor.equals("blank")){
            jewelServo.setPosition(0.8);
            sleep(1000);
            jewelServo.setPosition(0);
            telemetry.addData("jewelServo Position", jewelServo.getPosition());
            telemetry.update();
            sleep(1000);
            readColor();
            light.setPower(0);
            //returnImage();
            telemetry.addData("Vumark" , vuMark);
            telemetry.update();
        }
//        closeGlyphManipulator();
//        sleep(1000);
//        //moveUpGlyph(2.54);
//        sleep(1000);

        //here, we looked for the ball by lowering arm and reading color. If ball is not found, we repeat the process

        if(ballColor.equals("blue")){
            encoderMecanumDrive(0.5, -10,-10,5000,0);
            jewelServo.setPosition(0.9);
            sleep(1000);
            encoderMecanumDrive(0.5,65,65,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("red")){
            encoderMecanumDrive(0.5,65,65,5000,0);
            jewelServo.setPosition(0.9);
            sleep(1000);
        }
        else{
            jewelServo.setPosition(0.9);
            sleep(1000);
            encoderMecanumDrive(0.5,65,65,5000,0);
        }


        gyroTurn(0.3,-88);
        sleep(1000);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            simpleRangeDistance(59, 0.6, rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            simpleRangeDistance(42, 0.6, rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            simpleRangeDistance(76, 0.6, rsBuffer);

        }


        sleep(1000);

        gyroTurn(0.3,0);

        sleep(750);

        moveDownGlyph(1.5);
        sleep(250);
        openGlyphManipulator();
        sleep(250);

        encoderMecanumDrive(0.3,30,30,1000,0);
    }


}

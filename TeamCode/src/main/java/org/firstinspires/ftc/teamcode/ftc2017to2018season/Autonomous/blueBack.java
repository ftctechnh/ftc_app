package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Final.Autonomous_General_final;

//10-28-17
@Autonomous(name="Autonomous Blue Back Final")
public class blueBack extends Autonomous_General {

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
        initiate();
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
//reseting gyro sensor
        gyro.resetZAxisIntegrator();

        toggleLight(true);
        light.setPower(1);
        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();

        double begintime= runtime.seconds();
        while(!vuMarkFound() && runtime.seconds() - begintime <= waitTime){


        }
        toggleLight(false);

        moveDownGlyph(3);
        sleep(200);
        closeGlyphManipulator();;
        sleep(200);
        moveUpGlyph(2);
        jewelServo.setPosition(0);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        sleep(1000);
        readColor();
        light.setPower(0);
        //returnImage();
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();
        closeGlyphManipulator();
        sleep(1000);
        moveUpGlyph(2.54);
        sleep(1000);

        if(ballColor.equals("blue")){
            encoderMecanumDrive(0.9, -10,-10,5000,0);
            jewelServo.setPosition(0.8);
            sleep(1000);
            encoderMecanumDrive(0.9,25,25,5000,0);
            sleep(1000);
        }
        else if(ballColor.equals("red")){
            encoderMecanumDrive(0.9,25,25,5000,0);
            jewelServo.setPosition(0.8);
            sleep(1000);
        }
        else{
            jewelServo.setPosition(0.8);
            sleep(1000);
            encoderMecanumDrive(0.9,25,25,5000,0);
        }

        //encoderMecanumDrive(0.4, 55, 55, 1000, 0);
        sleep(100);
        encoderMecanumDrive(0.9,35,35,5000,0);

        gyroTurn(0.3,0);

        sleep(250);

        if (vuMark == RelicRecoveryVuMark.CENTER){
            simpleRangeDistance(112,0.35,rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.LEFT){
            simpleRangeDistance(104,0.35,rsBuffer);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            simpleRangeDistance(123,0.35,rsBuffer);

        }

        gyroTurn(0.3,90);
        sleep(750);
        moveDownGlyph(1.54);
        sleep(500);
        openGlyphManipulator();

        encoderMecanumDrive(0.65,55,55,1000,0);



    }


}

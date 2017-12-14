package org.firstinspires.ftc.teamcode.MinerClue;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.lang.Math;

/**
 * Created by JL on 9/25/2017.
 */
@TeleOp(name = "Sample", group = "TeleOp")
public class JLKoenigTeleOp extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor rightMotorTest;
    private DcMotor leftMotorTest;
    private DcMotor elevatorMotorTest;
    private DcMotor beltMotorTest;
    private DigitalChannel digitalTouch;
    private DistanceSensor sensorColorRange;
    private Color currentColor;
    private ColorSensor sensorColorRecognition;
    private float [] HSB = new float [3];
    private CRServo servoTest;
    double elevatorPower = 1;
    boolean elevatorFlag = false;
    boolean precision = false;
    boolean detectBlue = false;
    boolean canSwitchPrecision = true;
    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        rightMotorTest = hardwareMap.get(DcMotor.class, "Rmotor");
        rightMotorTest.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorTest = hardwareMap.get(DcMotor.class, "Lmotor");
        beltMotorTest = hardwareMap.get (DcMotor.class, "belt1");
        elevatorMotorTest = hardwareMap.get (DcMotor.class, "elevator");
        //digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        sensorColorRecognition = hardwareMap.get (ColorSensor.class, "sensorColorRange");
        servoTest = hardwareMap.get(CRServo.class, "servoTest");
        telemetry.addData("Status", "Initialized");;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)
        double tgtPower = 0;
        while (opModeIsActive()) {



            tgtPower = -this.gamepad1.left_stick_y;
            leftMotorTest.setPower(tgtPower);
            tgtPower = -this.gamepad1.right_stick_y;
            rightMotorTest.setPower(tgtPower);
            telemetry.addData("Target Power", tgtPower);
            telemetry.addData("Right Motor Power", rightMotorTest.getPower( ));
            telemetry.addData("Left Motor Power", leftMotorTest.getPower());
            telemetry.addData("Precision Active", precision);

            telemetry.addData("Blue", sensorColorRecognition.blue());


            Color.RGBToHSV(sensorColorRecognition.red(), sensorColorRecognition.green(), sensorColorRecognition.blue(), HSB);
            telemetry.addData( "Saturation", HSB [1]);
            telemetry.addData ("Hue", HSB [0]);
            if (HSB [0] > 200 && HSB [0] < 275 && HSB [1] >= 0.6) {
                detectBlue = true;
                telemetry.addData("Color", "BLUE!!!");

//               rightMotorTest.setPower(1);
            } else {
                detectBlue = false;
                telemetry.addData("Color", "Not Blue :(");
//                rightMotorTest.setPower(0);
            }

            telemetry.addData("Distance", sensorColorRange.getDistance(DistanceUnit.CM));
            if (sensorColorRange.getDistance(DistanceUnit.CM) < 2.54)
                telemetry.addData ("Close", "Yes");
            else
                telemetry.addData ("Close", "No");




            if(this.gamepad1.a)
                elevatorMotorTest.setPower(elevatorPower);
            else if(this.gamepad1.b)
                elevatorMotorTest.setPower(-elevatorPower);
            else
                elevatorMotorTest.setPower(0);

            if(this.gamepad1.right_bumper)
                servoTest.setPower(1);
            else if(this.gamepad1.left_bumper)
                servoTest.setPower(-1);
            else
                servoTest.setPower(0);
            telemetry.addData("Servo Test",servoTest.getPower());

            telemetry.addData("Status", "Running");
            telemetry.update();
            if (this.gamepad1.x)
                beltMotorTest.setPower (5);
            else if(this.gamepad1.y)
                beltMotorTest.setPower (-0.5);
            else
                beltMotorTest.setPower (0);

        }

    }



//    //Defining motors
//    DcMotor rightMotor;
//    DcMotor leftMotor;
//
//
//
//    //Motor powers
//    double rightPower;
//    double leftPower;
//
//
//    @Override
//    public void init() {
//        rightMotor = hardwareMap.dcMotor.get("Rmoter");
//        leftMotor = hardwareMap.dcMotor.get("Lmotor");
//
//        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//    }
//
//    @Override
//    public void loop() {
//        rightPower = -gamepad1.right_stick_y;
//        leftPower = -gamepad1.left_stick_y;
//
//        rightMotor.setPower(rightPower);
//        leftMotor.setPower(leftPower);
//
//        double tgtPower = 0;
//        while (opModeIsActive()) {
//            tgtPower = -this.gamepad1.left_stick_y;
//            telemetry.addData("Target Power", tgtPower);
//            telemetry.addData("Motor Power", motorTest.getPower());
//            telemetry.addData("Status", "Running");
//            telemetry.update();
//        }
//
//    }
}

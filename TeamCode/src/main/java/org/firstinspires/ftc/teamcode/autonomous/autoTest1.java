package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

/**
 * Created by shiva on 10-02-2018.
 */

@Autonomous(name = "test1", group = "auto")

public class autoTest1 extends LinearOpMode{

    //DRIVE
    private static DcMotor motorFrontLeft;
    private static DcMotor motorBackLeft;
    private static DcMotor motorFrontRight;
    private static DcMotor motorBackRight;

    //GRAB
    private static DcMotor grabDC;
    private static Servo grabTopLeft;
    private static Servo grabBottomLeft;
    private static Servo grabTopRight;
    private static Servo grabBottomRight;

    //RELIC
    private static DcMotor relicDc;
    private static Servo relicArm;
    private static Servo relicGrab;

    //JEWEL
    private static Servo jewelArm;
    private static Servo jewelKnock;

    //SENSORS
    private static ColorSensor jColor;
    private static ModernRoboticsI2cGyro gyro;

    private static double gtlOPEN = 0.4;
    private static double gtlCLOSE = 0.6;

    private static double gtrOPEN = 0.35;
    private static double gtrCLOSE = 0.4;

    private static double gblOPEN = 0.25;
    private static double gblCLOSE = 0.6;
    private static double gblEXCLOSE = 0.8;

    private static double gbrOPEN = 0.55;
    private static double gbrCLOSE = 0.4;
    private static double gbrEXCLOSE = 0.1;

    private static double jaUP = 0.73;
    private static double jaDOWN = 0.2;

    private static double jkCENTER = 0.44;
    private static double jkRIGHT = 0;
    private static double jkLEFT = 1;

    private static double raOPEN = 0; /**change*/
    private static double raCLOSE = 0; /**change*/
    private static double raINITIAL = 0; /**change*/

    private static double rgOPEN = 1;
    private static double rgCLOSE = 0; /**change*/
    private static double rgINTITIAL = 0; /**change*/

    private double jaPos = jaUP;

    private static int zAccumulated;

    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws  InterruptedException{

        //DRIVE
        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

        //GRAB
        grabDC = hardwareMap.dcMotor.get("GDC");
        grabTopLeft = hardwareMap.servo.get("GTL");
        grabBottomLeft = hardwareMap.servo.get("GBL");
        grabTopRight = hardwareMap.servo.get("GTR");
        grabBottomRight = hardwareMap.servo.get("GBR");

        //RELIC
        relicDc = hardwareMap.dcMotor.get("RDC");
        relicArm = hardwareMap.servo.get("RA");
        relicGrab = hardwareMap.servo.get("RG");

        //JEWEL
        jewelKnock = hardwareMap.servo.get("JK");
        jewelArm = hardwareMap.servo.get("JA");

        //DC MODE
        motorFrontLeft.setMode(RUN_USING_ENCODER);
        motorBackLeft.setMode(RUN_USING_ENCODER);
        motorFrontRight.setMode(RUN_USING_ENCODER);
        motorBackRight.setMode(RUN_USING_ENCODER);

        grabDC.setMode(RUN_USING_ENCODER);
        relicDc.setMode(RUN_USING_ENCODER);

        motorFrontLeft.setDirection(REVERSE);
        motorBackLeft.setDirection(REVERSE);

        //SENSORS
        jColor = hardwareMap.colorSensor.get("colF");
        //jColor.setI2cAddress(I2cAddr.create8bit(0x3c));  /**check I2c address*/
        jColor.enableLed(true);
        gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro.calibrate();

        //STARTING VALUES
        grabBottomLeft.setPosition(gblEXCLOSE);
        grabTopLeft.setPosition(gtlOPEN);
        grabTopRight.setPosition(gtrOPEN);
        grabBottomRight.setPosition(gbrEXCLOSE);

        relicArm.setPosition(raINITIAL);
        relicGrab.setPosition(rgINTITIAL);

        jewelArm.setPosition(jaUP);
        jewelKnock.setPosition(jkRIGHT);

        waitForStart();
        telemetry.addData("JA:", jewelArm.getPosition());
        telemetry.update();

    //JEWEL KNOCK FOR BLUE SIDE
    jewelKnock.setPosition(jkCENTER);

    do{
        jaPos -= 0.02;
        jewelArm.setPosition(jaPos);
        telemetry.addData("JA:", jewelArm.getPosition());
        telemetry.update();
    } while (jewelArm.getPosition() > jaDOWN);

    Thread.sleep(800);

    telemetry.addData("blue: ", jColor.blue());
    telemetry.update();

    if (jColor.blue() < 3){
        jewelKnock.setPosition(jkRIGHT);
    }
    else {
        jewelKnock.setPosition(jkLEFT);
    }

    Thread.sleep(500);

    jewelArm.setPosition(0.4);
    jewelKnock.setPosition(jkRIGHT);

    do{
        jaPos += 0.03;
        jewelArm.setPosition(jaPos);
        telemetry.addData("JA:", jewelArm.getPosition());
        telemetry.update();
    } while (jewelArm.getPosition() < jaUP);



    //Grip the block and lift
    grabTopLeft.setPosition(0.3);
    grabTopRight.setPosition(0.4);
    GRABUP(1600);


    //Move forward
    FORWARD(4000, 0.5);
    gyro.calibrate();
    telemetry.addData("Gyro val:", gyro.getHeading());
    telemetry.update();

    //Turn towards safe zone
    //turnAbsolute(90, 0.07);
    AXISRIGHT(2500);
    telemetry.addData("Gyro val:", gyro.getHeading());
    telemetry.update();

    //Move towards safezone
    FORWARD(1200, 0.5);

    //Drop glyph
    grabTopLeft.setPosition(0.4);
    grabTopRight.setPosition(0.3);

    BACKWARD(1200);


    Thread.sleep(1000);

    }



    public static void FORWARD(int degrees, double power) {
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        motorFrontRight.setPower(power);
        motorBackRight.setPower(power);
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(power);

        motorBackLeft.setTargetPosition(degrees);
        motorFrontRight.setTargetPosition(degrees);
        motorBackRight.setTargetPosition(degrees);
        motorFrontLeft.setTargetPosition(degrees);

        motorFrontLeft.setMode(RUN_TO_POSITION);
        motorFrontRight.setMode(RUN_TO_POSITION);
        motorBackLeft.setMode(RUN_TO_POSITION);
        motorBackRight.setMode(RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }

        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
    }

    public static void BACKWARD(int degrees) {
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorFrontLeft.setPower(-1);
        motorBackLeft.setPower(-1);
        motorFrontRight.setPower(-1);
        motorBackRight.setPower(-1);

        motorBackLeft.setTargetPosition(-degrees);
        motorFrontRight.setTargetPosition(-degrees);
        motorBackRight.setTargetPosition(-degrees);
        motorFrontLeft.setTargetPosition(-degrees);

        motorFrontLeft.setMode(RUN_TO_POSITION);
        motorFrontRight.setMode(RUN_TO_POSITION);
        motorBackLeft.setMode(RUN_TO_POSITION);
        motorBackRight.setMode(RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }

    public static void AXISLEFT(int degrees) {
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorFrontLeft.setPower(-1);
        motorBackLeft.setPower(-1);

        motorFrontRight.setPower(1);
        motorBackRight.setPower(1);

        motorBackLeft.setTargetPosition(-degrees);
        motorFrontLeft.setTargetPosition(-degrees);

        motorBackRight.setTargetPosition(degrees);
        motorFrontRight.setTargetPosition(degrees);

        motorFrontLeft.setMode(RUN_TO_POSITION);
        motorFrontRight.setMode(RUN_TO_POSITION);
        motorBackLeft.setMode(RUN_TO_POSITION);
        motorBackRight.setMode(RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }

    public static void AXISRIGHT(int degrees) {
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorFrontLeft.setPower(1);
        motorBackLeft.setPower(1);
        motorFrontRight.setPower(-1);
        motorBackRight.setPower(-1);

        motorBackLeft.setTargetPosition(degrees);
        motorFrontLeft.setTargetPosition(degrees);
        motorBackRight.setTargetPosition(-degrees);
        motorFrontRight.setTargetPosition(-degrees);

        motorFrontLeft.setMode(RUN_TO_POSITION);
        motorFrontRight.setMode(RUN_TO_POSITION);
        motorBackLeft.setMode(RUN_TO_POSITION);
        motorBackRight.setMode(RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }

    public static void DIAGONALFORWARDRIGHT(int degrees) {
        //motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorFrontLeft.setPower(1);
        //motorBackLeft.setPower(1);
        //motorFrontRight.setPower(1);
        motorBackRight.setPower(1);

        //motorBackLeft.setTargetPosition(degrees);
        //motorFrontRight.setTargetPosition(degrees);
        motorBackRight.setTargetPosition(degrees);
        motorFrontLeft.setTargetPosition(degrees);

        motorFrontLeft.setMode(RUN_TO_POSITION);
        //motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }

    public static void DIAGONALFORWARDLEFT(int degrees) {
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //motorFrontLeft.setPower(1);
        motorBackLeft.setPower(1);
        motorFrontRight.setPower(1);
        //motorBackRight.setPower(1);

        motorBackLeft.setTargetPosition(degrees);
        motorFrontRight.setTargetPosition(degrees);
        //motorBackRight.setTargetPosition(degrees);
        //motorFrontLeft.setTargetPosition(degrees);

        //motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(RUN_TO_POSITION);
        motorBackLeft.setMode(RUN_TO_POSITION);
        //motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }

    public static void DIAGONALBACKWARDRIGHT(int degrees) {
        //motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorFrontLeft.setPower(-1);
        //motorBackLeft.setPower(1);
        //motorFrontRight.setPower(1);
        motorBackRight.setPower(-1);

        //motorBackLeft.setTargetPosition(degrees);
        //motorFrontRight.setTargetPosition(degrees);
        motorBackRight.setTargetPosition(-degrees);
        motorFrontLeft.setTargetPosition(-degrees);

        motorFrontLeft.setMode(RUN_TO_POSITION);
        //motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }

    public static void DIAGONALBACKWARDLEFT(int degrees) {
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //motorFrontLeft.setPower(1);
        motorBackLeft.setPower(-1);
        motorFrontRight.setPower(-1);
        //motorBackRight.setPower(1);

        motorBackLeft.setTargetPosition(-degrees);
        motorFrontRight.setTargetPosition(-degrees);
        //motorBackRight.setTargetPosition(degrees);
        //motorFrontLeft.setTargetPosition(degrees);

        //motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(RUN_TO_POSITION);
        motorBackLeft.setMode(RUN_TO_POSITION);
        //motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }

    public static void SWAYLEFT(int degrees) {
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorFrontLeft.setPower(-1);
        motorBackLeft.setPower(1);
        motorFrontRight.setPower(1);
        motorBackRight.setPower(-1);

        motorBackLeft.setTargetPosition(degrees);
        motorFrontRight.setTargetPosition(degrees);
        motorBackRight.setTargetPosition(-degrees);
        motorFrontLeft.setTargetPosition(-degrees);

        motorFrontLeft.setMode(RUN_TO_POSITION);
        motorFrontRight.setMode(RUN_TO_POSITION);
        motorBackLeft.setMode(RUN_TO_POSITION);
        motorBackRight.setMode(RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);

    }

    public static void SWAYRIGHT(int degrees) {
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorFrontLeft.setPower(0.7);
        motorBackLeft.setPower(-0.7);
        motorFrontRight.setPower(-0.7);
        motorBackRight.setPower(0.7);

        motorFrontLeft.setTargetPosition(degrees);
        motorBackLeft.setTargetPosition(-degrees);
        motorFrontRight.setTargetPosition(-degrees);
        motorBackRight.setTargetPosition(degrees);

        motorFrontLeft.setMode(RUN_TO_POSITION);
        motorBackLeft.setMode(RUN_TO_POSITION);
        motorFrontRight.setMode(RUN_TO_POSITION);
        motorBackRight.setMode(RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }
    public static void GRABUP(int degrees) {
        grabDC.setMode(STOP_AND_RESET_ENCODER);
        grabDC.setMode(RUN_USING_ENCODER);

        grabDC.setPower(0.75);

        grabDC.setTargetPosition(degrees);

        grabDC.setMode(RUN_TO_POSITION);

        while (grabDC.isBusy()) {
            //wait till motors done doing its thing
        }

        grabDC.setPower(0);
    }
    public static void GRABDOWN(int degrees) {
        grabDC.setMode(STOP_AND_RESET_ENCODER);
        grabDC.setMode(RUN_USING_ENCODER);

        grabDC.setPower(-0.75);

        grabDC.setTargetPosition(degrees);

        grabDC.setMode(RUN_TO_POSITION);

        while (grabDC.isBusy()) {
            //wait till motors done doing its thing
        }

        grabDC.setPower(0);
    }

    public static void GYROTOZERO() throws InterruptedException
    {
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (gyro.getHeading() >= 357 || gyro.getHeading() <= 3) {
        } else {

            if (gyro.getHeading() > 180 && gyro.getHeading() < 357) {

                motorFrontLeft.setPower(0.2);
                motorBackLeft.setPower(0.2);
                motorFrontRight.setPower(-0.2);
                motorBackRight.setPower(-0.2);
            }
            if (gyro.getHeading() <= 180 && gyro.getHeading() > 3) {

                motorFrontLeft.setPower(-0.2);
                motorBackLeft.setPower(-0.2);
                motorFrontRight.setPower(0.2);
                motorBackRight.setPower(0.2
                );
            }

            while (((gyro.getHeading() > 3) && (gyro.getHeading() < 357))) {

            }

            if (((gyro.getHeading() < 3) && (gyro.getHeading() > 357))) {
                motorFrontLeft.setPower(0);
                motorBackLeft.setPower(0);
                motorFrontRight.setPower(0);
                motorBackRight.setPower(0);
            }
        }
    }

    public static void turnAbsolute(int target, double turnSpeed) {

        zAccumulated = gyro.getIntegratedZValue();  //Set variables to gyro readings
        //turnSpeed = 0.07;

        while (Math.abs(zAccumulated - target) > 1) {  //Continue while the robot direction is further than three degrees from the target
            if (zAccumulated > target) {  //if gyro is positive, we will turn right
                motorBackLeft.setPower(turnSpeed);
                motorFrontLeft.setPower(turnSpeed);
                motorBackRight.setPower(-turnSpeed);
                motorFrontRight.setPower(-turnSpeed);
            }

            if (zAccumulated < target) {  //if gyro is positive, we will turn left
                motorBackLeft.setPower(-turnSpeed);
                motorFrontLeft.setPower(-turnSpeed);
                motorBackRight.setPower(turnSpeed);
                motorFrontRight.setPower(turnSpeed);
            }

            zAccumulated = gyro.getIntegratedZValue();  //Set variables to gyro readings
        }

        motorBackLeft.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorFrontRight.setPower(0);

    }
//    public static void GYROAXISRIGHT(int targetVal) {
//        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        motorFrontLeft.setPower(1);
//        motorBackLeft.setPower(1);
//        motorFrontRight.setPower(-1);
//        motorBackRight.setPower(-1);
//
//        while (!(gyro.getHeading() > (targetVal - 2)) && (gyro.getHeading() < (targetVal + 2))) {
//            // wait till value is reached
//        }
//        motorFrontLeft.setPower(0);
//        motorBackLeft.setPower(0);
//        motorFrontRight.setPower(0);
//        motorBackRight.setPower(0);
//
//    }
//
//    public static void GYROAXISLEFT(int targetVal) {
//        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        motorFrontLeft.setPower(-1);
//        motorBackLeft.setPower(-1);
//        motorFrontRight.setPower(1);
//        motorBackRight.setPower(1);
//
//        while (!(gyro.getHeading() < (-targetVal - 3) && (gyro.getHeading() > -(targetVal + 3)))) {
//            // wait till value is reached
//        }
//
//        motorFrontLeft.setPower(0);
//        motorBackLeft.setPower(0);
//        motorFrontRight.setPower(0);
//        motorBackRight.setPower(0);
//    }



}

package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.I2cAddr;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
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

    private static int redBallValR = 1; /**change*/
    private static int redBallValB = 1; /**change*/
    private static int redBallValG = 1; /**change*/
    private static int blueBallValR = 1; /**change*/
    private static int blueBallValB = 1; /**change*/
    private static int blueBallValG = 1; /**change*/

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

    private static double jaUP = 0; /**change*/
    private static double jaDOWN = 0; /**change*/

    private static double jkINITIAL = 0;
    private static double jkCENTER = 0.55;
    private static double jkRIGHT = 0;
    private static double jkLEFT = 1;

    private static double raOPEN = 0; /**change*/
    private static double raCLOSE = 0; /**change*/

    private static double rgOPEN = 0; /**change*/
    private static double rgCLOSE = 0; /**change*/

    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws  InterruptedException{

        //DRIVE
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorBackRight = hardwareMap.dcMotor.get("BR");

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
        jColor = hardwareMap.colorSensor.get("colorFloor");
        jColor.setI2cAddress(I2cAddr.create8bit(0x3c));  /**check I2c address*/
        jColor.enableLed(true);

        //STARTING VALUES
        grabTopLeft.setPosition(gtlCLOSE);
        grabBottomLeft.setPosition(gblEXCLOSE);
        grabTopRight.setPosition(gtrCLOSE);
        grabBottomRight.setPosition(gbrEXCLOSE);

        //relicArm.setPosition(0);
        //relicGrab.setPosition(0);

        //jewelArm.setPosition(0.71);
        //jewelKnock.setPosition(0);

    waitForStart();

    }
    public static void FORWARD(int degrees) {
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        motorFrontRight.setPower(1);
        motorBackRight.setPower(1);
        motorFrontLeft.setPower(1);
        motorBackLeft.setPower(1);

        motorBackLeft.setTargetPosition(degrees);
        motorFrontRight.setTargetPosition(degrees);
        motorBackRight.setTargetPosition(degrees);
        motorFrontLeft.setTargetPosition(degrees);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy()) {
            //wait till motors done doing its thing
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }
}

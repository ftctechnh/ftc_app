package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

@Autonomous(name = "Blue side position 1", group = "auto")

public class NEW_BLUE1 extends LinearOpMode {
    //DRIVE
    private static DcMotor motorFrontLeft;
    private static DcMotor motorBackLeft;
    private static DcMotor motorFrontRight;
    private static DcMotor motorBackRight;

    //GRAB
    private static DcMotor grabMotor;
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

    private double gtlOPEN;
    private double gtlGRAB;

    private double gtrOPEN;
    private double gtrGRAB;

    private double gblOPEN;
    private double gblGRAB;

    private double gbrOPEN;
    private double gbrGRAB;

    private static double jaUP = 0.635;
    private static double jaDOWN = 0.1;

    private static double jkCENTER = 0.5;
    private static double jkRIGHT = 0.27;
    private static double jkLEFT = 0.69;
    private static double jkINITIAL = 0;

//    private static double raINITIAL = 0; /**change*///    private static double rgINTITIAL = 0; /**change*/
//    private static double raOPEN = 0; /**change*/
//    private static double rgOPEN = 1; /**change*/
//    private static double raCLOSE = 0; /**change*/
//    private static double rgCLOSE = 0; /**change*/

    private int gridColumn;

    private static int zAccumulated;

    private double jaPos = jaUP;

    ElapsedTime timer = new ElapsedTime();

    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;

    @Override

    public void runOpMode() throws  InterruptedException{

        //Grab servo values
        gtlOPEN = 0.61;
        gtlGRAB = 0.26;

        gtrOPEN = 0.12;
        gtrGRAB = 0.49;

        gblOPEN = 0.07;
        gblGRAB = 0.39;

        gbrOPEN = 0.71;
        gbrGRAB = 0.31;

        gridColumn = 3;

        //DRIVING
        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        //GRAB
        grabMotor = hardwareMap.dcMotor.get("GrabDC");
        grabTopLeft = hardwareMap.servo.get("GTL");
        grabBottomLeft = hardwareMap.servo.get("GBL");
        grabTopRight = hardwareMap.servo.get("GTR");
        grabBottomRight = hardwareMap.servo.get("GBR");

        //RELIC
        relicDc = hardwareMap.dcMotor.get("RelicDC");
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

        grabMotor.setMode(RUN_USING_ENCODER);
        relicDc.setMode(RUN_USING_ENCODER);

        motorFrontLeft.setDirection(REVERSE);
        motorBackLeft.setDirection(REVERSE);

        //SENSORS
        jColor = hardwareMap.colorSensor.get("colF");
        //jColor.setI2cAddress(I2cAddr.create8bit(0x3c));  /**check I2c address*/
        jColor.enableLed(true);

//        gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
//        telemetry.log().add("Gyro Calibrating. Do Not Move!");
//        gyro.calibrate();
//        // Wait until the gyro calibration is complete
//        timer.reset();
//        while (!isStopRequested() && gyro.isCalibrating())  {
//            telemetry.addData("calibrating", "%s", Math.round(timer.seconds())%2==0 ? "|.." : "..|");
//            telemetry.update();
//            sleep(50);
//        }
//        telemetry.log().clear(); telemetry.log().add("Gyro Calibrated. Press Start.");
//        telemetry.clear(); telemetry.update();
//        telemetry.addData("Gyro value: ", gyro.getHeading());
//        telemetry.update();


        //STARTING VALUES
        grabBottomLeft.setPosition(gblOPEN);
        grabTopLeft.setPosition(gtlOPEN);
        grabTopRight.setPosition(gtrOPEN);
        grabBottomRight.setPosition(gbrOPEN);

//        relicArm.setPosition(raINITIAL);
//        relicGrab.setPosition(rgINTITIAL);

        jewelArm.setPosition(jaUP);
        jewelKnock.setPosition(jkRIGHT);

        /**<VUFORIA>*/
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "ASg9+Lf/////AAAAmSV/ZiXUrU22pM3b5qOg2oJoTEYLmeQoyo7QENEfWgcz+LnuTsVPHDypRkMZI88hbCcjqmV3oD33An5LQK/c4B8mdl+wiHLQlpgTcgfkmzSnMJRx0fA7+iVlor2ascTwNhmDjt38DUHzm70ZVZQC8N5e8Ajp8YBieWUEL4+zaOJzi4dzaog/5nrVMpOdMwjLsLC1x4RaU89j6browKc84rzHYCrwwohZpxiiBNlqLfyCbIRzP99E3nVQ7BlnrzSP8WDdfjhMj6sRIxDXCEgHhrDW+xYmQ+qc8tjW5St1pTO9IZj31SLYupSCN7n0otW1FIyc9TTJZM4FKAOSbMboniQsSTve+9EaHMGfhVbcQf/M";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        /**</VUFORIA>*/



        waitForStart();



        //VUFORIA
        relicTrackables.activate();

        //JEWEL KNOCK FOR BLUE SIDE
        jewelKnock.setPosition(jkCENTER);

        do {
            jaPos -= 0.02;
            jewelArm.setPosition(jaPos);
            // telemetry.addData("JA:", jewelArm.getPosition());
            // telemetry.update();
        } while (jewelArm.getPosition() > jaDOWN);

        Thread.sleep(800);

        //telemetry.addData("blue: ", jColor.blue());
        //telemetry.update();

        if (jColor.blue() < 3) {
            jewelKnock.setPosition(jkLEFT);
        } else {
            jewelKnock.setPosition(jkRIGHT);
        }

        Thread.sleep(500);

        jewelArm.setPosition(0.4);
        jewelKnock.setPosition(jkRIGHT);

        do {
            jaPos += 0.03;
            jewelArm.setPosition(jaPos);
            //telemetry.addData("JA:", jewelArm.getPosition());
            //telemetry.update();
        } while (jewelArm.getPosition() < jaUP);

        Thread.sleep(1000);

        /**<VUFORIA>*/
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            telemetry.addData("VuMark", "%s visible", vuMark);
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                gridColumn = 2;
            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                gridColumn = 1;
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                gridColumn = 3;
            } else {
                telemetry.addData("error", gridColumn);
            }
        } else {
            telemetry.addData("VuMark", "not visible");
        }
        telemetry.update();
        /**</VUFORIA>*/

        //Degrees travlled at this point
        telemetry.addData("front left degrees = ", motorFrontLeft.getCurrentPosition());
        telemetry.addData("front right degrees = ", motorFrontRight.getCurrentPosition());
        telemetry.addData("back left degrees = ", motorBackLeft.getCurrentPosition());
        telemetry.addData("back right degrees = ", motorBackRight.getCurrentPosition());
        telemetry.update();

        //Grip the block and lift
        grabTopLeft.setPosition(gtlGRAB);
        grabTopRight.setPosition(gtrGRAB);
        GRABUP(1605);

        if (gridColumn == 2) {

            //Move forward: MIDDLE
            BACKWARD(3950); /**change*/

            //Lower grabber
            GRABDOWN(1400);

            //GYRO CALIBRATE
//            gyro.resetZAxisIntegrator();
//            telemetry.addData("Gyro value: ", gyro.getHeading());
//            telemetry.update();

            AXISRIGHT(2300); /**change*/

            //Move towards safezone
            FORWARD(450, 0.5); /**change*/

            //Drop glyph
            grabTopLeft.setPosition(gtlOPEN);
            grabTopRight.setPosition(gtrOPEN);

            FORWARD(820, 0.5);

            BACKWARD(500); /**change*/

            FORWARD(600, 0.5); /**change*/
            BACKWARD(550); /**change*/

            FORWARD(570, 0.5); /**change*/
            BACKWARD(1000); /**change*/

            AXISRIGHT(4500);
            BACKWARD(650);

        }

        //Placement of block according to Vuforia
//        if (gridColum == 2){ //MIDDLE
//            //Move forward: MIDDLE
//            BACKWARD(4100, 0.5);
//            gyro.calibrate();
//            telemetry.addData("Gyro val:", gyro.getHeading());
//            telemetry.update();
//
//            AXISRIGHT(2545);
//            telemetry.addData("Gyro val:", gyro.getHeading());
//            telemetry.update();
//
//            //Move towards safezone
//            FORWARD(1270, 0.5);
//
//            //Drop glyph
//            grabTopLeft.setPosition(0.4);
//            grabTopRight.setPosition(0.3);
//
//
//            BACKWARD(500, 0.5);
//
//            FORWARD(500, 0.5);
//            BACKWARD(500,0.5);
//            FORWARD(570,0.5);
//            BACKWARD(500,0.5);
//        }

        if (gridColumn == 1){

            //Move forward: RIGHT
            BACKWARD(3000); /**change*/

            //Lower grabber
            GRABDOWN(1400);

            //GYRO CALIBRATE
//            gyro.resetZAxisIntegrator();
//            telemetry.addData("Gyro value: ", gyro.getHeading());
//            telemetry.update();

            AXISRIGHT(2300); /**change*/

            //Move towards safezone
            FORWARD(450, 0.5); /**change*/

            //Drop glyph
            grabTopLeft.setPosition(gtlOPEN);
            grabTopRight.setPosition(gtrOPEN);

            FORWARD(820, 0.5); /**change*/

            BACKWARD(500); /**change*/

            FORWARD(600, 0.5); /**change*/
            BACKWARD(550); /**change*/

            //FOR RIGHT SIDE
            SWAYRIGHT(650); /**change*/

            FORWARD(570,0.5); /**change*/
            BACKWARD(1000); /**change*/

            AXISRIGHT(4500);
            BACKWARD(650);

        }

        //        if (gridColum == 1){ //RIGHT
//            //Move forward: RIGHT
//            BACKWARD(4920, 0.5);
//            gyro.calibrate();
//            telemetry.addData("Gyro val:", gyro.getHeading());
//            telemetry.update();
//
//            AXISRIGHT(2500);
//            telemetry.addData("Gyro val:", gyro.getHeading());
//            telemetry.update();
//
//            //Move towards safezone
//            FORWARD(1270, 0.5);
//
//            //Drop glyph
//            grabTopLeft.setPosition(0.4);
//            grabTopRight.setPosition(0.3);
//
//            BACKWARD(500, 0.5);
//
//            FORWARD(500, 0.5);
//            BACKWARD(500,0.5);
//
//            //FOR LEFT SIDE
//            SWAYLEFT(600);
//
//            FORWARD(570,0.5);
//            BACKWARD(500,0.5);
//        }

        if (gridColumn == 3){

            //Move forward: MIDDLE
            BACKWARD(4850); /**change*/

            //Lower grabber
            GRABDOWN(1400);

            //GYRO CALIBRATE
//            gyro.resetZAxisIntegrator();
//            telemetry.addData("Gyro value: ", gyro.getHeading());
//            telemetry.update();

            AXISRIGHT(2200); /**change*/

            //Move towards safezone
            FORWARD(450, 0.5); /**change*/

            //Drop glyph
            grabTopLeft.setPosition(gtlOPEN);
            grabTopRight.setPosition(gtrOPEN);

            FORWARD(820, 0.5); /**change*/

            BACKWARD(500); /**change*/

            FORWARD(600, 0.5); /**change*/
            BACKWARD(550); /**change*/

            //FOR LEFT SIDE
            SWAYLEFT(650); /**change*/

            FORWARD(570, 0.5); /**change*/
            BACKWARD(1000); /**change*/

            AXISRIGHT(4500);
            BACKWARD(650);

        }

//        if (gridColum == 3){ //LEFT
//            //Move forward: LEFT
//            BACKWARD(3080, 0.5);
//            gyro.calibrate();
//            telemetry.addData("Gyro val:", gyro.getHeading());
//            telemetry.update();
//
//            AXISRIGHT(2450);
//            telemetry.addData("Gyro val:", gyro.getHeading());
//            telemetry.update();
//
//            //Move towards safezone
//            FORWARD(1270, 0.5);
//
//            //Drop glyph
//            grabTopLeft.setPosition(0.4);
//            grabTopRight.setPosition(0.3);
//
//            BACKWARD(500, 0.5);
//
//            FORWARD(500, 0.5);
//            BACKWARD(500,0.5);
//
//            //FOR RIGHT SIDE
//            SWAYRIGHT(600);
//
//            FORWARD(570, 0.1);
//            BACKWARD(500,0.5);
//        }

        //Degrees travlled at this point
        telemetry.addData("front left degrees = ", motorFrontLeft.getCurrentPosition());
        telemetry.addData("front right degrees = ",motorFrontRight.getCurrentPosition());
        telemetry.addData("back left degrees = ", motorBackLeft.getCurrentPosition());
        telemetry.addData("back right degrees = ", motorBackRight.getCurrentPosition());
        telemetry.update();

        Thread.sleep(5000);

        //Degrees travlled at this point
        telemetry.addData("front left degrees = ", motorFrontLeft.getCurrentPosition());
        telemetry.addData("front right degrees = ",motorFrontRight.getCurrentPosition());
        telemetry.addData("back left degrees = ", motorBackLeft.getCurrentPosition());
        telemetry.addData("back right degrees = ", motorBackRight.getCurrentPosition());
        telemetry.update();
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
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(power);
        motorBackRight.setPower(power);

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
        motorFrontRight.setPower(-1);
        motorBackLeft.setPower(-1);
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

        motorFrontRight.setPower(0.7);
        motorBackRight.setPower(-0.7);
        motorBackLeft.setPower(0.7);
        motorFrontLeft.setPower(-0.7);

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
        grabMotor.setMode(STOP_AND_RESET_ENCODER);
        grabMotor.setMode(RUN_USING_ENCODER);


        grabMotor.setPower(1);

        grabMotor.setTargetPosition(degrees);

        grabMotor.setMode(RUN_TO_POSITION);

        while (grabMotor.isBusy()) {
            //wait till motors done doing its thing
        }

        grabMotor.setPower(0);
    }
    public static void GRABDOWN(int degrees) {
        grabMotor.setMode(STOP_AND_RESET_ENCODER);
        grabMotor.setMode(RUN_USING_ENCODER);

        grabMotor.setPower(-0.75);

        grabMotor.setTargetPosition(-degrees);

        grabMotor.setMode(RUN_TO_POSITION);

        while (grabMotor.isBusy()) {
            //wait till motors done doing its thing
        }

        grabMotor.setPower(0);
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

    public void turnAbsolute(int target, double turnSpeed) {

        zAccumulated = gyro.getIntegratedZValue();  //Set variables to gyro readings

        while (Math.abs(zAccumulated - target) > 2) {  //Continue while the robot direction is further than three degrees from the target
            if (zAccumulated > target) {  //if gyro is positive, we will turn right
                motorBackLeft.setPower(turnSpeed);
                motorFrontLeft.setPower(turnSpeed);
                motorBackRight.setPower(-turnSpeed);
                motorFrontRight.setPower(-turnSpeed);

                telemetry.addData("Gyro sensor: ", gyro.getIntegratedZValue());
                telemetry.update();
            }

            if (zAccumulated < target) {  //if gyro is positive, we will turn left
                motorBackLeft.setPower(-turnSpeed);
                motorFrontLeft.setPower(-turnSpeed);
                motorBackRight.setPower(turnSpeed);
                motorFrontRight.setPower(turnSpeed);

                telemetry.addData("Gyro sensor: ", gyro.getIntegratedZValue());
                telemetry.update();
            }


            telemetry.addData("Gyro sensor: ", gyro.getIntegratedZValue());
            telemetry.update();

            zAccumulated = gyro.getIntegratedZValue();  //Set variables to gyro readings

            telemetry.addData("Gyro sensor: ", gyro.getIntegratedZValue());
            telemetry.update();
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



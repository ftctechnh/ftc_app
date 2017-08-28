
package org.usfirst.ftc.theintersect.code;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "ColorSensorTest")
public class ColorSensorTest extends LinearOpMode {
    static DcMotor rF, rB, lF, lB, flywheel1, flywheel2, sweeperLow;
    static ModernRoboticsI2cGyro gyro;
    static ColorSensor beaconColor,floorColor;
    static int conversionFactor = 50;
    static CRServo buttonPusher;
    boolean red = false;
    double average;
    DriveTrain driveTrain;
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        waitForStart();
        if (opModeIsActive()) {
            while(opModeIsActive()){
                telemetry.addData("blue", beaconColor.blue());
                telemetry.addData("red", beaconColor.red());
                telemetry.addData("FloorWhite", average);
                telemetry.update();
            }
        }
    }

    public void initHardware() {
        buttonPusher = hardwareMap.crservo.get("buttonPusher");
        rF = hardwareMap.dcMotor.get("rF");
        rB = hardwareMap.dcMotor.get("rB");
        lF = hardwareMap.dcMotor.get("lF");
        lB = hardwareMap.dcMotor.get("lB");
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel1 = hardwareMap.dcMotor.get("flywheel1");
        flywheel2 = hardwareMap.dcMotor.get("flywheel2");
        flywheel1.setDirection(DcMotorSimple.Direction.REVERSE);
        //beaconColor = hardwareMap.colorSensor.get("beaconColor");
        floorColor = hardwareMap.colorSensor.get("floorColor");
        beaconColor = hardwareMap.colorSensor.get("beaconColor");
        sweeperLow = hardwareMap.dcMotor.get("sweeperLow");
        lF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setDirection(DcMotor.Direction.FORWARD);
        rB.setDirection(DcMotor.Direction.FORWARD);
        lB.setDirection(DcMotor.Direction.REVERSE);
        lF.setDirection(DcMotor.Direction.REVERSE);
        //beaconColor.setI2cAddress(I2cAddr.create8bit(0x3c));
        //floorColor.setI2cAddress(I2cAddr.create8bit(0x4c));
        beaconColor.setI2cAddress(I2cAddr.create8bit(0x3c));
        //floorColor.setI2cAddress(I2cAddr.create8bit(0x3a));
        driveTrain = new DriveTrain(lB, rB, lF, rF, this, gyro, floorColor);
        Servo lLift = hardwareMap.servo.get("lLift");
        Servo rLift = hardwareMap.servo.get("rLift");
        lLift.setDirection(Servo.Direction.REVERSE);
        lLift.setPosition(Functions.liftUpPos);
        rLift.setPosition((Functions.liftUpPos));
        resetEncoders();
        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        //driveTrain.calibrateGyro(telemetry);
    }
    public void colorDriveBkwd(double speed, int maxDist,char color, int timeoutS){
        driveTrain.moveBkwd(speed, maxDist, timeoutS);
        if(color == 'b') {
            while (!detectBlue(beaconColor)) {

            }
        } else if(color == 'r') {
            while (!detectRed(beaconColor)) {

            }
        }
        driveTrain.stopAll();
    }
    public void extendPusher(int time){
        buttonPusher.setPower(1);
        Functions.waitFor(time);
        buttonPusher.setPower(0);

    }
    public void retractPusher(int time){
        buttonPusher.setPower(-1);
        Functions.waitFor(time);
        buttonPusher.setPower(0);

    }
    public void runFlywheel(double power){
        flywheel2.setPower(power);
        flywheel1.setPower(power);
    }
    public void wallAlign(){
        driveTrain.strafeRight(.6);
        Functions.waitFor(1000);
        driveTrain.stopAll();
    }
    public void wallGTFO(){
        driveTrain.strafeLeft(0.5);
        Functions.waitFor(500);
        driveTrain.stopAll();
    }
    public void pressBeacon(){
        buttonPusher.setPower(1);
        Functions.waitFor(1500);
        buttonPusher.setPower(0);
        Functions.waitFor(1500);
        buttonPusher.setPower(1);
        Functions.waitFor(1500);
        buttonPusher.setPower(0);
    }
    public void moveBeacon(){
        boolean beaconRed = detectRed(beaconColor);
        if (beaconRed){
            telemetry.addLine("blue detected");
            telemetry.addData("red", beaconColor.red());
            telemetry.addData("blue", beaconColor.blue());
            telemetry.update();
            wallGTFO();
            Functions.waitFor(200);
            rB.setPower(.3);
            lF.setPower(.3);
            rF.setPower(.3);
            lB.setPower(.3);
            Functions.waitFor(400);
            driveTrain.stopAll();
        } else{
            telemetry.addLine("red detected");
            telemetry.addData("red", beaconColor.red());
            telemetry.addData("blue", beaconColor.blue());
            telemetry.update();
            Functions.waitFor(1000);
            /*driveTrain.strafeLeft(0.5);
            Functions.waitFor(300);*/
            /*driveTrain.moveBkwd(0.3);
            Functions.waitFor(300);
            driveTrain.stopAll();*/
        }
    }
    public void resetEncoders() {
        lF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void firstBeacon(){
        if (red) {
            driveTrain.strafeLeft(0.3, 18, 5);
            driveTrain.moveBkwd(0.3,6, 5);
            driveTrain.moveFwd(0.2);
            while (!detectRed(beaconColor) && opModeIsActive()){
            }
            driveTrain.stopAll();
            buttonPusher.setPower(0.6);
            driveTrain.strafeRight(0.2,3,5);
        }
        else {
            driveTrain.strafeRight(0.3, 18,5);
            driveTrain.moveBkwd(0.3,6,5);
            driveTrain.moveFwd(0.2);
            while (!detectBlue(beaconColor) && opModeIsActive()){
            }
            driveTrain.stopAll();
            buttonPusher.setPower(0.6);
            driveTrain.strafeRight(0.2,3,3);
        }
    }
    public static void waitFor(int mill) {
        try {
            Thread.sleep(mill);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    public static double convertGamepad(float y) {
        int m;
        if (y < 0) {
            m = 1;
        } else {
            m = -1;
        }
        return m * (1 - Math.sqrt(1 - (y * y)));
    }


    public void sweeperOn(double power){
        sweeperLow.setPower(power);
    }
    public void sweeperOff(){
        sweeperLow.setPower(0);
    }


    public void flywheelOn(double power){
        for(double i = 0; i < power*10; i++){
           flywheel1.setPower(i/10);
           flywheel2.setPower(i/10);
            Functions.waitFor(100);
        }
    }
    public void flywheelOff(double power){
        for(double i = power*10; i > 0; i--){
            flywheel1.setPower(i/10);
            flywheel2.setPower(i/10);
            Functions.waitFor(100);
        }
    }



    public void debugColor(ColorSensor colorsensor, Telemetry telemetry) {
        while (System.currentTimeMillis()
                < System.currentTimeMillis() + 10000000000L && opModeIsActive()) {
            telemetry.addData("Red", colorsensor.red());
            telemetry.addData("Green", colorsensor.green());
            telemetry.addData("Blue", colorsensor.blue());
            telemetry.update();
            Functions.waitFor(50);
        }
    }

    public boolean detectWhite(ColorSensor colorsensor) {
        int red = colorsensor.red();
        int green = colorsensor.green();
        int blue = colorsensor.blue();
        average = (red + green + blue) / 3;
        return (average > 17);
    }


    public boolean detectBlue(ColorSensor colorsensor) {
        int blue = colorsensor.blue();
        return blue > 3;
    }

    public boolean detectRed(ColorSensor colorsensor) {
        int red = colorsensor.red();
        int blue = colorsensor.blue();
        return red>3;
    }
    public void stopAtWhite(double power, long timeout, Telemetry telemetry) {
        driveTrain.moveFwd(power);
        long endTime = System.currentTimeMillis() + (timeout * 1000);
        while (System.currentTimeMillis() < endTime) {
            if (detectWhite(beaconColor)) {
                break;
            }
        }
        driveTrain.stopAll();
        waitFor(5000);
    }
    public void options(){
        telemetry.addData("Team", "Blue");
        telemetry.update();
        boolean confirmed = false;
        red = false;
        while(!confirmed){
            if (gamepad1.a){
                red = true;
                telemetry.addData("Team", red ? "Red": "Blue");
            }
            if (gamepad1.b){
                red = false;
                telemetry.addData("Team", red ? "Red": "Blue");
            }
            telemetry.update();

            if (gamepad1.left_stick_button && gamepad1.right_stick_button){
                telemetry.addData("Team", red ? "Red" : "Blue");
                telemetry.addData("Confirmed!", "");
                telemetry.update();
                confirmed = true;
            }

        }
    }
}


package org.firstinspires.ftc.teamcode;
//All imports go here, anything you will use, like motors or servos.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;
import android.util.Log;

//This makes the OpMode available in the Autonomouds group under the name 'Autonomous', in the Driver Station
//@Disabled //AUSTIN REMEMBER TO REMOVE THIS -Austin from 4:55 P. M. on 11/7/17 // remembered
@Autonomous(name = "RAutonomousR", group = "Autonomous")
@Disabled
//This is the basic class
public class RedRecovery extends LinearOpMode {
    //Declare all of your motors, servos, sensors, etc.
    //Forklift
    private Servo rightClaw;
    private Servo leftClaw;
    private GyroSensor gyroSensor;
    private double clawPosition = 0.0, clawHighEnd = 0.45, clawLowEnd = 0.2;
    private DcMotor DrawerSlide;
    private double DrawerSlideLowEnd;
    private double DrawerSlideHighEnd;
    private double DrawerSlideSpeed = 0;
    //Jewel Arm
    Servo servo;
    DcMotor FrontLeftMotor, FrontRightMotor, BackLeftMotor, BackRightMotor;
    boolean a = true;
    ColorSensor cS;
    // Now declare any universal value you will need more then once, like encoder CPR(Clicks per rotation)
    int CPR = 1120; //Encoder CPR
    int Tm = 1; // The part of the gear ratio attached to the motor
    int Tw = 1; //The part of the gear ratio attached to the wheel
    double D = 4.0; //Diameter of wheels
    double C = D * Math.PI;//One rotation of tank gear/wheel
    int heading = 0;
    public void runOpMode() throws InterruptedException {
        //Start with the basic declaration of variable strings that the phones will read

        FrontLeftMotor = hardwareMap.dcMotor.get("m1");
        FrontRightMotor = hardwareMap.dcMotor.get("m2");
        BackLeftMotor = hardwareMap.dcMotor.get("m3");
        BackRightMotor = hardwareMap.dcMotor.get("m4");


        try {
            rightClaw = hardwareMap.servo.get("s1");
            rightClaw.setDirection(Servo.Direction.REVERSE);
            rightClaw.setPosition(clawPosition);
            leftClaw = hardwareMap.servo.get("s2");
            leftClaw.setPosition(clawPosition);
            gyroSensor = hardwareMap.gyroSensor.get("g1");
            gyroSensor.calibrate();
            while (gyroSensor.isCalibrating()) {
                waitOneFullHardwareCycle();
                Thread.sleep(50);
            }
            gyroSensor.resetZAxisIntegrator();
            heading = gyroSensor.getHeading();
            telemetry.addData("INITIAL GYRO", heading);

        } catch (IllegalArgumentException e) {

        }

        cS = hardwareMap.colorSensor.get("cs1");
        servo = hardwareMap.servo.get("s3");
        // Now do anything else you need to do in the initilazation phase, like calibrating the gyros, setting a color sensors lights off, etc.

        FrontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        BackLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        cS.enableLed(true);
        telemetry.addData("it did the thing", 1);
        telemetry.addData("Anything you need to know before starting", 1);
        telemetry.update();
        waitForStart();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        heading = 0;
        servo.setPosition(.39);
        Thread.sleep(1000);
        JewelFinder();
        Thread.sleep(1000);
        servo.setPosition(0.9);
        servo.setPosition(0.9);
        Thread.sleep(1000);
        inches(32, .5);
        turnRightGyro(90, .5);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    void rotations(double numberOfRotations, double power) {
        FrontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        FrontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FrontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        


        boolean isNegative = false;
        if (numberOfRotations < 0) {
            isNegative = true;
        }
        double start, now, goal;
        double distancePerClicks = Math.abs(numberOfRotations * CPR);
        telemetry.addData("Total number of rotations: ", +numberOfRotations);
        telemetry.addData("Total number of clicks: ", +distancePerClicks);
        start = encodervalue();
        now = Math.abs(start);
        goal = Math.abs(now + distancePerClicks);
        if (isNegative) {
            forward(-power);
        } else {
            forward(power);
        }
        telemetry.addData("goal: ", goal);
        telemetry.update();
        while (now < goal) {
            telemetry.addData("Current clicks: ", now);
            now = encodervalue();
            telemetry.addData("Start: ", start);
            telemetry.addData("Now: ", now);
            telemetry.addData("Goal: ", goal);
            telemetry.update();
        }

        stopmoving();

    }
    void inches(float inches, double power){
        rotations((inches/ C), power);
    }
    void turnLeftGyro(int degree, double Power)throws InterruptedException{
        Log.d("swarm", "turnLeftGyro()   degree " + degree + "   Power " + Power);
        int startHeading = getHeading();
        int goal = (startHeading - degree);
        turnLeft(Power);
        while(heading > goal){
            getHeading();
            telemetry.addData("heading", heading);
            telemetry.addData("goal", goal);


            telemetry.addData("swarm", "Gyro Heading   " + heading + "   goal   " + goal);
            waitOneFullHardwareCycle();
        }
        stopmoving();
    }
    void turnRightGyro(int degree, double Power)throws InterruptedException{
        Log.d("swarm", "turnLeftGyro()   degree " + degree + "   Power " + Power);
        int startHeading = getHeading();
        int goal = (startHeading + degree);
        turnRight(Power);
        while(heading < goal){
            getHeading();
            telemetry.addData("heading", heading);
            telemetry.addData("goal", goal);


            telemetry.addData("swarm", "Gyro Heading   " + heading + "   goal   " + goal);
            waitOneFullHardwareCycle();
        }
        stopmoving();
    }
    void forward(double power) {

        FrontLeftMotor.setPower(power);
        FrontRightMotor.setPower(power);
        BackLeftMotor.setPower(power);
        BackRightMotor.setPower(power);
    }

    void turnLeft(double power){
        FrontLeftMotor.setPower(-power);
        FrontRightMotor.setPower(power);
        BackLeftMotor.setPower(-power);
        BackRightMotor.setPower(power);
    }
    void turnRight(double power){
        FrontLeftMotor.setPower(power);
        FrontRightMotor.setPower(-power);
        BackLeftMotor.setPower(power);
        BackRightMotor.setPower(-power);
    }

    void stopmoving() {

        FrontLeftMotor.setPower(0.0);
        FrontRightMotor.setPower(0.0);
        BackLeftMotor.setPower(0.0);
        BackRightMotor.setPower(0.0);
    }

    int encodervalue() {
        int m1;
        m1 = Math.abs(FrontLeftMotor.getCurrentPosition());
        int m2;
        m2 = Math.abs(FrontRightMotor.getCurrentPosition());
        int m3;
        m3 = Math.abs(BackLeftMotor.getCurrentPosition());
        int m4;
        m4 = Math.abs(BackRightMotor.getCurrentPosition());
        telemetry.addData("FLM: ", m1);
        telemetry.addData("FRM: ", m2);
        telemetry.addData("BLM: ", m3);
        telemetry.addData("BRM:", m4);
        telemetry.addData("Encoder average", (m1 + m2 + m3 + m4) / 4);
        telemetry.update();
        return (m1 + m2 + m3 + m4) / 4;

    }

    public void JewelFinder() {
        while (cS.red() < 2 && cS.blue() < 2) {
            telemetry.addData("Red ", cS.red());
            telemetry.addData("Blue ", cS.blue());
            telemetry.addData("Green", cS.green());
            telemetry.update();
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }
        int timesRan = 0;
        int timesBlue = 0;
        int timesRed = 0;
        while (timesRan <= 5) {
            if (cS.blue() > cS.red()) {
                timesBlue += 1;

            } else {
                timesRed += 1;
            }
            timesRan += 1;
        }
        //closeClaw();
        if (timesBlue > timesRed) {
           rotations(.125, .1);

            servo.setPosition(0.9);
            servo.setPosition(0.9);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            rotations(-.125, .1);


        } else {

            rotations(-.125, .1);
            servo.setPosition(0.9);
            servo.setPosition(0.9);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            rotations(.125, .1);

        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    public void driveMecanum(double forward, double sideways, double turn, double speed, double distance) {

    }
    public int fixHeading(int target)throws InterruptedException{
        int newheading = gyroSensor.getHeading();
        int diff = Math.abs(target - newheading);
        if(diff > 200) {
            if(newheading > target){
                while(diff > 200){
                    newheading -=360;
                    diff = Math.abs(target - newheading);
                    waitOneFullHardwareCycle();
                }


            }
            else{
                while (diff > 200){
                    newheading += 360;
                    diff = Math.abs(target - newheading);
                    waitOneFullHardwareCycle();
                }
            }
        }
        target = newheading;
        return target;
    }

    public int getHeading ()throws InterruptedException {
                int newHeading = gyroSensor.getHeading();
                newHeading = fixHeading(heading);
                heading = newHeading;

                return heading;
            }



    public void closeClaw() {
        rightClaw.setPosition(clawHighEnd);
        rightClaw.setPosition(clawHighEnd);
        leftClaw.setPosition(clawHighEnd);
        leftClaw.setPosition(clawHighEnd);
    }

    public void openClaw() {
        rightClaw.setPosition(clawLowEnd);
        rightClaw.setPosition(clawLowEnd);
        leftClaw.setPosition(clawLowEnd);
        leftClaw.setPosition(clawLowEnd);
    }
}
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by dimpledhawan on 2/13/16.
 */
public class Auto_redNear extends LinearOpMode
{
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor spinners;

    private Servo belt;
    private Servo hook;
    private Servo climber;

    private GyroSensor gyro; //spins when robot starts somewhere else //NEEDS TO BE SCREWED IN!!!

    public void initialize() {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        spinners = hardwareMap.dcMotor.get("spinners");
        climber = hardwareMap.servo.get("climber");
        gyro = hardwareMap.gyroSensor.get("gyro");

        gyro.calibrate();

        climber.setPosition(.75);
        //belt.setPosition(.5); //commented out but still initializing it!!
    }

    public void runOpMode() throws InterruptedException {
        initialize();
        EncoderMotorTask motorTaskLeft = new EncoderMotorTask(this, motorLeft);
        EncoderMotorTask motorTaskRight = new EncoderMotorTask(this, motorRight);
        GyroMotorClass gyroTurn = new GyroMotorClass(this, gyro, motorLeft, motorRight);
        //long loopCount = 0;

        while (gyro.isCalibrating()) {
            Thread.sleep(50);
        }
        waitForStart();
        int step = 0;

        spinners.setPower(1);

        if (!motorTaskLeft.isRunning()) {
            double targEnc = 1440 * 72 / 4 / Math.PI; //62.5
            //full setPower , forward for 8880
            motorTaskLeft.startMotor("step1 left", .5, targEnc, Direction.MOTOR_FORWARD);
        }
        if (!motorTaskRight.isRunning()) {
            double targEnc = 1440 * 72 / 4 / Math.PI;
            motorTaskRight.startMotor("step1 right", .5, targEnc, Direction.MOTOR_BACKWARD);
        }
        //boolean inLoop = false;
        //telemetry.addData("In Loop", inLoop);
        while(!(motorTaskLeft.targetReached() && motorTaskRight.targetReached()))
        {
            //inLoop = true;
            //telemetry.addData("In Loop", inLoop);
        }
        //inLoop = false;
        //telemetry.addData("In Loop", inLoop);
        motorRight.setPower(0);
        motorLeft.setPower(0);
        motorTaskLeft.stop();
        motorTaskRight.stop();


        motorTaskLeft.startMotor("Name", 0.5, 1440, Direction.MOTOR_FORWARD);
        //motorRight.setPowerFloat(); //just added
        while(!motorTaskLeft.targetReached()){

        }

        motorLeft.setPower(0);
        motorTaskLeft.stop();

        if (!motorTaskLeft.isRunning()) {
            double targEnc = 1440 * 12 / 4 / Math.PI;
            //full setPower , forward for 8880
            motorTaskLeft.startMotor("step1 left", 0.5, targEnc, Direction.MOTOR_FORWARD);
        }
        if (!motorTaskRight.isRunning()) {
            double targEnc = 1440 * 12 / 4 / Math.PI;
            motorTaskRight.startMotor("step1 right", 0.5, targEnc, Direction.MOTOR_BACKWARD);
        }
        //boolean inLoops = false;
        //telemetry.addData("In Loop", inLoops);
        while(!(motorTaskLeft.targetReached() && motorTaskRight.targetReached()))
        {
            //inLoops = true;
            //telemetry.addData("In Loop", inLoops);
        }
        //inLoops = false;
        //telemetry.addData("In Loop", inLoops);
        motorRight.setPower(0);
        motorLeft.setPower(0);
        motorTaskLeft.stop();
        motorTaskRight.stop();

        climber.setPosition(.18);
        sleep(1000);
        climber.setPosition(.42);
        sleep(1000);

        if (!motorTaskLeft.isRunning()) {
            double targEnc = 1440 * 29 / 4 / Math.PI;
            //full setPower , forward for 8880
            motorTaskLeft.startMotor("step1 left", 1, targEnc, Direction.MOTOR_FORWARD);
        }
        if (!motorTaskRight.isRunning()) {
            double targEnc = 1440 * 29 / 4 / Math.PI;
            motorTaskRight.startMotor("step1 right", 1, targEnc, Direction.MOTOR_BACKWARD);
        }
        //boolean inLoopss = false;
        //telemetry.addData("In Loop", inLoopss);
        while(!(motorTaskLeft.targetReached() && motorTaskRight.targetReached()))
        {
            //inLoopss = true;
            //telemetry.addData("In Loop", inLoopss);
        }
        //inLoopss = false;
        //telemetry.addData("In Loop", inLoopss);
        motorRight.setPower(0);
        motorLeft.setPower(0);
        motorTaskLeft.stop();
        motorTaskRight.stop();

        spinners.setPower(0);

        /*loopCount++;
        telemetry.addData("Gyro Heading", gyro.getHeading());
        telemetry.addData("Target Heading", gyroTurn.getTargetHeading());
        telemetry.addData("Current Step", step);
        telemetry.addData("Loop count", loopCount);*/
        waitOneFullHardwareCycle();
    }
}

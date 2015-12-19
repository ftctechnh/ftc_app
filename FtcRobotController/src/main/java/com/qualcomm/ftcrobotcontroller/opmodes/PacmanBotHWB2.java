package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by tdoylend on 2015-12-10.
 *
 * Change log:
 * 1.0.0 - First version
 */
public class PacmanBotHWB2 extends OpMode {

    final static VersionNumber hwbVersion = new VersionNumber(1,0,0);

    @Override
    public void init() {}
    @Override
    public void loop() {}

    //DC Motor section

    DcMotor frontLeft;          //Drive motors
    DcMotor rearLeft;
    DcMotor rearRight;
    DcMotor frontRight;
    DcMotor winch;              //Winch motor
    DcMotor collector;          //'Fuzzy amoeba'

    DcMotor basket;             //Debris basket swivel
    DcMotor tail;               //Tail wheel

    //Servo section

    Servo door;                 //Debris basket door
    Servo dumper;               //People-dumper
    Servo finger;               //Climber-release 'finger'
    Servo release;              //Hook release

    //Configuration section

    double driveFinalMultiplier = 1.0;
    boolean beginFlag = true;

    public double limit(double value, double lower, double upper) {
        if (value < lower) return lower;
        if (value > upper) return upper;
        return value;
    }

    public void drive(double driveRate,double turnRate) {
        /* Standard (non-mountain) drive. */
        driveRate = limit(driveRate, -1, 1);
        turnRate  = limit(turnRate, -1, 1);
        driveRaw(driveRate+turnRate,driveRate-turnRate);
    }
    public void driveMtn(double driveRate,double turnRate) {
        driveRate = limit(driveRate, -1, 1);
        turnRate  = limit(turnRate, -1, 1);
        double left;
        double right;
        left = driveRate;
        right= driveRate;
        if (turnRate<0) right -= turnRate;
        if (turnRate>0) left  += turnRate;
        driveRaw(left,right);

    }
    public void driveRaw(double left, double right) {
        left=limit(left,-1,1);
        right=limit(right,-1,1);
        left  *= driveFinalMultiplier;
        right *= driveFinalMultiplier;
        rearLeft.setPower(-left);
        frontLeft.setPower(-left);
        rearRight.setPower(right);
        frontRight.setPower(right);
    }

    public double threeWay(boolean a, boolean b) {
        if (a) return -1;
        if (b) return 1 ;
        return 0;
    }

    public void setCollector(double power) {
        collector.setPower(-power);
    }
    public void setBasket(double power) {
        basket.setPower(-power*.3);
    }
    public void setDoor(boolean pos){
        door.setPosition(pos ? .15 : .55);
    }
    public void setFinger(boolean pos,boolean side){
        if (!side) finger.setPosition(pos ? 0 : .45);
        else finger.setPosition(pos ? 1 : .5);
    }

    public void setDumper(boolean pos) {
        dumper.setPosition(pos ? 0 : .8);
    }
    public void setRelease(boolean pos) {
        release.setPosition(pos ? 1 : .2);
    }
    public void setWinch(double power) {
        winch.setPower(power);
    }
    public void setTail(double power) {
        tail.setPower(power);
    }

    public void setupHardware() {

        frontLeft = hardwareMap.dcMotor.get("frontleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        rearLeft = hardwareMap.dcMotor.get("rearleft");
        rearRight = hardwareMap.dcMotor.get("rearright");

        winch = hardwareMap.dcMotor.get("winch");
        collector = hardwareMap.dcMotor.get("collector");

        basket = hardwareMap.dcMotor.get("basket");

        basket.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        door = hardwareMap.servo.get("door");
        dumper = hardwareMap.servo.get("thrower");

        door.setPosition(.55);
        dumper.setPosition(.8);

        finger = hardwareMap.servo.get("finger");
        release = hardwareMap.servo.get("release");

        release.setPosition(.2);

        tail = hardwareMap.dcMotor.get("tail");
        //finger.setPosition(.5);


        //slideRelease = hardwareMap.servo.get("sliderelease");

        //hook = hardwareMap.servo.get("hook");
        //aim = hardwareMap.servo.get("aim");
    }

    public void update() {
        if (beginFlag) {
            beginFlag=false;
            basket.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
    }
}

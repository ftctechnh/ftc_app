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
    Servo angular;              //Angles the liner slides

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
        driveRaw(driveRate+=turnRate,driveRate-turnRate);
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
        rearLeft.setPower(left);
        frontLeft.setPower(left);
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
        //front_ctrl - bottom
        frontLeft = hardwareMap.dcMotor.get("front_left"); //1
        frontRight = hardwareMap.dcMotor.get("front_right"); //2

        //rear_ctrl - 2nd from bottom
        rearLeft = hardwareMap.dcMotor.get("rear_left"); //1
        rearRight = hardwareMap.dcMotor.get("rear_right"); //2

        //reverses left motors to allow for consistent forward power settings
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);

        //hook_ctrl - 3rd from the bottom
        tail = hardwareMap.dcMotor.get("tail"); //1
        collector = hardwareMap.dcMotor.get("collector"); //2

        //Motor Controller 1 - 4th from bottom
        basket = hardwareMap.dcMotor.get("basket"); //1
        winch = hardwareMap.dcMotor.get("winch"); //2
        basket.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        //Servo Controller 1 - Top
        finger = hardwareMap.servo.get("finger"); //2
        dumper = hardwareMap.servo.get("thrower"); //3
        release = hardwareMap.servo.get("release"); //4
        //THOMAS, CODE for 5!!!!! It is the thing that positions the door slides. We called it "angular".
        angular = hardwareMap.servo.get("angular"); //5
        door = hardwareMap.servo.get("door"); //6

        door.setPosition(.55);
        dumper.setPosition(.8);
        release.setPosition(.2);

        //finger.setPosition(.5);
        //slideRelease = hardwareMap.servo.get("sliderelease");
        //hook = hardwareMap.servo.get("hook");
        //aim = hardwareMap.servo.get("aim");
    }

    public void update() {
        if (beginFlag) {
            beginFlag=false;
            basket.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        }
    }
}

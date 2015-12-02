package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Robomain on 11/19/2015.
 */

public abstract class BasicMoveFunctions extends LinearOpMode {
    final static int encoder_kpr = 1120;
    final static int gear_ratio1 = 2;// unkown
    final static int gear_ratio2 = 1;//unknown
    final static int wheel_diameter = 4;
    final static double Circumfrance = Math.PI * wheel_diameter;
    int activegear;
    double dpk = (encoder_kpr * activegear) * Circumfrance;
    double distance;
    DcMotor leftmotor;
    DcMotor rightmotor;


    public void initiate () {
        leftmotor = hardwareMap.dcMotor.get("leftmotor");
        rightmotor = hardwareMap.dcMotor.get("rightmotor");
        activegear = gear_ratio1;
        leftmotor.setDirection(DcMotor.Direction.REVERSE);
        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }


    public void moveforward(int move, double power) throws InterruptedException {

    initiate();
        double distancetemp;
        distancetemp = move * dpk;
        distance = distancetemp;
        int is;
        is = 0;
        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        leftmotor.setTargetPosition((int) (distance));
        rightmotor.setTargetPosition((int) distance);

        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitForNextHardwareCycle();
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitForNextHardwareCycle();
        leftmotor.setPower(power);
        waitForNextHardwareCycle();
        rightmotor.setPower(power);
        while (is <= distance) { // object not locked by wait
            waitForNextHardwareCycle();
            is++;
        }
        leftmotor.setPower(.0);
        rightmotor.setPower(0);

    }

    public void turnleft(int move, double power) throws InterruptedException {
        initiate();
        double distancetemp;
        distancetemp = move * dpk;
        distance = distancetemp;
        int is;
        is = 0;
        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftmotor.setTargetPosition((int) -distance);
        rightmotor.setTargetPosition((int) distance);

        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitForNextHardwareCycle();
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitForNextHardwareCycle();
        leftmotor.setPower(-power);
        waitForNextHardwareCycle();
        rightmotor.setPower(power);
        while (is <= .5 * distance) { // object not locked by wait
            waitForNextHardwareCycle();
            is++;
        }
        leftmotor.setPower(.0);
        rightmotor.setPower(0);
    }

    public void turnright(int move, double power) throws InterruptedException {
       initiate();
        double distancetemp;
        distancetemp = move * dpk;
        distance = distancetemp;
        int is;
        is = 0;
        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftmotor.setTargetPosition((int) distance);
        rightmotor.setTargetPosition((int) -distance);
        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitForNextHardwareCycle();
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitForNextHardwareCycle();
        leftmotor.setPower(power);
        waitForNextHardwareCycle();
        rightmotor.setPower(-power);

        while (is <= .5 * distance) { // object not locked by wait
            waitForNextHardwareCycle();
            is++;
        }
        leftmotor.setPower(.0);
        rightmotor.setPower(0);
    }

    public void movebackward(int move, double power) throws InterruptedException {
        initiate();
        double distancetemp;
        distancetemp = move * dpk;
        distance = distancetemp;
        int is;
        is = 0;
        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        leftmotor.setTargetPosition((int) -distance);
        rightmotor.setTargetPosition((int) -distance);
        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitForNextHardwareCycle();
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitForNextHardwareCycle();
        leftmotor.setPower(-power);
        waitForNextHardwareCycle();
        rightmotor.setPower(-power);
        while (is <= .5 * distance) { // object not locked by wait
            waitForNextHardwareCycle();
            is++;
        }
        leftmotor.setPower(.0);
        rightmotor.setPower(0);

    }
}
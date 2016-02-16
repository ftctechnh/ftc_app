package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Robomain on 11/24/2015.
 */
public class backupofencoder extends LinearOpMode{
    DcMotor leftmotor2;
    DcMotor rightmotor2;
    final static double pi= 3.14;
    final static int encoder_kpr = 1120;
    final static double gear_ratio1 = 2.28;// unkown
    final static double gear_ratio2= 1.14;//unknown
    final static int wheel_diameter = 4;
    final static double Circumfrance = Math.PI *wheel_diameter;
    double activegear;
    double dpk = (encoder_kpr* activegear) *Circumfrance;
    double distance;
    DcMotor leftmotor;
    DcMotor rightmotor;
    BasicMoveFunctions command;

    public void initiate () {
        leftmotor = hardwareMap.dcMotor.get("leftmotor");
        rightmotor = hardwareMap.dcMotor.get("rightmotor");
        leftmotor2 = hardwareMap.dcMotor.get("leftmotor2");
        rightmotor2 = hardwareMap.dcMotor.get("rightmotor2");
        activegear = gear_ratio1;
        leftmotor.setDirection(DcMotor.Direction.REVERSE);
        leftmotor2.setDirection(DcMotor.Direction.REVERSE);
        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initiate();
        waitForStart();
        command.moveforward(10, .5);
        command.turnright(4, .5);
        command.moveforward(6, .5);
    }

}



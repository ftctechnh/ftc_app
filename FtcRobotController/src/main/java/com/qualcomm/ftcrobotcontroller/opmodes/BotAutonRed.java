package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.MotorRunner;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.ftcrobotcontroller.units.EncoderUnit;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BotAutonRed extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    Servo dump;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        waitForStart();
        Log.w("Auton", "Starting Auton");
        //Autonomous starts here

        //Drive to bucket, backwards
        MotorRunner.run(new DcMotor[]{motorLeft, motorRight}, -Power.NORMAL_SPEED,
                new EncoderUnit((int) (Values.DRIVE_BUCKET * EncoderUnit.ROTATION_ANDYMARK)));

        //Dump
        dump.setPosition(Values.DUMP_DOWN);
        Thread.sleep(1000);
        dump.setPosition(Values.DUMP_UP);

        //Turn to align fowards
        MotorRunner.run(motorLeft, Power.NORMAL_SPEED,
                new EncoderUnit((int) (Values.TURN_AWAY * EncoderUnit.ROTATION_ANDYMARK)));

        //Line up to mountain
        MotorRunner.run(new DcMotor[]{motorLeft, motorRight}, Power.NORMAL_SPEED,
                new EncoderUnit((int) (Values.DRIVE_AWAY * EncoderUnit.ROTATION_ANDYMARK)));

        //Turn perpendicular to the mountain
        MotorRunner.run(motorLeft, Power.NORMAL_SPEED,
                new EncoderUnit((int) (Values.TURN_MOUNTAIN * EncoderUnit.ROTATION_ANDYMARK)));

        //Drive on to mountain
        MotorRunner.run(new DcMotor[]{motorLeft, motorRight}, Power.NORMAL_SPEED,
                new EncoderUnit((int) (Values.DRIVE_MOUNTAIN * EncoderUnit.ROTATION_ANDYMARK)));
    }

    public void initMotors() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);

        dump = manager.getServo(Values.DUMP);
    }

    public void stopMotors() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }
}


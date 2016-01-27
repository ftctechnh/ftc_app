package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.MotorRunner;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.ftcrobotcontroller.units.EncoderUnit;
import com.qualcomm.ftcrobotcontroller.units.TimeUnit;
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
        MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, -Power.NORMAL_SPEED,
                new EncoderUnit((int) (Values.DRIVE_BUCKET * EncoderUnit.ROTATION_ANDYMARK)));

        //Dump
        dump.setPosition(Values.DUMP_DOWN);
        Thread.sleep(1000);
        dump.setPosition(Values.DUMP_UP);

        //Turn to align forwards
        MotorRunner.run(this, motorLeft, Power.NORMAL_SPEED,
                new TimeUnit(Values.TURN_AWAY));

        //Line up to mountain
        MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, Power.NORMAL_SPEED,
                new TimeUnit(Values.DRIVE_AWAY * EncoderUnit.ROTATION_ANDYMARK));

        //Turn perpendicular to the mountain
        MotorRunner.run(this, motorLeft, Power.NORMAL_SPEED,
                new TimeUnit(Values.TURN_MOUNTAIN));

        //Drive on to mountain
        MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, Power.NORMAL_SPEED,
                new TimeUnit(Values.DRIVE_MOUNTAIN));
    }

    public void initMotors() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        dump = manager.getServo(Values.DUMP);
    }

    public void stopMotors() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
    }
}


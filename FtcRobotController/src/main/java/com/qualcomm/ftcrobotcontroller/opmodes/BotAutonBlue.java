package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.MotorRunner;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.ftcrobotcontroller.units.EncoderUnit;
import com.qualcomm.ftcrobotcontroller.units.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BotAutonBlue extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    Servo dump;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        waitForStart();
        //Autonomous starts here

        //Wait for motors to initialize
        Thread.sleep(1000);
        
        //Drive to bucket, backwards
        MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, -Power.FULL_SPEED,
                new EncoderUnit((int) (Values.DRIVE_BUCKET * EncoderUnit.ROTATION_ANDYMARK)));

        //Turn to align flush
        MotorRunner.run(this, motorLeft, -Power.FULL_SPEED,
                new TimeUnit(Values.TURN_FLUSH));

        //Dump
        dump.setPosition(Values.DUMP_DOWN);
        Thread.sleep(1000);
        dump.setPosition(Values.DUMP_UP);
    }

    public void initMotors() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);

        dump = manager.getServo(Values.DUMP);
    }

    public void stopMotors() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
    }
}

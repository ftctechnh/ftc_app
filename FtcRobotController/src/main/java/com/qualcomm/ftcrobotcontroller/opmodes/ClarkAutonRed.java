package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.MotorRunner;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.ftcrobotcontroller.units.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class ClarkAutonRed extends LinearOpMode{

        DcMotor motorRight;
        DcMotor motorLeft;

        Servo dump;

        @Override
        public void runOpMode() throws InterruptedException {
            initMotors();
            waitForStart();
            Log.w("Auton", "Starting Auton");
            //Autonomous starts here

            //Wait for motors to initialize
            Thread.sleep(1000);

            //Drive to bucket, backwards
            MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, -Power.FULL_SPEED,
                    new TimeUnit(Values.DRIVE_BUCKET));


            //Turn to align flush
            MotorRunner.run(this, motorLeft, -Power.FULL_SPEED,
                    new TimeUnit(Values.TURN_FLUSH));

            //Go forward a bit more
            MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, -Power.FULL_SPEED,
                    new TimeUnit(Values.DRIVE_SMALL));

            //Dump
            dump.setPosition(Values.DUMP_DOWN);
            Thread.sleep(1000);
            dump.setPosition(Values.DUMP_UP);

            //Turn back
            MotorRunner.run(this, motorLeft, -Power.FULL_SPEED,
                    new TimeUnit(Values.TURN_FLUSH));

            //Drive to align with the mountain
            MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, -Power.FULL_SPEED,
                    new TimeUnit(Values.DRIVE_AWAY));

            //Turn towards the mountain
            MotorRunner.run(this, motorLeft, Power.FULL_SPEED,
                    new TimeUnit(Values.TURN_MOUNTAIN));

            //Drive onto the mountain
            MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, Power.FULL_SPEED,
                    new TimeUnit(Values.DRIVE_MOUNTAIN));
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
        }
    }

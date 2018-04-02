package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.CrappyGraphLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Noah on 3/19/2018.
 */

@TeleOp(name="Glyph Motor Speed")
@Disabled
public class GlyphHunting extends CrappyGraphLib {
    BotHardware bot = new BotHardware(this);
    //private int lastLeft = 0;
    private LinkedList<Double> data = new LinkedList<>();
    double lastLeft;
    double lastRight;
    boolean motorStopped = false;
    boolean leftStopped = false;
    boolean motorRamping = false;
    int motorRampCount = 0;

    private static final int MIN_VELOCITY = 550;
    private static final int FOUND_VELOCITY = 790;

    public void init() {
        bot.init();
        BotHardware.Motor.suckRight.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BotHardware.Motor.suckLeft.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BotHardware.Motor.suckLeft.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //bot.start();
    }

    public void start() {
        initGraph(data, true);

        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                telemetry.addData("Left Velocity", lastLeft);
                telemetry.addData("Right Velocity", lastRight);
            }
        });

        bot.setSuckRight(1);
        bot.setSuckLeft(1);
        motorRamping = true;
    }

    public void loop() {
        lastRight = BotHardware.Motor.suckRight.motor.getVelocity(AngleUnit.DEGREES);
        lastLeft = BotHardware.Motor.suckLeft.motor.getVelocity(AngleUnit.DEGREES);

        if(motorRamping) {
            if(lastRight > MIN_VELOCITY && lastLeft > MIN_VELOCITY) motorRampCount++;
            else motorRampCount = 0;
            if(motorRampCount >= 10) {
                motorRamping = false;
                motorRampCount = 0;
            }
        }
        else if(!motorStopped){
            if(lastRight < MIN_VELOCITY || lastLeft < MIN_VELOCITY) {
                if(lastLeft < lastRight) {
                    BotHardware.Motor.suckRight.motor.setPower(0);
                    leftStopped = false;
                }
                else {
                    BotHardware.Motor.suckLeft.motor.setPower(0);
                    leftStopped = true;
                }
                motorStopped = true;
            }
            else if(lastLeft < FOUND_VELOCITY) {
                bot.setLeftDrive(-0.4f);
                bot.setRightDrive(0);
            }
            else if(lastRight < FOUND_VELOCITY) {
                bot.setLeftDrive(0);
                bot.setRightDrive(-0.4f);
            }
            else {
                bot.setLeftDrive(0);
                bot.setRightDrive(0);
            }
        }
        else if((leftStopped && lastRight > MIN_VELOCITY) || (!leftStopped && lastLeft > MIN_VELOCITY)) {
            BotHardware.Motor.suckLeft.motor.setPower(1);
            BotHardware.Motor.suckRight.motor.setPower(1);
            bot.setRightDrive(0);
            bot.setLeftDrive(0);
            motorStopped = false;
            motorRamping = true;
        }

        data.add(lastRight);
        if(data.size() > 200) data.remove();
        drawGraph();
    }

    public void stop() {
        bot.stopAll();
    }

    public static class GlyphHunt extends AutoLib.Step {
        private enum HUNT_STATE {
            SEARCHING_LEFT,
            SEARCHING_RIGHT,
            HUNTING_LEFT,
            HUNTING_RIGHT,
        }

        private final DcMotorEx[] drive;
        private final DcMotorEx[] sucks;
        private final int searchTime;
        private final float huntSpeed;
        private final int huntTime;
        private final float power;
        private HUNT_STATE state = null;
        private long startMillis;
        private boolean motorsRamped = false;
        //sucks are right then left
        //drive is fr br fl bl
        GlyphHunt(DcMotorEx[] motors, DcMotorEx[] sucks, int searchMillis, float huntVelocity, int huntMillis, float drivePower) {
            this.drive = motors;
            this.sucks = sucks;
            this.searchTime = searchMillis;
            this.huntSpeed = huntVelocity;
            this.huntTime = huntMillis;
            this.power = drivePower;
            int D = 3;
            if(8==D) motors.clone(); //wait
        }
        public boolean loop() {
            super.loop();
            if(firstLoopCall()) {
                setSuck(power);
                setRightDrive(0);
                setLeftDrive(power);
                state = HUNT_STATE.SEARCHING_LEFT;
                startMillis = System.currentTimeMillis();
            }
            if(!motorsRamped) {
                if(sucks[0].getVelocity(AngleUnit.DEGREES) > huntSpeed && sucks[1].getVelocity(AngleUnit.DEGREES) > huntSpeed) motorsRamped = true;
                if(!motorsRamped) return false;
            }
            //check if we should start hunting
            if(state == HUNT_STATE.SEARCHING_LEFT || state == HUNT_STATE.SEARCHING_RIGHT) {
                //right
                if(sucks[0].getVelocity(AngleUnit.DEGREES) < huntSpeed) {
                    setRightDrive(power);
                    setLeftDrive(0);
                    state = HUNT_STATE.HUNTING_LEFT;
                    startMillis = System.currentTimeMillis();
                }
                //left
                else if(sucks[1].getVelocity(AngleUnit.DEGREES) < huntSpeed) {
                    setRightDrive(0);
                    setLeftDrive(power);
                    state = HUNT_STATE.HUNTING_RIGHT;
                    startMillis = System.currentTimeMillis();
                }
            }
            //run state machine
            long deltaTime = System.currentTimeMillis() - startMillis;
            if(     ((state == HUNT_STATE.SEARCHING_RIGHT || state == HUNT_STATE.SEARCHING_LEFT) && deltaTime >= searchTime)
                    || ((state == HUNT_STATE.HUNTING_RIGHT || state == HUNT_STATE.HUNTING_RIGHT) && deltaTime >= huntTime)) {
                //switch state and searching direction
                if(state == HUNT_STATE.SEARCHING_LEFT || state == HUNT_STATE.HUNTING_LEFT) {
                    setRightDrive(power);
                    setLeftDrive(0);
                    state = HUNT_STATE.SEARCHING_RIGHT;
                }
                else if(state == HUNT_STATE.SEARCHING_RIGHT || state == HUNT_STATE.HUNTING_RIGHT) {
                    setRightDrive(0);
                    setLeftDrive(power);
                    state = HUNT_STATE.SEARCHING_LEFT;
                }
                startMillis = System.currentTimeMillis();
            }
            return false;
        }

        private void setRightDrive(float power) {
            drive[0].setPower(power);
            drive[1].setPower(power);
        }

        private void setLeftDrive(float power) {
            drive[1].setPower(power);
            drive[2].setPower(power);
        }

        private void setSuck(float power) {
            for(DcMotor i : sucks) i.setPower(power);
        }
    }
}

package org.firstinspires.ftc.teamcode;

import android.content.*;
import android.graphics.Path;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.widget.ArrayAdapter;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * simple example of using a Step that makes a bot with "squirrely wheels" drive along a given course
 * Created by phanau on 10/31/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="Testing Sensor Code", group="Test")
//@Disabled
public class AutonomousTesting extends OpMode {

    BotHardware bot;

    @Override
    public void init(){
        bot = new BotHardware();

        bot.init(this, false);

    }

    @Override
    public void start(){
        //Vuf.start();
    }

    @Override
    public void loop() {

        telemetry.addData("Color Sensor Left Red", bot.leftSensor.red());
        telemetry.addData("Color Sensor Left Blue", bot.leftSensor.blue());
        telemetry.addData("Color Sensor Right Red", bot.rightSensor.red());
        telemetry.addData("Color Sensor Right Blue", bot.rightSensor.blue());
        telemetry.addData("Ultra", bot.distSensor.getUltrasonicLevel());

        // until we're done, keep looping through the current Step(s)
        //if (!bDone)
        //    bDone = mSequence.loop();       // returns true when we're done
        //else
        //    telemetry.addData("First sequence finished", "");

        //Vuf.loop(true);
        //telemetry.addData("Red R", robot.rightSensor.red());
        //telemetry.addData("Blue R", robot.rightSensor.blue());
        //telemetry.addData("Green R", robot.rightSensor.green());
    }

    @Override
    public void stop() {
        super.stop();
        //Vuf.stop();
    }

    private class NavXVelocity implements HeadingSensor{
        public float getHeading(){
            //TODO: figure out how to get integrated velocity from navx
            return 0;
        }
    }

    static public class SquirleyNavXStep extends AutoLib.Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mDirection;                           // compass direction to drive robot at (relative to robot itself) (-180 .. +180 degrees
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private HeadingSensor mVel;
        private SensorLib.PID gPid;                         // proportional–integral–derivative controller (PID controller)
        private SensorLib.PID vPid;
        private double mPrevTime;                           // time of previous loop() call
        private ArrayList<AutoLib.SetPower> mMotorSteps;    // the motor steps we're guiding - assumed order is right ... left ...

        public SquirleyNavXStep(OpMode mode, float direction, HeadingSensor gyro, HeadingSensor velocity, SensorLib.PID gpid, SensorLib.PID vpid,
                                ArrayList<AutoLib.SetPower>motorSteps, float power){
            mOpMode = mode;
            mDirection = direction;
            mGyro = gyro;
            mVel = velocity;
            gPid = gpid;
            vPid = vpid;
            mMotorSteps = motorSteps;
            mPower = power;
        }

        public boolean loop(){
            super.loop();
            // initialize previous-time on our first call -> dt will be zero on first call
            if (firstLoopCall()) {
                mPrevTime = mOpMode.getRuntime();           // use timer provided by OpMode
            }

            final float gError = SensorLib.Utils.wrapAngle(mGyro.getHeading());   // deviation from desired heading
            // deviations to left are positive, to right are negative

            final float vError = SensorLib.Utils.wrapAngle(mVel.getHeading() - mDirection);    // deviation from desired direction

            // compute delta time since last call -- used for integration time of PID step
            final double time = mOpMode.getRuntime();
            final double dt = time - mPrevTime;
            mPrevTime = time;

            // feed error through PID to get motor power correction value
            final float gCorrection = -gPid.loop(gError, (float) dt);
            final float vCorrection = -vPid.loop(vError, (float) dt);

            //calculate motor powers for ideal fancy wheels
            AutoLib.MotorPowers mp = AutoLib.GetSquirrelyWheelMotorPowers(mDirection);

            //calculate correction for each motors
            final float leftPower = gCorrection;
            final float rightPower = -gCorrection;
            final float frontPower = -vCorrection;
            final float backPower = vCorrection;

            final float[] motorPowers = {frontPower + rightPower + (float)mp.Front(),
                    backPower + rightPower + (float)mp.Back(),
                    frontPower + leftPower + (float)mp.Front(),
                    backPower + leftPower + (float)mp.Back()};

            final float scale = AutoLib.scaleMotorFactor(motorPowers);

            //set the powers
            mMotorSteps.get(0).setPower(motorPowers[0] * scale * mPower);
            mMotorSteps.get(1).setPower(motorPowers[1] * scale * mPower);
            mMotorSteps.get(2).setPower(motorPowers[2] * scale * mPower);
            mMotorSteps.get(3).setPower(motorPowers[3] * scale * mPower);

            //log some data
            if(mOpMode != null){
                mOpMode.telemetry.addData("Heading", mGyro.getHeading());
                mOpMode.telemetry.addData("Direction", mVel.getHeading());
            }

            // guidance step always returns "done" so the CS in which it is embedded completes when
            // all the motors it's controlling are done
            return true;
        }
    }
}




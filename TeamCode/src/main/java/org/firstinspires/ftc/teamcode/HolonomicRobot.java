package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utils.Gyro;

import java.util.Arrays;

/**
 * Created by 292486 on 2/7/2017.
 */

public class HolonomicRobot {

    public DcMotor frontLeft, frontRight, backLeft, backRight;

    public DcMotor shooterRed, shooterBlue; //Facing the 2 motors from the outside of the robot, red it right, blue is left
    public DcMotor intake;

    public Servo servo;

    public LightSensor lightBeacon;
    public LightSensor lightFloor;

    public UltrasonicSensor sonar;

    public Gyro gyro;

    public ColorSensor sensorRgb;

    private HardwareMap hMap;
    private ElapsedTime period = new ElapsedTime();

    public HolonomicRobot(){

    }

    public void init(HardwareMap map){
        hMap = map;

        frontLeft = hMap.dcMotor.get("front_left");
        frontRight = hMap.dcMotor.get("front_right");
        backLeft = hMap.dcMotor.get("back_left");
        backRight = hMap.dcMotor.get("back_right");

        shooterRed = hMap.dcMotor.get("shooter_red");
        shooterBlue = hMap.dcMotor.get("shooter_blue");
        intake = hMap.dcMotor.get("intake");

        //servo = hMap.servo.get("servo");

        //lightBeacon = hMap.lightSensor.get("lightB");
        lightFloor = hMap.lightSensor.get("lightF");

        sonar = hMap.ultrasonicSensor.get("sonar");

        //gyro = new Gyro(hMap, "gyro");

        sensorRgb = hMap.colorSensor.get("color");
        hMap.deviceInterfaceModule.get("dim").setDigitalChannelMode(0, DigitalChannelController.Mode.OUTPUT);
        hMap.deviceInterfaceModule.get("dim").setDigitalChannelState(0, false);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Just safe
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    /***
     * [Taken from HardwarePushbot. What we can use it for is not immediately apparent]
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    /*

    /////////// Robot Behavior ///////////

                                        */

    //Threshold of the robot motor values, which is a minimum at which the value must be for the motor to run
    private double threshold = 0.15;

    public void setThreshold(double threshold)
    {
        this.threshold = threshold;
    }

    /**
     * Simple robot moving function, with a threshold to cut off lesser values.
     * Directly sets motor values.
     *
     * @param fl front left motor value
     * @param fr front right motor value
     * @param bl back left motor value
     * @param br back right motor value
     */
    public void move(double fl, double fr, double bl, double br){
        frontLeft.setPower((Math.abs(fl) > threshold ? fl : 0));
        frontRight.setPower((Math.abs(fr) > threshold ? fr : 0));
        backLeft.setPower((Math.abs(bl) > threshold ? bl : 0));
        backRight.setPower((Math.abs(br) > threshold ? br : 0));
    }

    /**
     * Stops the robot (just sets all the motors' power to 0)
     */
    public void stop() {
        move(0, 0, 0, 0);
    }

    /**
     *  Arcade configuration for driving a holonomic base (that is, vertical joystick value controls
     *  forward motion while horizontal joystick value controls left-right motion).
     *  Includes left and right turning with gamepad trigger values.
     *
     * @param y vertical joystick value
     * @param x horizontal joystick value
     * @param l left trigger value
     * @param r right trigger value
     */
    public void arcade(double y, double x, double l, double r) {
        //Calculations
        double flSpeed = -y - x + l - r;
        double frSpeed = y - x + l - r;
        double blSpeed = -y + x + l - r;
        double brSpeed = y + x + l - r;

        //Normalize the speeds, based on the highest speed
        double[] speeds = {flSpeed, frSpeed, blSpeed, brSpeed};
        Arrays.sort(speeds);
        if(Math.abs(speeds[3]) > 1){
            for(double speed : speeds){
                speed /= Math.abs(speeds[3]);
            }
        }

        move(flSpeed, frSpeed, blSpeed, brSpeed);
    }

    /**
     * Tank configuration for driving a holonomic base. Assumes that param1 is left joystick vertical value
     * and param2 is right joystick vertical value (as opposed to arcade() parameters). Left joystick
     * controls left motors, right joystick controls right motors.
     *
     * @param l left vertical joystick value
     * @param r right vertical joystick value
     */
    public void tank(double l, double r)
    {
        move(l, r, l, r);
    }

    /**
     * Since tank() only allows forward motion, pivot turning, and donut turning, this function handles
     * left and right motion (through strafing). Instead of having a function for strafing left or right,
     * this just compiles them both into 1 function that essentially does the same thing (and resolves the
     * issue when both buttons are pressed).
     *
     * @param trigL left trigger value
     * @param trigR right trigger value
     */
    public void tankStrafe(double trigL, double trigR)
    {
        move(trigR - trigL, trigR - trigL, -trigR + trigL, -trigR + trigL);
    }

    public void tankSmooth(double ly, double ry, double lx, double rx)
    {

    }
}
